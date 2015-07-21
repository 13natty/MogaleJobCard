package com.nattySoft.mogalejobcard;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Telephony;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nattySoft.mogalejobcard.listener.PushListener;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.push.GCMBroadcastReceiver;
import com.nattySoft.mogalejobcard.util.Preferences;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;

public class MainActivity extends Activity implements RequestResponseListener, PushListener {

    private String TAG = MainActivity.class.getSimpleName();

    private long enqueue;
    private DownloadManager dm;
    private boolean registered = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String employeeNUM = null;
    public static String employeeName = null;
    public static String employeeDesignation = null;

    static final String REGISTRATION_SUCCESS = "0";
    static final String NEW_INCIDENT = "1";
    static final String INCIDENT_UPDATE = "2";
    static final String CHAT_MESSAGE = "3";
    static final String INCIDENT_ACCEPT = "4";
    public static final String UPDATE_APP = "5";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    ArrayList<Fragment> prevFrag;
    private int prevPos;

    private Bundle savedInstanceState;

    public static int incidentCount = 0;

    public static Action action;
    public static String version;

    protected static String incidentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	this.savedInstanceState = savedInstanceState;
	if (Preferences.getPreference(MainActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL) == null) {
	    Preferences.savePreference(MainActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL, AppConstants.Config.HOST);
	} else {
	    AppConstants.Config.HOST = Preferences.getPreference(MainActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL);
	    AppConstants.Config.SERVER_URL = AppConstants.Config.HOST + "/Mogale/Controller";

	}
	version = getVersionInfo();
	String regStr = Preferences.getPreference(this, AppConstants.PreferenceKeys.KEY_REGISTERED);
	registered = regStr != null && regStr.equalsIgnoreCase("true") ? true : false;
	prevFrag = new ArrayList<Fragment>();
	if (registered) {
	    // Check device for Play Services APK.
	    if (checkPlayServices()) {
		new DeleteInstallerThread().start();
		employeeNUM = Preferences.getPreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM);

		String user = Preferences.getPreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_USER);
		if (user != null) {
		    extractUserInfo(user);
		} else {
		    action = Action.GET_USER;
		    CommunicationHandler.getUser(this, this);
		}
		Intent current = getIntent();
		if (current != null) {
		    Bundle extras = current.getExtras();
		    if (extras != null) {
			String type = extras.getString("type");
			String incidentId = extras.getString("incidentId");
			if (type != null && type.equalsIgnoreCase("" + NEW_INCIDENT)) {
			    CommunicationHandler.pingIncidentReceived(this, this, null, incidentId);
			}
		    }
		}

