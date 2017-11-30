package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


public class ResultsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private TWFClient client;
	private SpecialLabel[] resultlab;
	
	public ResultsPanel(String[] temp,TWFClient theclient){
		client=theclient;
		resultlab = new SpecialLabel[temp.length];
    	setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    	
    	
    	for(int i=0;i<temp.length-1;i++){
    		resultlab[i] = new SpecialLabel(temp[i+1],this);
    		add(resultlab[i]);
    	}
    	
	}
	public class SpecialLabel extends JLabel {
		

		private static final long serialVersionUID = 1L;
		
		
		JPopupMenu themenu = new JPopupMenu("settings");
    	JMenuItem themenuitem = new JMenuItem("add friend");
    	ResultsPanel thepan;
		JLabel check;
		public SpecialLabel(String s,ResultsPanel pan){
			super(s);
			thepan = pan;
			check = (JLabel) this;
			setFont(new Font("Book Antiqua",Font.BOLD,22));
    		setForeground(Color.green);	    		
    		themenu = new JPopupMenu("Options:");
    		themenuitem = new JMenuItem("add friend");
    		themenuitem.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					int a = check.getText().indexOf(":");
					boolean key;
					if(a==-1){
						key =client.attemptAdd(check.getText());
					}
					else {
						String[] thetext = check.getText().split(":");
						key = client.attemptAdd(thetext[0]);
					}
					if(key)
						removeResult(check.getText());
				}
    			
    		});
    		themenu.add(themenuitem);
    		
    		add(themenu);
    		addMouseListener(new MouseListener(){
    			
				@Override
				public void mouseClicked(MouseEvent arg0) {}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {
				
					themenu.show(check,arg0.getX(),arg0.getY());
				};

				@Override
				public void mouseReleased(MouseEvent arg0) {}
    			
    		});
		}
		public void removeResult(String s){
			for (int i=0;i<resultlab.length;i++){
				String temp = resultlab[i].getText();
				if(temp.equalsIgnoreCase(s)){
					thepan.remove(resultlab[i]);
					thepan.repaint();
					break;
				}
			}
		}
	}
}
