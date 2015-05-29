package com.weblib.manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.helper.http.HttpFileRequest;

public class DownloadFile {
	private static int g_chunksize = 4096;
	
	private DownloadFileInfo m_info;
	private FileOutputStream m_writer;
	private HttpFileRequest m_request;
	private byte[] m_buffer;
	private int m_bufferpointer;
	
	DownloadFile(DownloadFileInfo info) throws IOException, NoSuchAlgorithmException{
		m_info = info;
		m_info.setM_statu(FileStatus.DOWNLOADING);
		m_request = new HttpFileRequest("http://" + m_info.getM_host() + "/group/downloadResource.action");
		m_request.addHeader("Cookie", m_info.getM_cookie());
		m_request.addParam("id", String.valueOf(m_info.getM_id()));
		m_buffer = new byte[g_chunksize];
		m_bufferpointer = 0;
	}
	
	public void openFile() throws IOException{
		m_request.openFile();
		m_info.setM_size(m_request.getContentLen());
		m_writer = new FileOutputStream(m_info.getM_dirpath() + "/" + m_info.getM_filename());
	}
	
	public DownloadFileInfo getFileInfo(){
		return m_info;
	}
	
	public boolean isComplete(){
		return m_request.isFinished();
	}
	
	public void close() throws IOException{
		if(m_bufferpointer != 0){
			this.writeData(m_bufferpointer);
			m_bufferpointer = 0;
		}
		
		m_writer.close();
		m_request.close();
	}
	
	public boolean canReadMore() throws IOException{
		return m_request.canReadMore() 
				&& m_info.getM_statu() == FileStatus.DOWNLOADING;
	}
	
	public int processData(long lens) throws IOException{
		lens = Math.min(lens, m_request.dataAvailable());
		int readlen = 1;
		int bufferreaded = 0;
		while(lens >= 0 && readlen > 0){
			int datalen = (int) Math.min(lens, g_chunksize - m_bufferpointer);
			readlen = m_request.readData(m_buffer, m_bufferpointer, datalen);
			if(readlen > 0){
				m_bufferpointer += readlen;
				if(m_bufferpointer >= g_chunksize){
					this.writeData(m_bufferpointer);
					m_bufferpointer = 0;
				}
				bufferreaded += readlen;
				lens -= readlen;
			}
		}

		return bufferreaded;
	}

	private void writeData(int datalen) throws IOException{
		m_writer.write(m_buffer, 0, datalen);
		m_info.addBytesWritten(datalen);
		m_bufferpointer = 0;
	}
}
