package com.nattySoft.mogalejobcard;

public class AppConstants {
public static class Config {
    	
		public static String HOST = "http://ilanda911.com";
    	public static String SERVER_URL = HOST + "/Mogale/Controller";
    	public static String SERVER_URL_UPDATE = HOST + "/Mogale/updates";
    	public static final String GCM_SENDER_ID = "7567532847";
    	public static long KEY_START_TIME;
		public static long KEY_JOB_TIME;
    }
    
    public static class PreferenceKeys {
    	
    	public static final String KEY_REGISTERED = "registered";
    	public static final String KEY_REGISTRATION_ID = "registration_id";
    	public static final String KEY_OPENED_INCIDENTS = "opened_incidents";
    	
    	public static final String KEY_USER_ID = "UserID";
    	public static final String KEY_EMPLOYEE_NUM = "employee_number";
    	public static final String KEY_JOB_CARD_ID = "job_card_id";
    	
    	public static final String KEY_PIPELINE_INFO = "pipeline_info";
    	public static final String KEY_EXISTING_METER_INFO = "existing_meter_info";
    	public static final String KEY_NEW_METER_INFO = "new_meter_info";
    	public static final String KEY_CONNECTION_INFO = "connection_info";
    	public static final String KEY_HYDRANT = "hydrant";
		public static final String KEY_VALVE = "valve";
		public static final String KEY_ADD_CHAT = "add_chat";
		public static final String KEY_SERVER_URL = "serverurl";
		public static final String KEY_USER = "user";		
		
    }

}
