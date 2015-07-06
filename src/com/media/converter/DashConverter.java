package com.media.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.format.json.MediaInfoJson;
import com.format.json.MediaStreamJson;
import com.global.defines.ConvertSettings;

public class DashConverter extends BaseConverter{
	private static final long g_minbitrate = 409600;
	private static int g_segdurfix = 100;
	
	private long m_orignalbitrate;
	private List<String> m_targfullnames;
	private List<Long> m_targetbitrates;
	private String m_srcfilename;
	private String m_tmpdirname;

	private int m_totalprocess = 100;
	private int m_curprocess = 0;
	
	private boolean m_twopass = true;
	
	private ProcessBuilder m_probuilder;
	private Process m_process;
	Logger logger = Logger.getLogger("pay-log");
	
	
	public DashConverter(String filepath, String filename, String outpath,
			String outname, String filetype,String realfilename) {
		super(filepath, filename, outpath, outname, filetype,realfilename);
		
		logger.info("dashconverter构造函数");
		
		m_srcfilename = filepath + "/" + filename;
		m_tmpdirname = outpath + "/" + ConvertSettings.getDashTmpDirName();
		m_targfullnames = new ArrayList<String>();
		m_targetbitrates = new ArrayList<Long>();
		m_realfilename = realfilename;
	}
	
	private Boolean checkFileCodecs(){
		logger.info("func DashConverter.checkFileCodecs");
		String info = getFileFormatInfo(m_srcfilename);
		logger.info("m_srcfilename="+m_srcfilename);
		if(info == null || info.length() < 10)
		{
			logger.warn("info==null");
			return null;
		}
		Boolean result = null;
		info = info.replace("\"default\":", "\"default1\":");
		JSONObject obj = JSONObject.fromObject(info);
		MediaInfoJson infojson = (MediaInfoJson) JSONObject.toBean(obj, MediaInfoJson.class);
		
		try {
			m_orignalbitrate = infojson.getFormat().getBit_rate();
		} catch (Exception e) {
			
			logger.error("从json中获取码率异常", e);
		}
		logger.info("m_orignalbitrate: "+m_orignalbitrate);
		
		for(MediaStreamJson stream : infojson.getStreams()){
			if(stream.getCodec_type().equalsIgnoreCase("video")){
				if(stream.getCodec_name().equalsIgnoreCase("h264")){
					try{
						m_orignalbitrate = Long.parseLong(stream.getBit_rate());
					}
					catch(NumberFormatException ex){
						logger.error("获得码率失败",ex);
					}
					result = true;
				}
				else{
					result = false;
				}
				break;
			}
		}
		logger.info("func DashConverter.checkFileCodecs return: "+result);
		return result;
	}
	
