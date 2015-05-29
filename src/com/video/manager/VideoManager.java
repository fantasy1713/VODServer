package com.video.manager;

import java.io.File;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.format.json.ErrorInfoFactory;
import com.format.json.JsonResponseInterface;
import com.format.json.MediaResponseJson;
import com.format.json.MediaResponseOrderJson;
import com.format.json.MediaResponseSegmentJson;
import com.format.json.MediaResponseViewJson;
import com.global.defines.MediaFileStatusDefine;
import com.global.defines.ServerSettings;
import com.media.hash.MediaIdGenerator;
import com.media.pipeline.MediaPipelineBuilder;
import com.media.pipeline.UploadPipelineBuilder;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Mediafileinfo;
import com.vodserver.hibernate.beans.Mediafileprototype;
import com.vodserver.hibernate.beans.Vodmedia;
import com.vodserver.hibernate.beans.Vodmediaparams;
import com.vodserver.hibernate.beans.VodmediaparamsId;

public class VideoManager {
	// 查找已经存在的点播请求
	public String findMediaExists(List<WeblibUrlInfo> urllist) {
		Session sen = HibernateSessionFactory.getSession();
		try {
			StringBuilder sb = new StringBuilder(500);
			sb.append("select t2.urlstring from vodmediaparams t1 ");
			sb.append("inner join vodmedia t2 on t1.mediaid = t2.id ");
			sb.append("right join (select mediaid from vodmediaparams ");
			sb.append("where ");
			for (int i = 0; i < urllist.size(); i++) {
				if (i != 0)
					sb.append("or ");
				sb.append("(weblibhost='" + urllist.get(i).url
						+ "' and weblibid=" + urllist.get(i).id
						+ " and viewindex=" + urllist.get(i).viewindex + ") ");
			}
			sb.append("group by mediaid having count(1)=");
			sb.append(urllist.size());
			sb.append(") t3 on t1.mediaid=t3.mediaid group by t2.urlstring ");
			sb.append("having count(1)=");
			sb.append(urllist.size());

			Query query = sen.createSQLQuery(sb.toString());
			List list = query.list();
			if (list == null || list.size() == 0)
				return null;
			else
				return list.get(0).toString();
		} catch (Exception ex) {
			return null;
		} finally {
			sen.disconnect();
		}
	}

