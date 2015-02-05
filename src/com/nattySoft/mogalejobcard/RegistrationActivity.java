package com.nattySoft.mogalejobcard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity implements RequestResponseListener {

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
					//the funcition callback called onActivityResult is called to display main app.
				}
			  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
	}
}

