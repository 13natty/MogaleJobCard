package com.nattySoft.mogalejobcard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.listener.PushListener;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.push.GCMBroadcastReceiver;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RegistrationActivity extends Activity implements RequestResponseListener, PushListener {

	EditText employeeNumber;
	Button regButton;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		employeeNumber = (EditText) findViewById(R.id.editText1);
		regButton = (Button) findViewById(R.id.regButton);
		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (employeeNumber.getText().length() <= 0) {
					new AlertDialog.Builder(RegistrationActivity.this).setTitle("Empty Input").setMessage("Please enter your employee number").setCancelable(true).show();
				} else {
					CommunicationHandler.registerUser(RegistrationActivity.this.getApplicationContext(), (RequestResponseListener) RegistrationActivity.this, ProgressDialog.show(RegistrationActivity.this, "Please wait", "Registering...", true, true), employeeNumber.getText().toString());
				}
			}
		});
		GCMBroadcastReceiver.setListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menItem = menu.getItem(1);
		menItem.setTitle("OS version "+getVersionInfo());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_change_host) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setMessage("Enter new host IP");

			final EditText hostInput = new EditText(this);
//			hostInput.setInputType(InputType.TYPE_CLASS_TEXT);
			hostInput.setHint(AppConstants.Config.SERVER_URL);

			alert.setView(hostInput);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
			    	AppConstants.Config.SERVER_URL = "http://"+hostInput.getText().toString()+"/Mogale/Controller";
			    	Preferences.savePreference(RegistrationActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL, AppConstants.Config.SERVER_URL);
			    }
			  });

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	AppConstants.Config.SERVER_URL = "http://192.198.100.27:8080/Mogale/Controller";
			    	Preferences.savePreference(RegistrationActivity.this.getApplicationContext(), AppConstants.PreferenceKeys.KEY_SERVER_URL, AppConstants.Config.SERVER_URL);
				dialog.cancel();
			    }
			  });

			alert.show();
		}
		else if (id == R.id.action_version) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void hasResponse(String response) {
		Log.d("RegistrationActivity", " response " + response);
		
		JSONObject regResults = null;
		
		// try parse the string to a JSON object
		try {
			regResults = new JSONObject(response);
			JSONArray resultArr = (JSONArray) regResults.get("response");
			String result = resultArr.get(0).toString();
			Log.d("RegistrationActivity", " result " + result);
			if(result.equals("fail"))
			{
				JSONArray messageArr = (JSONArray) regResults.get("message");
				String message = messageArr.get(0).toString();
				Log.d("RegistrationActivity", " message " + message);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				
				// set title
				alertDialogBuilder.setTitle(result);
	 
				// set dialog message
				alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
						// // if this button is clicked, close
						// // current activity
						// RegistrationActivity.this.finish();
							dialog.cancel();
						}
					  });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
			}
			else
			{
				sucsess();
			}
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			sucsess();
		}
		
		
	}

	private void sucsess() {
		String empN = employeeNumber.getText().toString();
		Preferences.savePreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM, empN);
		Preferences.savePreference(getBaseContext(), AppConstants.PreferenceKeys.KEY_REGISTERED, "true");
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Sucsess");

		// set dialog message
		alertDialogBuilder
			.setMessage("sucsessfully registered "+empN)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				// // if this button is clicked, close
				// // current activity
					dialog.cancel();
					RegistrationActivity.this.finish();
					//now display main app
					startActivityForResult(new Intent(RegistrationActivity.this, MainActivity.class), 0);
				}
			  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
	}

	@Override
	public void pushReceived(Context context, Intent intent) {
		final Bundle extras = intent.getExtras();
		if (extras.getString("type").equalsIgnoreCase(MainActivity.REGISTRATION_SUCCESS)) {
			
			String message = extras.getString("body");
			// Prepare intent which is triggered if the
			// Build notification
			// Actions are just fake
			Notification noti = new NotificationCompat.Builder(this).setContentTitle("Registered successfully").setContentText(message).setSmallIcon(R.drawable.mogale_icon_push).setWhen(System.currentTimeMillis()).setDefaults(Notification.DEFAULT_SOUND).setDefaults(Notification.DEFAULT_VIBRATE).setDefaults(Notification.DEFAULT_LIGHTS).setAutoCancel(true).build();
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// hide the notification after its selected
			noti.flags |= Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(0, noti);			
			
		}
	}
	
	public String getVersionInfo() {
        String strVersion = "";

        PackageInfo packageInfo;
        try {
            packageInfo = getApplicationContext()
                .getPackageManager()
                .getPackageInfo(
                    getApplicationContext().getPackageName(), 
                    0
                );
            strVersion += packageInfo.versionName;
        } catch (NameNotFoundException e) {
            strVersion += "Unknown";
        }

        return strVersion;
    }
}

