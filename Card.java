/** Card objects have a given number and suit and can be either
 * 	face up or face down and can be drawn either way using a 
 * 	Graphics2D object.
 * 	@author Tom Starkie, Benji Gabay, Ben Carter
 * 	Date May 17, 2017
 * 	Per.2
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Card implements Comparable<Card>
{
	//constants used for graphics
	private final static int CARD_WIDTH = 100;
	private final static int CARD_HEIGHT = 150;
	private final int NUM_BUFFER = 10;
	private static final char SPADE = '\u2660';
	private static final char DIAMOND = '\u2666';
	private static final char CLUB = '\u2663';
	private static final char HEART = '\u2764';
	
	private int cas; //ace through king
	private String suit; //heart, diamond, spade, or club
	private int x; //x coordinate of card
	private int y; //y coordinate of card
	private boolean faceUp; //determines whether card will be displayed face up(true) or face down(false)
	
	/**
	 * Constructor that gives Card given suit, case, and x/y coordinates
	 * @param cas Case of card (int)
	 * @param suit Suit of card
	 * @param cx X coordinate
	 * @param cy Y coordinate
	 */
	public Card(int cas, String suit, int cx, int cy)
	{
		this.cas = cas;
		this.suit = suit;
		x = cx;
		y = cy;
		faceUp = true;
	}
	
	/**
	 * Draws Card
	 * @param g2 Graphics2D object used for GUI
	 */
	public void draw(Graphics2D g2)
	{
		Rectangle c = new Rectangle(x,y,CARD_WIDTH,CARD_HEIGHT);
		g2.setColor(Color.black);
		g2.draw(c);
		if(faceUp)
		{
			g2.setColor(Color.white);
			g2.fill(c);
			//place suit symbol in center of card
			String s = "";
			switch(suit)
			{
				case("heart"):
				{
					g2.setColor(Color.red);
					s = Character.toString(HEART);
					break;
				}
				case("diamond"):
				{
					g2.setColor(Color.red);
					s = Character.toString(DIAMOND);
					break;
				}
				case("spade"):
				{
					g2.setColor(Color.black);
					s = Character.toString(SPADE);
					break;
				}
				case("club"):
				{
					g2.setColor(Color.black);
					s = Character.toString(CLUB);
					break;
				}
			}
			g2.setFont(new Font( "SansSerif", Font.PLAIN, 40));
			g2.drawString(s, x + 33, y + 85);
			//place case in top right and bottom left corners of card
			drawCase(g2);
		}
		else
		{
			//if card is face down
			g2.setColor(Color.LIGHT_GRAY);
			g2.fill(c);
		}
	}
	
	/**
	 * Puts the case of the Card in the top right and bottom left corners of card
	 * @param g2 Graphics2D object used for GUI
	 */
	private void drawCase(Graphics2D g2)
	{
		g2.setFont(new Font( "SansSerif", Font.PLAIN, 15));
		if(cas > 1 && cas < 11)
		{
			g2.drawString(Integer.toString(cas), x + NUM_BUFFER, y + NUM_BUFFER + 5);
			g2.drawString(Integer.toString(cas), x + CARD_WIDTH - NUM_BUFFER - 10, y + CARD_HEIGHT - NUM_BUFFER);
		}
		else if(cas == 1)
		{
			g2.drawString("A", x + NUM_BUFFER, y + NUM_BUFFER + 5);
			g2.drawString("A", x + CARD_WIDTH - NUM_BUFFER - 10, y + CARD_HEIGHT - NUM_BUFFER);
		}
		else if(cas==11)
		{
			g2.drawString("J", x + NUM_BUFFER, y + NUM_BUFFER + 5);
			g2.drawString("J", x + CARD_WIDTH - NUM_BUFFER - 10, y + CARD_HEIGHT - NUM_BUFFER);	
		}
		else if(cas==12)
		{
			g2.drawString("Q", x + NUM_BUFFER, y + NUM_BUFFER + 5);
			g2.drawString("Q", x + CARD_WIDTH - NUM_BUFFER - 10, y + CARD_HEIGHT - NUM_BUFFER);
		}
		else
		{
			g2.drawString("K", x + NUM_BUFFER, y + NUM_BUFFER + 5);
			g2.drawString("K", x + CARD_WIDTH - NUM_BUFFER - 10, y + CARD_HEIGHT - NUM_BUFFER);
		}
	}
	
	/**
	 * @return Case of Card
	 */
	public int getCase()
	{
		return cas;
	}
	
	/**
	 * @return Suit of Card
	 */
	public String getSuit()
	{
		return suit;
	}
	
	/**
	 * @return case followed by suit as a String
	 */
	public String toString()
	{
		return Integer.toString(cas) + suit;
	}
	
	/**
	 * Reset Card's x coordinate
	 * @param newX New x coordinate
	 */
	public void setX(int newX)
	{
		x = newX;
	}
	
	/**
	 * Reset Card's y coordinate
	 * @param newY New y coordinate
	 */
	public void setY(int newY)
	{
		y = newY;
	}
	
	/**
	 * @return width of card
	 */
	public static int getWidth()
	{
		return CARD_WIDTH;
	}
	
	/**
	 * @return height of card
	 */
	public static int getHeight()
	{
		return CARD_HEIGHT;
	}
	
	/**
	 * turns card face up
	 */
	public void faceUp()
	{
		faceUp = true;
	}
	
	/**
	 * turns card face down
	 */
	public void faceDown()
	{
		faceUp = false;
	}
	
	/**
	 * Compares Card objects based on case
	 * @param Card to be compared to
	 * @return positive if this Card has larger case, negative if it has
	 * 		smaller case, and zero if their cases are the same
	 */
	@Override
	public int compareTo(Card c)
	{
		if(this.getCase() == c.getCase())
			if(this.equals(c))
				return 0;
			else
				return -1;
		return this.getCase() - c.getCase();
	}
	
	/**
	 * @return whether Cards have same case and suit
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || getClass() != obj.getClass())
			return false;
		Card c = (Card) obj;
		if(c.getCase() == getCase() && c.getSuit() == getSuit())
			return true;
		return false;
	}
}
