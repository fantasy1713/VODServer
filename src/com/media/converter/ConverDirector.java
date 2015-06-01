package com.media.converter;

import java.io.File;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.global.defines.MediaFileStatusDefine;
import com.media.pipeline.MediaPipelineTaskClass;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Localmediafileprototype;
import com.vodserver.hibernate.beans.Mediafileinfo;
import com.vodserver.hibernate.beans.Mediafileprototype;

public class ConverDirector {
	/*
	 * public static void main(String []args){ ConverDirector dir = new
	 * ConverDirector(); // for(int i=0; i<10; i++) // dir.test("6.ts", "d:\\3",
	 * counter++ + ".mp4", "d:\\3\\2", i, true);
	 * 
	 * // counter = 100; // for(int i=0; i<10; i++) // dir.test("6.ts", "d:\\3",
	 * counter++ + ".mp4", "d:\\3\\1", i, false); }
	 * 
	 * public void test2(String filename, String filepath, String targetname,
	 * String targetpath, int plv, Boolean twopass){ File test = new
	 * File("d:\\time\\" + filename + "_" + plv + "_" + twopass.toString() +
	 * ".txt"); FileWriter fw = null; long start = System.currentTimeMillis();
	 * try { fw = new FileWriter(test, true); fw.write(filename + "_" + plv);
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * fw.write("\r\n" + format.format(new Date())); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * DashConverterFactory dashfact = new DashConverterFactory(); BaseConverter
	 * dashcon = (DashConverter) dashfact.createConverter(targetpath,
	 * targetname, targetpath + "/dash/" + targetname, targetname + "." +
	 * dashfact.getFileSuffix());
	 * 
	 * dashcon.setM_presetlv(plv); ((DashConverter)
	 * dashcon).useTwoPass(twopass); dashcon.startConver();
	 * 
	 * try { fw.write("\r\ndashfinished at:" +
	 * String.valueOf(System.currentTimeMillis() - start)); fw.flush();
	 * fw.close(); } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * public void test(String filename, String filepath, String targetname,
	 * String targetpath, int plv, Boolean twopass){ File test = new
	 * File("d:\\time\\" + filename + "_" + plv + "_" + twopass.toString() +
	 * ".txt"); FileWriter fw = null; long start = System.currentTimeMillis();
	 * try { fw = new FileWriter(test, true); fw.write(filename + "_" + plv +
	 * " start at "); SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); fw.write(format.format(new
	 * Date())); } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * Mp4ConverterFactory mp4fact = new Mp4ConverterFactory(); BaseConverter
	 * mp4con = (Mp4Converter) mp4fact.createConverter(filepath, filename,
	 * targetpath, targetname); mp4con.setM_presetlv(plv);
	 * //将媒体信息对象作为参数传入方法，并通过返回的对象来更新自己 mp4con.startConver();
	 * 
	 * try { fw.write("\r\nmp4finished at:" +
	 * String.valueOf(System.currentTimeMillis() - start)); SimpleDateFormat
	 * format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); fw.write("\r\n" +
	 * format.format(new Date())); start = System.currentTimeMillis(); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * DashConverterFactory dashfact = new DashConverterFactory(); BaseConverter
	 * dashcon = (DashConverter) dashfact.createConverter(targetpath,
	 * targetname, targetpath + "/dash/" + targetname, targetname + "." +
	 * dashfact.getFileSuffix());
	 * 
	 * //TODO: 由于dash是通过对mp4分片得到，因此部分参数可以直接读取得到，若要准确获得转码后参数，可以分析mpd文件获得
	 * dashcon.setM_width(mp4con.getM_width());
	 * dashcon.setM_height(mp4con.getM_height());
	 * dashcon.setM_durations(mp4con.getM_durations());
	 * dashcon.setM_ratio(mp4con.getM_ratio());
	 * dashcon.setM_framerate(mp4con.getM_framerate());
	 * dashcon.setM_videocodes(mp4con.getM_videocodes());
	 * dashcon.setM_audiocodes(mp4con.getM_audiocodes());
	 * dashcon.setM_presetlv(plv);
	 * 
	 * ((DashConverter) dashcon).useTwoPass(twopass);
	 * 
	 * dashcon.startConver();
	 * 
	 * try { fw.write("\r\ndashfinished at:" +
	 * String.valueOf(System.currentTimeMillis() - start)); SimpleDateFormat
	 * format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); fw.write("\r\n" +
	 * format.format(new Date())); fw.flush(); fw.close(); } catch (IOException
	 * e) { e.printStackTrace(); } }
	 */

