package com.media.converter;

import java.io.File;

import org.apache.log4j.Logger;

import com.global.defines.ServerSettings;

public class Mp4ConverterFactory implements IConverterFactory{
	public Mp4ConverterFactory(){
		File mfile = new File(this.getFileStorePath());
		if(!mfile.exists())
			mfile.mkdirs();
		Logger logger = Logger.getLogger("pay-log");
		logger.info("����·��"+this.getFileStorePath());
	}
	
	public String getFileType(){
		return "mp4";
	}
	
	public String getFileSuffix(){
		return "mp4";
	}
	
	public String getFileStorePath(){
		return ServerSettings.getVideoDirPath() + "/mp4";
	}
	
	@Override
	public BaseConverter createConverter(String srcfilepath, String srcfilename, String outfilepath, String outfilename,String realfilename) {
		return new Mp4Converter(srcfilepath, srcfilename, outfilepath, outfilename, this.getFileType(),realfilename);
	}
}
