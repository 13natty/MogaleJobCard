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

public class ExistingMeterInformation extends Fragment implements OnCheckedChangeListener {

	private CheckBox meterAbove;
	private CheckBox meterBelow;
	private int existingMeterPosition;

	private EditText existingMeterNo;
	private CheckBox existingPrepaid;
	private CheckBox existingConventional;
	private int existingMeterType = 0;
	private CheckBox existingBulk;
	private CheckBox existingSingle;
	private CheckBox existingCombination;
	private int existingConventionalMeterType = 0;

	private CheckBox existingElsterKent;
	private CheckBox existingTeqnovo;
	private CheckBox existingSensus;
	private CheckBox existingPrepaidKent;
	private int existingMeterMake = 0;

	private EditText existingSize;
	private EditText existingMeterReading;
	private EditText existingNoDigits;

	// private DatePicker date;

	// private EditText distanceFromLeft;
	// private EditText distanceFromRight;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.existing_meter_info, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		meterAbove = (CheckBox) view.findViewById(R.id.above_ground);
		meterAbove.setOnCheckedChangeListener(this);
		meterBelow = (CheckBox) view.findViewById(R.id.below_ground);
		meterBelow.setOnCheckedChangeListener(this);

		existingMeterNo = (EditText) view.findViewById(R.id.existing_meter_no);
		existingPrepaid = (CheckBox) view.findViewById(R.id.meterTypePrepaid);
		existingPrepaid.setOnCheckedChangeListener(this);
		existingConventional = (CheckBox) view.findViewById(R.id.meterTypeConv);
		existingConventional.setOnCheckedChangeListener(this);

		existingBulk = (CheckBox) view.findViewById(R.id.meterTypeConvBulk);
		existingBulk.setOnCheckedChangeListener(this);
		existingBulk.setVisibility(View.GONE);
		existingCombination = (CheckBox) view.findViewById(R.id.meterTypeConvComb);
		existingCombination.setOnCheckedChangeListener(this);
		existingCombination.setVisibility(View.GONE);
		existingSingle = (CheckBox) view.findViewById(R.id.meterTypeConvSingle);
		existingSingle.setOnCheckedChangeListener(this);
		existingSingle.setVisibility(View.GONE);

