package com.mybus.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TrainSetupRouteActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.train_setup_main);
		
		Spinner lineSpinner = (Spinner) findViewById(R.id.train_line);
		
		List<String> lines = new ArrayList<String>();
		lines.add("red");
		lines.add("blue");
		lines.add("orange");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lines);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		lineSpinner.setAdapter(dataAdapter);
	}
	
	

}
