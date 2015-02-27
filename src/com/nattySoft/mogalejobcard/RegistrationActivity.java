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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity implements RequestResponseListener, PushListener {

	EditText employeeNumber;
	Button regButton;

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
		//getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
}

