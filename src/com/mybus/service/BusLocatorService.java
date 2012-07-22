package com.mybus.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mybus.model.RouteInfo;
import com.mybus.model.RouteName;
import com.mybus.model.Stop;

public class BusLocatorService extends LocatorService{
	
	/**
	 * Gets List of available routes for mbta right now.
	 * @return
	 */
	public List<RouteName> getRoutes(){
		List<RouteName> routes = new ArrayList<RouteName>();
		String routesJson = getJsonString(serverName+"/busInfo/getRoutes/mbta");
		
		try{
			JSONObject json = new JSONObject(routesJson);
			JSONArray array = json.getJSONArray("routes");
			for (int i=0; i< array.length();i++){
				json = array.getJSONObject(i);
				routes.add(new RouteName(json.getString("tag"), json.getString("title")));
			}
		}catch(Exception e){
			Log.e(BusLocatorService.class.toString(),e.getLocalizedMessage());
			
		}
		return routes;
	}
	
	/**
	 * For a particular route get a list of stops
	 * @param routeTag
	 * @return
	 */
	public List<RouteInfo> getStops(String routeTag){
		List<RouteInfo> busRouteList = new ArrayList<RouteInfo>();
		String stopsJson = getJsonString(serverName+"/busInfo/getStops/mbta/"+routeTag);
	
		try{
			
			JSONArray jsonDirectionArray = new JSONArray(stopsJson);
			for (int i=0;i<jsonDirectionArray.length();i++){
				RouteInfo busRoute = new RouteInfo();
				JSONObject directionJson = jsonDirectionArray.getJSONObject(i);
				String directionTitle = directionJson.getString("direction");
				busRoute.setDirection(directionTitle);
				busRouteList.add(busRoute);
				JSONArray jsonStopList = directionJson.getJSONArray("stopList");
				for (int j=0; j<jsonStopList.length();j++){
					JSONObject jsonStop = jsonStopList.getJSONObject(j);
					Stop stop = new Stop(jsonStop.getString("tag"),jsonStop.getString("name"));
					
					busRoute.getStopList().add(stop);
				}
			}
		
		}catch(Exception e){
			Log.e(BusLocatorService.class.toString(),e.getLocalizedMessage());
			
		}		
		
		return busRouteList; 
	}
	
	public Map<String,String> getPredictionInformation(String fromBusTag,String toBusTag){
		String jsonStr = getJsonString(serverName+"/busInfo/getEstimate/mbta/"+fromBusTag +"/"+toBusTag);
		Map<String,String> estimateList = new HashMap<String,String>();
		try{
			JSONObject estimatejson = new JSONObject(jsonStr);
			JSONArray routeNames = estimatejson.names();
			if (routeNames == null){
				return estimateList;
			}
			
			for (int i=0; i< routeNames.length(); i++){
				String busNumber = (String)routeNames.get(i);
				String busTime = estimatejson.getString(busNumber);
				estimateList.put(busNumber ,busTime);
			}
			
		}catch(Exception e){
			Log.e(BusLocatorService.class.toString(),e.getLocalizedMessage());
			
		}	
		return estimateList;
	}
	
	
}