		BroadcastReceiver receiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
			    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			    Query query = new Query();
			    query.setFilterById(enqueue);
			    Cursor c = dm.query(query);
			    if (c.moveToFirst()) {
				int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
				if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

				    try {
					// FileUtil.createDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
					File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mogaleupdate/MogaleJobCard.apk");
					if (apkFile.exists()) {
					    Intent intentAPK = new Intent(Intent.ACTION_VIEW);
					    intentAPK.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
					    startActivity(intentAPK);
					}

				    } catch (Exception e) {
					e.printStackTrace();
				    }
				}
			    }
			}
		    }
		};

		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

		BroadcastReceiver myReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
			startActivity(intent);
		    }
		};

		registerReceiver(myReceiver, new IntentFilter("ACTION"));
		unregisterReceiver(myReceiver);

	    }
	} else {
	    action = Action.REGISTER;
	    Intent intentREG = new Intent(this, RegistrationActivity.class);
	    startActivityForResult(intentREG, 0);
	    finish();
	}

    }
    
    @Override
    protected void onStart() {
	GCMBroadcastReceiver bcr = new GCMBroadcastReceiver();
	bcr.setListener(this);
        super.onStart();
    }

    private Boolean hasTelephony;

    public final static int INCIDENT_SENT = 1;
    public final static int INCIDENT_RECEIVED_ON_SERVER = 2;
    public final static int INCIDENT_RECEIVED_ON_DEVICE = 3;
    public final static int INCIDENT_READ_BY_USER = 4;
    public final static int INCIDENT_ACCEPTED_BY_USER = 5;
    public final static int INCIDENT_DECLINED_BY_USER = 6;
    public final static int INCIDENT_JOB_CARD_RECEIVED = 7;
    
    public boolean hasTelephony() {
	if (hasTelephony == null) {
	    TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	    if (tm == null) {
		hasTelephony = new Boolean(false);
		return hasTelephony.booleanValue();
	    }
	    // if(this.getSDKVersion() < 5)
	    // {
	    // hasTelephony=new Boolean(true);
	    // return hasTelephony;
	    // }
	    PackageManager pm = this.getPackageManager();
	    Method method = null;
	    if (pm == null)
		return hasTelephony;
	    else {
		try {
		    Class[] parameters = new Class[1];
		    parameters[0] = String.class;
		    method = pm.getClass().getMethod("hasSystemFeature", parameters);
		    Object[] parm = new Object[1];
		    parm[0] = new String(PackageManager.FEATURE_TELEPHONY);
		    Object retValue = method.invoke(pm, parm);
		    if (retValue instanceof Boolean)
			hasTelephony = new Boolean(((Boolean) retValue).booleanValue());
		    else
			hasTelephony = new Boolean(false);
		} catch (Exception e) {
		    hasTelephony = new Boolean(false);
		}
	    }
	}
	return hasTelephony;
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
	if ("Manager".equals(employeeDesignation) || "Foreman".equals(employeeDesignation)) {
	    dataList.add(new DrawerItem("   All Open Incidents ", R.drawable.all_incidents));
	}
	dataList.add(new DrawerItem("   "+employeeName+"'s Incidents", R.drawable.close_envelope));
	if ("Manager".equals(employeeDesignation) || "Foreman".equals(employeeDesignation)) {
	    dataList.add(new DrawerItem("   My Escalations ", R.drawable.justice_hammer));
	}
	dataList.add(new DrawerItem("   Chat", R.drawable.chat_oval_speech_bubble));
	dataList.add(new DrawerItem("   Settings", R.drawable.settings));
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

	// String servicestring = Context.DOWNLOAD_SERVICE;
	// DownloadManager downloadmanager;
	// downloadmanager = (DownloadManager) getSystemService(servicestring);
	// Uri uri = Uri
	// .parse("https://app.asana.com/app/asana/-/download_asset?asset_id=28246520176063");
	// DownloadManager.Request request = new Request(uri);
	// Long reference = downloadmanager.enqueue(request);
	// Log.d("DOWNLOAD", "reference "+reference);

	action = Action.GET_ALL_MY_OPEN_INCIDENCES;
	CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving All My Incidents..."));

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
	if (count > 0)
	    dItem0.setCount("" + count);
	else
	    dItem0.setCount("");

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
	MenuItem menItem = menu.getItem(1);
	menItem.setTitle("OS version " + version);
	return true;
    }

    public Fragment SelectItem(int possition) {

	if (!"Manager".equals(employeeDesignation) && !"Foreman".equals(employeeDesignation)) {
	    return SelectItemArtisan(possition);
	}
	Fragment fragment = null;
	Bundle args = new Bundle();
	switch (possition) {
	case 0://all open incidents 
	    fragment = new FragmentAllOpen();
	    args.putString(FragmentAllOpen.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentAllOpen.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    
	    break;
	case 1://my open incidents
	    // fragment = new FragmentJobCard();
	    // args.putString(FragmentOne.ITEM_NAME,
	    // dataList.get(possition).getItemName());
	    // args.putInt(FragmentOne.IMAGE_RESOURCE_ID,
	    // dataList.get(possition).getImgResID());

	    fragment = new FragmentOne();
	    if (incidentCount > 0) {
		incidentCount = 0;
		setCount(incidentCount);
	    }
	    args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    break;
	case 2://Escalations
	    fragment = new FragmentThree();
	    args.putString(FragmentThree.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    
	    break;
	case 3://chat
	    fragment = new FragmentTwo();
	    args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    break;
	case 4://settings
	    CharSequence settingsOptions[] = new CharSequence[] {"Change Host IP", "OS version" + version};

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Settings");
	    builder.setItems(settingsOptions, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            menuItemClicked(dialog, which);
	        }
	    });
	    builder.show();
	    return null;
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
	return fragment;
    }

    private Fragment SelectItemArtisan(int possition) {
	Fragment fragment = null;
	Bundle args = new Bundle();
	switch (possition) {
	case 0://my open incidents
	    fragment = new FragmentOne();
	    if (incidentCount > 0) {
		incidentCount = 0;
		setCount(incidentCount);
	    }
	    args.putString(FragmentOne.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentOne.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    break;
	case 1://chat
	    fragment = new FragmentTwo();
	    args.putString(FragmentTwo.ITEM_NAME, dataList.get(possition).getItemName());
	    args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(possition).getImgResID());
	    break;
	case 2://settings
	    CharSequence settingsOptions[] = new CharSequence[] {"Change Host IP", "OS version" + version};

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Settings");
	    builder.setItems(settingsOptions, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            menuItemClicked(dialog, which);
	        }
	    });
	    builder.show();
	    return null;
	
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
	return fragment;
	
    }

    protected void menuItemClicked(DialogInterface dialog, int which) {
	if (which == 0) {
	    AlertDialog.Builder passwordAlert = new AlertDialog.Builder(this);

	    final EditText edittext = new EditText(MainActivity.this);
	    passwordAlert.setMessage("Enter password");
	    passwordAlert.setTitle("Enter Password");

	    passwordAlert.setView(edittext);

	    passwordAlert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    // What ever you want to do with the value
		    String YouEditTextValue = edittext.getText().toString();
		    if (YouEditTextValue.equalsIgnoreCase("idol")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

			alert.setMessage("Enter new host IP");

			final EditText hostInput = new EditText(MainActivity.this);
			// hostInput.setInputType(InputType.TYPE_CLASS_TEXT);
			hostInput.setHint(AppConstants.Config.SERVER_URL);

			alert.setView(hostInput);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				// get user input and set it to
				// result
				// edit text
				if (!hostInput.getText().toString().equals("")) {
				    AppConstants.Config.HOST = "http://" + hostInput.getText().toString();
				    Preferences.savePreference(MainActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL, AppConstants.Config.HOST);
				    AppConstants.Config.SERVER_URL = AppConstants.Config.HOST + "/Mogale/Controller";
				}
			    }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				// AppConstants.Config.SERVER_URL
				// =
				// "http://192.198.100.27:8080/Mogale/Controller";
				// Preferences.savePreference(MainActivity.this.getApplicationContext(),
				// AppConstants.PreferenceKeys.KEY_SERVER_URL,
				// AppConstants.Config.SERVER_URL);
				dialog.cancel();
			    }
			});

			alert.show();
		    }
		}
	    });

	    passwordAlert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		}
	    });

	    passwordAlert.show();

	} else if (which == 1) {
	    dialog.dismiss();
	}
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
	GCMBroadcastReceiver bcr = new GCMBroadcastReceiver();
	bcr.setListener(this);
    }

    @Override
    protected void onResume() {
	super.onResume();
	Bundle intent_extras = getIntent().getExtras();
	if (intent_extras != null && intent_extras.containsKey("action")) {
	    if (intent_extras.get("action") == "newIncident") {
		action = Action.GET_ALL_MY_OPEN_INCIDENCES;
		CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving All My Incidents..."));
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
	int id = item.getItemId();
	if("Search Incident".equals(item.getTitle())){
	    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
	    alert.setTitle("Search Incident");
	    alert.setIcon(drawable.ic_menu_search);
	    // Set an EditText view to get user input 
	    final EditText input = new EditText(MainActivity.this);
	    alert.setView(input);
	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
	        String result = input.getText().toString();
	            //do what you want with your result
	        	action = Action.GET_ALL_OPEN_INCIDENCES;
	        	CommunicationHandler.searchIncident(getBaseContext(), MainActivity.this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving All Open Incidents..."), result);
	            }
	        });
	    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	             // Canceled.
	            }
	        });
	    alert.show();
	}
	if (id == android.R.id.home && !mDrawerToggle.isDrawerIndicatorEnabled()) {
	    mDrawerToggle.setDrawerIndicatorEnabled(true);
	    onBackPressed();
            return true;
	}
	if (mDrawerToggle.onOptionsItemSelected(item)) {
	    return true;
	} 
	// return super.onOptionsItemSelected(item);

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
		switch (position) {
		case 0://all open incidents
		    action = Action.GET_ALL_OPEN_INCIDENCES;
		    CommunicationHandler.getAllIncidents(getBaseContext(), MainActivity.this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving All Open Incidents..."));
		    break;
		case 2://escalations
		    MainActivity.action = Action.GET_ESCALATED_INCIDENCES_ASSIGNED_TO_ME;
		    CommunicationHandler.getIncidentsAssignedTome(getBaseContext(), MainActivity.this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Escaleted Incidents..."));
		    break;
		default:
		    SelectItem(position);
		    break;
		}
		
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
		    
	if (action == Action.GET_USER) {
	    if (!responce.equalsIgnoreCase("Did not work!")) {
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_USER, responce);

		extractUserInfo(responce);
	    }
	} else if (action == Action.VIEW_LOGS) {
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).viewLogs(responce);
	    }
	    Log.d("VIEW_LOGS ", "responce "+responce);
	}else if (action == Action.INCIDENT_PROGRESS) {
//	    action = Action.GET_ALL_MY_OPEN_INCIDENCES;
//	    CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
	    Log.d("progress ", "responce "+responce);
	}else if (action == Action.CLOSE_INCIDENT) {
	    action = Action.GET_ALL_MY_OPEN_INCIDENCES;
	    CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
	}else if (action == Action.RE_ASSIGN) {
	    Log.d(TAG, "responce " + responce);
	    action = Action.RE_ASSIGNED;
	    CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Incident has been re assigned..."));
	} else if (action == Action.RE_ASSIGNED) {
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_MY_INCIDENTS, responce);
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).updateAssignees();
	    }
	} else if (action == Action.ASSIGNEE_REMOVED) {
	    Log.d(TAG, "responce " + responce);
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_MY_INCIDENTS, responce);
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).updateAssignees();
	    }
	}else if (action == Action.REMOVE_ASSIGNEE) {
	    Log.d(TAG, "ASSIGNEE_REMOVED " + responce);
	    action = Action.ASSIGNEE_REMOVED;
	    CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Assignee has been removed..."));
	}else if (action == Action.GET_ALL_USERS) {
	    Log.d(TAG, "responce " + responce);
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).assignUsers(responce);
	    }
	}else if(action == Action.GET_ALL_OPEN_INCIDENCES){
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_ALL_OPEN_INCIDENTS, responce);
	    
	    SelectItem(0);	    
	    
	}else if (action == Action.GET_ALL_MY_OPEN_INCIDENCES || action == Action.INCIDENT_ACCEPTED  || action == Action.INCIDENT_DECLINED) {
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_MY_INCIDENTS, responce);
	    if ("Manager".equals(employeeDesignation) || "Foreman".equals(employeeDesignation)) 
		SelectItem(1);
	    else{
		SelectItem(0);
		if(action == Action.INCIDENT_ACCEPTED){
		    MainActivity.action = Action.INCIDENT_PROGRESS;
		    CommunicationHandler.incidentProgressStatus(this, this, FragmentIncident.incidentID, ""+INCIDENT_ACCEPTED_BY_USER, employeeNUM);
		}else if(action == Action.INCIDENT_DECLINED){
		    MainActivity.action = Action.INCIDENT_PROGRESS;
		    CommunicationHandler.incidentProgressStatus(this, this, FragmentIncident.incidentID, ""+INCIDENT_DECLINED_BY_USER, employeeNUM);
		}
	    }
	} else if (action == Action.GET_ESCALATED_INCIDENCES_ASSIGNED_TO_ME) {
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_OPENED_INCIDENTS_ASSIGNED_TO_ME, responce);
//	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
//	    if (frag instanceof FragmentThree) {
//		((FragmentThree) frag).populateEscalations(responce, (FragmentThree) frag);
//	    }
	    SelectItem(2);
	} else if (action == Action.GET_ALL_OPEN_INCIDENCES_BG) {
	    if (responce.equalsIgnoreCase("{\"message\":[\"Server returned success.\"],\"response\":[\"success\"]}"))
		return;
	    if (!responce.equalsIgnoreCase("Did not work!"))
		Preferences.savePreference(this, AppConstants.PreferenceKeys.KEY_MY_INCIDENTS, responce);
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentOne) {
		((FragmentOne) frag).setMenus(responce);
	    } else {
		for (int i = 0; i < prevFrag.size(); i++) {
		    if (prevFrag.get(i) instanceof FragmentOne) {
			((FragmentOne) prevFrag.get(i)).setMenus(responce);
			incidentCount++;
			setCount(incidentCount);
			Toast.makeText(getApplicationContext(), "New Incident Received", Toast.LENGTH_LONG).show();
		    }
		}

		// vibrate and make sound
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
	} else if (action == Action.DECLINE_INCIDENT) {
	    action = Action.INCIDENT_DECLINED;
	    CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
	} else if (action == Action.ACCEPT_INCIDENT) {
	    if (responce.contains("success")) {
		action = Action.INCIDENT_ACCEPTED;
		CommunicationHandler.getMYOpenIncidents(this, this, ProgressDialog.show(MainActivity.this, "Please wait", "Retrieving Open Incidents..."));
	    }
	} else if (action == Action.GET_COMMENTS) {
	    Log.d(TAG, "responce " + responce);
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).showComments(responce);
	    }
	} else if(action == Action.GET_JOB_CARD){
	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
	    if (frag instanceof FragmentIncident) {
		((FragmentIncident) frag).showJobCard(responce);
	    }
	}else if (action == Action.SAVE_JOB_CARD) {
	    if(!responce.isEmpty()){
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
        		
        		MainActivity.action = Action.INCIDENT_PROGRESS;
        		CommunicationHandler.incidentProgressStatus(this, this, FragmentIncident.incidentID, ""+INCIDENT_JOB_CARD_RECEIVED, employeeNUM);
        	    }
        	    // action = Action.INCIDENT_STATUS;
        	    // CommunicationHandler.updateIncident(this, this,
        	    // ProgressDialog.show(MainActivity.this, "Please wait",
        	    // "Updating incident..."), incidentStatus);
        	    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
        	    DialogClass cdd = new DialogClass(this, "", "", "JOB CARD SAVED");
        	    cdd.show();
	    }
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

    private void extractUserInfo(String responce) {
	JSONObject userProfile = null;

	// try parse the string to a JSON object
	try {
	    userProfile = new JSONObject(responce);
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

	if (userProfile != null) {
	    try {

		// Getting JSON Array from URL
		String[] data = new String[userProfile.length()];
		Log.d("forst size", "data1 size " + data.length);
		Log.d(TAG, "userProfile " + userProfile);

		JSONArray userProfileArray = userProfile.getJSONArray("data");

		Log.d("forst size", "userProfileArray size " + userProfileArray.length());
		JSONObject userProfileObject = userProfileArray.getJSONObject(0);
		employeeName = userProfileObject.getString("name");
		employeeDesignation = userProfileObject.getString("designation");
		startApp(savedInstanceState);
	    } catch (Exception e) {
		employeeName = "Unknown";
		startApp(savedInstanceState);
	    }

	}
    }

    @Override
    public void pushReceived(Context context, Intent intent) {
	final Bundle extras = intent.getExtras();
	if (extras.containsKey("type")) {

	    if (isAppRunning(context)) {
//		    NEW_APK_UPDATE(5, "New APK Available"),
//		    INCIDENT_SERVER_RECEIVED(6, "Incident received by server"),
//		    INCIDENT_DEVICE_RECEIVED(7, "Incident received on device"),
//		    INCIDENT_DEVICE_READ(8, "Incident has been read on device"),
//		    INCIDENT_JOBCARD_RECEIVED(9, "Incident job card received");
		if (extras.getString("type").equalsIgnoreCase(""+6)){//Incident received by server
			
		    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
		    if (frag instanceof FragmentIncident) {
			((FragmentIncident) frag).updateProgressIcon(INCIDENT_RECEIVED_ON_SERVER);				
		    }
		}else if(extras.getString("type").equalsIgnoreCase(""+7)){//Incident received on device
			
		    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
		    if (frag instanceof FragmentIncident) {
			((FragmentIncident) frag).updateProgressIcon(INCIDENT_RECEIVED_ON_DEVICE);				
		    }
		}else if(extras.getString("type").equalsIgnoreCase(""+8)){//Incident has been read on device
                    			
		    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
		    if (frag instanceof FragmentIncident) {
			((FragmentIncident) frag).updateProgressIcon(INCIDENT_READ_BY_USER);				
		    }
		}else if(extras.getString("type").equalsIgnoreCase(""+9)){//Incident job card received
                    	
		    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
		    if (frag instanceof FragmentIncident) {
			((FragmentIncident) frag).updateProgressIcon(INCIDENT_JOB_CARD_RECEIVED);				
		    }                    
		}else if (extras.getString("type").equalsIgnoreCase(REGISTRATION_SUCCESS)) {
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
		    CommunicationHandler.getMYOpenIncidents(this, this, null);
		    CommunicationHandler.pingIncidentReceived(this, this, null, extras.getString("incidentId"));
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
		    if (extras.getString("senderEmployNum") != null && !extras.getString("senderEmployNum").equals(employeeNUM)) {
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			if (frag instanceof FragmentTwo) {
			    ((FragmentTwo) frag).sendChatMessage(extras.getString("senderName"), extras.getString("body"), extras.getString("timestamp"));
			} else {
			    if (FragmentTwo.chatArrayAdapter != null) {
				FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body"), extras.getString("timestamp"), extras.getString("senderName")));
			    } else {
				FragmentTwo.chatArrayAdapter = new ChatArrayAdapter(this.getApplicationContext(), R.layout.single_chat_message);
				FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body"), extras.getString("timestamp"), extras.getString("senderName")));
			    }
			}
		    }else {
			Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
			if (frag instanceof FragmentTwo) {
			    ((FragmentTwo) frag).sendChatMessage(extras.getString("senderName"), extras.getString("body"), extras.getString("timestamp"));
			} else {
			    if (FragmentTwo.chatArrayAdapter != null) {
				FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body"), extras.getString("timestamp"), extras.getString("senderName")));
			    } else {
				FragmentTwo.chatArrayAdapter = new ChatArrayAdapter(this.getApplicationContext(), R.layout.single_chat_message);
				FragmentTwo.chatArrayAdapter.add(new ChatMessage(false, extras.getString("body"), extras.getString("timestamp"), extras.getString("senderName")));
			    }
			}
		    }

		} else if (extras.getString("type").equalsIgnoreCase(INCIDENT_ACCEPT)) {
		    Fragment frag = getFragmentManager().findFragmentById(R.id.content_frame);
		    if(extras.getString("body") != null){
			if (frag instanceof FragmentIncident) {
			    String incidentId_local = ((FragmentIncident) frag).incidentID;
			    DialogClass cdd = new DialogClass(this, incidentId_local, extras.getString("incidentId"), extras.getString("body"));
			    cdd.show();
			}
		    }

		} else if (extras.getString("type").equalsIgnoreCase(UPDATE_APP)) {

		    dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		    Request request = new Request(Uri.parse(AppConstants.Config.SERVER_URL_UPDATE + "/MogaleJobCard.apk"));

		    request.setVisibleInDownloadsUi(true);
		    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/mogaleupdate/MogaleJobCard.apk");
		    enqueue = dm.enqueue(request);

		}
	    } else {
		if (extras.getString("type").equalsIgnoreCase(NEW_INCIDENT)) {
		    String message = extras.getString("body");
		    // Prepare intent which is triggered if the
		    // notification is selected
		    Intent myIntent = new Intent(context, MainActivity.class);
		    Bundle extrasBundle = new Bundle();
		    String inciID = extras.getString("incidentId");
		    extrasBundle.putString("incidentId", inciID);
		    extrasBundle.putString("type", NEW_INCIDENT);
		    myIntent.putExtras(extrasBundle);
		    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		    // Build notification
		    // Actions are just fake
		    Notification noti = new NotificationCompat.Builder(this).setContentTitle("New incident ").setContentText(message).setSmallIcon(R.drawable.mogale_icon_push).setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_SOUND).setDefaults(Notification.DEFAULT_VIBRATE).setDefaults(Notification.DEFAULT_LIGHTS).setAutoCancel(true).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);
		} else if (extras.getString("type").equalsIgnoreCase(UPDATE_APP)) {

		    dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		    Request request = new Request(
		    // Uri.parse("http://192.198.100.27:8080/Mogale/updates/MogaleJobCard.apk"));
		    Uri.parse(AppConstants.Config.SERVER_URL_UPDATE + "/MogaleJobCard.apk"));

		    request.setVisibleInDownloadsUi(true);
		    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/mogaleupdate/MogaleJobCard.apk");
		    enqueue = dm.enqueue(request);

		}
		// else {
		// NotificationManager notificationManager =
		// (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// String message = extras.getString("body");
		// Notification notification = new
		// Notification(R.drawable.mogale_icon_push, message,
		// System.currentTimeMillis());
		//
		// Intent notificationIntent = new Intent(context,
		// MainActivity.class);
		// notificationIntent.putExtra("type",
		// extras.getString("type"));
		//
		// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_SINGLE_TOP |
		// Intent.FLAG_ACTIVITY_NEW_TASK);
		//
		// PendingIntent pIntent = PendingIntent.getActivity(context, 0,
		// notificationIntent, 0);
		//
		// notification.setLatestEventInfo(context, "", message,
		// pIntent);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// notification.defaults |= Notification.DEFAULT_LIGHTS;
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		//
		// notificationManager.notify(0, notification);
		// }
	    }

	    String message = extras.getString("description") + " : " + extras.getString("message");

	}
    }

    private void DeleteRecursive(File fileOrDirectory) {

	if (fileOrDirectory.isDirectory())
	    for (File child : fileOrDirectory.listFiles())
		DeleteRecursive(child);

	fileOrDirectory.delete();
    }

    public void showDownload() {
	Intent i = new Intent();
	i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
	startActivity(i);
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
	if (mDrawerLayout != null && prevFrag != null && prevFrag.size() > 1) {
	    if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
		if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof PipeLineInfo) {
		    ((PipeLineInfo) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof ExistingMeterInformation) {
		    ((ExistingMeterInformation) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof NewMeterInformation) {
		    ((NewMeterInformation) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof NewConnectionInfo) {
		    ((NewConnectionInfo) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof Hydrant) {
		    ((Hydrant) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof Valve) {
		    ((Valve) getFragmentManager().findFragmentById(R.id.content_frame)).saveForm();
		} else if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof FragmentOne) {
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
	} else if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mDrawerList)) {
	    mDrawerList.setItemChecked(prevPos, true);
	    setTitle(dataList.get(prevPos).getItemName());
	    mDrawerLayout.closeDrawer(mDrawerList);
	} else {
	    new AlertDialog.Builder(MainActivity.this).setTitle("Exit App").setMessage("Are you sure you want to exit").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    exit();
		}
	    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		}
	    }).setIcon(android.R.drawable.ic_dialog_alert).show();

	}
    }

    protected void exit() {
	super.onBackPressed();
    }

    public String getVersionInfo() {
	String strVersion = "";

	PackageInfo packageInfo;
	try {
	    packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
	    strVersion += packageInfo.versionName;
	} catch (NameNotFoundException e) {
	    strVersion += "Unknown";
	}

	return strVersion;
    }

}
