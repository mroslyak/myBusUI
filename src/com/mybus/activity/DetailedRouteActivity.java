package com.mybus.activity;

import java.util.Map;

import com.mybus.model.BusTrip;
import com.mybus.model.TrainTrip;
import com.mybus.model.Trip;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailedRouteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_detail);

		Bundle b = getIntent().getExtras();
		
		Trip trip = (Trip) b.getSerializable("selectedRoute");
		TextView text = (TextView) findViewById(R.id.fromStopIdDetail);
		text.setText(trip.getFromStop().getName());
		
		text = (TextView) findViewById(R.id.toStopIdDetail);
		text.setText(trip.getToStop().getName());
		
		
		TableLayout routeEstimateTable = (TableLayout) findViewById(R.id.routeTableEstimates);
		populateRouteEstimates(routeEstimateTable,trip);
	}
	
	private void populateRouteEstimates(TableLayout table,Trip trip ){
		
		Map<String,String> estimates = trip.getEstimatedArrivalMap();
		
		int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				                (float) 1, getResources().getDisplayMetrics());
		if (estimates == null){
			finish();
			return;
		}

		TableRow headerRow = new TableRow(this);
		
		TextView header1 = new TextView(this);
		if (trip instanceof BusTrip)
			header1.setText("Bus #");
		else
			header1.setText("Train:");
		
		TextView header2 = new TextView(this);
		header2.setText("Arriving in");
		header2.setTextColor(Color.BLACK);
		header1.setTextColor(Color.BLACK);
		header1.setPadding(20*dip, 10*dip, 0, 0);
		headerRow.addView(header1);
		headerRow.addView(header2);
		table.addView(headerRow, new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		for (String bus:estimates.keySet()){
			if(bus.startsWith("next"))
				continue;
			String times = estimates.get(bus);
			
			TableRow row = new TableRow(this);
			
			TextView busView = new TextView(this);
			busView.setText(bus);
			
			TextView timeView = new TextView(this);
			timeView.setText(times +" min");
		    busView.setTypeface(null, 1);
		    timeView.setTypeface(null, 1);
		                busView.setTextSize(15);
		                timeView.setTextSize(15);
		                busView.setWidth(250 * dip);
		                timeView.setWidth(200 * dip);
		                busView.setPadding(20*dip, 0, 0, 0);

			row.addView(busView);
			row.addView(timeView);
			table.addView(row, new TableLayout.LayoutParams(
	                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		}
	}

}
