package com.format.json;

public class SelectMemeberJson implements JsonResponseInterface{
	private MemberJson[] members;

	public MemberJson[] getMembers() {
		return members;
	}

	public void setMembers(MemberJson[] members) {
		this.members = members;
	}
}