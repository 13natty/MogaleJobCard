package com.nattySoft.mogalejobcard;

import java.util.Calendar;

import com.nattySoft.mogalejobcard.listener.ChatResponceListener;
import com.nattySoft.mogalejobcard.listener.RequestResponseListener;
import com.nattySoft.mogalejobcard.net.CommunicationHandler;
import com.nattySoft.mogalejobcard.net.CommunicationHandler.Action;
import com.nattySoft.mogalejobcard.util.Preferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity implements RequestResponseListener, ChatResponceListener{
    
    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;

    Intent intent;
    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.chat_layout);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listView1);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.single_chat_message);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chatText);
//        chatText.setOnKeyListener(new OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    return sendChatMessage();
//                }
//                return false;
//            }
//        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	MainActivity.action = Action.SEND_CHAT;
    			CommunicationHandler.sendChat(ChatActivity.this, ChatActivity.this, ProgressDialog.show(ChatActivity.this, "Please wait", "Sending Message..."), chatText.getText().toString(), Preferences.getPreference(ChatActivity.this, AppConstants.PreferenceKeys.KEY_EMPLOYEE_NUM));
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage(){
    	Calendar c = Calendar.getInstance(); 
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minutes = c.get(Calendar.MINUTE);
    	
        chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString(), hour+":"+minutes, "me"));
        chatText.setText("");
        side = !side;
        return true;
    }
    
    public boolean sendChatMessage(String sender, String message, String time){
        chatArrayAdapter.add(new ChatMessage(false, message, time, sender));
        chatText.setText("");
        side = !side;
        return true;
    }

	@Override
	public void hasResponse(String response) {
		Log.d(TAG, "Chat Reply "+response);
		
	}

	@Override
	public void hasResponse(String message, String sender, String time) {
		sendChatMessage(sender, message, time);
	}

}