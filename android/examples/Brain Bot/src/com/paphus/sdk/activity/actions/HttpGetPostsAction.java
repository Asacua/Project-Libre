package com.paphus.sdk.activity.actions;

import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.paphus.sdk.activity.MainActivity;
import com.paphus.sdk.activity.forum.ChoosePostActivity;
import com.paphus.sdk.config.BrowseConfig;
import com.paphus.sdk.config.ForumPostConfig;

public class HttpGetPostsAction extends HttpUIAction {
	BrowseConfig config;
	List<ForumPostConfig> posts;

	public HttpGetPostsAction(Activity activity, BrowseConfig config) {
		super(activity);
		this.config = config;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
		this.posts = MainActivity.connection.getPosts(this.config);
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
		MainActivity.posts = this.posts;
		
        Intent intent = new Intent(this.activity, ChoosePostActivity.class);		
        this.activity.startActivity(intent);
	}
}