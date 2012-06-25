package com.mybus.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mybus.activity.R;
import com.mybus.model.RouteEstimate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class RouteListRowAdapter extends ArrayAdapter<RouteEstimate> {

	private Context context = null;
	private ArrayList<RouteEstimate> routeList = null;
	private LayoutInflater inflater = null;

	public RouteListRowAdapter(Context context, int textViewResourceId,
			List<RouteEstimate> objects) {
		super(context, textViewResourceId, objects);
		
		this.context = context;
		this.routeList = (ArrayList<RouteEstimate>)objects;
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
            
            TextView textView = (TextView) view.findViewById(R.id.fromStopId);
            textView.setText( "from" );
            textView = (TextView) view.findViewById(R.id.toStopId);
            textView.setText("some place nice");
            
            textView = (TextView) view.findViewById(R.id.timeEstimate);
            textView.setText("5 minutes");
            return view;
		
		
	}
}
