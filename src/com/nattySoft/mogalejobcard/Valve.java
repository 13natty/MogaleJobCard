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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Valve extends Fragment implements OnCheckedChangeListener {

	private EditText valveOpenTime_hh;
	private EditText valveOpenTime_mm;

	private CheckBox Left_H;
	private CheckBox right_H;
	private boolean leftSide = false;

	private EditText valveClosedTime_hh;
	private EditText valveClosedTime_mm;
	
	private EditText streetName;
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

		valveOpenTime_hh = (EditText) view.findViewById(R.id.opened_time_hh);
		valveOpenTime_mm = (EditText) view.findViewById(R.id.opened_time_mm);

		valveClosedTime_hh = (EditText) view.findViewById(R.id.closed_time_hh);
		valveClosedTime_mm = (EditText) view.findViewById(R.id.closed_time_mm);

		Left_H = (CheckBox) view.findViewById(R.id.l_h);
		Left_H.setOnCheckedChangeListener(this);
		right_H = (CheckBox) view.findViewById(R.id.r_h);
		Left_H.setChecked(true);
		right_H.setOnCheckedChangeListener(this);
		
		streetName = (EditText) view.findViewById(R.id.street);		
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);

				valveOpenTime_hh.setText(json.getString("valveOpenTime_hh"));
				valveOpenTime_mm.setText(json.getString("valveOpenTime_mm"));
				
				valveClosedTime_hh.setText(json.getString("valveClosedTime_hh"));
				valveClosedTime_mm.setText(json.getString("valveClosedTime_mm"));
				
				leftSide = Boolean.parseBoolean(json.getString("leftSide"));
				if (!leftSide) {
					right_H.setChecked(true);
				} else {
					Left_H.setChecked(true);
				}
				
				streetName.setText(json.getString("streetName"));

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
			json.accumulate("valveOpenTime_hh", valveOpenTime_hh.getText().toString());
			json.accumulate("valveOpenTime_mm", valveOpenTime_mm.getText().toString());
			
			json.accumulate("valveClosedTime_hh", valveClosedTime_hh.getText().toString());
			json.accumulate("valveClosedTime_mm", valveClosedTime_mm.getText().toString());

			json.accumulate("leftSide", leftSide);
			
			json.accumulate("streetName", streetName.getText().toString());	
			json.accumulate("repairCode", repairCode);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID, json.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) Left_H && isChecked) {
			right_H.setOnCheckedChangeListener(null);
			right_H.setChecked(false);
			right_H.setOnCheckedChangeListener(this);
			leftSide = true;
		} else if (buttonView == (CompoundButton) right_H && isChecked) {
			Left_H.setOnCheckedChangeListener(null);
			Left_H.setChecked(false);
			Left_H.setOnCheckedChangeListener(this);
			leftSide = false;
		} else if (buttonView == (CompoundButton) Left_H || buttonView == (CompoundButton) right_H) {
			Left_H.setOnCheckedChangeListener(null);
			Left_H.setChecked(true);
			Left_H.setOnCheckedChangeListener(this);
			leftSide = true;
		}
		
	}
}