	// 根据URL信息组合成新的媒体记录，并返回媒体链接id
	public String createMediaQuery(List<WeblibUrlInfo> urllist) {
		if (urllist == null || urllist.size() == 0)
			return null;

		Session sen = HibernateSessionFactory.getSession();
		Transaction tran = sen.beginTransaction();
		try {
			// 获取已有文件hash值
			StringBuilder sb = new StringBuilder(200);
			sb.append("select DISTINCT(srcfileid), weblibhost, weblibid from vodmediaparams where ");
			for (int i = 0; i < urllist.size(); i++) {
				if (i != 0)
					sb.append("or ");
				sb.append("(weblibhost='" + urllist.get(i).url
						+ "' and weblibid=" + urllist.get(i).id + ") ");
			}
			Query query = sen.createSQLQuery(sb.toString());
			List list = query.list();
			List<ExistCacheState> extlist = new ArrayList<ExistCacheState>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] itemquery = (Object[]) list.get(i);
					ExistCacheState ste = new ExistCacheState();
					ste.prototypeid = (Integer) itemquery[0];
					ste.weblibhost = itemquery[1].toString();
					ste.weblibid = (Integer) itemquery[2];
					extlist.add(ste);
				}
			}

			List<WeblibUrlInfo> lacklist = new ArrayList<WeblibUrlInfo>();
			boolean found = false;
			for (WeblibUrlInfo info : urllist) {
				found = false;
				for (ExistCacheState item : extlist) {
					if (item.weblibhost.equalsIgnoreCase(info.url)
							&& item.weblibid == info.id) {
						Query extquery = sen
								.createQuery("from Mediafileprototype where id="
										+ item.prototypeid);
						info.prototype = (Mediafileprototype) extquery
								.uniqueResult();
						found = true;
						break;
					}
				}
				if (found == false)
					lacklist.add(info);
			}

			// 创建本地缺少资源的记录
			if (lacklist.size() > 0) {
				for (WeblibUrlInfo info : lacklist) {
					Mediafileprototype pty = new Mediafileprototype();
					pty.setCreatetime(new Timestamp(new Date().getTime()));
					pty.setSrcweblibhost(info.url);
					pty.setSrcweblibid(info.id);
					sen.save(pty);
					info.prototype = pty;
				}
			}

			String vodid = null;
			BigInteger count;
			do {
				vodid = MediaIdGenerator.generateMediaId();
				String idquery = "select count(1) from vodmedia where urlstring='"
						+ vodid + "'";
				count = (BigInteger) sen.createSQLQuery(idquery).uniqueResult();
			} while (count.intValue() > 0);

			List<Integer> viewlist = new ArrayList<Integer>();
			for (WeblibUrlInfo info : urllist) {
				if (!viewlist.contains(info.viewindex))
					viewlist.add(info.viewindex);
			}

			// 创建媒体记录信息
			Vodmedia media = new Vodmedia(null, vodid, viewlist.size(), null,
					null, "preparing", null, null, new Timestamp(
							new Date().getTime()), 0, null, 0, null, null,
					new Timestamp(new Date().getTime()), 0, null);
			sen.save(media);

			for (WeblibUrlInfo info : urllist) {
				if (info.prototype == null) {
					throw new Exception("Error: null prototype file found");
				}

				VodmediaparamsId paramid = new VodmediaparamsId();
				paramid.setOrderindex(info.orderindex);
				paramid.setViewindex(info.viewindex);
				paramid.setVodmedia(media);
				Vodmediaparams param = new Vodmediaparams(paramid,
						info.prototype, info.url, info.id, null,
						info.orignalurl, info.startoffset);
				sen.save(param);
			}

			tran.commit();
			return media.getUrlstring();
		} catch (Exception ex) {
			tran.rollback();
			ex.printStackTrace();
			return null;
		} finally {
			sen.disconnect();
		}
	}

	public void updateMediaContent(String mediastring) {
		if (mediastring == null || mediastring.length() == 0)
			return;

		String str = "select t1.id as mid, t2.orderindex, t2.viewindex, t2.weblibhost, t2.weblibid, t3.id, t4.status from vodmedia t1 "
				+ "inner join vodmediaparams t2 on t1.id=t2.mediaid "
				+ "inner join mediafileprototype t3 on t2.srcfileid=t3.id "
				+ "left join mediafileinfo t4 on t3.id=t4.srcfileid "
				+ "where t1.urlstring='"
				+ mediastring
				+ "' order by t2.orderindex";
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createSQLQuery(str);
		List list = query.list();
		List<MediaFileState> filelist = new ArrayList<MediaFileState>();
		int mediaid = -1;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] itemquery = (Object[]) list.get(i);
				MediaFileState ste = new MediaFileState();
				mediaid = (Integer) itemquery[0];
				ste.orderindex = (Integer) itemquery[1];
				ste.viewindex = (Integer) itemquery[2];
				ste.weblibhost = (String) itemquery[3];
				ste.weblibid = (Integer) itemquery[4];
				ste.prototypeid = (Integer) itemquery[5];
				ste.status = (String) itemquery[6];
				filelist.add(ste);
			}
		}

		for (MediaFileState fileinfo : filelist) {
			if (fileinfo.status == null
					|| fileinfo.status.equals(MediaFileStatusDefine.DELETED)
					|| fileinfo.status.equals(MediaFileStatusDefine.ERROR)) {
				boolean result = MediaPipelineBuilder.addNewQuest(
						fileinfo.weblibhost, fileinfo.weblibid,
						fileinfo.prototypeid, ServerSettings.getDownloadPath(),
						java.util.UUID.randomUUID().toString(), mediaid);
				if (result) {
					System.out.println("add task success:" + fileinfo.weblibid);
				} else {
					System.out.println("task add failed:" + fileinfo.weblibid);
				}
			}
		}

		sen.disconnect();
	}

	public void refreshMediaStatus(int mediaid, String status) {
		Session sen = HibernateSessionFactory.getSession();
		Transaction tran = sen.beginTransaction();
		Query query = sen
				.createQuery("Update Vodmedia set status=:status where id=:id");
		query.setString("status", status);
		query.setInteger("id", mediaid);
		query.executeUpdate();
		tran.commit();
	}

	public JsonResponseInterface getMediaContents(String mediaid) {
		if (mediaid == null || mediaid.length() == 0)
			return null;

		MediaResponseJson response = new MediaResponseJson();

		Session sen = HibernateSessionFactory.getSession();
		Query query1 = sen
				.createQuery("from Vodmedia where urlstring=:urlstring");
		query1.setString("urlstring", mediaid);
		Vodmedia media = (Vodmedia) query1.uniqueResult();
		if (media == null) {
			return ErrorInfoFactory.requestError("无id为" + mediaid + "的媒体记录");
		}

		response.setId(media.getId());
		response.setViewnum(media.getViews());
		response.setIdstring(media.getUrlstring());
		response.setDurations(media.getDurations());
		response.setOwner(media.getOwner());
		response.setStatus(media.getStatus());
		response.setTitle(media.getTitle());

		Query query2 = sen
				.createSQLQuery("select t2.id, t1.viewindex, t1.orderindex, t2.filetype,"
						+ " t2.status, t2.bitrate, t2.framerate, t2.durations, t2.size, t2.width, t2.height,"
						+ " t2.videocodes, t2.audiocodes from vodmediaparams t1,"
						+ " mediafileinfo t2 where t1.srcfileid=t2.srcfileid and t1.mediaid="
						+ media.getId()
						+ " order by t1.viewindex, t1.orderindex");
		List list = query2.list();
		MediaResponseViewJson viewinfo = null;
		MediaResponseOrderJson orderinfo = null;
		List<MediaResponseViewJson> viewlist = new ArrayList<MediaResponseViewJson>();
		response.setViews(viewlist);
		for (int i = 0; i < list.size(); i++) {
			Object[] itemquery = (Object[]) list.get(i);
			int viewindex = (Integer) itemquery[1];
			if (viewinfo == null || viewinfo.getViewindex() != viewindex) {
				viewinfo = new MediaResponseViewJson();
				viewinfo.setViewindex(viewindex);
				viewinfo.setOrder(new ArrayList<MediaResponseOrderJson>());
				viewlist.add(viewinfo);
				orderinfo = null;
			}

			int orderindex = (Integer) itemquery[2];
			if (orderinfo == null || orderinfo.getOrderindex() != orderindex) {
				orderinfo = new MediaResponseOrderJson();
				orderinfo.setOrderindex(orderindex);
				orderinfo
						.setSegments(new ArrayList<MediaResponseSegmentJson>());
				viewinfo.getOrder().add(orderinfo);
			}

			MediaResponseSegmentJson seginfo = new MediaResponseSegmentJson();
			seginfo.setId((Integer) itemquery[0]);
			seginfo.setBitrate((String) itemquery[5]);
			seginfo.setDurations((Float) itemquery[7]);
			seginfo.setFramerate((Float) itemquery[6]);
			seginfo.setWidth(itemquery[9] != null ? (Integer) itemquery[9] : 0);
			seginfo.setHeight(itemquery[10] != null ? (Integer) itemquery[10]
					: 0);
			seginfo.setVideocodes((String) itemquery[11]);
			seginfo.setAudiocodes((String) itemquery[12]);
			if (itemquery[8] == null)
				seginfo.setSize(null);
			else
				seginfo.setSize(((BigInteger) itemquery[8]).longValue());
			seginfo.setStatus((String) itemquery[4]);
			seginfo.setType((String) itemquery[3]);
			orderinfo.getSegments().add(seginfo);
		}

		return response;
	}

	/**
	 * @author zhangfan 数据库查询
	 * @return
	 */
	public List getExistCache() {
		Session sess = HibernateSessionFactory.getSession();
		String hql = "from Mediafileinfo as m where m.isdeleted !=:isdeleted and m.filetype='mpeg-dash' and m.status='ready' and m.size>0";
		Query query = sess.createQuery(hql);
		query.setBoolean("isdeleted", true);
		List list = query.list();
		return list;
	}

	class ExistCacheState {
		public int prototypeid;
		public String weblibhost;
		public int weblibid;
	}

	class MediaFileState {
		public int orderindex;
		public int viewindex;
		public String weblibhost;
		public int weblibid;
		public int prototypeid;
		public String status;
	}

	/**
	 * @author zhangfan 根据缓存视频的id值获得点播特征码
	 * @param id
	 *            缓存id
	 * @return 特征码
	 */
	public String getAttrCode(String id) {
		Session sess = HibernateSessionFactory.getSession();
		Mediafileinfo mf = (Mediafileinfo) sess.get(Mediafileinfo.class,
				Integer.parseInt(id));
		if (mf != null && mf.getMediafileprototype() != null) {
			// 存在对应云记录
			Integer srcfileid = mf.getMediafileprototype().getId();
			String hql = "select v.id.vodmedia.urlstring from Vodmediaparams as v where v.mediafileprototype.id=:srcfileid";
			Query query = sess.createQuery(hql);
			query.setInteger("srcfileid", srcfileid);
			List list = query.list();
			sess.close();
			if (list == null || list.size() < 1) {
				return null;
			} else
				return (String) list.get(0);
		} else {
			// 云记录不存在的情况
			return null;
		}

	}

	/**
	 * @author zhangfan 创建视频媒体上传过程处理
	 * @param file
	 * @param contentType
	 * @param fileName
	 */
	public boolean createMediaUpload(File file, String contentType,
			String fileName) {
		Session sess = HibernateSessionFactory.getSession();
		Transaction tran = sess.beginTransaction();
		// 查找是否已经纯在同名文件
		Query query1 = sess
				.createQuery("from Localmediafileprototype as t where t.filename=:fileName");
		query1.setString("filename", fileName);
		List list = query1.list();
		if (list != null && list.size() > 0)// 存在同名文件
		{
			return false;
		}
		// 通过流水线进行视频媒体上传
		boolean result = UploadPipelineBuilder.addUploadTask(file, contentType,
				fileName, ServerSettings.getUploadPath(), java.util.UUID
						.randomUUID().toString());
		if(!result){
			return result;
		}
		// 在数据库中创建相应的媒体记录
		return true;
	}
}
