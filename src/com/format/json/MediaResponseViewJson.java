package com.format.json;

import java.util.List;

public class MediaResponseViewJson implements JsonResponseInterface{
	private int viewindex;
	private List<MediaResponseOrderJson> order;
	public int getViewindex() {
		return viewindex;
	}
	public int getSegnums() {
		if(order == null)
			return 0;
		else
			return order.size();
	}
	public void setViewindex(int viewindex) {
		this.viewindex = viewindex;
	}
	public List<MediaResponseOrderJson> getOrder() {
		return order;
	}
	public void setOrder(List<MediaResponseOrderJson> order) {
		this.order = order;
	}
}
