package com.mybus.service;

import java.util.ArrayList;
import java.util.List;

import com.mybus.model.TrainTrip;
import com.mybus.model.Trip;

import android.app.Activity;

public class TrainPreferenceService extends PreferenceService<Trip>{

	public TrainPreferenceService(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getPreferencePrefix() {
		// TODO Auto-generated method stub
		return "Train_Routes";
	}
	

	public List<Trip> getRoutes(){
		List<Trip> tripList = new ArrayList<Trip>();
		
		List<String> tripStrList = getRoutesStr();
		for (String tripStr:tripStrList){
				tripList.add(new TrainTrip(tripStr));	
		}		
		return tripList;
	}
	
	
	
	
	public void saveRoute(Trip trip){
		saveRouteStr(trip.toString());
	}

}
