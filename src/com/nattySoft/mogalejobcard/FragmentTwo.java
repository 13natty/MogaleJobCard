package com.nattySoft.mogalejobcard;

import java.util.Calendar;

import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTwo extends Fragment {

	ImageView ivIcon;
	TextView tvItemName;

	public static ChatArrayAdapter chatArrayAdapter;
	private ListView listView;
	private EditText chatText;
	private EditText timeText;
	private EditText senderText;
	private Button buttonSend;

	Intent intent;
	private boolean side = false;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";

	View view;

	public FragmentTwo() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.chat_layout, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ivIcon = (ImageView) view.findViewById(R.id.frag2_icon);
		tvItemName = (TextView) view.findViewById(R.id.frag2_text);

		tvItemName.setText(getArguments().getString(ITEM_NAME));
		ivIcon.setImageDrawable(view.getResources().getDrawable(getArguments().getInt(IMAGE_RESOURCE_ID)));

		buttonSend = (Button) view.findViewById(R.id.buttonSend);

		listView = (ListView) view.findViewById(R.id.listView1);

		if (chatArrayAdapter == null)
			chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.single_chat_message);
		listView.setAdapter(chatArrayAdapter);

		chatText = (EditText) view.findViewById(R.id.chatText);
		timeText = (EditText) view.findViewById(R.id.messegeTime);
		senderText = (EditText) view.findViewById(R.id.owner);
		// chatText.setOnKeyListener(new OnKeyListener() {
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
		// KeyEvent.KEYCODE_ENTER)) {
		// return sendChatMessage();
		// }
		// return false;
		// }
		// });
		buttonSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.action = Action.SEND_CHAT;
				CommunicationHandler.sendChat(getActivity().getApplicationContext(), (MainActivity) getActivity(), ProgressDialog.show(getActivity(), "Please wait", "Sending Message..."), chatText.getText().toString(), Preferences.getPreference(getActivity(), AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
				sendChatMessage();
			}
		});

		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setAdapter(chatArrayAdapter);

		// to scroll the list view to bottom on data change
		chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				listView.setSelection(chatArrayAdapter.getCount() - 1);
			}
		});

	}

	private boolean sendChatMessage() {
		Calendar c = Calendar.getInstance(); 
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minutes = c.get(Calendar.MINUTE);
    	
		chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString(), hour+":"+minutes, "Me"));
		chatText.setText("");
		side = !side;
		return true;
	}

	public boolean sendChatMessage(String sender, String message, String time) {
		chatArrayAdapter.add(new ChatMessage(false, message, time, sender));
		chatText.setText("");
		side = !side;
		return true;
	}

}