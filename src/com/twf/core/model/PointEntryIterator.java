package com.twf.core.model;
import java.io.Serializable;
import java.util.ArrayList;


public class PointEntryIterator implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<PointEntry> list;
	public PointEntryIterator(){
		list = new ArrayList<PointEntry>();
	}
	public String getLatLong(String name){ //epistrefei ws string tis syntetagmenes
		for(int i=0;i<list.size();i++){
			if(list.get(i).name.equalsIgnoreCase(name)){
				return list.get(i).lat+"@@@@"+list.get(i).lon;
			}
		}
		return null;
	}
	public void setLatLong(String name,String lat,String lon){
		for(int i=0;i<list.size();i++){
			if(list.get(i).name.equalsIgnoreCase(name)){
				list.get(i).lat=lat;
				list.get(i).lon=lon;
			}
		}
	}
	public double distance(double lat1, double lon1, double lat2, double lon2, char unit) { //apostasi 2 simeiwn basismeni se syntetagmenes
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	      if (unit == 'K') {
	        dist = dist * 1.609344;
	      } else if (unit == 'N') {
	        dist = dist * 0.8684;
	        }
	      return (dist);
	}
	public ArrayList<String> getNearby(String name,int meters){ //briskei osous xristes briskontai konta se apostasi meters apo ton name
		String s = getLatLong(name);
		if(name==null){
			return null;
		}
		String[] temp=s.split("@@@@");
		double lat1 = Double.parseDouble(temp[0]);
		double lon1 = Double.parseDouble(temp[1]);
		ArrayList<String> returnval = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).name.equalsIgnoreCase(name)){
				continue;
			}
			double lat2 = Double.parseDouble(list.get(i).lat);
			double lon2 = Double.parseDouble(list.get(i).lon);
			double check = distance(lat1,lon1,lat2,lon2,'K')*1000;
			int tempmeters = (int)check;
			if(tempmeters<=meters){
				returnval.add(list.get(i).name+"@@@@"+list.get(i).lat+"@@@@"+list.get(i).lon);
			}
		}
		return returnval;
	}

    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
    public static void main(String[] args){
    	PointEntryIterator point = new PointEntryIterator();
    	System.out.println(point.distance(37.92, 23.70, 37.96786, 23.62506,'K'));
    }
}
