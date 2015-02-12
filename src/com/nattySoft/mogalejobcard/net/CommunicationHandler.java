package com.nattySoft.mogalejobcard.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
//import com.nattySoft.mogalejobcard.RegistrationActivity;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

public class CommunicationHandler {

	private static final String SERVER_URL = AppConstants.Config.SERVER_URL;
	private static final String LOG_TAG = CommunicationHandler.class.getSimpleName();

	public enum Action {
		GET_ALL_OPEN_INCIDENCES, REGISTER, ACCEPT_INCIDENT, DECLINE_INCIDENT, ADD_COMMENT, GET_COMMENTS, SEND_CHAT, SAVE_JOB_CARD, INCIDENT_STATUS;
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
	
	public static void saveJobCard(Activity activity, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId) {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("nav", "savejobcard.mobi");
			String existingmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO+FragmentIncident.incidentID);
			JSONObject existingmeterJSON = new JSONObject(existingmeterJSONSTR);
			json.accumulate("meterAboveOnSidewalk", existingmeterJSON.getString("meterAboveOnSidewalk"));
			json.accumulate("meterBelowOnSidewalk", existingmeterJSON.getString("meterBelowOnSidewalk"));
			json.accumulate("meterOnStandAbove", existingmeterJSON.getString("meterOnStandAbove"));
			json.accumulate("meterOnStandBelow", existingmeterJSON.getString("meterOnStandBelow"));
			
			json.accumulate("meterPosition", existingmeterJSON.getString("meterPosition"));
			json.accumulate("meterNum", existingmeterJSON.getString("existingMeterNo"));
			json.accumulate("meterScalingFactor", existingmeterJSON.getString("scalingFactor"));
			json.accumulate("meterMake", existingmeterJSON.getString("make"));
			json.accumulate("meterDiameter", existingmeterJSON.getString("dia"));
			json.accumulate("meterReading", existingmeterJSON.getString("meterReading"));
			json.accumulate("numOfDigits", existingmeterJSON.getString("noDigits"));
			json.accumulate("meterReadingDate", existingmeterJSON.getString("meterReadingDate"));
			json.accumulate("DistFromLeftCorner", existingmeterJSON.getString("distanceFromLeft"));
			json.accumulate("DistFromRighCorner", existingmeterJSON.getString("distanceFromRight"));
			
			String newmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_NEW_METER_INFO+FragmentIncident.incidentID);
			JSONObject newmeterJSON = new JSONObject(newmeterJSONSTR);
			
			json.accumulate("newMeterNum", newmeterJSON.getString("existingMeterNo"));
			json.accumulate("meterScalingFactor", newmeterJSON.getString("scalingFactor"));
			json.accumulate("newMeterPosition", newmeterJSON.getString("newMeterPosition"));
			json.accumulate("newMeterMake", newmeterJSON.getString("make"));
			json.accumulate("newMeterDiameter", newmeterJSON.getString("dia"));
			json.accumulate("newMeterReading", newmeterJSON.getString("meterReading"));
			json.accumulate("newMeterNumOfDigits", newmeterJSON.getString("noDigits"));
			
			String connectionJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
			JSONObject connectionJSON = new JSONObject(connectionJSONSTR);
			
			json.accumulate("connectionServiceType", connectionJSON.getString("connectionServiceType"));
			json.accumulate("connectionLegth", connectionJSON.getString("connectionLegth"));
			json.accumulate("connectionDiameter", connectionJSON.getString("connectionDiameter"));
			json.accumulate("connectionCrossRoad", connectionJSON.getString("connectionCrossRoad"));
			json.accumulate("connectionDepth", connectionJSON.getString("connectionDepth"));
			json.accumulate("connectionMaterialType", connectionJSON.getString("connectionMaterialType"));
			json.accumulate("connectionType", connectionJSON.getString("connectionType"));
			
			String pipelineJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
			JSONObject pipelineJSON = new JSONObject(pipelineJSONSTR);
			
