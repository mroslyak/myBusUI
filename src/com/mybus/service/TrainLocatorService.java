package com.mybus.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mybus.model.RouteInfo;
import com.mybus.model.RouteName;
import com.mybus.model.Stop;

public class TrainLocatorService extends LocatorService{

	
	public List<RouteName> getRoutes(){
		List<RouteName> routes = new ArrayList<RouteName>();
		routes.add(new RouteName("red", "red"));
		routes.add(new RouteName("orange", "orange"));
		routes.add(new RouteName("blue", "blue"));
		return routes;
	}
	
	
	public List<RouteInfo> getStops(String routeTag){
		List<RouteInfo> trainRouteList = new ArrayList<RouteInfo>();
		String responseJson = getJsonString(serverName+"/trainInfo/stops/"+routeTag);
	
		try{
			
			JSONObject jsonDirections = new JSONObject(responseJson);
			Iterator<String> directionIter = jsonDirections.keys();
			while (directionIter.hasNext()){
				RouteInfo trainRoute = new RouteInfo();
				String direction = directionIter.next();
				trainRoute.setDirection(direction);
				List<Stop> stopList = new ArrayList<Stop>();
				JSONArray stopsJson = jsonDirections.getJSONArray(direction);
				for (int i=0; i< stopsJson.length(); i++){
					String tag= stopsJson.getJSONObject(i).getString("tag");
					String title = stopsJson.getJSONObject(i).getString("name");
					stopList.add(new Stop(tag,title));
				}
				trainRoute.setStopList(stopList);
				trainRouteList.add(trainRoute);
			}
		
		}catch(Exception e){
			Log.e(BusLocatorService.class.toString(),e.getLocalizedMessage());
			
		}		
		
		return trainRouteList; 
	}
}
