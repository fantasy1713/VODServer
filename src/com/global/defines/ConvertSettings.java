package com.global.defines;

import java.util.ArrayList;
import java.util.List;

public class ConvertSettings {
	private static String g_ffmpegpath = "ffmpeg";
	private static String g_ffprobepath = "ffprobe";
	private static String g_mp4boxpath = "MP4Box";
	private static int g_segmenttime = 2;
	private static int g_bitratelv = 4;
	private static String g_tmpdirname = "dashtmp";
	private static List<String> g_ffmpegpreset = null;
	
	public static int getBitrateLv(){
		return g_bitratelv;
	}
	
	public static String getDashTmpDirName(){
		return g_tmpdirname;
	}
	
	public static String getPreset(int lv){
		if(g_ffmpegpreset == null){
			g_ffmpegpreset = new ArrayList<String>();
			g_ffmpegpreset.add("placebo");
			g_ffmpegpreset.add("veryslow");
			g_ffmpegpreset.add("slower");
			g_ffmpegpreset.add("slow");
			g_ffmpegpreset.add("medium");
			g_ffmpegpreset.add("fast");
			g_ffmpegpreset.add("faster");
			g_ffmpegpreset.add("veryfast");
			g_ffmpegpreset.add("superfast");
			g_ffmpegpreset.add("ultrafast");
		}
		
		if(lv >= g_ffmpegpreset.size()){
			lv = g_ffmpegpreset.size() - 1;
		}
		
		if(lv < 0){
			lv = 0;
		}
		
		return g_ffmpegpreset.get(lv);
	}
	public static String getFFmpegpath() {
		return g_ffmpegpath;
	}
	public static String getFFprobepath(){
		return g_ffprobepath;
	}
	public static String getMp4Boxpath(){
		return g_mp4boxpath;
	}
	public static int getSegmentTime(){
		return g_segmenttime;
	}
}
