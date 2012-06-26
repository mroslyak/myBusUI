package com.mybus.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SetupRouteActivity extends Activity {
	static String APP_NAME ="myBus";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.setup_main);
        
	}
}
