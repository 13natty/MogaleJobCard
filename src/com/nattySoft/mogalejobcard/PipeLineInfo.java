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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PipeLineInfo extends Fragment implements OnCheckedChangeListener {

	private CheckBox midBlock;
	private CheckBox sideWalk;
	private int pipelineLocation = 0;

	private CheckBox roadCrossingYes;
	private CheckBox roadCrossingNo;
	private boolean pipelineRoadCrossing = false;

	private CheckBox pipeDiameter20;
	private CheckBox pipeDiameter25;
	private CheckBox pipeDiameter32;
	private CheckBox pipeDiameter40;
	private CheckBox pipeDiameter50;
	private CheckBox pipeDiameter75;
	private CheckBox pipeDiameter90;
	private CheckBox pipeDiameter110;
	private CheckBox pipeDiameter160;
	private CheckBox pipeDiameter200;
	private CheckBox pipeDiameter250;
	private CheckBox pipeDiameterOther;
	private TextView otherDiameterLabel;
	private EditText otherDiameter;
	private int pipeDiameter = 0;

	private CheckBox pipeMaterialSteel;
	private CheckBox pipeMaterialHDPE;
	private CheckBox pipeMaterialuPVC;
	private CheckBox pipeMaterialAC;
	private int pipeMaterial = 0;

	private CheckBox typeOfRepairCutOutLength;
	private CheckBox typeOfRepairFixBurstPipe;
	private int typeOfRepair = 0;

	private TextView cutOutLengthLabel;
	private EditText cutOutLength;
	private int pipeLineCutOutLength = 0;

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

		midBlock = (CheckBox) view.findViewById(R.id.mid_block);
		midBlock.setOnCheckedChangeListener(this);
		sideWalk = (CheckBox) view.findViewById(R.id.side_walk);
		sideWalk.setOnCheckedChangeListener(this);

		roadCrossingYes = (CheckBox) view.findViewById(R.id.pipe_line_crossing_yes);
		roadCrossingYes.setOnCheckedChangeListener(this);
		roadCrossingNo = (CheckBox) view.findViewById(R.id.pipe_line_crossing_no);
		roadCrossingNo.setChecked(true);
		roadCrossingNo.setOnCheckedChangeListener(this);

		pipeDiameter20 = (CheckBox) view.findViewById(R.id.pipe_dia_20);
		pipeDiameter20.setOnCheckedChangeListener(this);
		pipeDiameter25 = (CheckBox) view.findViewById(R.id.pipe_dia_25);
		pipeDiameter25.setOnCheckedChangeListener(this);
		pipeDiameter32 = (CheckBox) view.findViewById(R.id.pipe_dia_32);
		pipeDiameter32.setOnCheckedChangeListener(this);
		pipeDiameter40 = (CheckBox) view.findViewById(R.id.pipe_dia_40);
		pipeDiameter40.setOnCheckedChangeListener(this);
		pipeDiameter50 = (CheckBox) view.findViewById(R.id.pipe_dia_50);
		pipeDiameter50.setOnCheckedChangeListener(this);
		pipeDiameter75 = (CheckBox) view.findViewById(R.id.pipe_dia_75);
		pipeDiameter75.setOnCheckedChangeListener(this);
		pipeDiameter90 = (CheckBox) view.findViewById(R.id.pipe_dia_90);
		pipeDiameter90.setOnCheckedChangeListener(this);
		pipeDiameter110 = (CheckBox) view.findViewById(R.id.pipe_dia_110);
		pipeDiameter110.setOnCheckedChangeListener(this);
		pipeDiameter160 = (CheckBox) view.findViewById(R.id.pipe_dia_160);
		pipeDiameter160.setOnCheckedChangeListener(this);
		pipeDiameter200 = (CheckBox) view.findViewById(R.id.pipe_dia_200);
		pipeDiameter200.setOnCheckedChangeListener(this);
		pipeDiameter250 = (CheckBox) view.findViewById(R.id.pipe_dia_250);
		pipeDiameter250.setOnCheckedChangeListener(this);
		pipeDiameterOther = (CheckBox) view.findViewById(R.id.pipe_dia_other);
		pipeDiameterOther.setOnCheckedChangeListener(this);

		otherDiameter = (EditText) view.findViewById(R.id.other_size);
		otherDiameterLabel = (TextView) view.findViewById(R.id.other_size_label);
		otherDiameter.setVisibility(View.GONE);
		otherDiameterLabel.setVisibility(View.GONE);

		pipeMaterialSteel = (CheckBox) view.findViewById(R.id.pipe_line_steel);
		pipeMaterialSteel.setOnCheckedChangeListener(this);
		pipeMaterialHDPE = (CheckBox) view.findViewById(R.id.pipe_line_hdpe_black);
		pipeMaterialHDPE.setOnCheckedChangeListener(this);
		pipeMaterialuPVC = (CheckBox) view.findViewById(R.id.pipe_line_upvc);
		pipeMaterialuPVC.setOnCheckedChangeListener(this);
		pipeMaterialAC = (CheckBox) view.findViewById(R.id.pipe_line_ac);
		pipeMaterialAC.setOnCheckedChangeListener(this);

		typeOfRepairCutOutLength = (CheckBox) view.findViewById(R.id.pipe_line_cut_out_length);
		typeOfRepairCutOutLength.setOnCheckedChangeListener(this);
		typeOfRepairFixBurstPipe = (CheckBox) view.findViewById(R.id.pipe_line_fix_burst_pipe);
		typeOfRepairFixBurstPipe.setOnCheckedChangeListener(this);
		cutOutLength = (EditText) view.findViewById(R.id.cut_out_lenth);
		cutOutLengthLabel = (TextView) view.findViewById(R.id.cut_out_length_label);
		cutOutLength.setVisibility(View.GONE);
		cutOutLengthLabel.setVisibility(View.GONE);

	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_PIPELINE_INFO + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);

				pipelineLocation = Integer.parseInt(json.getString("pipelineLocation"));
				if (pipelineLocation == 1) {
					midBlock.setChecked(true);
				} else if (pipelineLocation == 2) {
					sideWalk.setChecked(true);
				}
				pipelineRoadCrossing = Boolean.parseBoolean(json.getString("pipelineCrossRoad"));
				if (pipelineRoadCrossing) {
					roadCrossingYes.setChecked(true);
				}
				pipeDiameter = Integer.parseInt(json.getString("pipelineDiameter"));
				if (pipeDiameter > 0) {
					if (pipeDiameter == 20 || pipeDiameter == 25 || pipeDiameter == 32 || pipeDiameter == 40 || pipeDiameter == 50 || pipeDiameter == 75 || pipeDiameter == 90 || pipeDiameter == 110 || pipeDiameter == 160 || pipeDiameter == 200 || pipeDiameter == 250) {
						switch (pipeDiameter) {
						case 20:
							pipeDiameter20.setChecked(true);
							break;
						case 25:
							pipeDiameter25.setChecked(true);
							break;
						case 32:
							pipeDiameter32.setChecked(true);
							break;
						case 40:
							pipeDiameter40.setChecked(true);
							break;
						case 50:
							pipeDiameter50.setChecked(true);
							break;
						case 75:
							pipeDiameter75.setChecked(true);
							break;
						case 90:
							pipeDiameter90.setChecked(true);
							break;
						case 110:
							pipeDiameter110.setChecked(true);
							break;
						case 160:
							pipeDiameter160.setChecked(true);
							break;
						case 200:
							pipeDiameter200.setChecked(true);
							break;
						case 250:
							pipeDiameter250.setChecked(true);
							break;

						default:
							break;
						}
					} else {
						pipeDiameterOther.setChecked(true);
						otherDiameter.setText(json.getString("pipelineDiameter"));
					}
				}
				pipeMaterial = Integer.parseInt(json.getString("pipelineMaterial"));
				switch (pipeMaterial) {
				case 1:
					pipeMaterialSteel.setChecked(true);
					break;
				case 2:
					pipeMaterialHDPE.setChecked(true);
					break;
				case 3:
					pipeMaterialuPVC.setChecked(true);
					break;
				case 4:
					pipeMaterialAC.setChecked(true);
					break;

				default:
					break;
				}
				typeOfRepair = Integer.parseInt(json.getString("pipelineRepairType"));
				switch (typeOfRepair) {
				case 1:
					typeOfRepairCutOutLength.setChecked(true);
					cutOutLength.setText(json.getString("pipelineCutOutLength"));
					break;
				case 2:
					typeOfRepairFixBurstPipe.setChecked(true);
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
			json.accumulate("pipelineLocation", pipelineLocation);
			json.accumulate("pipelineCrossRoad", pipelineRoadCrossing);
			if (pipeDiameter == 0) {
				if (otherDiameter.getText().length() > 0) {
					int dia = Integer.parseInt(otherDiameter.getText().toString());
					if (dia > 0) {
						pipeDiameter = dia;
					}
				}
			}
			json.accumulate("pipelineDiameter", pipeDiameter);
			json.accumulate("pipelineMaterial", pipeMaterial);
			json.accumulate("pipelineRepairType", typeOfRepair);
			if (typeOfRepair == 1) {
				if (cutOutLength.getText().length() > 0) {
					int cutLength = Integer.parseInt(cutOutLength.getText().toString());
					if (cutLength > 0) {
						pipeLineCutOutLength = cutLength;
					} else {
						pipeLineCutOutLength = 6;
					}
				} else {
					pipeLineCutOutLength = 6;
				}
			}
			json.accumulate("pipelineCutOutLength", pipeLineCutOutLength);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_PIPELINE_INFO + FragmentIncident.incidentID, json.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) midBlock && isChecked) {
			sideWalk.setOnCheckedChangeListener(null);
			sideWalk.setChecked(false);
			sideWalk.setOnCheckedChangeListener(this);
			pipelineLocation = 1;
		} else if (buttonView == (CompoundButton) sideWalk && isChecked) {
			midBlock.setOnCheckedChangeListener(null);
			midBlock.setChecked(false);
			midBlock.setOnCheckedChangeListener(this);
			pipelineLocation = 2;
		} else if ((buttonView == (CompoundButton) midBlock || buttonView == (CompoundButton) sideWalk) && !isChecked) {
			pipelineLocation = 0;
		} else if (buttonView == (CompoundButton) roadCrossingYes && isChecked) {
			roadCrossingNo.setOnCheckedChangeListener(null);
			roadCrossingNo.setChecked(false);
			roadCrossingNo.setOnCheckedChangeListener(this);
			pipelineRoadCrossing = true;
		} else if (buttonView == (CompoundButton) roadCrossingNo && isChecked) {
			roadCrossingYes.setOnCheckedChangeListener(null);
			roadCrossingYes.setChecked(false);
			roadCrossingYes.setOnCheckedChangeListener(this);
			pipelineRoadCrossing = false;
		} else if (buttonView == (CompoundButton) roadCrossingYes || buttonView == (CompoundButton) roadCrossingNo) {
			roadCrossingNo.setOnCheckedChangeListener(null);
			roadCrossingNo.setChecked(true);
			roadCrossingNo.setOnCheckedChangeListener(this);
			pipelineRoadCrossing = false;
		} else if (buttonView == (CompoundButton) pipeDiameter20 || buttonView == (CompoundButton) pipeDiameter25 || buttonView == (CompoundButton) pipeDiameter32 || buttonView == (CompoundButton) pipeDiameter40 || buttonView == (CompoundButton) pipeDiameter50 || buttonView == (CompoundButton) pipeDiameter75 || buttonView == (CompoundButton) pipeDiameter90 || buttonView == (CompoundButton) pipeDiameter110 || buttonView == (CompoundButton) pipeDiameter160 || buttonView == (CompoundButton) pipeDiameter200 || buttonView == (CompoundButton) pipeDiameter250 || buttonView == (CompoundButton) pipeDiameterOther) {
			pipeDiameter20.setChecked(false);
			pipeDiameter25.setChecked(false);
			pipeDiameter32.setChecked(false);
			pipeDiameter40.setChecked(false);
			pipeDiameter50.setChecked(false);
			pipeDiameter75.setChecked(false);
			pipeDiameter90.setChecked(false);
			pipeDiameter110.setChecked(false);
			pipeDiameter160.setChecked(false);
			pipeDiameter200.setChecked(false);
			pipeDiameter250.setChecked(false);
			pipeDiameterOther.setChecked(false);
			pipeDiameter = 0;
			otherDiameter.setVisibility(View.GONE);
			otherDiameterLabel.setVisibility(View.GONE);
			if (buttonView == (CompoundButton) pipeDiameter20 && isChecked) {
				pipeDiameter20.setOnCheckedChangeListener(null);
				pipeDiameter20.setChecked(true);
				pipeDiameter20.setOnCheckedChangeListener(this);
				pipeDiameter = 20;
			} else if (buttonView == (CompoundButton) pipeDiameter25 && isChecked) {
				pipeDiameter25.setOnCheckedChangeListener(null);
				pipeDiameter25.setChecked(true);
				pipeDiameter25.setOnCheckedChangeListener(this);
				pipeDiameter = 25;
			} else if (buttonView == (CompoundButton) pipeDiameter32 && isChecked) {
				pipeDiameter32.setOnCheckedChangeListener(null);
				pipeDiameter32.setChecked(true);
				pipeDiameter32.setOnCheckedChangeListener(this);
				pipeDiameter = 32;
			} else if (buttonView == (CompoundButton) pipeDiameter40 && isChecked) {
				pipeDiameter40.setOnCheckedChangeListener(null);
				pipeDiameter40.setChecked(true);
				pipeDiameter40.setOnCheckedChangeListener(this);
				pipeDiameter = 40;
			} else if (buttonView == (CompoundButton) pipeDiameter50 && isChecked) {
				pipeDiameter50.setOnCheckedChangeListener(null);
				pipeDiameter50.setChecked(true);
				pipeDiameter50.setOnCheckedChangeListener(this);
				pipeDiameter = 50;
			} else if (buttonView == (CompoundButton) pipeDiameter75 && isChecked) {
				pipeDiameter75.setOnCheckedChangeListener(null);
				pipeDiameter75.setChecked(true);
				pipeDiameter75.setOnCheckedChangeListener(this);
				pipeDiameter = 75;
			} else if (buttonView == (CompoundButton) pipeDiameter90 && isChecked) {
				pipeDiameter90.setOnCheckedChangeListener(null);
				pipeDiameter90.setChecked(true);
				pipeDiameter90.setOnCheckedChangeListener(this);
				pipeDiameter = 90;
			} else if (buttonView == (CompoundButton) pipeDiameter110 && isChecked) {
				pipeDiameter110.setOnCheckedChangeListener(null);
				pipeDiameter110.setChecked(true);
				pipeDiameter110.setOnCheckedChangeListener(this);
				pipeDiameter = 110;
			} else if (buttonView == (CompoundButton) pipeDiameter160 && isChecked) {
				pipeDiameter160.setOnCheckedChangeListener(null);
				pipeDiameter160.setChecked(true);
				pipeDiameter160.setOnCheckedChangeListener(this);
				pipeDiameter = 160;
			} else if (buttonView == (CompoundButton) pipeDiameter200 && isChecked) {
				pipeDiameter200.setOnCheckedChangeListener(null);
				pipeDiameter200.setChecked(true);
				pipeDiameter200.setOnCheckedChangeListener(this);
				pipeDiameter = 200;
			} else if (buttonView == (CompoundButton) pipeDiameter250 && isChecked) {
				pipeDiameter250.setOnCheckedChangeListener(null);
				pipeDiameter250.setChecked(true);
				pipeDiameter250.setOnCheckedChangeListener(this);
				pipeDiameter = 250;
			} else if (buttonView == (CompoundButton) pipeDiameterOther && isChecked) {
				pipeDiameterOther.setOnCheckedChangeListener(null);
				pipeDiameterOther.setChecked(true);
				pipeDiameterOther.setOnCheckedChangeListener(this);

				otherDiameter.setVisibility(View.VISIBLE);
				otherDiameterLabel.setVisibility(View.VISIBLE);
			}
		} else if (buttonView == (CompoundButton) pipeMaterialSteel || buttonView == (CompoundButton) pipeMaterialHDPE || buttonView == (CompoundButton) pipeMaterialuPVC || buttonView == (CompoundButton) pipeMaterialAC) {
			pipeMaterialSteel.setChecked(false);
			pipeMaterialHDPE.setChecked(false);
			pipeMaterialuPVC.setChecked(false);
			pipeMaterialAC.setChecked(false);
			pipeMaterial = 0;

			if (buttonView == (CompoundButton) pipeMaterialSteel && isChecked) {
				pipeMaterialSteel.setOnCheckedChangeListener(null);
				pipeMaterialSteel.setChecked(true);
				pipeMaterialSteel.setOnCheckedChangeListener(this);
				pipeMaterial = 1;
			} else if (buttonView == (CompoundButton) pipeMaterialHDPE && isChecked) {
				pipeMaterialHDPE.setOnCheckedChangeListener(null);
				pipeMaterialHDPE.setChecked(true);
				pipeMaterialHDPE.setOnCheckedChangeListener(this);
				pipeMaterial = 2;
			} else if (buttonView == (CompoundButton) pipeMaterialuPVC && isChecked) {
				pipeMaterialuPVC.setOnCheckedChangeListener(null);
				pipeMaterialuPVC.setChecked(true);
				pipeMaterialuPVC.setOnCheckedChangeListener(this);
				pipeMaterial = 3;
			} else if (buttonView == (CompoundButton) pipeMaterialAC && isChecked) {
				pipeMaterialAC.setOnCheckedChangeListener(null);
				pipeMaterialAC.setChecked(true);
				pipeMaterialAC.setOnCheckedChangeListener(this);
				pipeMaterial = 4;
			}
		} else if (buttonView == (CompoundButton) typeOfRepairCutOutLength || buttonView == (CompoundButton) typeOfRepairFixBurstPipe) {
			typeOfRepairCutOutLength.setChecked(false);
			typeOfRepairFixBurstPipe.setChecked(false);
			pipeLineCutOutLength = 0;
			cutOutLength.setVisibility(View.GONE);
			cutOutLengthLabel.setVisibility(View.GONE);
			typeOfRepair = 0;

			if (buttonView == (CompoundButton) typeOfRepairCutOutLength && isChecked) {
				typeOfRepairCutOutLength.setOnCheckedChangeListener(null);
				typeOfRepairCutOutLength.setChecked(true);
				typeOfRepairCutOutLength.setOnCheckedChangeListener(this);
				typeOfRepair = 1;
				cutOutLength.setVisibility(View.VISIBLE);
				cutOutLengthLabel.setVisibility(View.VISIBLE);
			} else if (buttonView == (CompoundButton) typeOfRepairFixBurstPipe && isChecked) {
				typeOfRepairFixBurstPipe.setOnCheckedChangeListener(null);
				typeOfRepairFixBurstPipe.setChecked(true);
				typeOfRepairFixBurstPipe.setOnCheckedChangeListener(this);
				typeOfRepair = 2;
			}
		}

	}
}
