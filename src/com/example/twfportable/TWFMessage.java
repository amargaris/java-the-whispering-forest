package com.example.twfportable;


import java.io.Serializable;
import java.net.InetAddress;



public class TWFMessage implements Serializable{
	

	
	private static final long serialVersionUID = 4893010256655147927L;
	private String from,to,header,message;
	private byte[] payload;
	public transient InetAddress s;
	public transient int port;

	public TWFMessage(String theheader,String thefrom,String theto,String themessage) {
		from = thefrom;
		to = theto;
		header = theheader;
		message = themessage;
	}
	public TWFMessage(String thefrom,String theheader ,String themessage){
		from = thefrom;
		header=theheader;
		message=themessage;
	}
	public TWFMessage(String theheader,String thefrom, String theto,String themessage,byte[] thebyte){
		from = thefrom;
		to = theto;
		header = theheader;
		message = themessage;
		payload = thebyte;
	}
	public String getFrom(){
		return from;
	}
	public String getTo(){
		return to;
	}
	public String getHeader(){
		return header;
	}
	public String getMessage(){
		return message;
	}
	public byte[] getByte(){
		return payload;
	}
	public void addIpAndPort(InetAddress thead,int theport){
		this.s=thead;
		this.port=theport;
	}
	public String getSynopsis(){
		return getHeader()+"-"+getFrom()+"-"+getTo()+"-"+getMessage();
	}
}
