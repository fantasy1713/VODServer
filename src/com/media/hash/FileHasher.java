package com.media.hash;

import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Semaphore;

public class FileHasher extends HashBase implements Runnable{
	private String m_filepath;
	private FileHashCallback m_caller;
	private static int g_buffercount = 10;
	private HashBuffer []m_buffer;
	private Thread m_readthread;
	private Semaphore m_readsem;
	private Semaphore m_writesem;
	private String m_sha1value;
	
	public FileHasher(FileHashCallback caller, String filepath, String algo) throws NoSuchAlgorithmException{
		super(algo);
		m_caller = caller;
		m_filepath = filepath;
		m_buffer = new HashBuffer[g_buffercount];
		for(int i=0; i < g_buffercount; i++)
			m_buffer[i] = new HashBuffer();
		m_readsem = new Semaphore(g_buffercount);
		m_writesem = new Semaphore(0);
	}
	
	public String getHashResult(){
		return m_sha1value;
	}
	
	@Override
	protected String getHashValue(){
		if(m_readthread != null)
			m_readthread.interrupt();
		
		m_readthread = new Thread(){
			public void run(){
				File srcfile = new File(m_filepath);
				if(!srcfile.exists()){
					setError("文件不存在");
				}
				int writeindex = 0;
				FileInputStream ins;
				try {
					ins = new FileInputStream(srcfile);
					while(true && !Thread.interrupted()){
						m_readsem.acquire();
						if(m_buffer[writeindex].isM_available() == false){
							HashBuffer hb = m_buffer[writeindex];
							hb.setM_lens(ins.read(hb.getM_buffer()));
							hb.setM_available(true);
							if(hb.getM_lens() < 0){
								hb.setM_endblock(true);
								m_writesem.release();
								break;
							}
							writeindex = (writeindex + 1) % g_buffercount;
						}
						m_writesem.release();
					}
					ins.close();
				} catch (Exception e) {
					setError(e.getMessage());
					m_buffer[writeindex].setM_available(true);
					m_buffer[writeindex].setM_endblock(true);
					m_writesem.release();
					e.printStackTrace();
				}
			}
		};
		m_readthread.start();
		
		String result = null;
		try {
			int readindex = 0;
			while(true){
				m_writesem.acquire();
				HashBuffer hb = m_buffer[readindex];
				if(hb.isM_available() == true){
					if(hb.isM_endblock()){
						m_readsem.release();
						break;
					}
					else{
						m_digest.update(hb.getM_buffer(), 0, hb.getM_lens());
					}
					readindex = (readindex + 1) % g_buffercount;
					hb.setM_available(false);
				}
				m_readsem.release();
			}

			if(m_success == true)
				result = getHexString(m_digest.digest());
		} catch (Exception e) {
			setError(e.getMessage());
			m_readthread.interrupt();
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void run() {
		m_sha1value = this.getHashValue();
		if(m_caller != null)
			m_caller.hashComplete(this);
	}
}
