package com.nattySoft.mogalejobcard;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Hydrant extends Fragment implements OnItemSelectedListener {
	
//	private CheckBox hydrantAbove;
//	private CheckBox hydrantBelow;
//	private String hydrantPosition;
//	
//	private EditText hydrantNumber;
//	private EditText hydrantPressure;
//	private EditText hydrantTime;
//	private EditText hydrantLeftCorner;
//	private EditText hydrantRightCorner;
//	
//	private Spinner typeOfRepairSpinner;
//	private boolean hydrantReplace = false;
//	private boolean hydrantReplaceLid = false;
//	private boolean hydrantCloseLeak = false;
//	private boolean hydrantReplaceBox = false;
//	
//	private EditText code;

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
//		hydrantAbove = (CheckBox) view.findViewById(R.id.hydrant_above_ground);
//		hydrantAbove.setOnClickListener(new OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if(hydrantAbove.isChecked()){
//                	hydrantBelow.setChecked(false);
//                	hydrantPosition = "Above ground";
//                }else{
//                    System.out.println("Un-Checked");
//                }
//            }
//        });
		
//		hydrantBelow = (CheckBox) view.findViewById(R.id.hydrant_below_ground);
//		hydrantBelow.setOnClickListener(new OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if(hydrantBelow.isChecked()){
//                	hydrantAbove.setChecked(false);
//                	hydrantPosition = "Below ground";
//                }else{
//                    System.out.println("Un-Checked");
//                }
//            }
//        });
		
//		hydrantNumber = (EditText) view.findViewById(R.id.hydrant_number);
//		hydrantPressure = (EditText) view.findViewById(R.id.hydrant_pressure);
//		hydrantTime = (EditText) view.findViewById(R.id.hydrant_time);
//		hydrantLeftCorner = (EditText) view.findViewById(R.id.hydrant_left_corner);
//		hydrantRightCorner = (EditText) view.findViewById(R.id.hydrant_right_corner);
//		
//		typeOfRepairSpinner = (Spinner) view.findViewById(R.id.hydrant_type_of_repair_spinner);
//		typeOfRepairSpinner.setOnItemSelectedListener(this);
//		
//		code = (EditText) view.findViewById(R.id.hydrant_code);
//		code.setVisibility(View.GONE);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(parent.getItemAtPosition(pos).toString().equals("Code"))
		{
//			code.setVisibility(View.VISIBLE);
//			hydrantReplace = false;
//			hydrantReplaceLid = false;
//			hydrantCloseLeak = false;
//			hydrantReplaceBox = false;
		}
		else
		{
//			if(parent.getItemAtPosition(pos).toString().equals("203:Replace hydrant"))
//			{
//				hydrantReplace = true;
//				hydrantReplaceLid = false;
//				hydrantCloseLeak = false;
//				hydrantReplaceBox = false;
//			}
//			else if(parent.getItemAtPosition(pos).toString().equals("200:Replace Lid"))
//			{
//				hydrantReplace = false;
//				hydrantReplaceLid = true;
//				hydrantCloseLeak = false;
//				hydrantReplaceBox = false;
//			}
//			else if(parent.getItemAtPosition(pos).toString().equals("201:Close leaking hydrant"))
//			{
//				hydrantReplace = false;
//				hydrantReplaceLid = false;
//				hydrantCloseLeak = true;
//				hydrantReplaceBox = false;
//			}
//			else if(parent.getItemAtPosition(pos).toString().equals("202:Repair hydrant"))
//			{
//				hydrantReplace = false;
//				hydrantReplaceLid = false;
//				hydrantCloseLeak = false;
//				hydrantReplaceBox = false;
//			}
//			else if(parent.getItemAtPosition(pos).toString().equals("203:Replace box"))
//			{
//				hydrantReplace = false;
//				hydrantReplaceLid = false;
//				hydrantCloseLeak = false;
//				hydrantReplaceBox = true;
//			}
//			else
//			{
//				hydrantReplace = false;
//				hydrantReplaceLid = false;
//				hydrantCloseLeak = false;
//				hydrantReplaceBox = false;
//			}
//			code.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {		
	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_HYDRANT+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				
//				hydrantAbove.setChecked(Boolean.parseBoolean(json.getString("hydrantAbove")));
//				hydrantBelow.setChecked(Boolean.parseBoolean(json.getString("hydrantBelow")));
//				
//				hydrantNumber.setText(json.getString("hydrantNumber"));
//				hydrantPressure.setText(json.getString("hydrantPressure"));
//				hydrantTime.setText(json.getString("hydrantTime"));
//				hydrantLeftCorner.setText(json.getString("hydrantLeftCorner"));
//				hydrantRightCorner.setText(json.getString("hydrantRightCorner"));
//				
//				typeOfRepairSpinner.setSelection(Integer.parseInt(json.getString("typeOfRepairSpinner")));
//				
//				code.setText(json.getString("code"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}
	
	public void saveForm() {
		JSONObject json = new JSONObject();
//		try {
//			json.accumulate("hydrantAbove", hydrantAbove.isChecked());
//			json.accumulate("hydrantBelow", hydrantBelow.isChecked());
//			json.accumulate("hydrantPosition", hydrantPosition);
//			
//			json.accumulate("hydrantNumber", hydrantNumber.getText().toString());
//			json.accumulate("hydrantPressure", hydrantPressure.getText().toString());
//			json.accumulate("hydrantTime", hydrantTime.getText().toString());
//			json.accumulate("hydrantLeftCorner", hydrantLeftCorner.getText().toString());
//			json.accumulate("hydrantRightCorner", hydrantRightCorner.getText().toString());
//			
//			json.accumulate("typeOfRepairSpinner", typeOfRepairSpinner.getSelectedItemPosition());
//			json.accumulate("hydrantReplace", hydrantReplace);
//			json.accumulate("hydrantReplaceLid", hydrantReplaceLid);
//			json.accumulate("hydrantCloseLeak", hydrantCloseLeak);
//			json.accumulate("hydrantReplaceBox", hydrantReplaceBox);
//			
//			json.accumulate("code", code.getText().toString());
			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_HYDRANT+FragmentIncident.incidentID,  json.toString());
		
	}

}
