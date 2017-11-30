package com.twf.core.model;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.twfportable.ForumPost;


public class ForumTopic implements Serializable {
	

	private static final long serialVersionUID = 2072693823966270151L;
	public ArrayList<ForumPost> theposts;
	public String authorname;
	public String topicname;
	public String creationdate;
	public String lastchangeddate;
	public int howmanyposts;
	public int howmanyviews;
	public static DateFormat form =new SimpleDateFormat("(dd/MM/yy-hh:mm)");
	
	public ForumTopic(String thetopicname,String author){
		Date dat = new Date();
		creationdate = form.format(dat);
		lastchangeddate=form.format(dat);
		topicname = thetopicname;
		authorname=author;
		howmanyposts=0;
		howmanyviews=0;
		theposts = new ArrayList<ForumPost>();
	}
	public void addPost(ForumPost post){
		Date dat = new Date();
		lastchangeddate=form.format(dat);
		howmanyposts = howmanyposts+1;
		theposts.add(post);
	}
	public void increaseViews(){
		howmanyviews = howmanyviews+1;
	}
	public void printPosts(){
		System.out.println("Topic: "+topicname);
		for(int i=0;i<theposts.size();i++){
			System.out.println(theposts.get(i).toString());
		}
	}
}
