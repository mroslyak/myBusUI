package com.mybus.model;

import java.io.Serializable;

public class Stop implements Serializable {

	String name, stopId;

	public Stop(String stopId, String name){
		this.stopId = stopId;
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	
	@Override
	public String toString(){
		return name;
	}
	
}
