package com.twf.core.model;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.twfportable.ForumPost;


public class ForumIterator {
	
	private FileOutputStream fs;// = new FileOutputStream("forum.ser");
	private FileInputStream in;
	private ObjectOutputStream ob;// = new ObjectOutputStream(fs);
	private ObjectInputStream obin;
	private ArrayList<Forum> forums;
	public final static String defaultfilename="forum.ser";
	
	public ForumIterator(){
		forums = new ArrayList<Forum>();
	}
	public void writeForumsToFile(String filename){
		try {
			fs=new FileOutputStream(filename);
			ob = new ObjectOutputStream(fs);
			for(int i=0;i<forums.size();i++){
				System.out.println("writing "+(i+1)+" forum");
				ob.writeObject(forums.get(i));
			}
			ob.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readForumsFromFile(String filename){
		try {
			in=new FileInputStream(filename);
			obin = new ObjectInputStream(in);
			int counter =0;
			while(true){
				Object temp = obin.readObject();
				counter = counter+1;
				System.out.println("adding "+counter+" forum");
				forums.add((Forum)temp);
			}

		} catch (Exception e) {
			try{
				if(in!=null){
					in.close();
				}
				if(obin!=null){
					obin.close();
				}
			}catch(Exception ea){

			}
		}
	}
	public void printForumNames(){
		for(int i=0;i<forums.size();i++){
			Forum temp = forums.get(i);
			System.out.println("Forum:"+temp.forumname);
			for(int j=0;j<temp.topics.size();j++) {
				temp.topics.get(j).printPosts();
			}
		}
	}
	public void add(Forum f){
		forums.add(f);
	}
	public void createSampleForums(){
		Forum forummisc = new Forum("General Information");
		ForumTopic top = new ForumTopic("Read this before Posting","Aristotelis");
		forummisc.addTopic(top);
		add(forummisc);
		Forum forum = new Forum("Music");
		ForumTopic top1 = new ForumTopic("Rock","Aristotelis");
		ForumTopic top2 = new ForumTopic("R'n'B","Aristotelis");
		ForumTopic top3 = new ForumTopic("Classical","Aristotelis");
		forum.addTopic(top1);
		forum.addTopic(top2);
		forum.addTopic(top3);
		add(forum);
		
		Forum forum2 = new Forum("Cinema");
		ForumTopic top12 = new ForumTopic("Previews","Tzina");
		ForumTopic top22 = new ForumTopic("Reviews","Tzina");
		ForumTopic top32 = new ForumTopic("Downloadable","Aristotelis");
		forum2.addTopic(top12);
		forum2.addTopic(top22);
		forum2.addTopic(top32);
		add(forum2);
		
		Forum forumcars = new Forum("Cars");
		ForumTopic rules = new ForumTopic("Forum Rules","marios");
		rules.addPost(new ForumPost("aristotelis","Here we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here1 we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here2 we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here3 we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here4 we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here5 we right the rules for this forum"));
		rules.addPost(new ForumPost("aristotelis","Here6 we right the rules for this forum"));
		
		ForumTopic tuning = new ForumTopic("Tuning","nikos");
		ForumTopic touring = new ForumTopic("Hipster Cars","orestis");
		forumcars.addTopic(rules);
		forumcars.addTopic(tuning);
		forumcars.addTopic(touring);
		add(forumcars);
	}
	public void clearForumList(){
		forums.clear();
	}
	public boolean deleteForumDatabase(){
		File f = new File(defaultfilename);
		if(f.exists()){
			try{
				boolean flag=f.delete();
				if(flag){
					return true;
				}
				else{
					return false;
				}
			}catch(Exception e){
				return false;
			}
		}
		else{
			return false;
		}
	}
	public ArrayList<String> getForumList(){
		ArrayList<String> forumlist = new ArrayList<String>();
		for(int i=0;i<forums.size();i++){
			forumlist.add(forums.get(i).forumname+"%%%%"+forums.get(i).topics.size());
		}
		return forumlist;
	}
	public ArrayList<String> getTopicList(String tempforumname){
		ArrayList<String> forumlist = new ArrayList<String>();
		for(int i=0;i<forums.size();i++){
			if(forums.get(i).forumname.equalsIgnoreCase(tempforumname)){
				for(int j=0;j<forums.get(i).topics.size();j++){
					forumlist.add(forums.get(i).topics.get(j).topicname+"%%%%"+forums.get(i).topics.get(j).authorname+"%%%%"+forums.get(i).topics.get(j).howmanyviews+"%%%%"+forums.get(i).topics.get(j).theposts.size()+"%%%%"+forums.get(i).topics.get(j).creationdate+"%%%%"+forums.get(i).topics.get(j).lastchangeddate);
				}
				break;
			}
		}
		return forumlist;
	}
	public ArrayList<ForumPost> getPostList(String tempforumname,String temptopicname){
		for(int i=0;i<forums.size();i++){
			if(forums.get(i).forumname.equalsIgnoreCase(tempforumname)){
				for(int j=0;j<forums.get(i).topics.size();j++){
					if(forums.get(i).topics.get(j).topicname.equalsIgnoreCase(temptopicname)){
						forums.get(i).topics.get(j).howmanyviews=forums.get(i).topics.get(j).howmanyviews+1;
						return forums.get(i).topics.get(j).theposts;
					}
				}
			}
		}
		return null;
	}
	public void addTopicAt(String forum,ForumTopic top){
		for(int i=0;i<forums.size();i++){
			if(forums.get(i).forumname.equalsIgnoreCase(forum)){
				forums.get(i).addTopic(top);
			}
		}
	}
	public void addPostAt(String forum,String top,ForumPost post){
		for(int i=0;i<forums.size();i++){
			if(forums.get(i).forumname.equalsIgnoreCase(forum)){
				for(int j=0;j<forums.get(i).topics.size();j++){
					if(forums.get(i).topics.get(j).topicname.equalsIgnoreCase(top)){
						forums.get(i).topics.get(j).addPost(post);
					}
				}
			}
		}
	}
	public static byte[] getPostBytes(ArrayList<ForumPost> alist){
		ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		try {
			ObjectOutputStream out = new ObjectOutputStream(baos);
			for(int i=0;i<alist.size();i++){
				out.writeObject(alist.get(i));
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args){
		ForumIterator forum = new ForumIterator();
		forum.createSampleForums();
		forum.writeForumsToFile(defaultfilename);
		forum.clearForumList();
		forum.readForumsFromFile(defaultfilename);
		forum.printForumNames();
		forum.deleteForumDatabase();
	}
}
