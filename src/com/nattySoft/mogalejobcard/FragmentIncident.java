package com.nattySoft.mogalejobcard;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.nattySoft.mogalejobcard.AppConstants;
import com.nattySoft.mogalejobcard.MainActivity;
import com.nattySoft.mogalejobcard.R;
import com.nattySoft.mogalejobcard.R.id;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentIncident extends Fragment {

	ImageView image;
	TextView statusText;
	TextView typeText;
	TextView title;
	TextView reporterName;
	TextView reporterSurName;
	TextView accountNumber;
	TextView township;
	TextView stand;
	TextView portion;
	TextView streename;
	TextView building;
	TextView reporterIdNumber;
	TextView reporterContact;

	LinearLayout assigneeLayout;
	LinearLayout accepteeLayout;

	Button acceptButton;
	Button commentButton;
	Button viewCommentsButton;
	Button declineButton;
	private ArrayList<HashMap<String, String>> assigneeAList;
	private ArrayList<HashMap<String, String>> accepteeAList;
	public String incidentID = null;
	private LayoutInflater inflater;
	private TableRow.LayoutParams params;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";

	public FragmentIncident() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_layout_incident, container, false);

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
		image = (ImageView) getActivity().findViewById(R.id.imageView_type);
		title = (TextView) getActivity().findViewById(R.id.title);
		reporterName = (TextView) getActivity().findViewById(R.id.reporterName);
		reporterSurName = (TextView) getActivity().findViewById(R.id.reporterSurname);
		accountNumber = (TextView) getActivity().findViewById(R.id.accountNumber);
		township = (TextView) getActivity().findViewById(R.id.township);
		stand = (TextView) getActivity().findViewById(R.id.stand);
		portion = (TextView) getActivity().findViewById(R.id.portion);
		streename = (TextView) getActivity().findViewById(R.id.street);
		building = (TextView) getActivity().findViewById(R.id.building);
		reporterIdNumber = (TextView) getActivity().findViewById(R.id.reporterIdNumber);
		reporterContact = (TextView) getActivity().findViewById(R.id.reporterContact);

		int left = 6;
		int top = 12;
		int right = 6;
		int bottom = 6;

		params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
		assigneeLayout = (LinearLayout) getActivity().findViewById(id.assigneeView);
		accepteeLayout = (LinearLayout) getActivity().findViewById(id.accepteesView);

		acceptButton = (Button) getActivity().findViewById(R.id.button_accept);
		 acceptButton.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 MainActivity.action = Action.ACCEPT_INCIDENT;
		 CommunicationHandler.acceptIncident(getActivity(),
		 (RequestResponseListener) getActivity(),
		 ProgressDialog.show(getActivity(), "Please wait",
		 "Accepting Incidents..."),
		 Preferences.getPreference(getActivity(),
		 AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID);
		 }
		 });

		commentButton = (Button) getActivity().findViewById(R.id.comment_button);
		// commentButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // custom dialog
		// final Dialog dialog = new Dialog(getActivity());
		// dialog.setContentView(R.layout.add_comment);
		// dialog.setTitle("Comment on " + title.getText());
		//
		// Button sendButton = (Button) dialog.findViewById(R.id.comment_send);
		// // if button is clicked, close the custom dialog
		// sendButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// EditText message = (EditText)
		// dialog.findViewById(R.id.comment_message);
		// MainActivity.action = Action.ADD_COMMENT;
		// CommunicationHandler.addComment(getActivity(),
		// (RequestResponseListener) getActivity(),
		// ProgressDialog.show(getActivity(), "Please wait",
		// "Sending your comment..."),
		// Preferences.getPreference(IncidentDetailsFragment.this.getActivity(),
		// AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), incidentID,
		// message.getText().toString());
		// dialog.dismiss();
		// }
		// });
		//
		// Button cancelButton = (Button)
		// dialog.findViewById(R.id.comment_cancel);
		// cancelButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
		// dialog.show();
		// }
		// });

		viewCommentsButton = (Button) getActivity().findViewById(R.id.view_comments);
		// viewCommentsButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// MainActivity.action = Action.GET_COMMENTS;
		// CommunicationHandler.getComments(getActivity(),
		// (RequestResponseListener) getActivity(),
		// ProgressDialog.show(getActivity(), "Please wait",
		// "Sending your comment..."), incidentID);
		// }
		// });

		declineButton = (Button) getActivity().findViewById(R.id.button_decline);
		 declineButton.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
			 DialogClass cdd=new DialogClass(getActivity(), incidentID);
			 cdd.show();  
		 }
		 });
		Bundle bundle = this.getArguments();
		HashMap<String, String> item = (HashMap<String, String>) bundle.getSerializable("HashMap");
		updateFields(item);
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

			if (item.get("accepted") != null) {
				boolean accepted = item.get("accepted").equals("true") ? true : false;
				if (accepted) {
					acceptButton.setVisibility(View.GONE);
					declineButton.setVisibility(View.GONE);
					commentButton.setVisibility(View.VISIBLE);
					viewCommentsButton.setVisibility(View.VISIBLE);
				} else {
					acceptButton.setVisibility(View.VISIBLE);
					declineButton.setVisibility(View.VISIBLE);
					commentButton.setVisibility(View.GONE);
					viewCommentsButton.setVisibility(View.GONE);
				}
			}
			statusText.setText("Status : " + item.get("status"));
			typeText.setText("Type : " + item.get("type"));
			title.setText(item.get("description"));
			reporterName.setText(item.get("reporterName"));
			reporterSurName.setText(item.get("reporterSurname"));
			accountNumber.setText(item.get("accountNumber"));
			township.setText(item.get("township"));
			stand.setText(item.get("stand"));
			portion.setText(item.get("portion"));
			streename.setText(item.get("street"));
			building.setText(item.get("building"));
			reporterIdNumber.setText(item.get("reporterIdNumber"));
			reporterContact.setText(item.get("reporterContact"));

			int assigneeSize = Integer.parseInt(item.get("assigneeSize"));
			assigneeAList = new ArrayList<HashMap<String, String>>();

			assigneeLayout.removeAllViews();

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
				TextView nameTV = new TextView(this.getActivity());
				nameTV.setImeActionLabel(map.get("assigneeEmployeeNum"), -1);
				nameTV.setText("Assignee Name : " + map.get("assigneeName"));
				nameTV.setLayoutParams(params);
				// nameTV.setOnLongClickListener(this);
				// nameTV.setBackground(getResources().getDrawable(R.drawable.assignee_border));
				assigneeLayout.addView(nameTV);
			}

			int accepteeSize = Integer.parseInt(item.get("accepteeSize"));
			if (accepteeSize > 0) {
				accepteeAList = new ArrayList<HashMap<String, String>>();

				accepteeLayout.removeAllViews();

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
					accepteeLayout.addView(nameTV);
				}
			}
			else
			{
				TextView acceptees = (TextView) getActivity().findViewById(id.acceptees_label);
				acceptees.setText("");
				acceptees.setBackgroundColor(0xffffff);
			}

		}
	}
}
