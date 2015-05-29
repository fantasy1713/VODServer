package com.media.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashBase {
	protected boolean m_success;
	protected String m_errorinfo;
	protected MessageDigest m_digest;

	public HashBase(String algoname) throws NoSuchAlgorithmException{
		m_success = true;
		m_digest = MessageDigest.getInstance(algoname);
	}
	
	public boolean isSuccess(){
		return m_success;
	}
	
	public String getErrorInfo(){
		return m_errorinfo;
	}
	
	protected void setError(String info){
		m_success = false;
		m_errorinfo = info;
	}
	
	public void appendContent(byte []buffer, int offset, int len){
		m_digest.update(buffer, offset, len);
	}
	
	protected String getHashValue(){
		return getHexString(m_digest.digest());
	}
	
	protected String getHexString(byte []buffer){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<buffer.length; i++){
			int tmp = buffer[i] & 0xff;
			String tmpstr = Integer.toHexString(tmp);
			if(tmpstr.length() < 2)
				sb.append('0');
			sb.append(tmpstr);
		}
		return sb.toString();
	}
}
