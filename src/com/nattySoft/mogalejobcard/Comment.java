package com.nattySoft.mogalejobcard;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Comment extends LinearLayout {

	private Context context;

	public Comment(Context context) {
		super(context);
		this.context = context;
	}
	
	public LinearLayout getView(Activity activity, LayoutInflater inflater)
	{
		return (LinearLayout )inflater.inflate(R.layout.single_comment, null, false);
	}

}
