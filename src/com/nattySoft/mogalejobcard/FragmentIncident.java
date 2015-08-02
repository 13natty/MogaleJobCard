package com.nattySoft.mogalejobcard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.AppConstants;
import com.nattySoft.mogalejobcard.MainActivity;
import com.nattySoft.mogalejobcard.R;
import com.nattySoft.mogalejobcard.FragmentOne.incidentAdapeter;
import com.nattySoft.mogalejobcard.R.id;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.util.Preferences;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentIncident extends Fragment {

    ImageView image;
    TextView statusText;
    TextView referenceText;
    TextView IDText;
    TextView typeText;
    TextView title;

    Button acceptButton;
    Button commentButton;
    Button jobCardButton;
    Button declineButton;
    Button assignButton;
    private ArrayList<HashMap<String, String>> assigneeAList;
    private ArrayList<HashMap<String, String>> accepteeAList;
    public static String incidentID = null;
    private LayoutInflater inflater;
    private TableRow.LayoutParams params;
    private boolean[] selectedAssignees;
    private JSONArray AssignedAssignees = null;
    private String[] allUsersData;
    private TextView addressText;
    private TextView reporterNameSurname;
    private TextView reporterPhone;
    private Activity mActivity;
    private ListView assigneeListView;

    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";

    public FragmentIncident() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	this.inflater = inflater;
	View view = inflater.inflate(R.layout.fragment_layout_incident, container, false);
	assigneeListView = (ListView) view.findViewById(R.id.assignees_listView);
	setHasOptionsMenu(true);
	// ivIcon = (ImageView) view.findViewById(R.id.frag3_icon);
	// tvItemName = (TextView) view.findViewById(R.id.frag3_text);
	//
	// tvItemName.setText(getArguments().getString(ITEM_NAME));
	// ivIcon.setImageDrawable(view.getResources().getDrawable(getArguments().getInt(IMAGE_RESOURCE_ID)));
	return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	statusText = (TextView) getActivity().findViewById(R.id.status_text);
	typeText = (TextView) getActivity().findViewById(R.id.text_type);
	referenceText = (TextView) getActivity().findViewById(R.id.refrence_text);
	IDText = (TextView) getActivity().findViewById(R.id.id_text);
	image = (ImageView) getActivity().findViewById(R.id.imageView_type);
	title = (TextView) getActivity().findViewById(R.id.title);
	addressText = (TextView) getActivity().findViewById(R.id.address_text);
	reporterNameSurname = (TextView) getActivity().findViewById(R.id.reporter_name);
	reporterPhone = (TextView) getActivity().findViewById(R.id.reporter_phone);

	int left = 6;
	int top = 12;
	int right = 6;
	int bottom = 6;

	params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	params.setMargins(left, top, right, bottom);

	acceptButton = (Button) getActivity().findViewById(R.id.button_accept);
	acceptButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		MainActivity.action = Action.ACCEPT_INCIDENT;
		CommunicationHandler.acceptIncident(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Accepting Incidents..."), Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID);
	    }
	});

	commentButton = (Button) getActivity().findViewById(R.id.comment_button);
	commentButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new AlertDialog.Builder(getActivity()).setTitle("Comments").setMessage("Choose a button below").setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			// custom dialog
			final Dialog dialogAdd = new Dialog(getActivity());
			dialogAdd.setContentView(R.layout.add_comment);
			String concated = (String) (title.getText().length() > 13 ? title.getText().subSequence(0, 10) + "..." : title.getText());
			dialogAdd.setTitle("Comment on " + concated);

			Button sendButton = (Button) dialogAdd.findViewById(R.id.comment_send);
			// if button is clicked, close the
			// custom dialog
			sendButton.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
				EditText message = (EditText) dialogAdd.findViewById(R.id.comment_message);
				MainActivity.action = Action.ADD_COMMENT;
				CommunicationHandler.addComment(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Sending your comment..."), Preferences.getPreference(FragmentIncident.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID, message.getText().toString());
				dialogAdd.dismiss();
			    }
			});

			Button cancelButton = (Button) dialogAdd.findViewById(R.id.comment_cancel);
			cancelButton.setOnClickListener(new OnClickListener() {
			    @Override
			    public void onClick(View v) {
				dialogAdd.dismiss();
			    }
			});
			dialogAdd.show();
		    }
		}).setNegativeButton("View Comment", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			MainActivity.action = Action.GET_COMMENTS;
			CommunicationHandler.getComments(getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Retrieving comments..."), incidentID);
		    }
		}).setIcon(android.R.drawable.ic_dialog_alert).show();

		/*
				
				*/
	    }
	});

	jobCardButton = (Button) getActivity().findViewById(R.id.job_card);
	jobCardButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		AppConstants.Config.KEY_START_TIME = System.currentTimeMillis();
		AppConstants.Config.KEY_START_TIME = System.currentTimeMillis();

		((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));

		Fragment fragment = new FragmentJobCard();
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	    }
	});

	declineButton = (Button) getActivity().findViewById(R.id.button_decline);
	declineButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		DialogClass cdd = new DialogClass(getActivity(), incidentID);
		cdd.show();
	    }
	});

	assignButton = (Button) getActivity().findViewById(R.id.assign_job);
	assignButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		MainActivity.action = Action.GET_ALL_USERS;
		CommunicationHandler.getAllUsers(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Getting Users..."));
	    }
	});

	Bundle bundle = this.getArguments();
	HashMap<String, String> item = (HashMap<String, String>) bundle.getSerializable("HashMap");
	updateFields(item);
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	mActivity = activity;
    }

    @Override
    public void onResume() {
	invalidate();
	super.onResume();
    }

    public void invalidate() {
	this.getView().post(new Runnable() {
	    @Override
	    public void run() {
		FragmentIncident.this.getView().invalidate();
	    }
	});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	int id = item.getItemId();
	if ("Close Incident".equals(item.getTitle())) {
	    new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure you want to CLOSE this incident ?").setMessage("Remove").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		    MainActivity.action = Action.CLOSE_INCIDENT;
		    CommunicationHandler.closeIncident(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Closing incident..."), incidentID, MainActivity.employeeNUM);
		}

	    }).setNegativeButton("No", null).show();

	}
	if ("View logs".equals(item.getTitle())) {
	    MainActivity.action = Action.VIEW_LOGS;
	    CommunicationHandler.getincidentlogs(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Getting incident logs..."), incidentID);
	}
	if ("Incident comments".equals(item.getTitle())) {
	    commentButton.performClick();
	    // MainActivity.action = Action.VIEW_LOGS;asfd
	    // CommunicationHandler.getincidentlogs(FragmentIncident.this.getActivity(),
	    // (RequestResponseListener) getActivity(),
	    // ProgressDialog.show(getActivity(), "Please wait",
	    // "Getting incident logs..."), incidentID);
	}
	return false;

    }

    public void updateFields(HashMap<String, String> item) {
	if (title != null) {
	    incidentID = item.get("id");
	    switch (item.get("severity")) {
	    case "Minor":
		image.setImageResource(R.drawable.minor);
		break;
	    case "Major":
		image.setImageResource(R.drawable.major);
		break;
	    case "Critical":
		image.setImageResource(R.drawable.critical);
		break;

	    default:
		break;
	    }

	    if ("Manager".equals(MainActivity.employeeDesignation) || "Foreman".equals(MainActivity.employeeDesignation)) {
		acceptButton.setVisibility(View.GONE);
		declineButton.setVisibility(View.GONE);

		assignButton.setVisibility(View.VISIBLE);
	    } else if (item.get("accepted") != null) {
		boolean accepted = item.get("accepted").equals("true") ? true : false;
		if (accepted) {
		    acceptButton.setVisibility(View.GONE);
		    declineButton.setVisibility(View.GONE);
		    commentButton.setVisibility(View.VISIBLE);
		    jobCardButton.setVisibility(View.VISIBLE);
		    List<DrawerItem> dataList = new ArrayList<DrawerItem>();
		    dataList.add(new DrawerItem("Add Comment", R.drawable.ic_action_add_comment, "accepted"));
		    dataList.add(new DrawerItem("View Comments", R.drawable.ic_action_view_comments, "accepted"));
		    // ((MainActivity)
		    // getActivity()).addMoreToDrawer(item.get("description"),
		    // dataList);
		} else {

		    // List<DrawerItem> dataList = new ArrayList<DrawerItem>();
		    // dataList.add(new DrawerItem("Accept",
		    // R.drawable.ic_action_accept, "not_accepted"));
		    // dataList.add(new DrawerItem("Decline",
		    // R.drawable.ic_action_decline, "not_accepted"));
		    // ((MainActivity)getActivity()).addMoreToDrawer(item.get("description"),
		    // dataList);
		    acceptButton.setVisibility(View.VISIBLE);
		    declineButton.setVisibility(View.VISIBLE);
		    commentButton.setVisibility(View.GONE);
		    jobCardButton.setVisibility(View.GONE);
		}
	    }
	    statusText.setText("Status : " + item.get("status"));
	    typeText.setText("Type : " + item.get("type"));
	    referenceText.setText("Reference : " + item.get("refNum"));
	    IDText.setText("ID : " + item.get("id"));
	    title.setText(item.get("description"));
	    reporterNameSurname.setText(item.get("reporterName") + " " + item.get("reporterSurname"));
	    reporterPhone.setText(item.get("reporterContact"));

	    String addrString = null;
	    if (item.get("street").length() > 0)
		addrString = "" + item.get("street");
	    if (item.get("township").length() > 0)
		addrString += ", " + item.get("township");
	    if (item.get("building").length() > 0)
		addrString += ", " + item.get("building");
	    if (item.get("stand").length() > 0)
		addrString += ", " + item.get("stand");
	    if (item.get("portion").length() > 0)
		addrString += ", " + item.get("portion");

	    addressText.setText(addrString);

	    int assigneeSize = Integer.parseInt(item.get("assigneeSize"));
	    assigneeAList = new ArrayList<HashMap<String, String>>();

	    AssignedAssignees = new JSONArray();
	    for (int i = 0; i < assigneeSize; i++) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("assigneeId", item.get("assigneeId_" + i));
		map.put("assigneeSuperiorId", item.get("assigneeSuperiorId_" + i));
		map.put("assigneeEmployeeNum", item.get("assigneeEmployeeNum_" + i));
		AssignedAssignees.put(item.get("assigneeEmployeeNum_" + i));
		map.put("assigneeEmail", item.get("assigneeEmail_" + i));
		map.put("assigneeCellphone", item.get("assigneeCellphone_" + i));
		map.put("assigneeName", item.get("assigneeName_" + i));
		map.put("assigneeDesignation", item.get("assigneeDesignation_" + i));
		map.put("assigneeSurname", item.get("assigneeSurname_" + i));
		if (item.containsKey("assigneeIncidentStatusID_" + i)) {
		    int statusID = Integer.parseInt(item.get("assigneeIncidentStatusID_" + i));
		    map.put("assigneeIncidentStatusID", "" + statusID);
		} else {
		    map.put("assigneeIncidentStatusID", "" + 0);
		}
		map.put("assigneeActive", item.get("assigneeActive_" + i));
		map.put("assigneePassword", item.get("assigneePassword_" + i));

		assigneeAList.add(map);
	    }

	    int accepteeSize = Integer.parseInt(item.get("accepteeSize"));
	    if (accepteeSize > 0) {
		accepteeAList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < accepteeSize; i++) {
		    HashMap<String, String> map = new HashMap<String, String>();
		    map.put("accepteeId", item.get("accepteeId_" + i));
		    map.put("accepteeSuperiorId", item.get("accepteeSuperiorId_" + i));
		    map.put("accepteeEmployeeNum", item.get("accepteeEmployeeNum_" + i));
		    AssignedAssignees.put(item.get("accepteeEmployeeNum_" + i));
		    map.put("accepteeEmail", item.get("accepteeEmail_" + i));
		    map.put("accepteeCellphone", item.get("accepteeCellphone_" + i));
		    map.put("accepteeName", item.get("accepteeName_" + i));
		    map.put("accepteeDesignation", item.get("accepteeDesignation_" + i));
		    map.put("accepteeSurname", item.get("accepteeAccepteeSurname_" + i));
		    if (item.containsKey("accepteeIncidentStatusID_" + i)) {
			int statusID = Integer.parseInt(item.get("accepteeIncidentStatusID_" + i));
			map.put("accepteeIncidentStatusID", "" + statusID);
		    } else {
			map.put("accepteeIncidentStatusID", "" + 0);
		    }
		    map.put("accepteeActive", item.get("accepteeActive_" + i));
		    map.put("accepteePassword", item.get("accepteePassword_" + i));

		    accepteeAList.add(map);
		    TextView nameTV = new TextView(this.getActivity());
		    nameTV.setImeActionLabel(map.get("accepteeEmployeeNum"), -1);
		    nameTV.setText("Acceptee Name : " + map.get("accepteeName"));
		    nameTV.setLayoutParams(params);
		    // nameTV.setOnLongClickListener(this);
		    // nameTV.setBackground(getResources().getDrawable(R.drawable.assignee_border));
		}
	    }

	    assigneeAdapeter adapter = new assigneeAdapeter(mActivity, assigneeAList, accepteeAList);
	    assigneeListView.setAdapter(adapter);
	    // listener.hasBeenClicked(incidentsBigList.get(0));
	    assigneeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    // Log.d("ITEM clicked", "productlist " +
		    // incidentsBigList.toString());
		    Log.d("ITEM clicked", "position " + position);
		    Log.d("ITEM clicked", "id " + id);
		    // listener.hasBeenClicked(incidentsBigList.get(position));
		}
	    });

	}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	menu.clear();
	switch (MainActivity.employeeDesignation) {
	case "Manager":
	    inflater.inflate(R.menu.incident, menu);
	    break;
	case "Foreman":
	    inflater.inflate(R.menu.incident, menu);
	    break;

	default:
	    inflater.inflate(R.menu.artisan_incident_menu, menu);
	    break;
	}
	
	// super.onCreateOptionsMenu(menu, inflater);
    }

    public void assignUsers(String responce) {

	JSONObject userProfile = null;
	allUsersData = null;

	AssignedAssignees = new JSONArray();
	String[] allUsersDataDisplay = null;

	// try parse the string to a JSON object
	try {
	    userProfile = new JSONObject(responce);
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

	if (userProfile != null) {
	    try {

		// Getting JSON Array from URL

		JSONArray userProfileArray = userProfile.getJSONArray("data");

		allUsersData = new String[userProfileArray.length()];
		allUsersDataDisplay = new String[userProfileArray.length()];
		selectedAssignees = new boolean[userProfileArray.length()];

		Log.d("forst size", "data1 size " + allUsersData.length);
		Log.d("TAG", "userProfile " + userProfile);

		Log.d("forst size", "userProfileArray size " + userProfileArray.length());

		for (int i = 0; i < userProfileArray.length(); i++) {

		    JSONObject userProfileObject = userProfileArray.getJSONObject(i);
		    allUsersData[i] = userProfileObject.getString("employeeNum");
		    allUsersDataDisplay[i] = userProfileObject.getString("name") + " " + userProfileObject.getString("surname");

		    // if (assigneeAList.contains(userProfileObject)) {
		    // selectedAssignees[i] = true;
		    // } else if (accepteeAList.contains(userProfileObject)) {
		    // selectedAssignees[i] = true;
		    // }

		    // for (int j = 0; j < assigneeListView.getChildCount();
		    // j++) {
		    // Log.d("assigneeListView", ""+assigneeListView);
		    //
		    // assigneeAdapeter assignee =
		    // (assigneeAdapeter)assigneeListView.getAdapter().getItem(j);
		    // Log.d("assignee.employeenumber "+assignee.employeenumber,
		    // "userProfileObject.getString(\"employeeNum\") "+userProfileObject.getString("employeeNum"));
		    // //
		    // if(assignee.employeenumber.equals(userProfileObject.getString("employeeNum")))
		    //
		    // //selectedAssignees[i] = true;
		    // }
		    // for (int j = 0; j < assigneeListView.getChildCount();
		    // j++) {
		    // assigneeAdapeter assignee = (assigneeAdapeter)
		    // assigneeListView.getAdapter().getItem(j);
		    // if(assignee.employeenumber
		    // }

		    // selectedAssignees[i] =
		}

	    } catch (Exception e) {
		Log.d("TAG", "Exception " + e);
	    }

	}

	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
	// set title
	alertDialogBuilder.setTitle("Select");
	// set dialog message
	alertDialogBuilder.setCancelable(true).setMultiChoiceItems(allUsersDataDisplay, selectedAssignees, new DialogSelectionClickHandler()).setPositiveButton("Assign", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		MainActivity.action = Action.RE_ASSIGN;
		CommunicationHandler.reassignincident(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Re assigning..."), MainActivity.employeeNUM, incidentID, AssignedAssignees);
	    }
	}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		dialog.dismiss();
	    }
	});

	// create alert dialog
	AlertDialog alertDialog = alertDialogBuilder.create();
	// show it
	alertDialog.show();
    }

    public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
	public void onClick(DialogInterface dialog, int clicked, boolean selected) {
	    selectedAssignees[clicked] = selected;
	    AssignedAssignees.put(allUsersData[clicked]);
	    Log.d(getTag(), "AssignedAssignees " + AssignedAssignees.toString());
	}
    }

    public void showComments(String responce) {
	JSONObject responceJSON = null;

	// try parse the string to a JSON object
	try {
	    responceJSON = new JSONObject(responce);
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

	JSONArray data = null;
	try {
	    if (responceJSON != null)
		data = responceJSON.getJSONArray("data");
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	View dialogView = inflater.inflate(R.layout.incident_comments, null, false);

	LinearLayout commentsList = (LinearLayout) dialogView.findViewById(R.id.commentsLayout);

	// Create custom dialog object
	final Dialog dialog = new Dialog(getActivity());
	// Include dialog.xml file
	dialog.setContentView(dialogView);
	// Set dialog title
	dialog.setTitle("Comments " + title.getText());
	if (data != null) {
	    if (data.length() > 0) {
		for (int i = 0; i < data.length(); i++) {
		    // comment = (LinearLayout
		    // )inflater.inflate(R.layout.single_comment, container,
		    // false);
		    LinearLayout comment = new Comment(getActivity()).getView(getActivity(), inflater);
		    TextView name = (TextView) comment.findViewById(R.id.commentor);
		    TextView time = (TextView) comment.findViewById(R.id.commentDate);
		    TextView singleComment = (TextView) comment.findViewById(R.id.singleComment);

		    try {
			String strComment = ((JSONObject) data.getJSONObject(i)).getString("comment");
			String timestamp = ((JSONObject) data.getJSONObject(i)).getString("timestamp");
			String strName = ((JSONObject) data.getJSONObject(i)).getJSONObject("commentor").getString("name");
			strName += " " + ((JSONObject) data.getJSONObject(i)).getJSONObject("commentor").getString("surname");

			name.setText(strName);
			time.setText(timestamp.substring(0, timestamp.indexOf(".")));
			singleComment.setText(strComment);

		    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    commentsList.addView(comment);

		}
		final ScrollView scroll = (ScrollView) dialogView.findViewById(R.id.commentsScroll);

		scroll.postDelayed(new Runnable() {
		    @Override
		    public void run() {
			scroll.fullScroll(ScrollView.FOCUS_DOWN);
		    }
		}, 1000);
	    } else {
		LinearLayout comment = new Comment(getActivity()).getView(getActivity(), inflater);
		TextView name = (TextView) comment.findViewById(R.id.commentor);
		TextView time = (TextView) comment.findViewById(R.id.commentDate);
		TextView singleComment = (TextView) comment.findViewById(R.id.singleComment);

		String strComment = "There are no comments on this incident";
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		String strName = "None";
		strName += " ";

		name.setText(strName);
		time.setText(formattedDate);
		singleComment.setText(strComment);

		commentsList.addView(comment);
	    }
	} else {
	    LinearLayout comment = new Comment(getActivity()).getView(getActivity(), inflater);
	    TextView name = (TextView) comment.findViewById(R.id.commentor);
	    TextView time = (TextView) comment.findViewById(R.id.commentDate);
	    TextView singleComment = (TextView) comment.findViewById(R.id.singleComment);

	    String strComment = "There are no comments on this incident";
	    Calendar c = Calendar.getInstance();
	    System.out.println("Current time => " + c.getTime());

	    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
	    String formattedDate = df.format(c.getTime());
	    String strName = "None";
	    strName += " ";

	    name.setText(strName);
	    time.setText(formattedDate);
	    singleComment.setText(strComment);

	    commentsList.addView(comment);
	}

	dialog.show();
    }

    class assigneeAdapeter extends ArrayAdapter<String> {

	Context context;
	List<HashMap<String, String>> combined = new ArrayList<HashMap<String, String>>();

	private View row;
	// private ImageView myImage;
	private ImageView profileImage;
	private TextView name;
	private ImageView progress;
	private int incident_progressID;
	private ImageView remove;
	private String employeenumber;
	private String employeeDesignation;

	public assigneeAdapeter(Context context, ArrayList<HashMap<String, String>> assigneeList, ArrayList<HashMap<String, String>> accepteeList) {
	    super(context, R.layout.assignee_item);
	    this.context = context;
	    if (assigneeList != null)
		combined.addAll(assigneeList);
	    if (accepteeList != null)
		combined.addAll(accepteeList);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

	    ViewHolder mViewHolder = null;
	    HashMap<String, String> song = null;

	    // if (convertView == null) {

	    song = new HashMap<String, String>();
	    mViewHolder = new ViewHolder();

	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    row = inflater.inflate(R.layout.assignee_item, parent, false);

	    row.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    MainActivity.action = Action.GET_JOB_CARD;
		    if (incident_progressID == MainActivity.INCIDENT_JOB_CARD_RECEIVED) {
			CommunicationHandler.getincidentjobcard(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Geting job card..."), incidentID, employeenumber);
		    }
		}
	    });
	    // myImage = (ImageView) row.findViewById(R.id.type_imageView);
	    profileImage = (ImageView) row.findViewById(R.id.profile_imageView);
	    name = (TextView) row.findViewById(R.id.name_text);
	    progress = (ImageView) row.findViewById(R.id.assignee_progress);
	    // ID = (TextView) row.findViewById(R.id.id_text);
	    remove = (ImageButton) row.findViewById(R.id.remove_button);
	    remove.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // Ask the user if they want to remove artisan
		    new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure you want to remove assignee " + name.getText() + " ?").setMessage("").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			    AssignedAssignees.remove(position);
			    if (AssignedAssignees.length() > 0) {
				MainActivity.action = Action.REMOVE_ASSIGNEE;
				CommunicationHandler.reassignincident(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Removing assignee..."), MainActivity.employeeNUM, incidentID, AssignedAssignees);
			    } else {
				AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
				alert.setTitle("Error");
				alert.setCancelable(true);
				alert.setMessage("An incident must have atleast one assignee");
				alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				    }
				});
				alert.show();
			    }
			}

		    }).setNegativeButton("No", null).show();
		}
	    });

	    // }
	    // else {
	    //
	    // mViewHolder = (ViewHolder) convertView.getTag();
	    //
	    // }

	    if (combined != null && combined.size() > 0) {
		int progressStatus = 0;
		try{
		    progressStatus = Integer.parseInt(combined.get(position).get("accepteeIncidentStatusID"));
		}catch(Exception e){
		    progressStatus = Integer.parseInt(combined.get(position).get("assigneeIncidentStatusID"));
		}
		switch (progressStatus) {
		case 0:
		    progress.setImageResource(R.drawable.not_delivered);
		    incident_progressID = 0;
		    break;
		case MainActivity.INCIDENT_SENT:
		    progress.setImageResource(R.drawable.not_delivered);
		    incident_progressID = MainActivity.INCIDENT_SENT;
		    break;
		case MainActivity.INCIDENT_RECEIVED_ON_SERVER:
		    progress.setImageResource(R.drawable.check_symbol_16);
		    incident_progressID = MainActivity.INCIDENT_RECEIVED_ON_SERVER;
		    break;
		case MainActivity.INCIDENT_RECEIVED_ON_DEVICE:
		    progress.setImageResource(R.drawable.tick_indicator_16);
		    incident_progressID = MainActivity.INCIDENT_RECEIVED_ON_DEVICE;
		    break;
		case MainActivity.INCIDENT_READ_BY_USER:
		    progress.setImageResource(R.drawable.double_tick_indicator_received);
		    incident_progressID = MainActivity.INCIDENT_READ_BY_USER;
		    break;
		case MainActivity.INCIDENT_ACCEPTED_BY_USER:
		    progress.setImageResource(R.drawable.incident_accepted);
		    incident_progressID = MainActivity.INCIDENT_ACCEPTED_BY_USER;
		    break;
		case MainActivity.INCIDENT_DECLINED_BY_USER:
		    progress.setImageResource(R.drawable.declined);
		    incident_progressID = MainActivity.INCIDENT_DECLINED_BY_USER;
		    break;
		case MainActivity.INCIDENT_JOB_CARD_RECEIVED:
		    progress.setImageResource(R.drawable.job_card_done);
		    incident_progressID = MainActivity.INCIDENT_JOB_CARD_RECEIVED;
		    // row.setBackgroundColor(0x4fedfb);
		    break;
		default:
		    break;
		}
		try {
		    if (combined.get(position).get("accepteeEmployeeNum") != null)
			employeenumber = combined.get(position).get("accepteeEmployeeNum");
		    if (combined.get(position).get("assigneeEmployeeNum") != null)
			employeenumber = combined.get(position).get("assigneeEmployeeNum");
		} catch (Exception ex) {
		    employeenumber = combined.get(position).get("assigneeEmployeeNum");
		}
		if (MainActivity.employeeNUM.equals(employeenumber)) {
		    name.setText("Me");
		} else {
		    try {
			if (combined.get(position).get("accepteeName") != null){
			    name.setText(combined.get(position).get("accepteeName") + " " + combined.get(position).get("accepteeSurname"));			    
			}
			if (combined.get(position).get("assigneeName") != null){
			    name.setText(combined.get(position).get("assigneeName") + " " + combined.get(position).get("assigneeSurname"));			    
			}
		    } catch (Exception ex) {
			name.setText(combined.get(position).get("assigneeName") + " " + combined.get(position).get("assigneeSurname"));
		    }
		}
		if (combined.get(position).get("accepteeDesignation") != null){
		    employeeDesignation = combined.get(position).get("accepteeDesignation");			    
		}
		if (combined.get(position).get("assigneeDesignation") != null){
		    employeeDesignation = combined.get(position).get("assigneeDesignation");			    
		}

		if (progressStatus < MainActivity.INCIDENT_READ_BY_USER) {
		    MainActivity.action = Action.INCIDENT_PROGRESS;
		    CommunicationHandler.incidentProgressStatus(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), incidentID, "" + MainActivity.INCIDENT_READ_BY_USER, this.employeenumber);
		}

	    }

	    switch (employeeDesignation) {
	    case "Manager":
		profileImage.setImageResource(R.drawable.manager);
		break;
	    case "Foreman":
		profileImage.setImageResource(R.drawable.foreman_icon);
		break;

	    default:
		profileImage.setImageResource(R.drawable.artisan);
		remove.setVisibility(View.INVISIBLE);
		break;
	    }

	    return row;
	}

	@Override
	public int getCount() {
	    int totalCount = 0;
	    if (combined != null && combined.size() > 0) {

		int productlistC = combined.size();
		totalCount += productlistC;
	    }
	    Log.d(getTag(), "totalCount " + totalCount);

	    return totalCount;
	}
    }

    class viewLogsAdapeter extends ArrayAdapter<String> {

	Context context;
	private ArrayList<HashMap<String, String>> logslist;
	private View row;
	private TextView title;
	private TextView date;
	private TextView namesurname;

	public viewLogsAdapeter(Context context, ArrayList<HashMap<String, String>> logslist) {
	    super(context, R.layout.log_item);
	    this.context = context;
	    this.logslist = logslist;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

	    ViewHolder mViewHolder = null;
	    HashMap<String, String> song = null;

	    if (convertView == null) {

		song = new HashMap<String, String>();
		mViewHolder = new ViewHolder();

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.log_item, parent, false);
		// myImage = (ImageView) row.findViewById(R.id.type_imageView);
		title = (TextView) row.findViewById(R.id.log_title);
		date = (TextView) row.findViewById(R.id.log_date);
		namesurname = (TextView) row.findViewById(R.id.logger_name_surname);

	    } else {

		mViewHolder = (ViewHolder) convertView.getTag();

	    }

	    if (logslist != null && logslist.size() > 0) {
		date.setText(logslist.get(position).get("timestamp"));
		namesurname.setText(logslist.get(position).get("name") + " " + logslist.get(position).get("surname"));
		title.setText(logslist.get(position).get("description"));
	    }

	    return row;
	}

	@Override
	public int getCount() {
	    int count = super.getCount();
	    if (logslist != null && logslist.size() > 0) {
		int listSize = logslist.size();

		int productlistC = logslist.size();
		Log.d("inside getCount", "productlist.size() " + listSize);
		Log.d("inside getCount", "superC " + count);
		Log.d("inside getCount", "productlistC " + productlistC);
		if (count < 1) {
		    return productlistC;
		}
	    }
	    return count;
	}
    }

    public void updateAssignees() {
	String responce = Preferences.getPreference(mActivity, AppConstants.PreferenceKeys.KEY_MY_INCIDENTS);

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

		    JSONArray incidentsArray = incidents.getJSONArray("data");

		    Log.d("forst size", "incidentsArray size " + incidentsArray.length());

		    for (int i = 0; i < incidentsArray.length(); i++) {
			HashMap<String, String> bigMap = new HashMap<String, String>();
			JSONObject c = incidentsArray.getJSONObject(i);
			// Storing JSON item in a Variable
			String type = c.getString("type");
			String created = c.getString("created");
			String description = c.getString("description");
			String id = c.getString("id");

			if (incidentID.equals(id)) {// Adding some value HashMap
						    // key => value
			    HashMap<String, String> map = new HashMap<String, String>();
			    map.put("type", type);
			    map.put("created", created);
			    map.put("description", description);

			    // Adding all value HashMap key => value
			    bigMap.put("status", c.getString("status"));
			    bigMap.put("reporterSurname", c.getString("reporterSurname"));
			    // assignee object
			    JSONArray assigneeArr = c.optJSONArray("assignees");
			    assigneeAList = new ArrayList<HashMap<String, String>>();
			    AssignedAssignees = new JSONArray();
			    if (assigneeArr != null) {
				bigMap.put("assigneeSize", "" + assigneeArr.length());
				assigneeAList = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < assigneeArr.length(); j++) {
				    HashMap<String, String> assigneeMap = new HashMap<String, String>();
				    JSONObject d = assigneeArr.getJSONObject(j);
				    assigneeMap.put("assigneeId", d.getString("id"));
				    assigneeMap.put("assigneeSuperiorId", d.getString("superiorId"));
				    assigneeMap.put("assigneeEmployeeNum", d.getString("employeeNum"));
				    AssignedAssignees.put(d.getString("employeeNum"));
				    assigneeMap.put("assigneeEmail", d.getString("email"));
				    assigneeMap.put("assigneeCellphone", d.getString("cellphone"));
				    assigneeMap.put("assigneeName", d.getString("name"));
				    assigneeMap.put("designation", d.getString("designation"));
				    assigneeMap.put("assigneeSurname", d.getString("surname"));
				    if (!d.isNull("assignedIncidentStatus")) {
					int statusID = Integer.parseInt(d.getJSONObject("assignedIncidentStatus").getString("statusId"));
					assigneeMap.put("incidentStatusID", "" + statusID);
				    } else {
					assigneeMap.put("incidentStatusID", "" + 0);
				    }
				    assigneeMap.put("active", d.getString("active"));
				    assigneeMap.put("password", d.getString("password"));
				    assigneeAList.add(assigneeMap);
				}
			    }

			    JSONArray accepteeArr = c.optJSONArray("acceptees");
			    boolean acctepted = false;
			    if (accepteeArr != null) {
				bigMap.put("accepteeSize", "" + accepteeArr.length());
				accepteeAList = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < accepteeArr.length(); j++) {
				    JSONObject d = accepteeArr.getJSONObject(j);
				    HashMap<String, String> accepteeMap = new HashMap<String, String>();
				    accepteeMap.put("accepteeId", d.getString("id"));
				    accepteeMap.put("accepteeSuperiorId", d.getString("superiorId"));
				    accepteeMap.put("accepteeEmployeeNum", d.getString("employeeNum"));
				    AssignedAssignees.put(d.getString("employeeNum"));
				    accepteeMap.put("accepteeEmail", d.getString("email"));
				    accepteeMap.put("accepteeCellphone", d.getString("cellphone"));
				    accepteeMap.put("accepteeName", d.getString("name"));
				    accepteeMap.put("designation", d.getString("designation"));
				    accepteeMap.put("accepteeSurname", d.getString("surname"));
				    if (!d.isNull("assignedIncidentStatus")) {
					int statusID = Integer.parseInt(d.getJSONObject("assignedIncidentStatus").getString("statusId"));
					accepteeMap.put("incidentStatusID", "" + statusID);
				    } else {
					accepteeMap.put("incidentStatusID", "" + 0);
				    }
				    accepteeMap.put("active", d.getString("active"));
				    accepteeMap.put("password", d.getString("password"));

				    accepteeAList.add(accepteeMap);
				}
			    }
			}

		    }

		    assigneeAdapeter adapter = new assigneeAdapeter(mActivity, assigneeAList, accepteeAList);
		    assigneeListView.setAdapter(adapter);
		    // listener.hasBeenClicked(incidentsBigList.get(0));
		    assigneeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    // Log.d("ITEM clicked", "productlist " +
			    // incidentsBigList.toString());
			    Log.d("ITEM clicked", "position " + position);
			    Log.d("ITEM clicked", "id " + id);
			    // listener.hasBeenClicked(incidentsBigList.get(position));
			}
		    });

		} catch (JSONException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public void viewLogs(String responce) {
	if ("{\"response\":[\"success\"],\"message\":[\"Server returned success.\"]}".equals(responce))
	    return;
	JSONObject logs = null;

	JSONArray logsArray = new JSONArray();

	// try parse the string to a JSON object
	try {
	    logs = new JSONObject(responce);
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

	if (logs != null) {
	    try {

		View dialogView = inflater.inflate(R.layout.logs_list_view, null, false);

		LinearLayout logsList = (LinearLayout) dialogView.findViewById(R.id.logsLayout);

		// Create custom dialog object
		final Dialog dialog = new Dialog(getActivity()) {
		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		    }
		};
		// Include dialog.xml file
		dialog.setContentView(dialogView);
		// Set dialog title
		dialog.setTitle("Incident Logs");

		// Getting JSON Array from URL

		logsArray = logs.getJSONArray("data");
		if (logsArray != null && logsArray.length() > 0) {
		    for (int i = 0; i < logsArray.length(); i++) {

			RelativeLayout logItem = new IncidentLogs(getActivity()).getView(getActivity(), inflater);
			TextView name = (TextView) logItem.findViewById(R.id.logger_name_surname);
			TextView time = (TextView) logItem.findViewById(R.id.log_date);
			time.setTypeface(null, Typeface.BOLD);
			TextView title = (TextView) logItem.findViewById(R.id.log_title);

			try {
			    String description = ((JSONObject) logsArray.getJSONObject(i)).getString("description");
			    String nameSurname = ((JSONObject) logsArray.getJSONObject(i)).getJSONObject("user").getString("name") + " " + ((JSONObject) logsArray.getJSONObject(i)).getJSONObject("user").getString("surname");
			    String date = ((JSONObject) logsArray.getJSONObject(i)).getString("timestamp");

			    name.setText(nameSurname);
			    time.setText(date);
			    title.setText(description);

			} catch (JSONException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			logsList.addView(logItem);
			// JSONObject log = logsArray.getJSONObject(i);
			// HashMap<String, String> map = new HashMap<String,
			// String>();
			// map.put("description", log.getString("description"));
			// map.put("name",((JSONObject)
			// log.getJSONObject("user")).getString("name"));
			// map.put("surname", ((JSONObject)
			// log.getJSONObject("user")).getString("surname"));
			// map.put("timestamp", log.getString("timestamp"));
			// logslist.add(map);
		    }
		    final ScrollView scroll = (ScrollView) dialogView.findViewById(R.id.logsScroll);

		    scroll.postDelayed(new Runnable() {
			@Override
			public void run() {
			    scroll.fullScroll(ScrollView.FOCUS_DOWN);
			}
		    }, 10);
		} else {
		    RelativeLayout log = new IncidentLogs(getActivity()).getView(getActivity(), inflater);
		    TextView name = (TextView) log.findViewById(R.id.logger_name_surname);
		    TextView time = (TextView) log.findViewById(R.id.log_date);
		    TextView title = (TextView) log.findViewById(R.id.log_title);

		    String strComment = "There are no comments on this incident";
		    Calendar c = Calendar.getInstance();
		    System.out.println("Current time => " + c.getTime());

		    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		    String formattedDate = df.format(c.getTime());
		    String strName = "None";
		    strName += " ";

		    name.setText(strName);
		    time.setText(formattedDate);
		    title.setText(strComment);

		    logsList.addView(log);
		}
		dialog.show();

	    } catch (Exception e) {
		Log.d("TAG", "Exception " + e);
	    }

	    // AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
	    // dialog.setTitle("Incident Logs");
	    // dialog.setCancelable(true);

	    // View view =
	    // (mActivity).getLayoutInflater().inflate(R.layout.logs_list_view,
	    // null);

	    // ListView list = (ListView) view.findViewById(R.id.logs_list);
	    // viewLogsAdapeter logsAdapter= new viewLogsAdapeter(mActivity,
	    // logslist);

	    // list.setAdapter(logsAdapter);

	    // dialog.setView(view);
	    // dialog.show();

	    // AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
	    // builder.setTitle("Incident Logs");
	    // LayoutInflater li = (LayoutInflater)
	    // mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    // View v = li.inflate(R.layout.logs_list_view, null, false);
	    // viewLogsAdapeter logsAdapter = new viewLogsAdapeter(mActivity,
	    // logslist);
	    // v.setAdapter(logsAdapter);
	    //
	    // builder.setView(v);
	    //
	    // final Dialog dialog = builder.create();
	    // dialog.show();
	}
    }

    public void updateProgressIcon(int type, Bundle extras) {
	String from = extras.getString("from");
	for (int i = 0; i < assigneeListView.getChildCount(); i++) {
	    int count = assigneeListView.getAdapter().getCount();
	    if (count > 0) {
		try {
		    assigneeAdapeter assignee = (assigneeAdapeter) assigneeListView.getAdapter().getItem(i);

		    if (from.equals(assignee.employeenumber)) {
			switch (type) {
			case 0:
			    assignee.progress.setImageResource(R.drawable.not_delivered);
			    assignee.incident_progressID = 0;
			    break;
			case MainActivity.INCIDENT_SENT:
			    assignee.progress.setImageResource(R.drawable.not_delivered);
			    assignee.incident_progressID = 1;
			    break;
			case MainActivity.INCIDENT_RECEIVED_ON_SERVER:
			    assignee.progress.setImageResource(R.drawable.not_delivered);
			    assignee.incident_progressID = 2;
			    break;
			case MainActivity.INCIDENT_RECEIVED_ON_DEVICE:
			    assignee.progress.setImageResource(R.drawable.tick_indicator_16);
			    assignee.incident_progressID = 3;
			    break;
			case MainActivity.INCIDENT_READ_BY_USER:
			    assignee.progress.setImageResource(R.drawable.double_tick_indicator_received);
			    assignee.incident_progressID = 4;
			    break;
			case MainActivity.INCIDENT_ACCEPTED_BY_USER:
			    assignee.progress.setImageResource(R.drawable.incident_accepted);
			    assignee.incident_progressID = 5;
			    break;
			case MainActivity.INCIDENT_DECLINED_BY_USER:
			    assignee.progress.setImageResource(R.drawable.declined);
			    assignee.incident_progressID = 6;
			    break;
			case MainActivity.INCIDENT_JOB_CARD_RECEIVED:
			    assignee.progress.setImageResource(R.drawable.job_card_done);
			    assignee.incident_progressID = 7;
			    // assignee.row.setBackgroundColor(0x4fedfb);
			    break;

			default:
			    break;
			}
		    }
		} catch (Exception ex) {

		}
	    }
	}
    }

    public void showJobCard(String responce) {
	LayoutInflater inflater = LayoutInflater.from(getActivity());
	View view = inflater.inflate(R.layout.jobcardview, null);

	JSONObject responceJSON = null;

	// try parse the string to a JSON object
	try {
	    responceJSON = new JSONObject(responce);
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

	JSONArray data = null;
	try {
	    if (responceJSON != null)
		data = responceJSON.getJSONArray("data");
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	JSONObject jobCard = null;

	// try parse the string to a JSON object
	try {
	    if (data != null && data.length() > 0) {
		jobCard = data.getJSONObject(0);

		TextView existingMeterNumber = (TextView) view.findViewById(R.id.existing_meter_number);
		existingMeterNumber.setText(jobCard.optString("meterNum"));

		TextView existingMeterPosition = (TextView) view.findViewById(R.id.existing_meter_position);
		String meterPos = jobCard.optString("meterPosition");
		switch (meterPos) {
		case "0":
		    existingMeterPosition.setText("Above ground");
		    break;
		case "1":
		    existingMeterPosition.setText("Below ground");
		    break;
		default:
		    existingMeterPosition.setText("Above ground");
		    break;
		}

		TextView existingMeterType = (TextView) view.findViewById(R.id.existing_meter_type);
		String meterType = jobCard.optString("meterType");
		switch (meterType) {
		case "0":
		    existingMeterType.setText("No meter type selected");
		    break;
		case "1":
		    existingMeterType.setText("Prepaid meter type");
		    break;
		case "2":
		    existingMeterType.setText("Conventional - Combination");
		    break;
		case "3":
		    existingMeterType.setText("Prepaid - UMS");
		    break;
		case "4":
		    existingMeterType.setText("Prepaid - Kgosigadi");
		    break;
		default:
		    existingMeterType.setText("Conventional - Bulk");
		    break;
		}

		TextView existingMeterMake = (TextView) view.findViewById(R.id.existing_meter_make);
		switch (jobCard.optString("meterMake")) {
		case "1":
		    existingMeterMake.setText("Elseter Kent");
		    break;
		case "2":
		    existingMeterMake.setText("Teqnovo");
		    break;
		case "3":
		    existingMeterMake.setText("Sensus");
		    break;
		case "4":
		    existingMeterMake.setText("Prepaid Kent");
		    break;

		default:
		    existingMeterMake.setText("Make not specified");
		    break;
		}

		TextView existingMeterSize = (TextView) view.findViewById(R.id.existing_meter_size);
		existingMeterSize.setText(jobCard.optString("meterSize"));

		TextView existingMeterReading = (TextView) view.findViewById(R.id.existing_meter_reading);
		existingMeterReading.setText(jobCard.optString("meterReading"));

		TextView existingMeterNumberOfDigits = (TextView) view.findViewById(R.id.existing_meter_number_of_digits);
		existingMeterNumberOfDigits.setText(jobCard.optString("meterNumOfDigits"));

		// /////////////////////////////////////////////////////////////////////////////////////////
		// end existing

		TextView newMeterNumber = (TextView) view.findViewById(R.id.new_meter_number);
		newMeterNumber.setText(jobCard.optString("newMeterNum"));

		TextView newMeterPosition = (TextView) view.findViewById(R.id.new_meter_position);

		String newMeterPos = jobCard.optString("newMeterPosition");
		switch (newMeterPos) {
		case "0":
		    newMeterPosition.setText("Above ground");
		    break;
		case "1":
		    newMeterPosition.setText("Below ground");
		    break;
		default:
		    newMeterPosition.setText("Above ground");
		    break;
		}

		TextView newMeterType = (TextView) view.findViewById(R.id.new_meter_type);
		String newMeterTypeValue = jobCard.optString("newMeterType");
		switch (newMeterTypeValue) {
		case "0":
		    newMeterType.setText("No meter type selected");
		    break;
		case "1":
		    newMeterType.setText("Prepaid meter type");
		    break;
		case "2":
		    newMeterType.setText("Conventional - Combination");
		    break;
		case "3":
		    newMeterType.setText("Prepaid - UMS");
		    break;
		case "4":
		    newMeterType.setText("Prepaid - Kgosigadi");
		    break;
		default:
		    newMeterType.setText("Conventional - Bulk");
		    break;
		}

		TextView newMeterMake = (TextView) view.findViewById(R.id.new_meter_make);
		switch (jobCard.optString("newMeterMake")) {
		case "1":
		    newMeterMake.setText("Elseter Kent");
		    break;
		case "2":
		    newMeterMake.setText("Teqnovo");
		    break;
		case "3":
		    newMeterMake.setText("Sensus");
		    break;
		case "4":
		    newMeterMake.setText("Prepaid Kent");
		    break;

		default:
		    newMeterMake.setText("Make not specified");
		    break;
		}

		TextView newMeterSize = (TextView) view.findViewById(R.id.new_meter_size);
		newMeterSize.setText(jobCard.optString("newMeterSize"));

		TextView newMeterReading = (TextView) view.findViewById(R.id.new_meter_reading);
		newMeterReading.setText(jobCard.optString("newMeterReading"));

		TextView newMeterNumberOfDigits = (TextView) view.findViewById(R.id.new_meter_number_of_digits);
		newMeterNumberOfDigits.setText(jobCard.optString("newMeterNumOfDigits"));

		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		// end new meter

		TextView typeOfService = (TextView) view.findViewById(R.id.type_of_service);
		typeOfService.setText(jobCard.optString("connectionServiceType"));
		switch (jobCard.optString("connectionServiceType")) {
		case "0":
		    typeOfService.setText("Unknown");
		    break;
		case "1":
		    typeOfService.setText("Fire");
		    break;
		case "2":
		    typeOfService.setText("Domestic");
		    break;

		default:
		    break;
		}

		TextView lengthOfConn = (TextView) view.findViewById(R.id.length_of_connection);
		lengthOfConn.setText(jobCard.optString("connectionLegth"));

		TextView connRoadCrossing = (TextView) view.findViewById(R.id.connection_road_crossing);
		connRoadCrossing.setText(jobCard.optString("connectionCrossRoad"));

		TextView connDepth = (TextView) view.findViewById(R.id.connection_depth);
		connDepth.setText(jobCard.optString("connectionDepth"));

		TextView typeOfMaterial = (TextView) view.findViewById(R.id.type_of_material);
		typeOfMaterial.setText(jobCard.optString("connectionMaterialType"));

		switch (jobCard.optString("connectionMaterialType")) {
		case "0":
		    typeOfMaterial.setText("Unknown");
		    break;
		case "1":
		    typeOfMaterial.setText("Steel");
		    break;
		case "2":
		    typeOfMaterial.setText("HDPE Black");
		    break;
		case "3":
		    typeOfMaterial.setText("uPVC");
		    break;
		case "4":
		    typeOfMaterial.setText("AC");
		    break;

		default:
		    break;
		}

		// //////////////////////////////////////////////////////////////////////////////////////////////////////
		// end connection info

		TextView location = (TextView) view.findViewById(R.id.location);
		location.setText(jobCard.optString("pipelineLocation"));

		switch (jobCard.optString("pipelineLocation")) {
		case "0":
		    location.setText("Unknown");
		    break;
		case "1":
		    location.setText("Mid Block");
		    break;
		case "2":
		    location.setText("Side Walk");
		    break;

		default:
		    location.setText("Unknown");
		    break;
		}

		TextView roadCrossing = (TextView) view.findViewById(R.id.pipe_line_road_crossing);
		roadCrossing.setText(jobCard.optString("pipelineCrossRoad"));

		TextView diameter = (TextView) view.findViewById(R.id.pipe_diameter);
		diameter.setText(jobCard.optString("pipelineDiameter"));

		TextView material = (TextView) view.findViewById(R.id.pipe_material);
		material.setText(jobCard.optString("pipelineMaterial"));
		switch (jobCard.optString("pipelineMaterial")) {
		case "0":
		    material.setText("unknown");
		    break;
		case "1":
		    material.setText("Steel");
		    break;
		case "2":
		    material.setText("HDPE Black");
		    break;
		case "3":
		    material.setText("uPVC");
		    break;
		case "4":
		    material.setText("AC");
		    break;

		default:
		    break;
		}

		TextView typeOfrepair = (TextView) view.findViewById(R.id.type_of_repair);
		typeOfrepair.setText(jobCard.optString("pipelineRepairType"));
		switch (jobCard.optString("pipelineRepairType")) {
		case "0":
		    typeOfrepair.setText("Unknown");
		    break;
		case "1":
		    typeOfrepair.setText("Cut out length");
		    TextView cutOutLength = (TextView) view.findViewById(R.id.cut_out_length);
		    cutOutLength.setText(jobCard.optString("pipelineCutOutLength"));
		    break;
		case "2":
		    typeOfrepair.setText("Fix burst pipe");
		    break;

		default:
		    break;
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////
		// end pipe line

		TextView hydrantPos = (TextView) view.findViewById(R.id.hydrant_position);
		hydrantPos.setText(jobCard.optString("hydrantPosition"));

		switch (jobCard.optString("hydrantPosition")) {
		case "0":
		    hydrantPos.setText("Unknown");
		    break;
		case "1":
		    hydrantPos.setText("Hydrant above ground");
		    break;
		case "2":
		    hydrantPos.setText("Hydrant below ground");
		    break;

		default:
		    hydrantPos.setText("Unknown");
		    break;
		}

		TextView hydrantTypeOfRepair = (TextView) view.findViewById(R.id.hydrant_type_of_repair);
		hydrantTypeOfRepair.setText(jobCard.optString("hydrantRepairType"));
		switch (jobCard.optString("hydrantRepairType")) {
		case "0":
		    hydrantTypeOfRepair.setText("Unknown");
		    break;
		case "1":
		    hydrantTypeOfRepair.setText("Replace hydrant");
		    break;
		case "2":
		    hydrantTypeOfRepair.setText("Replace lid");
		    break;
		case "3":
		    hydrantTypeOfRepair.setText("Close Leaking hydrant");
		    break;
		case "4":
		    hydrantTypeOfRepair.setText("Repair hydrant");
		    break;
		case "5":
		    hydrantTypeOfRepair.setText("Replace Box");
		    break;
		case "6":
		    hydrantTypeOfRepair.setText("Surfacing hydrant");
		    break;

		default:
		    break;
		}

		TextView hydrantPressure = (TextView) view.findViewById(R.id.hydrant_pressure);
		hydrantPressure.setText(jobCard.optString("hydrantPressure"));

		long timeInMillis = Long.parseLong(jobCard.optString("hydrantPressureTime"));
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TextView hydrantTime = (TextView) view.findViewById(R.id.hydrant_time);
		hydrantTime.setText(dateFormat.format(cal1.getTime()));

		// //////////////////////////////////////////////////////////////////////////////////////////////////////end
		// hydrant
		JSONArray valves = jobCard.getJSONArray("valves");
		String valvesData = "No data for valves";
		if (valves != null) {
		    valvesData = "";
		    for (int i = 0; i < valves.length(); i++) {
			valvesData += "Valve :" + (i + 1) + "\n";
			long openTimeMillis = valves.getJSONObject(i).optLong("openTime");
			Calendar openTimecal = Calendar.getInstance();
			openTimecal.setTimeInMillis(openTimeMillis);
			SimpleDateFormat openTimedateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			valvesData += "Valve open time :	" + openTimedateFormat.format(openTimecal.getTime()) + "\n";

			long closeTimeMillis = valves.getJSONObject(i).optLong("openTime");
			Calendar closeTimecal = Calendar.getInstance();
			openTimecal.setTimeInMillis(closeTimeMillis);
			SimpleDateFormat closeTimedateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			valvesData += "Valve close time :	" + closeTimedateFormat.format(closeTimecal.getTime()) + "\n";

			valvesData += "Valve side :	" + (valves.getJSONObject(i).optBoolean("leftSide") == true ? "Left" : "Right") + "\n";
			valvesData += "Street name :	" + valves.getJSONObject(i).optString("streetName") + "\n";
			int repair = valves.getJSONObject(i).optInt("valveRepairType");
			switch (repair) {
			case 0:
			    valvesData += "Valve repair type : 	Unknown.\n";
			    break;
			case 1:
			    valvesData += "Valve repair type : 	Repair gland.\n";
			    break;
			case 2:
			    valvesData += "Valve repair type : 	Tighten gland.\n";
			    break;
			case 3:
			    valvesData += "Valve repair type : 	Replace lid.\n";
			    break;
			case 4:
			    valvesData += "Valve repair type : 	Replace valve.\n";
			    break;
			default:
			    valvesData += "Valve repair type : 	Unknown.\n";
			    break;
			}
		    }
		}
		TextView valve = (TextView) view.findViewById(R.id.valve);
		valve.setText(valvesData);

		// /////////////////////////////////////////////////////////////////////////////////////////////////////end
		// valve

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Job Card");
		// alertDialog.setMessage("Here is a really long message.");
		alertDialog.setView(view);
		alertDialog.setNegativeButton("OK", null);
		AlertDialog alert = alertDialog.create();
		alert.show();
	    } else {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Job Card");
		// alertDialog.setMessage("Here is a really long message.");
		alertDialog.setMessage("Job Card is empty");
		alertDialog.setNegativeButton("OK", null);
		AlertDialog alert = alertDialog.create();
		alert.show();
	    }
	} catch (JSONException e) {
	    Log.e("JSON Parser", "Error parsing data " + e.toString());
	}

    }
}
