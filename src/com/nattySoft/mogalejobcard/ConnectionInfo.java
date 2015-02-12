package com.nattySoft.mogalejobcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

public class ConnectionInfo extends Fragment {
	
	private Spinner typeOfServiceSpinner;
	private EditText connectionLength;
	private EditText connectionDiameter;
	private Spinner roadCrossingSpinner;
	private EditText connectionDepth;
	private Spinner typeOfMaterialSpinner;
	private Spinner typeOfConnectionSpinner;
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.connection_info, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		typeOfServiceSpinner = (Spinner) view.findViewById(R.id.type_of_service_spinner);
		
		connectionLength = (EditText) view.findViewById(R.id.connection_length);
		connectionDiameter = (EditText) view.findViewById(R.id.connection_diameter);
		
		roadCrossingSpinner = (Spinner) view.findViewById(R.id.connection_road_crossing_spinner);
		
		connectionDepth = (EditText) view.findViewById(R.id.connection_depth);
		
		typeOfMaterialSpinner = (Spinner) view.findViewById(R.id.type_of_material_spinner);
		typeOfConnectionSpinner = (Spinner) view.findViewById(R.id.type_of_connection_spinner);
	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				
				typeOfServiceSpinner.setSelection(Integer.parseInt(json.getString("typeOfServiceSpinner")));
				
				connectionLength.setText(json.getString("connectionLength"));
				connectionDiameter.setText(json.getString("connectionDiameter"));
				
				roadCrossingSpinner.setSelection(Integer.parseInt(json.getString("roadCrossingSpinner")));
				
				connectionDepth.setText(json.getString("connectionDepth"));
				
				typeOfMaterialSpinner.setSelection(Integer.parseInt(json.getString("typeOfMaterialSpinner")));
				typeOfConnectionSpinner.setSelection(Integer.parseInt(json.getString("typeOfConnectionSpinner")));
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
			json.accumulate("typeOfServiceSpinner", typeOfServiceSpinner.getSelectedItemPosition());
			Log.d("connectionInfo", "typeOfServiceSpinner.getItemAtPosition(typeOfServiceSpinner.getSelectedItemPosition()) "+typeOfServiceSpinner.getItemAtPosition(typeOfServiceSpinner.getSelectedItemPosition()));
			json.accumulate("connectionServiceType", typeOfServiceSpinner.getItemAtPosition(typeOfServiceSpinner.getSelectedItemPosition()));			
			
			json.accumulate("connectionLength", connectionLength.getText().toString());
			json.accumulate("connectionDiameter", connectionDiameter.getText().toString());
			
			json.accumulate("roadCrossingSpinner", roadCrossingSpinner.getSelectedItemPosition());
			json.accumulate("connectionCrossRoad", roadCrossingSpinner.getItemAtPosition(roadCrossingSpinner.getSelectedItemPosition()).equals("yes")?true:false);			
			
			json.accumulate("connectionDepth", connectionDepth.getText().toString());
						
			json.accumulate("typeOfMaterialSpinner", typeOfMaterialSpinner.getSelectedItemPosition());
			json.accumulate("connectionMaterialType", typeOfMaterialSpinner.getItemAtPosition(typeOfMaterialSpinner.getSelectedItemPosition()));
			
			json.accumulate("typeOfConnectionSpinner", typeOfConnectionSpinner.getSelectedItemPosition());
			json.accumulate("connectionType", typeOfConnectionSpinner.getItemAtPosition(typeOfConnectionSpinner.getSelectedItemPosition()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID,  json.toString());
	}
}
