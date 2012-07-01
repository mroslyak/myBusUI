package com.mybus.model;

public class BusTrip {
	Route route;
	Stop fromStop, toStop;
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
	
}
