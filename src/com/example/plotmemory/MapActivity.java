package com.example.plotmemory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.LoginResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity  {
	private GoogleMap mMap;  
	private String story;
	private String fileID;
	private Boolean picture;
	private CMSessionToken token;
	private LocallySavableCMObject item;
	@SuppressLint("NewApi")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map);
	        
	        Bundle extras = getIntent().getExtras();
	        //this code is commented out because the emmunlator could not get the current location
	        //however if we un comment this code and put it on an android, it will work
	        
	       LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double lng = location.getLongitude();
			double lat = location.getLatitude();
		
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
			mMap.setMyLocationEnabled(true);
			
			story = extras.getString("storyInfo").toString();
			picture =  extras.getBoolean("picture");
			if(picture){
				fileID =extras.getString("fileID").toString();
				
			}
			 CMUser user = new MainActivity().setNGetUser();
			  user.login(this, new Response.Listener<LoginResponse>() {
	              @Override
	              public void onResponse(LoginResponse loginResponse) {
	              	
	            	  token=loginResponse.getSessionToken();
	                   
	              }
	          }, new Response.ErrorListener() {
	              @Override
	              public void onErrorResponse(VolleyError volleyError) {
	                   
	              }
	          });
			   
			 
			Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
					.title("Click here to save").draggable(true)
					.snippet(story));
			mark.showInfoWindow();
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	            @Override
	            public void onInfoWindowClick(Marker marker) {
	            	String lng = String.valueOf(marker.getPosition().longitude);
	            	String lat = String.valueOf(marker.getPosition().latitude);
	            	String story = marker.getSnippet();
	            	if(picture){
	            		item = new CMInfo(story, fileID, lng, lat);
	 	            	item.save(getApplicationContext(), token, new Response.Listener<ObjectModificationResponse>() {
	 	            	    @Override
	 	            	    public void onResponse(ObjectModificationResponse response) {
	 	            	        Log.d("saved", "item was saved? " + response.wasModified(item.getObjectId()));     
	 	            	       Intent intent = new Intent(MapActivity.this, MainActivity.class);
	 	            	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 	            	        startActivity(intent);
	 	            	    }
	 	            	});
	            		
	            	}
	            	else
	            	{
	            	 item = new CMInfo(story, lng, lat);
	            	item.save(getApplicationContext(), token, new Response.Listener<ObjectModificationResponse>() {
	            	    @Override
	            	    public void onResponse(ObjectModificationResponse response) {
	            	        Log.d("saved", "item was saved? " + response.wasModified(item.getObjectId()));  
	            	        Intent intent = new Intent(MapActivity.this, MainActivity.class);
 	            	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	            	        startActivity(intent);
	            	    }
	            	});
	            	}
	            	//write code to go to main menu

	            }
	        });
	}
}
