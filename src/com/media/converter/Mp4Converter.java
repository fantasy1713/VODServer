package com.media.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.format.json.MediaFormatJson;
import com.format.json.MediaInfoJson;
import com.format.json.MediaStreamJson;
import com.global.defines.ConvertSettings;

public class Mp4Converter extends BaseConverter{
	private String m_srcfullname;
	private String m_outfilename;
	
	private ProcessBuilder m_probuilder;
	private Process m_process;
	
	public Mp4Converter(String srcdir, String srcname, String targdir, String targname, String filetype,String realfilename){
		super(srcdir, srcname, targdir, targname, filetype,realfilename);
		
		m_srcfullname = m_filepath + "/" + m_filename;
		m_outfilename = m_outpath + "/" + m_outname;
		m_pecent = 0;
		m_issuccess = true;
		m_realfilename =realfilename;
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

	private Boolean checkFileCodecs(String filename){
		String info = getFileFormatInfo(filename);
		if(info == null || info.length() < 10)
			return null;
		
		Boolean result = null;
		info = info.replace("\"default\":", "\"default1\":");
		JSONObject obj = JSONObject.fromObject(info);
		MediaInfoJson infojson = (MediaInfoJson) JSONObject.toBean(obj, MediaInfoJson.class);
		
		for(MediaStreamJson stream : infojson.getStreams()){
			if(stream.getCodec_type().equalsIgnoreCase("video")){
				if(stream.getCodec_name().equalsIgnoreCase("h264"))
					result = true;
				else
					result = false;
				break;
			}
		}
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
		
		for(int i=0; i<cmdparams.size(); i++){
			System.out.print(cmdparams.get(i) + " ");
		}
		
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

	private boolean execConvert(List<String> cmd){
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
	
	public String getMediaInfo(InputStream is) throws IOException {
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
	
	public boolean getProcessInfo(InputStream is) throws IOException {
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
                    System.out.println("Mp4Converter: TotalTime " + m_totaltime);
                    patten = Pattern.compile(regexDuration);
                    startinfo = false;
            	}
            	else{
            		m_processtime = timeConverter(m.group(5));
            		m_pecent = m_processtime / m_totaltime * 100;
            		System.out.println("Mp4Converter: Convert Process " + m_pecent);
            	}
            }
        }
        br.close();
        
        return !interrupt;
    }
	
	private boolean setMediaInfo(){
		String info = getFileFormatInfo(m_outfilename);
		if(info == null)
			return false;
		if(info.length() < 10){
			setError("文件转码失败");
			return false;
		}
		
		info = info.replace("\"default\":", "\"default1\":");
		JSONObject obj = JSONObject.fromObject(info);
		MediaInfoJson infojson = (MediaInfoJson) JSONObject.toBean(obj, MediaInfoJson.class);
		MediaFormatJson format = infojson.getFormat();
		this.setM_bitrate(format.getBit_rate());
		this.setM_durations(Float.parseFloat(format.getDuration()));
		this.setM_size(Long.parseLong(format.getSize()));
		
		for(MediaStreamJson stream : infojson.getStreams()){
			if(stream.getCodec_type().equalsIgnoreCase("audio")){
				this.setM_audiocodes(stream.getCodec_name());
			}
			else if(stream.getCodec_type().equalsIgnoreCase("video")){
				this.setM_videocodes(stream.getCodec_name());
				this.setM_height(stream.getHeight());
				this.setM_width(stream.getWidth());
				this.setM_ratio(stream.getDisplay_aspect_ratio());
				String []rates = stream.getR_frame_rate().split("/");
				this.setM_framerate(Float.parseFloat(rates[0]) / Float.parseFloat(rates[1]));
			}
		}
		return true;
	}
	

	public boolean startConver(){
		File srcfile = new File(m_srcfullname);
		if(!srcfile.exists()){
			setError("源文件不存在：" + m_srcfullname);
			return false;
		}
		
		Boolean codescheck = this.checkFileCodecs(m_srcfullname);
		if(codescheck == null){
			setError("目标文件无视频流");
			return false;
		}
		
		List<String> cmdparams = new LinkedList<String>();
		cmdparams.add(ConvertSettings.getFFmpegpath());
		cmdparams.add("-y");
		cmdparams.add("-i");
		cmdparams.add(m_srcfullname);
		cmdparams.add("-preset");
		cmdparams.add(ConvertSettings.getPreset(m_presetlv));
		cmdparams.add("-c:v");
		cmdparams.add("libx264");
		cmdparams.add("-c:a");
		cmdparams.add("libvo_aacenc");
		cmdparams.add("-profile:v");
		cmdparams.add("high");
		cmdparams.add("-force_key_frames");
		cmdparams.add("expr:gte(t,n_forced*" + ConvertSettings.getSegmentTime() + ")");
		cmdparams.add(m_outfilename);
		
		for(int j=0; j<cmdparams.size(); j++){
			System.out.print(cmdparams.get(j) + " ");
		}
		System.out.println("");
		
		execConvert(cmdparams);
		
		setMediaInfo();
		return m_issuccess;
	}
	
	public static void main(String[] args){
		long time = System.currentTimeMillis();
		Mp4Converter con = new Mp4Converter("d:/test", "6.ts", "d:/2", "2.mp4", "mp4","2");
		if(con.startConver()){
			System.out.println("success");
		}
		else{
			System.out.println("failed");
			System.out.println(con.getError());
		}
		time = System.currentTimeMillis() - time;
		System.out.println(time);
	}
}
