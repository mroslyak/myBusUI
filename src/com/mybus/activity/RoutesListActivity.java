package com.mybus.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.adapter.UpdateMainScreenAsyncTask;
import com.mybus.model.BusTrip;
import com.mybus.service.SavedRoutesService;

public class RoutesListActivity extends ListActivity {
	SavedRoutesService savedRoutesService;
	ScheduledExecutorService executorService;
	// used for context menu to know which item in the listview was clicked.
	int clickedRoutePosition;
	Button buton;
	private Handler mHandler = new Handler();
	private Timer refreshScreenTimer;
	RouteListRowAdapter routeAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		savedRoutesService = new SavedRoutesService(this);
		List<BusTrip> userRoutes = savedRoutesService.getRoutes();

		setContentView(R.layout.routes_main);

		routeAdapter = new RouteListRowAdapter(this, userRoutes);
		setListAdapter(routeAdapter);

		getListView().setLongClickable(true);
		registerForContextMenu(getListView());

	}

	@Override
	public void onResume() {
		super.onResume();
		if (haveNetworkConnection() == false) {
			Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG);
		} else {
			refreshScreenTimer = new Timer();
			refreshScreenTimer
					.schedule(new RefreshListTimer(this), 1000, 30000);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		clickedRoutePosition = info.position;

		menu.setHeaderTitle("Action");
		menu.add(0, v.getId(), 0, "Remove");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Remove") {
			savedRoutesService.deleteRoute(clickedRoutePosition);

			BusTrip trip = routeAdapter.getItem(clickedRoutePosition);
			routeAdapter.remove(trip);
			routeAdapter.notifyDataSetChanged();
			Toast.makeText(this, "Removed", 2000).show();

		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST, 0, "Bus Route Setup").setShortcut('0', 'b');
		menu.add(0, Menu.FIRST+1, 0, "MBTA Train Setup").setShortcut('1', 't');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getTitle().equals("Setup")) {
		if (item.getItemId() == Menu.FIRST){
			showBusSetupActivity(null);

			return true;
		}
		if (item.getItemId() == (Menu.FIRST +1)){
			Toast.makeText(this, "adding mbta", Toast.LENGTH_LONG);
			showTrainSetupActivity(null);
		}

		return super.onOptionsItemSelected(item);
	}

	public void showBusSetupActivity(View view) {
		startActivityForResult((new Intent(getApplicationContext(),
				BusSetupRouteActivity.class)), 1);

	}
	
	public void showTrainSetupActivity(View view){
		startActivityForResult(new Intent(getApplicationContext(),TrainSetupRouteActivity.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("CheckStartActivity", "onActivityResult and resultCode = "
				+ resultCode);

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			if (savedRoutesService.getRoutesCount() > routeAdapter.getCount()) {
				List<BusTrip> updatedTrips = savedRoutesService.getRoutes();
				routeAdapter.add(updatedTrips.get(updatedTrips.size() - 1));
				routeAdapter.notifyDataSetChanged();
				Toast.makeText(this, "Added", 2000).show();

			}
		} else {
			Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
		}
	}

	private class RefreshListTimer extends TimerTask {
		Activity activity;

		public RefreshListTimer(Activity activity) {
			this.activity = activity;
		}

		public void run() {

			mHandler.post(new Runnable() {
				public void run() {
					new UpdateMainScreenAsyncTask(routeAdapter).execute("");
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (refreshScreenTimer != null)
			refreshScreenTimer.cancel();

	}

	
	/**
	 * Check if there is network connection
	 * @return
	 */
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}
	
	
	public void showDetailedRoute(View view){
		
		Toast.makeText(this, "yes "+view.getTag(), Toast.LENGTH_SHORT);
		BusTrip trip = routeAdapter.getItem(( Integer)view.getTag());
		
		Intent detailIntent = new Intent(getApplicationContext(),DetailedRouteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("selectedRoute", trip);
		detailIntent.putExtras(bundle);
		startActivity(detailIntent);

	}
}