package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import com.mybus.model.TrainTrip;
import com.mybus.model.Trip;
import com.mybus.model.RouteInfo;
import com.mybus.model.RouteName;
import com.mybus.model.Stop;
import com.mybus.service.BusPreferenceService;
import com.mybus.service.TrainLocatorService;
import com.mybus.service.TrainPreferenceService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TrainSetupRouteActivity extends Activity {

	TrainLocatorService service = new TrainLocatorService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.train_setup_main);

		Spinner lineSpinner = (Spinner) findViewById(R.id.train_line);

		List<RouteName> lines = service.getRoutes();
		ArrayAdapter<RouteName> dataAdapter = new ArrayAdapter<RouteName>(this,
				android.R.layout.simple_spinner_item, lines);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lineSpinner.setAdapter(dataAdapter);
		lineSpinner.setOnItemSelectedListener(new LineSelectedListener(this,"lines"));
		
		
		Spinner directionSpinner = (Spinner) findViewById(R.id.train_direction);
		directionSpinner.setOnItemSelectedListener(new LineSelectedListener(this,"direction"));
		
	}

	private class LineSelectedListener implements
			AdapterView.OnItemSelectedListener {
		Activity activity;
		String action;
		public LineSelectedListener(Activity activity,String action) {
			this.activity = activity;
			this.action = action;
		}

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TrainSetupBackgroundLoaderTask stopRoutesTask = new TrainSetupBackgroundLoaderTask(
					activity, action);
			stopRoutesTask.execute("");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class TrainSetupBackgroundLoaderTask extends
			AsyncTask<String, Void, List> {
		Activity activity;
		String actionSelected;

		public TrainSetupBackgroundLoaderTask(Activity activity, String action) {
			this.activity = activity;
			this.actionSelected = action;
		}

		@Override
		protected List doInBackground(String... notused) {
			if (actionSelected.equals("lines")) {
				Spinner lineSpinner = (Spinner) findViewById(R.id.train_line);
				RouteName selectedLine = (RouteName) lineSpinner.getSelectedItem();
				List<RouteInfo> list = service.getStops(selectedLine.getName());
				return list;
			}

			else if ( actionSelected.equals("direction")){
				Spinner directionSpinner = (Spinner) findViewById(R.id.train_direction);
				RouteInfo selectedRouteDirection = (RouteInfo) directionSpinner.getSelectedItem();
				List<Stop> stopList = selectedRouteDirection.getStopList();
				return stopList;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List result) {
			if (result == null) {
				return;
			}
			if (actionSelected.equals("lines")){
				Spinner directionSpinner = (Spinner) findViewById(R.id.train_direction);
				ArrayAdapter<RouteInfo> dataAdapter = new ArrayAdapter<RouteInfo>(activity,
						android.R.layout.simple_spinner_item, result);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				directionSpinner.setAdapter(dataAdapter);
			}
			if (actionSelected.equals("direction")) {
				Spinner stopSpinner = (Spinner) findViewById(R.id.train_stop);
				ArrayAdapter<RouteInfo> dataAdapter = new ArrayAdapter<RouteInfo>(activity,
						android.R.layout.simple_spinner_item, result);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				stopSpinner.setAdapter(dataAdapter);

			} else if (actionSelected.equals("direction")) {
				Spinner spinnerDirection = (Spinner) findViewById(R.id.direction);
				RouteInfo busRoute = (RouteInfo) spinnerDirection
						.getSelectedItem();

				Spinner spinner = (Spinner) findViewById(R.id.fromBusStop);
				ArrayAdapter dataAdapterFromStop = new ArrayAdapter(activity,
						android.R.layout.simple_spinner_item, busRoute
								.getStopList().subList(0,
										busRoute.getStopList().size() - 1));
				dataAdapterFromStop
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(dataAdapterFromStop);

			} 
		}
	}
	
	
	public void addTrainRoute(View view){
		Spinner lineSpinner = (Spinner) findViewById(R.id.train_line);
		Spinner directionSpinner = (Spinner) findViewById(R.id.train_direction);
		Spinner stopSpinner = (Spinner) findViewById(R.id.train_stop);

		Trip newTrainRoute = new TrainTrip();
		newTrainRoute.setRoute((RouteName) lineSpinner.getSelectedItem());
		newTrainRoute.setFromStop((Stop) stopSpinner.getSelectedItem());
		newTrainRoute.setToStop((RouteInfo)directionSpinner.getSelectedItem());

		TrainPreferenceService service = new TrainPreferenceService(this);
		service.saveRoute(newTrainRoute);
		Intent in = new Intent();
		setResult(2, in);
		finish();

		
	}
}
