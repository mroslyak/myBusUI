package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mybus.model.BusRoute;
import com.mybus.model.BusTrip;
import com.mybus.model.Route;
import com.mybus.model.Stop;
import com.mybus.service.BusLocatorService;
import com.mybus.service.SavedRoutesService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

public class BusSetupRouteActivity extends Activity {
	static String APP_NAME = "myBus";
	private BusLocatorService service = new BusLocatorService();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setup_main);
		BackgroundLoaderTask busRoutesTask = new BackgroundLoaderTask(this,"routes");
		busRoutesTask.execute("");

		Spinner routespinner = (Spinner) findViewById(R.id.route);
		routespinner.setOnItemSelectedListener(new RouteSelectedListener(this));
		
		Spinner directionspinner = (Spinner) findViewById(R.id.direction);
		directionspinner.setOnItemSelectedListener(new DirectionSelectedListener(this));
	}

	public void addRoute(View view) {
		Spinner routeSpinner = (Spinner) findViewById(R.id.route);
		Spinner fromStopSpinner = (Spinner) findViewById(R.id.fromBusStop);
		Spinner toStopSpinner = (Spinner) findViewById(R.id.toBusStop);

		BusTrip newBusRoute = new BusTrip();
		newBusRoute.setRoute((Route) routeSpinner.getSelectedItem());
		newBusRoute.setFromStop((Stop) fromStopSpinner.getSelectedItem());
		newBusRoute.setToStop((Stop) toStopSpinner.getSelectedItem());

		SavedRoutesService service = new SavedRoutesService(this);
		service.saveRoute(newBusRoute);
		Intent in = new Intent();
		setResult(1, in);
		finish();

	}

	private class DirectionSelectedListener implements AdapterView.OnItemSelectedListener {
		Activity activity;

		public DirectionSelectedListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			BackgroundLoaderTask stopRoutesTask = new BackgroundLoaderTask(activity, "direction");
			stopRoutesTask.execute("");
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	private class RouteSelectedListener implements AdapterView.OnItemSelectedListener {
		Activity activity;

		public RouteSelectedListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			BackgroundLoaderTask stopRoutesTask = new BackgroundLoaderTask(activity, "stops");
			stopRoutesTask.execute("");
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	private class BackgroundLoaderTask extends AsyncTask<String, Void, List> {
		Activity activity;
		String action;

		public BackgroundLoaderTask(Activity activity, String action) {
			this.activity = activity;
			this.action = action;
		}

		@Override
		protected List doInBackground(String... notused) {
			if (action.equals("routes")) {
				List<Route> list = service.getRoutes();
				return list;
			} else if (action.equals("stops")) {
				Spinner routeSpinner = (Spinner) findViewById(R.id.route);
				Route selectedRoute = (Route) routeSpinner.getSelectedItem();
				List<BusRoute> list = service.getStops(selectedRoute.getTag());
				return list;
			} else if (action.equals("direction"))
				return new ArrayList();
			return null;
		}

		@Override
		protected void onPostExecute(List result) {
			if (result == null) {
				return;
			}
			if (action.equals("routes")) {
				Spinner spinner = (Spinner) findViewById(R.id.route);
				spinner.setPrompt("Choose Your Bus Route");
				@SuppressWarnings({ "unchecked", "rawtypes" })
				ArrayAdapter dataAdapter = new ArrayAdapter(activity,
						android.R.layout.simple_spinner_item, result);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapter);
			} else if (action.equals("stops")) {
				List<BusRoute> busRoutes = (List<BusRoute>) result;
				//currentBusRoutes = busRoutes;
				Spinner spinnerDirection = (Spinner) findViewById(R.id.direction);
				ArrayAdapter dataAdapter = new ArrayAdapter(activity,
						android.R.layout.simple_spinner_item, busRoutes);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerDirection.setAdapter(dataAdapter);

				Spinner spinner = (Spinner) findViewById(R.id.fromBusStop);
				ArrayAdapter dataAdapterFromStop = new ArrayAdapter(activity,
						android.R.layout.simple_spinner_item, busRoutes.get(0)
								.getStopList());
				dataAdapterFromStop
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapterFromStop);

				Spinner spinner2 = (Spinner) findViewById(R.id.toBusStop);
				spinner2.setAdapter(dataAdapterFromStop);

			}else if (action.equals("direction")){
				Spinner spinnerDirection = (Spinner) findViewById(R.id.direction);
				BusRoute busRoute = (BusRoute)spinnerDirection.getSelectedItem();
				Spinner spinner = (Spinner) findViewById(R.id.fromBusStop);
				ArrayAdapter dataAdapterFromStop = new ArrayAdapter(activity,
						android.R.layout.simple_spinner_item, busRoute.getStopList());
				dataAdapterFromStop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapterFromStop);

				Spinner spinner2 = (Spinner) findViewById(R.id.toBusStop);
				spinner2.setAdapter(dataAdapterFromStop);
				
			}
		}
	}
}
