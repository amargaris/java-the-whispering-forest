package com.twf.core.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.twf.core.TWFUser;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;


public class TWFPanel extends JPanel{


	private static final long serialVersionUID = 1L;
	private TWFUser user;
	private JLabel name,pan;
	public JTextArea sentence;
	private ImageIcon logintree;
	private JPopupMenu menu = new JPopupMenu("Options");
	private RoundRectangle2D round;
	private JScrollPane sentencescroll;
	private TWFClient theclient;
	
	public TWFPanel(TWFUser auser,TWFClient client,boolean online){
		theclient=client;
		//self=this;
		try 
		    {
		      UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		    } 
		    catch (Exception e) 
		    {
		      e.printStackTrace();
		    }
		
		this.user=auser;
		setSize(400,76);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(false);
		int temp = user.username.length();
		name = new JLabel(user.username);
		name.setFont(new Font("Book Antiqua",Font.BOLD,180/temp));
		name.setForeground(Color.GREEN);
		name.add(menu);
		name.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {
				menu.show(name, arg0.getX(), arg0.getY());
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		
		sentence = new JTextArea();//user.sentence);
		sentence.setBackground(null);
		sentencescroll = new JScrollPane(sentence);
	
		sentencescroll.setBackground(new Color(0,0.39f,0,0.6f));
		sentencescroll.setOpaque(false);
		sentencescroll.setBorder(null);
		sentence.setEditable(false);
		String[] temporal = user.getMsg().split("@");
		sentence.setText(temporal[1]);// PATENTA GIA NA MI FAINETAI I WRA
		sentence.setFont(new Font("Serif",Font.PLAIN,25));
		sentence.setBackground(new Color(0,0.39f,0,0.0f));
		if(online){
			logintree = new ImageIcon(getClass().getResource("images/tree-icon2.png"));  
		}
		else {
			logintree = new ImageIcon(getClass().getResource("images/tree-icon2-online.png"));
		}
		pan = new JLabel(logintree);
		JMenuItem menu1 = new JMenuItem("History");
		menu1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				theclient.historyWindow(user.username);
			}
			
		});
		JMenuItem menu2 = new JMenuItem("Remove f.");
		menu2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "remove friend selected");
				theclient.attemptRemove(user);
			}
			
		});
		JMenuItem menu3 = new JMenuItem("Open chat");
		menu3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				theclient.chatWindow(user.username);
				
			}
			
		});
		menu3.setEnabled(online);
		
		JMenuItem menu4 = new JMenuItem("Send File");
		menu4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				theclient.sendFileTo(user.username);
			}
		});
		menu4.setEnabled(online);
		
		round = new RoundRectangle2D.Double(0,0,this.getWidth(),this.getHeight(),25,25);
		menu.add(menu1);
		menu.add(menu2);
		menu.add(menu3);
		menu.add(menu4);
		ColorThread thread = new ColorThread(sentence);
		thread.start();
	
		sentencescroll.setPreferredSize(new Dimension(190,55));
		add(sentencescroll,FlowLayout.LEFT);
		add(name,FlowLayout.LEFT);
		add(pan,FlowLayout.LEFT);
		
		
		
	}
	public String getPanelName(){
		return user.username;
	}
	public class ColorThread extends Thread {  //eswteriko thread poy enallassei ta xrwmata sti sentence
		public JTextArea lab;
		
		public ColorThread(JTextArea thelab){
			lab=thelab;
		}
		public void run(){
			Random ran = new Random();
			while(true){
				lab.setForeground(new Color(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255)));
				try {
					sleep(500);
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
	}
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color temp =g2.getColor();
		g2.setColor(new Color(0,0.39f,0,0.6f));
		g2.fill(round);
		g2.setColor(temp);
		paintComponents(g);
	}
	public void paintSentence(){
		//sentencescroll.setPreferredSize(new Dimension(this.getWidth()-name.getWidth()-pan.getWidth(),80));
	}

}
