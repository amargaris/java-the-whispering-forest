package com.twf.core.threads;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.example.twfportable.TWFMessage;
import com.twf.core.gfx.TWFServer;


public class ServerReceiveThread extends Thread{
	public DatagramSocket recsocket;
	public TWFServer serv;
	public int servermode; //0 gia kanonikos ,1 gia ftp,2 gia gaming server
	public boolean flag=true;
	
	public static final int NORMAL =0;
	public static final int FTP =1;
	public static final int GAME =2;
	
	public ServerReceiveThread(DatagramSocket thesock,TWFServer theserv,int mode){
		recsocket=thesock;
		serv=theserv;
		servermode=mode;
	}
	
	public void run(){
		while(flag){
			TWFMessage mes = receiveMessage(recsocket);
			if(mes==null){
				continue;
			}
			int l=0;
			if(mes.getHeader().equalsIgnoreCase("updateuser")){ //Patcharisma gia to "updateuser" message
				if(servermode==0){
					l = serv.nameToId(mes.getFrom());
					String addr = mes.s.toString().substring(1);
					System.out.println("entry"+serv.userlist.get(l).username+" "+ addr+ mes.port);
					serv.setIpPort(serv.userlist.get(l), addr, mes.port);
					continue;
				}
				else if(servermode==1){
					l = serv.nameToId(mes.getFrom());
					serv.setFtpPort(serv.userlist.get(l),mes.port);
					continue;
				}
				else if(servermode==2){
					l = serv.nameToId(mes.getFrom());
					serv.setGamePort(serv.userlist.get(l),mes.port);
					continue;
				}
			}
			TWFMessage response;
			if(servermode==0){ //Normal server
				response = serv.decide(mes);
			}
			else if(servermode==1){ // FTP server
				response = serv.decideFTP(mes);
			}
			else { //Gaming server
				response = serv.decideGame(mes);
			}
            if(response==null){ 
            	continue;
            }
            sendMessage(response,recsocket);
		}
	}
	public void setFlag(boolean key){
		flag=key;
	}
	public void sendMessage(TWFMessage mes,DatagramSocket sock){ //Methodos poy apostellei ObjectBased mhnyma stin address to kai port toport POY BRISKONTAI ENTOS TOY OBJECT
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(mes);		
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,mes.s, mes.port );
		    sock.send(sendPacket);
		}
		catch(Exception e){
			//e.printStackTrace();
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
			//e.printStackTrace();
			return null;
		}
	}

}
