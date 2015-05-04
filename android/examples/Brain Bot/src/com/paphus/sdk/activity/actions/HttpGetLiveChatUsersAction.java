package com.paphus.sdk.activity.actions;

import java.util.List;

import android.app.Activity;

import com.paphus.sdk.activity.MainActivity;
import com.paphus.sdk.activity.livechat.LiveChatActivity;
import com.paphus.sdk.config.UserConfig;

public class HttpGetLiveChatUsersAction extends HttpAction {
	String csv;
	List<UserConfig> users;

	public HttpGetLiveChatUsersAction(Activity activity, String csv) {
		super(activity);
		this.csv = csv;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
		this.users = MainActivity.connection.getUsers(this.csv);
		} catch (Exception exception) {
			this.exception = exception;
		}
		return "";
	}

	@Override
	protected void onPostExecute(String xml) {
		super.onPostExecute(xml);
		if (this.exception != null) {
			return;
		}
		try {
			((LiveChatActivity)this.activity).setUsers(this.users);
	
		} catch (Exception error) {
			this.exception = error;
			return;			
		}
	}
	
}