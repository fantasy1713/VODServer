package com.video.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.format.json.VideoPostRequestJson;
import com.vodserver.hibernate.beans.Mediafileprototype;

public class VideoRequestHandler {
	private String []m_urllist;
	private List<VideoPostRequestJson> m_jsonlist;
	
	public VideoRequestHandler(){
		m_urllist = null;
		m_jsonlist = null;
	}
	
	public void setUrlList(String []urllist){
		m_urllist = urllist;
	}
	
	public void setJsonList(List<VideoPostRequestJson> jsonlist){
		m_jsonlist = jsonlist;
	}
	
	public MediaIdInfo getMediaId(){
		MediaIdInfo info = new MediaIdInfo();
		info.setAvailable(false);
		if(m_urllist == null || m_urllist.length == 0){
			info.setContent("点播资源链接不能为空");
			return info;
		}
		
		List<WeblibUrlInfo> webliblist = null;
		try {
			webliblist = getUrlList();
		} catch (MalformedURLException e) {
			info.setContent(e.getMessage());
			e.printStackTrace();
			return info;
		}
		
		VideoManager manager = new VideoManager();
		String mediaid = null;
		if((mediaid = manager.findMediaExists(webliblist)) == null){
			mediaid = manager.createMediaQuery(webliblist);
		}
		
		if(mediaid == null){
			info.setContent("创建媒体链接失败");
			return info;
		}
		
		manager.updateMediaContent(mediaid);

		info.setAvailable(true);
		info.setContent(mediaid);
		return info;
	}
	

	private List<WeblibUrlInfo> getUrlList() throws MalformedURLException{
		List<WeblibUrlInfo> webliblist = new ArrayList<WeblibUrlInfo>();
		Pattern pattern = Pattern.compile("id=(\\d+)");
		int counter = 1;
		if(m_urllist != null){
			for(String signurl : m_urllist){
				URL url = new URL(signurl);
				Matcher m = pattern.matcher(url.getQuery());
				if(m.find()){
					WeblibUrlInfo winfo = new WeblibUrlInfo();
					winfo.url = url.getHost();
					Integer port;
					if((port = url.getPort())!=-1){
						winfo.url+=":"+port.toString();
					}
					winfo.id = Integer.parseInt(m.group(1));
					winfo.orderindex = 1;
					winfo.viewindex = counter++;
					winfo.orignalurl = signurl;
					winfo.startoffset = 0;
					webliblist.add(winfo);
				}
			}
		}
		else if(m_jsonlist != null){
			for(VideoPostRequestJson item : m_jsonlist){
				URL url = new URL(item.getHost());
				WeblibUrlInfo winfo = new WeblibUrlInfo();
				winfo.url = url.getHost();
				winfo.id = item.getId();
				winfo.orderindex = item.getOrder();
				winfo.viewindex = item.getView();
				winfo.orignalurl = item.getHost();
				winfo.startoffset = item.getToffset();
				webliblist.add(winfo);
			}
		}
		
		return webliblist;
	}
	
	public static void main(String arg[]){
	//	String teststr[] = new String[1];
	//	teststr[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=392811";
		//teststr[1] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390676";
		//teststr[2] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390675";
	//	VideoRequestHandler test = new VideoRequestHandler(teststr);
	//	System.out.println(test.getMediaId().content);
		
		String teststr2[] = new String[1];
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=393008";	//cow.mp4
	//	teststr2[1] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=392811";	//univac.webm
	//	teststr2[3] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390675";	//1.mp4
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390676";	//Dirt3.webm
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=393705";	//变形金刚
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=393706";	//最终幻想
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=393707";	//bunny
	//	teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=393708";	//变形金刚二avi
		teststr2[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=407047";
		
		VideoRequestHandler test2 = new VideoRequestHandler();
		test2.setUrlList(teststr2);
		VideoResponseHandler test3 = new VideoResponseHandler();
		System.out.println(test3.getMediaContent(test2.getMediaId().getContent()));
		
/*		String teststr3[] = new String[3];
		teststr3[1] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=392811";
		teststr3[2] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390676";
		teststr3[0] = "http://weblib2011.ccnl.scut.edu.cn/user/getResources.action?id=390675";
		VideoRequestHandler test3 = new VideoRequestHandler(teststr3);
		System.out.println(test3.getMediaId().content);*/
	}
}

class WeblibUrlInfo{
	public String url;
	public int id;
	public int viewindex;
	public int orderindex;
	public String orignalurl;
	public int startoffset;
	public Mediafileprototype prototype;
}