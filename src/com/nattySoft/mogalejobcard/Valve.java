package com.nattySoft.mogalejobcard;

import java.util.Calendar;

import org.json.JSONArray;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;

public class Valve extends Fragment implements OnCheckedChangeListener {

	private TimePicker valveOpenTime;

	private CheckBox Left_H;
	private CheckBox right_H;
	private boolean leftSide = false;

	private TimePicker valveClosedTime;

	private EditText streetName;

	private CheckBox repairGland;
	private CheckBox tightenGland;
	private CheckBox replaceLid;
	private CheckBox replaceValve;
	private int valveRepairType;

	private LinearLayout layout2;
	private TimePicker valveOpenTime2;

	private CheckBox Left_H2;
	private CheckBox right_H2;
	private boolean leftSide2 = false;

	private TimePicker valveClosedTime2;

	private EditText streetName2;

	private CheckBox repairGland2;
	private CheckBox tightenGland2;
	private CheckBox replaceLid2;
	private CheckBox replaceValve2;
	private int valveRepairType2;

	private LinearLayout layout3;
	private TimePicker valveOpenTime3;

	private CheckBox Left_H3;
	private CheckBox right_H3;
	private boolean leftSide3 = false;

	private TimePicker valveClosedTime3;

	private EditText streetName3;

	private CheckBox repairGland3;
	private CheckBox tightenGland3;
	private CheckBox replaceLid3;
	private CheckBox replaceValve3;
	private int valveRepairType3;

	private LinearLayout layout4;
	private TimePicker valveOpenTime4;

	private CheckBox Left_H4;
	private CheckBox right_H4;
	private boolean leftSide4 = false;

	private TimePicker valveClosedTime4;

	private EditText streetName4;

	private CheckBox repairGland4;
	private CheckBox tightenGland4;
	private CheckBox replaceLid4;
	private CheckBox replaceValve4;
	private int valveRepairType4;

	private LinearLayout layout5;
	private TimePicker valveOpenTime5;

	private CheckBox Left_H5;
	private CheckBox right_H5;
	private boolean leftSide5 = false;

	private TimePicker valveClosedTime5;

	private EditText streetName5;

	private CheckBox repairGland5;
	private CheckBox tightenGland5;
	private CheckBox replaceLid5;
	private CheckBox replaceValve5;
	private int valveRepairType5;

	private LinearLayout layout6;
	private TimePicker valveOpenTime6;

	private CheckBox Left_H6;
	private CheckBox right_H6;
	private boolean leftSide6 = false;

	private TimePicker valveClosedTime6;

	private EditText streetName6;

	private CheckBox repairGland6;
	private CheckBox tightenGland6;
	private CheckBox replaceLid6;
	private CheckBox replaceValve6;
	private int valveRepairType6;

	private Button addMore;
	private Button remove;

	View view;

	private int valveCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.valve, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		valveOpenTime = (TimePicker) view.findViewById(R.id.opened_time);
		valveOpenTime.setIs24HourView(true);
		valveClosedTime = (TimePicker) view.findViewById(R.id.closed_time);
		valveClosedTime.setIs24HourView(true);
		Left_H = (CheckBox) view.findViewById(R.id.l_h);
		Left_H.setOnCheckedChangeListener(this);
		right_H = (CheckBox) view.findViewById(R.id.r_h);
		Left_H.setChecked(true);
		right_H.setOnCheckedChangeListener(this);
		streetName = (EditText) view.findViewById(R.id.street);
		repairGland = (CheckBox) view.findViewById(R.id.repair_repair_gland);
		repairGland.setOnCheckedChangeListener(this);
		tightenGland = (CheckBox) view.findViewById(R.id.tighten_gland);
		tightenGland.setOnCheckedChangeListener(this);
		replaceLid = (CheckBox) view.findViewById(R.id.replace_valve_lid);
		replaceLid.setOnCheckedChangeListener(this);
		replaceValve = (CheckBox) view.findViewById(R.id.replace_valve);
		replaceValve.setOnCheckedChangeListener(this);