		existingElsterKent = (CheckBox) view.findViewById(R.id.meterMakeElster);
		existingElsterKent.setOnCheckedChangeListener(this);
		existingTeqnovo = (CheckBox) view.findViewById(R.id.meterMakeTeqnovo);
		existingTeqnovo.setOnCheckedChangeListener(this);
		existingSensus = (CheckBox) view.findViewById(R.id.meterMakeSensus);
		existingSensus.setOnCheckedChangeListener(this);
		existingPrepaidKent = (CheckBox) view.findViewById(R.id.meterMakePrepaidKent);
		existingPrepaidKent.setOnCheckedChangeListener(this);
		existingSize = (EditText) view.findViewById(R.id.dia);
		existingMeterReading = (EditText) view.findViewById(R.id.meter_reading);
		existingNoDigits = (EditText) view.findViewById(R.id.no_digits);
	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);
				existingMeterPosition = Integer.parseInt(json.getString("existingMeterPosition"));
				if (existingMeterPosition == 1) {
					meterAbove.setChecked(true);
				} else if (existingMeterPosition == 2) {
					meterBelow.setChecked(true);
				}
								
				existingMeterNo.setText(json.getString("existingMeterNo"));
				
				existingMeterType = Integer.parseInt(json.getString("existingMeterType"));
				if(existingMeterType == 1)
				{
					existingPrepaid.setChecked(true);
				}
				else if(existingMeterType == 2)
				{
					existingConventional.setChecked(true);
					existingConventionalMeterType = Integer.parseInt(json.getString("existingConventionalMeterType"));
					if(existingConventionalMeterType == 1)
					{
						existingBulk.setChecked(true);
					}else if(existingConventionalMeterType == 2)
					{
						existingSingle.setChecked(true);
					}else if(existingConventionalMeterType == 3)
					{
						existingCombination.setChecked(true);
					}
				}
				
				existingMeterMake = Integer.parseInt(json.getString("existingMeterMake"));
				if(existingMeterMake == 1)
				{
					existingElsterKent.setChecked(true);
				}else if(existingMeterMake == 2)
				{
					existingTeqnovo.setChecked(true);
				}else if(existingMeterMake == 3)
				{
					existingSensus.setChecked(true);
				}else if(existingMeterMake == 4)
				{
					existingPrepaidKent.setChecked(true);
				}
				
				existingSize.setText(json.getString("existingSize"));
				existingMeterReading.setText(json.getString("existingMeterReading"));
				existingNoDigits.setText(json.getString("existingNoDigits"));
				
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
			
			json.accumulate("existingMeterPosition", existingMeterPosition);
			json.accumulate("existingMeterNo", existingMeterNo.getText().toString());

			json.accumulate("existingMeterType", existingMeterType);
			json.accumulate("existingConventionalMeterType", existingConventionalMeterType);
			json.accumulate("existingMeterMake", existingMeterMake);
			
			json.accumulate("existingSize", existingSize.getText().toString());
			json.accumulate("existingMeterReading", existingMeterReading.getText().toString());
			json.accumulate("existingNoDigits", existingNoDigits.getText().toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO + FragmentIncident.incidentID, json.toString());

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) meterAbove && isChecked) {
			meterBelow.setOnCheckedChangeListener(null);
			meterBelow.setChecked(false);
			meterBelow.setOnCheckedChangeListener(this);
			existingMeterPosition = 1;
		} else if (buttonView == (CompoundButton) meterBelow && isChecked) {
			meterAbove.setOnCheckedChangeListener(null);
			meterAbove.setChecked(false);
			meterAbove.setOnCheckedChangeListener(this);
			existingMeterPosition = 2;
		} else if ((buttonView == (CompoundButton) meterAbove || buttonView == (CompoundButton) meterBelow) && !isChecked) {
			existingMeterPosition = 0;
		} else if (buttonView == (CompoundButton) existingPrepaid || buttonView == (CompoundButton) existingConventional || buttonView == (CompoundButton) existingBulk || buttonView == (CompoundButton) existingSingle || buttonView == (CompoundButton) existingCombination) {
			

			if (buttonView == (CompoundButton) existingPrepaid && isChecked) {
				existingPrepaid.setChecked(false);
				existingConventional.setChecked(false);
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingMeterType = 0;
				
				existingPrepaid.setOnCheckedChangeListener(null);
				existingPrepaid.setChecked(true);
				existingPrepaid.setOnCheckedChangeListener(this);
				existingMeterType = 1;
				existingBulk.setVisibility(View.GONE);
				existingSingle.setVisibility(View.GONE);
				existingCombination.setVisibility(View.GONE);
				existingConventionalMeterType = 0;
			} else if (buttonView == (CompoundButton) existingConventional && isChecked) {
				existingPrepaid.setChecked(false);
				existingConventional.setChecked(false);
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingMeterType = 0;
				
				existingConventional.setOnCheckedChangeListener(null);
				existingConventional.setChecked(true);
				existingConventional.setOnCheckedChangeListener(this);
				existingBulk.setVisibility(View.VISIBLE);
				existingSingle.setVisibility(View.VISIBLE);
				existingCombination.setVisibility(View.VISIBLE);
				existingMeterType = 2;
			} else if (buttonView == (CompoundButton) existingConventional && !isChecked) {
				existingPrepaid.setChecked(false);
				existingConventional.setChecked(false);
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingMeterType = 0;
				
				existingBulk.setVisibility(View.GONE);
				existingSingle.setVisibility(View.GONE);
				existingCombination.setVisibility(View.GONE);
				existingConventionalMeterType = 0;
			} else if (buttonView == (CompoundButton) existingBulk && isChecked) {
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingBulk.setOnCheckedChangeListener(null);
				existingBulk.setChecked(true);
				existingBulk.setOnCheckedChangeListener(this);
				existingConventionalMeterType = 1;
			} else if (buttonView == (CompoundButton) existingSingle && isChecked) {
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingSingle.setOnCheckedChangeListener(null);
				existingSingle.setChecked(true);
				existingSingle.setOnCheckedChangeListener(this);
				existingConventionalMeterType = 2;
			} else if (buttonView == (CompoundButton) existingCombination && isChecked) {
				existingBulk.setChecked(false);
				existingSingle.setChecked(false);
				existingCombination.setChecked(false);
				existingCombination.setOnCheckedChangeListener(null);
				existingCombination.setChecked(true);
				existingCombination.setOnCheckedChangeListener(this);
				existingConventionalMeterType = 3;
			}
		}else if (buttonView == (CompoundButton) existingElsterKent || buttonView == (CompoundButton) existingTeqnovo || buttonView == (CompoundButton) existingSensus || buttonView == (CompoundButton) existingPrepaidKent) {
			existingElsterKent.setChecked(false);
			existingTeqnovo.setChecked(false);
			existingSensus.setChecked(false);
			existingPrepaidKent.setChecked(false);
			existingMeterMake = 0;

			if (buttonView == (CompoundButton) existingElsterKent && isChecked) {
				existingElsterKent.setOnCheckedChangeListener(null);
				existingElsterKent.setChecked(true);
				existingElsterKent.setOnCheckedChangeListener(this);
				existingMeterMake = 1;
			} else if (buttonView == (CompoundButton) existingTeqnovo && isChecked) {
				existingTeqnovo.setOnCheckedChangeListener(null);
				existingTeqnovo.setChecked(true);
				existingTeqnovo.setOnCheckedChangeListener(this);
				existingMeterMake = 2;
			}else if (buttonView == (CompoundButton) existingSensus && isChecked) {
				existingSensus.setOnCheckedChangeListener(null);
				existingSensus.setChecked(true);
				existingSensus.setOnCheckedChangeListener(this);
				existingMeterMake = 3;
			}else if (buttonView == (CompoundButton) existingPrepaidKent && isChecked) {
				existingPrepaidKent.setOnCheckedChangeListener(null);
				existingPrepaidKent.setChecked(true);
				existingPrepaidKent.setOnCheckedChangeListener(this);
				existingMeterMake = 4;
			}
		}

	}
}
