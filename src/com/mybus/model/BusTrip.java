package com.mybus.model;

public class BusTrip {
	Route route;
	Stop fromStop, toStop;
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
	
	public String toString(){
		return route.getTag()+"-"+route.getName() +separator+
				fromStop.getStopId()+"-"+fromStop.getName() +separator+ 
				toStop.getStopId() +"-"+ toStop.getName();
	}
}
