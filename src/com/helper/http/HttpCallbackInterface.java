package com.helper.http;

import java.util.Map;

public interface HttpCallbackInterface {
	public void httpCallback(boolean success, String result, String url, Map<String, String> reqparams);
}
