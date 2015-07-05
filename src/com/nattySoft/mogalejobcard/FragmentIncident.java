package com.nattySoft.mogalejobcard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
	    reporterPhone.setText(item.get("reporterContact"));

	    String addrString = null;
	    if (item.get("township").length() > 0)
		addrString = "" + item.get("township");
	    if (item.get("stand").length() > 0)
		addrString += ", " + item.get("stand");
	    if (item.get("portion").length() > 0)
		addrString += ", " + item.get("portion");
	    if (item.get("street").length() > 0)
		addrString += ", " + item.get("street");
	    if (item.get("building").length() > 0)
		addrString += ", " + item.get("building");

	    addressText.setText(addrString);

	    int assigneeSize = Integer.parseInt(item.get("assigneeSize"));
	    assigneeAList = new ArrayList<HashMap<String, String>>();

	    for (int i = 0; i < assigneeSize; i++) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("assigneeId", item.get("assigneeId_" + i));
		map.put("assigneeSuperiorId", item.get("assigneeSuperiorId_" + i));
		map.put("assigneeEmployeeNum", item.get("assigneeEmployeeNum_" + i));
		map.put("assigneeEmail", item.get("assigneeEmail_" + i));
		map.put("assigneeCellphone", item.get("assigneeCellphone_" + i));
		map.put("assigneeName", item.get("assigneeName_" + i));
		map.put("designation", item.get("designation_" + i));
		map.put("assigneeSurname", item.get("assigneeSurname_" + i));
		map.put("active", item.get("active_" + i));
		map.put("password", item.get("password_" + i));

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
		    map.put("accepteeEmail", item.get("accepteeEmail_" + i));
		    map.put("accepteeCellphone", item.get("accepteeCellphone_" + i));
		    map.put("accepteeName", item.get("accepteeName_" + i));
		    map.put("designation", item.get("designation_" + i));
		    map.put("accepteeSurname", item.get("accepteeSurname_" + i));
		    map.put("active", item.get("active_" + i));
		    map.put("password", item.get("password_" + i));

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
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
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
		CommunicationHandler.reassignincident(FragmentIncident.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Getting Users..."), MainActivity.employeeNUM, incidentID, AssignedAssignees);
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
	private ArrayList<HashMap<String, String>> accepteelist;
	private ArrayList<HashMap<String, String>> assigneelist;
	private View row;
	// private ImageView myImage;
	private ImageView profileImage;
	private TextView name;
	private ImageView progress;
	// private TextView ID;
	private ImageView remove;

	public assigneeAdapeter(Context context, ArrayList<HashMap<String, String>> assigneeList, ArrayList<HashMap<String, String>> accepteeList) {
	    super(context, R.layout.assignee_item);
	    this.context = context;
	    this.assigneelist = assigneeList;
	    this.accepteelist = accepteeList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder mViewHolder = null;
	    HashMap<String, String> song = null;

	    if (convertView == null) {

		song = new HashMap<String, String>();
		mViewHolder = new ViewHolder();

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = inflater.inflate(R.layout.assignee_item, parent, false);
		// myImage = (ImageView) row.findViewById(R.id.type_imageView);
		profileImage = (ImageView) row.findViewById(R.id.profile_imageView);
		name = (TextView) row.findViewById(R.id.name_text);
		progress = (ImageView) row.findViewById(R.id.assignee_progress);
		// ID = (TextView) row.findViewById(R.id.id_text);
		remove = (ImageButton) row.findViewById(R.id.remove_button);

	    } else {

		mViewHolder = (ViewHolder) convertView.getTag();

	    }

	    if (accepteelist != null && accepteelist.size() > 0) {
		if (MainActivity.employeeNUM.equals(accepteelist.get(position).get("accepteeEmployeeNum"))) {
		    name.setText("Me");
		    progress.setImageResource(R.drawable.double_tick_indicator_received);
		} else {
		    name.setText(accepteelist.get(position).get("accepteeName") + " " + accepteelist.get(position).get("accepteeSurname"));
		    progress.setImageResource(R.drawable.tick_indicator_16);
		}
		
	    } else if (assigneelist != null && assigneelist.size() > 0) {
		if (MainActivity.employeeNUM.equals(assigneelist.get(position).get("assigneeEmployeeNum"))) {
		    name.setText("Me");
		    progress.setImageResource(R.drawable.double_tick_indicator_received);
		} else {
		    name.setText(assigneelist.get(position).get("assigneeName") + " " + assigneelist.get(position).get("assigneeSurname"));
		    progress.setImageResource(R.drawable.tick_indicator_16);
		}
		
	    }

	    switch (MainActivity.employeeDesignation) {
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
	    int count = super.getCount();
	    if (accepteelist != null) {
		int listSize = accepteelist.size();

		int productlistC = accepteelist.size();
		Log.d("inside getCount", "productlist.size() " + listSize);
		Log.d("inside getCount", "superC " + count);
		Log.d("inside getCount", "productlistC " + productlistC);
		if (count < 1) {
		    return productlistC;
		}
	    } else if (assigneelist != null) {
		int listSize = assigneelist.size();

		int productlistC = assigneelist.size();
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
}
