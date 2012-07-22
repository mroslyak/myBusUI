package com.mybus.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mybus.activity.R;
import com.mybus.model.Trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


public class RouteListRowAdapter extends ArrayAdapter<Trip> {


	private Context context = null;
	private ArrayList<Trip> routeList = null;
	private LayoutInflater inflater = null;

	public RouteListRowAdapter(Context context,
			List<Trip> objects) {
		super(context, R.layout.routes_row,objects);
		
		this.context = context;
		this.routeList = (ArrayList<Trip>)objects;
		this.inflater = LayoutInflater.from(this.context);
	}
	
	@Override
	public int getCount() {
		if( routeList != null ){
			return routeList.size();
		}
		return 0;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		    View view = inflater.inflate(R.layout.routes_row, null);
            
		    Trip trip = routeList.get(position);
            TextView startingStopTV = (TextView) view.findViewById(R.id.fromStopId);
            TextView goingToTV = (TextView) view.findViewById(R.id.toStopId);
            TextView estimateTV = (TextView) view.findViewById(R.id.nextBusEstimate);
            

            startingStopTV.setText( trip.getFromStop().getName() );
            goingToTV.setText(trip.getToStop().getName());
            
            if (trip.getNextArrivalTime() == null)
            	estimateTV.setText("");
            else
            	estimateTV.setText(trip.getNextArrivalTime() +" min");
            
            
            TextView textView = (TextView) view.findViewById(R.id.nextBusNumber);
            Button btn = (Button)view.findViewById(R.id.moreDetailLink);
            if (trip.getNextArrivalBusNumber() == null){
            	textView.setText("No Predictions");
            	btn.setVisibility(View.GONE);
            }else{
            	textView.setText(trip.getNextArrivalBusNumber());
            	btn.setVisibility(View.VISIBLE);
            }
            
            btn.setTag(new Integer(position));
            
            btn.setLongClickable(true);
            return view;		
	}

	
	
	
}
