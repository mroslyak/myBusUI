package com.mybus.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;

import com.mybus.model.BusTrip;
import com.mybus.model.TrainTrip;
import com.mybus.model.Trip;
import com.mybus.service.BusLocatorService;
import com.mybus.service.TrainLocatorService;

public class  UpdateMainScreenAsyncTask extends AsyncTask<String, Void, String>{
	BusLocatorService busService = new BusLocatorService();
	TrainLocatorService trainService = new TrainLocatorService();
	RouteListRowAdapter routeAdapter;
	public UpdateMainScreenAsyncTask(RouteListRowAdapter adapter){
		this.routeAdapter = adapter;
	}
	@Override
	protected String doInBackground(String... params) {
		for (int i = 0; i < routeAdapter.getCount(); i++) {
			Trip trip = (Trip) routeAdapter.getItem(i);
			
			if (trip instanceof BusTrip)
				trip.setEstimatedArrival(busService.getPredictionInformation(trip.getFromStop().getStopId(), trip.getToStop().getStopId()));
			
			if (trip instanceof TrainTrip)
				trip.setEstimatedArrival(trainService.getPredictionInformation((TrainTrip)trip));
		}
		return " ";
	}
	@Override
	protected void onPostExecute(String result) {
		routeAdapter.notifyDataSetChanged();
	}
}