	private String getFileFormatInfo(String filename){
		File dstfile = new File(filename);
		if(!dstfile.exists()){
			this.setError("源文件不存在");
			return "";
		}
		
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getFFprobepath());
		cmdparams.add("-v");
		cmdparams.add("quiet");
		cmdparams.add("-print_format");
		cmdparams.add("json");
		cmdparams.add("-show_format");
		cmdparams.add("-show_streams");
		cmdparams.add(filename);
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<cmdparams.size(); i++){
			System.out.print(cmdparams.get(i) + " ");
			sb.append(cmdparams.get(i) + " ");
		}
		logger.info(sb.toString());
		return execAnalyze(cmdparams);
	}
	
	private String execAnalyze(List<String> cmd){
		m_probuilder = new ProcessBuilder();
		m_probuilder.redirectErrorStream(true);
		m_probuilder.command(cmd);
		String json = null;
		try {
			m_process = m_probuilder.start();
			InputStream is = m_process.getInputStream();
			if((json = getMediaInfo(is)) == null)
				m_process.destroy();
			else
				m_process.waitFor();
		} catch (IOException e) {
			setError(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			if(m_process != null)
				m_process.destroy();
			setError("转码过程被终止");
			e.printStackTrace();
		}
		return json;
	}
	
	private String getMediaInfo(InputStream is) throws IOException {
		boolean interrupt = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer sb = new StringBuffer();
        while((line = br.readLine()) != null && (interrupt = Thread.interrupted()) == false){
        	sb.append(line);
        }
        br.close();
        
        if(interrupt == true)
        	return null;
        else
        	return sb.toString();
    }
	
	private boolean execFFmpegConvert(List<String> cmd){
		m_probuilder = new ProcessBuilder();
		m_probuilder.redirectErrorStream(true);
		m_probuilder.command(cmd);
		try {
			m_startime = System.currentTimeMillis();
			m_process = m_probuilder.start();
			InputStream is = m_process.getInputStream();
			if(getBitrateProcessInfo(is))
				m_process.waitFor();
			else
				m_process.destroy();
		} catch (IOException e) {
			setError(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			if(m_process != null)
				m_process.destroy();
			setError("转码过程被终止");
			e.printStackTrace();
			return false;
		}
		finally{
			m_startime = System.currentTimeMillis() - m_startime;
		}
		
		System.out.println("time spend:" + m_startime);
		
		return true;
	}
	
	private double timeConverter(String timeString){
		Pattern pattern = Pattern.compile("(\\d*):([0-5]?\\d):([0-5]?\\d).(\\d*)|(\\d*):([0-5]?\\d):([0-5]?\\d)$");
		Matcher m = pattern.matcher(timeString);
		if(m.find()){
			Integer hour = Integer.parseInt(m.group(1));
			Integer minute = Integer.parseInt(m.group(2));
			Integer second = Integer.parseInt(m.group(3));
			Double msecond = null;
			if(m.group(4) != null && m.group(4).length() > 0)
				msecond = Double.valueOf("." + m.group(4));
			double time = second + 60 * minute + 3600 * hour;
			if(msecond != null)
				time += msecond;
			return time;
		}
		else
			return -1;
	}
	
	private boolean setTargetBitrates(){
		if(m_orignalbitrate == 0)
			return false;
		
		String outname = m_tmpdirname + "/";
		String outpostfix = ".mp4";
		int index = m_filename.lastIndexOf('.');
		if(index > 0)
			outname += m_filename.substring(0, index);
		else
			outname += m_filename;
		
		int lv = ConvertSettings.getBitrateLv();
		long inc = m_orignalbitrate - g_minbitrate;
		//记录最低码率
		if(inc > g_minbitrate){
			m_targetbitrates.add(g_minbitrate);
			m_targfullnames.add(outname + "_" + m_targetbitrates.get(0) + outpostfix);
			lv--;
		}
		
		//计算码率等级
		while(lv > 1 && ((inc / lv) < g_minbitrate)){
			lv--;
		}
		
		inc = inc / lv;
		long curbitrate = m_orignalbitrate - inc;
		//根据等级数计算码率，不包括最高码率在内
		for(int i=1; i < lv; i++){
			m_targetbitrates.add(curbitrate);
			m_targfullnames.add(outname + "_" + curbitrate + outpostfix);
			curbitrate -= inc;
		}
		
		m_totalprocess = m_targetbitrates.size() * 200 + 100;
		
		return true;
	}
	
	private boolean execOnePass(List<Long> bitrates, List<String> targetnames){
		m_totalprocess = 200;
		
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getFFmpegpath());
		cmdparams.add("-y");
		cmdparams.add("-i");
		cmdparams.add(m_srcfilename);
		
		for(int i=0; i < bitrates.size(); i++){
			cmdparams.add("-force_key_frames");
			cmdparams.add("expr:gte(t,n_forced*" + ConvertSettings.getSegmentTime() + ")");
			cmdparams.add("-b:v");
			cmdparams.add(String.valueOf(bitrates.get(i)));
			cmdparams.add("-profile:v");
			cmdparams.add("high");
			cmdparams.add("-preset");
			cmdparams.add(ConvertSettings.getPreset(m_presetlv));
			cmdparams.add(targetnames.get(i));
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<cmdparams.size(); i++){
			System.out.print(cmdparams.get(i) + " ");
			sb.append(cmdparams.get(i)+" ");
		}
		logger.info("exec  Pass:  "+sb.toString());

		return execFFmpegConvert(cmdparams);
	}
	
	private boolean execFirstPass(long bitrate){
		boolean iswindows = this.isWindowsOs();
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getFFmpegpath());
		cmdparams.add("-y");
		cmdparams.add("-i");
		cmdparams.add(m_srcfilename);
		cmdparams.add("-force_key_frames");
		cmdparams.add("expr:gte(t,n_forced*" + ConvertSettings.getSegmentTime() + ")");
		cmdparams.add("-preset");
		cmdparams.add(ConvertSettings.getPreset(m_presetlv));
		cmdparams.add("-pass");
		cmdparams.add("1");
		cmdparams.add("-profile:v");
		cmdparams.add("high");
		cmdparams.add("-b:v");
		cmdparams.add(String.valueOf(bitrate));
		cmdparams.add("-an");
		cmdparams.add("-f");
		cmdparams.add("mp4");
		cmdparams.add("-passlogfile");
		cmdparams.add(m_tmpdirname + "/" + m_outname);
		if(iswindows)
			cmdparams.add("NUL");
		else
			cmdparams.add("/dev/null");
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<cmdparams.size(); i++){
			System.out.print(cmdparams.get(i) + " ");
			sb.append(cmdparams.get(i)+" ");
		}
		logger.info("exec 1 Pass:  "+sb.toString());

		return execFFmpegConvert(cmdparams);
	}
	
	private boolean execSecondPass(long bitrate, String targetname){
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getFFmpegpath());
		cmdparams.add("-y");
		cmdparams.add("-i");
		cmdparams.add(m_srcfilename);
		cmdparams.add("-force_key_frames");
		cmdparams.add("expr:gte(t,n_forced*" + ConvertSettings.getSegmentTime() + ")");
		cmdparams.add("-b:v");
		cmdparams.add(String.valueOf(bitrate));
		cmdparams.add("-profile:v");
		cmdparams.add("high");
		cmdparams.add("-preset");
		cmdparams.add(ConvertSettings.getPreset(m_presetlv));
		cmdparams.add("-pass");
		cmdparams.add("2");
		cmdparams.add("-passlogfile");
		cmdparams.add(m_tmpdirname + "/" + m_outname);
		cmdparams.add(targetname);
		
		StringBuffer sb = new StringBuffer();
		for(int j=0; j<cmdparams.size(); j++){
			System.out.print(cmdparams.get(j) + " ");
			sb.append(cmdparams.get(j)+" ");
		}
		logger.info("exec 2 Pass:  "+sb.toString());
		execFFmpegConvert(cmdparams);
		return true;
	}
	
	private boolean execBitrateConvert(boolean usetwopass){
		logger.info("func execBitrateConvert is2pass?"+usetwopass);
		if(this.checkFileCodecs() == null){
			setError("媒体无视频流");
			logger.info("媒体无视频流");
			return false;
		}
		
		if(!this.setTargetBitrates()){
			setError("码率等级计算失败");
			logger.info("码率等级计算失败");
			return false;
		}
		
		if(m_targfullnames.size() == 0){
			return true;
		}
		
		if(usetwopass){
			for(int i=0; i < m_targfullnames.size(); i++){	
				this.execFirstPass(m_targetbitrates.get(i));
				m_curprocess += 100;
				this.execSecondPass(m_targetbitrates.get(i), m_targfullnames.get(i));
				m_curprocess += 100;
			}
		}
		else{
			this.execOnePass(m_targetbitrates, m_targfullnames);
		}
		logger.info("func execBitrateConvert finish");
		return true;
	}
	
	private boolean getBitrateProcessInfo(InputStream is) throws IOException {
		boolean interrupt = false;
		boolean startinfo = true;
		String startRegexInfo = "Duration:(.*?), start:(.*?), bitrate:(.*?)";
        String regexDuration = "frame=(.*?) fps=(.*?) q=(.*?) size=(.*?)kB time=(.*?) bitrate=(.*?)kbits\\/s";
       
        Pattern patten = Pattern.compile(startRegexInfo);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null && (interrupt = Thread.interrupted()) == false){
            Matcher m = patten.matcher(line);
            if (m.find()) {
            	if(startinfo){
            		m_totaltime = timeConverter(m.group(1));
                    System.out.println("DashConvert: TotalTime " + m_totaltime);
                    patten = Pattern.compile(regexDuration);
                    startinfo = false;
            	}
            	else{
            		m_processtime = timeConverter(m.group(5));
            		m_pecent = (m_processtime / m_totaltime * 100 + m_curprocess) / m_totalprocess;
            		System.out.println("DashConvert: Convert Process " + m_pecent);
            	}
            }
        }
        br.close();
        
        return !interrupt;
	}
	
	public boolean getProcessInfo(InputStream is) throws IOException {
		logger.info("func DashConverter.getProcessInfo:切片操作细节信息\r\n");
		
		boolean interrupt = false;
		String startFileInfo = "DASHing file (\\S*)";
        String proceDuration = "ISO File Fragmenting: \\|=*\\s+\\| \\((\\d\\d)/100\\)";
       
        int filecount = m_targfullnames.size();
        int curcount = -1;
        
        Pattern patten1 = Pattern.compile(startFileInfo);
        Pattern patten2 = Pattern.compile(proceDuration);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null && (interrupt = Thread.interrupted()) == false){
        	logger.info("line: "+line);
            Matcher m2 = patten2.matcher(line);
            if (m2.find()) {
        		int proc = Integer.parseInt(m2.group(1));
        		m_pecent = (curcount  + (double)proc / 100) / filecount * 100;
            }
            else{
            	Matcher m1 = patten1.matcher(line);
            	if(m1.find()){
            		curcount++;
                	m_pecent = (double)curcount / filecount * 100;
            	}
            }
            System.out.println("DashConverter: Cutting Process" + m_pecent);
            logger.info("DashConverter: Cutting Process" + m_pecent);
        }
        br.close();
        
        return !interrupt;
    }
	
	private boolean execDashConvert(List<String> cmd){
		logger.info("DashConverter.execDashConvert");
		
		m_probuilder = new ProcessBuilder();
		m_probuilder.redirectErrorStream(true);
		m_probuilder.command(cmd);
		try {
			m_startime = System.currentTimeMillis();
			m_process = m_probuilder.start();
			InputStream is = m_process.getInputStream();
			if(getProcessInfo(is))
				m_process.waitFor();
			else
				m_process.destroy();
		} catch (IOException e) {
			logger.error("转码过程被终止",e);
			setError(e.getMessage());
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			if(m_process != null)
				m_process.destroy();
			logger.error("转码过程被终止",e);
			setError("转码过程被终止");
			e.printStackTrace();
			return false;
		}
		finally{
			m_startime = System.currentTimeMillis() - m_startime;
		}
		
		System.out.println("time spend:" + m_startime);
		logger.info("time spend:" + m_startime);
		
		return true;
	}
	
	private boolean execDashConvert(){
		logger.info("func execDashConvert");
		
		boolean iswindows = this.isWindowsOs();
		Date curtime = new Date();
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMdd_hhmmss");
		
		String segname = dateformat.format(curtime) + "_%s";
		if(!iswindows)
			segname = m_outpath+"/"+segname;
		
		logger.info("segname: "+segname);
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getMp4Boxpath());
		cmdparams.add("-dash");
		cmdparams.add(String.valueOf(ConvertSettings.getSegmentTime() * 1000 + g_segdurfix));
		cmdparams.add("-frag");
		cmdparams.add(String.valueOf(ConvertSettings.getSegmentTime() * 1000 + g_segdurfix));
		cmdparams.add("-rap");
		//cmdparams.add("-frag-rap");
		cmdparams.add("-segment-name");
		cmdparams.add(segname);
		cmdparams.add("-tmp");
		cmdparams.add(this.stringFormatByOs(m_outpath, iswindows));
		cmdparams.add("-url-template");
		for(int i=0; i<m_targfullnames.size(); i++){
			cmdparams.add(this.stringFormatByOs(m_targfullnames.get(i), iswindows));
		}
		cmdparams.add(this.stringFormatByOs(m_srcfilename, iswindows));
		cmdparams.add("-out");
		cmdparams.add(this.stringFormatByOs(m_outpath + "/" + m_outname, iswindows));
		
		StringBuffer sb = new StringBuffer();
		for(int j=0; j<cmdparams.size(); j++){
			System.out.print(cmdparams.get(j) + " ");
			sb.append(cmdparams.get(j) + " ");
		}
		logger.info("视频切割操作 ： "+sb.toString());
		System.out.println("");
		
		if(!execDashConvert(cmdparams))
			return false;
		
		return true;
	}
	
	private boolean createDirs(){
		File dfile = new File(m_outpath);
		if(!dfile.exists() && (dfile.mkdirs() == false)){
			setError("创建Dash文件目录失败");
			return false;
		}
		
		File dtmpfile = new File(m_tmpdirname);
		if(!dtmpfile.exists() && (dtmpfile.mkdirs() == false)){
			setError("创建Dash临时文件目录失败");
			return false;
		}
		return true;
	}
	
	private void cleanExtraTempFiles(){
		File dtmpfile = new File(m_tmpdirname);
		for(File fi : dtmpfile.listFiles()){
			fi.delete();
		}
		dtmpfile.delete();
	}
	
	public void useTwoPass(boolean twopass){
		m_twopass = twopass;
	}

	@Override
	public boolean startConver() {
		logger.info("func startConver is2pass?"+m_twopass);
		if(!this.createDirs()){
			return false;
		}
		
		if(!this.execBitrateConvert(m_twopass)){
			setError("多码率编码失败");
			logger.info("多码率编码失败");
			return false;
		}
		
		if(!this.execDashConvert()){
			setError("Dash切片失败");
			logger.info("Dash切片失败");
			return false;
		}

		this.setMediaSize();
		
		this.cleanExtraTempFiles();
		
		return true;
	}
	
	private void setMediaSize(){
		File dir = new File(m_outpath);
		long size = 0;
		for(File subfile : dir.listFiles()){
			if(!subfile.isDirectory())
				size += subfile.length();
		}
		this.setM_size(size);
	}
	
	private boolean isWindowsOs(){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os.startsWith("win") || os.startsWith("Win")){
			return true;
		}
		return false;
	}
	
	private String stringFormatByOs(String str, boolean iswindows){
		if(iswindows)
			return str.replace('/', '\\');
		else
			return str;
	}
	
	public static void main(String[] args){
		DashConverter con = new DashConverter("d:/2", "1.mp4", "d:/2", "test.mpd", "mp4","test");
		if(con.startConver()){
			System.out.println("success");
		}
		else{
			System.out.println("failed");
			System.out.println(con.getError());
		}
	}
}
