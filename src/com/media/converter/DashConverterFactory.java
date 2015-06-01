package com.media.converter;

import java.io.File;

import com.global.defines.ServerSettings;

public class DashConverterFactory implements IConverterFactory{
	public DashConverterFactory(){
		File mfile = new File(this.getFileStorePath());
		if(!mfile.exists())
			mfile.mkdirs();
	}
	
	public String getFileType(){
		return "mpeg-dash";
	}
	
	public String getFileSuffix(){
		return "mpd";
	}
	
	public String getFileStorePath(){
		return ServerSettings.getVideoDirPath() + "/dash";
	}
	
	@Override
	public BaseConverter createConverter(String srcfilepath,
			String srcfilename, String outfilepath, String outfilename,String realfilename) {
		return new DashConverter(srcfilepath, srcfilename, outfilepath, outfilename, this.getFileType(),realfilename);
	}
}
