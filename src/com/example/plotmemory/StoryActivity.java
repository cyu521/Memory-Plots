package com.example.plotmemory;


import java.io.Serializable;

import com.cloudmine.api.CMSessionToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StoryActivity extends Activity{

	EditText mEdit;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_story);
	        mEdit   = (EditText)findViewById(R.id.StoryInfo);
	    }
	    public void pinMap(View view){
	    	  Intent intent = new Intent(StoryActivity.this, MapActivity.class);
	    	  intent.putExtra("storyInfo", mEdit.getText().toString());
	    	  intent.putExtra("picture", false);
	    

	         startActivity(intent); 
	    	
	    }
	    @Override
	    public void onBackPressed() {
	    	AlertDialog.Builder alertExit=new AlertDialog.Builder(this);
	    	alertExit.setMessage("Return to main menu?").setCancelable(true)
    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
    		{
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				StoryActivity.super.onBackPressed();
    				
    			}
    		}).setNegativeButton("No", new DialogInterface.OnClickListener() 
    		{
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.cancel();
    				
    			}
    		});
    		AlertDialog alertDialog=alertExit.create();
    		alertDialog.show();

	     
	    }
	    
}
