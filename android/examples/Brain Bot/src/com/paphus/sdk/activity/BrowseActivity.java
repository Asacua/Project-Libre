package com.paphus.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.paphus.brainbot.R;
import com.paphus.sdk.activity.actions.HttpAction;
import com.paphus.sdk.activity.actions.HttpFetchAction;
import com.paphus.sdk.activity.actions.HttpGetCategoriesAction;
import com.paphus.sdk.activity.actions.HttpGetInstancesAction;
import com.paphus.sdk.activity.actions.HttpGetTagsAction;
import com.paphus.sdk.config.BrowseConfig;
import com.paphus.sdk.config.InstanceConfig;

/**
 * Browse activity for searching for a bot instance.
 */
public class BrowseActivity extends Activity {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		CheckBox checkbox = (CheckBox)findViewById(R.id.imagesCheckBox);
		checkbox.setChecked(MainActivity.showImages);

		resetLast();
    	
		Spinner sortSpin = (Spinner) findViewById(R.id.sortSpin);
		ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, new String[]{
        			"connects",
        			"connects today",
        			"connects this week",
        			"connects this month",
        			"name",
        			"date",
        			"size",
        			"last connect"
			    
        });
		sortSpin.setAdapter(adapter);

    	HttpAction action = new HttpGetTagsAction(this, getType());
    	action.execute();
    	
    	action = new HttpGetCategoriesAction(this, getType());
    	action.execute();
	}
	
	public void resetLast() {
		if (MainActivity.current == null) {
			return;
		}
		Button button = (Button)findViewById(R.id.lastButton);
    	SharedPreferences cookies = MainActivity.current.getPreferences(Context.MODE_PRIVATE);
    	String last = cookies.getString("instance", null);
    	if (last != null) {
    		button.setText(last);
    		button.setVisibility(View.VISIBLE);
    	} else {
    		button.setVisibility(View.GONE);
    	}
	}

	public void superOnCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		resetLast();
    	super.onResume();
	}

	public void openLast(View view) {
    	SharedPreferences cookies = MainActivity.current.getPreferences(Context.MODE_PRIVATE);
    	String last = cookies.getString("instance", null);
        if (last == null) {
        	MainActivity.showMessage("Bot is invalid", this);
        	return;
        }

        InstanceConfig config = new InstanceConfig();
        config.name = last;
        HttpAction action = new HttpFetchAction(this, config);
    	action.execute();
	}
	
	public void browse(View view) {
		BrowseConfig config = new BrowseConfig();
		
		config.typeFilter = "Public";
		RadioButton radio = (RadioButton)findViewById(R.id.privateRadio);
		if (radio.isChecked()) {
			config.typeFilter = "Private";
		}
		radio = (RadioButton)findViewById(R.id.personalRadio);
		if (radio.isChecked()) {
			config.typeFilter = "Personal";
		}
		Spinner sortSpin = (Spinner)findViewById(R.id.sortSpin);
		config.sort = (String)sortSpin.getSelectedItem();
		AutoCompleteTextView tagText = (AutoCompleteTextView)findViewById(R.id.tagsText);
		config.tag = (String)tagText.getText().toString();
		AutoCompleteTextView categoryText = (AutoCompleteTextView)findViewById(R.id.categoriesText);
		config.category = (String)categoryText.getText().toString();
		EditText filterEdit = (EditText)findViewById(R.id.filterText);
		config.filter = filterEdit.getText().toString();
		CheckBox checkbox = (CheckBox)findViewById(R.id.imagesCheckBox);
		MainActivity.showImages = checkbox.isChecked();
		
		config.type = getType();
		
		HttpAction action = new HttpGetInstancesAction(this, config);
		action.execute();
	}
	
	public String getType() {		
		return "Bot";
	}
}
