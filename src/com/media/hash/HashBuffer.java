package com.media.hash;

public class HashBuffer {
	private byte []m_buffer;
	private int m_lens;
	private boolean m_endblock;
	private boolean m_available;
	
	public HashBuffer(){
		m_buffer = new byte[1048576];
		m_available = false;
	}

	public byte[] getM_buffer() {
		return m_buffer;
	}

	public boolean isM_available() {
		return m_available;
	}

	public void setM_buffer(byte[] m_buffer) {
		this.m_buffer = m_buffer;
	}

	public void setM_available(boolean m_available) {
		this.m_available = m_available;
	}

	public int getM_lens() {
		return m_lens;
	}

	public void setM_lens(int m_lens) {
		this.m_lens = m_lens;
	}

	public boolean isM_endblock() {
		return m_endblock;
	}

	public void setM_endblock(boolean m_endblock) {
		this.m_endblock = m_endblock;
	}
}
