package com.nattySoft.mogalejobcard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class IncidentLogs extends RelativeLayout {

	private Context context;

	public IncidentLogs(Context context) {
		super(context);
		this.context = context;
	}
	
	public RelativeLayout getView(Activity activity, LayoutInflater inflater)
	{
		return (RelativeLayout )inflater.inflate(R.layout.log_item, null, false);
	}

}