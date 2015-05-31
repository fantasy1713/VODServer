package com.media.hash;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.global.defines.MediaFileStatusDefine;
import com.media.pipeline.MediaPipelineClass;
import com.media.pipeline.MediaPipelineTaskClass;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Mediafileprototype;

public class VideoHasher extends MediaPipelineClass implements FileHashCallback{
	private Map<FileHasher, MediaPipelineTaskClass> m_tasklist;
	
	public VideoHasher(){
		m_tasklist = new HashMap<FileHasher, MediaPipelineTaskClass>();
	}

	@Override
	public boolean addPipeTask(MediaPipelineTaskClass task) {
		try {
			FileHasher hasher = new FileHasher(this, task.getFilepath() + "/" + task.getFilename(), "SHA-1");
			Thread hashthread = new Thread(hasher);
			hashthread.start();
			task.setStatus(MediaFileStatusDefine.HASHING);
			m_tasklist.put(hasher, task);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void hashComplete(FileHasher sender) {
		if(sender.isSuccess()){
			MediaPipelineTaskClass task = m_tasklist.get(sender);
			m_tasklist.remove(sender);
			task.setSha1value(sender.getHashResult());
			//修改数据库
			if(task.getIslocalupload()){
				this.updateLocalPrototypeMediaInfo(task);//修改本地上传数据库表
			}
			else{
				this.updatePrototypeMediaInfo(task);
			}
			
			this.taskFinished(false, task);
		}
	}
	
	/**
	 * @author Phan
	 * @param task
	 */
	private void updateLocalPrototypeMediaInfo(MediaPipelineTaskClass task){
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createQuery("from Localmediafileprototype where sha1value=:sha1").setString("sha1", task.getSha1value());
		Mediafileprototype info = (Mediafileprototype) query.uniqueResult();
		File file = new File(task.getFilepath() + "/" + task.getFilename());
		Transaction tan = sen.beginTransaction();
		if(info != null){
			if(info.getId() != task.getPrototypeid()){
				query = sen.createSQLQuery("delete from Localmediafileprototype where id=:id");
				query.setInteger("id", task.getPrototypeid());
				query.executeUpdate();
				task.setPrototypeid(info.getId());
			}
			//delete file
			System.out.println("same file:" + task.getFilename() + "  sha1value:" + task.getSha1value());
		}
		else{
			query = sen.createSQLQuery("update Localmediafileprototype set sha1value=:sha1, size=:size, createtime=:createtime where id=:id");
			query.setString("sha1", task.getSha1value());
			query.setLong("size", file.length());
			query.setDate("createtime", new Date());
			query.setInteger("id", task.getPrototypeid());
			query.executeUpdate();
			
			System.out.println("new file:" + task.getFilename() + "  sha1value:" + task.getSha1value());
		}
		tan.commit();
	}
	private void updatePrototypeMediaInfo(MediaPipelineTaskClass task){
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createQuery("from Mediafileprototype where sha1value=:sha1").setString("sha1", task.getSha1value());
		Mediafileprototype info = (Mediafileprototype) query.uniqueResult();
		File file = new File(task.getFilepath() + "/" + task.getFilename());
		Transaction tan = sen.beginTransaction();
		if(info != null){
			if(info.getId() != task.getPrototypeid()){
				query = sen.createSQLQuery("delete from Mediafileprototype where id=:id");
				query.setInteger("id", task.getPrototypeid());
				query.executeUpdate();
				task.setPrototypeid(info.getId());
			}
			//delete file
			System.out.println("same file:" + task.getFilename() + "  sha1value:" + task.getSha1value());
		}
		else{
			query = sen.createSQLQuery("update Mediafileprototype set sha1value=:sha1, size=:size, createtime=:createtime where id=:id");
			query.setString("sha1", task.getSha1value());
			query.setLong("size", file.length());
			query.setDate("createtime", new Date());
			query.setInteger("id", task.getPrototypeid());
			query.executeUpdate();
			
			System.out.println("new file:" + task.getFilename() + "  sha1value:" + task.getSha1value());
		}
		tan.commit();
	}
}
