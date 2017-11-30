package com.twf.core.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Forum implements Serializable {
	
	private static final long serialVersionUID = 8428196232061663441L;
	private int topiccount;
	public String forumname;
	public ArrayList<ForumTopic> topics;
	
	public Forum(String thetheme){
		forumname = thetheme;
		topiccount=0;
		topics = new ArrayList<ForumTopic>();
	}
	public void addTopic(ForumTopic top){
		topiccount=topiccount+1;
		topics.add(top);
	}
	public String getTopics(){
		String s ="\n";
		for(int i=1;i<=topics.size();i++){
			s = s + ""+i+"."+topics.get(i-1).topicname+"\n";
		}
		return s;
	}
	
}
