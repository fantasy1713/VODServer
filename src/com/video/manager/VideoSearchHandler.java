package com.video.manager;

import java.util.List;

/**
 * @author zhangfan
 *
 */
public class VideoSearchHandler {
	/**
	 * @author zhangfan
	 * �����Ѿ������dash��ʽ��Ƶ�ļ�
	 * @return
	 */
	public List getExistCache(){
		VideoManager manager = new VideoManager();
		List list = manager.getExistCache();
		return list;
	}

}
