package com.example.plotmemory;

import com.cloudmine.api.db.LocallySavableCMObject;

public class CMInfo extends LocallySavableCMObject {
	private String story;
	private String lat;
	private String lng;
	private String fileID;
	

	 public static final String CLASS_NAME = "MyObject";
	
	public CMInfo() {
		super();
	}
	
	public CMInfo(String story, String lng, String lat) {
		this();
		this.story = story;
		this.lat = lat;
		this.lng = lng;
		this.fileID = "none";
	}
	
	public CMInfo(String lng, String lat) {
		this();
		this.lat = lat;
		this.lng = lng;
	}
	public CMInfo(String story, String fileID, String lng, String lat) {
		this();
		this.story = story;
		this.lat = lat;
		this.lng = lng;
		this.setFileID(fileID);
	}

	@Override
	    public String getClassName() {
	        return CLASS_NAME;
	    }
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
}
