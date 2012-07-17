package com.mybus.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BusTrip implements Serializable {
	Route route;
	Stop fromStop, toStop;
	Map<String,String> estimatedArrivalTimeList;
	private static final String separator =":";
	public BusTrip(String savedTrip){
		String[] segments = savedTrip.split(separator);
		String[] routeStr = segments[0].split("-");
		route = new Route(routeStr[0],routeStr[1]);
		
		String[] stopStr = segments[1].split("-");
		fromStop = new Stop(stopStr[0],stopStr[1]);
		
		stopStr = segments[2].split("-");
		toStop = new Stop(stopStr[0],stopStr[1]);
		
	}
	public BusTrip(){}
	
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Stop getFromStop() {
		return fromStop;
	}
	public void setFromStop(Stop fromStop) {
		this.fromStop = fromStop;
	}
	public Stop getToStop() {
		return toStop;
	}
	public void setToStop(Stop toStop) {
		this.toStop = toStop;
	}
	
	public String getNextArrivalBusNumber(){
		if (estimatedArrivalTimeList == null)
			return "Loading..";
		String bus = estimatedArrivalTimeList.get("nextBusRoute");
		if (bus == null)
			return null;
		
		return bus;		
	}
	public String getNextArrivalTime(){
		if (estimatedArrivalTimeList == null)
			return "Loading..";
		
		String time = estimatedArrivalTimeList.get("nextBusEstimate");
		if (time == null)
			return "N/A";
		return time;
	}
	
	public Map<String,String> getEstimatedArrivalMap(){
		return estimatedArrivalTimeList;
	}
	public void setEstimatedArrival(Map<String,String> list){
		estimatedArrivalTimeList = list;
	}
	public String toString(){
		return route.getTag()+"-"+route.getName() +separator+
				fromStop.getStopId()+"-"+fromStop.getName() +separator+ 
				toStop.getStopId() +"-"+ toStop.getName();
	}
}
