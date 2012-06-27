package com.mybus.model;

public class Route {
	String tag, name;

	public Route(String a, String b) {
		this.tag = a;
		this.name = b;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String str) {
		tag = str;
	}

	public String getName() {
		return name;
	}

	public void setName(String str) {
		name = str;
	}

	@Override
	public String toString() {
		return name;
	}
}
