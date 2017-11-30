package com.twf.core.threads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.example.twfportable.TWFMessage;
import com.twf.core.gfx.TWFClient;


public class UpdateThread extends Thread{
	
	private TWFClient theclient;
	private DatagramSocket thesock;
	private boolean flag=true;
	public UpdateThread(TWFClient client,DatagramSocket sock){
		theclient=client;
		thesock=sock;
	}
	public void run(){
		//minima gia apothikeysi stoixeiwn toy update socket

		sendMessage(new TWFMessage("updateuser",theclient.username,"",""),thesock,theclient.IPAddress,theclient.portdest);
		sendMessage(new TWFMessage("updateuser",theclient.username,"",""),thesock,theclient.IPAddress,theclient.portftpdest);
		sendMessage(new TWFMessage("updateuser",theclient.username,"",""),thesock,theclient.IPAddress,theclient.portgamedest);
		while(flag){
		    TWFMessage s = receiveMessage(thesock);
			if(s!=null){
				theclient.resolveMessage(s);
			}
			else {
				continue;
			}
		}
	}
	public void setFlag(boolean val){
		flag=val;
	}
	public synchronized void sendMessage(TWFMessage mes,DatagramSocket sock,InetAddress to,int toport){ //Methodos poy apostellei ObjectBased mhnyma stin address to kai port toport
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(mes);		
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,to, toport );
		    sock.send(sendPacket);
		}
		catch(Exception e){
		}
	}
	public TWFMessage receiveMessage(DatagramSocket sock){ //Methodos poy lambanei ObjectBased mhnyma apo to sygkekrimeno socket
		try{
			byte[] recData = new byte[1024];
		    DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
		    sock.receive(recPacket);
		   
		    ByteArrayInputStream inaos = new ByteArrayInputStream(recPacket.getData());
		    ObjectInputStream ins = new ObjectInputStream(inaos);
		    TWFMessage themessage = (TWFMessage) ins.readObject();
		    themessage.addIpAndPort(recPacket.getAddress(), recPacket.getPort()); //edw ebloytizetai to message me plhrofories apo xamhlotera(tcp/ip)
		    return themessage;
		}catch(Exception e){
			return null;
		}
	}
}
