package com.media.converter;

public abstract class BaseConverter {
	protected String m_filepath;
	protected String m_filename;
	protected String m_realfilename;
	protected String m_outpath;
	protected String m_outname;
	protected boolean m_issuccess;
	protected String m_errorinfo;
	protected String m_videocodes;
	protected String m_audiocodes;
	protected long m_bitrate;
	protected float m_durations;
	protected float m_framerate;
	protected int m_width;
	protected int m_height;
	protected String m_ratio;
	protected long m_size;
	protected double m_totaltime;
	protected double m_processtime;
	protected double m_pecent;
	protected long m_startime;
	protected String m_filetype;
	protected int m_presetlv;
	
	public BaseConverter(String filepath, String filename, String outpath, String outname, String filetype ,String realfilename){
		m_filepath = filepath;
		m_filename = filename;
		m_outpath = outpath;
		m_outname = outname;
		m_filetype = filetype;
		m_presetlv = 9; 	//0-9
		m_realfilename = realfilename;
	}
	
	public int getM_presetlv() {
		return m_presetlv;
	}

	public void setM_presetlv(int m_presetlv) {
		this.m_presetlv = m_presetlv;
	}

	public String getFiletype() {
		return m_filetype;
	}

	public String getFilePath(){
		return m_filepath;
	}
	
	public String getFileName(){
		return m_filename;
	}
	public String getRealFileName(){
		return m_realfilename;
	}
	
	public String getOutPath(){
		return m_outpath;
	}
	
	public String getOutName(){
		return m_outname;
	}
	
	public boolean isSuccess(){
		return m_issuccess;
	}
	
	public String getError(){
		return m_errorinfo;
	}
	
	protected void setError(String info){
		m_issuccess = false;
		m_errorinfo = info;
	}
	
	public String getM_videocodes() {
		return m_videocodes;
	}

	public String getM_audiocodes() {
		return m_audiocodes;
	}

	public long getM_bitrate() {
		return m_bitrate;
	}

	public float getM_durations() {
		return m_durations;
	}

	public float getM_framerate() {
		return m_framerate;
	}

	public int getM_width() {
		return m_width;
	}

	public int getM_height() {
		return m_height;
	}

	public String getM_ratio() {
		return m_ratio;
	}

	public long getM_size() {
		return m_size;
	}

	public void setM_videocodes(String m_videocodes) {
		this.m_videocodes = m_videocodes;
	}

	public void setM_audiocodes(String m_audiocodes) {
		this.m_audiocodes = m_audiocodes;
	}

	public void setM_bitrate(long m_bitrate) {
		this.m_bitrate = m_bitrate;
	}

	public void setM_durations(float m_durations) {
		this.m_durations = m_durations;
	}

	public void setM_framerate(float m_framerate) {
		this.m_framerate = m_framerate;
	}

	public void setM_width(int m_width) {
		this.m_width = m_width;
	}

	public void setM_height(int m_height) {
		this.m_height = m_height;
	}

	public void setM_ratio(String m_ratio) {
		this.m_ratio = m_ratio;
	}

	public void setM_size(long m_size) {
		this.m_size = m_size;
	}

	public double getM_totaltime() {
		return m_totaltime;
	}

	public double getM_processtime() {
		return m_processtime;
	}

	public double getM_pecent() {
		return m_pecent;
	}

	public long getM_startime() {
		return m_startime;
	}

	public void setM_totaltime(double m_totaltime) {
		this.m_totaltime = m_totaltime;
	}

	public void setM_processtime(double m_processtime) {
		this.m_processtime = m_processtime;
	}

	public void setM_pecent(double m_pecent) {
		this.m_pecent = m_pecent;
	}

	public void setM_startime(long m_startime) {
		this.m_startime = m_startime;
	}

	public abstract boolean startConver();
}
