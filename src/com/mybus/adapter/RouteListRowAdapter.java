package com.mybus.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mybus.activity.R;
import com.mybus.model.BusTrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RouteListRowAdapter extends ArrayAdapter<BusTrip> {


	private Context context = null;
	private ArrayList<BusTrip> routeList = null;
	private LayoutInflater inflater = null;

	public RouteListRowAdapter(Context context,
			List<BusTrip> objects) {
		super(context, R.layout.routes_row,objects);
		
		this.context = context;
		this.routeList = (ArrayList<BusTrip>)objects;
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
           // ImageView image = (ImageView) view.findViewById(R.id.rowImage);
           // image.setImageResource(R.drawable.ic_launcher);
            
		    BusTrip trip = routeList.get(position);
            TextView textView = (TextView) view.findViewById(R.id.fromStopId);
            textView.setText( trip.getFromStop().getName() );
            textView = (TextView) view.findViewById(R.id.toStopId);
            textView.setText(trip.getToStop().getName());
            
            textView = (TextView) view.findViewById(R.id.timeEstimate);
            
            textView.setText(trip.getEstimatedArrival());
            return view;		
	}

	
	
	
}
