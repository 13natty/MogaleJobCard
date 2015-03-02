package com.nattySoft.mogalejobcard;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;

public class Hydrant extends Fragment implements OnItemSelectedListener, OnCheckedChangeListener {

	private CheckBox hydrantAbove;
	private CheckBox hydrantBelow;
	private int hydrantPosition;

	// private EditText hydrantNumber;
	private EditText hydrantPressure;
	private TimePicker hydrantTime;
	// private EditText hydrantLeftCorner;
	// private EditText hydrantRightCorner;

	private CheckBox replaceHydrant;
	private CheckBox replaceLid;
	private CheckBox closeLeakingHydrant;
	private CheckBox repairHydrant;
	private CheckBox replaceBox;
	private CheckBox surfacingHydrant;

	private int hydrantRepairType = 0;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.hydrant, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		hydrantAbove = (CheckBox) view.findViewById(R.id.hydrant_above_ground);
		hydrantAbove.setOnCheckedChangeListener(this);
		hydrantBelow = (CheckBox) view.findViewById(R.id.hydrant_below_ground);
		hydrantBelow.setOnCheckedChangeListener(this);

		replaceHydrant = (CheckBox) view.findViewById(R.id.repair_repace_hydrant);
		replaceHydrant.setOnCheckedChangeListener(this);
		replaceLid = (CheckBox) view.findViewById(R.id.repair_Replace_lid);
		replaceLid.setOnCheckedChangeListener(this);
		closeLeakingHydrant = (CheckBox) view.findViewById(R.id.close_leaking_hydrant);
		closeLeakingHydrant.setOnCheckedChangeListener(this);
		repairHydrant = (CheckBox) view.findViewById(R.id.repair_hydrant);
		repairHydrant.setOnCheckedChangeListener(this);
		replaceBox = (CheckBox) view.findViewById(R.id.repair_repace_box);
		replaceBox.setOnCheckedChangeListener(this);
		surfacingHydrant = (CheckBox) view.findViewById(R.id.repair_surfacing_hydrant);
		surfacingHydrant.setOnCheckedChangeListener(this);

		hydrantPressure = (EditText) view.findViewById(R.id.hydrant_pressure);
		hydrantTime = (TimePicker) view.findViewById(R.id.hydrant_timePicker);
		hydrantTime.setIs24HourView(true);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if (parent.getItemAtPosition(pos).toString().equals("Code")) {
			// code.setVisibility(View.VISIBLE);
			// hydrantReplace = false;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = false;
		} else {
			// if(parent.getItemAtPosition(pos).toString().equals("203:Replace hydrant"))
			// {
			// hydrantReplace = true;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = false;
			// }
			// else
			// if(parent.getItemAtPosition(pos).toString().equals("200:Replace Lid"))
			// {
			// hydrantReplace = false;
			// hydrantReplaceLid = true;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = false;
			// }
			// else
			// if(parent.getItemAtPosition(pos).toString().equals("201:Close leaking hydrant"))
			// {
			// hydrantReplace = false;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = true;
			// hydrantReplaceBox = false;
			// }
			// else
			// if(parent.getItemAtPosition(pos).toString().equals("202:Repair hydrant"))
			// {
			// hydrantReplace = false;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = false;
			// }
			// else
			// if(parent.getItemAtPosition(pos).toString().equals("203:Replace box"))
			// {
			// hydrantReplace = false;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = true;
			// }
			// else
			// {
			// hydrantReplace = false;
			// hydrantReplaceLid = false;
			// hydrantCloseLeak = false;
			// hydrantReplaceBox = false;
			// }
			// code.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_HYDRANT + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);

				hydrantPosition = Integer.parseInt(json.getString("hydrantPosition"));
				if (hydrantPosition == 1) {
					hydrantAbove.setChecked(true);
				} else if (hydrantPosition == 2) {
					hydrantBelow.setChecked(true);
				}

				hydrantPressure.setText(json.getString("hydrantPressure"));

				hydrantRepairType = Integer.parseInt(json.getString("hydrantRepairType"));
				if (hydrantRepairType == 1) {
					replaceHydrant.setChecked(true);
				} else if (hydrantRepairType == 2) {
					replaceLid.setChecked(true);
				} else if (hydrantRepairType == 3) {
					closeLeakingHydrant.setChecked(true);
				} else if (hydrantRepairType == 4) {
					repairHydrant.setChecked(true);
				} else if (hydrantRepairType == 5) {
					replaceBox.setChecked(true);
				} else if (hydrantRepairType == 6) {
					surfacingHydrant.setChecked(true);
				}
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.parseLong(json.getString("hydrantPressureTime")));
				hydrantTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
				hydrantTime.setCurrentMinute(calendar.get(Calendar.MINUTE));
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

