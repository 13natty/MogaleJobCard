package com.nattySoft.mogalejobcard;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.FragmentOne.incidentAdapeter;
import com.nattySoft.mogalejobcard.listener.IncidentClickedListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentThree extends Fragment implements IncidentClickedListener {

    // ImageView ivIcon;
//    TextView tvItemName;

    private ArrayList<HashMap<String, String>> escalationsList = new ArrayList<HashMap<String, String>>();
    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";
    private SwipeRefreshLayout swipeContainer;

    private Activity mActivity;

    private ListView escalationListView;

    public FragmentThree() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View view = inflater.inflate(R.layout.fragment_layout_three, container,
		false);

	swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
	// Setup refresh listener which triggers new data loading
	swipeContainer.setOnRefreshListener(new OnRefreshListener() {
	    @Override
	    public void onRefresh() {
		// Your code to refresh the list here.
		// Make sure you call swipeContainer.setRefreshing(false)
		// once the network request has completed successfully.
		MainActivity.action = Action.GET_ESCALATED_INCIDENCES_ASSIGNED_TO_ME;
		CommunicationHandler.getIncidentsAssignedTome(mActivity.getBaseContext(), (MainActivity) mActivity,ProgressDialog.show(mActivity, "Please wait","Retrieving Escaleted Incidents..."));
	    }
	});
	// Configure the refreshing colors
	swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

	// ivIcon = (ImageView) view.findViewById(R.id.frag3_icon);
//	tvItemName = (TextView) view.findViewById(R.id.frag3_text);

//	tvItemName.setText(getArguments().getString(ITEM_NAME));
	// ivIcon.setImageDrawable(view.getResources().getDrawable(
	// getArguments().getInt(IMAGE_RESOURCE_ID)));

	escalationListView = (ListView) view.findViewById(R.id.escalation_list);

	// Defined Array values to show in ListView
//	String[] values = new String[] { "Android List View",
//		"Adapter implementation", "Simple List View In Android",
//		"Create List View Android", "Android Example",
//		"List View Source Code", "List View Array Adapter",
//		"Android Example List View" };

	// Define a new Adapter
	// First parameter - Context
	// Second parameter - Layout for the row
	// Third parameter - ID of the TextView to which the data is written
	// Forth - the Array of data

//	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
//		.getApplicationContext(), android.R.layout.simple_list_item_1,
//		android.R.id.text1, values) {
//	    @Override
//	    public View getView(int position, View convertView, ViewGroup parent) {
//		TextView textView = (TextView) super.getView(position,
//			convertView, parent);
//
//		// String currentLocation =
//		// RouteFinderBookmarksActivity.this.getResources().getString(R.string.Current_Location);
//		// int textColor =
//		// textView.getText().toString().equals(currentLocation) ?
//		// R.color.holo_blue : R.color.text_color_btn_holo_dark;
//		textView.setTextColor(Color.BLACK);
//
//		return textView;
//	    }
//	};

	// Assign adapter to ListView
//	listView.setAdapter(adapter);

	String responce = Preferences
		.getPreference(
			mActivity,
			AppConstants.PreferenceKeys.KEY_OPENED_INCIDENTS_ASSIGNED_TO_ME);
	if (responce != null)
	    populateEscalations(responce, (IncidentClickedListener) this);

	return view;
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	mActivity = activity;
    }

    public void populateEscalations(String responce, IncidentClickedListener incidentClickedListener) {

	Log.d(getTag(), "populateEscalations " + responce);

	JSONObject responceJson = null;
	try {
	    responceJson = new JSONObject(responce);
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (responceJson != null) {
	    try {

		// Getting JSON Array from URL
		String[] data = new String[responceJson.length()];
		Log.d("forst size", "data1 size " + data.length);
		Log.d(getTag(), "incidents " + responceJson);

		JSONArray escalationsArray = responceJson.getJSONArray("data");
		escalationsList.clear();

		for (int i = 0; i < escalationsArray.length(); i++) {

		    JSONObject c = escalationsArray.getJSONObject(i);
		    // Storing JSON item in a Variable
		    String title = c.getString("description");
		    String incidentRefNum = c.getString("incidentRefNum");
		    String incidentId = c.getString("incidentId");
		    // Adding some value HashMap key => value
		    HashMap<String, String> map = new HashMap<String, String>();
		    map.put("title", title);
		    map.put("incidentRefNum", incidentRefNum);
		    map.put("incidentId", incidentId);

		    JSONArray tasksArr = c.optJSONArray("tasks");
		    if (tasksArr != null) {
			for (int j = 0; j < tasksArr.length(); j++) {
			    JSONObject d = tasksArr.getJSONObject(j);
			    map.put("designation",
				    d.getString("assigneDesignation"));
			    map.put("description", d.getString("description"));
			    map.put("assignee", d.getString("assignee"));
			    map.put("date", d.getString("dateAssigned"));
			    map.put("taskId", d.getString("taskId"));
			    escalationsList.add(map);
			}
			
		    }

		}
		incidentAdapeter adapter = new incidentAdapeter(
			mActivity, escalationsList);
		escalationListView.setAdapter(adapter);
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	}

    }

    class incidentAdapeter extends ArrayAdapter<String> {

	Context context;
	ArrayList<HashMap<String, String>> escalationslist;
	private View row;

	public incidentAdapeter(Context context,
		ArrayList<HashMap<String, String>> escalationslist) {
	    super(context, R.layout.escalation_item);
	    this.context = context;
	    this.escalationslist = escalationslist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    LayoutInflater inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    row = inflater.inflate(R.layout.escalation_item, parent, false);

	    TextView title = (TextView) row.findViewById(R.id.tv_title);
	    TextView assignee = (TextView) row.findViewById(R.id.tv_assignee);
	    TextView assignee_value = (TextView) row
		    .findViewById(R.id.tv_assignee_value);
	    TextView designation = (TextView) row
		    .findViewById(R.id.tv_designation);
	    TextView designation_value = (TextView) row
		    .findViewById(R.id.tv_designation_value);
	    TextView date = (TextView) row.findViewById(R.id.tv_date);
	    TextView date_value = (TextView) row
		    .findViewById(R.id.tv_date_value);
	    TextView description = (TextView) row
		    .findViewById(R.id.tv_description);
	    TextView description_value = (TextView) row
		    .findViewById(R.id.tv_description_value);

	    title.setText(escalationslist.get(position).get("title"));
	    assignee_value.setText(escalationslist.get(position)
		    .get("assignee"));
	    designation_value.setText(escalationslist.get(position).get(
		    "designation"));
	    date_value.setText(escalationslist.get(position).get("date"));
	    description_value.setText(escalationslist.get(position).get(
		    "description"));

	    return row;
	}

	@Override
	public int getCount() {
	    int listSize = escalationslist.size();
	    int count = super.getCount();
	    int escalationslistC = escalationslist.size();
	    if (count < 1) {
		return escalationslistC;
	    }

	    return count;
	}
    }

    @Override
    public void hasBeenClicked(HashMap<String, String> item) {
	// TODO Auto-generated method stub

    }

}