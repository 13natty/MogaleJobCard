package com.nattySoft.mogalejobcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.nattySoft.mogalejobcard.util.Preferences;

import android.R.integer;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PipeLineInfo extends Fragment implements OnItemSelectedListener {
	private Spinner pipeDiameterSpinner;
	private Spinner pipeMaterialSpinner;
	private Spinner typeOfRepairSpinner;

	private Spinner locationSpinner;
	private Spinner roadCrossingSpinner;
	private Spinner holeSizeSpinner;

	private EditText otherDiameter;
	private EditText otherMaterial;
	private EditText cutOutLength;
	private EditText replaceLength;
	private EditText flushHydrantTime;
	private EditText code;

	private EditText distance;
	private EditText left;
	private EditText right;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.pipe_line_info, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pipeDiameterSpinner = (Spinner) view.findViewById(R.id.pipe_diameter_spinner);
		pipeDiameterSpinner.setOnItemSelectedListener(this);

		pipeMaterialSpinner = (Spinner) view.findViewById(R.id.pipe_material_spinner);
		pipeMaterialSpinner.setOnItemSelectedListener(this);

		typeOfRepairSpinner = (Spinner) view.findViewById(R.id.type_of_repair_spinner);
		typeOfRepairSpinner.setOnItemSelectedListener(this);

		locationSpinner = (Spinner) view.findViewById(R.id.location_spinner);
		roadCrossingSpinner = (Spinner) view.findViewById(R.id.road_crossing_spinner);
		holeSizeSpinner = (Spinner) view.findViewById(R.id.hole_size_spinner);

		otherDiameter = (EditText) view.findViewById(R.id.other_size);
		otherDiameter.setVisibility(View.GONE);

		otherMaterial = (EditText) view.findViewById(R.id.other_material);
		otherMaterial.setVisibility(View.GONE);

		cutOutLength = (EditText) view.findViewById(R.id.cut_out_length);
		cutOutLength.setVisibility(View.GONE);

		replaceLength = (EditText) view.findViewById(R.id.replace_length);
		replaceLength.setVisibility(View.GONE);

		flushHydrantTime = (EditText) view.findViewById(R.id.flush_hydrant_time);
		flushHydrantTime.setVisibility(View.GONE);

		code = (EditText) view.findViewById(R.id.code);
		code.setVisibility(View.GONE);

		distance = (EditText) view.findViewById(R.id.distance);
		left = (EditText) view.findViewById(R.id.left);
		right = (EditText) view.findViewById(R.id.right);

	}
	
	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_PIPELINE_INFO+FragmentIncident.incidentID);
		try {
			if(jsonStr != null)
			{
				JSONObject json = new JSONObject(jsonStr);
				
				pipeDiameterSpinner.setSelection(Integer.parseInt(json.getString("pipeDiameterSpinner")));
				pipeMaterialSpinner.setSelection(Integer.parseInt(json.getString("pipeMaterialSpinner")));
				typeOfRepairSpinner.setSelection(Integer.parseInt(json.getString("typeOfRepairSpinner")));
				locationSpinner.setSelection(Integer.parseInt(json.getString("locationSpinner")));
				roadCrossingSpinner.setSelection(Integer.parseInt(json.getString("roadCrossingSpinner")));
				holeSizeSpinner.setSelection(Integer.parseInt(json.getString("holeSizeSpinner")));
				
				otherDiameter.setText(json.getString("otherDiameter"));
				otherMaterial.setText(json.getString("otherMaterial"));
				cutOutLength.setText(json.getString("cutOutLength"));
				replaceLength.setText(json.getString("replaceLength"));
				flushHydrantTime.setText(json.getString("flushHydrantTime"));
				code.setText(json.getString("code"));
				distance.setText(json.getString("distance"));
				left.setText(json.getString("left"));
				right.setText(json.getString("right"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

		if (parent.getItemAtPosition(pos).toString().equals("Other size")) {
			otherDiameter.setVisibility(View.VISIBLE);
		} else if (parent.equals(pipeDiameterSpinner)) {
			otherDiameter.setVisibility(View.GONE);
		} else if (parent.getItemAtPosition(pos).toString().equals("Other")) {
			otherMaterial.setVisibility(View.VISIBLE);
		} else if (parent.equals(pipeMaterialSpinner)) {
			otherMaterial.setVisibility(View.GONE);
		} else if (parent.getItemAtPosition(pos).toString().equals("300:Cut out")) {
			cutOutLength.setVisibility(View.VISIBLE);
			replaceLength.setVisibility(View.GONE);
			flushHydrantTime.setVisibility(View.GONE);
			code.setVisibility(View.GONE);
		} else if (parent.getItemAtPosition(pos).toString().equals("302:Replace")) {
			replaceLength.setVisibility(View.VISIBLE);
			cutOutLength.setVisibility(View.GONE);
			flushHydrantTime.setVisibility(View.GONE);
			code.setVisibility(View.GONE);
		} else if (parent.getItemAtPosition(pos).toString().equals("205:Flush hydrant")) {
			flushHydrantTime.setVisibility(View.VISIBLE);
			cutOutLength.setVisibility(View.GONE);
			replaceLength.setVisibility(View.GONE);
			code.setVisibility(View.GONE);
		} else if (parent.getItemAtPosition(pos).toString().equals("Code")) {
			code.setVisibility(View.VISIBLE);
			cutOutLength.setVisibility(View.GONE);
			replaceLength.setVisibility(View.GONE);
			flushHydrantTime.setVisibility(View.GONE);
		} else {
			cutOutLength.setVisibility(View.GONE);
			replaceLength.setVisibility(View.GONE);
			flushHydrantTime.setVisibility(View.GONE);
			code.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		otherDiameter.setVisibility(View.GONE);

	}

	public void saveForm() {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("pipeDiameterSpinner", pipeDiameterSpinner.getSelectedItemPosition());
			json.accumulate("pipelineDiameter", pipeDiameterSpinner.getItemAtPosition(pipeDiameterSpinner.getSelectedItemPosition()));
			json.accumulate("pipeMaterialSpinner", pipeMaterialSpinner.getSelectedItemPosition());
			json.accumulate("pipelineMaterial", pipeMaterialSpinner.getItemAtPosition(pipeMaterialSpinner.getSelectedItemPosition()));
			json.accumulate("typeOfRepairSpinner", typeOfRepairSpinner.getSelectedItemPosition());
			json.accumulate("pipelineRepairType", typeOfRepairSpinner.getItemAtPosition(typeOfRepairSpinner.getSelectedItemPosition()));
			json.accumulate("locationSpinner", locationSpinner.getSelectedItemPosition());
			json.accumulate("pipelineLocation", locationSpinner.getItemAtPosition(locationSpinner.getSelectedItemPosition()));
			json.accumulate("roadCrossingSpinner", roadCrossingSpinner.getSelectedItemPosition());
			json.accumulate("pipelineCrossRoad", roadCrossingSpinner.getItemAtPosition(roadCrossingSpinner.getSelectedItemPosition()));
			json.accumulate("holeSizeSpinner", holeSizeSpinner.getSelectedItemPosition());
			json.accumulate("pipelineHoleSize", holeSizeSpinner.getItemAtPosition(holeSizeSpinner.getSelectedItemPosition()));
			
			json.accumulate("otherDiameter", otherDiameter.getText().toString());
			json.accumulate("otherMaterial", otherMaterial.getText().toString());
			json.accumulate("cutOutLength", cutOutLength.getText().toString());
			json.accumulate("replaceLength", replaceLength.getText().toString());
			json.accumulate("flushHydrantTime", flushHydrantTime.getText().toString());
			json.accumulate("code", code.getText().toString());
			json.accumulate("distance", distance.getText().toString());
			json.accumulate("left", left.getText().toString());
			json.accumulate("right", right.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_PIPELINE_INFO+FragmentIncident.incidentID,  json.toString());
	}
}
