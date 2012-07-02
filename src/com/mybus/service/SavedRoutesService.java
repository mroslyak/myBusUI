package com.mybus.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.mybus.model.BusTrip;

public class SavedRoutesService {
	SharedPreferences preferences;
	Context context ;
	private static final String ROUTE_COUNT_NAME="Routes";
	private static final String ROUTE_INFO="RouteInfo_";
	
	
	public SavedRoutesService(Activity activity){
		preferences = activity.getSharedPreferences("MyBus",Context.MODE_PRIVATE );
		context = activity.getApplicationContext();
	}
	
	public int getRoutesCount(){
		return preferences.getInt(ROUTE_COUNT_NAME, 0);

	}
	
	public void saveRoute(BusTrip trip){
		int routeCount = getRoutesCount();
		Editor editor = preferences.edit();
		editor.putInt(ROUTE_COUNT_NAME, routeCount+1);
		editor.putString(ROUTE_INFO+routeCount, trip.toString());
		editor.apply();
		Toast.makeText(context, "Saved", 2000).show();
	}
	
	public List<BusTrip> getRoutes(){
		List<BusTrip> tripList = new ArrayList<BusTrip>();
		
		int routeCount = getRoutesCount();
		for (int i=0;i< routeCount; i++){
			String tripStr = preferences.getString(ROUTE_INFO+i, null);
			if (tripStr != null){
				tripList.add(new BusTrip(tripStr));
			}
		}
		
		return tripList;
	}
	
	public void deleteRoute(){
		Toast.makeText(context, "Deleted ToDO", 2000).show();
		
	}
	
}