			json.accumulate("pipelineLocation", pipelineJSON.getString("pipelineLocation"));
			json.accumulate("pipelineDistanceFromLeft", pipelineJSON.getString("left"));
			json.accumulate("pipelineDistanceFromRight", pipelineJSON.getString("right"));
			json.accumulate("pipelineCrossRoad", pipelineJSON.getString("pipelineCrossRoad"));
			json.accumulate("pipelineDiameter", pipelineJSON.getString("pipelineDiameter"));
			json.accumulate("pipelineMaterial", pipelineJSON.getString("pipelineMaterial"));
			json.accumulate("pipelineRepairType", pipelineJSON.getString("pipelineRepairType"));
			json.accumulate("pipelineHoleSize", pipelineJSON.getString("pipelineHoleSize"));
			json.accumulate("pipelineHydrantFlushTime", pipelineJSON.getString("flushHydrantTime"));
			json.accumulate("pipelineCode", pipelineJSON.getString("code"));
			
			String hydrantJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
			JSONObject hydrantJSON = new JSONObject(hydrantJSONSTR);
			
			json.accumulate("hydrantPosition", hydrantJSON.getString("hydrantPosition"));
			json.accumulate("hydrantNum", hydrantJSON.getString("hydrantNumber"));
			json.accumulate("hydrantPressure", hydrantJSON.getString("hydrantPressure"));
			json.accumulate("hydrantPressureTime", hydrantJSON.getString("hydrantTime"));
			json.accumulate("hydrantDistanceFromLeft", hydrantJSON.getString("hydrantLeftCorner"));
			json.accumulate("hydrantDistanceFromRight", hydrantJSON.getString("hydrantRightCorner"));
			json.accumulate("hydrantReplace", hydrantJSON.getString("hydrantReplace"));
			json.accumulate("hydrantReplaceLid", hydrantJSON.getString("hydrantReplaceLid"));
			json.accumulate("hydrantCloseLeak", hydrantJSON.getString("hydrantCloseLeak"));
			json.accumulate("hydrantReplaceBox", hydrantJSON.getString("hydrantReplaceBox"));
			json.accumulate("hydrantCode", hydrantJSON.getString("code"));
			
			String valveJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
			JSONObject valveJSON = new JSONObject(valveJSONSTR);
			
			json.accumulate("valveRepackGland", valveJSON.getString("valveRepackGland"));
			json.accumulate("valveTightenGland", valveJSON.getString("valveTightenGland"));
			json.accumulate("valveReplaceLid", valveJSON.getString("valveReplaceLid"));
			json.accumulate("valveReplaceRSVValve", valveJSON.getString("valveReplaceRSVValve"));
			json.accumulate("valveReplaceIronValve", valveJSON.getString("valveReplaceIronValve"));
			
			json.accumulate("openTime", valveJSON.getString("valveOpenTime"));
			json.accumulate("closeTime", valveJSON.getString("valveClosedTime"));
			json.accumulate("numOfTurns", valveJSON.getString("valveNumTurns"));
			json.accumulate("leftSide", valveJSON.getString("leftSide"));
			json.accumulate("RightSide", valveJSON.getString("RightSide"));
			json.accumulate("valveNum", valveJSON.getString("valveNo"));
			json.accumulate("oppositeStand", valveJSON.getString("oppStd"));
			json.accumulate("houseNum", valveJSON.getString("hseNo"));
			json.accumulate("streetName", valveJSON.getString("streetName"));
			json.accumulate("repairCode", valveJSON.getString("repairCode"));
			json.accumulate("distanceFromLeftBoundary", valveJSON.getString("distanceLeft"));
			json.accumulate("distanceFromRightBoundary", valveJSON.getString("distanceRight"));
			
			json.accumulate("incidentId", FragmentIncident.incidentID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(activity, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));

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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(LOG_TAG, "SERVER_URL " + SERVER_URL);
		Log.d(LOG_TAG, "nameValuePairs.toString() " + json.toString());
		new ConnectionManager().post(context, listener, dialog, new Pair<String, JSONObject>(SERVER_URL, json));
		
	}

}