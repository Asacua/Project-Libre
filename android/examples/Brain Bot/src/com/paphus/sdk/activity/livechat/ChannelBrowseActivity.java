package com.paphus.sdk.activity.livechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.paphus.brainbot.R;
import com.paphus.sdk.activity.BrowseActivity;
import com.paphus.sdk.activity.MainActivity;
import com.paphus.sdk.activity.actions.HttpAction;
import com.paphus.sdk.activity.actions.HttpFetchAction;
import com.paphus.sdk.activity.actions.HttpGetCategoriesAction;
import com.paphus.sdk.activity.actions.HttpGetTagsAction;
import com.paphus.sdk.config.ChannelConfig;

/**
 * Browse activity for searching for a channel.
 */
public class ChannelBrowseActivity extends BrowseActivity {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.superOnCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		resetLast();

		RadioButton radio = (RadioButton)findViewById(R.id.personalRadio);
		radio.setText("My Channels");
    	
		Spinner sortSpin = (Spinner) findViewById(R.id.sortSpin);
		ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{
        			"connects",
        			"connects today",
        			"connects this week",
        			"connects this month",
        			"name",
        			"date",
        			"messages",
        			"users"
			    
        });
		sortSpin.setAdapter(adapter);

    	HttpAction action = new HttpGetTagsAction(this, getType());
    	action.execute();
    	
    	action = new HttpGetCategoriesAction(this, getType());
    	action.execute();
	}
	
	public void resetLast() {
		Button button = (Button)findViewById(R.id.lastButton);
		if (button == null) {
			return;
		}
		if (MainActivity.current == null) {
			return;
		}
    	SharedPreferences cookies = MainActivity.current.getPreferences(Context.MODE_PRIVATE);
    	String last = cookies.getString("channel", null);
    	if (last != null) {
    		button.setText(last);
    		button.setVisibility(View.VISIBLE);
    	} else {
    		button.setVisibility(View.GONE);
    	}
	}

	public void openLast(View view) {
    	SharedPreferences cookies = MainActivity.current.getPreferences(Context.MODE_PRIVATE);
    	String last = cookies.getString("channel", null);
        if (last == null) {
        	MainActivity.showMessage("Channel is invalid", this);
        	return;
        }

        ChannelConfig config = new ChannelConfig();
        config.name = last;
        HttpAction action = new HttpFetchAction(this, config);
    	action.execute();
	}
	
	public String getType() {		
		return "Channel";
	}
}