			json.accumulate("hydrantPosition", hydrantPosition);
			json.accumulate("hydrantPressure", hydrantPressure.getText().toString());
			hydrantTime.clearFocus();
			Calendar calendar = Calendar.getInstance();
			calendar.set(0, 0, 0, hydrantTime.getCurrentHour(), hydrantTime.getCurrentMinute());
			long time = calendar.getTimeInMillis();
			json.accumulate("hydrantPressureTime", time);

			json.accumulate("hydrantRepairType", hydrantRepairType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_HYDRANT + FragmentIncident.incidentID, json.toString());

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) hydrantAbove && isChecked) {
			hydrantBelow.setOnCheckedChangeListener(null);
			hydrantBelow.setChecked(false);
			hydrantBelow.setOnCheckedChangeListener(this);
			hydrantPosition = 1;
		} else if (buttonView == (CompoundButton) hydrantBelow && isChecked) {
			hydrantAbove.setOnCheckedChangeListener(null);
			hydrantAbove.setChecked(false);
			hydrantAbove.setOnCheckedChangeListener(this);
			hydrantPosition = 2;
		} else if ((buttonView == (CompoundButton) hydrantAbove || buttonView == (CompoundButton) hydrantBelow) && !isChecked) {
			hydrantPosition = 0;
		} else if (buttonView == (CompoundButton) replaceHydrant || buttonView == (CompoundButton) replaceLid || buttonView == (CompoundButton) closeLeakingHydrant || buttonView == (CompoundButton) repairHydrant || buttonView == (CompoundButton) replaceBox || buttonView == (CompoundButton) surfacingHydrant) {
			replaceHydrant.setChecked(false);
			replaceLid.setChecked(false);
			closeLeakingHydrant.setChecked(false);
			repairHydrant.setChecked(false);
			replaceBox.setChecked(false);
			surfacingHydrant.setChecked(false);
			hydrantRepairType = 0;

			if (buttonView == (CompoundButton) replaceHydrant && isChecked) {
				replaceHydrant.setOnCheckedChangeListener(null);
				replaceHydrant.setChecked(true);
				replaceHydrant.setOnCheckedChangeListener(this);
				hydrantRepairType = 1;
			} else if (buttonView == (CompoundButton) replaceLid && isChecked) {
				replaceLid.setOnCheckedChangeListener(null);
				replaceLid.setChecked(true);
				replaceLid.setOnCheckedChangeListener(this);
				hydrantRepairType = 2;
			} else if (buttonView == (CompoundButton) closeLeakingHydrant && isChecked) {
				closeLeakingHydrant.setOnCheckedChangeListener(null);
				closeLeakingHydrant.setChecked(true);
				closeLeakingHydrant.setOnCheckedChangeListener(this);
				hydrantRepairType = 3;
			} else if (buttonView == (CompoundButton) repairHydrant && isChecked) {
				repairHydrant.setOnCheckedChangeListener(null);
				repairHydrant.setChecked(true);
				repairHydrant.setOnCheckedChangeListener(this);
				hydrantRepairType = 4;
			} else if (buttonView == (CompoundButton) replaceBox && isChecked) {
				replaceBox.setOnCheckedChangeListener(null);
				replaceBox.setChecked(true);
				replaceBox.setOnCheckedChangeListener(this);
				hydrantRepairType = 5;
			} else if (buttonView == (CompoundButton) surfacingHydrant && isChecked) {
				surfacingHydrant.setOnCheckedChangeListener(null);
				surfacingHydrant.setChecked(true);
				surfacingHydrant.setOnCheckedChangeListener(this);
				hydrantRepairType = 6;
			}
		}
	}

}
