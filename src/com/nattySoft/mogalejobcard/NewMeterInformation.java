package com.nattySoft.mogalejobcard;

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
import android.widget.DatePicker;
import android.widget.EditText;

public class NewMeterInformation extends Fragment {
	
	private CheckBox meterAboveOnSidewalk;
	private CheckBox meterBelowOnSidewalk;
	private CheckBox meterOnStandAbove;
	private CheckBox meterOnStandBelow;
	private String newMeterPosition;

	private EditText existingMeterNo;
	private EditText scalingFactor;
	private EditText make;
	private EditText dia;
	private EditText meterNo;
	private EditText meterReading;
	private EditText noDigits;
	private EditText comMType;
	private EditText comMDia;
	private EditText comMMeterNo;
	private EditText comMMeterReading;
	private EditText comMNoDigits;

	
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
		
		meterAboveOnSidewalk  = (CheckBox) view.findViewById(R.id.new_above_ground);
		meterAboveOnSidewalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterAboveOnSidewalk.isChecked()){
                	meterBelowOnSidewalk.setChecked(false);
                	meterOnStandAbove.setChecked(false);
                	meterOnStandBelow.setChecked(false);
                	newMeterPosition = "Meter Above On Sidewalk";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		meterBelowOnSidewalk  = (CheckBox) view.findViewById(R.id.new_below_ground);
		meterBelowOnSidewalk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterBelowOnSidewalk.isChecked()){
                	meterAboveOnSidewalk.setChecked(false);
                	meterOnStandAbove.setChecked(false);
                	meterOnStandBelow.setChecked(false);
                	newMeterPosition = "Meter Below On Sidewalk";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		meterOnStandAbove  = (CheckBox) view.findViewById(R.id.new_onstand_above_ground);
		meterOnStandAbove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterOnStandAbove.isChecked()){
                	meterAboveOnSidewalk.setChecked(false);
                	meterBelowOnSidewalk.setChecked(false);
                	meterOnStandBelow.setChecked(false);
                	newMeterPosition = "Meter On Stand Above";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		meterOnStandBelow  = (CheckBox) view.findViewById(R.id.new_onstand_below_ground);
		meterOnStandBelow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterOnStandBelow.isChecked()){
                	meterAboveOnSidewalk.setChecked(false);
                	meterBelowOnSidewalk.setChecked(false);
                	meterOnStandAbove.setChecked(false);
                	newMeterPosition = "Meter On Stand Below";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		existingMeterNo = (EditText) view.findViewById(R.id.new_existing_meter_no);
		scalingFactor = (EditText) view.findViewById(R.id.new_scaling_factor);
		make = (EditText) view.findViewById(R.id.new_make);
		dia = (EditText) view.findViewById(R.id.new_dia);
		meterNo = (EditText) view.findViewById(R.id.new_meter_no);
		meterReading = (EditText) view.findViewById(R.id.new_meter_reading);
		noDigits = (EditText) view.findViewById(R.id.new_no_digits);
		comMType = (EditText) view.findViewById(R.id.new_type);
		comMDia = (EditText) view.findViewById(R.id.new_dia2);
		comMMeterNo = (EditText) view.findViewById(R.id.new_meter_no_2);
		comMMeterReading = (EditText) view.findViewById(R.id.new_meter_reading_2);
		comMNoDigits = (EditText) view.findViewById(R.id.new_no_digits_2);
	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_NEW_METER_INFO+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				meterAboveOnSidewalk.setChecked(Boolean.parseBoolean(json.getString("meterAboveOnSidewalk")));
				meterBelowOnSidewalk.setChecked(Boolean.parseBoolean(json.getString("meterBelowOnSidewalk")));
				meterOnStandAbove.setChecked(Boolean.parseBoolean(json.getString("meterOnStandAbove")));
				meterOnStandBelow.setChecked(Boolean.parseBoolean(json.getString("meterOnStandBelow")));				
				
				existingMeterNo.setText(json.getString("existingMeterNo"));
				scalingFactor.setText(json.getString("scalingFactor"));
				make.setText(json.getString("make"));
				dia.setText(json.getString("dia"));
				meterNo.setText(json.getString("meterNo"));
				meterReading.setText(json.getString("meterReading"));
				noDigits.setText(json.getString("noDigits"));
				comMType.setText(json.getString("comMType"));
				comMDia.setText(json.getString("comMDia"));
				comMMeterNo.setText(json.getString("comMMeterNo"));
				comMMeterReading.setText(json.getString("comMMeterReading"));
				comMNoDigits.setText(json.getString("comMNoDigits"));
				
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
			json.accumulate("meterAboveOnSidewalk", meterAboveOnSidewalk.isChecked());
			json.accumulate("meterBelowOnSidewalk", meterBelowOnSidewalk.isChecked());
			json.accumulate("meterOnStandAbove", meterOnStandAbove.isChecked());			
			json.accumulate("meterOnStandBelow", meterOnStandBelow.isChecked());
			json.accumulate("newMeterPosition", newMeterPosition);
			
			json.accumulate("existingMeterNo", existingMeterNo.getText().toString());
			json.accumulate("scalingFactor", scalingFactor.getText().toString());
			json.accumulate("make", make.getText().toString());
			json.accumulate("dia", dia.getText().toString());
			json.accumulate("meterNo", meterNo.getText().toString());
			json.accumulate("meterReading", meterReading.getText().toString());
			json.accumulate("noDigits", noDigits.getText().toString());
			json.accumulate("comMType", comMType.getText().toString());
			json.accumulate("comMDia", comMDia.getText().toString());
			json.accumulate("comMMeterNo", comMMeterNo.getText().toString());
			json.accumulate("comMMeterReading", comMMeterReading.getText().toString());
			json.accumulate("comMNoDigits", comMNoDigits.getText().toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_NEW_METER_INFO+FragmentIncident.incidentID,  json.toString());
		
	}
}
