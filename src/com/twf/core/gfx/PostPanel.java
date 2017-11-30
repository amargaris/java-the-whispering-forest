package com.twf.core.gfx;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.example.twfportable.ForumPost;


public class PostPanel extends JPanel{
	
	//KSENO KOMMATI GIA EIDIKA EFE STO JPANEL credits @ http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
    protected final int strokeSize = 1;/** Stroke size. it is recommended to set it to 1 for better view */  
    protected final Color shadowColor = Color.yellow;/** Color of shadow */
    protected final boolean shady = true;/** Sets if it drops shadow */
    protected final boolean highQuality = true;/** Sets if it has an High Quality view */
    protected final Dimension arcs = new Dimension(20, 20);/** Double values for Horizontal and Vertical radius of corner arcs */
    protected final int shadowGap = 5;/** Distance between shadow border and opaque panel border */
    protected final int shadowOffset = 4;/** The offset of shadow.  */
    protected final int shadowAlpha = 150;/** The transparency value of shadow. ( 0 - 255) */
    //Telos apo kseno kommati
	
    private static final long serialVersionUID = 1L;
	private ForumPost post;
	private JTextArea datetext;
	private JTextArea authortext;
	private JTextArea text;
	public PostPanel(ForumPost thepost){
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(400,50));
		setOpaque(false);
		post=thepost;
		datetext = new JTextArea();
		datetext.setBackground(null);
		datetext.setOpaque(false);
		datetext.setEditable(false);
		datetext.append("Posted: "+post.date);
		authortext= new JTextArea();
		authortext.setLineWrap(true);
		authortext.setBackground(null);
		authortext.setOpaque(false);
		authortext.setEditable(false);
		authortext.append("User: "+post.creator);
		text = new JTextArea();
		text.setBackground(null);
		text.setOpaque(false);
		text.setEditable(false);
		text.setLineWrap(true);
		text.append(post.message);
		add(datetext);
		add(authortext);
		add(text);
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
