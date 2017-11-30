package com.example.twfportable;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ForumPost implements Serializable {

	private static final long serialVersionUID = -55861404176378410L;
	public String creator;
	public String message;
	public String date;
	public static DateFormat form =new SimpleDateFormat("(dd/MM/yy-hh:mm)");
	
	public ForumPost(String who,String themessage){
		Date dat = new Date();
		date = form.format(dat);
		creator=who;
		message=themessage;
	}
	public String toString(){
		return date+" - "+creator+" : "+message;
	}
	public static void main(String[] args){
		ForumPost post = new ForumPost("aristotelis","hello world!");
		System.out.println(post.toString());//post.date+" - "+post.creator+" : "+post.message);
	}
}
