package com.twf.core;
import java.io.Serializable;


public class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4507439771306004476L;
	public String username;
	public String password;
	public String ip;
	public String port1;
	public String port2;
	public boolean autologin;
	public boolean sound;
	public boolean ftpenabled;
	
	public State(String name,String pass,String theip,String theport1,String theport2,boolean login,boolean wantsound,boolean wantftp){
		username=name;
		password=pass;
		ip=theip;
		port1=theport1;
		port2=theport2;
		autologin=login;
		sound=wantsound;
		ftpenabled=wantftp;
	}

}
