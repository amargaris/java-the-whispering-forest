package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.example.twfportable.ForumPost;
import com.example.twfportable.TWFMessage;
import com.twf.core.TWFUser;
import com.twf.core.model.Forum;
import com.twf.core.model.ForumIterator;
import com.twf.core.model.ForumTopic;
import com.twf.core.model.PointEntry;
import com.twf.core.model.PointEntryIterator;
import com.twf.core.threads.ServerReceiveThread;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;


public class TWFServer {
	//Metablites Systhmatos
	public static int userlimit=12;
	public int port=10000;
	public int ftpport= 10001;
	public int gameport = 10002;
	public DatagramSocket recsocket,recftpsocket,recgamesocket,sendsocket;
	public DatagramPacket pack;
	//public TWFuser[] userlist= new TWFuser[userlimit];
	public ArrayList<TWFUser> userlist = new ArrayList<TWFUser>();
	private Date date;
	private DateFormat litedateFormat;
	public boolean operatekey=false;
	public boolean operateftpkey=false;
	public boolean operategamekey=false;
	public ServerReceiveThread thethread,theftpthread,thegamethread;
	
	//Metablites GUI
	public JFrame monitor;
	public JTextArea log,logftp,loggame;
	public JLabel userlog;
	public JLabel top;
	//public JLabel[] users = new JLabel[userlimit];
	public ArrayList<JLabel> users = new ArrayList<JLabel>();
	public JButton operbutt,operftpbutt,opergamebutt;
	public ImageIcon theicon=new ImageIcon(getClass().getResource("images/Oak.png"));
	public JScrollBar vertical;
	public JScrollPane logscroll,logftpscroll,loggamescroll,userscroll;
	public JTabbedPane tabpan;
	private JPanel normalpan,filepan,gamepan;
	private ForumIterator iterator;
	private PointEntryIterator pointiterator;
	
