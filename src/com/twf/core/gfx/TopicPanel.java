package com.twf.core.gfx;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class TopicPanel extends JPanel{
	

	private static final long serialVersionUID = 1L;
	private String authorname;
	private String topicname;
	private String creationdate;
	private String lastdate;
	private String views;
	private String posts;
	private String forum;
	
	private JTextArea authorlab;
	private JTextArea creationlab;
	private JTextArea postlab;
	private JButton topicbutt;
	
	private TWFClient client;
	
	//KSENO KOMMATI GIA EIDIKA EFE STO JPANEL credits @ http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
    
    protected final int strokeSize = 1;/** Stroke size. it is recommended to set it to 1 for better view */  
    protected final Color shadowColor = Color.green;/** Color of shadow */
    protected final boolean shady = true;/** Sets if it drops shadow */
    protected final boolean highQuality = true;/** Sets if it has an High Quality view */
    protected final Dimension arcs = new Dimension(20, 20);/** Double values for Horizontal and Vertical radius of corner arcs */
    protected final int shadowGap = 5;/** Distance between shadow border and opaque panel border */
    protected final int shadowOffset = 4;/** The offset of shadow.  */
    protected final int shadowAlpha = 150;/** The transparency value of shadow. ( 0 - 255) */
    //Telos apo kseno kommati
	
	private ImageIcon icon = new ImageIcon(getClass().getResource("images/topic.png"));
	
	public TopicPanel(String parseable,String theforum,TWFClient theclient){
		super();
		client=theclient;
		forum = theforum;
		String[] arr = parseable.split("%%%%");
		topicname = arr[0];
		authorname=arr[1];
		views = arr[2];
		posts = arr[3];
		creationdate = arr[4];
		lastdate = arr[5];
		setPreferredSize(new Dimension(450,80));
		setOpaque(false);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		authorlab = new JTextArea();//JLabel(authorname);
		authorlab.setOpaque(false);
		authorlab.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		authorlab.append("Created By:\n"+authorname);
		authorlab.setEditable(false);
		creationlab = new JTextArea();//new JLabel("created: "+creationdate+"\nchanged: "+lastdate);
		creationlab.setOpaque(false);
		creationlab.setEditable(false);
		creationlab.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		creationlab.append("created: "+creationdate+"\nchanged: "+lastdate);
		//lastdatelab = new JLabel(lastdate);
		topicbutt = new JButton(topicname,icon);
		topicbutt.setName(topicname+"@@@@"+forum);
		topicbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String temp = ((JButton)arg0.getSource()).getName();
				String[] keytemp = temp.split("@@@@");
				client.queryPosts(keytemp[1],keytemp[0]);
			}
			
		});
		postlab = new JTextArea();//JLabel(posts);
		postlab.setOpaque(false);
		postlab.setEditable(false);
		postlab.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
		postlab.append("Views: "+views+"\nPosts: "+posts);
		//viewslab = new JLabel(views);
		add(topicbutt);
		add(creationlab);
		add(authorlab);
		add(postlab);
	}
	protected void paintComponent(Graphics g) { //Kseno Kommati Override tis paintComponent apo to idio site http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int shadowGap = this.shadowGap;
        Color shadowColorA = new Color(shadowColor.getRed(),shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    width - strokeSize - shadowOffset, // width
                    height - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap, 
		height - shadowGap, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(0, 0, width - shadowGap, 
		height - shadowGap, arcs.width, arcs.height);

        //Sets strokes to default, is better.
        graphics.setStroke(new BasicStroke());
    }
}
