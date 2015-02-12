package com.nattySoft.mogalejobcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Valve extends Fragment implements OnItemSelectedListener {

	private EditText valveOpenTime;
	private EditText valveNumTurns;

	private Spinner valveSideSpinner;
	private boolean leftSide;
	private boolean RightSide;

	private EditText valveClosedTime;

	private EditText valveNo;
	private EditText oppStd;
	private EditText hseNo;

	private Spinner ValveRepairCodeSpinner;

	private EditText streetName;
	private EditText distanceFromBoundary;
	private EditText distanceLeft;
	private EditText distanceRight;

	private boolean valveRepackGland = false;
	private boolean valveTightenGland = false;
	private boolean valveReplaceLid = false;
	private boolean valveReplaceRSVValve = false;
	private boolean valveReplaceIronValve = false;
	private String repairCode;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.valve, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		valveOpenTime = (EditText) view.findViewById(R.id.valve_open_time);
		valveNumTurns = (EditText) view.findViewById(R.id.valve_num_turns);

		valveSideSpinner = (Spinner) view.findViewById(R.id.Valve_side_spinner);

		valveClosedTime = (EditText) view.findViewById(R.id.valve_closed_time);

		valveNo = (EditText) view.findViewById(R.id.valve_no);
		oppStd = (EditText) view.findViewById(R.id.opp_std);
		hseNo = (EditText) view.findViewById(R.id.hse_no);

		ValveRepairCodeSpinner = (Spinner) view.findViewById(R.id.Valve_repair_code_spinner);
		ValveRepairCodeSpinner.setOnItemSelectedListener(this);

		streetName = (EditText) view.findViewById(R.id.street_name);
		distanceFromBoundary = (EditText) view.findViewById(R.id.distance_from_boundary);
		distanceLeft = (EditText) view.findViewById(R.id.distance_left);
		distanceRight = (EditText) view.findViewById(R.id.distance_right);
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);

				valveOpenTime.setText(json.getString("valveOpenTime"));
				valveNumTurns.setText(json.getString("valveNumTurns"));

				valveSideSpinner.setSelection(Integer.parseInt(json.getString("valveSideSpinner")));

				valveClosedTime.setText(json.getString("valveClosedTime"));

				valveNo.setText(json.getString("valveNo"));
				oppStd.setText(json.getString("oppStd"));
				hseNo.setText(json.getString("hseNo"));

				ValveRepairCodeSpinner.setSelection(Integer.parseInt(json.getString("ValveRepairCodeSpinner")));

				streetName.setText(json.getString("streetName"));
				distanceFromBoundary.setText(json.getString("distanceFromBoundary"));
				distanceLeft.setText(json.getString("distanceLeft"));
				distanceRight.setText(json.getString("distanceRight"));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	public void saveForm() {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("valveOpenTime", valveOpenTime.getText().toString());
			json.accumulate("valveNumTurns", valveNumTurns.getText().toString());

			json.accumulate("valveSideSpinner", valveSideSpinner.getSelectedItemPosition());

			if (valveSideSpinner.getItemAtPosition(valveSideSpinner.getSelectedItemPosition()).equals("R/H")) {
				RightSide = true;
				leftSide = false;
			}
			else if (valveSideSpinner.getItemAtPosition(valveSideSpinner.getSelectedItemPosition()).equals("L/H")) {
				RightSide = false;
				leftSide = true;
			}
			else
			{
				RightSide = false;
				leftSide = false;
			}
			
			json.accumulate("RightSide", RightSide);
			json.accumulate("leftSide", leftSide);
			
			json.accumulate("valveClosedTime", valveClosedTime.getText().toString());

			json.accumulate("valveNo", valveNo.getText().toString());
			json.accumulate("oppStd", oppStd.getText().toString());
			json.accumulate("hseNo", hseNo.getText().toString());

			json.accumulate("ValveRepairCodeSpinner", ValveRepairCodeSpinner.getSelectedItemPosition());

			json.accumulate("streetName", streetName.getText().toString());
			json.accumulate("distanceFromBoundary", distanceFromBoundary.getText().toString());
			json.accumulate("distanceLeft", distanceLeft.getText().toString());
			json.accumulate("distanceRight", distanceRight.getText().toString());

			json.accumulate("valveRepackGland", valveRepackGland);
			json.accumulate("valveTightenGland", valveTightenGland);
			json.accumulate("valveReplaceLid", valveReplaceLid);
			json.accumulate("valveReplaceRSVValve", valveReplaceRSVValve);
			json.accumulate("valveReplaceIronValve", valveReplaceIronValve);
			
			json.accumulate("repairCode", repairCode);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID, json.toString());
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

		if (parent.getItemAtPosition(pos).toString().equals("100:Repack gland ")) {
			valveRepackGland = true;
			valveTightenGland = false;
			valveReplaceLid = false;
			valveReplaceRSVValve = false;
			valveReplaceIronValve = false;
			repairCode = "100:Repack gland ";
		} else if (parent.getItemAtPosition(pos).toString().equals("101:Tighten gland")) {
			valveRepackGland = false;
			valveTightenGland = true;
			valveReplaceLid = false;
			valveReplaceRSVValve = false;
			valveReplaceIronValve = false;
			repairCode = "101:Tighten gland";
		} else if (parent.getItemAtPosition(pos).toString().equals("103:Replace lid")) {
			valveRepackGland = false;
			valveTightenGland = false;
			valveReplaceLid = true;
			valveReplaceRSVValve = false;
			valveReplaceIronValve = false;
			repairCode = "103:Replace lid";
		} else if (parent.getItemAtPosition(pos).toString().equals("115:Replace RSV valve")) {
			valveRepackGland = false;
			valveTightenGland = false;
			valveReplaceLid = false;
			valveReplaceRSVValve = true;
			valveReplaceIronValve = false;
			repairCode = "115:Replace RSV valve";
		} else if (parent.getItemAtPosition(pos).toString().equals("114:Replace cast iron valve")) {
			valveRepackGland = false;
			valveTightenGland = false;
			valveReplaceLid = false;
			valveReplaceRSVValve = false;
			valveReplaceIronValve = true;
			repairCode = "114:Replace cast iron valve";
		} else {
			valveRepackGland = false;
			valveTightenGland = false;
			valveReplaceLid = false;
			valveReplaceRSVValve = false;
			valveReplaceIronValve = false;
			repairCode = "";
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