	public TWFServer() {  // Constructoras
		
		//Date
		litedateFormat=new SimpleDateFormat("HH:mm:ss");
		date = new Date();
		
		//Elegxos an yparxei arxeio records
		
		File input = new File("records.ser");
		if(!input.exists()){
		
			//Data Generation
			addMember(new TWFUser("alex","alex","hello this is alex"));
			addMember(new TWFUser("orestis","orestis","hello this is orestis"));
			addMember(new TWFUser("aristotelis","aristotelis","Hello this is Administrator"));
			addMember(new TWFUser("nikos","nikoss","Hello this is Nikos"));
			addMember(new TWFUser("marios","marios","Hello this is Marios"));
			addMember(new TWFUser("aggelos","aggelos","Hello this is Aggelos"));
			addMember(new TWFUser("mixalis","mixalis","Hello this is Mixalis"));
			addMember(new TWFUser("kwstas","kwstas","Hello this is Kwstas"));
			addMember(new TWFUser("grigoris","grigoris","Hello this is Grigoris"));
			addMember(new TWFUser("tzina","tzina","Hello eimai i tzinoula"));
		}
		else {
			readFile(); 
		}
		//Dhmioyrgia toy ForumIterator o opoios diaxeirizetai ta forums
		iterator = new ForumIterator();
		File forumfile = new File("forum.ser");
		if(forumfile.exists()){
			iterator.readForumsFromFile(ForumIterator.defaultfilename);
		}
		else{
			iterator.createSampleForums();
		}
		//Dhmioyrgia toy PointIterator o opoios diaxeirizetai tous xartes kai tis syntetagmenes
		File pointsfile = new File("points.ser");
		if(pointsfile.exists()){
			try {
				FileInputStream fin = new FileInputStream(pointsfile);
				ObjectInputStream obin = new ObjectInputStream(fin);
				pointiterator = (PointEntryIterator)obin.readObject();
				System.out.println("Point file loaded!");
				obin.close();
				fin.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		else{
			pointiterator = new PointEntryIterator();
			pointiterator.list.add(new PointEntry("aristotelis","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("alex","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("orestis","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("marios","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("tzina","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("grigoris","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("kwstas","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("mixalis","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("aggelos","43.321312","22.41231231"));
			pointiterator.list.add(new PointEntry("nikos","43.321312","22.41231231"));
			
		}
		
		//Synthetica
	   
		try 
	    {
	      UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
	      UIManager.put("Synthetica.tabbedPane.keepOpacity", true);
	    } 
	    catch (Exception e) 
	    {
	      e.printStackTrace();
	    }
	    //GUI settings
		
		monitor = new JFrame();
		monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		monitor.setTitle("This is the Whispering Forest's Server");
		monitor.setSize(800,500);
		monitor.setLocationRelativeTo(null);
		monitor.getContentPane().setBackground(new Color(124,252,0));
		monitor.setLayout(new FlowLayout());
		monitor.setIconImage(theicon.getImage());
		monitor.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {}

			@Override
			public void windowClosing(WindowEvent arg0) {
				writeFile();
				iterator.writeForumsToFile(ForumIterator.defaultfilename);
				writePointFile();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}

			@Override
			public void windowDeiconified(WindowEvent arg0) {}

			@Override
			public void windowIconified(WindowEvent arg0) {}

			@Override
			public void windowOpened(WindowEvent arg0) {}
			
		});
		
		//Component settings
		
		//To log toy Main Pane
		log = new JTextArea();
		log.setEditable(false);
		log.setBackground(Color.white);
		log.setForeground(Color.black);
		log.append("---------------------------LOG-----------------------------");
		logscroll = new JScrollPane(log);
		logscroll.setPreferredSize(new Dimension(400,400));
		
		//To log toy FTP Pane
		logftp = new JTextArea();
		logftp.setEditable(false);
		logftp.setBackground(Color.white);
		logftp.setForeground(Color.black);
		logftp.append("----------------------FILE-LOG-----------------------------");
		logftpscroll = new JScrollPane(logftp);
		logftpscroll.setPreferredSize(new Dimension(400,400));
		
		//To log toy Game Pane
		loggame = new JTextArea();
		loggame.setEditable(false);
		loggame.setBackground(Color.white);
		loggame.setForeground(Color.black);
		loggame.append("----------------------GAME-LOG-----------------------------");
		loggamescroll = new JScrollPane(loggame);
		loggamescroll.setPreferredSize(new Dimension(400,400));
		
		//To log twn xrhstwn ( koino gia ola ta Pane) 
		userlog = new JLabel();
		userlog.setLayout(new BoxLayout(userlog,BoxLayout.Y_AXIS));
		userlog.setOpaque(true);
		userlog.setBackground(Color.white);
		userlog.setForeground(Color.black);
		top = new JLabel("-------Users:-------");
		userlog.add(top);
		userscroll = new JScrollPane(userlog);
		userscroll.setPreferredSize(new Dimension(100,300));

		//Normal Operating Button
		operbutt = new JButton("Start");
		//operbutt.setBackground(Color.white);
		operbutt.setOpaque(true);
		operbutt.addActionListener(new ActionListener(){
			public int state=1;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(state==1){
					state=2;
					operbutt.setText("Stop");
					operatekey=true;
					operate();
				}
				else{
					state=1;
					operatekey=false;
					operate();
					operbutt.setText("Start");
				}		
			}		
		});
		
		//FTP Operating Button
		operftpbutt = new JButton("Start");
		operftpbutt.setOpaque(true);
		operftpbutt.addActionListener(new ActionListener(){
			public int state=1;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(state==1){
					state=2;
					operftpbutt.setText("Stop");
					operateftpkey=true;
					operateFTP();
				}
				else{
					state=1;
					operateftpkey=false;
					operateFTP();
					operftpbutt.setText("Start");
				}		
			}		
		});
		//GAME Opearting Button
		opergamebutt = new JButton("Start");
		opergamebutt.setOpaque(true);
		opergamebutt.addActionListener(new ActionListener(){
			public int state=1;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(state==1){
					state=2;
					opergamebutt.setText("Stop");
					operategamekey=true;
					operateGAME();
				}
				else{
					state=1;
					operategamekey=false;
					operateGAME();
					opergamebutt.setText("Start");
				}		
			}		
		});
		
		
		//Dhmioyrgia toy TabbedPane me ta panels gia tin kathe leitoyrgia
		tabpan = new JTabbedPane();

		normalpan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		normalpan.setOpaque(false);
		normalpan.add(logscroll);
		normalpan.add(operbutt);
		filepan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filepan.setOpaque(false);
		filepan.add(logftpscroll);
		filepan.add(operftpbutt);
		
		gamepan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gamepan.setOpaque(false);
		gamepan.add(loggamescroll);
		gamepan.add(opergamebutt);
		
		
		tabpan.add(normalpan,"Normal");
		tabpan.add(filepan,"File Transfer");
		tabpan.add(gamepan,"Gaming");
		tabpan.setBackgroundAt(0,new Color(1.0f ,0.0f ,0.0f, 1.0f));
		//Teliki topothetisi se container
		monitor.add(userscroll);
		monitor.add(tabpan);
		monitor.setVisible(true);
		//Methodos gia Refresh Twn Panels
		refreshUserPanel();	
		//Pathma twn koybiwn
		operbutt.doClick();
		operftpbutt.doClick();
		opergamebutt.doClick();
	}
	public void operate(){ //Basiki synarthsh leitoyrgias
		date=new Date();
		String time=litedateFormat.format(date);
		if(operatekey){
			try {
				recsocket = new DatagramSocket(port);
				recsocket.setSoTimeout(5000);
				thethread = new ServerReceiveThread(recsocket,this,ServerReceiveThread.NORMAL);
				thethread.setPriority(Thread.MAX_PRIORITY);
				thethread.start();
				log.append("\n"+time+": Server Started");
				
			} catch (SocketException e) {
				System.out.print("Failed to open socket at "+port);
			}
		}
		else {
			log.append("\n"+time+": Server Stopped");
			synchronized(thethread){
				thethread.setFlag(false);
			}
			recsocket.close();
		}
				
	}
	public void operateFTP(){
		date=new Date();
		String time=litedateFormat.format(date);
		if(operateftpkey){
			try {
				recftpsocket = new DatagramSocket(ftpport);
				recftpsocket.setSoTimeout(5000);
				theftpthread = new ServerReceiveThread(recftpsocket,this,ServerReceiveThread.FTP);
				theftpthread.setPriority(Thread.MAX_PRIORITY);
				theftpthread.start();
				logftp.append("\n"+time+": Server Started");
				
			} catch (SocketException e) {
				System.out.print("Failed to open socket at "+ftpport);
			}
		}
		else {
			logftp.append("\n"+time+": Server Stopped");
			synchronized(theftpthread){
				theftpthread.setFlag(false);
			}
			recftpsocket.close();
		}
	}
	public void operateGAME(){
		date=new Date();
		String time=litedateFormat.format(date);
		if(operategamekey){
			try {
				recgamesocket = new DatagramSocket(gameport);
				recgamesocket.setSoTimeout(5000);
				thegamethread = new ServerReceiveThread(recftpsocket,this,ServerReceiveThread.GAME);
				thegamethread.setPriority(Thread.MAX_PRIORITY);
				thegamethread.start();
				loggame.append("\n"+time+": Server Started");
				
			} catch (SocketException e) {
				System.out.print("Failed to open socket at "+gameport);
			}
		}
		else {
			loggame.append("\n"+time+": Server Stopped");
			synchronized(thegamethread){
				thegamethread.setFlag(false);
			}
			recgamesocket.close();
		}
	}
	public void writeFile(){ //Prospathei na seiriopoiisei ta antikeimena
		FileOutputStream fileOut=null;
		ObjectOutputStream out=null;
		try
	      {
	         fileOut =  new FileOutputStream("records.ser");
	         out = new ObjectOutputStream(fileOut);
	      }
		catch (Exception ff){
			ff.printStackTrace();
		}
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)!=null){
			try
		      {
		         out.writeObject(userlist.get(i));
		         
		      }catch(IOException f)
		      {
		    	  
		          f.printStackTrace();
		      }
			}
			else break;
			 
		}
		try {
			out.close();
	        fileOut.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void writePointFile(){
		try{
			FileOutputStream fout = new FileOutputStream("points.ser");
			ObjectOutputStream obout = new ObjectOutputStream(fout);
			obout.writeObject(pointiterator);
			System.out.println("Point File saved!");
			obout.close();
			fout.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void readFile(){ //Fortwnei ta seiriopoiimena antikeimena
		FileInputStream fileIn=null;
		ObjectInputStream in=null;
		try
	      {
	         fileIn =  new FileInputStream("records.ser");
	         in = new ObjectInputStream(fileIn);
	      }
		catch (Exception ff){
			ff.printStackTrace();
		}
		Object s=null;
		try {
			s = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(s!=null){
			addMember((TWFUser) s);
			try {
				s=in.readObject();
			} catch (Exception e) {
				break;
			}
		}
		try {
			in.close();
			fileIn.close();
			//JOptionPane.showMessageDialog(null, "importing successful");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void refreshUserPanel(){ //ananewnei ta JLabel twn xristwn poy einai active/inactive
		userlog.invalidate();
		userlog.removeAll();
		users.clear();
		userlog.add(new JLabel("-------Users:-------"));
		for(int i=0;i<userlist.size();i++){
			users.add( new JLabel(userlist.get(i).username));
			users.get(i).addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					JLabel lab = (JLabel)e.getComponent();
					lab.setOpaque(true);
					lab.setBackground(Color.blue);
					lab.getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			});
			JPopupMenu menu = new JPopupMenu();
			menu.setName(""+i);
			menu.addPopupMenuListener(new PopupMenuListener(){
				@Override
				public void popupMenuCanceled(PopupMenuEvent arg0) {
					JLabel ref=users.get(Integer.parseInt(((JPopupMenu)arg0.getSource()).getName()));
					ref.setBackground(Color.white);
				}
				@Override
				public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}
				@Override
				public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {}
			});
			JMenuItem item1 = new JMenuItem("Logout");
			item1.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					userlist.get(Integer.parseInt(((JPopupMenu)((JMenuItem)arg0.getSource()).getParent()).getName())).active=false;
					refreshUserPanel();
				}
				
			});
			JMenuItem item2 = new JMenuItem("Ban");
			menu.add(item1);
			menu.add(item2);
			
			users.get(i).setComponentPopupMenu(menu);
			userlog.add(users.get(i));
		}
		userlog.validate();
		userlog.repaint();
	}
	public boolean addMember(TWFUser theuser){ //topotheti neo xristi 
		userlist.add(theuser);
		return true;
	}
	public void setIpPort(TWFUser a,String s,int p){ //Rythmizei ta ip kai port sta opoia epikoinwnei o xristis
		a.setIP(s,p);
	}
	public void setFtpPort(TWFUser a,int p){
		a.setFtpPort(p);
	}
	public void setGamePort(TWFUser a,int p){
		a.setGamePort(p);
	}
	public TWFMessage decide(TWFMessage input){ // decider model gia ta minimata toy protokoloy (Basikos server)
		
		date=new Date();
		String time=litedateFormat.format(date);
		
		TWFMessage theresp = null;
		if(input.getSynopsis().indexOf("chat")==-1){ //DATE 30/3/2013
			log.append("\n"+time+"UserMsG: "+input.getSynopsis());
		}
			if(input.getHeader().equalsIgnoreCase("login")){
				int number =nameToId(input.getFrom());
				if(number==-1){
					theresp= new TWFMessage("login-error","","","");
				}
				else{
					if(userlist.get(nameToId(input.getFrom()))!=null){//[nameToId(input.getFrom())]!=null){
						if(!userlist.get(nameToId(input.getFrom())).active){
							boolean flag = attemptLogin(input.getFrom(),input.getTo());
							if(flag){
								log.append("\n"+time+" User: "+input.getFrom()+" has Logged in");
								theresp = new TWFMessage("login-ok",userlist.get(nameToId(input.getFrom())).getMsg(),"","");
								userlist.get(nameToId(input.getFrom())).port = input.port+"";
								users.get(nameToId(input.getFrom())).setForeground(Color.green);
								broadcastOnline(userlist.get(nameToId(input.getFrom())));
							}
							else {
								theresp= new TWFMessage("login-error","","","");//"login-error";
							}
						}
						else {
							theresp=new TWFMessage("login-error","","","");//"login-error";
						}
						theresp.addIpAndPort(input.s, input.port);
					}
					else {
						theresp=new TWFMessage("login-error","","","");
						theresp.addIpAndPort(input.s, input.port);
					}
				}
			}
			else if(input.getHeader().equalsIgnoreCase("viewforum")){
				ArrayList<String> names = iterator.getForumList();
				if(names.size()==0){
					theresp=new TWFMessage("emptyforumdatabase","","","");
				}
				else{
					String s = "";
					for(int i=0;i<names.size()-1;i++){
						s = s+names.get(i)+"@@@@";
					}
					s = s+names.get(names.size()-1);
					theresp = new TWFMessage("forums","","",s);
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("createforum")){
				ArrayList<String> names = iterator.getForumList();
				boolean flag=true;
				for(int i=0;i<names.size();i++){
					if(names.get(i).equalsIgnoreCase(input.getMessage())){
						flag=false;
						theresp = new TWFMessage("forumerror@1","","","");
						break;
					}
				}
				if(flag){
					iterator.add(new Forum(input.getMessage()));
					theresp = new TWFMessage("forumaddsuccess","","","");
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("viewtopics")){
				ArrayList<String> topicnames = iterator.getTopicList(input.getMessage());
				if(topicnames.size()==0){
					theresp = new TWFMessage("emptyforum","","","");
				}
				else{
					String s ="";
					for(int i=0;i<topicnames.size()-1;i++){
						s = s+topicnames.get(i)+"@@@@";
					}
					s = s+topicnames.get(topicnames.size()-1)+"@@@@";
					theresp = new TWFMessage("topics","","",s);
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("createtopic")){ //Create topic
				ArrayList<String> names = iterator.getTopicList(input.getMessage());
				boolean flag=true;
				for(int i=0;i<names.size();i++){
					if(names.get(i).equalsIgnoreCase(input.getTo())){
						flag=false;
						theresp = new TWFMessage("forumerror@1","","","");
						break;
					}
				}
				if(flag){
					iterator.addTopicAt(input.getMessage(), new ForumTopic(input.getTo(),input.getFrom()));
					theresp = new TWFMessage("topicaddsuccess","","","");
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("viewposts")){ //View posts se topic
				String[] kk = input.getMessage().split("@");
				ArrayList<ForumPost> posts = iterator.getPostList(kk[0], kk[1]);
				if(posts.size()==0){
					theresp = new TWFMessage("noposts","","","");
				}
				else{
					byte[] tempbyte =ForumIterator.getPostBytes(posts);
					if(tempbyte==null){
						theresp = new TWFMessage("byteerror","","","");
					}
					else{
						theresp = new TWFMessage("forumposts","","",input.getMessage(),tempbyte);
					}
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("topicreply")){ // Reply se topic
				String[] kk = input.getMessage().split("@");
				//ArrayList<ForumPost> posts = iterator.getPostList(kk[0], kk[1]);
				//posts.add(new ForumPost(input.getFrom(),input.getTo()));
				iterator.addPostAt(kk[0],kk[1],new ForumPost(input.getFrom(),input.getTo()));
				theresp = new TWFMessage("replyaddsuccess","","","");
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("logout")){
				boolean key = this.attemptLogin(input.getFrom(),input.getTo());
				if(key){					
					int count = nameToId(input.getFrom());
					userlist.get(count).setActive(false);
					theresp=new TWFMessage("logout-ok","","","");//"logout-ok";
					log.append("\n"+time+" User: "+input.getFrom()+" has Logged out");
					users.get(nameToId(input.getFrom())).setForeground(new Color(204,204,204));
					broadcastOffline(userlist.get(nameToId(input.getFrom())));//[nameToId(input.getFrom())]);
				}
				
				else {
					theresp=new TWFMessage("logout-error","","","");//"logout-error";
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("register")){
				int key = nameToId(input.getFrom());
				if(key!=-1){
					theresp=new TWFMessage("register-failed","","","");//"register-failed";
				}
				else {
					boolean check = addMember(new TWFUser(input.getFrom(),input.getTo(),input.getMessage()));
					pointiterator.list.add(new PointEntry(input.getFrom(),"30.0","30.0"));
					if(check){
						theresp =new TWFMessage("register-success","","","");//"register-success";
						this.refreshUserPanel();
					}
					else {
						theresp=new TWFMessage("register-failed2","","","");//"register-failed2";
					}
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("allnames")){
				String s= "";
				for(TWFUser u:userlist){
					if(u.username.equalsIgnoreCase(input.getFrom())){
						continue;
					}
					s = s+u.username+"@";
				}
				s = s.substring(0, s.length()-2);
				theresp = new TWFMessage("allnamesresult","","",s);
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("search")){
				String[] temp = searchName(input.getFrom());
				
				if(temp[0].equalsIgnoreCase("@@@")){
					theresp=new TWFMessage("search-failed","","","");//"search-failed";
				}
				else {
					//theresp="search";
					String results="search";
					for(int i=0;i<temp.length;i++){
						results=results+"-"+temp[i];
					}
					theresp = new TWFMessage("search","","",results);
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("setlocation")){ //allagh thesis xristi
				pointiterator.setLatLong(input.getFrom(), input.getTo(), input.getMessage());
				theresp = new TWFMessage("locationupdated","","","");
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("getlocation")){ //eyresi thesis toy xristi "to"
				String stemp =pointiterator.getLatLong(input.getTo());
				if(stemp==null){
					theresp= new TWFMessage("getlocation-error","","","");
				}
				else{
					theresp = new TWFMessage("getlocation-success","","",stemp);
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("getuserswithdistance")){ //evresi thesis twn kontinwn xristwn
				ArrayList<String> temp =pointiterator.getNearby(input.getFrom(),Integer.parseInt(input.getMessage()));
				if(temp==null){
					theresp = new TWFMessage("getuserswithdistance-empty","","","");
				}
				else{
					theresp = new TWFMessage("getuserswithdistance-success","","",temp.toString());
				}
			}
			else if(input.getHeader().equalsIgnoreCase("keysearch")){
				String[] temp = searchKeys(input.getFrom());
				if(temp[0].equalsIgnoreCase("@@@")){
					theresp=new TWFMessage("keysearch-failed","","","");//"keysearch-failed";
				}
				else {
					//theresp="keysearch";
					String results ="keysearch";
					for(int i=0;i<temp.length;i++){
						results=results+"-"+temp[i];
					}
					theresp = new TWFMessage("keysearch","","",results);
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("chating")){
				chatNormalMessage(input.getTo(),input.getFrom(),input.getMessage());
				theresp = null;
			}
			else if(input.getHeader().equalsIgnoreCase("removefriend")){
				if(userlist.get(nameToId(input.getFrom())).active){
					boolean what = userlist.get(nameToId(input.getFrom())).removefriend(userlist.get(nameToId(input.getTo())));
					if(what){
						theresp=new TWFMessage("removecomplete",input.getTo(),"","");//"removecomplete-"+keys[2];
					}
					else {
						theresp=new TWFMessage("removefailed","","","");//"removefailed";
					}
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			else if(input.getHeader().equalsIgnoreCase("addfriend")){
				if(userlist.get(nameToId(input.getFrom())).active){
					boolean what =userlist.get(nameToId(input.getFrom())).addfriend(userlist.get(nameToId(input.getTo())));
					if(what){
						theresp=new TWFMessage("addcomplete",input.getTo(),userlist.get(nameToId(input.getTo())).getMsg(),""+userlist.get(nameToId(input.getTo())).active);//"addcomplete-"+keys[2]+"-"+userlist[nameToId(keys[2])].getMsg()+"-"+userlist[nameToId(keys[2])].active;;
					}
					else {
						theresp=new TWFMessage("addfailed","","","");//"addfailed";
					}
					theresp.addIpAndPort(input.s, input.port);
				}
			}
			else if(input.getHeader().equalsIgnoreCase("chattype")){
				chatTypeMsg(input.getTo(),input.getFrom());
					theresp = null;
			}
			else if(input.getHeader().equalsIgnoreCase("data")){
				if(input.getTo().equalsIgnoreCase("flist")){
					int key = nameToId(input.getFrom());

					if(userlist.get(key).active){
						ArrayList<TWFUser> temp=userlist.get(key).flist;
						int counter =temp.size();
						//for(int i=0;i<temp.size();i++){
							//if(temp[i]==null){
								//break;
							//}
							//counter=counter+1;
						//}
						if(counter==0){
							theresp=new TWFMessage("data-emptyflist","","","");
						}
						else {
							String result="";
							int i=0;
							for(i=0;i<temp.size()-1;i++){
									result=result+temp.get(i).username+"-";
								
							}
							if(temp.get(i)!=null){
							result=result+temp.get(i).username;
							}
							theresp=new TWFMessage("data-flist","","",result);
						}		
					}
					else{
						theresp= new TWFMessage("data-flist-error","","","");
					}
				}
				else if(input.getTo().equalsIgnoreCase("history")){
					int key = nameToId(input.getFrom());
					if(userlist.get(key).active){
						if(userlist.get(key).hasFriend(input.getMessage())){
							ArrayList<String> temp = userlist.get(nameToId(input.getMessage())).history;
							ArrayList<String> tempor = userlist.get(nameToId(input.getMessage())).historydate;
							String results="data-history-"+input.getMessage();
							for(int i=0;i<temp.size();i++){
								results = results+"-"+tempor.get(i)+"@"+temp.get(i);
							}
							theresp=new TWFMessage("data-history",input.getMessage(),"",results);
						}
						else {
							theresp=new TWFMessage("data-history-error","","","");
						}
					}
					else{
						theresp=new TWFMessage("data-error","","","");
					}
				}
				else if(input.getTo().equalsIgnoreCase("friend")){
					int key = nameToId(input.getFrom());
					if(userlist.get(key).active){
						if(userlist.get(key).hasFriend(input.getMessage())){
							String activestate;
							if(userlist.get(nameToId(input.getMessage())).active){
								activestate="online";
							}
							else {
								activestate="offline";
							}
							theresp=new TWFMessage("data-friend",input.getMessage(),userlist.get(nameToId(input.getMessage())).getMsg(),activestate);
						}
						else{
							theresp=new TWFMessage("data-friend-error","","","");
						}
					}
					else{
						theresp=new TWFMessage("data-error","","","");
					}
				}
				else if(input.getTo().equalsIgnoreCase("sentence")){
					int key=nameToId(input.getFrom());
					if(userlist.get(key).active){
						boolean answer =userlist.get(key).newSentence(input.getMessage());
						if(answer){
						theresp=new TWFMessage("data-sentencerefreshed",userlist.get(key).getMsg(),"","");
						broadcastMessage(userlist.get(key));
						}
						else
						theresp=new TWFMessage("data-sentenceerror2","","","");
					}
					else {
						theresp=new TWFMessage("data-sentenceerror","","","");
					}
				}
				theresp.addIpAndPort(input.s, input.port);
			}
			if(theresp!=null){
				log.append("\n"+time+" ServerMSG: "+theresp.getSynopsis());
			}
			//System.out.println("inside");
			return theresp;
		}
	public TWFMessage decideFTP(TWFMessage input){ //Decider model gia FTP server
		date=new Date();
		String time=litedateFormat.format(date);

		TWFMessage theresp = null;
		logftp.append("\n"+time+"UserMsG: "+input.getSynopsis());
		if(input.getHeader().equalsIgnoreCase("ftprequest")){
			if(userlist.get(nameToId(input.getTo())).active){
				sendFTPRequest(input);
			}
			else {
				theresp = new TWFMessage("ftprequesterror","","","");
				theresp.addIpAndPort(input.s, input.port);
			}
		}
		else if(input.getHeader().equalsIgnoreCase("ftprequestaccept")){
			if(userlist.get(nameToId(input.getTo())).active){
				sendFTPAcceptRequest(input,true);
			}
			else{
				theresp= new TWFMessage("ftprequestaccepterror","","","");
				theresp.addIpAndPort(input.s, input.port);
			}
		}
		else if(input.getHeader().equalsIgnoreCase("ftprequestdecline")){
			if(userlist.get(nameToId(input.getTo())).active){
				sendFTPAcceptRequest(input,false);
			}
			else{
				theresp = new TWFMessage("ftprequestdeclineerror","","","");
				theresp.addIpAndPort(input.s, input.port);
			}
		}
		else if(input.getHeader().equalsIgnoreCase("ftpstoprequest")){
			if(userlist.get(nameToId(input.getTo())).active){
				sendFTPStopRequest(input);
			}
		}
		else if(input.getHeader().equalsIgnoreCase("ftpfragment")) {
			if(userlist.get(nameToId(input.getTo())).active){
				forwardFTPFragment(input);
			}
			else {
				//Tipota
			}
		}
		else if(input.getHeader().equalsIgnoreCase("ftpack")){
			if(userlist.get(nameToId(input.getTo())).active){
				forwardFTPAck(input);
			}
			else{
				//Tipota
			}
		}
		if(theresp!=null){
			logftp.append("\n"+time+" ServerMSG: "+theresp.getSynopsis());
		}
		return theresp;
	}
	public TWFMessage decideGame(TWFMessage input){ //Decider model gia GAME server
		TWFMessage theresp = null;
		return theresp;
	}
	public void broadcastMessage(TWFUser a){ //Stelnei ena update minima se kathe user poy exei friend ton friend poy molis ananewse tin katastasi toy
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				return;
			}
			if(userlist.get(i).hasFriend(a.username)){
				if(userlist.get(i).active){
					try{
						synchronized(thethread){
							sendMessage(new TWFMessage("update",a.username,"",a.getMsg()),recsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}	
	}
	public void broadcastOnline(TWFUser a){
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				return;
			}
			if(userlist.get(i).hasFriend(a.username)){
				if(userlist.get(i).active){
					try {
						synchronized(thethread){
							sendMessage(new TWFMessage("online",a.username,"",""),recsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void broadcastOffline(TWFUser a){
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				return;
			}
			if(userlist.get(i).hasFriend(a.username)){
				if(userlist.get(i).active){
					try {

						synchronized(thethread){
							sendMessage(new TWFMessage("offline",a.username,"",""),recsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void chatTypeMsg(String to,String from){
		int i = nameToId(to);
		try {
			synchronized(thethread){
				sendMessage(new TWFMessage("typing...",from,"",""),recsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void chatNormalMessage(String to,String from,String msg){
		int i = nameToId(to);
		try {
			synchronized(thethread){
				sendMessage(new TWFMessage("chatmsg",from,"",msg),recsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendFTPRequest(TWFMessage input){
		int i = nameToId(input.getTo());
		String[] f = input.getMessage().split("@@@@");
		try{
			synchronized(theftpthread){
				sendMessage(new TWFMessage("ftprequest",input.getFrom(),f[0],f[1]),recftpsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendFTPAcceptRequest(TWFMessage input, boolean how){
		int i = nameToId(input.getTo());
		if(how){
			try{
				synchronized(theftpthread){
					sendMessage(new TWFMessage("ftprequestaccept",input.getFrom(),"",input.getMessage()),recftpsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			try{
				synchronized(theftpthread){
					sendMessage(new TWFMessage("ftprequestdecline",input.getFrom(),"",input.getMessage()),recftpsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void sendFTPStopRequest(TWFMessage input){
		int i = nameToId(input.getTo());
		try{
			synchronized(theftpthread){
				sendMessage(new TWFMessage("ftpstoprequest",input.getFrom(),"",input.getMessage()),recftpsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void forwardFTPFragment(TWFMessage input){
		int i = nameToId(input.getTo());
		try{
			synchronized(theftpthread){
				sendMessage(input,recftpsocket,InetAddress.getByName(userlist.get(i).ip),userlist.get(i).portdest);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void forwardFTPAck(TWFMessage input){
		int i=nameToId(input.getTo());
		try{
			synchronized(theftpthread){
				sendMessage(input,recftpsocket,InetAddress.getByName(userlist.get(i).ip),Integer.parseInt(userlist.get(i).port));//portdest+1);//TODO problima me porta
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
			
		
	}
	public boolean attemptLogin(String username,String userpass){ //prospathia login kai energopoiisi active state gia to xristi
		int a = nameToId(username);
		if(a==-1){
			return false;
		}
		else{
			boolean flag2 =userlist.get(a).checkPass(userpass);
			if(flag2){
				userlist.get(a).setActive(true);
				return true;
			}
			else{
				return false;
			}
		}
	}
	public int nameToId(String name){ // Name to id (int) resolution
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				break;
				}
			if(userlist.get(i).username.equalsIgnoreCase(name)){
				return i;
			}
		}
		return -1;
	}
	public void sendMessage(TWFMessage mes,DatagramSocket sock,InetAddress to,int toport){ //Methodos poy apostellei ObjectBased mhnyma stin address to kai port toport
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(mes);		
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,to, toport );
		    sock.send(sendPacket);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public String[] searchName(String name){ //Epistrefei array me String poy na tairiazoyn me to search key
		int counter =0;
		
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				break;
			}
			if(userlist.get(i).username.indexOf(name)!=-1){
				counter=counter+1;
			}
		}

		if(counter==0){
			String[] error = new String[1];
			error[0] = "@@@";
			return error;
		}
		else {
			
			String[] temp = new String[counter];
			counter=0;
			for(int i=0;i<userlist.size();i++){
				if(userlist.get(i)==null||counter==temp.length){
					break;
				}
				if(userlist.get(i).username.indexOf(name)!=-1){
					temp[counter]=userlist.get(i).username;
					counter=counter+1;
				}
			}
			return temp;
		}
	}
	public String[] searchKeys(String thekey){
		String[] result= new String[100];
		result[0] = "@@@";
		int counter = 0;
		for(int i=0;i<userlist.size();i++){
			if(userlist.get(i)==null){
				break;
			}
			ArrayList<String> temp2 = userlist.get(i).history;
			for(int j=0;j<temp2.size();j++){
				int isit = temp2.get(j).indexOf(thekey);
				if( isit!=-1){
					result[counter]= userlist.get(i).username+": "+temp2.get(j);
					counter = counter +1;
				}
			}
		}
		if(counter==0){
		return result;
		}
		else {
			int k;
			for (k=0;k<result.length;k++){
				if(result[k]==null){
					break;
				}
			}
			String[] result2 = new String[k];
			for( k=0;k<result2.length;k++){
				result2[k] = result[k];
			}
			return result2;
		}
	}
	public static void main(String[] args){ //Main
		@SuppressWarnings("unused")
		TWFServer thehost = new TWFServer();
	}
}
