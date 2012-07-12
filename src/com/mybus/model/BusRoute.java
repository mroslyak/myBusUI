package com.mybus.model;

import java.util.ArrayList;
import java.util.List;

public class BusRoute {
	String direction;
	List<Stop> stopList;
	
	public void setDirection(String direction){
		this.direction = direction;
	}
	
	public String getDirection(){return direction;}
	
	public void setStopList(List<Stop> list){
		this.stopList = list;
	}
	public List<Stop> getStopList(){
		if (stopList == null){
			stopList = new ArrayList<Stop>();
		}
		return stopList;
	}
	
	public String toString(){
		return direction;
	}
}
