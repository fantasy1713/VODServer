package com.media.converter;

public interface IConverterFactory {
	public String getFileType();
	public String getFileSuffix();
	public String getFileStorePath();
	public BaseConverter createConverter(String srcfilepath, String srcfilename, String outfilepath, String outfilename,String realfilename);
}
