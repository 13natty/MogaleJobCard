package com.nattySoft.mogalejobcard;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nattySoft.mogalejobcard.AppConstants;
import com.nattySoft.mogalejobcard.RegistrationActivity;
import com.nattySoft.mogalejobcard.R;
import com.nattySoft.mogalejobcard.listener.IncidentClickedListener;
import com.nattySoft.mogalejobcard.listener.PushListener;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.push.GCMBroadcastReceiver;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements RequestResponseListener, PushListener {

	private String TAG = MainActivity.class.getSimpleName();
	
	private boolean registered = false;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	private static final String REGISTRATION_SUCCESS = "0";
	private static final String NEW_INCIDENT = "1";
	private static final String INCIDENT_UPDATE = "2";
	private static final String CHAT_MESSAGE = "3";
	private static final String INCIDENT_ACCEPT = "4";
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;
	static Action action;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String regStr = Preferences.getPreference(this, AppConstants.PreferenceKeys.KEY_REGISTERED);
		registered = regStr != null && regStr.equalsIgnoreCase("true") ? true : false;
		if (registered) {
			// Check device for Play Services APK.
			if (checkPlayServices()) {

				startApp(savedInstanceState);

			}
		} else {
			action = Action.REGISTER;
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivityForResult(intent, 0);
		}
		
	}

	private void startApp(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);

		ActionBar bar = getActionBar();
		// for color
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffb566")));
		// for image
		// bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mogale_icon));

		// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// Add Drawer Item to dataList
//		dataList.add(new DrawerItem(true)); // adding a spinner to the list

//		dataList.add(new DrawerItem("My Favorites")); // adding a header to the
														// list
		dataList.add(new DrawerItem("Incidents", R.drawable.ic_action_incident));
		dataList.add(new DrawerItem("Chat", R.drawable.ic_action_chat));
//		dataList.add(new DrawerItem("Games", R.drawable.ic_action_gamepad));
//		dataList.add(new DrawerItem("Lables", R.drawable.ic_action_labels));

//		dataList.add(new DrawerItem("Main Options"));// adding a header to the
														// list
//		dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
//		dataList.add(new DrawerItem("Cloud", R.drawable.ic_action_cloud));
//		dataList.add(new DrawerItem("Camara", R.drawable.ic_action_camera));
//		dataList.add(new DrawerItem("Video", R.drawable.ic_action_video));
//		dataList.add(new DrawerItem("Groups", R.drawable.ic_action_group));
//		dataList.add(new DrawerItem("Import & Export", R.drawable.ic_action_import_export));

//		dataList.add(new DrawerItem("Other Option")); // adding a header to the
														// list
//		dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
//		dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
//		dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));
		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);

				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle); 

//		if (savedInstanceState == null) {
//
//			if (dataList.get(0).isSpinner() & dataList.get(1).getTitle() != null) {
//				SelectItem(2);
//			} else if (dataList.get(0).getTitle() != null) {
//				SelectItem(1);
//			} else {
//				SelectItem(0);
//			}
//		}
		
		action = Action.GET_ALL_OPEN_INCIDENCES;
		CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {
		case 0:
			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 1:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
//		case 2:
//			fragment = new FragmentThree();
//			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 3:
//			fragment = new FragmentOne();
//			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 4:
//			fragment = new FragmentTwo();
//			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 5:
//			fragment = new FragmentThree();
//			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 7:
//			fragment = new FragmentTwo();
//			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 8:
//			fragment = new FragmentThree();
//			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 9:
//			fragment = new FragmentOne();
//			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 10:
//			fragment = new FragmentTwo();
//			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 11:
//			fragment = new FragmentThree();
//			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 12:
//			fragment = new FragmentOne();
//			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 14:
//			fragment = new FragmentThree();
//			args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 15:
//			fragment = new FragmentOne();
//			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
//		case 16:
//			fragment = new FragmentTwo();
//			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
//			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
//			break;
		default:
			break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if(mDrawerToggle != null)
			mDrawerToggle.syncState();
		GCMBroadcastReceiver.pushListener = this;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return false;
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (dataList.get(position).getTitle() == null) {
				SelectItem(position);
			}

		}
	}

	@Override
	public void hasResponse(String responce) {
		Log.d(TAG, " response "+responce);
		if(action == Action.GET_ALL_OPEN_INCIDENCES)
		{
			Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_OPENED_INCIDENTS, responce);
			SelectItem(0);
		}
		else if(action == Action.DECLINE_INCIDENT)
		{
			action = Action.GET_ALL_OPEN_INCIDENCES;
			CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
		}
		else if(action == Action.ACCEPT_INCIDENT)
		{
			if(responce.contains("success"))
			{
//				DialogClass cdd=new DialogClass(getActivity(), incidentID);
//				 cdd.show();
			}
		}
		
	}

	@Override
	public void pushReceived(Context context, Intent intent) {
		final Bundle extras = intent.getExtras();
		if(extras.containsKey("type"))
		{
			
			
			if(extras.getString("type").equalsIgnoreCase(REGISTRATION_SUCCESS))
			{
				
			}
			else if(extras.getString("type").equalsIgnoreCase(NEW_INCIDENT))
			{
				
			}
			else if(extras.getString("type").equalsIgnoreCase(INCIDENT_UPDATE))
			{
				
			}
			else if(extras.getString("type").equalsIgnoreCase(CHAT_MESSAGE))
			{
				
			}
			else if(extras.getString("type").equalsIgnoreCase(INCIDENT_ACCEPT))
			{				
				Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
				if(frag instanceof FragmentIncident)
				{
					String incidentId_local = ((FragmentIncident)frag).incidentID;
					DialogClass cdd=new DialogClass(this, incidentId_local, extras.getString("incidentId"), extras.getString("message"));
					cdd.show(); 
				}
				
			}
		
			String message = extras.getString("description")+" : "+extras.getString("message");					
			
		}
	}

}