package com.mybus.model;

import java.io.Serializable;

public class RouteName implements Serializable{
	String tag, name;

	public RouteName(String a, String b) {
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