	// TODO: 由于后来替换了编码工具，导致修改代码后的设计模式比较混乱，以后再对这部分进行重构
	public void startConverter(MediaPipelineTaskClass task,
			List<Mediafileinfo> list) {
		Session sen = HibernateSessionFactory.getSession();
		Query query;
		Mediafileprototype proinfo =null;
		Localmediafileprototype lproinfo = null;
		if (task.getIslocalupload()) {// 如果是本地上传文件
			query = sen
					.createQuery("from Localmediafileprototype where id=:id")
					.setInteger("id", task.getPrototypeid());
			lproinfo = (Localmediafileprototype) query
					.uniqueResult();
			
		} else {
			query = sen.createQuery("from Mediafileprototype where id=:id")
					.setInteger("id", task.getPrototypeid());
			proinfo = (Mediafileprototype) query
					.uniqueResult();
			
		}

		Mp4ConverterFactory mp4fact = new Mp4ConverterFactory();
		BaseConverter mp4con = (Mp4Converter) mp4fact.createConverter(
				task.getFilepath(), task.getFilename(),
				mp4fact.getFileStorePath(),
				task.getFilename() + "." + mp4fact.getFileSuffix(),task.getRealfilename());
		Mediafileinfo mp4info = this.getMediaInfoByType(mp4fact.getFileType(),
				list);
		// 将媒体信息对象作为参数传入方法，并通过返回的对象来更新自己
		mp4info = this.converOperate(mp4info, mp4con, proinfo,lproinfo);

		// dash是基于mp4视频源进行的编码
		if (mp4info != null
				&& mp4info.getStatus() == MediaFileStatusDefine.READY) {
			DashConverterFactory dashfact = new DashConverterFactory();
			String filename = this.getFilename(mp4info.getFilename());
			BaseConverter dashcon = (DashConverter) dashfact.createConverter(
					mp4con.getOutPath(), mp4con.getOutName(),
					dashfact.getFileStorePath() + "/" + filename, filename
							+ "." + dashfact.getFileSuffix(),mp4con.getRealFileName());

			// TODO: 由于dash是通过对mp4分片得到，因此部分参数可以直接读取得到，若要准确获得转码后参数，可以分析mpd文件获得
			dashcon.setM_width(mp4con.getM_width());
			dashcon.setM_height(mp4con.getM_height());
			dashcon.setM_durations(mp4con.getM_durations());
			dashcon.setM_ratio(mp4con.getM_ratio());
			dashcon.setM_framerate(mp4con.getM_framerate());
			dashcon.setM_videocodes(mp4con.getM_videocodes());
			dashcon.setM_audiocodes(mp4con.getM_audiocodes());

			Mediafileinfo dashinfo = this.getMediaInfoByType(
					dashfact.getFileType(), list);
			// 将媒体信息对象作为参数传入方法，并通过返回的对象来更新自己
			dashinfo = this.converOperate(dashinfo, dashcon, proinfo,lproinfo);
		}

		// 转码结束后删除源文件
		File srcfile = new File(task.getFilepath() + "/" + task.getFilename());
		if(srcfile.exists()){
			//System.out.println("exists");
			srcfile.delete();
		}
		
	}

	private String getFilename(String fullname) {
		int index = fullname.lastIndexOf('.');
		if (index < 0)
			return fullname;
		else
			return fullname.substring(0, index);
	}

	private Mediafileinfo converOperate(Mediafileinfo mediainfo,
			BaseConverter con, Mediafileprototype proinfo,Localmediafileprototype lproinfo) {
		if (mediainfo == null) {
			mediainfo = new Mediafileinfo();
			mediainfo.setRequesttimes(0);
			mediainfo.setDeletetime(null);
			mediainfo.setIsdeleted(false);
			mediainfo.setMediafileprototype(proinfo);
			mediainfo.setLocalmediafileprototype(lproinfo);//本地上传的资源
			mediainfo.setStatus(MediaFileStatusDefine.CREATED);
		}

		if (!mediainfo.getStatus().equals(MediaFileStatusDefine.READY)) {
			Session sen = HibernateSessionFactory.getSession();
			Transaction tran = sen.beginTransaction();
			mediainfo.setFilename(con.getOutName());
			mediainfo.setRealfilename(con.getRealFileName());
			mediainfo.setFilepath(con.getOutPath());
			mediainfo.setFiletype(con.getFiletype());
			if (con.startConver() == true) {
				mediainfo.setVideocodes(con.getM_videocodes());
				mediainfo.setAudiocodes(con.getM_audiocodes());
				mediainfo.setBitrate(String.valueOf(con.getM_bitrate()));
				mediainfo.setDurations(con.getM_durations());
				mediainfo.setFramerate(con.getM_framerate());
				mediainfo.setHeight(con.getM_height());
				mediainfo.setWidth(con.getM_width());
				mediainfo.setRatio(con.getM_ratio());
				mediainfo.setSize(con.getM_size());
				mediainfo.setRequesttimes(mediainfo.getRequesttimes() + 1);
				mediainfo.setStatus(MediaFileStatusDefine.READY);
			} else {
				mediainfo.setStatus(MediaFileStatusDefine.READY);
			}
			sen.save(mediainfo);
			tran.commit();
		}

		return mediainfo;
	}

	private Mediafileinfo getMediaInfoByType(String type,
			List<Mediafileinfo> infolist) {
		for (Mediafileinfo mfi : infolist) {
			if (mfi.getFiletype().equalsIgnoreCase(type))
				return mfi;
		}
		return null;
	}
}
