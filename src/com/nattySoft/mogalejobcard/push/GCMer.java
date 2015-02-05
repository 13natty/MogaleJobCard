package com.nattySoft.mogalejobcard.push;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;

public final class GCMer {

//	private static final String LOG_TAG = GCMer.class.getSimpleName();
	
	public static void onCreate(Context context, String employeeNumber, RequestResponseListener listener, ProgressDialog dialog) {
		if (hasPlayServices(context)) {
				registerBackground(context, employeeNumber, listener, dialog);
		}
	}
		
	private static boolean hasPlayServices(final Context context) {

		final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
//				Toast.makeText(context, "Please update Play Services", Toast.LENGTH_LONG).show();
			} else {
//				Toast.makeText(context, "resultCode [" + resultCode + "]", Toast.LENGTH_LONG).show();
			}
			return false;
		}
		return true;
	}
	
//    private static boolean isRegistered(final Context context) {
//		
//    	final String registrationId = Preferences.getPreference(context, AppConstants.PreferenceKeys.KEY_GCM_REGISTRATION_ID);
////    	Log.d(LOG_TAG, "registrationId [" + registrationId + "]");
//    	
//    	if (registrationId != null) {
//    		return true;
//    	}
//		return false;
//	}
	
	private static void registerBackground(final Context context, String employeeNumber, RequestResponseListener listener, ProgressDialog dialog) {
		new GCMRegisterTask(context, employeeNumber, listener, dialog).execute();
	}
	
}
