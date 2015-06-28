package com.nattySoft.mogalejobcard.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.ConnectionManager;
import com.nattySoft.mogalejobcard.AppConstants;
import com.nattySoft.mogalejobcard.FragmentIncident;
import com.nattySoft.mogalejobcard.MainActivity;
//import com.nattySoft.mogalejobcard.RegistrationActivity;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

public class CommunicationHandler {

	private static final String SERVER_URL = AppConstants.Config.SERVER_URL;
	private static final String LOG_TAG = CommunicationHandler.class.getSimpleName();

	public enum Action {
		GET_ALL_USERS, GET_USER, GET_ALL_OPEN_INCIDENCES, REGISTER, ACCEPT_INCIDENT, DECLINE_INCIDENT, ADD_COMMENT, GET_COMMENTS, SEND_CHAT, UPDATE_JOB_CARD, SAVE_JOB_CARD, INCIDENT_STATUS, GET_ALL_OPEN_INCIDENCES_BG, RE_ASSIGN, GET_INCIDENCES_ASSIGNED_TO_ME;
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
	
	public static void getIncidentsAssignedTome(Context context, RequestResponseListener listener, ProgressDialog dialog) {

		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "assignedtome.mobi");
			json.accumulate("employeeNum", Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}
	
	public static void getAllUsers(Context context, RequestResponseListener listener, ProgressDialog dialog) {

		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "getallusers.mobi");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}
	
	public static void reassignincident(Context context, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, JSONArray assignees) {

		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "reassignincident.mobi");
			json.accumulate("employeeNum", employeeNum);
			json.accumulate("incidentId", incidentId);
			json.accumulate("assignees", assignees);
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

	public static void addComment(Activity activity, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, String message) {
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
		new ConnectionManager().post(activity, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void saveJobCard(Activity activity, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, String incidentStatus, int incidentType, long jobDuration) {
		JSONObject json = new JSONObject();
		try {
			String jobCardId = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_JOB_CARD_ID + FragmentIncident.incidentID);
			if (jobCardId == null)
				json.accumulate("nav", "savejobcard.mobi");
			else {
				MainActivity.action = Action.UPDATE_JOB_CARD;
				json.accumulate("nav", "updatejobcard.mobi");
				json.accumulate("id", jobCardId);
			}

			json.accumulate("incidentId", FragmentIncident.incidentID);
			json.accumulate("employeeNum", MainActivity.employeeNUM);
			json.accumulate("status", incidentStatus);
			json.accumulate("incidentType", incidentType);
			json.accumulate("jobDuration", jobDuration);

			String existingmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO + FragmentIncident.incidentID);
			if (existingmeterJSONSTR != null) {
				JSONObject existingmeterJSON = new JSONObject(existingmeterJSONSTR);
				if (isValid(existingmeterJSON.getString("meterNum")))
					json.accumulate("meterNum", existingmeterJSON.getString("meterNum"));
				if (isValid(existingmeterJSON.getString("meterPosition")))
					json.accumulate("meterPosition", existingmeterJSON.getString("meterPosition"));				
				if (isValid(existingmeterJSON.getString("meterType")))
					json.accumulate("meterType", existingmeterJSON.getString("meterType"));				
				if (isValid(existingmeterJSON.getString("meterMake")))
					json.accumulate("meterMake", existingmeterJSON.getString("meterMake"));				
				if (isValid(existingmeterJSON.getString("meterSize")))
					json.accumulate("meterSize", existingmeterJSON.getString("meterSize"));				
				if (isValid(existingmeterJSON.getString("meterReading")))
					json.accumulate("meterReading", existingmeterJSON.getString("meterReading"));				
				if (isValid(existingmeterJSON.getString("meterNumOfDigits")))
					json.accumulate("meterNumOfDigits", existingmeterJSON.getString("meterNumOfDigits"));
				
			}

			String newmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_NEW_METER_INFO + FragmentIncident.incidentID);
			if (newmeterJSONSTR != null) {
				JSONObject newmeterJSON = new JSONObject(newmeterJSONSTR);				
				if (isValid(newmeterJSON.getString("newMeterNum")))
					json.accumulate("newMeterNum", newmeterJSON.getString("newMeterNum"));
				if (isValid(newmeterJSON.getString("newMeterPosition")))
					json.accumulate("newMeterPosition", newmeterJSON.getString("newMeterPosition"));
				if (isValid(newmeterJSON.getString("newMeterType")))
					json.accumulate("newMeterType", newmeterJSON.getString("newMeterType"));
				if (isValid(newmeterJSON.getString("newMeterMake")))
					json.accumulate("newMeterMake", newmeterJSON.getString("newMeterMake"));
				if (isValid(newmeterJSON.getString("newMeterSize")))
					json.accumulate("newMeterSize", newmeterJSON.getString("newMeterSize"));
				if (isValid(newmeterJSON.getString("newMeterReading")))
					json.accumulate("newMeterReading", newmeterJSON.getString("newMeterReading"));
				if (isValid(newmeterJSON.getString("newMeterNumOfDigits")))
					json.accumulate("newMeterNumOfDigits", newmeterJSON.getString("newMeterNumOfDigits"));
			}

			String connectionJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO + FragmentIncident.incidentID);
			if (connectionJSONSTR != null) {
				JSONObject connectionJSON = new JSONObject(connectionJSONSTR);

				if (isValid(connectionJSON.getString("connectionServiceType")))
					json.accumulate("connectionServiceType", connectionJSON.getString("connectionServiceType"));
				if (isValid(connectionJSON.getString("connectionLegth")))
					json.accumulate("connectionLegth", connectionJSON.getString("connectionLegth"));
				if (isValid(connectionJSON.getString("connectionSize")))
					json.accumulate("connectionSize", connectionJSON.getString("connectionSize"));
				if (isValid(connectionJSON.getString("connectionCrossRoad")))
					json.accumulate("connectionCrossRoad", connectionJSON.getString("connectionCrossRoad"));
				if (isValid(connectionJSON.getString("connectionDepth")))
					json.accumulate("connectionDepth", connectionJSON.getString("connectionDepth"));
				if (isValid(connectionJSON.getString("connectionMaterialType")))
					json.accumulate("connectionMaterialType", connectionJSON.getString("connectionMaterialType"));
//				if (isValid(connectionJSON.getString("connectionType")))
//					json.accumulate("connectionType", connectionJSON.getString("connectionType"));
			}

