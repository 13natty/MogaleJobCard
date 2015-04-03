package com.nattySoft.mogalejobcard;

import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.util.Preferences;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentJobCard extends Fragment {
	private Button incidentButton;
	private Button updateButton;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_job_card, container, false);

		// Here we turn your string.xml in an array
		String[] myKeys = getResources().getStringArray(R.array.sections);
		return view;

		// TextView myTextView = (TextView) findViewById(R.id.job_textview);
		// myTextView.setText(myKeys[position]);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		incidentButton = (Button) view.findViewById(R.id.button_incident);
		incidentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		updateButton = (Button) view.findViewById(R.id.button_update);
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// open popup
				final CharSequence options[] = new CharSequence[] { "Complete", "None Issue", "Pending" };

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Update this incident");
				builder.setItems(options, new DialogInterface.OnClickListener() {
					private AlertDialog levelDialog;

					@Override
					public void onClick(DialogInterface dialog, final int pos) {
						final CharSequence optionsStrings[] = new CharSequence[] { "complete", "none_issue", "pending" };
//						AlertDialog levelDialog;
						// Strings to Show In Dialog with Radio Buttons
						
						final CharSequence[] items = { " Water Tower ", " Water Reservior ", " Water Pipe Burst ", " Water Pump ", " No Water ", " Valve ", " Prepaid ", " Meter Leak - Stop Cock "
								, " Meter Leak - Joint Leak ", " Meter Faulty - Stopped ", " Meter Faulty - Erratic Reading ", " Meter Faulty - Unreadable Screen ", " Meter Faulty - High Consumption "};

						// Creating and Building the Dialog
						AlertDialog.Builder builderType = new AlertDialog.Builder(FragmentJobCard.this.getActivity());
						builderType.setTitle("Select incident type");
						builderType.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int item) {
								int incidentType = 0;

								switch (item) {
								case 0:
									incidentType = 1;
									break;
								case 1:
									incidentType = 2;
									break;
								case 2:
									incidentType = 3;
									break;
								case 3:
									incidentType = 4;
									break;
								case 4:
									incidentType = 5;
									break;
								case 5:
									incidentType = 8;
									break;
								case 6:
									incidentType = 9;
									break;
								case 7:
									incidentType = 10;
									break;
								case 8:
									incidentType = 11;
									break;
								case 9:
									incidentType = 12;
									break;
								case 10:
									incidentType = 13;
									break;
								case 11:
									incidentType = 14;
									break;
								case 12:
									incidentType = 15;
									break;

								}
								levelDialog.dismiss();
								MainActivity.incidentStatus = (String) optionsStrings[pos];
								MainActivity.action = Action.SAVE_JOB_CARD;
								AppConstants.Config.KEY_JOB_TIME = System.currentTimeMillis() - AppConstants.Config.KEY_START_TIME;
								CommunicationHandler.saveJobCard(FragmentJobCard.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Saving Incident..."), Preferences.getPreference(FragmentJobCard.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), FragmentIncident.incidentID, ((String) optionsStrings[pos]).toLowerCase(), incidentType);
							}
						});
						levelDialog = builderType.create();
						levelDialog.show();

					}
				});
				builder.show();
			}
		});

		ListView lv = (ListView) view.findViewById(R.id.job_list_view);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				TextView item = (TextView) view;
				Log.d("TAG ", "pos " + position);
				switch (position) {
				case 0:
					Log.d("TAG ", "pos " + position);
					Log.d("TAG ", "view " + item.getText());
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
					Fragment fragment = new ExistingMeterInformation();
					FragmentManager frgManager = getFragmentManager();
					frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
					break;
				case 1:
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
					Fragment fragment1 = new NewMeterInformation();
					FragmentManager frgManager1 = getFragmentManager();
					frgManager1.beginTransaction().replace(R.id.content_frame, fragment1).commit();
					break;
				case 2:
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
					Fragment fragment2 = new NewConnectionInfo();
					FragmentManager frgManager2 = getFragmentManager();
					frgManager2.beginTransaction().replace(R.id.content_frame, fragment2).commit();
					break;
				case 3:
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
					Log.d("TAG ", "must open pipeline ");
					Fragment fragment3 = new PipeLineInfo();
					Log.d("TAG ", "pipeline openned");
					FragmentManager frgManager3 = getFragmentManager();
					frgManager3.beginTransaction().replace(R.id.content_frame, fragment3).commit();
					Log.d("TAG ", "switched to pipeline");
					break;
				case 4:
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
					Fragment fragment4 = new Hydrant();
					FragmentManager frgManager4 = getFragmentManager();
					frgManager4.beginTransaction().replace(R.id.content_frame, fragment4).commit();
					break;
				case 5:
					((MainActivity) getActivity()).prevFrag.add(getFragmentManager().findFragmentById(R.id.content_frame));
//					Fragment fragment5 = new Valve();
//					FragmentManager frgManager5 = getFragmentManager();
//					frgManager5.beginTransaction().replace(R.id.content_frame, fragment5).commit();
					Fragment fragment5 = new Valve();
					FragmentManager frgManager5 = getFragmentManager();
					frgManager5.beginTransaction().replace(R.id.content_frame, fragment5).commit();
					break;

				default:
					Log.d("TAG ", "view " + item.getText());
					break;
				}
				getActivity().setTitle(item.getText());
			}
		});
	}
}
