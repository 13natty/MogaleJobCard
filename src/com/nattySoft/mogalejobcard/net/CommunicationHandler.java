package com.nattySoft.mogalejobcard.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.View.OnClickListener;

import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.ConnectionManager;
import com.nattySoft.mogalejobcard.AppConstants;
//import com.nattySoft.mogalejobcard.RegistrationActivity;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

public class CommunicationHandler {

	private static final String SERVER_URL = AppConstants.Config.SERVER_URL;
	private static final String LOG_TAG = CommunicationHandler.class.getSimpleName();

	public enum Action {
		GET_ALL_OPEN_INCIDENCES, REGISTER, ACCEPT_INCIDENT, DECLINE_INCIDENT, ADD_COMMENT, GET_COMMENTS, SEND_CHAT;
	}

	public static void registerForPush(final Context context, String deviceId, String employeeNumber, RequestResponseListener listener, ProgressDialog dialog) {

		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "registerdevice.mobi");
			json.accumulate("registrationId", deviceId);
			json.accumulate("employeeNum", employeeNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "json.toString() " + json.toString());

		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}

	public static void getOpenIncidents(Context context, RequestResponseListener listener, ProgressDialog dialog) {

		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "incidents.mobi");
			json.accumulate("employeeNum", Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}

	public static void registerUser(Context context, RequestResponseListener listener, ProgressDialog dialog, String employeeNumber) {

		GCMer.onCreate(context, employeeNumber, listener, dialog);

	}

	public static void acceptIncident(Context context, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "acceptincident.mobi");
			json.accumulate("employeeNum", employeeNum);
			json.accumulate("incidentId", incidentId);
			json.accumulate("accept", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void declineIncident(Context context, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, String reason) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "acceptincident.mobi");
			json.accumulate("employeeNum", employeeNum);
			json.accumulate("incidentId", incidentId);
			json.accumulate("accept", false);
			json.accumulate("declineReason", reason);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void addComment(FragmentActivity context, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, String message) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "addincidentcomment.mobi");
			json.accumulate("employeeNum", employeeNum);
			json.accumulate("incidentId", incidentId);
			json.accumulate("comment", message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void getComments(FragmentActivity context, RequestResponseListener listener, ProgressDialog dialog, String incidentId) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "getincidentcomments.mobi");
			json.accumulate("incidentId", incidentId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void sendChat(Context context, RequestResponseListener listener, ProgressDialog dialog, String message, String employeeNum) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "sendmessage.mobi");
			json.accumulate("body", message);
			json.accumulate("senderEmployNum", employeeNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

}