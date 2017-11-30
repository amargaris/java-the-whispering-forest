package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.twf.core.gfx.ext.WrapLayout;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;


public class ChatFrame {
	
	private TWFClient client;
	private String chat;
	private JFrame chatframe;
	private JTextArea chatinput;
	private JButton chatsend;
	private JScrollPane chatscroll;
	private ImageIcon chatback;
	private JPanel messagepan;
	private JLabel usernamelab;
	private JLabel othernamelab;
	private JLabel somethingbelow1;
	private JLabel somethingbelow2;
	private JPanel inputpan;
	private JPanel panicon2;
	private JPanel panicon1;
	private JLabel icon2;
	private JLabel icon1;
	private ImageIcon chattreeicon2;
	private ImageIcon chattreeicon1;
	
	public ChatFrame(TWFClient theclient,String chatuser){
		
		client=theclient;
		chat =chatuser;
		//Grafika
		//Synthetica
		
				try {
				     UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
				    } 
				catch (Exception e) 
				  {
				    e.printStackTrace();
				  }
				  
		// ~Synthetica		

		//Importing Images...
				chatback = new ImageIcon(getClass().getResource("images/chatback.jpg"));//Eikona background
				chattreeicon2 = new ImageIcon(getClass().getResource("images/chat2.png"));//anapodo dentro
				chattreeicon1 = new ImageIcon(getClass().getResource("images/chat1.png"));//kanoniko dentro
				//Arxiko JPanel me background
				JLabel mainpane = new JLabel(chatback);
				mainpane.setLayout(new FlowLayout());
				
				//Rythimiseis toy JFrame
				chatframe = new JFrame();
				chatframe.setSize(650,460);
				chatframe.setResizable(false);
				chatframe.setTitle("Chatting with Tree :"+chat);
				chatframe.setLocationRelativeTo(null);
				chatframe.setIconImage(chattreeicon1.getImage());
				
				//Aristero eikonidio 
				icon1 = new JLabel(chattreeicon1);
				icon1.setPreferredSize(new Dimension(150,200));
				icon1.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Deksio eikonidio
				icon2 = new JLabel(chattreeicon2);
				icon2.setPreferredSize(new Dimension(150,200));
				icon2.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Aristero Panel ( toy xrhsth ) 
				panicon1 = new JPanel(){

					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g){
						g.setColor(new Color(1.0f,1.0f,1.0f,0.9f));
						g.fillRoundRect(0, 0, 150, 250, 25, 25);
						super.paintComponent(g);
					}
				};
				panicon1.setOpaque(false);
				panicon1.setLayout(new BoxLayout(panicon1,BoxLayout.Y_AXIS));
				panicon1.setPreferredSize(new Dimension(150,250));
				
				//Deksio Panel ( toy target Xrhsth )
				panicon2 = new JPanel(){
				
					private static final long serialVersionUID = 1L;

					public void paintComponent(Graphics g){
						g.setColor(new Color(1.0f,1.0f,1.0f,0.9f));
						g.fillRoundRect(0, 0, 150, 250, 25, 25);
						super.paintComponent(g);
					}
				};
				panicon2.setOpaque(false);
				panicon2.setPreferredSize(new Dimension(150,250));
				panicon2.setLayout(new BoxLayout(panicon2,BoxLayout.Y_AXIS));
			
				//Onoma toy Xrhsth
				usernamelab = new JLabel(theclient.username,JLabel.CENTER);
				usernamelab.setForeground(Color.black);
				usernamelab.setFont(new Font("Jokerman",Font.BOLD,15));
				usernamelab.setPreferredSize(new Dimension(50,7));
				usernamelab.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Onoma toy target xrhsth
				othernamelab = new JLabel(chat,JLabel.CENTER);
				othernamelab.setForeground(Color.black);
				othernamelab.setFont(new Font("Jokerman",Font.BOLD,15));
				othernamelab.setPreferredSize(new Dimension(50,7));
				othernamelab.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				//Shmeiwsh toy xrhsth
				somethingbelow1 = new JLabel("");
				somethingbelow1.setPreferredSize(new Dimension(20,5));
				somethingbelow1.setAlignmentX(Component.CENTER_ALIGNMENT);
				somethingbelow1.setForeground(Color.black);
				somethingbelow1.setFont(new Font("Broadway",Font.ITALIC,10));
				
