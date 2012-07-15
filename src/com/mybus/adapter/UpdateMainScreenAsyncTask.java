package com.mybus.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;

import com.mybus.model.BusTrip;
import com.mybus.service.BusLocatorService;

public class  UpdateMainScreenAsyncTask extends AsyncTask<String, Void, String>{
	BusLocatorService service = new BusLocatorService();
	RouteListRowAdapter routeAdapter;
	public UpdateMainScreenAsyncTask(RouteListRowAdapter adapter){
		this.routeAdapter = adapter;
	}
	@Override
	protected String doInBackground(String... params) {
		for (int i = 0; i < routeAdapter.getCount(); i++) {
			BusTrip trip = (BusTrip) routeAdapter.getItem(i);
			
			trip.setEstimatedArrival(service.getPredictionInformation(trip.getFromStop().getStopId(), trip.getToStop().getStopId()));
		}
		return " ";
	}
	@Override
	protected void onPostExecute(String result) {
		routeAdapter.notifyDataSetChanged();
	}
}
