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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class NewMeterInformation extends Fragment {

	private CheckBox meterAbove;
	private CheckBox meterBelow;
	private String meterPosition;

	private EditText existingMeterNo;
	private CheckBox prepaid;
	private CheckBox conventional;
	private CheckBox bulk;
	private CheckBox single;
	private CheckBox combination;
	private CheckBox elsterKent;
	private CheckBox teqnovo;
	private CheckBox sensus;
	private CheckBox prepaidKent;
	private EditText size;
	private EditText meterReading;
	private EditText noDigits;

//	private DatePicker date;

//	private EditText distanceFromLeft;
//	private EditText distanceFromRight;
	
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
		
		meterAbove  = (CheckBox) view.findViewById(R.id.new_meter_above_ground);
		meterAbove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterAbove.isChecked()){
                	meterBelow.setChecked(false);
                	meterPosition = "Meter Above ground";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		meterBelow  = (CheckBox) view.findViewById(R.id.new_meter_below_ground);
		meterBelow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(meterBelow.isChecked()){
                	meterAbove.setChecked(false);
                	meterPosition = "Meter Below ground";
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
		
		existingMeterNo = (EditText) view.findViewById(R.id.new_meter_existing_meter_no);
		prepaid = (CheckBox) view.findViewById(R.id.new_meter_meterTypePrepaid);
		conventional = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConv);
		bulk = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvBulk);
		combination = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvComb);
		single = (CheckBox) view.findViewById(R.id.new_meter_meterTypeConvSingle);
		elsterKent = (CheckBox) view.findViewById(R.id.new_meter_meterMakeElster);
		teqnovo = (CheckBox) view.findViewById(R.id.new_meter_meterMakeTeqnovo);
		sensus = (CheckBox) view.findViewById(R.id.new_meter_meterMakeSensus);
		sensus = (CheckBox) view.findViewById(R.id.new_meter_meterMakePrepaidKent);
		size = (EditText) view.findViewById(R.id.new_meter_dia);
		meterReading = (EditText) view.findViewById(R.id.new_meter_meter_reading);
		noDigits = (EditText) view.findViewById(R.id.new_meter_no_digits);
	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				meterAbove.setChecked(Boolean.parseBoolean(json.getString("meterAboveOnSidewalk")));
				meterBelow.setChecked(Boolean.parseBoolean(json.getString("meterBelowOnSidewalk")));
				
				existingMeterNo.setText(json.getString("existingMeterNo"));
				prepaid.setChecked(Boolean.parseBoolean(json.getString("prepaid")));
				conventional.setChecked(Boolean.parseBoolean(json.getString("conventional")));
				bulk.setChecked(Boolean.parseBoolean(json.getString("bulk")));
				single.setChecked(Boolean.parseBoolean(json.getString("single")));
				combination.setChecked(Boolean.parseBoolean(json.getString("combination")));
				elsterKent.setChecked(Boolean.parseBoolean(json.getString("elsterKent")));
				teqnovo.setChecked(Boolean.parseBoolean(json.getString("teqnovo")));
				sensus.setChecked(Boolean.parseBoolean(json.getString("sensus")));
				prepaidKent.setChecked(Boolean.parseBoolean(json.getString("prepaidKent")));
				size.setText(json.getString("dia"));
				meterReading.setText(json.getString("meterReading"));
				noDigits.setText(json.getString("noDigits"));
				
				String dateStr = json.getString("date");
				if(dateStr.indexOf(';') != -1)
				{
					int year = Integer.parseInt(dateStr.substring(0, dateStr.indexOf(';')));
					dateStr = dateStr.substring(dateStr.indexOf(';')+1);
					int month = Integer.parseInt(dateStr.substring(0, dateStr.indexOf(';')));
					dateStr = dateStr.substring(dateStr.indexOf(';')+1);
					int day = Integer.parseInt(dateStr.substring(0));
//					date.updateDate(year, month, day);
				}
								
//				distanceFromLeft.setText(json.getString("distanceFromLeft"));
//				distanceFromRight.setText(json.getString("distanceFromRight"));
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
			json.accumulate("meterAboveOnSidewalk", meterAbove.isChecked());
			json.accumulate("meterBelowOnSidewalk", meterBelow.isChecked());
			json.accumulate("meterPosition", meterPosition);
			
			json.accumulate("existingMeterNo", existingMeterNo.getText().toString());
//			json.accumulate("make", make.getText().toString());
			json.accumulate("dia", size.getText().toString());
//			json.accumulate("meterNo", meterNo.getText().toString());
			json.accumulate("meterReading", meterReading.getText().toString());
			json.accumulate("noDigits", noDigits.getText().toString());
//			json.accumulate("comMType", comMType.getText().toString());
//			json.accumulate("comMDia", comMDia.getText().toString());
//			json.accumulate("comMMeterNo", comMMeterNo.getText().toString());
//			json.accumulate("comMMeterReading", comMMeterReading.getText().toString());
//			json.accumulate("comMNoDigits", comMNoDigits.getText().toString());
			
//			json.accumulate("checkMeterReading", checkMeterReading.isChecked());
			
//			json.accumulate("date", date.getYear()+";"+date.getMonth()+";"+date.getDayOfMonth());
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
//			long startTime = calendar.getTimeInMillis();
//			json.accumulate("meterReadingDate", startTime);
			
//			json.accumulate("distanceFromLeft", distanceFromLeft.getText().toString());
//			json.accumulate("distanceFromRight", distanceFromRight.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_EXISTING_METER_INFO+FragmentIncident.incidentID,  json.toString());
		
	}

}
