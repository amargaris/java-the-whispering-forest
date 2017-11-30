package com.twf.core.gfx;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class ButtonsPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4247362844841595820L;
	public TWFClient client;
	public ImageIcon leaf = new ImageIcon(getClass().getResource("images/leaf.png"));
	public ImageIcon logicon = new ImageIcon(getClass().getResource("images/logout.png"));
	public ImageIcon whisper = new ImageIcon(getClass().getResource("images/whisper.png"));
	public ImageIcon berry = new ImageIcon(getClass().getResource("images/blackberry.png"));
	public ButtonsPanel(TWFClient theclient){
		client=theclient;
		setVisible(true);
		setSize(500,100);
		setOpaque(false);
		setLayout(new FlowLayout());
		
		ImageIcon leaf = new ImageIcon(getClass().getResource("images/leaf.png"));
		ImageIcon logicon = new ImageIcon(getClass().getResource("images/logout.png"));
		ImageIcon whisper = new ImageIcon(getClass().getResource("images/whisper.png"));
		ImageIcon berry = new ImageIcon(getClass().getResource("images/blackberry.png"));
		
		JButton searchbutt = new JButton("Add Friend:",leaf);
		searchbutt.setToolTipText("Press this Button to search and Add some friends!");
		searchbutt.setSize(50,50);
		searchbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.addFriendWindow();
				
			}
			
		});
		JButton logoutbutt = new JButton("Logout",logicon);
		logoutbutt.setToolTipText("Press this button to Logout to the Login screen");
		logoutbutt.setSize(50,50);
		//logoutbutt.setText("Logout");
		logoutbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				client.attemptLogout();	
				
			}
			
		});
		JButton whisperbutt = new JButton("Whisper",whisper);//"Whisper");
		whisperbutt.setToolTipText("Press this button to update your Whispering Forest Status!");
		whisperbutt.setSize(50,50);
		//whisperbutt.setText("Whisper");
		whisperbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				String key=JOptionPane.showInputDialog("Enter new Whisper");
				if(!(key.equalsIgnoreCase(""))){
				client.refreshWhisper(key);
				}
				}
				catch(Exception ae){
					
				}
			}
			
		});
		
		JButton testbutt = new JButton("Forums",berry);
		testbutt.setToolTipText("Press this button to open The Whispering Forest Forum Page");
		testbutt.setSize(50,50);
		testbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.forumWindow();
			}
			
		});
		
		add(testbutt);
		add(searchbutt);
		add(whisperbutt);
		add(logoutbutt);
	}
}
