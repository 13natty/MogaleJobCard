package com.nattySoft.mogalejobcard;

import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.util.Preferences;

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
				final CharSequence options[] = new CharSequence[] { "Complete", "Awaiting parts", "None Issue", "Pending" };

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Update this incident");
				builder.setItems(options, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int pos) {
						MainActivity.incidentStatus = (String) options[pos];
						MainActivity.action = Action.SAVE_JOB_CARD;
						CommunicationHandler.saveJobCard(FragmentJobCard.this.getActivity(), (RequestResponseListener) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Saving Incident..."), Preferences.getPreference(FragmentJobCard.this.getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM), FragmentIncident.incidentID);
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
					Fragment fragment2 = new ConnectionInfo();
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
