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
import com.nattySoft.mogalejobcard.MainActivity;
//import com.nattySoft.mogalejobcard.RegistrationActivity;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.push.GCMer;
import com.nattySoft.mogalejobcard.util.Preferences;

public class CommunicationHandler {

	private static final String SERVER_URL = AppConstants.Config.SERVER_URL;
	private static final String LOG_TAG = CommunicationHandler.class.getSimpleName();

	public enum Action {
		GET_ALL_OPEN_INCIDENCES, REGISTER, ACCEPT_INCIDENT, DECLINE_INCIDENT, ADD_COMMENT, GET_COMMENTS, SEND_CHAT, UPDATE_JOB_CARD, SAVE_JOB_CARD, INCIDENT_STATUS, GET_ALL_OPEN_INCIDENCES_BG;
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

	public static void saveJobCard(Activity activity, RequestResponseListener listener, ProgressDialog dialog, String employeeNum, String incidentId, String incidentStatus) {
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

			String existingmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO + FragmentIncident.incidentID);
			if (existingmeterJSONSTR != null) {
				JSONObject existingmeterJSON = new JSONObject(existingmeterJSONSTR);
				if (isValid(existingmeterJSON.getString("meterAboveOnSidewalk")))
					json.accumulate("meterAboveOnSidewalk", existingmeterJSON.getString("meterAboveOnSidewalk"));
				if (isValid(existingmeterJSON.getString("meterBelowOnSidewalk")))
					json.accumulate("meterBelowOnSidewalk", existingmeterJSON.getString("meterBelowOnSidewalk"));
				if (isValid(existingmeterJSON.getString("meterOnStandAbove")))
					json.accumulate("meterOnStandAbove", existingmeterJSON.getString("meterOnStandAbove"));
				if (isValid(existingmeterJSON.getString("meterOnStandBelow")))
					json.accumulate("meterOnStandBelow", existingmeterJSON.getString("meterOnStandBelow"));

				if (isValid(existingmeterJSON.getString("meterPosition")))
					json.accumulate("meterPosition", existingmeterJSON.getString("meterPosition"));
				if (isValid(existingmeterJSON.getString("meterNum")))
					json.accumulate("meterNum", existingmeterJSON.getString("existingMeterNo"));
				if (isValid(existingmeterJSON.getString("meterScalingFactor")))
					json.accumulate("meterScalingFactor", existingmeterJSON.getString("scalingFactor"));
				if (isValid(existingmeterJSON.getString("meterMake")))
					json.accumulate("meterMake", existingmeterJSON.getString("make"));
				if (isValid(existingmeterJSON.getString("meterDiameter")))
					json.accumulate("meterDiameter", existingmeterJSON.getString("dia"));
				if (isValid(existingmeterJSON.getString("meterReading")))
					json.accumulate("meterReading", existingmeterJSON.getString("meterReading"));
				if (isValid(existingmeterJSON.getString("numOfDigits")))
					json.accumulate("numOfDigits", existingmeterJSON.getString("noDigits"));
				if (isValid(existingmeterJSON.getString("meterReadingDate")))
					json.accumulate("meterReadingDate", existingmeterJSON.getString("meterReadingDate"));
				if (isValid(existingmeterJSON.getString("DistFromLeftCorner")))
					json.accumulate("DistFromLeftCorner", existingmeterJSON.getString("distanceFromLeft"));
				if (isValid(existingmeterJSON.getString("DistFromRighCorner")))
					json.accumulate("DistFromRighCorner", existingmeterJSON.getString("distanceFromRight"));
			}

			String newmeterJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_NEW_METER_INFO + FragmentIncident.incidentID);
			if (newmeterJSONSTR != null) {
				JSONObject newmeterJSON = new JSONObject(newmeterJSONSTR);

				if (isValid(newmeterJSON.getString("newMeterNum")))
					json.accumulate("newMeterNum", newmeterJSON.getString("existingMeterNo"));
				if (isValid(newmeterJSON.getString("meterScalingFactor")))
					json.accumulate("meterScalingFactor", newmeterJSON.getString("scalingFactor"));
				if (isValid(newmeterJSON.getString("newMeterPosition")))
					json.accumulate("newMeterPosition", newmeterJSON.getString("newMeterPosition"));
				if (isValid(newmeterJSON.getString("newMeterMake")))
					json.accumulate("newMeterMake", newmeterJSON.getString("make"));
				if (isValid(newmeterJSON.getString("newMeterDiameter")))
					json.accumulate("newMeterDiameter", newmeterJSON.getString("dia"));
				if (isValid(newmeterJSON.getString("newMeterReading")))
					json.accumulate("newMeterReading", newmeterJSON.getString("meterReading"));
				if (isValid(newmeterJSON.getString("newMeterNumOfDigits")))
					json.accumulate("newMeterNumOfDigits", newmeterJSON.getString("noDigits"));
			}

