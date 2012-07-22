package com.mybus.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.mybus.model.BusTrip;
import com.mybus.model.Trip;

public class BusPreferenceService extends PreferenceService<Trip> {
	@Override
	public String getPreferencePrefix() {
		// TODO Auto-generated method stub
		return "Bus_Routes";
	}
	
	
	public BusPreferenceService(Activity activity){
		super(activity);
	}
	
	
	public List<Trip> getRoutes(){
		List<Trip> tripList = new ArrayList<Trip>();
		
		List<String> tripStrList = getRoutesStr();
		for (String tripStr:tripStrList){
				tripList.add(new BusTrip(tripStr));	
		}		
		return tripList;
	}
	
	
	public void deleteRoute(int index){
		List<Trip> tripList = getRoutes();
		tripList.remove(index);
		preferences.edit().clear().apply();
		
		for (Trip trip:tripList){
			saveRoute(trip);
		}
			
	}
	
	public void saveRoute(Trip trip){
		saveRouteStr(trip.toString());
	}

	
}
