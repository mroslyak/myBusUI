package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.model.BusTrip;
import com.mybus.model.RouteEstimate;
import com.mybus.service.SavedRoutesService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class RoutesListActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SavedRoutesService savedRoutesService = new SavedRoutesService(this);
        List<BusTrip> userRoutes = savedRoutesService.getRoutes();
       
      	setContentView(R.layout.routes_main);
        
        ListView listView = (ListView) findViewById(R.id.list);
        //TextView setupButton = (TextView) findViewById(R.id.emptyLayout);
        RouteListRowAdapter routeAdapter = new RouteListRowAdapter(this, R.id.txtLoadingList,userRoutes );
        
        listView.setAdapter(routeAdapter);
        listView.setEmptyView(findViewById(R.id.list_empty));
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	  SavedRoutesService savedRoutesService = new SavedRoutesService(this);
          List<BusTrip> userRoutes = savedRoutesService.getRoutes();
         
        	setContentView(R.layout.routes_main);
          
          ListView listView = (ListView) findViewById(R.id.list);
          //TextView setupButton = (TextView) findViewById(R.id.emptyLayout);
          RouteListRowAdapter routeAdapter = new RouteListRowAdapter(this, R.id.txtLoadingList,userRoutes );
          
          listView.setAdapter(routeAdapter);
    }
    
    public void showSetupActivity(View view){
    	startActivity(new Intent(getApplicationContext(),SetupRouteActivity.class));
    	
    }
}