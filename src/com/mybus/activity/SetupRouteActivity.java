package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import com.mybus.model.Route;
import com.mybus.service.BusLocatorService;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SetupRouteActivity extends Activity {
	static String APP_NAME ="myBus";
    private BusLocatorService service = new BusLocatorService();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.setup_main);
        
        
        Spinner spinner = (Spinner) findViewById(R.id.fromBusStop);
        spinner.setPrompt("Pick starting stop");
        List<Route> list = service.getRoutes();
    	ArrayAdapter<Route> dataAdapter = new ArrayAdapter<Route>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
        spinner.setAdapter(dataAdapter);
	}
}