		layout2 = (LinearLayout) view.findViewById(R.id.valve2);
		valveOpenTime2 = (TimePicker) view.findViewById(R.id.opened_time2);
		valveOpenTime2.setIs24HourView(true);
		valveClosedTime2 = (TimePicker) view.findViewById(R.id.closed_time2);
		valveClosedTime2.setIs24HourView(true);
		Left_H2 = (CheckBox) view.findViewById(R.id.l_h2);
		Left_H2.setOnCheckedChangeListener(this);
		right_H2 = (CheckBox) view.findViewById(R.id.r_h2);
		Left_H2.setChecked(true);
		right_H2.setOnCheckedChangeListener(this);
		streetName2 = (EditText) view.findViewById(R.id.street2);
		repairGland2 = (CheckBox) view.findViewById(R.id.repair_repair_gland2);
		repairGland2.setOnCheckedChangeListener(this);
		tightenGland2 = (CheckBox) view.findViewById(R.id.tighten_gland2);
		tightenGland2.setOnCheckedChangeListener(this);
		replaceLid2 = (CheckBox) view.findViewById(R.id.replace_valve_lid2);
		replaceLid2.setOnCheckedChangeListener(this);
		replaceValve2 = (CheckBox) view.findViewById(R.id.replace_valve2);
		replaceValve2.setOnCheckedChangeListener(this);

		layout3 = (LinearLayout) view.findViewById(R.id.valve3);
		valveOpenTime3 = (TimePicker) view.findViewById(R.id.opened_time3);
		valveOpenTime3.setIs24HourView(true);
		valveClosedTime3 = (TimePicker) view.findViewById(R.id.closed_time3);
		valveClosedTime3.setIs24HourView(true);
		Left_H3 = (CheckBox) view.findViewById(R.id.l_h3);
		Left_H3.setOnCheckedChangeListener(this);
		right_H3 = (CheckBox) view.findViewById(R.id.r_h3);
		Left_H3.setChecked(true);
		right_H3.setOnCheckedChangeListener(this);
		streetName3 = (EditText) view.findViewById(R.id.street3);
		repairGland3 = (CheckBox) view.findViewById(R.id.repair_repair_gland3);
		repairGland3.setOnCheckedChangeListener(this);
		tightenGland3 = (CheckBox) view.findViewById(R.id.tighten_gland3);
		tightenGland3.setOnCheckedChangeListener(this);
		replaceLid3 = (CheckBox) view.findViewById(R.id.replace_valve_lid3);
		replaceLid3.setOnCheckedChangeListener(this);
		replaceValve3 = (CheckBox) view.findViewById(R.id.replace_valve3);
		replaceValve3.setOnCheckedChangeListener(this);

		layout4 = (LinearLayout) view.findViewById(R.id.valve4);
		valveOpenTime4 = (TimePicker) view.findViewById(R.id.opened_time4);
		valveOpenTime4.setIs24HourView(true);
		valveClosedTime4 = (TimePicker) view.findViewById(R.id.closed_time4);
		valveClosedTime4.setIs24HourView(true);
		Left_H4 = (CheckBox) view.findViewById(R.id.l_h4);
		Left_H4.setOnCheckedChangeListener(this);
		right_H4 = (CheckBox) view.findViewById(R.id.r_h4);
		Left_H4.setChecked(true);
		right_H4.setOnCheckedChangeListener(this);
		streetName4 = (EditText) view.findViewById(R.id.street4);
		repairGland4 = (CheckBox) view.findViewById(R.id.repair_repair_gland4);
		repairGland4.setOnCheckedChangeListener(this);
		tightenGland4 = (CheckBox) view.findViewById(R.id.tighten_gland4);
		tightenGland4.setOnCheckedChangeListener(this);
		replaceLid4 = (CheckBox) view.findViewById(R.id.replace_valve_lid4);
		replaceLid4.setOnCheckedChangeListener(this);
		replaceValve4 = (CheckBox) view.findViewById(R.id.replace_valve4);
		replaceValve4.setOnCheckedChangeListener(this);

