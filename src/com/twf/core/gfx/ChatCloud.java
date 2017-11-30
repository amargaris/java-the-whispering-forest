package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Polygon;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;


public class ChatCloud extends JPanel{

	private static final long serialVersionUID = 1L;
	private String message;
	private String[] escape= {"<3",":\\)",":\\(",":D"};
	private int numofletters; //posa grammata stin protasi
	private int xsizeletters; //poso x pairnei apo tin protasi
	private int ysizeletters; //poso y pairnei apo tin protasi
	private int xperletter=7; //posa pixel ana gramma ston x aksona
	private int yperletter=19;//posa pixel ana gramma ston y aksona
	private int xwrapper=50; //max arithmos ston x aksona
	private int ywrapper=5; //max arithmos ston y aksona
	private int how;
	
	public static final int MYMESSAGE=0;
	public static final int OTHERMESSAGE=1;
	
	public ChatCloud(String themessage,int thehow){
		
		//Synthetica
		
				try {
				     UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
				    } 
				catch (Exception e) 
				  {
				    e.printStackTrace();
				  }
				  
		// ~Synthetica
		
		message=themessage;
		how = thehow;
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		setOpaque(false);
		
		numofletters = message.length();
		
		if(numofletters>xwrapper*ywrapper){
			message= message.substring(0,(xwrapper*ywrapper)-1);
		}
		numofletters = message.length();

		if(numofletters/xwrapper>0){
			xsizeletters=xwrapper;
		}
		else {
			xsizeletters = numofletters%xwrapper;
		}
		ysizeletters = (numofletters/xwrapper)+1;
		
		boolean flag =true;
		for(int i=0;i<escape.length;i++){
			if(message.indexOf(escape[i])!=-1){
				flag=false;
			}
		}
		flag=false;
		if(flag){

			JLabel[] labs = new JLabel[ysizeletters];
			if(labs.length==1){
				labs[0] = new JLabel(message);
				labs[0].setForeground(Color.black);
				//labs[0].setForeground
				add(labs[0]);
			}
			else {
				int i;
				for (i=0;i<labs.length-1;i++){
					String temp = message.substring(i*xwrapper,(i+1)*xwrapper );
					labs[i] = new JLabel(temp);
					labs[i].setForeground(Color.black);
					add(labs[i]);
				}
				String temp = message.substring(i*xwrapper);
				labs[i] = new JLabel(temp);
				labs[i].setForeground(Color.black);
				add(labs[i]);
			}

		}
		else{
			String[] concaded = new String[ysizeletters];
			for(int i=0;i<concaded.length;i++){
				String temp;
				try{
					temp = message.substring(i*xwrapper,(i+1)*xwrapper );
				}catch(IndexOutOfBoundsException e){
					temp = message.substring(i*xwrapper);
				}
				concaded[i] = temp;
			}
			for(int k =0;k<concaded.length;k++){
				String[] result= Special(0, concaded[k]);
				for(int i=0;i<result.length;i++){
					boolean flag2 =false;
					for(int j=0;j<escape.length;j++){
						if(result[i].equalsIgnoreCase(escape[j])){
							flag2=true;
						}
					}
					if(flag2){
						add(resolveIcon(result[i]));
					}
					else {
						JLabel templab = new JLabel(result[i]);
						templab.setForeground(Color.black);
						add(templab);
					}
				}
			}
		
		}
		setPreferredSize(new Dimension(xsizeletters*xperletter+5,ysizeletters*yperletter+20));
		/*
		addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//  Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//  Auto-generated method stub
				JOptionPane.showMessageDialog(null, "hi");
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//  Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//  Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// Auto-generated method stub
				
			}

			
			
		});
		*/
	}
	public int getHow(){
		return how;
	}
	public String[] fuseArray(String[][] input){ //methodos poy enwnei ta NXM arrays se ena array me isa stoixeia
	
		int counter =0;
		for(int i=0;i<input.length;i++){
			counter = counter +input[i].length;
		}
		String[] thereturn = new String[counter];
		counter=0;
		for(int i=0;i<input.length;i++){
			for(int j=0;j<input[i].length;j++){
				thereturn[counter] = input[i][j];
				counter = counter+1;
			}
		}
		return thereturn;
	}
	public String[] Special(int a,String s){
		if(a>escape.length-1){
			String[] end = new String[1];
			end[0] = s;
			return end;
		}
		else {
			String[] s1 = s.split(escape[a]);
			String[] s11 = new String[(2*s1.length)-1];
			for(int i=0;i<s1.length;i++){
				s11[2*i]= s1[i];
				if(i<s1.length-1){
				s11[2*i+1]=escape[a];
				}
			}
			String[][] s2= new String[s11.length][];
			a = a+1;
			for(int i=0;i<s2.length;i++){
				s2[i] = Special(a,s11[i]);
			}
			return fuseArray(s2);
		}
	}
	public JLabel resolveIcon(String s){
		JLabel val= new JLabel();
		if(s.equalsIgnoreCase("<3")){
			ImageIcon heart = new ImageIcon(getClass().getResource("images/heart.png"));
			val = new JLabel(heart);
		}
		else if(s.equalsIgnoreCase(":\\)")){
			ImageIcon smile = new ImageIcon(getClass().getResource("images/smile.jpg"));
			val = new JLabel(smile);
		}
		else if(s.equalsIgnoreCase(":\\(")){
			ImageIcon sad = new ImageIcon(getClass().getResource("images/sad.jpg"));
			val = new JLabel(sad);
		}
		else if(s.equalsIgnoreCase(":D")){
			ImageIcon bigsmile = new ImageIcon(getClass().getResource("images/biglaugh.png"));
			val = new JLabel(bigsmile);
		}
		return val;
	}
	public void paintComponent(Graphics g){

		if(how==0){
			g.setColor(new Color(112,219,147));
			g.fillRoundRect(0, 0, xsizeletters*xperletter,ysizeletters*yperletter , 10, 10);
			//Akroyla toy mhnymatos gia MYMESSAGE
			Polygon poly = new Polygon();
			poly.addPoint(10, ysizeletters*yperletter);
			poly.addPoint(10, ysizeletters*yperletter+15);
			poly.addPoint(30, ysizeletters*yperletter);
			//g.fillPolygon(poly);
			g.setColor(Color.black);
			g.drawRoundRect(0, 0, xsizeletters*xperletter,ysizeletters*yperletter , 10, 10);
			
			g.setColor(new Color(112,219,147));
			g.fillPolygon(poly);
			g.setColor(Color.black);
			g.drawLine(10, ysizeletters*yperletter, 10, ysizeletters*yperletter+15);
			g.drawLine(10, ysizeletters*yperletter+15, 30, ysizeletters*yperletter);
		}
		else {
			g.setColor(new Color(112,219,147));
			g.fillRoundRect(0, 0, xsizeletters*xperletter,ysizeletters*yperletter , 10, 10);
			//Akroyla toy mhnymatos gia OTHERMESSAGE
			Polygon poly = new Polygon();
			poly.addPoint(xsizeletters*xperletter-10, ysizeletters*yperletter);
			poly.addPoint(xsizeletters*xperletter-10, ysizeletters*yperletter+15);
			poly.addPoint(xsizeletters*xperletter-30, ysizeletters*yperletter);
			g.setColor(Color.black);
			g.drawRoundRect(0, 0, xsizeletters*xperletter,ysizeletters*yperletter , 10, 10);
			g.setColor(new Color(112,219,147));
			g.fillPolygon(poly);
			g.setColor(Color.black);
			g.drawLine(xsizeletters*xperletter-10, ysizeletters*yperletter, xsizeletters*xperletter-10, ysizeletters*yperletter+15);
			g.drawLine(xsizeletters*xperletter-10, ysizeletters*yperletter+15, xsizeletters*xperletter-30, ysizeletters*yperletter);
		}
		super.paintComponents(g);
	}
	public static void main(String[] args){
		
		JFrame fram = new JFrame();
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fram.setLocationRelativeTo(null);
		fram.setSize(250,500);
		Container cont = fram.getContentPane();
		cont.setLayout(new FlowLayout());
		ChatCloud test = new ChatCloud("My name is Aristotelis happy <3 <3 i am 22 years old and i am a Java Developer. Currenty i am working on a Client-Server model with a chat variance",0);
		ChatCloud test2 = new ChatCloud("I couldnt care any:( :) less for what you do the fact is that you are a broke retarded piece of shit!!!",1);
		ChatCloud test3 = new ChatCloud("Aristotelis:<3<3<3 :D :D <3<3<3<3<3<3 happy sad happy sad <3<3blablabalblabla i am here hello people <3 blabla bla",ChatCloud.MYMESSAGE);
		ChatCloud test4 = new ChatCloud("Blablabla",ChatCloud.OTHERMESSAGE);
		cont.add(test);
		cont.add(test2);
		cont.add(test3);
		cont.add(test4);
		cont.setBackground(Color.white);
		fram.setVisible(true);
	}
}
