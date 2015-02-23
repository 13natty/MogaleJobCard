package com.nattySoft.mogalejobcard;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import android.R.integer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements RequestResponseListener, PushListener {

	private String TAG = MainActivity.class.getSimpleName();

	private boolean registered = false;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static String employeeNUM = null;

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

	ArrayList<Fragment> prevFrag;
	private int prevPos;

	public static Action action;

	protected static String incidentStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String regStr = Preferences.getPreference(this, AppConstants.PreferenceKeys.KEY_REGISTERED);
		registered = regStr != null && regStr.equalsIgnoreCase("true") ? true : false;
		prevFrag = new ArrayList<Fragment>();
		if (registered) {
			// Check device for Play Services APK.
			if (checkPlayServices()) {
				employeeNUM = Preferences.getPreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM);
				startApp(savedInstanceState);

			}
		} else {
			action = Action.REGISTER;
			Intent intentREG = new Intent(this, RegistrationActivity.class);
			startActivityForResult(intentREG, 0);
			finish();
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
		// dataList.add(new DrawerItem(true)); // adding a spinner to the list

		// dataList.add(new DrawerItem("My Favorites")); // adding a header to
		// the
		// list
		dataList.add(new DrawerItem("Incidents", R.drawable.ic_action_incident));
		dataList.add(new DrawerItem("Chat", R.drawable.ic_action_chat));
		// dataList.add(new DrawerItem("Games", R.drawable.ic_action_gamepad));
		// dataList.add(new DrawerItem("Lables", R.drawable.ic_action_labels));

		// dataList.add(new DrawerItem("Main Options"));// adding a header to
		// the
		// list
		// dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
		// dataList.add(new DrawerItem("Cloud", R.drawable.ic_action_cloud));
		// dataList.add(new DrawerItem("Camara", R.drawable.ic_action_camera));
		// dataList.add(new DrawerItem("Video", R.drawable.ic_action_video));
		// dataList.add(new DrawerItem("Groups", R.drawable.ic_action_group));
		// dataList.add(new DrawerItem("Import & Export",
		// R.drawable.ic_action_import_export));

		// dataList.add(new DrawerItem("Other Option")); // adding a header to
		// the
		// list
		// dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
		// dataList.add(new DrawerItem("Settings",
		// R.drawable.ic_action_settings));
		// dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));
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

		// if (savedInstanceState == null) {
		//
		// if (dataList.get(0).isSpinner() & dataList.get(1).getTitle() != null)
		// {
		// SelectItem(2);
		// } else if (dataList.get(0).getTitle() != null) {
		// SelectItem(1);
		// } else {
		// SelectItem(0);
		// }
		// }

		action = Action.GET_ALL_OPEN_INCIDENCES;
		CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));

	}

	public void addMoreToDrawer(String header, List<DrawerItem> items) {
		if (dataList.size() > 2) {
			for (int i = (dataList.size() - 1); i > 1; i--) {
				dataList.remove(i);
			}
		}
		dataList.add(new DrawerItem(header));// adding a header to the
		// list
		for (int i = 0; i < items.size(); i++) {
			dataList.add(items.get(i));
		}

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	public void setCount(int count) {
		DrawerItem dItem0 = dataList.get(0);
		DrawerItem dItem1 = dataList.get(1);
		dataList.clear();
		
		dItem0.setimgCountBG(R.drawable.messagecount);
		dItem0.setCount(""+count);
		
		dataList.add(dItem0);
		dataList.add(dItem1);
		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);
		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
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
			// fragment = new FragmentJobCard();
			// args.putString(FragmentOne.ITEM_NAME,
			// dataList.get(possition).getItemName());
			// args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
			// dataList.get(possition).getImgResID());

			fragment = new FragmentOne();
			args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		case 1:
			fragment = new FragmentTwo();
			args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
			args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
			break;
		// case 2:
		// fragment = new FragmentThree();
		// args.putString(FragmentThree.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentThree.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 3:
		// fragment = new FragmentOne();
		// args.putString(FragmentOne.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 4:
		// fragment = new FragmentTwo();
		// args.putString(FragmentTwo.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentTwo.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 5:
		// fragment = new FragmentThree();
		// args.putString(FragmentThree.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentThree.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 7:
		// fragment = new FragmentTwo();
		// args.putString(FragmentTwo.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentTwo.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 8:
		// fragment = new FragmentThree();
		// args.putString(FragmentThree.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentThree.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 9:
		// fragment = new FragmentOne();
		// args.putString(FragmentOne.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 10:
		// fragment = new FragmentTwo();
		// args.putString(FragmentTwo.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentTwo.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 11:
		// fragment = new FragmentThree();
		// args.putString(FragmentThree.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentThree.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 12:
		// fragment = new FragmentOne();
		// args.putString(FragmentOne.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 14:
		// fragment = new FragmentThree();
		// args.putString(FragmentThree.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentThree.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 15:
		// fragment = new FragmentOne();
		// args.putString(FragmentOne.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		// case 16:
		// fragment = new FragmentTwo();
		// args.putString(FragmentTwo.ITEM_NAME,
		// dataList.get(possition).getItemName());
		// args.putInt(FragmentTwo.IMAGE_RESOURCE_ID,
		// dataList.get(possition).getImgResID());
		// break;
		default:
			break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);
		
		prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
		prevPos = possition;

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
		if (mDrawerToggle != null)
			mDrawerToggle.syncState();
		GCMBroadcastReceiver.pushListener = this;
	}

	@Override
	protected void onResume() {
		super.onResume();

		Bundle intent_extras = getIntent().getExtras();
		if (intent_extras != null && intent_extras.containsKey("action")) {
			if (intent_extras.get("action") == "newIncident") {
				action = Action.GET_ALL_OPEN_INCIDENCES;
				CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
			}
		}
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
			DrawerItem item = (DrawerItem) mDrawerList.getItemAtPosition(position);
			String tittle = item.getTitle();
			String itemName = item.getItemName();
			String type = item.type;
			if (type == null && dataList.get(position).getTitle() == null) {
				SelectItem(position);
			} else if (type != null) {
				switch (type) {
				case "accepted":

					break;
				case "not_accepted":
					Log.d(TAG, "tittle " + tittle);
					Log.d(TAG, "itemName " + itemName);
					// if (itemName.equalsIgnoreCase("Accept")) {
					// Fragment frag =
					// getFragmentManager().findFragmentById(R.id.content_frame);
					// if (frag instanceof FragmentIncident) {
					// MainActivity.action = Action.ACCEPT_INCIDENT;
					// CommunicationHandler.acceptIncident(MainActivity.this,
					// (RequestResponseListener) MainActivity.this,
					// ProgressDialog.show(MainActivity.this, "Please wait",
					// "Accepting Incidents..."),
					// Preferences.getPreference(MainActivity.this,
					// AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM),
					// ((FragmentIncident) frag).incidentID);
					// }
					// } else if (itemName.equalsIgnoreCase("Decline")) {
					// Fragment frag =
					// getFragmentManager().findFragmentById(R.id.content_frame);
					// if (frag instanceof FragmentIncident) {
					// DialogClass cdd = new DialogClass(MainActivity.this,
					// ((FragmentIncident) frag).incidentID);
					// cdd.show();
					// }
					// }
					break;
				default:
					break;
				}
				mDrawerList.setItemChecked(position, true);
				setTitle(dataList.get(position).getItemName());
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		}
	}

	@Override
	public void hasResponse(String responce) {
		Log.d(TAG, " response " + responce);
		Log.d(TAG, " action " + action);
		if (action == Action.GET_ALL_OPEN_INCIDENCES) {
			if (!responce.equalsIgnoreCase("Did not work!"))
				Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_OPENED_INCIDENTS, responce);
			SelectItem(0);
		}else if(action == Action.GET_ALL_OPEN_INCIDENCES_BG){
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			if (frag instanceof FragmentOne) {
				((FragmentOne) frag).setMenus(responce);
			}else
			{
				for (int i = 0; i < prevFrag.size(); i++) {
					if(prevFrag.get(i) instanceof FragmentOne)
					{
						((FragmentOne)prevFrag.get(i)).setMenus(responce);
					}
				}
				
				setCount(1);
				//vibrate and make sound
				Vibrator vibrator;
				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(500);
				
				try {
				    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
				    r.play();
				} catch (Exception e) {
				    e.printStackTrace();
				}
			}
		}
		else if (action == Action.DECLINE_INCIDENT) {
			action = Action.GET_ALL_OPEN_INCIDENCES;
			CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
		} else if (action == Action.ACCEPT_INCIDENT) {
			if (responce.contains("success")) {
				action = Action.GET_ALL_OPEN_INCIDENCES;
				CommunicationHandler.getOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
			}
		} else if (action == Action.GET_COMMENTS) {
			Log.d(TAG, "responce " + responce);
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			if (frag instanceof FragmentIncident) {
				((FragmentIncident) frag).showComments(responce);
			}
		} else if (action == Action.SAVE_JOB_CARD) {
			String jobCardId = Preferences.getPreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_JOB_CARD_ID + FragmentIncident.incidentID);
			if (jobCardId == null) {
				String id = null;
				try {
					JSONObject ob = new JSONObject(responce);
					id = ob.getString("jobCardId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Preferences.savePreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_JOB_CARD_ID + FragmentIncident.incidentID, id);
			}
			// action = Action.INCIDENT_STATUS;
			// CommunicationHandler.updateIncident(this, this,
			// ProgressDialog.show(MainActivity.this, "Please wait",
			// "Updating incident..."), incidentStatus);
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			DialogClass cdd = new DialogClass(this, "", "", "JOB CARD SAVED");
			cdd.show();
		} else if (action == Action.UPDATE_JOB_CARD) {
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			DialogClass cdd = new DialogClass(this, "", "", "JOB CARD UPDATED");
			cdd.show();
			// action = Action.INCIDENT_STATUS;
			// CommunicationHandler.updateIncident(this, this,
			// ProgressDialog.show(MainActivity.this, "Please wait",
			// "Updating incident..."), incidentStatus);
		}

	}

	@Override
	public void pushReceived(Context context, Intent intent) {
		final Bundle extras = intent.getExtras();
		if (extras.containsKey("type")) {

			if (isAppRunning(context)) {

				if (extras.getString("type").equalsIgnoreCase(REGISTRATION_SUCCESS)) {
					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					String message = extras.getString("body");
					Notification notification = new Notification(R.drawable.mogale_icon_push, message, System.currentTimeMillis());

					Intent notificationIntent = new Intent(context, MainActivity.class);

					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

					PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

					notification.setLatestEventInfo(context, "", message, pIntent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults |= Notification.DEFAULT_SOUND;
					notification.defaults |= Notification.DEFAULT_LIGHTS;
					notification.defaults |= Notification.DEFAULT_VIBRATE;

					notificationManager.notify(0, notification);

				} else if (extras.getString("type").equalsIgnoreCase(NEW_INCIDENT)) {

					action = Action.GET_ALL_OPEN_INCIDENCES_BG;
					CommunicationHandler.getOpenIncidents(this, this, null);
				} else if (extras.getString("type").equalsIgnoreCase(INCIDENT_UPDATE)) {
					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					String message = extras.getString("body");
					Notification notification = new Notification(R.drawable.mogale_icon_push, message, System.currentTimeMillis());

					Intent notificationIntent = new Intent(context, MainActivity.class);

					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

					PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

					notification.setLatestEventInfo(context, "", message, pIntent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults |= Notification.DEFAULT_SOUND;
					notification.defaults |= Notification.DEFAULT_LIGHTS;
					notification.defaults |= Notification.DEFAULT_VIBRATE;

					notificationManager.notify(0, notification);

				} else if (extras.getString("type").equalsIgnoreCase(CHAT_MESSAGE)) {
					if (!extras.getString("senderEmployNum").equals(MainActivity.employeeNUM)) {
						Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
						if (frag instanceof FragmentTwo) {
							((FragmentTwo) frag).sendChatMessage(extras.getString("senderName"), extras.getString("body"), extras.getString("timestamp"));
						} else {
							if (FragmentTwo.chatArrayAdapter != null) {
								FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body")));
							} else {
								FragmentTwo.chatArrayAdapter = new ChatArrayAdapter(this.getApplicationContext(), R.layout.single_chat_message);
								FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body")));
							}
						}
					}

				} else if (extras.getString("type").equalsIgnoreCase(INCIDENT_ACCEPT)) {
					Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
					if (frag instanceof FragmentIncident) {
						String incidentId_local = ((FragmentIncident) frag).incidentID;
						DialogClass cdd = new DialogClass(this, incidentId_local, extras.getString("incidentId"), extras.getString("body"));
						cdd.show();
					}

				}
			} else {
				if (extras.getString("type").equalsIgnoreCase(NEW_INCIDENT)) {
					String message = extras.getString("body");
					// Prepare intent which is triggered if the
					// notification is selected
					Intent myIntent = new Intent(context, MainActivity.class);
					myIntent.putExtra("type", NEW_INCIDENT);
					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FLAG_ACTIVITY_CLEAR_TASK);

					// Build notification
					// Actions are just fake
					Notification noti = new NotificationCompat.Builder(this).setContentTitle("New incident ").setContentText(message).setSmallIcon(R.drawable.mogale_icon_push).setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_SOUND).setDefaults(Notification.DEFAULT_VIBRATE).setDefaults(Notification.DEFAULT_LIGHTS).setAutoCancel(true).build();
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					// hide the notification after its selected
					noti.flags |= Notification.FLAG_AUTO_CANCEL;

					notificationManager.notify(0, noti);
				} else {
					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					String message = extras.getString("body");
					Notification notification = new Notification(R.drawable.mogale_icon_push, message, System.currentTimeMillis());

					Intent notificationIntent = new Intent(context, MainActivity.class);
					notificationIntent.putExtra("type", extras.getString("type"));

					notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

					PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

					notification.setLatestEventInfo(context, "", message, pIntent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.defaults |= Notification.DEFAULT_SOUND;
					notification.defaults |= Notification.DEFAULT_LIGHTS;
					notification.defaults |= Notification.DEFAULT_VIBRATE;

					notificationManager.notify(0, notification);
				}
			}

			String message = extras.getString("description") + " : " + extras.getString("message");

		}
	}

	// foreground---

	/**
	 * Check if the android application is being sent in the background (i.e
	 * behind another application's Activity).
	 * 
	 * @param context
	 *            the context
	 * @return true if another application will be above this one.
	 */
	public static boolean isAppRunning(Context context) {
		// check with the first task(task in the foreground)
		// in the returned list of tasks
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
		if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
			// your application is running in the background
			return true;
		}
		return false;
	}

	// Defined in Activity class, so override
	@Override
	public void onBackPressed() {
		if (prevFrag != null && prevFrag.size() > 1) {
			if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
				if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof PipeLineInfo) {
					((PipeLineInfo) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof ExistingMeterInformation) {
					((ExistingMeterInformation) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof NewMeterInformation) {
					((NewMeterInformation) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof ConnectionInfo) {
					((ConnectionInfo) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof Hydrant) {
					((Hydrant) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof Valve) {
					((Valve) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
				}
				else if(getFragmentManager().findFragmentById(R.id.content_frame) instanceof FragmentOne) {
					super.onBackPressed();
				}
				int index = prevFrag.size() - 1;
				FragmentManager frgManager = getFragmentManager();
				frgManager.beginTransaction().replace(R.id.content_frame, prevFrag.get(index)).commit();
				prevFrag.remove(index);
			} else {
				mDrawerList.setItemChecked(prevPos, true);
				setTitle(dataList.get(prevPos).getItemName());
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		} else if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerList.setItemChecked(prevPos, true);
			setTitle(dataList.get(prevPos).getItemName());
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			super.onBackPressed();
		}
	}

}