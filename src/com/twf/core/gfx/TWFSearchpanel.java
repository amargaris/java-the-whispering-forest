package com.twf.core.gfx;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;


public class TWFSearchpanel extends JPanel{
	
	
	private static final long serialVersionUID = 1L;
	private JLabel name,pan;
	private JTextArea input;
	private ImageIcon flute;
	private JIconButton butt; 
	private TWFClient theclient;
	private JComboBox<String> selection;
	private JPanel selectpan;
	
	@SuppressWarnings({ })
	public TWFSearchpanel(TWFClient here){  //Constructor toy antikeimenoy
		 try 
		    {
		      UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		    } 
		    catch (Exception e) 
		    {
		      e.printStackTrace();
		    }
		theclient=here;
		setSize(600,150);
		setOpaque(false);
		flute = new ImageIcon(getClass().getResource("images/Flute.png")); 
		pan = new JLabel(flute);
		

		selectpan = new JPanel();
		selectpan.setLayout(new BoxLayout(selectpan,BoxLayout.Y_AXIS));
		selectpan.setPreferredSize(new Dimension(130,75));
		String[] choices = new String[2];
		choices[0] = "By Name:";
		choices[1] = "By Whisper:";
		selection = new JComboBox<String>(choices);
		selectpan.add(pan);
		selectpan.add(selection);
		//End
		name = new JLabel("Flute:");
		name.setFont(new Font("Book Antiqua",Font.BOLD,30));
		name.setForeground(new Color(255,211,155));
		input = new JTextArea(1,10);
		input.setFont(new Font("Book Antiqua",Font.ITALIC,30));
		input.setForeground(Color.BLUE);
		input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"doNothing");
		input.requestFocus();
		input.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(input.isFocusOwner()){
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
						//JOptionPane.showMessageDialog(null, "enter pressed");
						if(input.getText().length()>3){
							theclient.resultcon.removeAll();
							theclient.searchchoice = selection.getSelectedIndex();
							theclient.attemptSearch();
							theclient.searchgui.repaint();
						}
						else
							JOptionPane.showMessageDialog(null, "too few characters");
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
			
		});
		butt = new JIconButton(getClass().getResource("images/note.png"));
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(input.getText().length()>3) {
					theclient.resultcon.removeAll();
					theclient.searchchoice = selection.getSelectedIndex();
					theclient.attemptSearch();
					theclient.searchgui.repaint();
				}
				else {
						JOptionPane.showMessageDialog(null, "too few characters");
				}
			}
			
		});
		//add(selection);
		//add(pan);
		add(selectpan);
		add(name);
		add(input);
		add(butt);
	}
	public String getKey(){
		return input.getText();
	}
	 class JIconButton extends JButton {
		
		private static final long serialVersionUID = 1L;

			public JIconButton(URL uri) {
		      super(new ImageIcon(uri));
		      setContentAreaFilled(false);
		      setBorderPainted(false);
		      setFocusPainted(false);
		    }
		  } 
}
