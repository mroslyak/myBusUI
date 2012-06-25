package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.model.RouteEstimate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class RoutesListActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_main);
        
        List<RouteEstimate> userRoutes = new ArrayList<RouteEstimate>();
        userRoutes.add(new RouteEstimate());
        userRoutes.add(new RouteEstimate());
        
        
        ListView listView = (ListView) findViewById(R.id.listView01);
        RouteListRowAdapter routeAdapter = new RouteListRowAdapter(this, R.layout.routes_main,userRoutes );
        listView.setAdapter(routeAdapter);
    }
}