				//Shmeiwsh toy "target" user
				somethingbelow2 = new JLabel("");
				somethingbelow2.setPreferredSize(new Dimension(20,5));
				somethingbelow2.setAlignmentX(Component.CENTER_ALIGNMENT);
				somethingbelow2.setForeground(Color.black);
				somethingbelow2.setFont(new Font("Broadway",Font.ITALIC,10));
				
				//Topothetisi se container
				panicon1.add(usernamelab);
				panicon1.add(icon1);
				panicon1.add(somethingbelow1);
				panicon2.add(othernamelab);
				panicon2.add(icon2);
				panicon2.add(somethingbelow2);
				
				//JPanel me mhnymata
				messagepan = new JPanel();
				messagepan.setAlignmentX(Component.CENTER_ALIGNMENT);
				messagepan.setLayout(new WrapLayout());//new BoxLayout(messagepan,BoxLayout.PAGE_AXIS));
				messagepan.setOpaque(false);
				messagepan.setSize(315,10);//.setPreferredSize(new Dimension(315,300));TODO edw allagh
				chatscroll = new JScrollPane(messagepan);
				/*
				chatscroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			        public void adjustmentValueChanged(AdjustmentEvent e) {  
			            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			        }
			    });
			    */
				chatscroll.setPreferredSize(new Dimension(315,300));
				chatscroll.setBackground(Color.gray);
				inputpan = new JPanel();
				inputpan.setLayout(new FlowLayout(FlowLayout.LEFT));
				inputpan.setPreferredSize(new Dimension(560,110));
				inputpan.setOpaque(false);
				chatinput = new JTextArea();
				chatinput.setLineWrap(true);
				chatinput.setPreferredSize(new Dimension(475,100));
				chatinput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "doNothing");
				chatinput.addKeyListener(new KeyListener(){
					public int a =0;
					@Override
					public void keyPressed(KeyEvent arg0) {
						
						if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
							//JOptionPane.showMessageDialog(null, "Message:"+chatinput.getText());
							if(!chatinput.getText().equalsIgnoreCase("")){
								client.sendChatMessage(chat,chatinput.getText());
								chatinput.setText("");
							}
						}
						else {
							if(a<3){
								a = a+1;
							}
							if(a==3){
							client.sendTypingMessage(chat);
							a=0;
							}
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {	
					}

					@Override
					public void keyTyped(KeyEvent arg0) {}
					
				});
				chatsend = new JButton();
				chatsend.setPreferredSize(new Dimension(70,100));
				chatsend.setText("Send!");
				chatsend.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String s = chatinput.getText();
							if(!s.equalsIgnoreCase("")){
								client.sendChatMessage(chat,s);
							}
						
						
					}
					
				});
				inputpan.add(chatinput);
				inputpan.add(chatsend);
				mainpane.add(panicon1);
				mainpane.add(chatscroll);
				mainpane.add(panicon2);
				mainpane.add(inputpan);
				chatframe.add(mainpane);
			    chatframe.addWindowListener(new WindowListener(){

					@Override
					public void windowActivated(WindowEvent arg0) {}

					@Override
					public void windowClosed(WindowEvent arg0) {
						
					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						setVisible(false);
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
	}
	public void setVisible(boolean how){
		chatframe.setVisible(how);
	}
	public void chatAlert(){
		Runnable run = new Runnable(){

			@Override
			public void run() {
				
				try {
					somethingbelow2.setText(chat+" is typing.");
					Thread.sleep(300);
					somethingbelow2.setText(chat+" is typing..");
					Thread.sleep(300);
					somethingbelow2.setText(chat+" is typing...");
					Thread.sleep(300);
					somethingbelow2.setText("");
				} catch (Exception e) {				
					e.printStackTrace();
				}
				
			}
			
		};
		new Thread(run).start();
	}
	public boolean isVisible(){
		return chatframe.isVisible();
	}
	public void addCloud(ChatCloud cha){
		chatframe.invalidate();
		messagepan.add(cha);
		chatframe.validate();
		chatframe.repaint();
	}
	
}
