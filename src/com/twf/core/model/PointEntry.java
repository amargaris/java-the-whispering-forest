package com.twf.core.model;
import java.io.Serializable;


public class PointEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String lat;
	public String lon;
	public PointEntry(String thename,String thelat,String thelon){
		name=thename;
		lat=thelat;
		lon=thelon;
	}
}
