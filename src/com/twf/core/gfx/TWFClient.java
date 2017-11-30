package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;


import com.example.twfportable.ForumPost;
import com.example.twfportable.TWFMessage;
import com.google.common.io.ByteStreams;
import com.twf.core.State;
import com.twf.core.TWFUser;
import com.twf.core.gfx.ext.JXMapKit;
import com.twf.core.gfx.ext.WrapLayout;
import com.twf.core.threads.FTPReceiveThread;
import com.twf.core.threads.FTPSendThread;
import com.twf.core.threads.UpdateThread;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class TWFClient {  //client toy Whispering Forest

	// GUI login 
	public JFrame loginframe;
	public JTextArea loginuser;
	public JPasswordField loginpass;
	public JLabel userlab,passlab,pan,title;
	public JButton loginbutt,registerbutt;
	public JMenuBar loginmenu;
	public ImageIcon logintree;
	public int xsize=211;
	public int ysize=365;
	public JCheckBox check;
	public JLabel checklab;
	
	//GUI Main
	public JFrame mainframe;
	public JButton updatebutt;
	public JPanel friendlistpan;
	public ImageIcon mainforest;
	public JLabel background;//JPanel background;
	public int xmainsize=1000;
	public int ymainsize=800;
	public TWFUserpanel userpan;
	public JButton mute;
	public ImageIcon mute1,mute2;
	public ButtonsPanel buttonspan;
	
	//GUI settings
	public JFrame setframe;
	public JTextArea set1,set2,set3; // portdest,portfrom,ip
	public JLabel lab1,lab2,lab3; 
	public JButton setdefault,setapply;
	public int setx=300;
	public int sety=300;
	
	//GUI sound settings
	public JFrame set2frame;
	public JCheckBox wantsound,wantftpcheck;
	public JLabel soundlab,ftplab;
	public JButton soundapply;
	public int set2x=180;
	public int set2y=300;
	
	//GUI register
	public JFrame regframe;
	public JTextArea reg1,reg2,reg3,reg4,reg5;
	public JLabel labr1,labr2,labr3,labr4,labr5;
	public JButton regbutton;
	public int regx=260;
	public int regy=300;
	public ImageIcon regicon1,regicon2;
	
	//GUI search
	public TWFSearchpanel search2;
	public JFrame searchgui;
	public JPanel resultcon;

	public int searchchoice; 
	
	//GUI history
	public JFrame historygui;
	public JTextArea historytext;
	public JLabel historylab;
	public JScrollPane historyscroll;
	
	//GUI chat
	ChatFrame[] chatframe;
	
	//GUI forum
	public JFrame forumgui;
	public JButton getforums,gettopics,getposts,goback,reply,createtopic;
	public ForumPanel[] forumbuttons;
	public TopicPanel[] topicbuttons;
	public JPanel forumpan1,forumpan2;
	public JScrollPane forumscroll;
	public Container forumcont;
	public ImageIcon forumback,backicon,newicon;
	public JLabel forumlab;
	public JTextArea forumtext;
	
	//System variables
	public int width,height;
	public String username,userpass;
	public DatagramSocket clientSocket,updateSocket;
	public InetAddress IPAddress;
	public int port=9999;
	public int portdest=10000;
	public int portftpdest=10001;
	public int portgamedest=10002;
	public String server = "127.0.0.1";//"192.168.2.3";
	public TWFUser theuser;
	public String[] friendlist;
	public TWFPanel[] theuserflistpanel = new TWFPanel[100];
	public boolean loadpreferences=false;
	public State theprogramstate;
	public boolean soundflag;
	public boolean wantftp=true;
	public UpdateThread thread;
	
	//FTP variables
	private FTPSendThread ftpsendthreads;//= new FTPSendThread[5];
	private FTPReceiveThread ftpreceivethreads;// = new FTPReceiveThread[5];
	
	//Music variables
	public Clip clip;
	
	
	public TWFClient() {  //constructor
		//Synthetica
		
		try {
		     UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		    } 
		catch (Exception e) 
		  {
		    e.printStackTrace();
		  }
		  
		// ~Synthetica
		width = Toolkit.getDefaultToolkit().getScreenSize().width;
		height = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		// Elegxos gia arxeio save.ser
		File file = new File("save.ser");
		
		if(file.exists()){
			loadpreferences=true;
		}
		
		loginWindow();
	}	
	public void loginWindow() { //Dimioyrgia Login Form
	
		logintree = new ImageIcon(getClass().getResource("images/tree-icon.png"));  //DHMIOYRGIA / SETARISMA TWN COMPONENT
		
		loginframe = new JFrame();
		loginframe.setTitle("THE WHISPERING FOREST");    //SETARISMA TOY LOGINFRAME
		loginframe.getContentPane().setBackground(new Color(0,139,0));
		loginframe.setLayout(new FlowLayout());
		loginframe.setLocation((width-xsize)/2,((height-ysize)/2));
		loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginframe.setIconImage(logintree.getImage());
		
		
		
		pan = new JLabel(logintree);
		
		title = new JLabel("The Whispering Forest");
		title.setFont(new Font("Book Antiqua",Font.BOLD,18));
		title.setForeground(new Color(0,190,0));
		
		userlab = new JLabel("Username:");
		passlab = new JLabel("Password:");
		loginpass = new JPasswordField(9);
		loginpass.getInputMap().put(KeyStroke.getKeyStroke("TAB"),"doNothing");
		loginpass.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_TAB){
					loginbutt.requestFocus();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		loginuser = new JTextArea(1,10);
		loginuser.getInputMap().put(KeyStroke.getKeyStroke("TAB"),"doNothing");
		loginuser.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_TAB){
					loginpass.requestFocus();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		
		check = new JCheckBox("remember me");
		loginmenu = new JMenuBar();
		JMenu fileMenu = new JMenu("Options..");
		JMenuItem theitem = new JMenuItem("network");
		theitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				settingsWindow();
			}
			
		});
		fileMenu.add(theitem);
		loginmenu.add(fileMenu);
		JMenuItem theitem2 = new JMenuItem("sound");
		theitem2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				settingsWindow2();
			}
			
		});
		fileMenu.add(theitem2);
		loginmenu.setBackground(new Color(34,139,34));
		
		loginbutt = new JButton("Login");
		loginbutt.setBackground(new Color(0,143,0));
		loginbutt.setOpaque(true);
		loginbutt.addActionListener(new ActionListener(){// Action Listener gia to Login

			@Override
			public void actionPerformed(ActionEvent e) { 
				try{
				username = loginuser.getText();
				userpass = new String(loginpass.getPassword());
				login();
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "error in input");
				}
			}
			
		});
		loginbutt.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(loginbutt.hasFocus()){
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						try{
							username = loginuser.getText();
							userpass = new String(loginpass.getPassword());
							login();
							}catch(Exception ex){
								JOptionPane.showMessageDialog(null, "error in input");
							}
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		
		registerbutt = new JButton("Register");
		registerbutt.setBackground(new Color(0,143,0));
		registerbutt.setOpaque(true);
		registerbutt.addActionListener(new ActionListener(){ //Action Listener gia to Register

			@Override
			public void actionPerformed(ActionEvent arg0) {
				registerWindow();
			}
			
		});
		
		JPanel tempan = new JPanel(new FlowLayout());
		tempan.setBorder(null);
		tempan.setOpaque(false);
		tempan.add(loginbutt);
		tempan.add(registerbutt);
		
		loginframe.setJMenuBar(loginmenu);   //TOPOTHETISI TWN COMPONENT STO CONTAINER
		loginframe.add(title);            
		loginframe.add(pan);
		loginframe.add(userlab);
		loginframe.add(loginuser);
		loginframe.add(passlab);
		loginframe.add(loginpass);
		loginframe.add(check);
		loginframe.add(tempan);
		loginframe.setSize(xsize,ysize);
		loginframe.setResizable(false);
		loginframe.setVisible(true);

		//kommati to opoio syblirwnei aytomata kapoia pedia
		if(loadpreferences){
			readInitializationFile();
			loginuser.setText(this.theprogramstate.username);
			loginpass.setText(this.theprogramstate.password);
			try{
				IPAddress = InetAddress.getByName(theprogramstate.ip);
				port = Integer.parseInt(theprogramstate.port1);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(theprogramstate.autologin){
				check.setSelected(true);
			}
			soundflag=theprogramstate.sound;
		}
		music("songoftime.wav");
	}
	public void mainWindow(){	//Dimioyrgia basikoy parathiroy
		
		mainforest = new ImageIcon(getClass().getResource("images/forest.jpg"));
		mainframe = new JFrame();
		mainframe.setVisible(true);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setLocation((width-mainforest.getIconWidth())/2,(height-mainforest.getIconHeight())/2);
		mainframe.setIconImage(logintree.getImage());
		mainframe.setTitle("Welcome to the Whispering Forest");		
		
		mainframe.setLayout(null);
		background = new JLabel(mainforest);
		background.setOpaque(false);
        background.setBounds(0,0,mainforest.getIconWidth(),mainforest.getIconHeight());
       
        Container pane = mainframe.getContentPane();
        pane.add(background);
        background.setLayout(null);
        mainframe.setSize(mainforest.getIconWidth(),mainforest.getIconHeight());
        mainframe.setResizable(false);
        
        //Custom Cursor
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = new ImageIcon(getClass().getResource("images/ocarina.png")).getImage();
        Cursor c = toolkit.createCustomCursor(image , new Point(mainframe.getContentPane().getX(),mainframe.getContentPane().getY()), "img");
        mainframe.getContentPane().setCursor(c);
        
        //Window Listener gia logout
        mainframe.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {}

			@Override
			public void windowClosing(WindowEvent arg0) {
			
				//attemptLogout();
				forceExit();
				createInitializationFile();
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
		mute1 = new ImageIcon(getClass().getResource("images/mute.png"));
		mute2 = new ImageIcon(getClass().getResource("images/unmute.png"));
		ImageIcon globe = new ImageIcon(getClass().getResource("images/globe.png"));
		JButton globebutton = new JButton(globe);
		globebutton.setToolTipText("Press this to select your map location");
		globebutton.setSize(new Dimension(50,50));
		//globebutton.setOpaque(false);
		globebutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openLocationSelectionFrame();
			}
			
		});
		globebutton.setLocation(background.getWidth()-110,background.getHeight()-90);
		globebutton.setContentAreaFilled(false);
        mute = new JButton(mute2);
        mute.addActionListener(new ActionListener(){
        	int a=1;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(a==1){
				mute.setIcon(mute1);
				a=2;
				mute();
				}
				else if(a==2){
					mute.setIcon(mute2);
					a=1;
					unmute();
				}
			}
        	
        });
        mute.setSize(new Dimension(30,30));
        mute.setLocation(background.getWidth()-60,background.getHeight()-80);
        background.add(mute);
        background.add(globebutton);
		//Userpanel
		userpan = new TWFUserpanel(this.theuser);
		userpan.setBounds((background.getWidth()-userpan.getWidth())/2,0,userpan.getWidth(),userpan.getHeight());
		
		//background.add(search);		
		background.add(userpan);
		
		//Panel me koybia elegxoy 
		buttonspan = new ButtonsPanel(this);
		buttonspan.setBounds((background.getWidth()-buttonspan.getWidth())/2,background.getHeight()-buttonspan.getHeight()-20,buttonspan.getWidth(),buttonspan.getHeight());
		
		background.add(buttonspan);
		
		updateFriendlist();
		if(clip!=null){
		clip.stop();
		}
		music("departure.wav");
	}
	public void settingsWindow(){  //Dimioyrgia parathiroy settings
		setframe = new JFrame();
		setframe.setSize(setx,sety);
		setframe.setTitle("Network Settings");
		setframe.setLocation((width-setx)/2,(height-sety)/2);
		setframe.setLayout(new FlowLayout());
		setframe.setResizable(false);
		setframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel setpan = new JPanel(new GridLayout(0,2,5,20));
		set1 = new JTextArea();
		set1.setBorder(BorderFactory.createEtchedBorder());
		set1.setText(server);
		set1.setSelectionStart(0);
		set1.setSelectionEnd(set1.getText().length());
		set2 = new JTextArea();	
		set2.setBorder(BorderFactory.createEtchedBorder());
		set2.setText(Integer.toString(portdest));
		set3 = new JTextArea();
		set3.setText(Integer.toString(port));
		set3.setBorder(BorderFactory.createEtchedBorder());
		lab1 = new JLabel("Destination IP:");
		lab2 = new JLabel("Destination port:");
		lab3 = new JLabel("Local port:");
		setpan.add(lab1);
		setpan.add(set1);
		setpan.add(lab2);
		setpan.add(set2);
		setpan.add(lab3);
		setpan.add(set3);
		setdefault = new JButton("Defaults");
		setdefault.addActionListener(new ActionListener(){ //Koybi default settings

			@Override
			public void actionPerformed(ActionEvent arg0) {
				set1.setText(server);
				set2.setText("10000");
				set3.setText("9999");	
			}
			
		});
		setapply = new JButton("Apply");
		setapply.addActionListener(new ActionListener(){ //Koybi apply settings

			@Override
			public void actionPerformed(ActionEvent e) {
				int a=-1;
				int b=-1;
				boolean flag=true;
				try {
					a = Integer.parseInt(set2.getText());
					b = Integer.parseInt(set3.getText());
				}catch(Exception f){
					flag = false;
				}
				String input = set1.getText();
				String[] temp = input.split("\\.");
				if(temp.length!=4){
					System.out.println("2");
					flag=false;
				}
				if(flag){
					server=set1.getText();
					try{
					IPAddress = InetAddress.getByName(server);
					}catch(Exception o){
						JOptionPane.showMessageDialog(null, "error with IP");
					}
					portdest = a;
					port = b;
					portftpdest = a+1;
					portgamedest = a+2;
					setframe.dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "error with input");
				}
				
			}
			
		});
		setapply.setSize(50,20);
		setdefault.setSize(50,20);
		setpan.add(setdefault);
		setpan.add(setapply);
		setframe.add(setpan);
		setframe.setVisible(true);
		
		//kommati aytomatis syblirwsis
		if(loadpreferences){
			set1.setText(this.theprogramstate.ip);
			set3.setText(this.theprogramstate.port1);
			set2.setText(this.theprogramstate.port2);
		}
	}
	public void settingsWindow2(){ //Dimioyrgia parathiroy gia settings hxoy k.a.
		set2frame = new JFrame();
		set2frame.setSize(set2x,set2y);
		set2frame.setLocationRelativeTo(null);
		set2frame.setResizable(false);
		set2frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container cont = set2frame.getContentPane();
		cont.setLayout(new FlowLayout());
		
		soundlab = new JLabel("Enable Sounds :");
		wantsound = new JCheckBox();
		wantsound.setSelected(true);
		ftplab = new JLabel("Enable FTP :");
		wantftpcheck = new JCheckBox();
		wantftpcheck.setSelected(true);
		soundapply = new JButton("apply");
		soundapply.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//if(wantsound.isSelected()){
					//soundflag=true;
			//	}
				//else {
					//soundflag=false;
				//}
				soundflag=wantsound.isSelected();
				wantftp = wantftpcheck.isSelected();
				set2frame.dispose();
			}
			
		});
		cont.add(soundlab);
		cont.add(wantsound);
		cont.add(ftplab);
		cont.add(wantftpcheck);
		cont.add(soundapply);
		
		if(loadpreferences){
			wantsound.setSelected(theprogramstate.sound);
			wantftpcheck.setSelected(theprogramstate.ftpenabled);
		}
		
		set2frame.setVisible(true);
	}
	public void registerWindow(){  //Dimioyrgia parathiroy register
		
		regframe = new JFrame();
		regframe.setTitle("Register:");
		regframe.setSize(regx,regy);
		regframe.setLocation((width-regx)/2,(height-regy)/2);
		regframe.setLayout(new FlowLayout());
		regframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		regframe.setResizable(false);
		
		labr1 = new JLabel("Username:");			//LABELS
		labr2 = new JLabel("Password:");
		labr3 = new JLabel("Retype Password:");
		labr4 = new JLabel("Whisper:");
		
		reg1 = new JTextArea();					//TextArea
		reg2 = new JTextArea();
		reg3 = new JTextArea();
		reg4 = new JTextArea();
		regicon1 = new ImageIcon(getClass().getResource("images/reg1.png"));  //Icons
		regicon2 = new ImageIcon(getClass().getResource("images/reg2.png"));
		
		JScrollPane sreg4 = new JScrollPane(reg4);
		regbutton = new JButton("Register:");			//button
		regbutton.addActionListener(new ActionListener(){ //actionlistener gia register button

			@Override
			public void actionPerformed(ActionEvent arg0) {
				register();
			}
			
		});
		JPanel regpan = new JPanel(new GridLayout(0,2,5,20));
		regpan.add(labr1);
		regpan.add(reg1);
		regpan.add(labr2);
		regpan.add(reg2);
		regpan.add(labr3);
		regpan.add(reg3);
		regpan.add(labr4);
		regpan.add(sreg4);
		
		regframe.add(regpan);
		regframe.add(regbutton);
		regframe.setVisible(true);
		
		
	}
	public void addFriendWindow(){ //Dimioyrgia parathiroy addFriend
		searchgui = new JFrame();
		searchgui.setLayout(new FlowLayout());
		searchgui.setSize(600,400);
		searchgui.setLocation((width-searchgui.getWidth())/2,(height-searchgui.getHeight())/2);
		search2 = new TWFSearchpanel(this);
		ImageIcon note = new ImageIcon(getClass().getResource("images/note.png"));
		searchgui.setIconImage(note.getImage());
		searchgui.setTitle("Searching for Whispering Trees");
		resultcon = new JPanel();
		resultcon.setLayout(new BoxLayout(resultcon,BoxLayout.Y_AXIS));
		//resultcon.setSize(400,200);
		JScrollPane scroll = new JScrollPane(resultcon);
		scroll.setPreferredSize(new Dimension(400,200));
		searchgui.setVisible(true);
		searchgui.add(search2);
		//searchgui.add(resultcon);
		searchgui.add(scroll);
	}
	public void historyWindow(String name){ //Dimioyrgia parathiroy history
		historygui = new JFrame();
		historygui.setTitle(name+"'s History!");
		ImageIcon scroll = new ImageIcon(getClass().getResource("images/scroll.png"));
		JLabel scrollpan = new JLabel(scroll);
		historygui.setIconImage(scroll.getImage());
		historygui.setSize(300,400);
		historygui.setLocation((width-historygui.getWidth())/2,(height-historygui.getHeight())/2);
		historygui.setLayout(new FlowLayout());
		historytext = new JTextArea();
		historytext.setEditable(false);
		historylab = new JLabel("This is "+name+"'s History:");
		historyscroll = new JScrollPane(historytext);
		historyscroll.setPreferredSize(new Dimension(270,300));
		historygui.add(scrollpan);
		historygui.add(historylab);
		historygui.add(historyscroll);
		historygui.setVisible(true);
		attemptQueryHistory(name);
	}
	public void chatWindow(String name){ //Dimioyrgia parathiroy chat gia ton xrhsth $name
		chatframe[theuser.getFriendIndex(name)].setVisible(true);
	}
	public void openFTPDialog(String name,String fileinfo1,String fileinfo2){ //Anoigei to parathyro gia na soy dwthei arxeio apo xrhsth
		if(wantftp){
				
				JFrame ftpfram = new JFrame("Incoming File");
				ftpfram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				//ftpfram
				Container con =ftpfram.getContentPane();
				ftpfram.setSize(350,100);
				ftpfram.setLocationRelativeTo(null);
				con.setLayout(new FlowLayout());
				JLabel lab = new JLabel("From: "+name+" Name: "+fileinfo1+" Size:"+fileinfo2);
				lab.setForeground(Color.white);
				JButton accept = new JButton("Accept");
				accept.setName(name+"@@@@"+fileinfo1+"@@@@"+fileinfo2);
				accept.addActionListener(new ActionListener(){
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String[] temp = ((JButton)arg0.getSource()).getName().split("@@@@");
						boolean flag=true;
						do{
							sendMessage(new TWFMessage("ftprequestaccept",username,temp[0],temp[1]),clientSocket,IPAddress,portftpdest);
							TWFMessage m = receiveMessage(clientSocket);
							if(m!=null){
								if(m.getHeader().equalsIgnoreCase("ftpack")){
									flag=false;
								}
							}
						}while( flag);
						Component component = (Component) arg0.getSource();
						JFrame frame = (JFrame) SwingUtilities.getRoot(component); 
						
				        JProgressBar bar = new JProgressBar();
				        bar.setValue(0);
				        bar.setStringPainted(true);
				        bar.setString("0%");
				        bar.setPreferredSize(new Dimension(100,20));
				        JLabel lab = new JLabel("Waiting for file...");
				        frame.invalidate();
				        frame.getContentPane().removeAll();
				        
				        frame.getContentPane().add(bar);
				        frame.getContentPane().add(lab);
				        frame.validate();
				        ftpreceivethreads = new FTPReceiveThread(frame,temp[1],temp[0],Integer.parseInt(temp[2]));
					    ftpreceivethreads.start();
				        frame.pack();
				        frame.getContentPane().repaint();
				       
		        	    frame.addWindowListener(new WindowListener(){
	
									@Override
									public void windowActivated(WindowEvent e) {
	
									}
	
									@Override
									public void windowClosed(WindowEvent e) {
	
									}
	
									@Override
									public void windowClosing(WindowEvent e) {
										ftpreceivethreads.setWorking(false);	
										ftpreceivethreads=null;
									}
	
									@Override
									public void windowDeactivated(WindowEvent e) {}
	
									@Override
									public void windowDeiconified(WindowEvent e) {}
	
									@Override
									public void windowIconified(WindowEvent e) {}
	
									@Override
									public void windowOpened(WindowEvent e) {}
									
								});

				     
					}
					
				});
				JButton decline = new JButton("Decline");
				decline.setName(name);
				decline.addActionListener(new ActionListener(){
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						boolean flag=true;
						do{
							sendMessage(new TWFMessage("ftprequestdecline",username,((JButton)arg0.getSource()).getName(),""),clientSocket,IPAddress,portftpdest);
							TWFMessage m = receiveMessage(clientSocket);
							if(m!=null){
								if(m.getHeader().equalsIgnoreCase("ftpack")){
									flag=false;
								}
							}
						} while(flag);
						Component component = (Component) arg0.getSource();
				        JFrame frame = (JFrame) SwingUtilities.getRoot(component);
				        frame.dispose();
					}
					
				});
				con.add(lab);
				con.add(accept);
				con.add(decline);
				ftpfram.pack();
				ftpfram.setVisible(true);
		}
		else {
			boolean flag=true;
			do{
				sendMessage(new TWFMessage("ftprequestdecline",username,name,fileinfo1),clientSocket,IPAddress,portftpdest);
				TWFMessage m = receiveMessage(clientSocket);
				if(m!=null){
					if(m.getHeader().equalsIgnoreCase("ftpack")){
						flag=false;
					}
				}
			} while(flag);
		}
	}
	public void forumWindow(){ //Anoigei to parathyro gia to browsing twn forums
		forumback = new ImageIcon(getClass().getResource("images/forumback.png"));
		backicon = new ImageIcon(getClass().getResource("images/back.png"));
		newicon = new ImageIcon(getClass().getResource("images/new.png"));
		forumgui = new JFrame("This is The Whispering Forest Forum");
		forumgui.setIconImage(buttonspan.berry.getImage());
		forumlab = new JLabel(forumback);
		forumcont = forumgui.getContentPane();
		forumgui.setSize(640,480);
		forumlab.setLayout(new FlowLayout(FlowLayout.CENTER));
		forumgui.setLocationRelativeTo(null);

		getforums = new JButton("Back");
		getforums.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				queryForums();
			}
			
		});
		forumcont.add(forumlab);
		queryForums();
		forumgui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		forumgui.setVisible(true);
		
	}
	public void attemptQueryHistory(String name){ //Prospathia gia apoktisi toy istorikoy toy xristi name
			sendMessage(new TWFMessage("data",theuser.username,"history",name),clientSocket,IPAddress,portdest);
			TWFMessage msg = receiveMessage(clientSocket);

		    if(msg.getHeader().equalsIgnoreCase("data-history-error")){
		    	JOptionPane.showMessageDialog(null, "data history error");
		    }
		    else if(msg.getHeader().equalsIgnoreCase("data-error")){
		    	JOptionPane.showMessageDialog(null,"data error");
		    }
		    else {
		    	String[] temp = msg.getMessage().split("-");
		    	for(int i=3;i<temp.length;i++){
		    		historytext.append(temp[i]+"\n");
		    	}
		    }
	}
	public void attemptSearch(){ //Prospatheia gia findFriend
		if(searchchoice==0){
		
				sendMessage(new TWFMessage("search",search2.getKey(),"",""),clientSocket,IPAddress,portdest);
				TWFMessage msg = receiveMessage(clientSocket);

			    if(msg.getHeader().equalsIgnoreCase("search-failed")){
			    	JOptionPane.showMessageDialog(null,"No matches found");
			    }
			    else {
			    	String[] temp = msg.getMessage().split("-");
			    	ResultsPanel thepan = new ResultsPanel(temp,this);
			    	resultcon.add(thepan);
			    	resultcon.repaint();
			    	searchgui.repaint();
			    	searchgui.validate();
			    }
		
		}
		else {
		
				sendMessage(new TWFMessage("keysearch",search2.getKey(),"",""),clientSocket,IPAddress,portdest);
				TWFMessage msg = receiveMessage(clientSocket);

			    if(msg.getHeader().equalsIgnoreCase("keysearch-failed")){
			    	JOptionPane.showMessageDialog(null,"No matches found");
			    }
			    else {
			    	String[] temp = msg.getMessage().split("-");
			    	ResultsPanel thepan = new ResultsPanel(temp,this);
			    	resultcon.add(thepan);
			    	resultcon.repaint();
			    	searchgui.repaint();
			    	searchgui.validate();
			    }

		}
	}
	public boolean attemptAdd(String name){ // Prospatheia gia addfriend
		
			sendMessage(new TWFMessage("addfriend",theuser.username,name,""),clientSocket,IPAddress,portdest);
			TWFMessage msg = receiveMessage(clientSocket);
	
		    if(msg.getHeader().equalsIgnoreCase("addfailed")){
		    	JOptionPane.showMessageDialog(null,"Error adding friend(normal) "+name);
		    	return false;
		    }
		    else {
    			boolean how;
    			if(msg.getMessage().equalsIgnoreCase("true")){
    				how=true;
    			}
    			else {
    				how=false;
    			}
    			this.theuser.addfriend(new TWFUser(1,msg.getFrom(),msg.getTo(),how));
    			createPanels(theuser.flist);
    			mainframe.repaint();
		    	return true;
		    }	    
	}
	public void attemptRemove(TWFUser a){ //Prospatheia gia removefriend
	
			sendMessage(new TWFMessage("removefriend",theuser.username,a.username,""),clientSocket,IPAddress,portdest);
			TWFMessage msg = receiveMessage(clientSocket);
	
		    if(msg.getHeader().equalsIgnoreCase("removefailed")){
		    	JOptionPane.showMessageDialog(null, "Problem Removing Friend");
		    }
		    else {
		    	int i;
		    	for(i=0;i<this.theuser.flist.size();i++){
		    		if(theuser.flist.get(i).username.equalsIgnoreCase(msg.getFrom())){
		    			break;
		    		}
		    	}
		    	theuser.removefriend(theuser.flist.get(i));
		    	createPanels(theuser.flist);
		    	background.repaint();
		    }

	}
	public void attemptLogout(){ //Prospatheia gia logout

				sendMessage(new TWFMessage("logout",username,userpass,""),clientSocket,IPAddress,portdest);
				TWFMessage msg = receiveMessage(clientSocket);
			 
			    if(msg.getHeader().equalsIgnoreCase("logout-error")){
			    	JOptionPane.showMessageDialog(null, "Error with Logout");
			    }
			    else {
			    	JOptionPane.showMessageDialog(null, "Logging out! Thanks for using us :)");
			    	clientSocket.close();
			    	clientSocket=null;
			    	synchronized(thread){
			    		thread.setFlag(false);
			    	}
			    	updateSocket.close();
			    	updateSocket=null;
			    	IPAddress=null;
			    	username=null;
			    	userpass=null;
			    	theuser=null;
			    	friendlist=null;
			    	theuserflistpanel = new TWFPanel[100];
			    	mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			    	mainframe.dispose();
			    	if(clip!=null){
			    		clip.close();
			    	}
			    	loginWindow();
			    }
	}
	public void forceExit(){ //Forced Termination tis efarmogis
		sendMessage(new TWFMessage("logout",username,userpass,""),clientSocket,IPAddress,portdest);
		System.exit(0);
	}
	public void refreshWhisper(String thesen){// aitisi gia refresh whisper
		
				sendMessage(new TWFMessage("data",username,"sentence",thesen),clientSocket,IPAddress,portdest);
				TWFMessage msg = receiveMessage(clientSocket);
		
			    if(msg.getHeader().equalsIgnoreCase("data-sentenceerror2")){
			    	JOptionPane.showMessageDialog(null,"error in the function");
			    }
			    else if(msg.getHeader().equalsIgnoreCase("data-sentenceerror")){
			    	JOptionPane.showMessageDialog(null,"error in the activity");
			    }
			    else {
			    	
			    	if(msg.getHeader().equalsIgnoreCase("data-sentencerefreshed")){
			    		theuser.newSentence(msg.getFrom());
			    		String[] temporal = theuser.getMsg().split("@");
			    		userpan.sentence2.setText(temporal[1]);
			    	}
			    }
	}
	public void login(){ //Propsatheia gia login
	
		try {
			clientSocket = new DatagramSocket(port);
			clientSocket.setSoTimeout(3000);
			//IPAddress = InetAddress.getByName(server);

			sendMessage(new TWFMessage("login",username,userpass,""),clientSocket,IPAddress,portdest);
			TWFMessage msg = receiveMessage(clientSocket);
			 
		    if(msg.getHeader().equalsIgnoreCase("login-error")){
		    	JOptionPane.showMessageDialog(null, "Login Failed- Invalid Data");
		    	clientSocket.close();
		    }
		    else {	   
		    	theuser = new TWFUser(username,userpass,msg.getFrom());
		    	loginframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    	if(check.isSelected()){ 
		    		createInitializationFile();
		    	}
		    	else {
		    		deleteInitializationFile();
		    		this.loadpreferences =false;
		    	}
		    	loginframe.dispose();
		    	mainWindow();
		    	createUpdateThread();
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error connecting to Server!");
			clientSocket.close();
		}
		 
	}
	public void sendTypingMessage(String to){ //Methodos i opoia enhmerwnei ton 
			 sendMessage(new TWFMessage("chattype",username,to,""),clientSocket,IPAddress,portdest);
	}
	public void sendChatMessage(String to,String msg){//Methodos i opoia apostellei ena chat msg se enan xrhsth -target
			 sendMessage(new TWFMessage("chating",username,to,msg),clientSocket,IPAddress,portdest);
			 chatframe[theuser.getFriendIndex(to)].addCloud(new ChatCloud(username+":"+msg,0));
	}
	public void receiveChatMessage(String from,String msg){
	
		if(!chatframe[theuser.getFriendIndex(from)].isVisible()){
			chatframe[theuser.getFriendIndex(from)].setVisible(true);
			music("chatalert.wav");
		}
		chatframe[theuser.getFriendIndex(from)].addCloud(new ChatCloud(from+":"+msg,1));
	}
	public void sendFileTo(String s){ //Ekinhsh diadikasias metaforas arxeioy (FTP) me basi ta udp datagrams
		JFileChooser chooser = new JFileChooser();
		File f;
		String filename;
		int returnVal = chooser.showOpenDialog(mainframe);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		   f=chooser.getSelectedFile();
		   filename=f.getName();
		   try{
			   FileInputStream fin = new FileInputStream(f);
			   byte[] thebytes =ByteStreams.toByteArray(fin);
			   int a = thebytes.length;
			   int b = a/FTPSendThread.packetsize;
			   int c = a%FTPSendThread.packetsize;
			   if(c!=0){
				   b=b+1;
			   }
			   JOptionPane.showMessageDialog(null, "length:"+a+"\nframents: "+b+"\nlast package has:"+c+" bytes");
			   boolean flag=true;
			   do{
				   sendMessage(new TWFMessage("ftprequest",username,s,filename+"@@@@"+a),clientSocket,IPAddress,portftpdest);
				   TWFMessage m = this.receiveMessage(clientSocket);  
				   if(m!=null){
					   if(m.getHeader().equalsIgnoreCase("ftpack")){
						   flag=false;
					   }
				   }
			   }while(flag);
				   JFrame ftpprogressframe = new JFrame("Sending File...");
				   ftpprogressframe.setSize(350,100);
				   ftpprogressframe.setLocationRelativeTo(null);
				   ftpprogressframe.setResizable(false);
				   Container cont =ftpprogressframe.getContentPane();
				   cont.setLayout(new FlowLayout());
				   JProgressBar prog = new JProgressBar();
				   prog.setPreferredSize(new Dimension(100,20));
				   prog.setIndeterminate(true);
				   JLabel ftplabel = new JLabel("Waiting for user: "+s);
				   cont.add(prog);
				   cont.add(ftplabel);
				   ftpprogressframe.setVisible(true);
				   ftpprogressframe.setName(""+0);
				   ftpprogressframe.addWindowListener(new WindowListener(){

					@Override
					public void windowActivated(WindowEvent arg0) {}

					@Override
					public void windowClosed(WindowEvent arg0) {}

					@Override
					public void windowClosing(WindowEvent arg0) {
						//ftpsendthreads.forceQuit()
						//ftpsendthreads=null;
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
				   ftpsendthreads = new FTPSendThread(filename,s,this,thebytes,ftpprogressframe);
			   fin.close();
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		}
		else{
			f=null;
			JOptionPane.showMessageDialog(null,"No file selected");
		}
	}
	public void openLocationSelectionFrame(){ //Anoigei ena input dialog sto opoio epilegeis ti thesi soy
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JXMapKit kit = new JXMapKit();
                kit.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);
                
                
                final int max = 17;
                TileFactoryInfo info = new TileFactoryInfo(1,max-2,max,
                        256, true, true, // tile size is 256 and x/y orientation is normal
                        "http://tile.openstreetmap.org",//5/15/10.png",
                        "x","y","z") {
                    public String getTileUrl(int x, int y, int zoom) {
                        zoom = max-zoom;
                        String url = baseURL +"/"+zoom+"/"+x+"/"+y+".png";
                        return url;
                    }

                };
                TileFactory tf = new DefaultTileFactory(info);
                kit.setTileFactory(tf);
                kit.setZoom(14);
                String temp = getLocation();
                if(temp==null){
                kit.setAddressLocation(new GeoPosition(51.5,0));
                }
                else{
                	String[] bla = temp.split("@@@@");
                	kit.setAddressLocation(new GeoPosition(Double.parseDouble(bla[0]),Double.parseDouble(bla[1])));
                }
                
                //kit.getMainMap().setDrawTileBorders(true);
                kit.getMainMap().setRestrictOutsidePanning(true);
                kit.setMiniMapVisible(false);
                kit.getMainMap().addMouseListener(new MouseAdapter(){
                	@Override
					public void mousePressed(MouseEvent arg0) {
						kit.setAddressLocation(kit.getMainMap().convertPointToGeoPosition(arg0.getPoint()));
					}
                });
                ((DefaultTileFactory)kit.getMainMap().getTileFactory()).setThreadPoolSize(8);
                JFrame frame = new JFrame("Choose your Location in the map");
                frame.getContentPane().setBackground(new Color(46,139,87));
                frame.setLayout(new FlowLayout());
                kit.setPreferredSize(new Dimension(400,300));
                frame.add(kit);
                //frame.pack();
                frame.setSize(500,420);
                frame.setVisible(true);
                frame.setIconImage(new ImageIcon(getClass().getResource("images/globe.png")).getImage());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JButton butt = new JButton("Set Cords");
                butt.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog(null,kit.getMainMap().getAddressLocation().toString());
						setLocationUpdate(kit.getMainMap().getAddressLocation().getLatitude()+"",kit.getMainMap().getAddressLocation().getLongitude()+"");
					}
                	
                });
                frame.add(butt);
                frame.setLocationRelativeTo(null);
            }
        });
	}
	public void setLocationUpdate(String lon,String lat){ //topothetei ti nea thesi sti basi dedomenwn
		sendMessage(new TWFMessage("setlocation",username,lon,lat),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg==null){
			System.out.println("no packet response");
		}
		else if(msg.getHeader().equalsIgnoreCase("locationupdated")){
			JOptionPane.showMessageDialog(null, "Success!");
		}
		else {
			JOptionPane.showMessageDialog(null, "Failed!");
		}
	}
	public String getLocation(){ //fernei tin topothesia poy yparxei sti basi dedomenwn
		sendMessage(new TWFMessage("getlocation","",username,""),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg==null){
			System.out.println("no packet response");
			return null;
		}
		else if(msg.getHeader().equalsIgnoreCase("getlocation-error")){
			//JOptionPane.showMessageDialog(null, "Failed to get Position!");
			return null;
		}
		else {
			JOptionPane.showMessageDialog(null, "Success!");
			return msg.getMessage();
		}
	}
	public void createUpdateThread(){ //Opens new Socket and creates the thread object for real time update
		try{
			updateSocket = new DatagramSocket(port-1);
			thread = new UpdateThread(this,updateSocket);
			thread.start();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void updateState(String from,String message){// synartisi poy kalei to thread gia na enimerwnei mia katastasi se periptwsi mhnymatos update
		//String[] call = sentence.split("-");
		//if(call[0].equalsIgnoreCase("update")){
			for(int i=0;i<theuser.flist.size();i++){
				if(theuser.flist.get(i).username.equalsIgnoreCase(from)){
					theuser.flist.get(i).newSentence(message);
					for(int f=0;f<theuserflistpanel.length;f++){
						if(theuserflistpanel[f].getPanelName().equalsIgnoreCase(from)){
							String[] tempy = theuser.flist.get(i).getMsg().split("@");
							theuserflistpanel[f].sentence.setText(tempy[1]);//theuser.flist[i].getMsg());
							break;
						}
					}
					break;
				}
			}
		
		//}
	}
	public void register(){ //apostoli minimatos registration kai anamoni apantisis
		String checkname =reg1.getText();
		String checkpass = reg2.getText();
		String checkpass2 = reg3.getText();
		String sentence = reg4.getText();
		if(!checkpass.equalsIgnoreCase(checkpass2)){
			JOptionPane.showMessageDialog(null, "please retype password");
		}
		else {
			
			try {
				clientSocket = new DatagramSocket(port);
				clientSocket.setSoTimeout(3000);
				IPAddress = InetAddress.getByName(server);
		
				sendMessage(new TWFMessage("register",checkname,checkpass,sentence),clientSocket,IPAddress,portdest);
				TWFMessage msg = receiveMessage(clientSocket);
				
			    if (msg.getHeader().equalsIgnoreCase("register-failed")||msg.getHeader().equalsIgnoreCase("register-failed2")){
			    	JOptionPane.showMessageDialog(null,"Error With Registration","Error",JOptionPane.INFORMATION_MESSAGE,regicon2);
			    	clientSocket.close();
			    }
			    else {
			    	JOptionPane.showMessageDialog(null, "Registration successful!","Success!",JOptionPane.INFORMATION_MESSAGE,regicon1);
			    	regframe.dispose();
			    }
			    clientSocket.close();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	JOptionPane.showMessageDialog(null,"Error With Registration(server)","Error",JOptionPane.INFORMATION_MESSAGE,regicon2);
		    	clientSocket.close();
		    }
		}
	}
	public void updateFriendlist(){ //enimerwnei to array me ta antikeimena TWFuser twn filwn 
		
			sendMessage(new TWFMessage("data",username,"flist",""),clientSocket,IPAddress,portdest);
			TWFMessage msg = receiveMessage(clientSocket);
			
		    if (msg.getHeader().equalsIgnoreCase("data-flist-error")){
		    	JOptionPane.showMessageDialog(null,"Error updating friendlist");
		    }
		    else if(msg.getHeader().equalsIgnoreCase("data-emptyflist")){
		    	JOptionPane.showMessageDialog(null,"You have no friends!!\n Find some with \nAdd Friend Button!");
		    }
		    else {
		    	String[] result = msg.getMessage().split("-");
		    	
		    	if(result[0]!=""){  //yparxoyn friends
		    		friendlist = new String[result.length];
			    	for (int i=0;i<result.length;i++){
			    		friendlist[i]=result[i];
			    	}
	 
			    for(int i=0;i<friendlist.length;i++){
			    	
	
			    	 sendMessage(new TWFMessage("data",username,"friend",friendlist[i]),clientSocket,IPAddress,portdest);
			    	 msg = receiveMessage(clientSocket);

		    		 if(msg.getHeader().equalsIgnoreCase("data-friend")){
			    		 String tempsent=msg.getTo();
			    		 String isonlinestring = msg.getMessage();
			    		 boolean isuseronline;
			    		 if(isonlinestring.equalsIgnoreCase("online")){
			    			 isuseronline=true;
			    		 }
			    		 else {
			    			 isuseronline=false;
			    		 }
			    		 this.theuser.addfriend(new TWFUser(1,friendlist[i],tempsent,isuseronline));
			    	 }
			    		 
			    }
			    createPanels(theuser.flist);
		    	}
		    	else {
		    		JOptionPane.showMessageDialog(null, "empty friendlist");
		    	}
		    }
		    
	}
	public void queryForums(){ //Stelnei request gia tin lista me ta forums kai enimerwnei to Frame twn forum me ta nea stoixeia
		sendMessage(new TWFMessage("viewforum",username,"",""),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			String[] names =msg.getMessage().split("@@@@");
			if(names.length>0){
				forumbuttons = new ForumPanel[names.length];
				forumlab.invalidate();
				forumlab.removeAll();
				JPanel forumcont = new JPanel();
				forumcont.setOpaque(false);
				forumcont.setLayout(new WrapLayout());
				forumcont.setSize(500,1);
				JScrollPane forumscroll = new JScrollPane(forumcont);
				forumscroll.setOpaque(false);
				forumscroll.setPreferredSize(new Dimension(540,350));
				forumscroll.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
				forumscroll.setBorder(null);
				for(int i=0;i<names.length;i++){
					forumbuttons[i] = new ForumPanel(this,names[i]);
					forumcont.add(forumbuttons[i]);
				}
				JButton createforum = new JButton("New*");
				createforum.setIcon(newicon);
				createforum.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{
							String s = JOptionPane.showInputDialog(null,"Enter the Forum Name:");
							if(s!=null){
								createNewForum(s);
							}
						}catch(Exception e){
							
						}
					}
					
				});
				createforum.setToolTipText("Press this to Create a new Forum");
				forumlab.add(forumscroll);
				forumlab.add(createforum);
				forumlab.validate();
				forumlab.repaint();
			}
		}
	}
	public void queryTopics(String s){ //Stelnei request gia ti lista me ta topics sto Forum #s kai enhmerwnei to Frame twn forum me ta nea stoixeia
		sendMessage(new TWFMessage("viewtopics",username,"",s),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			createtopic = new JButton("Create Topic");
			createtopic.setIcon(newicon);
			createtopic.setName(s);
			createtopic.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					String s = ((JButton)e.getSource()).getName();
					try{
						String ss = JOptionPane.showInputDialog(null,"Enter topic name");
						createForumTopic(s,ss);
					}catch(Exception aaa){
						
					}
					
				}
				
			});
			if(msg.getHeader().equalsIgnoreCase("emptyforum")){
				forumlab.invalidate();
				forumlab.removeAll();
				forumlab.repaint();
				JOptionPane.showMessageDialog(null, "This forum is empty!\nUse *Create topic* to add new topic!");
			
				forumlab.add(createtopic);
				forumlab.add(getforums);
				forumlab.validate();
				forumlab.repaint();
			}
			else{
				String[] topics = msg.getMessage().split("@@@@");
				if(topics.length>0){
					topicbuttons = new TopicPanel[topics.length];//new JButton[topics.length];
					forumlab.invalidate();
					forumlab.removeAll();
					JPanel topiccont = new JPanel();
					topiccont.setOpaque(false);
					topiccont.setLayout(new WrapLayout());
					JScrollPane topiccontscroll = new JScrollPane(topiccont);
					topiccontscroll.setOpaque(false);
					topiccontscroll.setPreferredSize(new Dimension(500,350));
					topiccontscroll.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
					topiccontscroll.setBorder(null);
					for(int i=0;i<topics.length;i++){
						topicbuttons[i] = new TopicPanel(topics[i],s,this);
						//forumlab.add(topicbuttons[i]);
						topiccont.add(topicbuttons[i]);
					}
					getforums.setText("Back");
					getforums.setIcon(backicon);
					forumlab.add(topiccontscroll);
					forumlab.add(createtopic);
					forumlab.add(getforums);
					forumlab.validate();
					forumlab.repaint();
				}
			}
		}
	}
	public void queryPosts(String forum,String topic){ //Stelnei request gia ti lista me ta posts sto Forum #forum kai sto topic toy forum #topic
		final String s1 =forum;
		final String s2 = topic;
		sendMessage(new TWFMessage("viewposts",username,"",forum+"@"+topic),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			ImageIcon replyicon = new ImageIcon(getClass().getResource("images/reply.png"));
			reply = new JButton("Reply");
			reply.setIcon(replyicon);
			reply.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						String s = JOptionPane.showInputDialog(null, "Enter your reply here:");
						if(s!=null){
							createForumTopicReply(s1,s2,s);
						}
					}catch(Exception aa){
						return;
					}
				}
				
			});
			goback = new JButton("back");
			goback.setIcon(backicon);
			goback.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					queryTopics(s1);
				}
				
			});
			if(msg.getHeader().equalsIgnoreCase("noposts")){
				JOptionPane.showMessageDialog(null, "This Topic has no posts");
				forumlab.invalidate();
				forumlab.removeAll();
				forumlab.add(reply);
				forumlab.add(goback);
				forumlab.validate();
				forumlab.repaint();
			}
			else if(msg.getHeader().equalsIgnoreCase("byteerror")){
				JOptionPane.showMessageDialog(null, "There has been an error with the byte payload");
				queryTopics(s1);
			}
			else{
				byte[] getdata = msg.getByte();
				ByteArrayInputStream inby = new ByteArrayInputStream(getdata);
				//forumtext = new JTextArea();
				//forumtext.setBackground(null);
				//forumtext.setOpaque(false);
				JPanel forumtextpanel = new JPanel();
				forumtextpanel.setLayout(new WrapLayout());
				forumtextpanel.setSize(400,1);
				forumtextpanel.setOpaque(false);
				forumscroll = new JScrollPane(forumtextpanel);
				forumscroll.setBackground(new Color(0,0.39f,0,0.0f));
				forumscroll.setBorder(null);
				forumscroll.setOpaque(false);
				forumscroll.setPreferredSize(new Dimension(500,350));
				try {
					ObjectInputStream in = new ObjectInputStream(inby);
					while(true){
						Object s=in.readObject();
						ForumPost post = (ForumPost)s;
						PostPanel pan = new PostPanel(post);
						forumtextpanel.add(pan);
						//forumtext.append("\n"+post.toString());
					}
				} catch (Exception e) {
					
				}

				forumlab.invalidate();
				forumlab.removeAll();
				forumlab.add(forumscroll);
				forumlab.add(reply);
				forumlab.add(goback);
				forumlab.validate();
				forumlab.repaint();
			}
			
			
		}
	}
	public void createNewForum(String forumname){ //Methodos poy stelnei ena create forum request sto server
		sendMessage(new TWFMessage("createforum",username,"",forumname),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			if(msg.getHeader().indexOf("error")!=-1){
				JOptionPane.showMessageDialog(null, "Error creating the forum");
			}
			else{
				queryForums();
			}
		}
	}
	public void createForumTopic(String forumname,String topicname){//Dhmioyrgeis to topic #topicname sto forum #forumname
		sendMessage(new TWFMessage("createtopic",username,topicname,forumname),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			if(msg.getHeader().indexOf("error")!=-1){
				JOptionPane.showMessageDialog(null, "Error creating the forum");
			}
			else{
				queryTopics(forumname);
			}
		}
	}
	public void createForumTopicReply(String forumname,String topicname,String message){//Kaneis reply sto forum %forumname kai topic %topicname
		sendMessage(new TWFMessage("topicreply",username,message,forumname+"@"+topicname),clientSocket,IPAddress,portdest);
		TWFMessage msg = receiveMessage(clientSocket);
		if(msg!=null){
			if(msg.getHeader().equalsIgnoreCase("replyaddsuccess")){
				queryPosts(forumname,topicname);
			}
		}
	}
	public void resolveMessage(TWFMessage msg){//Methodos i opoia analyei ta eiserxomena mhnymata ston client
		if(msg.getHeader().equalsIgnoreCase("update")){
			updateState(msg.getFrom(),msg.getMessage());
			music("D-short.wav");
		}
		else if(msg.getHeader().equalsIgnoreCase("online")){			
			for(int i=0;i<this.theuser.flist.size();i++){
				if(theuser.flist.get(i).username.equalsIgnoreCase(msg.getFrom())){
					theuser.flist.get(i).active=true;
					break;
				}
			}
			music("B-short.wav");
			createPanels(theuser.flist);
		}
		else if(msg.getHeader().equalsIgnoreCase("offline")){
			for(int i=0;i<this.theuser.flist.size();i++){
				if(theuser.flist.get(i).username.equalsIgnoreCase(msg.getFrom())){
					theuser.flist.get(i).active=false;
					break;
				}
			}
			music("A-short.wav");
			createPanels(theuser.flist);
		}
		else if(msg.getHeader().equalsIgnoreCase("typing...")){		
			userIsTyping(msg.getFrom());
		}
		else if(msg.getHeader().equalsIgnoreCase("chatmsg")){
			receiveChatMessage(msg.getFrom(),msg.getMessage());
		}
		else if(msg.getHeader().equalsIgnoreCase("ftprequest")){
			
			openFTPDialog(msg.getFrom(),msg.getTo(),msg.getMessage());
			sendMessage(new TWFMessage("ftpack",theuser.username,msg.getFrom(),""),clientSocket,IPAddress,portftpdest);
		}
		else if(msg.getHeader().equalsIgnoreCase("ftprequestaccept")){
			
			beginToTransmit(msg.getFrom(),msg.getMessage());
			sendMessage(new TWFMessage("ftpack",theuser.username,msg.getFrom(),""),clientSocket,IPAddress,portftpdest);
		}
		else if(msg.getHeader().equalsIgnoreCase("ftpfragment")){
			
			boolean flag =resolveFTPPayload(msg);
			if(flag){
				sendMessage(new TWFMessage("ftpack",theuser.username,msg.getFrom(),""),clientSocket,IPAddress,portftpdest);
			}
		}
		else if(msg.getHeader().equalsIgnoreCase("ftpstoprequest")){
			stopTransmit(msg.getFrom(),msg.getMessage());
			sendMessage(new TWFMessage("ftpack",theuser.username,msg.getFrom(),""),clientSocket,IPAddress,portftpdest);
		}
	}
	public void userIsTyping(String chatusername){ //Methodos i opoia enhmerwnei to parathyro toy chat oti o xrhsths pliktrologei ena mhnyma
		chatframe[theuser.getFriendIndex(chatusername)].chatAlert();
	}
	public void createPanels(ArrayList<TWFUser> theuserlist){  //dhmioyrgia twn eidikwn jpanels 
		mainframe.invalidate();
	    for(int i=0;i<theuserflistpanel.length;i++){ //katharisma twn proigoymenwn panels
	    	if(theuserflistpanel[i]==null){
	    		break;
	    	}
	    	background.remove(theuserflistpanel[i]);
	    }
	      
	    int j=0;
		for(int i=0;i<theuserlist.size();i++){  //dhmioyrgia twn jpanels
	    	
	    	theuserflistpanel[i]= new TWFPanel(theuserlist.get(i),this,theuserlist.get(i).active);
	    	if(i%2==0){
	    		theuserflistpanel[i].setBounds(20,170+j*90,theuserflistpanel[i].getWidth(),theuserflistpanel[i].getHeight());
	    	}
	    	else{
	    		theuserflistpanel[i].setBounds(50+theuserflistpanel[i].getWidth(),170+j*90,theuserflistpanel[i].getWidth(),theuserflistpanel[i].getHeight());
	    		j=j+1;
	    	}
	    	background.add(theuserflistpanel[i]);
	    }
		createChatResources();
		mainframe.validate();
		mainframe.repaint();
	}
	public void createChatResources(){ //dhmioyrgei ta antikeimena chatFrame
		chatframe = new ChatFrame[theuser.flist.size()];
		for(int i=0;i<theuser.flist.size();i++){
			chatframe[i] = new ChatFrame(this,theuser.flist.get(i).username);
		}
		
	}
	public void createInitializationFile(){ //dhmioyrgia toy arxeioy me to opoio tha swzontai oi protimiseis toy xrhsth kai tha fortwnontai meta

		theprogramstate = new State(loginuser.getText(),userpass,server,Integer.toString(port),Integer.toString(portdest),true,soundflag,wantftp);
		FileOutputStream fileOut=null;
		ObjectOutputStream out=null;
		try
	      {
	         fileOut =  new FileOutputStream("save.ser");
	         out = new ObjectOutputStream(fileOut);
	         out.writeObject(theprogramstate);
	         out.close();
		     fileOut.close();
	      }
		catch (Exception ff){
			ff.printStackTrace();
		}
	}
	public void readInitializationFile(){ //Fortwsh stoixeiwn apo seiriopoiimeno object gia grhgorh eisagwgh twn pediwn
		FileInputStream fileIn=null;
		ObjectInputStream in=null;
		try
	      {
	         fileIn =  new FileInputStream("save.ser");
	         in = new ObjectInputStream(fileIn);
	         Object s=null;
	         s = in.readObject();
	         theprogramstate = (State) s;
	      }
		catch (Exception ff){
			ff.printStackTrace();
		}
	}
	public void deleteInitializationFile(){ //Katharizei ( diagrafei ) to arxeio apothikeymenwn stoixeiwn
		File f = new File("save.ser");
		try{
			f.delete();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void testObject(){ //Boithitiki methodos gia na leitoyrgisoyn ta objects 
	
		TWFMessage mess = new TWFMessage("Aristotelis","Aristotelis2","SimpleMessage","Hello Aristotelis");
		try{
		sendMessage(mess,clientSocket,InetAddress.getByName("localhost"),9999);
		TWFMessage s = receiveMessage(clientSocket);
		JOptionPane.showMessageDialog(null,s.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void sendMessage(TWFMessage mes,DatagramSocket sock,InetAddress to,int toport){ //Methodos poy apostellei ObjectBased mhnyma stin address to kai port toport
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(mes);		
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,to, toport );
		    sock.send(sendPacket);
		}
		catch(Exception e){
			//e.printStackTrace();
		}
	}
	public static void sendMessage(TWFMessage mes,DatagramSocket sock){ //Methodos poy apostellei ObjectBased mhnyma stin address to kai port toport POY BRISKONTAI ENTOS TOY OBJECT
		ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
		try{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(mes);		
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,mes.s, mes.port );
		    sock.send(sendPacket);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void beginToTransmit(String from,String message){ //Energopoieitai an labei o xrhsths ena ftpaccept message
		if(ftpsendthreads!=null){
			if(ftpsendthreads.isAMatch(message,from)){
				try{
					ftpsendthreads.start();
				}
				catch(Exception e){
					
				}
			}	
		}
	}
	public void stopTransmit(String from ,String message){ //OBSOLETE?
		if(ftpsendthreads.isAMatch(message,from)){
			
		}
	}
	public boolean resolveFTPPayload(TWFMessage msg){ //Diaxeirizetai ena eiserxomeno mhnyma me periexomeno to payload gia ena ftp transportation
		String s = msg.getFrom();
		String s2 = msg.getMessage();
		String[] s3 = s2.split("@@@@");
		int f = Integer.parseInt(s3[1]);
		if(ftpreceivethreads!=null){
			if(ftpreceivethreads.isAMatch(s3[0], s)){
				boolean flag =ftpreceivethreads.addByte(msg.getByte(), f);
				if(flag){
					ftpreceivethreads.setWorking(false);
				}
				return true;
			}
			else{
				return false;
			}
		}
		else {
			return false;
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
	public void music(String s){ //anaparagwgh background music
		if(theprogramstate==null){
			try {

				URL defaultSound = getClass().getResource("music/"+s);
			    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
				clip = AudioSystem.getClip();
		        clip.open(audioInputStream);
		        clip.start( );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if(theprogramstate.sound){
				try {

					URL defaultSound = getClass().getResource("music/"+s);
				    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
					clip = AudioSystem.getClip();
			        clip.open(audioInputStream);
			        clip.start( );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public void mute(){ //Mute toy hxoy
		if(clip!=null){
		try {
			clip.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		//MGP=null;
	}
	public void unmute(){ //UnMute toy hxoy
		if(clip!=null){
		clip.start();
		}
	}
	public static void main(String[] args){ //main
		@SuppressWarnings("unused")
		TWFClient client = new TWFClient();
	}
	
}
