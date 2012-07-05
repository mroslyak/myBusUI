package com.mybus.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.model.BusTrip;
import com.mybus.model.RouteEstimate;
import com.mybus.service.SavedRoutesService;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RoutesListActivity extends ListActivity {
	SavedRoutesService savedRoutesService;
    ScheduledExecutorService executorService;
    // used for context menu to know which item in the listview was clicked.
    int clickedRoutePosition; 
    Button buton;  
    //handler to update estimated arrival time 
    final Handler mHandler = new Handler();
    ListView routeList;
    TextView text ;
    RouteListRowAdapter routeAdapter;
    List<BusTrip> userRoutes;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedRoutesService = new SavedRoutesService(this);
        userRoutes = savedRoutesService.getRoutes();
       
      	setContentView(R.layout.routes_main);
        
        routeAdapter = new RouteListRowAdapter(this, userRoutes );
        setListAdapter(routeAdapter);
        
           
        executorService = Executors.newScheduledThreadPool(1);
        Runnable refreshRouteThread = new RefreshScheduleThread(routeList,routeAdapter,userRoutes);
        executorService.scheduleWithFixedDelay(refreshRouteThread, 20, 30, TimeUnit.SECONDS);

        getListView().setLongClickable(true);
        registerForContextMenu(getListView());
    }
 
    
    
    @Override
    public void onResume(){
    	super.onResume();

    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	
    	AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
    	clickedRoutePosition = info.position;
       
        menu.setHeaderTitle("Action");  
        menu.add(0, v.getId(), 0, "Remove");  
    }  


    
    @Override
    public boolean onContextItemSelected(MenuItem item) {  
        if(item.getTitle()=="Remove"){
        	savedRoutesService.deleteRoute(clickedRoutePosition);
        	
        	BusTrip trip = routeAdapter.getItem(clickedRoutePosition);
        	routeAdapter.remove(trip);
        	routeAdapter.notifyDataSetChanged();
    		Toast.makeText(this, "Removed", 2000).show();

        }else {return false;}  
        return true;  
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, 0, "Setup").setShortcut('0', 's');

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Setup")){
        	showSetupActivity(null);
        	
        	return true;
        }
     
        return super.onOptionsItemSelected(item);
    }
    public void showSetupActivity(View view){
    	startActivityForResult((new Intent(getApplicationContext(),SetupRouteActivity.class)),1);   	
    	
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CheckStartActivity","onActivityResult and resultCode = "+resultCode);
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
        	if (savedRoutesService.getRoutesCount() > routeAdapter.getCount()){
        		List<BusTrip> updatedTrips = savedRoutesService.getRoutes();
        		routeAdapter.add(updatedTrips.get(updatedTrips.size()-1));
        		routeAdapter.notifyDataSetChanged();
        		Toast.makeText(this, "Added", 2000).show();

        	}
        }
        else{
            Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
        }
    }
    public class RefreshScheduleThread extends Thread{
    	ListView list;
    	RouteListRowAdapter routeAdapter2;
    	List<BusTrip> userTrips;
    	public RefreshScheduleThread(ListView routeList, RouteListRowAdapter adapter, List<BusTrip> trips){
    		this.list = routeList;
    		routeAdapter2 = adapter;
    		userTrips = trips;
    	}
    	
		@Override
		public void run() {
			super.run();
			mHandler.post(new Runnable(){
				public void run(){
			try{
					
					for (int i=0; i< routeAdapter.getCount();i++){
						List<String> times = new ArrayList<String>();
						times.add(new Date().getSeconds()+"");
						BusTrip trip = (BusTrip)routeAdapter.getItem(i);
						trip.setEstimatedArrival(times);
					}
					routeAdapter.notifyDataSetChanged();
					
			}catch(Exception e){
				e.printStackTrace();
				Log.e("App","error",e);
			}
				}
			});
			
		}
    	
    	
    }
}