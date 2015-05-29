package com.video.manager;

import java.util.List;

/**
 * @author zhangfan
 *
 */
public class VideoSearchHandler {
	/**
	 * @author zhangfan
	 * 查找已经缓存的dash格式视频文件
	 * @return
	 */
	public List getExistCache(){
		VideoManager manager = new VideoManager();
		List list = manager.getExistCache();
		return list;
	}

}
