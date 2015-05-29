package com.format.json;

public class ErrorInfoFactory {
	public static ErrorInfoJson requestError(String info){
		return new ErrorInfoJson(400, "error", info);
	}
}