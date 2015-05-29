package com.video.manager;

import net.sf.json.JSONObject;

import com.format.json.JsonResponseInterface;

public class VideoResponseHandler {
	public String getMediaContent(String mediaid){
		VideoManager manager = new VideoManager();
		JsonResponseInterface resp = manager.getMediaContents(mediaid);
		JSONObject obj = JSONObject.fromObject(resp);
		manager.updateMediaContent(mediaid);
		return obj.toString();
	}

	/**
	 * @author zhangfan
	 * ���ݻ�����Ƶ��idֵ��õ㲥������
	 * @param id ����id
	 * @return ������
	 */
	public String getAttrCode(String id) {
		VideoManager manager = new VideoManager();
		return manager.getAttrCode(id);
	}
}