package com.nattySoft.mogalejobcard.push;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.analytics.n;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;
import com.nattySoft.mogalejobcard.DialogClass;
import com.nattySoft.mogalejobcard.FragmentIncident;
import com.nattySoft.mogalejobcard.MainActivity;
import com.nattySoft.mogalejobcard.R;
import com.nattySoft.mogalejobcard.listener.PushListener;
//import com.nattySoft.mogale.ChatActivity;
//import com.nattySoft.mogale.MainActivity;
//import com.nattySoft.mogale.listener.ChatResponceListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;

public final class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

	private static final String LOG_TAG = GCMBroadcastReceiver.class.getSimpleName();
	private static final String REGISTRATION_SUCCESS = "0";
	private static final String NEW_INCIDENT = "1";
	private static final String INCIDENT_UPDATE = "2";
	private static final String CHAT_MESSAGE = "3";
	private static final String INCIDENT_ACCEPT = "4";
	public static PushListener pushListener = null;

	public static void setListener(PushListener pushListener)
	{
		GCMBroadcastReceiver.pushListener = pushListener;
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {

		Log.d(LOG_TAG, "onReceive(" + intent + ")");

		final Bundle extras = intent.getExtras();
		final GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(context);
		final String messageType = googleCloudMessaging.getMessageType(intent);
		
		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				Log.d(LOG_TAG,
	                    "extras.size() " + extras.size());
				Log.d(LOG_TAG,
	                    "extras.toString() " + extras.toString());
				Log.d(LOG_TAG,
	                    "Message received: " + extras.getString("message"));
				
				RunningTaskInfo info = isAppInForeground(context);
				
				if(info!=null)
				{
					String className = info.topActivity.getShortClassName();
					if(className.equalsIgnoreCase(".MainActivity"))
					{	
						if(pushListener != null)
							pushListener.pushReceived(context, intent);						
					}
				}

				NotificationManager notificationManager = (NotificationManager) context
			            .getSystemService(Context.NOTIFICATION_SERVICE);
				String message = extras.getString("message");
			    Notification notification = new Notification(R.drawable.mogale_icon_push, message, System.currentTimeMillis());

			    Intent notificationIntent = new Intent(context, MainActivity.class);
			    
			    if (extras.containsKey("type")) {

					if (extras.getString("type").equalsIgnoreCase(REGISTRATION_SUCCESS)) {

					} else if (extras.getString("type").equalsIgnoreCase(NEW_INCIDENT)) {
						notificationIntent.putExtra("action", "newIncident");

					} else if (extras.getString("type").equalsIgnoreCase(INCIDENT_UPDATE)) {

					} else if (extras.getString("type").equalsIgnoreCase(CHAT_MESSAGE)) {

					} else if (extras.getString("type").equalsIgnoreCase(INCIDENT_ACCEPT)) {
					

					}
			    }
			    
			    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

			    PendingIntent pIntent = PendingIntent.getActivity(context, 0,
			            notificationIntent, 0);

			    notification.setLatestEventInfo(context, "", message, pIntent);
			    notification.flags |= Notification.FLAG_AUTO_CANCEL;
			    notification.defaults |= Notification.DEFAULT_SOUND;
			    notification.defaults |= Notification.DEFAULT_VIBRATE;
			    
			    notificationManager.notify(0, notification);
			}
		}
	}
	
	// foreground---
    public static RunningTaskInfo isAppInForeground(Context context) {
        List<RunningTaskInfo> tasks =
            ((ActivityManager) context.getSystemService(
             Context.ACTIVITY_SERVICE))
             .getRunningTasks(1);
        if (tasks.isEmpty()) {
            return null;
        }
        
        RunningTaskInfo info = tasks.get(0);
        String packageName = info.topActivity.getPackageName();
        String className = info.topActivity.getShortClassName();
      
        return info;
//        return tasks
//            .get(0)
//            .topActivity
//            .getPackageName()
//            .equalsIgnoreCase(context.getPackageName());
    }

	private void startNotification(final Context context, final String message) {

		final Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
		
		final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(com.nattySoft.mogalejobcard.R.drawable.ic_launcher)
				.setContentTitle("Mogale")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message);
		
		notificationManager.notify(1337, mBuilder.build());
	}
}