package com.paphus.sdk.activity.actions;

import com.paphus.brainbot.R;
import com.paphus.sdk.activity.MainActivity;
import com.paphus.sdk.config.WebMediumConfig;

import android.app.Activity;
import android.view.View;

public class HttpFlagAction extends HttpUIAction {
	WebMediumConfig config;

	public HttpFlagAction(Activity activity, WebMediumConfig config) {
		super(activity);
		this.config = config;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
		MainActivity.connection.flag(this.config);
		} catch (Exception exception) {
			this.exception = exception;
		}
		return "";
	}

	@Override
	public void onPostExecute(String xml) {
		super.onPostExecute(xml);
		if (this.exception != null) {
			return;
		}
	    MainActivity.instance.isFlagged = true;
	    this.activity.findViewById(R.id.flaggedLabel).setVisibility(View.VISIBLE);
	}
	
}