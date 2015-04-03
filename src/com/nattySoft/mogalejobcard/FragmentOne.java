package com.nattySoft.mogalejobcard;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.R;
import com.nattySoft.mogalejobcard.AppConstants;
import com.nattySoft.mogalejobcard.MainActivity;
import com.nattySoft.mogalejobcard.listener.IncidentClickedListener;
import com.nattySoft.mogalejobcard.util.Preferences;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentOne extends Fragment implements IncidentClickedListener, OnWindowFocusChangeListener {

	private String TAG = FragmentOne.class.getSimpleName();

	ListView menuList;
	ArrayList<HashMap<String, String>> incidentslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> incidentsBigList = new ArrayList<HashMap<String, String>>();
	int[] icons = { R.drawable.no_water_50, R.drawable.water_meter_50, R.drawable.burst_pipe_50, R.drawable.water_pump_50, R.drawable.resevoir, R.drawable.water_tower_50 };
	ImageView ivIcon;
	TextView tvItemName;

	private SwipeRefreshLayout swipeContainer;

	private Activity mActivity;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";

	public FragmentOne() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_one, container, false);

		swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				MainActivity.action = Action.GET_ALL_OPEN_INCIDENCES;
				CommunicationHandler.getOpenIncidents(mActivity.getBaseContext(), (MainActivity) mActivity, ProgressDialog.show(mActivity, "Please wait", "Retrieving Open Incidents..."));
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

		menuList = (ListView) view.findViewById(R.id.Incidents_listView);
		ivIcon = (ImageView) view.findViewById(R.id.frag1_icon);
		tvItemName = (TextView) view.findViewById(R.id.frag1_text);

		tvItemName.setText(getArguments().getString(ITEM_NAME));
		ivIcon.setImageDrawable(view.getResources().getDrawable(getArguments().getInt(IMAGE_RESOURCE_ID)));
		String responce = Preferences.getPreference(mActivity, AppConstants.PreferenceKeys.KEY_OPENED_INCIDENTS);
		if (responce != null)
			setMenus(responce, (IncidentClickedListener) this);
		return view;
	}

	public void setMenus(String responce) {
		IncidentClickedListener listener = (IncidentClickedListener) this;
		setMenus(responce, listener);
	}

	public void setMenus(final String responce, final IncidentClickedListener listener) {

		if (responce.contains("html")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setTitle("Could Not Connect");

			WebView wv = new WebView(mActivity);
			// wv.loadUrl("http://www.google.com");
			wv.loadData(responce, "text/html", "UTF-8");

			wv.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					// view.loadData(responce, "text/html", "UTF-8");

					return true;
				}
			});

			alert.setView(wv);
			alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			alert.show();
		} else {
			JSONObject incidents = null;

			// try parse the string to a JSON object
			try {
				incidents = new JSONObject(responce);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			if (incidents != null) {
				try {

					// Getting JSON Array from URL
					String[] data = new String[incidents.length()];
					Log.d("forst size", "data1 size " + data.length);
					Log.d(TAG, "incidents " + incidents);

					JSONArray incidentsArray = incidents.getJSONArray("data");

					Log.d("forst size", "incidentsArray size " + incidentsArray.length());

					incidentslist.clear();
					incidentsBigList.clear();

					for (int i = 0; i < incidentsArray.length(); i++) {
						HashMap<String, String> bigMap = new HashMap<String, String>();
						JSONObject c = incidentsArray.getJSONObject(i);
						// Storing JSON item in a Variable
						String type = c.getString("type");
						String created = c.getString("created");
						String description = c.getString("description");
						// Adding some value HashMap key => value
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("type", type);
						map.put("created", created);
						map.put("description", description);

						incidentslist.add(map);

						// Adding all value HashMap key => value
						bigMap.put("status", c.getString("status"));
						bigMap.put("reporterSurname", c.getString("reporterSurname"));
						// assignee object
						JSONArray assigneeArr = c.optJSONArray("assignees");
						if (assigneeArr != null) {
							bigMap.put("assigneeSize", "" + assigneeArr.length());
							for (int j = 0; j < assigneeArr.length(); j++) {
								JSONObject d = assigneeArr.getJSONObject(j);
								bigMap.put("assigneeId_" + j, d.getString("id"));
								bigMap.put("assigneeSuperiorId_" + j, d.getString("superiorId"));
								bigMap.put("assigneeEmployeeNum_" + j, d.getString("employeeNum"));
								bigMap.put("assigneeEmail_" + j, d.getString("email"));
								bigMap.put("assigneeCellphone_" + j, d.getString("cellphone"));
								String name = d.getString("name");
								bigMap.put("assigneeName_" + j, d.getString("name"));
								bigMap.put("designation_" + j, d.getString("designation"));
								bigMap.put("assigneeSurname_" + j, d.getString("surname"));
								bigMap.put("active_" + j, d.getString("active"));
								bigMap.put("password_" + j, d.getString("password"));
							}
						}

						JSONArray accepteeArr = c.optJSONArray("acceptees");
						boolean acctepted = false;
						if (accepteeArr != null) {
							bigMap.put("accepteeSize", "" + accepteeArr.length());
							for (int j = 0; j < accepteeArr.length(); j++) {
								JSONObject d = accepteeArr.getJSONObject(j);
								bigMap.put("accepteeId_" + j, d.getString("id"));
								bigMap.put("accepteeSuperiorId_" + j, d.getString("superiorId"));
								bigMap.put("accepteeEmployeeNum_" + j, d.getString("employeeNum"));
								bigMap.put("accepteeEmail_" + j, d.getString("email"));
								bigMap.put("accepteeCellphone_" + j, d.getString("cellphone"));
								bigMap.put("accepteeName_" + j, d.getString("name"));
								bigMap.put("designation_" + j, d.getString("designation"));
								bigMap.put("accepteeSurname_" + j, d.getString("surname"));
								bigMap.put("active_" + j, d.getString("active"));
								bigMap.put("password_" + j, d.getString("password"));
								if (Preferences.getPreference(mActivity, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM) != null) {
									if (Preferences.getPreference(mActivity, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM).equals(d.optString("employeeNum"))) {
										acctepted = true;
									}
								}
							}
						}
						if (acctepted) {
							bigMap.put("accepted", "true");
						} else {
							bigMap.put("accepted", "false");
						}
						bigMap.put("refNum", c.getString("refNum"));
						bigMap.put("type", type);
						bigMap.put("severity", c.getString("severity"));
						bigMap.put("reporterName", c.getString("reporterName"));
						bigMap.put("id", c.getString("id"));
						bigMap.put("accountNumber", c.getString("accountNumber"));
						bigMap.put("township", c.optString("township"));
						bigMap.put("source", c.optString("source"));
						bigMap.put("stand", c.optString("stand"));
						bigMap.put("created", created);
						bigMap.put("description", description);
						bigMap.put("reporterIdNumber", c.optString("reporterIdNumber"));
						bigMap.put("reporterContact", c.optString("reporterContact"));
						bigMap.put("portion", c.optString("portion"));
						bigMap.put("street", c.optString("street"));
						bigMap.put("building", c.optString("building"));
						incidentsBigList.add(bigMap);
					}

					incidentAdapeter adapter = new incidentAdapeter(mActivity, icons, incidentsBigList);
					menuList.setAdapter(adapter);
					// listener.hasBeenClicked(incidentsBigList.get(0));
					menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Log.d("ITEM clicked", "productlist " + incidentsBigList.toString());
							Log.d("ITEM clicked", "position " + position);
							Log.d("ITEM clicked", "id " + id);
							listener.hasBeenClicked(incidentsBigList.get(position));
						}
					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void hasBeenClicked(HashMap<String, String> item) {
		Log.d("FragmentOne", "clicked incident " + item);
		Bundle args = new Bundle();
		args.putSerializable("HashMap", item);
		((MainActivity) mActivity).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
		Fragment fragment = new FragmentIncident();
		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

	class incidentAdapeter extends ArrayAdapter<String> {

		Context context;
		int[] icons;
		int[] severity = { R.drawable.minor, R.drawable.moderate, R.drawable.major, R.drawable.critical };
		ArrayList<HashMap<String, String>> productlist;
		private View row;

		public incidentAdapeter(Context context, int[] icons, ArrayList<HashMap<String, String>> productlist) {
			super(context, R.layout.incident_intro_item);
			this.context = context;
			this.icons = icons;
			this.productlist = productlist;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.incident_intro_item, parent, false);
			ImageView myImage = (ImageView) row.findViewById(R.id.type_imageView);
			TextView desc = (TextView) row.findViewById(R.id.title_text);
			TextView dateText = (TextView) row.findViewById(R.id.date_text);
			TextView ID = (TextView) row.findViewById(R.id.id_text);
			ImageView severityImage = (ImageView) row.findViewById(R.id.severity_imageView);
			ImageView accepted = (ImageView) row.findViewById(R.id.acceptedimage);
			if (productlist.get(position).get("accepted") != null) {
				if (productlist.get(position).get("accepted").equals("true")) {
					accepted.setImageResource(R.drawable.ic_action_good);
				}
			}

			int category = 0;
			Log.d("getView", "pos " + position);
			switch (productlist.get(position).get("type")) {
			case "No Water":
				category = 0;
				break;
			case "Water Meter":
				category = 1;
				break;
			case "Water Pipe Bust":
				category = 2;
				break;
			case "Water Pump":
				category = 3;
				break;
			case "Water Reservoir":
				category = 4;
				break;
			case "Water Tower":
				category = 5;
				break;

			default:
				break;
			}

			int severe = 0;
			switch (productlist.get(position).get("severity")) {
			case "Critical":
				severe = 3;
				break;
			case "Major":
				severe = 2;
				break;
			case "Minor":
				severe = 0;
				break;
			case "Moderate":
				severe = 1;
				break;
			default:
				break;
			}

			myImage.setImageResource(icons[category]);
			String descriprionText = productlist.get(position).get("description");
			if (descriprionText.length() > 35) {
				descriprionText = descriprionText.substring(0, 33) + "...";
			}
			desc.setText(descriprionText);
			dateText.setText((productlist.get(position).get("created")).substring(0, 10));
			Log.d(TAG, "ID " + productlist.get(position).get("id"));
			ID.setText("ID : " + productlist.get(position).get("id"));

			severityImage.setImageResource(severity[severe]);

			return row;
		}

		@Override
		public int getCount() {
			int listSize = productlist.size();
			int count = super.getCount();
			int productlistC = productlist.size();
			Log.d("inside getCount", "productlist.size() " + listSize);
			Log.d("inside getCount", "superC " + count);
			Log.d("inside getCount", "productlistC " + productlistC);
			if (count < 1) {
				return productlistC;
			}

			return count;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Intent intent = mActivity.getIntent();
		if (intent != null && intent.getIntExtra("type", 0) == 1) {
			Log.d(TAG, "call method now");

		}

	}

}