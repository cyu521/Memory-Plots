package com.example.plotmemory;



import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.persistance.ClassNameRegistry;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.LoginResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends FragmentActivity  {
	
	private static final String APP_ID = "d1522320deb14c0fb4e90dd767334ecb";
	private static final String API_KEY = "881FC2DB5353467F8C6B3EA6B5C9C8DE";
	private static final int NUM_PAGES = 2;
	private  CMUser user;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    public CMSessionToken sessionToken;
    static {
        ClassNameRegistry.register(CMInfo.CLASS_NAME, CMInfo.class);
    }
    
    public CMSessionToken getCMSessionToken(){
    	return user.getSessionToken();
    }
    public CMUser setNGetUser(){
    	 
          CMUser user = new CMUser("474144553@qq.com", "123456");
          
    	return user;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // initialize CloudMine library
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());

      user=setNGetUser();
        user.login(this, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
            	
            	sessionToken=loginResponse.getSessionToken();
                 
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                 
            }
        });
        
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        	 @Override
             public void onPageSelected(int position) {
        	
             }

             @Override
             public void onPageScrolled(int position,
                     float positionOffset, int positionOffsetPixels) {
            	 if(position ==1)
        		 {
        			 LocallySavableCMObject.loadAllObjects(context, sessionToken, new Response.Listener<CMObjectResponse>() {
        		    	 String fileID;   
 		    	    	List<String> snip = new ArrayList<String>();
 		    	    	List<String> id = new ArrayList<String>();
        				 @Override
        		    	    public void onResponse(CMObjectResponse objectResponse) {
        		    	    	Log.d("tag", objectResponse.getObjects().toString());
        		    	    	String info1 = objectResponse.getObjects().toString();
        		    	    	try{
        		    	    	JSONArray cmInfo = new JSONArray(info1);
        		    	    	map.clear();
        		        		for (int i = 0; i < cmInfo.length(); i++) {
        		        			JSONObject info = cmInfo.getJSONObject(i);
        		        			if(info.getJSONObject(info.keys().next()).has("story"))
        		        			{
        		        			String story = info.getJSONObject(info.keys().next()).getString("story");
        		        			String lng = info.getJSONObject(info.keys().next()).getString("lng");
        		        			String lat = info.getJSONObject(info.keys().next()).getString("lat");
        		        			fileID = info.getJSONObject(info.keys().next()).getString("fileID");
        		        			snip.add(story);
        		        			id.add(fileID);
        		        		 	Log.d("tag", story+lng+lat);
        		        		 	 map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
        		      				        .title("Click to view").snippet(story));
        		        		 	 
        		        		 	map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
        		        	            @Override
        		        	            public void onInfoWindowClick(Marker marker) {
        		        	            	
        		        	            	
        		        	            
        		        	            		
        		        	            	Intent intent = new Intent(MainActivity.this, MemoryViewer.class);
        		        	            	intent.putExtra("info",marker.getSnippet());
        		        	            	intent.putExtra("fileID", 	id.get(snip.indexOf(marker.getSnippet())));
        		        	            	startActivity(intent);


        		        	            }
        		        	        });
        		        			}
        		        		}
        		        	
        		        	} catch (Exception e) {
        		        		// TODO Auto-generated catch block
        		        		e.printStackTrace();
        		        	} 
        		    	    }
        		    	});
        		 }
             }

             @Override
             public void onPageScrollStateChanged(int state) {
             }
         });
        
        
    }
    
    

    public void showCamera(View view){
    	  Intent intent = new Intent(MainActivity.this, PictureActivity.class);
      	startActivity(intent); 
         
    }
    public void uploadPic(View view){
    	Intent intent = new Intent(MainActivity.this, UploadActivity.class);
      	startActivity(intent); 
    }
  
 
    
    
    public void openStory(View view){
    	Intent intent = new Intent(MainActivity.this, StoryActivity.class);

    	startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
         
            
        }

        @Override
        public Fragment getItem(int position) {
        	 MemoryViewers memory = new MemoryViewers();
        	 return memory.create(position);
            
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	private 	LocallySavableCMObject  loca;
	private Context context;
	  private SupportMapFragment fragment;
  	private GoogleMap map;
  	private Boolean flag= false;
  	private Boolean flag1= false;
    class MemoryViewers extends Fragment {
        /**
         * The argument key for the page number this fragment represents.
         */
        public static final String ARG_PAGE = "page";

        /**
         * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
         */
        private int mPageNumber;

        /**
         * Factory method for this fragment class. Constructs a new fragment for the given page number.
         */
        public MemoryViewers create(int pageNumber) {
        	MemoryViewers fragment = new MemoryViewers();
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, pageNumber);
            fragment.setArguments(args);
         
            return fragment;
        }

        public MemoryViewers() {
        }
      
        @Override
    	public void onActivityCreated(Bundle savedInstanceState) {
    		super.onActivityCreated(savedInstanceState);
    		if(flag)
    		{
    		FragmentManager fm = getChildFragmentManager();
    		fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
    		if (fragment == null) {
    			fragment = SupportMapFragment.newInstance();
    			fm.beginTransaction().replace(R.id.map, fragment).commit();

    		}
    		}
    	}
        public void refreshMap(){
        	
        	
 			String temp = "temp";
 			new getMemory().execute(temp);
        }

    	@Override
    	public void onResume() {
    		super.onResume();
    		if(flag)
    		{
    		if (map == null) {
    			map = fragment.getMap();
    			LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
    			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    			double lng = location.getLongitude();
    			double lat = location.getLatitude();
    			
    			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
    			map.setMyLocationEnabled(true);
    			
    			context=getApplicationContext();
     			String temp = "temp";
     			new getMemory().execute(temp);
    			
    			flag1=true;
    			flag=false;
    		}
    		
    	
    		}
    	}
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPageNumber = getArguments().getInt(ARG_PAGE);
            
        }
        @Override
        @SuppressLint("NewApi")
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout containing a title and body text.
        	
            ViewGroup rootView = (ViewGroup) inflater
                    .inflate(R.layout.main, container, false);
            flag=false;
        	
            if(getPageNumber()==1)
            	{

                 rootView = (ViewGroup) inflater
                        .inflate(R.layout.memory, container, false);
              
               
               
     		   flag=true;
               

            	}
            return rootView;
        }

        /**
         * Returns the page number represented by this fragment object.
         */
        public int getPageNumber() {
            return mPageNumber;
        }
   
  //class that gets the train info using asynctask
    class getMemory extends AsyncTask<String, Void, String> {

    	private String info=null;
    @Override
    protected String doInBackground(String... uri) {
    	LocallySavableCMObject.loadAllObjects(context, user.getSessionToken(), new Response.Listener<CMObjectResponse>() {
    	    @Override
    	    public void onResponse(CMObjectResponse objectResponse) {
    	    	Log.d("tag", objectResponse.getObjects().toString());
    	    	info=objectResponse.getObjects().toString();
    	    	try{
    	    	JSONArray cmInfo = new JSONArray(info);
    	    	map.clear();
        		for (int i = 0; i < cmInfo.length(); i++) {
        			JSONObject info = cmInfo.getJSONObject(i);
        			
        			String story = info.getJSONObject(info.keys().next()).getString("story");
        			String lng = info.getJSONObject(info.keys().next()).getString("lng");
        			String lat = info.getJSONObject(info.keys().next()).getString("lat");
        			//String tit = info.getJSONObject(info.keys().next()).getString("title");
        		 	Log.d("tag", story+lng+lat);
        		 	 map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
      				        .title("Click to view").snippet(story));
        		 	map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
        	            @Override
        	            public void onInfoWindowClick(Marker marker) {
        	            	
        	            	Intent intent = new Intent(MainActivity.this, MemoryViewer.class);
        	            	intent.putExtra("info",marker.getSnippet());
        	            	startActivity(intent);


        	            }
        	        });
        		}
        	
        	} catch (Exception e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	} 
    	    }
    	});
    	return info;
       
    	
    }
   

    }
    }
}
