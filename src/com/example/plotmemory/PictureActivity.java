package com.example.plotmemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CacheableCMFile;
import com.cloudmine.api.rest.response.FileCreationResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureActivity extends Activity {
	 private static final int CAMERA_REQUEST = 1888; 
	    private ImageView imageView;
	    private String selectedImagePath;
		private EditText mEdit;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_picture);
	        this.imageView = (ImageView)this.findViewById(R.id.picture);
	         mEdit = (EditText)findViewById(R.id.StoryInfo);

	            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	           /**
	            	Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
    				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	            */
	            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	         }
	       
	    public void pinMap(View view){
	    	if(fileId!=null)
	    	{
	    	  Intent intent = new Intent(PictureActivity.this, MapActivity.class);
	    	  intent.putExtra("storyInfo", mEdit.getText().toString());
	    	  intent.putExtra("picture", true);
	    	  intent.putExtra("fileID", fileId);
	    
	         startActivity(intent); 
	    	}
	    	else
	    	{
				Toast.makeText( getApplicationContext(), "Please wait a few second before pinning it to map", Toast.LENGTH_SHORT).show();

	    	}
	    	
	    }
	    private CMSessionToken token;
		private String fileId=null;
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
	       
	        	 Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                File imgFile = new  File(selectedImagePath);
	                if(imgFile.exists()){
	                    Bitmap photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	              
	    	            int nh = (int) ( photo.getHeight() * (512.0 / photo.getWidth()) );
	    	            Bitmap scaled = Bitmap.createScaledBitmap(photo, 512, nh, true);
	    	            Matrix matrix = new Matrix();
	    	            matrix.postRotate(90);
	    	            scaled = Bitmap.createBitmap(scaled, 0, 0, 
	    	            		scaled.getWidth(), scaled.getHeight(), 
	    	                                          matrix, true);
	    	            imageView.setImageBitmap(scaled);
	    	            CacheableCMFile newFile;
						try {
							newFile = new CacheableCMFile(new FileInputStream(imgFile));
					
		            	newFile.save(this, token, new Response.Listener<FileCreationResponse>() {
		            	    @Override
		            	    public void onResponse(FileCreationResponse fileCreationResponse) {
		            	    	fileId= fileCreationResponse.getfileId();
		            	         
		            	    }
		            	}, new Response.ErrorListener() {
		            	    @Override
		            	    public void onErrorResponse(VolleyError volleyError) {
		            	         
		            	    }
		            	});
		                    
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                
	        
	                }
	               
	        
	           }
	        else  if (resultCode == RESULT_CANCELED) 
            {
            	Toast.makeText(this, "Returning to Main Menu ", Toast.LENGTH_SHORT).show();
              	this.finish();
            }
	    }


		private String getPath(Uri uri) {
			 // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }
		//prevent data lost. Ask the user if they want to return to main menu 
		@Override
	    public void onBackPressed() {
	    	AlertDialog.Builder alertExit=new AlertDialog.Builder(this);
	    	alertExit.setMessage("Return to main menu?").setCancelable(true)
    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
    		{
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				PictureActivity.super.onBackPressed();
    				
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

	
	
	
	
