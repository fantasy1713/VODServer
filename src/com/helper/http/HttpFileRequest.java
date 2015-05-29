package com.helper.http;

import java.io.IOException;
import java.net.MalformedURLException;

public class HttpFileRequest extends BaseHttpClass{
	private int m_bytesdownloaded;
	
	public HttpFileRequest(String url) throws MalformedURLException{
		super(url);
	}
	
	public boolean canReadMore() throws IOException{
		if(m_connection.getInputStream().available() >= 0)
			return true;
		else
			return false;
	}
	
	public int dataAvailable() throws IOException{
		return m_connection.getInputStream().available();
	}
	
	public int getContentLen(){
		return m_contentlen;
	}

	public boolean isFinished(){
		if(m_bytesdownloaded >= m_contentlen)
			return true;
		else
			return false;
	}
	
	public void close(){
		if(m_connection != null){
			try {
				m_connection.getInputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			m_connection.disconnect();
		}
	}
	
	public int readData(byte[] data, int offset, int chunksize) throws IOException{
		int len = m_connection.getInputStream().read(data, offset, chunksize);
		if(len > 0)
			m_bytesdownloaded += len;
		return len;
	}
	
	public void openFile() throws IOException{
		try {
			this.createConnection();
		} catch (IOException e) {
			this.getResponseContent(m_connection.getErrorStream(), false);
			throw e;
		}
	}
}
