package com.example.plotmemory;

import java.io.InputStream;
import java.util.Arrays;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.CacheableCMFile;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.FileLoadResponse;
import com.cloudmine.api.rest.response.LoginResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MemoryViewer extends Activity{
private static final CMSessionToken sessionToken = null;
private ImageView imageView;

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.memoryview);
    Bundle extras = getIntent().getExtras();
    
    TextView text = (TextView)this.findViewById(R.id.memory);
    text.setText( extras.get("info").toString());
    String fieldID=extras.get("fileID").toString();
	 this.imageView = (ImageView)this.findViewById(R.id.image);
   if(fieldID!="none" && fieldID!=null){

	   
	   CacheableCMFile.loadFile(this, fieldID, sessionToken, new Response.Listener<FileLoadResponse>() {
		    @Override
		    public void onResponse(FileLoadResponse fileLoadResponse) {
		 
		 byte[] file = fileLoadResponse.getFile().getFileContents();
		 Bitmap decodedByte = BitmapFactory.decodeByteArray(file, 0, file.length); 
	     int nh = (int) ( decodedByte.getHeight() * (512.0 / decodedByte.getWidth()) );
         Bitmap scaled = Bitmap.createScaledBitmap(decodedByte, 512, nh, true);
         Matrix matrix = new Matrix();
         matrix.postRotate(90);
         scaled = Bitmap.createBitmap(scaled, 0, 0, 
         		scaled.getWidth(), scaled.getHeight(), 
                                       matrix, true);
         imageView.setImageBitmap(scaled);
		         
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError volleyError) {
		         
		    }
		});
	   
   }
}
}