		layout5 = (LinearLayout) view.findViewById(R.id.valve5);
		valveOpenTime5 = (TimePicker) view.findViewById(R.id.opened_time5);
		valveOpenTime5.setIs24HourView(true);
		valveClosedTime5 = (TimePicker) view.findViewById(R.id.closed_time5);
		valveClosedTime5.setIs24HourView(true);
		Left_H5 = (CheckBox) view.findViewById(R.id.l_h5);
		Left_H5.setOnCheckedChangeListener(this);
		right_H5 = (CheckBox) view.findViewById(R.id.r_h5);
		Left_H5.setChecked(true);
		right_H5.setOnCheckedChangeListener(this);
		streetName5 = (EditText) view.findViewById(R.id.street5);
		repairGland5 = (CheckBox) view.findViewById(R.id.repair_repair_gland5);
		repairGland5.setOnCheckedChangeListener(this);
		tightenGland5 = (CheckBox) view.findViewById(R.id.tighten_gland5);
		tightenGland5.setOnCheckedChangeListener(this);
		replaceLid5 = (CheckBox) view.findViewById(R.id.replace_valve_lid5);
		replaceLid5.setOnCheckedChangeListener(this);
		replaceValve5 = (CheckBox) view.findViewById(R.id.replace_valve5);
		replaceValve5.setOnCheckedChangeListener(this);

		layout6 = (LinearLayout) view.findViewById(R.id.valve6);
		valveOpenTime6 = (TimePicker) view.findViewById(R.id.opened_time6);
		valveOpenTime6.setIs24HourView(true);
		valveClosedTime6 = (TimePicker) view.findViewById(R.id.closed_time6);
		valveClosedTime6.setIs24HourView(true);
		Left_H6 = (CheckBox) view.findViewById(R.id.l_h6);
		Left_H6.setOnCheckedChangeListener(this);
		right_H6 = (CheckBox) view.findViewById(R.id.r_h6);
		Left_H6.setChecked(true);
		right_H6.setOnCheckedChangeListener(this);
		streetName6 = (EditText) view.findViewById(R.id.street6);
		repairGland6 = (CheckBox) view.findViewById(R.id.repair_repair_gland6);
		repairGland6.setOnCheckedChangeListener(this);
		tightenGland6 = (CheckBox) view.findViewById(R.id.tighten_gland6);
		tightenGland6.setOnCheckedChangeListener(this);
		replaceLid6 = (CheckBox) view.findViewById(R.id.replace_valve_lid6);
		replaceLid6.setOnCheckedChangeListener(this);
		replaceValve6 = (CheckBox) view.findViewById(R.id.replace_valve6);
		replaceValve6.setOnCheckedChangeListener(this);

		valveCount = 1;

