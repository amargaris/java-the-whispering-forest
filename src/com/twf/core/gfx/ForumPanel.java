package com.twf.core.gfx;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class ForumPanel extends JPanel{
	

	private static final long serialVersionUID = 1L;
	private TWFClient client;
	private String forumname;
	private JButton butt;
	private JLabel lab;
	private ImageIcon poke = new ImageIcon(getClass().getResource("images/forum.png"));
	
	//KSENO KOMMATI GIA EIDIKA EFE STO JPANEL credits @ http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
    protected final int strokeSize = 1;/** Stroke size. it is recommended to set it to 1 for better view */  
    protected final Color shadowColor = Color.blue;/** Color of shadow */
    protected final boolean shady = true;/** Sets if it drops shadow */
    protected final boolean highQuality = true;/** Sets if it has an High Quality view */
    protected final Dimension arcs = new Dimension(20, 20);/** Double values for Horizontal and Vertical radius of corner arcs */
    protected final int shadowGap = 5;/** Distance between shadow border and opaque panel border */
    protected final int shadowOffset = 4;/** The offset of shadow.  */
    protected final int shadowAlpha = 150;/** The transparency value of shadow. ( 0 - 255) */
    //Telos apo kseno kommati
	
	
	public ForumPanel(TWFClient theclient,String parseable){
		super();
		client=theclient;
		setPreferredSize(new Dimension(250,85));
		setOpaque(false);
		String[] temp = parseable.split("%%%%");
		forumname=temp[0];
		butt = new JButton(temp[0],poke);
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.queryTopics(forumname);
			}
			
		});
		lab = new JLabel("Topics: "+temp[1]);
		lab.setFont(new Font("Times",Font.BOLD,10));
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(butt);
		add(lab);
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
