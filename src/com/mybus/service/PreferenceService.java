package com.mybus.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mybus.model.Trip;

public abstract class PreferenceService<T> {
	SharedPreferences preferences;
	Context context ;

	public PreferenceService(Activity activity){
		preferences = activity.getSharedPreferences("MyBus",Context.MODE_PRIVATE );
		context = activity.getApplicationContext();

	}
	public abstract String getPreferencePrefix();
	
	public  int getRoutesCount(){
		return preferences.getInt(getPreferencePrefix()+"_count", 0);
	}
	
	
	public abstract List<T> getRoutes();
	
	public abstract void saveRoute(T trip);
	
	public List<String> getRoutesStr(){
		List<String> tripList = new ArrayList<String>();
		
		int routeCount = getRoutesCount();
		for (int i=0;i< routeCount; i++){
			String tripStr = preferences.getString(getPreferencePrefix()+i, null);
			if (tripStr != null){
				tripList.add(tripStr);
			}
		}
		
		return tripList;
	}
	
	public void deleteRoute(int index){

		int routeCount = getRoutesCount();
		
		
		Editor editor = preferences.edit();
		for (int i=index; i< routeCount; i++){
			editor.remove(getPreferencePrefix()+i);
			String str = preferences.getString(getPreferencePrefix()+(i+1), null);
			if (str != null){
				editor.putString(getPreferencePrefix()+i, str);
			}
		}
		editor.putInt(getPreferencePrefix() +"_count", routeCount-1);

		editor.commit();	
	}
	
	public void saveRouteStr(String trip){
		int routeCount = getRoutesCount();
		Editor editor = preferences.edit();
		editor.putInt(getPreferencePrefix() +"_count", routeCount+1);
		editor.putString(getPreferencePrefix()+routeCount, trip.toString());
		editor.apply();
	}
	
}
