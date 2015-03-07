package com.nattySoft.mogalejobcard;

import java.util.Calendar;

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
import android.widget.TimePicker;

public class Valve extends Fragment implements OnCheckedChangeListener {

	private TimePicker valveOpenTime;

	private CheckBox Left_H;
	private CheckBox right_H;
	private boolean leftSide = false;

	private TimePicker valveClosedTime;
	
	private EditText streetName;
	
	private CheckBox repairGland;
	private CheckBox tightenGland;
	private CheckBox replaceLid;
	private CheckBox replaceValve;
	private int valveRepairType;

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

		valveOpenTime = (TimePicker) view.findViewById(R.id.opened_time);
		valveOpenTime.setIs24HourView(true);

		valveClosedTime = (TimePicker) view.findViewById(R.id.closed_time);
		valveClosedTime.setIs24HourView(true);

		Left_H = (CheckBox) view.findViewById(R.id.l_h);
		Left_H.setOnCheckedChangeListener(this);
		right_H = (CheckBox) view.findViewById(R.id.r_h);
		Left_H.setChecked(true);
		right_H.setOnCheckedChangeListener(this);
		
		streetName = (EditText) view.findViewById(R.id.street);
		
		repairGland = (CheckBox) view.findViewById(R.id.repair_repair_gland);
		repairGland.setOnCheckedChangeListener(this);
		tightenGland = (CheckBox) view.findViewById(R.id.tighten_gland);
		tightenGland.setOnCheckedChangeListener(this);
		replaceLid = (CheckBox) view.findViewById(R.id.replace_valve_lid);
		replaceLid.setOnCheckedChangeListener(this);
		replaceValve = (CheckBox) view.findViewById(R.id.replace_valve);
		replaceValve.setOnCheckedChangeListener(this);
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.parseLong(json.getString("openTime")));
				valveOpenTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
				valveOpenTime.setCurrentMinute(calendar.get(Calendar.MINUTE));
				
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
				valveClosedTime.setCurrentHour(calendar2.get(Calendar.HOUR_OF_DAY));
				valveClosedTime.setCurrentMinute(calendar2.get(Calendar.MINUTE));
				
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
			valveOpenTime.clearFocus();
			Calendar calendar = Calendar.getInstance();
//			calendar.set(0, 0, 0, valveOpenTime.getCurrentHour(), valveOpenTime.getCurrentMinute());
			calendar.set(Calendar.HOUR, valveOpenTime.getCurrentHour());
			calendar.set(Calendar.MINUTE, valveOpenTime.getCurrentMinute());
			long time = calendar.getTimeInMillis();
			json.accumulate("openTime", time);
			
			valveClosedTime.clearFocus();
			Calendar calendar2 = Calendar.getInstance();
//			calendar2.set(0, 0, 0, valveClosedTime.getCurrentHour(), valveClosedTime.getCurrentMinute());
			calendar.set(Calendar.HOUR, valveClosedTime.getCurrentHour());
			calendar.set(Calendar.MINUTE, valveClosedTime.getCurrentMinute());
			long time2 = calendar2.getTimeInMillis();
			json.accumulate("closeTime", time2);

			json.accumulate("leftSide", leftSide);
			
			json.accumulate("streetName", streetName.getText().toString());	
			json.accumulate("valveRepairType", valveRepairType);

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
		}else if (buttonView == (CompoundButton) repairGland || buttonView == (CompoundButton) replaceLid || buttonView == (CompoundButton) tightenGland || buttonView == (CompoundButton) replaceValve) {
			repairGland.setChecked(false);
			replaceLid.setChecked(false);
			tightenGland.setChecked(false);
			replaceValve.setChecked(false);
			valveRepairType = 0;

			if (buttonView == (CompoundButton) repairGland && isChecked) {
				repairGland.setOnCheckedChangeListener(null);
				repairGland.setChecked(true);
				repairGland.setOnCheckedChangeListener(this);
				valveRepairType = 1;
			} else if (buttonView == (CompoundButton) replaceLid && isChecked) {
				replaceLid.setOnCheckedChangeListener(null);
				replaceLid.setChecked(true);
				replaceLid.setOnCheckedChangeListener(this);
				valveRepairType = 2;
			} else if (buttonView == (CompoundButton) tightenGland && isChecked) {
				tightenGland.setOnCheckedChangeListener(null);
				tightenGland.setChecked(true);
				tightenGland.setOnCheckedChangeListener(this);
				valveRepairType = 3;
			} else if (buttonView == (CompoundButton) replaceValve && isChecked) {
				replaceValve.setOnCheckedChangeListener(null);
				replaceValve.setChecked(true);
				replaceValve.setOnCheckedChangeListener(this);
				valveRepairType = 4;
			} 
		}
		
	}
}