			String connectionJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO + FragmentIncident.incidentID);
			if (connectionJSONSTR != null) {
				JSONObject connectionJSON = new JSONObject(connectionJSONSTR);

				if (isValid(connectionJSON.getString("connectionServiceType")))
					json.accumulate("connectionServiceType", connectionJSON.getString("connectionServiceType"));
				if (isValid(connectionJSON.getString("connectionLegth")))
					json.accumulate("connectionLegth", connectionJSON.getString("connectionLegth"));
				if (isValid(connectionJSON.getString("connectionDiameter")))
					json.accumulate("connectionDiameter", connectionJSON.getString("connectionDiameter"));
				if (isValid(connectionJSON.getString("connectionCrossRoad")))
					json.accumulate("connectionCrossRoad", connectionJSON.getString("connectionCrossRoad"));
				if (isValid(connectionJSON.getString("connectionDepth")))
					json.accumulate("connectionDepth", connectionJSON.getString("connectionDepth"));
				if (isValid(connectionJSON.getString("connectionMaterialType")))
					json.accumulate("connectionMaterialType", connectionJSON.getString("connectionMaterialType"));
				if (isValid(connectionJSON.getString("connectionType")))
					json.accumulate("connectionType", connectionJSON.getString("connectionType"));
			}

			String pipelineJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO + FragmentIncident.incidentID);
			if (pipelineJSONSTR != null) {
				JSONObject pipelineJSON = new JSONObject(pipelineJSONSTR);

				if (isValid(pipelineJSON.getString("pipelineLocation")))
					json.accumulate("pipelineLocation", pipelineJSON.getString("pipelineLocation"));
				if (isValid(pipelineJSON.getString("pipelineDistanceFromLeft")))
					json.accumulate("pipelineDistanceFromLeft", pipelineJSON.getString("left"));
				if (isValid(pipelineJSON.getString("pipelineDistanceFromRight")))
					json.accumulate("pipelineDistanceFromRight", pipelineJSON.getString("right"));
				if (isValid(pipelineJSON.getString("pipelineCrossRoad")))
					json.accumulate("pipelineCrossRoad", pipelineJSON.getString("pipelineCrossRoad"));
				if (isValid(pipelineJSON.getString("pipelineDiameter")))
					json.accumulate("pipelineDiameter", pipelineJSON.getString("pipelineDiameter"));
				if (isValid(pipelineJSON.getString("pipelineMaterial")))
					json.accumulate("pipelineMaterial", pipelineJSON.getString("pipelineMaterial"));
				if (isValid(pipelineJSON.getString("pipelineRepairType")))
					json.accumulate("pipelineRepairType", pipelineJSON.getString("pipelineRepairType"));
				if (isValid(pipelineJSON.getString("pipelineHoleSize")))
					json.accumulate("pipelineHoleSize", pipelineJSON.getString("pipelineHoleSize"));
				if (isValid(pipelineJSON.getString("pipelineHydrantFlushTime")))
					json.accumulate("pipelineHydrantFlushTime", pipelineJSON.getString("flushHydrantTime"));
				if (isValid(pipelineJSON.getString("pipelineCode")))
					json.accumulate("pipelineCode", pipelineJSON.getString("code"));
			}

			String hydrantJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO + FragmentIncident.incidentID);
			if (hydrantJSONSTR != null) {
				JSONObject hydrantJSON = new JSONObject(hydrantJSONSTR);

				if (isValid(hydrantJSON.getString("hydrantPosition")))
					json.accumulate("hydrantPosition", hydrantJSON.getString("hydrantPosition"));
				if (isValid(hydrantJSON.getString("hydrantNum")))
					json.accumulate("hydrantNum", hydrantJSON.getString("hydrantNumber"));
				if (isValid(hydrantJSON.getString("hydrantPressure")))
					json.accumulate("hydrantPressure", hydrantJSON.getString("hydrantPressure"));
				if (isValid(hydrantJSON.getString("hydrantPressureTime")))
					json.accumulate("hydrantPressureTime", hydrantJSON.getString("hydrantTime"));
				if (isValid(hydrantJSON.getString("hydrantDistanceFromLeft")))
					json.accumulate("hydrantDistanceFromLeft", hydrantJSON.getString("hydrantLeftCorner"));
				if (isValid(hydrantJSON.getString("hydrantDistanceFromRight")))
					json.accumulate("hydrantDistanceFromRight", hydrantJSON.getString("hydrantRightCorner"));
				if (isValid(hydrantJSON.getString("hydrantReplace")))
					json.accumulate("hydrantReplace", hydrantJSON.getString("hydrantReplace"));
				if (isValid(hydrantJSON.getString("hydrantReplaceLid")))
					json.accumulate("hydrantReplaceLid", hydrantJSON.getString("hydrantReplaceLid"));
				if (isValid(hydrantJSON.getString("hydrantCloseLeak")))
					json.accumulate("hydrantCloseLeak", hydrantJSON.getString("hydrantCloseLeak"));
				if (isValid(hydrantJSON.getString("hydrantReplaceBox")))
					json.accumulate("hydrantReplaceBox", hydrantJSON.getString("hydrantReplaceBox"));
				if (isValid(hydrantJSON.getString("hydrantCode")))
					json.accumulate("hydrantCode", hydrantJSON.getString("code"));
			}

			String valveJSONSTR = Preferences.getPreference(activity, AppConstants.PreferenceKeys.KEY_CONNECTION_INFO + FragmentIncident.incidentID);
			if (valveJSONSTR != null) {
				JSONObject valveJSON = new JSONObject(valveJSONSTR);

				if (isValid(valveJSON.getString("valveRepackGland")))
					json.accumulate("valveRepackGland", valveJSON.getString("valveRepackGland"));
				if (isValid(valveJSON.getString("valveTightenGland")))
					json.accumulate("valveTightenGland", valveJSON.getString("valveTightenGland"));
				if (isValid(valveJSON.getString("valveReplaceLid")))
					json.accumulate("valveReplaceLid", valveJSON.getString("valveReplaceLid"));
				if (isValid(valveJSON.getString("valveReplaceRSVValve")))
					json.accumulate("valveReplaceRSVValve", valveJSON.getString("valveReplaceRSVValve"));
				if (isValid(valveJSON.getString("valveReplaceIronValve")))
					json.accumulate("valveReplaceIronValve", valveJSON.getString("valveReplaceIronValve"));

				if (isValid(valveJSON.getString("openTime")))
					json.accumulate("openTime", valveJSON.getString("valveOpenTime"));
				if (isValid(valveJSON.getString("closeTime")))
					json.accumulate("closeTime", valveJSON.getString("valveClosedTime"));
				if (isValid(valveJSON.getString("numOfTurns")))
					json.accumulate("numOfTurns", valveJSON.getString("valveNumTurns"));
				if (isValid(valveJSON.getString("leftSide")))
					json.accumulate("leftSide", valveJSON.getString("leftSide"));
				if (isValid(valveJSON.getString("RightSide")))
					json.accumulate("RightSide", valveJSON.getString("RightSide"));
				if (isValid(valveJSON.getString("valveNum")))
					json.accumulate("valveNum", valveJSON.getString("valveNo"));
				if (isValid(valveJSON.getString("oppositeStand")))
					json.accumulate("oppositeStand", valveJSON.getString("oppStd"));
				if (isValid(valveJSON.getString("houseNum")))
					json.accumulate("houseNum", valveJSON.getString("hseNo"));
				if (isValid(valveJSON.getString("streetName")))
					json.accumulate("streetName", valveJSON.getString("streetName"));
				if (isValid(valveJSON.getString("repairCode")))
					json.accumulate("repairCode", valveJSON.getString("repairCode"));
				if (isValid(valveJSON.getString("distanceFromLeftBoundary")))
					json.accumulate("distanceFromLeftBoundary", valveJSON.getString("distanceLeft"));
				if (isValid(valveJSON.getString("distanceFromRightBoundary")))
					json.accumulate("distanceFromRightBoundary", valveJSON.getString("distanceRight"));
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

}