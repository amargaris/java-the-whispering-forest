package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.twf.core.TWFUser;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;



public class TWFUserpanel extends JPanel{

	private static final long serialVersionUID = 1L;
	public JLabel name,pan;
	public JTextArea sentence2;
	private ImageIcon logintree;
	private RoundRectangle2D round;
	@SuppressWarnings("unused")
	private TWFUser user;
	private JScrollPane sentencescroll2;
	
	public TWFUserpanel(TWFUser theuser){
		 try 
		    {
		      UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		    } 
		    catch (Exception e) 
		    {
		      e.printStackTrace();
		    }
		user=theuser;
		setSize(450,150);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(false);
		name = new JLabel(theuser.username);
		int len = theuser.username.length();
		name.setFont(new Font("Book Antiqua",Font.BOLD,180/len));
		name.setForeground(Color.GREEN);
		logintree = new ImageIcon(getClass().getResource("images/Oak.png"));  
		pan = new JLabel(logintree);
		//JScrollPane sentencescroll = new JScrollPane(sentence);
		String[] temporal = theuser.getMsg().split("@");
		sentence2 = new JTextArea(temporal[1]);//theuser.getMsg());
		sentencescroll2 = new JScrollPane(sentence2);
		sentencescroll2.setPreferredSize(new Dimension(200,80));
		sentencescroll2.setBorder(null);
		sentencescroll2.setOpaque(false);
		sentencescroll2.setBackground(new Color(0,0.39f,0,0.6f));
		sentence2.setFont(new Font("Serif",Font.PLAIN,25));
		sentence2.setForeground(new Color(0,100,0));
		sentence2.setBackground(new Color(0,0.3f,0,0.0f));
		sentence2.setEditable(false);
		add(name);
		add(pan);
		add(sentencescroll2);

		ColorThread thread = new ColorThread(sentence2);
		thread.start();
		round = new RoundRectangle2D.Double(0,0,this.getWidth(),this.getHeight(),25,25);
	}
	public class ColorThread extends Thread {
		public JLabel lab;
		public JTextArea text;
		
		public ColorThread(JLabel thelab){
			lab=thelab;
		}
		public ColorThread(JTextArea thetext){
			text = thetext;
		}
		public void run(){
			Random ran = new Random();
			while(true){
				if(lab!=null)
				lab.setForeground(new Color(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255)));
				else
					text.setForeground(new Color(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255)));
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
		Color temp =g2.getColor();
		g2.setColor(new Color(0,0.39f,0,0.6f));
		g2.fill(round);
		g2.setColor(temp);
		paintComponents(g);
	}

}