			String pipelineJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_PIPELINE_INFO + FragmentIncident.incidentID);
			if (pipelineJSONSTR != null) {
				JSONObject pipelineJSON = new JSONObject(pipelineJSONSTR);

				if (isValid(pipelineJSON.getString("pipelineLocation")))
					json.accumulate("pipelineLocation", pipelineJSON.getString("pipelineLocation"));
//				if (isValid(pipelineJSON.getString("pipelineDistanceFromLeft")))
//					json.accumulate("pipelineDistanceFromLeft", pipelineJSON.getString("left"));
//				if (isValid(pipelineJSON.getString("pipelineDistanceFromRight")))
//					json.accumulate("pipelineDistanceFromRight", pipelineJSON.getString("right"));
				if (isValid(pipelineJSON.getString("pipelineCrossRoad")))
					json.accumulate("pipelineCrossRoad", pipelineJSON.getString("pipelineCrossRoad"));
				if (isValid(pipelineJSON.getString("pipelineDiameter")))
					json.accumulate("pipelineDiameter", pipelineJSON.getString("pipelineDiameter"));
				if (isValid(pipelineJSON.getString("pipelineMaterial")))
					json.accumulate("pipelineMaterial", pipelineJSON.getString("pipelineMaterial"));
				if (isValid(pipelineJSON.getString("pipelineRepairType")))
					json.accumulate("pipelineRepairType", pipelineJSON.getString("pipelineRepairType"));
				if (isValid(pipelineJSON.getString("pipelineCutOutLength")))
					json.accumulate("pipelineCutOutLength", pipelineJSON.getString("pipelineCutOutLength"));
			}

			String hydrantJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_HYDRANT + FragmentIncident.incidentID);
			if (hydrantJSONSTR != null) {
				JSONObject hydrantJSON = new JSONObject(hydrantJSONSTR);

				if (isValid(hydrantJSON.getString("hydrantPosition")))
					json.accumulate("hydrantPosition", hydrantJSON.getString("hydrantPosition"));
				if (isValid(hydrantJSON.getString("hydrantPressure")))
					json.accumulate("hydrantPressure", hydrantJSON.getString("hydrantPressure"));
				if (isValid(hydrantJSON.getString("hydrantPressureTime")))
					json.accumulate("hydrantPressureTime", hydrantJSON.getString("hydrantPressureTime"));
				if (isValid(hydrantJSON.getString("hydrantRepairType")))
					json.accumulate("hydrantRepairType", hydrantJSON.getString("hydrantRepairType"));
			}

			String valveJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID);
			if (valveJSONSTR != null) {
				JSONObject valveJSON = new JSONObject(valveJSONSTR);

				if (isValid(valveJSON.getString("openTime")))
					json.accumulate("openTime", valveJSON.getString("openTime"));
				if (isValid(valveJSON.getString("closeTime")))
					json.accumulate("closeTime", valveJSON.getString("closeTime"));
				if (isValid(valveJSON.getString("leftSide")))
					json.accumulate("leftSide", valveJSON.getString("leftSide"));
				if (isValid(valveJSON.getString("streetName")))
					json.accumulate("streetName", valveJSON.getString("streetName"));
				if (isValid(valveJSON.getString("valveRepairType")))
					json.accumulate("valveRepairType", valveJSON.getString("valveRepairType"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(activity, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	private static boolean isValid(String string) {
		if (string != null && !string.isEmpty()) {
			return true;
		}

		return false;
	}

	public static void getComments(Activity activity, RequestResponseListener listener, ProgressDialog dialog, String incidentId) {
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
		new ConnectionManager().post(activity, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

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

	public static void updateIncident(Context context, RequestResponseListener listener, ProgressDialog dialog, String incidentStatus) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "updateincident.mobi");
			json.accumulate("status", incidentStatus);
			json.accumulate("incidentId", FragmentIncident.incidentID);
			json.accumulate("employeeNum", MainActivity.employeeNUM);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

	}

	public static void pingIncidentReceived(Context context, RequestResponseListener listener, ProgressDialog dialog, String incidentId) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "incidentreceived.mobi");
			json.accumulate("employeeNum", Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
			json.accumulate("incidentId", incidentId); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
	}

	public static void getUser(Context context, RequestResponseListener listener) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "getuser.mobi");
			json.accumulate("employeeNum", Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, null, new Pair<String, JSONObject>(SERVER_URL, json));
		
	}

}