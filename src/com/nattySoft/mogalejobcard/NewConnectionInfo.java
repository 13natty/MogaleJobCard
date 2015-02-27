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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NewConnectionInfo extends Fragment implements OnCheckedChangeListener {
	
	private CheckBox typeOfServiceFire;
	private CheckBox typeOfServiceDomestic;
	private int connectiontypeOfService = 0;
	private EditText connectionLength;
	private EditText connectionDiameter;
	private CheckBox roadCrossingYes;
	private CheckBox roadCrossingNo;
	private boolean connectionRoadCrossing;
	private EditText connectionDepth;
	private CheckBox connectionMaterialSteel;
	private CheckBox connectionMaterialHDPE;
	private CheckBox connectionMaterialuPVC;
	private CheckBox connectionMaterialAC;
	private int connectionMaterial = 0;
	
	
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
		typeOfServiceFire = (CheckBox) view.findViewById(R.id.service_fire);
		typeOfServiceFire.setOnCheckedChangeListener(this);
		typeOfServiceDomestic = (CheckBox) view.findViewById(R.id.service_domestic);
		typeOfServiceDomestic.setOnCheckedChangeListener(this);
		
		connectionLength = (EditText) view.findViewById(R.id.length_of_conn);
		connectionDiameter = (EditText) view.findViewById(R.id.size);
		
		roadCrossingYes = (CheckBox) view.findViewById(R.id.road_cross_yes);
		roadCrossingYes.setOnCheckedChangeListener(this);
		roadCrossingNo = (CheckBox) view.findViewById(R.id.road_cross_no);
		roadCrossingNo.setChecked(true);
		roadCrossingNo.setOnCheckedChangeListener(this);
		
		connectionDepth = (EditText) view.findViewById(R.id.depth);
		
		connectionMaterialSteel = (CheckBox) view.findViewById(R.id.steel);
		connectionMaterialSteel.setOnCheckedChangeListener(this);
		connectionMaterialHDPE = (CheckBox) view.findViewById(R.id.hdpe_black);
		connectionMaterialHDPE.setOnCheckedChangeListener(this);
		connectionMaterialuPVC = (CheckBox) view.findViewById(R.id.upvc);
		connectionMaterialuPVC.setOnCheckedChangeListener(this);
		connectionMaterialAC = (CheckBox) view.findViewById(R.id.ac);
		connectionMaterialAC.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				
				connectiontypeOfService = Integer.parseInt(json.getString("connectiontypeOfService"));
				if (connectiontypeOfService == 1) {
					typeOfServiceFire.setChecked(true);
				} else if (connectiontypeOfService == 2) {
					typeOfServiceDomestic.setChecked(true);
				}
				connectionRoadCrossing = Boolean.parseBoolean(json.getString("connectionRoadCrossing"));
				if (connectionRoadCrossing) {
					roadCrossingYes.setChecked(true);
				}
				
				connectionLength.setText(json.getString("connectionLength"));
				connectionDiameter.setText(json.getString("connectionDiameter"));
				
				connectionDepth.setText(json.getString("connectionDepth"));
				
				connectionMaterial = Integer.parseInt(json.getString("connectionMaterial"));
				switch (connectionMaterial) {
				case 1:
					connectionMaterialSteel.setChecked(true);
					break;
				case 2:
					connectionMaterialHDPE.setChecked(true);
					break;
				case 3:
					connectionMaterialuPVC.setChecked(true);
					break;
				case 4:
					connectionMaterialAC.setChecked(true);
					break;

				default:
					break;
				}
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
			json.accumulate("connectiontypeOfService", connectiontypeOfService);		
			
			json.accumulate("connectionLength", connectionLength.getText().toString());
			json.accumulate("connectionDiameter", connectionDiameter.getText().toString());
			json.accumulate("connectionDepth", connectionDepth.getText().toString());
			json.accumulate("connectionRoadCrossing", connectionRoadCrossing);
			json.accumulate("connectionMaterial", connectionMaterial);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_CONNECTION_INFO+FragmentIncident.incidentID,  json.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) typeOfServiceFire && isChecked) {
			typeOfServiceDomestic.setOnCheckedChangeListener(null);
			typeOfServiceDomestic.setChecked(false);
			typeOfServiceDomestic.setOnCheckedChangeListener(this);
			connectiontypeOfService = 1;
		} else if (buttonView == (CompoundButton) typeOfServiceDomestic && isChecked) {
			typeOfServiceFire.setOnCheckedChangeListener(null);
			typeOfServiceFire.setChecked(false);
			typeOfServiceFire.setOnCheckedChangeListener(this);
			connectiontypeOfService = 2;
		}
		else if ((buttonView == (CompoundButton) typeOfServiceFire || buttonView == (CompoundButton) typeOfServiceDomestic) && !isChecked) {
			connectiontypeOfService = 0;
		}else if (buttonView == (CompoundButton) roadCrossingYes && isChecked) {
			roadCrossingNo.setOnCheckedChangeListener(null);
			roadCrossingNo.setChecked(false);
			roadCrossingNo.setOnCheckedChangeListener(this);
			connectionRoadCrossing = true;
		} else if (buttonView == (CompoundButton) roadCrossingNo && isChecked) {
			roadCrossingYes.setOnCheckedChangeListener(null);
			roadCrossingYes.setChecked(false);
			roadCrossingYes.setOnCheckedChangeListener(this);
			connectionRoadCrossing = false;
		} else if (buttonView == (CompoundButton) roadCrossingYes || buttonView == (CompoundButton) roadCrossingNo) {
			roadCrossingNo.setOnCheckedChangeListener(null);
			roadCrossingNo.setChecked(true);
			roadCrossingNo.setOnCheckedChangeListener(this);
			connectionRoadCrossing = false;
		} else if (buttonView == (CompoundButton) connectionMaterialSteel || buttonView == (CompoundButton) connectionMaterialHDPE || buttonView == (CompoundButton) connectionMaterialuPVC || buttonView == (CompoundButton) connectionMaterialAC) {
			connectionMaterialSteel.setChecked(false);
			connectionMaterialHDPE.setChecked(false);
			connectionMaterialuPVC.setChecked(false);
			connectionMaterialAC.setChecked(false);
			connectionMaterial = 0;

			if (buttonView == (CompoundButton) connectionMaterialSteel && isChecked) {
				connectionMaterialSteel.setOnCheckedChangeListener(null);
				connectionMaterialSteel.setChecked(true);
				connectionMaterialSteel.setOnCheckedChangeListener(this);
				connectionMaterial = 1;
			} else if (buttonView == (CompoundButton) connectionMaterialHDPE && isChecked) {
				connectionMaterialHDPE.setOnCheckedChangeListener(null);
				connectionMaterialHDPE.setChecked(true);
				connectionMaterialHDPE.setOnCheckedChangeListener(this);
				connectionMaterial = 2;
			} else if (buttonView == (CompoundButton) connectionMaterialuPVC && isChecked) {
				connectionMaterialuPVC.setOnCheckedChangeListener(null);
				connectionMaterialuPVC.setChecked(true);
				connectionMaterialuPVC.setOnCheckedChangeListener(this);
				connectionMaterial = 3;
			} else if (buttonView == (CompoundButton) connectionMaterialAC && isChecked) {
				connectionMaterialAC.setOnCheckedChangeListener(null);
				connectionMaterialAC.setChecked(true);
				connectionMaterialAC.setOnCheckedChangeListener(this);
				connectionMaterial = 4;
			}
		} 
	}
}
