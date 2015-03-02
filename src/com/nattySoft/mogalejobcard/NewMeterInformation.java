package com.nattySoft.mogalejobcard;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NewMeterInformation extends Fragment implements OnCheckedChangeListener {

	private CheckBox meterAbove;
	private CheckBox meterBelow;
	private int newMeterPosition = 0;

	private EditText newMeterNo;
	private CheckBox newPrepaid;
	private CheckBox newConventional;
	private int newMeterType = 0;
	private CheckBox newBulk;
	private CheckBox newSingle;
	private CheckBox newCombination;
	private int newConventionalMeterType = 0;

	private CheckBox newElsterKent;
	private CheckBox newTeqnovo;
	private CheckBox newSensus;
	private CheckBox newPrepaidKent;
	private int newMeterMake = 0;

	private EditText newSize;
	private EditText newMeterReading;
	private EditText newNoDigits;

	// private DatePicker date;

	// private EditText distanceFromLeft;
	// private EditText distanceFromRight;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.new_meter_info, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		meterAbove = (CheckBox) view.findViewById(R.id.new_meter_above_ground);
		meterAbove.setOnCheckedChangeListener(this);
		meterBelow = (CheckBox) view.findViewById(R.id.new_meter_below_ground);
		meterBelow.setOnCheckedChangeListener(this);

		newMeterNo = (EditText) view.findViewById(R.id.new_meter_meter_no);
		newPrepaid = (CheckBox) view.findViewById(R.id.new_meter_meterTypePrepaid);
		newPrepaid.setOnCheckedChangeListener(this);
		newConventional = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConv);
		newConventional.setOnCheckedChangeListener(this);

		newBulk = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvBulk);
		newBulk.setOnCheckedChangeListener(this);
		newBulk.setVisibility(View.GONE);
		newCombination = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvComb);
		newCombination.setOnCheckedChangeListener(this);
		newCombination.setVisibility(View.GONE);
		newSingle = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvSingle);
		newSingle.setOnCheckedChangeListener(this);
		newSingle.setVisibility(View.GONE);

		newElsterKent = (CheckBox) view.findViewById(R.id.new_meter_meterMakeElster);
		newElsterKent.setOnCheckedChangeListener(this);
		newTeqnovo = (CheckBox) view.findViewById(R.id.new_meter_meterMakeTeqnovo);
		newTeqnovo.setOnCheckedChangeListener(this);
		newSensus = (CheckBox) view.findViewById(R.id.new_meter_meterMakeSensus);
		newSensus.setOnCheckedChangeListener(this);
		newPrepaidKent = (CheckBox) view.findViewById(R.id.new_meter_meterMakePrepaidKent);
		newPrepaidKent.setOnCheckedChangeListener(this);
		newSize = (EditText) view.findViewById(R.id.new_meter_dia);
		newMeterReading = (EditText) view.findViewById(R.id.new_meter_meter_reading);
		newNoDigits = (EditText) view.findViewById(R.id.new_meter_no_digits);
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_NEW_METER_INFO + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);
				newMeterPosition = Integer.parseInt(json.getString("newMeterPosition"));
				if (newMeterPosition == 1) {
					meterAbove.setChecked(true);
				} else if (newMeterPosition == 2) {
					meterBelow.setChecked(true);
				}

				newMeterNo.setText(json.getString("newMeterNum"));

				newMeterType = Integer.parseInt(json.getString("newMeterType"));
				if (newMeterType == 1) {
					newPrepaid.setChecked(true);
				} else if (newMeterType == 2) {
					newConventional.setChecked(true);
					newConventionalMeterType = Integer.parseInt(json.getString("newConventionalMeterType"));
					if (newConventionalMeterType == 1) {
						newBulk.setChecked(true);
					} else if (newConventionalMeterType == 2) {
						newSingle.setChecked(true);
					} else if (newConventionalMeterType == 3) {
						newCombination.setChecked(true);
					}
				}

				newMeterMake = Integer.parseInt(json.getString("newMeterMake"));
				if (newMeterMake == 1) {
					newElsterKent.setChecked(true);
				} else if (newMeterMake == 2) {
					newTeqnovo.setChecked(true);
				} else if (newMeterMake == 3) {
					newSensus.setChecked(true);
				} else if (newMeterMake == 4) {
					newPrepaidKent.setChecked(true);
				}

				newSize.setText(json.getString("newMeterSize"));
				newMeterReading.setText(json.getString("newMeterReading"));
				newNoDigits.setText(json.getString("newMeterNumOfDigits"));

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

			json.accumulate("newMeterPosition", newMeterPosition);
			if (newMeterNo != null)
				json.accumulate("newMeterNum", newMeterNo.getText().toString());
			else
				json.accumulate("newMeterNum", "");
			
			json.accumulate("newMeterType", newMeterType);
			json.accumulate("newConventionalMeterType", newConventionalMeterType);
			json.accumulate("newMeterMake", newMeterMake);

			if (newSize != null)
				json.accumulate("newMeterSize", newSize.getText().toString());
			else
				json.accumulate("newMeterSize", "");
			
			if (newMeterReading != null)
				json.accumulate("newMeterReading", newMeterReading.getText().toString());
			else
				json.accumulate("newMeterReading", "");
			if (newNoDigits != null)
				json.accumulate("newMeterNumOfDigits", newNoDigits.getText().toString());
			else
				json.accumulate("newMeterNumOfDigits", "");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_NEW_METER_INFO + FragmentIncident.incidentID, json.toString());

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) meterAbove && isChecked) {
			meterBelow.setOnCheckedChangeListener(null);
			meterBelow.setChecked(false);
			meterBelow.setOnCheckedChangeListener(this);
			newMeterPosition = 1;
		} else if (buttonView == (CompoundButton) meterBelow && isChecked) {
			meterAbove.setOnCheckedChangeListener(null);
			meterAbove.setChecked(false);
			meterAbove.setOnCheckedChangeListener(this);
			newMeterPosition = 2;
		} else if ((buttonView == (CompoundButton) meterAbove || buttonView == (CompoundButton) meterBelow) && !isChecked) {
			newMeterPosition = 0;
		} else if (buttonView == (CompoundButton) newPrepaid || buttonView == (CompoundButton) newConventional || buttonView == (CompoundButton) newBulk || buttonView == (CompoundButton) newSingle || buttonView == (CompoundButton) newCombination) {

			if (buttonView == (CompoundButton) newPrepaid && isChecked) {
				newPrepaid.setChecked(false);
				newConventional.setChecked(false);
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newMeterType = 0;

				newPrepaid.setOnCheckedChangeListener(null);
				newPrepaid.setChecked(true);
				newPrepaid.setOnCheckedChangeListener(this);
				newMeterType = 1;
				newBulk.setVisibility(View.GONE);
				newSingle.setVisibility(View.GONE);
				newCombination.setVisibility(View.GONE);
				newConventionalMeterType = 0;
			} else if (buttonView == (CompoundButton) newConventional && isChecked) {
				newPrepaid.setChecked(false);
				newConventional.setChecked(false);
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newMeterType = 0;

				newConventional.setOnCheckedChangeListener(null);
				newConventional.setChecked(true);
				newConventional.setOnCheckedChangeListener(this);
				newBulk.setVisibility(View.VISIBLE);
				newSingle.setVisibility(View.VISIBLE);
				newCombination.setVisibility(View.VISIBLE);
				newMeterType = 2;
			} else if (buttonView == (CompoundButton) newConventional && !isChecked) {
				newPrepaid.setChecked(false);
				newConventional.setChecked(false);
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newMeterType = 0;

				newBulk.setVisibility(View.GONE);
				newSingle.setVisibility(View.GONE);
				newCombination.setVisibility(View.GONE);
				newConventionalMeterType = 0;
			} else if (buttonView == (CompoundButton) newBulk && isChecked) {
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newBulk.setOnCheckedChangeListener(null);
				newBulk.setChecked(true);
				newBulk.setOnCheckedChangeListener(this);
				newConventionalMeterType = 1;
			} else if (buttonView == (CompoundButton) newSingle && isChecked) {
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newSingle.setOnCheckedChangeListener(null);
				newSingle.setChecked(true);
				newSingle.setOnCheckedChangeListener(this);
				newConventionalMeterType = 2;
			} else if (buttonView == (CompoundButton) newCombination && isChecked) {
				newBulk.setChecked(false);
				newSingle.setChecked(false);
				newCombination.setChecked(false);
				newCombination.setOnCheckedChangeListener(null);
				newCombination.setChecked(true);
				newCombination.setOnCheckedChangeListener(this);
				newConventionalMeterType = 3;
			}
		} else if (buttonView == (CompoundButton) newElsterKent || buttonView == (CompoundButton) newTeqnovo || buttonView == (CompoundButton) newSensus || buttonView == (CompoundButton) newPrepaidKent) {
			newElsterKent.setChecked(false);
			newTeqnovo.setChecked(false);
			newSensus.setChecked(false);
			newPrepaidKent.setChecked(false);
			newMeterMake = 0;

			if (buttonView == (CompoundButton) newElsterKent && isChecked) {
				newElsterKent.setOnCheckedChangeListener(null);
				newElsterKent.setChecked(true);
				newElsterKent.setOnCheckedChangeListener(this);
				newMeterMake = 1;
			} else if (buttonView == (CompoundButton) newTeqnovo && isChecked) {
				newTeqnovo.setOnCheckedChangeListener(null);
				newTeqnovo.setChecked(true);
				newTeqnovo.setOnCheckedChangeListener(this);
				newMeterMake = 2;
			} else if (buttonView == (CompoundButton) newSensus && isChecked) {
				newSensus.setOnCheckedChangeListener(null);
				newSensus.setChecked(true);
				newSensus.setOnCheckedChangeListener(this);
				newMeterMake = 3;
			} else if (buttonView == (CompoundButton) newPrepaidKent && isChecked) {
				newPrepaidKent.setOnCheckedChangeListener(null);
				newPrepaidKent.setChecked(true);
				newPrepaidKent.setOnCheckedChangeListener(this);
				newMeterMake = 4;
			}
		}

	}
}
