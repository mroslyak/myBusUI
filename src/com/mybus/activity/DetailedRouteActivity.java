package com.mybus.activity;

import com.mybus.model.BusTrip;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedRouteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_detail);

		Bundle b = getIntent().getExtras();
		
		BusTrip trip = (BusTrip) b.getSerializable("selectedRoute");
		TextView text = (TextView) findViewById(R.id.test);
		text.setText(trip.getRoute().getName());
	}

}
