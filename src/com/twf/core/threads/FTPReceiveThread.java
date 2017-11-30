package com.twf.core.threads;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;


public class FTPReceiveThread extends Thread{
	
	private JFrame fram;
	private JProgressBar bar;
	private JLabel lab;
	public String receiver;
	public String file;
	private byte[] bytes;
	private boolean[] check;
	private boolean working=true;
	private static final int byteperpacket = 512;
	
	public FTPReceiveThread(JFrame theframe,String filename,String from,int bytesize){
		bytes= new byte[bytesize];
		check = new boolean[bytesize];
		for(int i=0;i<check.length;i++){
			check[i]=false;
		}
		fram=theframe;
		file=filename;
		receiver=from;
		Container cont = fram.getContentPane();
		Component[] s = cont.getComponents();
		lab = (JLabel) s[1];
		bar = (JProgressBar) s[0];
	}
	public void run(){
		while(working){
			int resultint = bytecount();
			bar.setValue(resultint);
			bar.setString(resultint+"%");
			lab.setText(resultint%2==0?"Waiting for file..":"Waiting for file.");
			//System.out.println("not breaking"+checkForFinish()+"--"+resultint);
			if(checkForFinish()){
				break;
			}
		}
		lab.setText("File transfer complete!");
		bar.setIndeterminate(true);
		bar.setStringPainted(false);
		JButton butt = new JButton("Open");
		butt.setName(file);
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = ((JButton)arg0.getSource()).getName();
				try{
					Desktop.getDesktop().open(new File(s));
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "Error opening the file! Open yourself Jackass");
				}
			}
			
		});
		try{
			FileOutputStream fs = new FileOutputStream(file);
			fs.write(bytes);
			fs.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		fram.add(butt);
		fram.pack();
	}
	public synchronized void setWorking(boolean b){
		working=b;
	}
	public synchronized boolean isAMatch(String testfile,String testfrom){
		if(file.equalsIgnoreCase(testfile)){
			if(testfrom.equalsIgnoreCase(receiver)){
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	public synchronized boolean addByte(byte[] thebytes,int location){
		int s = thebytes.length;
		if(s==byteperpacket){
			for(int i=0;i<byteperpacket;i++){
				bytes[location*byteperpacket+i]=thebytes[i];
				check[location*byteperpacket+i]=true;
			}
		}
		else {
			for(int i=0;i<thebytes.length;i++){
				bytes[location*byteperpacket+i]=thebytes[i];
				check[location*byteperpacket+i]=true;
			}
		}
		boolean key = checkForFinish();
		return key;
	}
	public int bytecount(){
		double count = 0;
		for(int i=0;i<check.length;i++){
			if(check[i]){
				count = count+1;
			}
		}
		double s = (count / bytes.length)*100.0;
		return (int)s;
	}
	public boolean checkForFinish(){
		for(int i=0;i<check.length;i++){
			if(check[i]){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
}
