package com.twf.core.threads;
import java.awt.Component;
import java.awt.Container;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.example.twfportable.TWFMessage;
import com.twf.core.gfx.TWFClient;


public class FTPSendThread extends Thread{

	private String filename,target;
	private TWFClient client;
	private byte[] bytes;
	private int timeout=10;
	private JFrame fram;
	private JProgressBar bar;
	private JLabel lab;
	public static final int packetsize=512;
	
	public FTPSendThread(String thefilename,String to,TWFClient theclient,byte[] thebytes,JFrame thefram){
		filename=thefilename;
		client=theclient;
		bytes=thebytes;
		target=to;
		fram=thefram;
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container s = fram.getContentPane();
		Component[] comp=s.getComponents();
		bar = (JProgressBar)comp[0];
		lab = (JLabel)comp[1];
	}
	public void run(){
	
		long start,stop ;//= System.currentTimeMillis();
		int howmany = bytes.length/packetsize;
		int checkmod = bytes.length%packetsize;
		if(checkmod!=0){
			howmany=howmany+1;
		}
		start = System.currentTimeMillis();
		stop=0;
		lab.setText("User accepted!:");
		bar.setIndeterminate(false);
		bar.setValue(0);
		bar.setStringPainted(true);
		bar.setString("0%");
		
		for(int i=0;i<howmany-1;i++){ //apo 0 sto telos
			byte[] dat = new byte[packetsize];
			for(int k=0;k<packetsize;k++){
				dat[k]=bytes[i*packetsize+k];
			}
			boolean flag=true;
			do{
				TWFClient.sendMessage(new TWFMessage("ftpfragment",client.username,target,filename+"@@@@"+i,dat), client.clientSocket,client.IPAddress,client.portftpdest);
				TWFMessage m = client.receiveMessage(client.clientSocket);
				if(m!=null){
					if(m.getHeader().equalsIgnoreCase("ftpack")){
						flag=false;
					}
				}
			}while(flag);
			
			//System.out.println("Out from "+i);
				//}
			double k = (double) i;
			double howmany2 = (double) howmany;
			double s = (k/howmany2)*100;
			int a = (int) s;
			bar.setValue(a);
			bar.setString(a+"%");
			long timestamp=System.currentTimeMillis();
			long timestamp2=System.currentTimeMillis();
			do {
				timestamp2 = System.currentTimeMillis();
			}while((timestamp2-timestamp)<timeout);
		}
		byte[] dat = new byte[checkmod];
		for(int k=0;k<checkmod;k++){
			dat[k]=bytes[(howmany-1)*packetsize+k];
		}
		boolean flag=true;
		do{
			TWFClient.sendMessage(new TWFMessage("ftpfragment",client.username,target,filename+"@@@@"+(howmany-1),dat), client.clientSocket,client.IPAddress,client.portftpdest);
			TWFMessage m = client.receiveMessage(client.clientSocket);
			if(m!=null){
				if(m.getHeader().equalsIgnoreCase("ftpack")){
					flag=false;
				}
			}
		}while(flag);
		stop = System.currentTimeMillis();

		bar.setValue(100);
		bar.setString("100%");
		fram.setTitle("Success!");
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lab.setText("Sending is complete! Time Elapsed:"+(stop-start)/1000+" seconds");
	}

	public synchronized boolean isAMatch(String s1,String s2){
		if(s1.equalsIgnoreCase(filename)){
			if(s2.equalsIgnoreCase(target)){
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
}