		addMore = (Button) view.findViewById(R.id.add_more_valve);
		addMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addAValve();
			}
		});

		remove = (Button) view.findViewById(R.id.remove_valve);
		remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeValve();
			}
		});

	}

	protected void removeValve() {
		switch (valveCount) {
		case 2:
			layout2.setVisibility(View.GONE);
			valveCount--;
			remove.setVisibility(View.GONE);
			break;
		case 3:
			layout3.setVisibility(View.GONE);
			valveCount--;
			break;
		case 4:
			layout4.setVisibility(View.GONE);
			valveCount--;
			break;
		case 5:
			layout5.setVisibility(View.GONE);
			valveCount--;
			break;
		case 6:
			layout6.setVisibility(View.GONE);
			valveCount--;
			break;

		default:
			break;
		}
	}

	protected void addAValve() {
		remove.setVisibility(View.VISIBLE);
		switch (valveCount) {
		case 1:
			layout2.setVisibility(View.VISIBLE);
			valveCount++;
			break;
		case 2:
			layout3.setVisibility(View.VISIBLE);
			valveCount++;
			break;
		case 3:
			layout4.setVisibility(View.VISIBLE);
			valveCount++;
			break;
		case 4:
			layout5.setVisibility(View.VISIBLE);
			valveCount++;
			break;
		case 5:
			layout6.setVisibility(View.VISIBLE);
			valveCount++;
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		String jsonStr = Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID);
		try {
			if (jsonStr != null) {
				JSONObject jsonObj = new JSONObject(jsonStr);

				JSONArray jsonArr = jsonObj.getJSONArray("valves");
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject json = jsonArr.getJSONObject(i);
					switch (i) {
					case 0:
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
						valveOpenTime.setCurrentMinute(calendar.get(Calendar.MINUTE));

						Calendar calendar2 = Calendar.getInstance();
						calendar2.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime.setCurrentHour(calendar2.get(Calendar.HOUR_OF_DAY));
						valveClosedTime.setCurrentMinute(calendar2.get(Calendar.MINUTE));

						leftSide = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide) {
							right_H.setChecked(true);
						} else {
							Left_H.setChecked(true);
						}

						streetName.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland.setOnCheckedChangeListener(null);
							repairGland.setChecked(true);
							repairGland.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid.setOnCheckedChangeListener(null);
							replaceLid.setChecked(true);
							replaceLid.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland.setOnCheckedChangeListener(null);
							tightenGland.setChecked(true);
							tightenGland.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve.setOnCheckedChangeListener(null);
							replaceValve.setChecked(true);
							replaceValve.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;
					case 1:
						remove.setVisibility(View.VISIBLE);
						layout2.setVisibility(View.VISIBLE);
						Calendar opencal2 = Calendar.getInstance();
						opencal2.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime2.setCurrentHour(opencal2.get(Calendar.HOUR_OF_DAY));
						valveOpenTime2.setCurrentMinute(opencal2.get(Calendar.MINUTE));

						Calendar closecal2 = Calendar.getInstance();
						closecal2.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime2.setCurrentHour(closecal2.get(Calendar.HOUR_OF_DAY));
						valveClosedTime2.setCurrentMinute(closecal2.get(Calendar.MINUTE));

						leftSide2 = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide2) {
							right_H2.setChecked(true);
						} else {
							Left_H2.setChecked(true);
						}

						streetName2.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland2.setOnCheckedChangeListener(null);
							repairGland2.setChecked(true);
							repairGland2.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid2.setOnCheckedChangeListener(null);
							replaceLid2.setChecked(true);
							replaceLid2.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland2.setOnCheckedChangeListener(null);
							tightenGland2.setChecked(true);
							tightenGland2.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve2.setOnCheckedChangeListener(null);
							replaceValve2.setChecked(true);
							replaceValve2.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;
					case 2:
						layout3.setVisibility(View.VISIBLE);
						Calendar opencal3 = Calendar.getInstance();
						opencal3.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime3.setCurrentHour(opencal3.get(Calendar.HOUR_OF_DAY));
						valveOpenTime3.setCurrentMinute(opencal3.get(Calendar.MINUTE));

						Calendar closecal3 = Calendar.getInstance();
						closecal3.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime3.setCurrentHour(closecal3.get(Calendar.HOUR_OF_DAY));
						valveClosedTime3.setCurrentMinute(closecal3.get(Calendar.MINUTE));

						leftSide3 = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide3) {
							right_H3.setChecked(true);
						} else {
							Left_H3.setChecked(true);
						}

						streetName3.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland3.setOnCheckedChangeListener(null);
							repairGland3.setChecked(true);
							repairGland3.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid3.setOnCheckedChangeListener(null);
							replaceLid3.setChecked(true);
							replaceLid3.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland3.setOnCheckedChangeListener(null);
							tightenGland3.setChecked(true);
							tightenGland3.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve3.setOnCheckedChangeListener(null);
							replaceValve3.setChecked(true);
							replaceValve3.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;
						
					case 3:
						layout4.setVisibility(View.VISIBLE);
						Calendar opencal4 = Calendar.getInstance();
						opencal4.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime4.setCurrentHour(opencal4.get(Calendar.HOUR_OF_DAY));
						valveOpenTime4.setCurrentMinute(opencal4.get(Calendar.MINUTE));

						Calendar closecal4 = Calendar.getInstance();
						closecal4.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime4.setCurrentHour(closecal4.get(Calendar.HOUR_OF_DAY));
						valveClosedTime4.setCurrentMinute(closecal4.get(Calendar.MINUTE));

						leftSide4 = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide4) {
							right_H4.setChecked(true);
						} else {
							Left_H4.setChecked(true);
						}

						streetName4.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland4.setOnCheckedChangeListener(null);
							repairGland4.setChecked(true);
							repairGland4.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid4.setOnCheckedChangeListener(null);
							replaceLid4.setChecked(true);
							replaceLid4.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland4.setOnCheckedChangeListener(null);
							tightenGland4.setChecked(true);
							tightenGland4.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve4.setOnCheckedChangeListener(null);
							replaceValve4.setChecked(true);
							replaceValve4.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;
					case 4:
						layout5.setVisibility(View.VISIBLE);
						Calendar opencal5 = Calendar.getInstance();
						opencal5.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime5.setCurrentHour(opencal5.get(Calendar.HOUR_OF_DAY));
						valveOpenTime5.setCurrentMinute(opencal5.get(Calendar.MINUTE));

						Calendar closecal5 = Calendar.getInstance();
						closecal5.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime5.setCurrentHour(closecal5.get(Calendar.HOUR_OF_DAY));
						valveClosedTime5.setCurrentMinute(closecal5.get(Calendar.MINUTE));

						leftSide5 = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide5) {
							right_H5.setChecked(true);
						} else {
							Left_H5.setChecked(true);
						}

						streetName5.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland5.setOnCheckedChangeListener(null);
							repairGland5.setChecked(true);
							repairGland5.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid5.setOnCheckedChangeListener(null);
							replaceLid5.setChecked(true);
							replaceLid5.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland5.setOnCheckedChangeListener(null);
							tightenGland5.setChecked(true);
							tightenGland5.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve5.setOnCheckedChangeListener(null);
							replaceValve5.setChecked(true);
							replaceValve5.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;
					case 5:
						layout6.setVisibility(View.VISIBLE);
						Calendar opencal6 = Calendar.getInstance();
						opencal6.setTimeInMillis(Long.parseLong(json.getString("openTime")));
						valveOpenTime6.setCurrentHour(opencal6.get(Calendar.HOUR_OF_DAY));
						valveOpenTime6.setCurrentMinute(opencal6.get(Calendar.MINUTE));

						Calendar closecal6 = Calendar.getInstance();
						closecal6.setTimeInMillis(Long.parseLong(json.getString("closeTime")));
						valveClosedTime6.setCurrentHour(closecal6.get(Calendar.HOUR_OF_DAY));
						valveClosedTime6.setCurrentMinute(closecal6.get(Calendar.MINUTE));

						leftSide6 = Boolean.parseBoolean(json.getString("leftSide"));
						if (!leftSide6) {
							right_H6.setChecked(true);
						} else {
							Left_H6.setChecked(true);
						}

						streetName6.setText(json.getString("streetName"));
						valveRepairType = json.getInt("valveRepairType");
						switch (valveRepairType) {
						case 1:
							repairGland6.setOnCheckedChangeListener(null);
							repairGland6.setChecked(true);
							repairGland6.setOnCheckedChangeListener(this);
							break;
						case 2:
							replaceLid6.setOnCheckedChangeListener(null);
							replaceLid6.setChecked(true);
							replaceLid6.setOnCheckedChangeListener(this);
							break;
						case 3:
							tightenGland6.setOnCheckedChangeListener(null);
							tightenGland6.setChecked(true);
							tightenGland6.setOnCheckedChangeListener(this);
							break;
						case 4:
							replaceValve6.setOnCheckedChangeListener(null);
							replaceValve6.setChecked(true);
							replaceValve6.setOnCheckedChangeListener(this);
							break;

						default:
							break;
						}
						break;

					default:
						break;
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	public void saveForm() {
		JSONArray jsonArray = new JSONArray();

		JSONObject json = new JSONObject();
		try {
			valveOpenTime.clearFocus();
			Calendar calendar = Calendar.getInstance();
			// calendar.set(0, 0, 0, valveOpenTime.getCurrentHour(),
			// valveOpenTime.getCurrentMinute());
			calendar.set(Calendar.HOUR_OF_DAY, valveOpenTime.getCurrentHour());
			calendar.set(Calendar.MINUTE, valveOpenTime.getCurrentMinute());
			long time = calendar.getTimeInMillis();
			json.accumulate("openTime", time);

			valveClosedTime.clearFocus();
			Calendar calendar2 = Calendar.getInstance();
			// calendar2.set(0, 0, 0, valveClosedTime.getCurrentHour(),
			// valveClosedTime.getCurrentMinute());
			calendar2.set(Calendar.HOUR_OF_DAY, valveClosedTime.getCurrentHour());
			calendar2.set(Calendar.MINUTE, valveClosedTime.getCurrentMinute());
			long time2 = calendar2.getTimeInMillis();
			json.accumulate("closeTime", time2);

			json.accumulate("leftSide", leftSide);

			json.accumulate("streetName", streetName.getText().toString());
			json.accumulate("valveRepairType", valveRepairType);
			jsonArray.put(json);

			switch (valveCount) {
			case 6:
				JSONObject json6 = new JSONObject();
				valveOpenTime6.clearFocus();
				Calendar openTimecal6 = Calendar.getInstance();
				openTimecal6.set(Calendar.HOUR_OF_DAY, valveOpenTime6.getCurrentHour());
				openTimecal6.set(Calendar.MINUTE, valveOpenTime6.getCurrentMinute());
				long opentim6 = openTimecal6.getTimeInMillis();
				json6.accumulate("openTime", opentim6);

				valveClosedTime6.clearFocus();
				Calendar clidesTimecal6 = Calendar.getInstance();
				// calendar6.set(0, 0, 0, valveClosedTime.getCurrentHour(),
				// valveClosedTime.getCurrentMinute());
				clidesTimecal6.set(Calendar.HOUR_OF_DAY, valveClosedTime6.getCurrentHour());
				clidesTimecal6.set(Calendar.MINUTE, valveClosedTime6.getCurrentMinute());
				long closetim6 = clidesTimecal6.getTimeInMillis();
				json6.accumulate("closeTime", closetim6);

				json6.accumulate("leftSide", leftSide6);

				json6.accumulate("streetName", streetName6.getText().toString());
				json6.accumulate("valveRepairType", valveRepairType6);
				jsonArray.put(json6);
			case 5:
				JSONObject json5 = new JSONObject();
				valveOpenTime5.clearFocus();
				Calendar openTimecal5 = Calendar.getInstance();
				openTimecal5.set(Calendar.HOUR_OF_DAY, valveOpenTime5.getCurrentHour());
				openTimecal5.set(Calendar.MINUTE, valveOpenTime5.getCurrentMinute());
				long opentim5 = openTimecal5.getTimeInMillis();
				json5.accumulate("openTime", opentim5);

				valveClosedTime5.clearFocus();
				Calendar clidesTimecal5 = Calendar.getInstance();
				// calendar5.set(0, 0, 0, valveClosedTime.getCurrentHour(),
				// valveClosedTime.getCurrentMinute());
				clidesTimecal5.set(Calendar.HOUR_OF_DAY, valveClosedTime5.getCurrentHour());
				clidesTimecal5.set(Calendar.MINUTE, valveClosedTime5.getCurrentMinute());
				long closetim5 = clidesTimecal5.getTimeInMillis();
				json5.accumulate("closeTime", closetim5);

				json5.accumulate("leftSide", leftSide5);

				json5.accumulate("streetName", streetName5.getText().toString());
				json5.accumulate("valveRepairType", valveRepairType5);
				jsonArray.put(json5);
			case 4:
				JSONObject json4 = new JSONObject();
				valveOpenTime4.clearFocus();
				Calendar openTimecal4 = Calendar.getInstance();
				openTimecal4.set(Calendar.HOUR_OF_DAY, valveOpenTime4.getCurrentHour());
				openTimecal4.set(Calendar.MINUTE, valveOpenTime4.getCurrentMinute());
				long opentim4 = openTimecal4.getTimeInMillis();
				json4.accumulate("openTime", opentim4);

				valveClosedTime4.clearFocus();
				Calendar clidesTimecal4 = Calendar.getInstance();
				// calendar4.set(0, 0, 0, valveClosedTime.getCurrentHour(),
				// valveClosedTime.getCurrentMinute());
				clidesTimecal4.set(Calendar.HOUR_OF_DAY, valveClosedTime4.getCurrentHour());
				clidesTimecal4.set(Calendar.MINUTE, valveClosedTime4.getCurrentMinute());
				long closetim4 = clidesTimecal4.getTimeInMillis();
				json4.accumulate("closeTime", closetim4);

				json4.accumulate("leftSide", leftSide4);

				json4.accumulate("streetName", streetName4.getText().toString());
				json4.accumulate("valveRepairType", valveRepairType4);
				jsonArray.put(json4);
			case 3:
				JSONObject json3 = new JSONObject();
				valveOpenTime3.clearFocus();
				Calendar openTimecal3 = Calendar.getInstance();
				openTimecal3.set(Calendar.HOUR_OF_DAY, valveOpenTime3.getCurrentHour());
				openTimecal3.set(Calendar.MINUTE, valveOpenTime3.getCurrentMinute());
				long opentim3 = openTimecal3.getTimeInMillis();
				json3.accumulate("openTime", opentim3);

				valveClosedTime3.clearFocus();
				Calendar clidesTimecal3 = Calendar.getInstance();
				// calendar3.set(0, 0, 0, valveClosedTime.getCurrentHour(),
				// valveClosedTime.getCurrentMinute());
				clidesTimecal3.set(Calendar.HOUR_OF_DAY, valveClosedTime3.getCurrentHour());
				clidesTimecal3.set(Calendar.MINUTE, valveClosedTime3.getCurrentMinute());
				long closetim3 = clidesTimecal3.getTimeInMillis();
				json3.accumulate("closeTime", closetim3);

				json3.accumulate("leftSide", leftSide3);

				json3.accumulate("streetName", streetName3.getText().toString());
				json3.accumulate("valveRepairType", valveRepairType3);
				jsonArray.put(json3);

			case 2:
				JSONObject json2 = new JSONObject();
				valveOpenTime2.clearFocus();
				Calendar openTimecal2 = Calendar.getInstance();
				openTimecal2.set(Calendar.HOUR_OF_DAY, valveOpenTime2.getCurrentHour());
				openTimecal2.set(Calendar.MINUTE, valveOpenTime2.getCurrentMinute());
				long opentim2 = openTimecal2.getTimeInMillis();
				json2.accumulate("openTime", opentim2);

				valveClosedTime2.clearFocus();
				Calendar clidesTimecal2 = Calendar.getInstance();
				// calendar2.set(0, 0, 0, valveClosedTime.getCurrentHour(),
				// valveClosedTime.getCurrentMinute());
				clidesTimecal2.set(Calendar.HOUR_OF_DAY, valveClosedTime2.getCurrentHour());
				clidesTimecal2.set(Calendar.MINUTE, valveClosedTime2.getCurrentMinute());
				long closetim2 = clidesTimecal2.getTimeInMillis();
				json2.accumulate("closeTime", closetim2);

				json2.accumulate("leftSide", leftSide2);

				json2.accumulate("streetName", streetName2.getText().toString());
				json2.accumulate("valveRepairType", valveRepairType2);
				jsonArray.put(json2);

				break;

			default:
				break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject valvesObj = new JSONObject();
		try {
			valvesObj.put("valves", jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Preferences.savePreference(getActivity(), AppConstants.PreferenceKeys.KEY_VALVE + FragmentIncident.incidentID, valvesObj.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == (CompoundButton) Left_H && isChecked) {
			right_H.setOnCheckedChangeListener(null);
			right_H.setChecked(false);
			right_H.setOnCheckedChangeListener(this);
			leftSide = true;
		} else if (buttonView == (CompoundButton) right_H && isChecked) {
			Left_H.setOnCheckedChangeListener(null);
			Left_H.setChecked(false);
			Left_H.setOnCheckedChangeListener(this);
			leftSide = false;
		} else if (buttonView == (CompoundButton) Left_H || buttonView == (CompoundButton) right_H) {
			Left_H.setOnCheckedChangeListener(null);
			Left_H.setChecked(true);
			Left_H.setOnCheckedChangeListener(this);
			leftSide = true;
		} else if (buttonView == (CompoundButton) repairGland || buttonView == (CompoundButton) replaceLid || buttonView == (CompoundButton) tightenGland || buttonView == (CompoundButton) replaceValve) {
			repairGland.setChecked(false);
			replaceLid.setChecked(false);
			tightenGland.setChecked(false);
			replaceValve.setChecked(false);
			valveRepairType = 0;

			if (buttonView == (CompoundButton) repairGland && isChecked) {
				repairGland.setOnCheckedChangeListener(null);
				repairGland.setChecked(true);
				repairGland.setOnCheckedChangeListener(this);
				valveRepairType = 1;
			} else if (buttonView == (CompoundButton) replaceLid && isChecked) {
				replaceLid.setOnCheckedChangeListener(null);
				replaceLid.setChecked(true);
				replaceLid.setOnCheckedChangeListener(this);
				valveRepairType = 2;
			} else if (buttonView == (CompoundButton) tightenGland && isChecked) {
				tightenGland.setOnCheckedChangeListener(null);
				tightenGland.setChecked(true);
				tightenGland.setOnCheckedChangeListener(this);
				valveRepairType = 3;
			} else if (buttonView == (CompoundButton) replaceValve && isChecked) {
				replaceValve.setOnCheckedChangeListener(null);
				replaceValve.setChecked(true);
				replaceValve.setOnCheckedChangeListener(this);
				valveRepairType = 4;
			}
		}

	}
}
