package com.twf.core;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class TWFUser implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7428939298502886538L;
	
	public String username;
	private String userpass;
	public String ip;
	public int portdest,portftpdest,portgamedest;
	//public  TWFuser[] flist = new TWFuser[TWFServer.userlimit]; //transient
	public ArrayList<TWFUser> flist = new ArrayList<TWFUser>();
	public String sentence;
	//public String[] history=new String[100];
	//public String[] historydate = new String[100];
	public ArrayList<String> history = new ArrayList<String>();
	public ArrayList<String> historydate = new ArrayList<String>();
	//public int numoffriends;
	public transient boolean active=false;
	public transient String port;
	
	
	public TWFUser(String name,String pass,String sen){ // Constructor me 3 orismata
		//this.numoffriends=0;
		//this.sentence=sen;
		newSentence(sen);
		this.username=name;
		this.userpass=pass;
	}
	public TWFUser(String name,String pass,String sen,boolean isonline){
		//this.numoffriends=0;
		this.sentence=sen;
		newSentence(sen);
		this.username=name;
		this.userpass=pass;
		setActive(isonline);
	}
	public TWFUser(String name,String pass){ //Constructor me 2 orismata
		//this.numoffriends=0;
		this.username=name;
		this.userpass=pass;
	}
	public TWFUser(int a,String name,String sen){
		this.username=name;
		this.sentence=sen;
		newSentence(sen);
	}
	public TWFUser(int a, String name,String sen,boolean ison){
		this.username=name;
		this.sentence=sen;
		newSentence(sen);
		this.active=ison;
	}
	public boolean addfriend(TWFUser a){
		//if(numoffriends<flist.size()){
			//for(int i=0;i<flist.size();i++){
			//	if(flist.get(i)==null){
				//	flist[i]=a;
					//return true;
				//}
			//}
		//}
		//else{
			//return false;
		//}
		//return false;
		flist.add(a);
		return true;
	}
	public boolean removefriend(TWFUser a){
		for(int i=0;i<flist.size();i++){
			if(flist.get(i).username.equalsIgnoreCase(a.username)){
				flist.remove(i);
				break;
			}
		}
		return true;
	}
	public boolean newSentence(String sen){
		if(sen.indexOf('@')==-1){
			//for(int i=0;i<history.length;i++){
				//if(history[i]==null){
					Date date=new Date();
					DateFormat litedateFormat=new SimpleDateFormat("HH:mm:ss");;
					String time=litedateFormat.format(date);
					historydate.add(time);//[i]=time;
					history.add(sen);//[i]=sen;
					return true;
					//break;
				//}	
			//}
			//return false;
		}
		else {
			String[] temp = new String[2];
			temp = sen.split("@");
			//for(int i=0;i<history.length;i++){
				//if(history[i]==null){
					historydate.add(temp[0]);//[i]=temp[0];
					history.add(temp[1]);//=temp[1];
					return true;
				//}
			//}
			//return false;
		}
	}
	public boolean hasFriend(String name){
		int point=-1;
		for(int i=0;i<this.flist.size();i++){
			//if(flist[i]!=null){
				if(flist.get(i).username.equalsIgnoreCase(name)){
					point=i;
				}
			//}
		}
		if(point==-1){
			return false;
		}
		else {
			return true;
		}
	}
	public int getFriendIndex(String name){
		int point=-1;
		for(int i=0;i<flist.size();i++){
			if(flist.get(i).username.equalsIgnoreCase(name)){
				point=i;
			}
		}
		return point;
	}
	public void setIP(String theip,int a){
		ip=theip;
		portdest = a;
	}
	public void setGamePort(int a){
		portgamedest=a;
	}
	public void setFtpPort(int a){
		portftpdest=a;
	}
	public String getMsg(){
		//int a =0;
		//for(a=0;a<history.length;a++){
			//if(history[a]==null){
				//break;
			//}
		//}
		//if(a==0){
			//return history[0];
		//}
		//else {
		return historydate.get(historydate.size()-1)+"@"+history.get(history.size()-1);
		
	}
	public void setActive(boolean key){
		active=key;
	}
	public boolean checkPass(String check){
		if(check.equalsIgnoreCase(userpass)){
			return true;
		}
		else{
			return false;
		}
	}
	public String[] getfmsg(){
		String[] list = new String[flist.size()];
		for(int i=0;i<flist.size();i++){
				list[i]=flist.get(i).username+flist.get(i).getMsg();
			
		}
		return list;
	}
}
