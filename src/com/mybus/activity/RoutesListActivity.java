package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.model.RouteEstimate;

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
        List<RouteEstimate> userRoutes = new ArrayList<RouteEstimate>();

        //Load user saved routes from Shared Preferences.  format is fromStopId:fromStopName,toStopId:toStopName
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SetupRouteActivity.APP_NAME, MODE_PRIVATE);
        int i=0;
        while (true){
        	if (preferences.contains("route_"+i))
        		userRoutes.add(new RouteEstimate(preferences.getString("route_"+i, null)));
        	else
        		break;
        	i++;      	
        	
        }

      	setContentView(R.layout.routes_main);
        
       // userRoutes.add(new RouteEstimate());
      //  userRoutes.add(new RouteEstimate());
        
        
        ListView listView = (ListView) findViewById(R.id.list);
        //TextView setupButton = (TextView) findViewById(R.id.emptyLayout);
        RouteListRowAdapter routeAdapter = new RouteListRowAdapter(this, R.id.txtLoadingList,userRoutes );
        
        listView.setAdapter(routeAdapter);
        listView.setEmptyView(findViewById(R.id.list_empty));
        
    }
    
    
    public void showSetupActivity(View view){
    	startActivity(new Intent(getApplicationContext(),SetupRouteActivity.class));
    
    }
}