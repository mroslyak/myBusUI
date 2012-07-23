package com.mybus.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import com.mybus.adapter.RouteListRowAdapter;
import com.mybus.adapter.UpdateMainScreenAsyncTask;
import com.mybus.model.BusTrip;
import com.mybus.model.Trip;
import com.mybus.service.BusPreferenceService;
import com.mybus.service.PreferenceService;
import com.mybus.service.TrainPreferenceService;

public class RoutesListActivity extends SherlockListActivity implements ActionBar.TabListener, OnNavigationListener {
	PreferenceService<Trip> busPreferenceService, trainPreferenceService;
	int lastestRouteTypeIndex;
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

		busPreferenceService = new BusPreferenceService(this);
		trainPreferenceService = new TrainPreferenceService(this);

		List<Trip> userBusRoutes = new ArrayList<Trip>();//busPreferenceService.getRoutes();
	//	List<Trip> userTrainRoutes = trainPreferenceService.getRoutes();
		
//		userBusRoutes.addAll(userTrainRoutes);
		setContentView(R.layout.routes_main);

		routeAdapter = new RouteListRowAdapter(this,  userBusRoutes);
		setListAdapter(routeAdapter);

		getListView().setLongClickable(true);

		registerForContextMenu(getListView());

		ActionBar ab = getSupportActionBar();
		Context context = ab.getThemedContext();

		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.routeTypes, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ab.setListNavigationCallbacks(list, this);
		ab.setCustomView(R.layout.action_layout);

	}

	@Override
	public void onResume() {
		super.onResume();
		if (haveNetworkConnection() == false) {
			Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG).show();
			for (int i = 0; i < routeAdapter.getCount(); i++) {
				routeAdapter.getItem(i).setEstimatedArrival(new HashMap<String, String>());
			}
		} else {
			refreshScreenTimer = new Timer();
			refreshScreenTimer.schedule(new RefreshListTimer(this), 1000, 30000);
		}

	}

	public void setup(View v) {
		Toast.makeText(getApplicationContext(), "Clicked ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		clickedRoutePosition = info.position;

		menu.setHeaderTitle("Action");
		menu.add(0, v.getId(), 0, "Remove");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Remove") {
			Trip trip = routeAdapter.getItem(clickedRoutePosition);
			if (trip instanceof BusTrip)
				busPreferenceService.deleteRoute(clickedRoutePosition);
			else
				trainPreferenceService.deleteRoute(clickedRoutePosition);

			routeAdapter.remove(trip);
			routeAdapter.notifyDataSetChanged();
			Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();

		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		// MenuInflater inflater = getSupportMenuInflater();
		// inflater.inflate(R.menu.tabs, menu);
		com.actionbarsherlock.view.MenuItem item = menu.add("Setup");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		item.setOnMenuItemClickListener(new SetupClickListener(this));

		return super.onCreateOptionsMenu(menu);

	}

	/**
	 * Dialog for choosing the right setup screen
	 * 
	 * @author mroslyakov
	 * 
	 */
	public class SetupClickListener implements com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener {
		Context context;

		public SetupClickListener(Context context) {
			this.context = context;
		}

		@Override
		public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
			final CharSequence[] items = { "Bus", "Train" };

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Pick Type.");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if ("Bus".equals(items[item]))
						showBusSetupActivity(null);
					if ("Train".equals(items[item]))
						showTrainSetupActivity(null);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
	}

	public void showBusSetupActivity(View view) {
		startActivityForResult((new Intent(getApplicationContext(), BusSetupRouteActivity.class)), 1);

	}

	public void showTrainSetupActivity(View view) {
		startActivityForResult(new Intent(getApplicationContext(), TrainSetupRouteActivity.class), 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode > 0) {
			reloadRouteList(lastestRouteTypeIndex);
			Toast.makeText(this, "Added", 2000).show();

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
	 * 
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

	public void showDetailedRoute(View view) {
		Trip trip = routeAdapter.getItem((Integer) view.getTag());

		Intent detailIntent = new Intent(getApplicationContext(), DetailedRouteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("selectedRoute", trip);
		detailIntent.putExtras(bundle);
		startActivity(detailIntent);

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		lastestRouteTypeIndex =itemPosition;
		reloadRouteList(itemPosition);
		
		return true;
	}

	private void reloadRouteList(int position) {
		List<Trip> updatedTrips = null;

		ActionBar bar = getSupportActionBar();
		Spinner spinner = (Spinner) bar.getCustomView().findViewById(R.id.routeType);
		String routeType = (String) spinner.getAdapter().getItem(position);
		if (routeType.equals("Bus")) {
			updatedTrips = busPreferenceService.getRoutes();
		} else {
			updatedTrips = trainPreferenceService.getRoutes();
		}

		routeAdapter.clear();
		routeAdapter.addAll(updatedTrips);
		routeAdapter.notifyDataSetChanged();
		refreshScreenTimer.cancel();
		refreshScreenTimer = new Timer();
		refreshScreenTimer.schedule(new RefreshListTimer(this), 1000, 30000);
	}
}
