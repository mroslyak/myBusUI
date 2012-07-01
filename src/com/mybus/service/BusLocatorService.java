package com.mybus.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import com.mybus.model.Route;
import com.mybus.model.Stop;

public class BusLocatorService {
	private String serverName = "http://23.21.134.13:9000";
	
	/**
	 * Gets List of available routes for mbta right now.
	 * @return
	 */
	public List<Route> getRoutes(){
		String routesJson = getJsonString(serverName+"/busInfo/getRoutes/mbta");
		List<Route> routes = new ArrayList<Route>();
		try{
			JSONObject json = new JSONObject(routesJson);
			JSONArray array = json.getJSONArray("routes");
			for (int i=0; i< array.length();i++){
				json = array.getJSONObject(i);
				routes.add(new Route(json.getString("tag"), json.getString("title")));
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
	public List<Stop> getStops(String routeTag){
		List<Stop> stops = new ArrayList<Stop>();
		
		stops.add(new Stop("1","test Stop"));
		stops.add(new Stop("2","test Stop2"));
		
		return stops; 
	}
	
	/**
	 * connects to REST WS and returns body as String
	 * @param url
	 * @return
	 */
	private String getJsonString(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(BusLocatorService.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
