/**	Displays Cards and gameplay information
 * 	@author Tom Starkie, Benji Gabay, Ben Carter
 * 	Date May 17, 2017
 * 	Per.2
 */
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;

public class TableComponent extends JComponent
{
	//constants used for spacing in the GUI
	public final static int TABLE_WIDTH = 800;
	public final static int TABLE_HEIGHT = 600;
	private final static int CARD_GAP = 5;
	private final static int TABLE_Y = (TABLE_HEIGHT / 2) - (Card.getHeight() / 2);
	private static final int LEFT_PLAYER_X = 10;
	private static final int RIGHT_PLAYER_X = TABLE_WIDTH - 2 * (Card.getWidth()) - CARD_GAP * 3;
	private static final int TOP_PLAYER_Y = 10;
	private static final int BOTTOM_PLAYER_Y = TABLE_HEIGHT - Card.getHeight() - 30;
	private static final int U_BET_WIDTH = 95;
	private static final int U_BET_HEIGHT = 25;
	private static final int U_RECT_WIDTH = 95;
	private static final int BOTTOM_BOX_Y = 80;
	private static final int TOP_BOX_Y = 50;
	private static final int POT_RECT_X = 325;
	private static final int POT_RECT_WIDTH = 98;
	private static final int LOG_BOX_Y = 170;
	private static final int LOG_BOX_WIDTH = 330;
	private static final int LOG_BOX_HEIGHT = 30;
	private static final int LEFT_MONEY_X = 140;
	private static final int RIGHT_MONEY_X = 715;
	private static final int TOP_MONEY_Y = 185;
	private static final int BOTTOM_MONEY_Y = 410;
	private static final int BET_RECT_WIDTH = 210;
	private static final int END_TEXT_X = 240;
	private static final int END_TEXT_XBUFFER = 20;
	private static final int END_TEXT_Y = 525;
	private static final int END_TEXT_YBUFFER = 25;
	private static final int TEXT_XBUFFER = 5;
	private static final int INSTRUCTION_START_Y = 118;
	private static final int INSTRUCTION_BUFFER = 10;
	private static final int LEFT_PTEXT_X = 40;
	private static final int RIGHT_PTEXT_X = 615;
	private static final int CALL_TEXT_Y = 100;
	private static final int LOG_TEXT_X = 240;
	private static final int LOG_TEXT_Y = 195;
	private static final int RESTART_INITIAL_X = 300;
	private static final int UBOX_INITIAL_X = 220;
	private static final int FOLD_BUFFER = 70;
	private static final int GREEN = 100;
	private static final int FONT_SIZE1 = 10;
	private static final int FONT_SIZE2 = 15;
	private static final int FONT_SIZE3 = 20;
	private static final int FONT_SIZE4 = 25;
	private static final int RL_AMT = 10;
	private static final int HIDE_X = 3000;
	
	private List<Integer> tableX; //x-coordinate of the table
	private static List<Player> players; //List of players in the game
	private double pot; //how much money is in the pot
	private double minBet;//minimum amount a player can bet
	private double uB; //amount user has bet
	private String logText; //text that tells user what CPU players do in their moves
	private List<Card> table; //cards on the table
	private int uBoxX;//x-coordinate of the box that displays the user options
	private int restartTextX; //x coordinate of restart text
	private static Menu m; //menu object
	private String wlText; //tells the user if they won or lost
	private String exitText; //prompts user to close program
	

	/**
	 * Constructs TableComponent object and instantiates instance variables
	 * to appropriate starting amounts
	 * @param ps List of players used to create instant variable "players"
	 */
	public TableComponent(List<Player> ps)
	{
		restartTextX = HIDE_X;
		logText = "";
		wlText = "";
		exitText = "";
		uB = 0;
		pot = 0;
		minBet = 0;
		table = new ArrayList<Card>();
		tableX = new ArrayList<>();
		for(int i = 0; i < 5; i++)
		{
			int base = (TABLE_WIDTH / 2) - (CARD_GAP * 2) - (Card.getWidth() / 2) - (Card.getWidth() * 2);
			tableX.add(base + (Card.getWidth() + CARD_GAP) * i);
		}
		m = new Menu();
		players = new ArrayList<Player>(ps);
		uBoxX = UBOX_INITIAL_X;
	}
	
	/**
	 * Paints cards on the table along with proper user toggles and information windows
	 * @param gr Graphics object used for GUI
	 */
	public void paintComponent(Graphics gr)
	{
		Graphics2D g2 = (Graphics2D) gr;
		Rectangle tab = new Rectangle(0, 0, TABLE_WIDTH, TABLE_HEIGHT);
		Rectangle uBet = new Rectangle(uBoxX, BOTTOM_BOX_Y, U_BET_WIDTH, U_BET_HEIGHT);
		Rectangle logBox = new Rectangle((TABLE_WIDTH / 2) - (LOG_BOX_WIDTH / 2), LOG_BOX_Y, LOG_BOX_WIDTH, LOG_BOX_HEIGHT);
		Rectangle potRect = new Rectangle(POT_RECT_X, TOP_BOX_Y, POT_RECT_WIDTH, U_BET_HEIGHT);
		Rectangle uRect = new Rectangle(uBoxX, TOP_BOX_Y, U_RECT_WIDTH, U_BET_HEIGHT);
		Rectangle betRect = new Rectangle(POT_RECT_X, BOTTOM_BOX_Y, BET_RECT_WIDTH, U_BET_HEIGHT);
		g2.setColor(new Color(0, GREEN, 0));
		g2.fill(tab);
		g2.setColor(Color.white);
		g2.fill(potRect);
		g2.fill(uBet);
		g2.fill(logBox);
		g2.fill(uRect);
		g2.fill(betRect);
		g2.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE2));
		g2.drawString("$" + players.get(0).getMoney(), LEFT_MONEY_X, TOP_MONEY_Y);
		g2.drawString("$" + players.get(1).getMoney(), LEFT_MONEY_X, BOTTOM_MONEY_Y);
		g2.drawString("$" + players.get(2).getMoney(), RIGHT_MONEY_X, TOP_MONEY_Y);
		g2.drawString("$" + players.get(3).getMoney(), RIGHT_MONEY_X, BOTTOM_MONEY_Y);
		g2.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE3));
		g2.drawString(wlText, END_TEXT_X + END_TEXT_XBUFFER, END_TEXT_Y);
		g2.drawString(exitText, END_TEXT_X, END_TEXT_Y + END_TEXT_YBUFFER);
		g2.drawString("Press SPACE to restart", restartTextX, END_TEXT_Y + END_TEXT_YBUFFER);
		g2.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE1));
		g2.drawString("*Increase/decrease bet", uBoxX, INSTRUCTION_START_Y);
		g2.drawString("with arrow keys and", uBoxX + TEXT_XBUFFER, INSTRUCTION_START_Y + INSTRUCTION_BUFFER);
		g2.drawString("press 'Z' to submit", uBoxX + TEXT_XBUFFER - 1, INSTRUCTION_START_Y + (INSTRUCTION_BUFFER * 2) + 1);
		g2.setColor(Color.black);
		g2.drawString("Press 'Z' to bet", uBoxX + TEXT_XBUFFER, TOP_BOX_Y + INSTRUCTION_BUFFER);
		g2.drawString("Press 'X' to fold", uBoxX + TEXT_XBUFFER, TOP_BOX_Y + (INSTRUCTION_BUFFER * 2));
		g2.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE4));
		g2.drawString("Player 1", LEFT_PTEXT_X, TOP_MONEY_Y);
		g2.drawString("Player 2", LEFT_PTEXT_X, BOTTOM_MONEY_Y);
		g2.drawString("Player 3", RIGHT_PTEXT_X, TOP_MONEY_Y);
		g2.drawString("Player 4", RIGHT_PTEXT_X, BOTTOM_MONEY_Y);
		g2.setFont(new Font("SansSerif", Font.PLAIN, FONT_SIZE2));
		g2.drawString("Mininum bet to call: $" + minBet, POT_RECT_X + TEXT_XBUFFER, CALL_TEXT_Y);
		g2.drawString("Pot: $" + pot, POT_RECT_X + TEXT_XBUFFER, TOP_BOX_Y + (INSTRUCTION_BUFFER * 2));
		g2.drawString("Bet: $" + uB, uBoxX + TEXT_XBUFFER, POT_RECT_WIDTH - 1);
		g2.drawString(logText, LOG_TEXT_X, LOG_TEXT_Y);
		g2.draw(potRect);
		g2.draw(uBet);
		g2.draw(uRect);
		g2.draw(betRect);
		g2.draw(logBox);
		for(int i = 1; i <= 4; i++)
		{
			addPlayerCards(players.get(i-1), i, g2);
		}
		for(Card c: table)
		{
			c.draw(g2);
		}
		m.draw(g2);
	}
	
	/**
	 * Tells menu object to advance screens
	 */
	public void advanceMenu()
	{
		m.advance();
		repaint();
	}
	
	/**
	 * Tells menu object to go back screens
	 */
	public void goBackMenu()
	{
		m.goBack();
		repaint();
	}
	
	/**
	 * Makes restart text visible
	 */
	public void showRestartText()
	{
		restartTextX = RESTART_INITIAL_X;
		repaint();
	}
	
	/**
	 * Makes restart text invisible
	 */
	public void hideRestartText()
	{
		restartTextX = HIDE_X;
		repaint();
	}
	
	/**
	 * Changes text to appear in the information log
	 * @param s String that tells what CPU did with its move
	 */
	public void setLogText(String s)
	{
		logText = s;
		repaint();
	}
	
	/**
	 * Adds a Card to the table
	 * @param c Card to be added
	 */
	public void addToTable(Card c)
	{
		table.add(c);
		for(int i = 0; i < table.size(); i++)
		{
			table.get(i).setY(TABLE_Y);
			table.get(i).setX(tableX.get(i));
		}
		repaint();
	}
	
	/**
	 * Makes user bet prompt visible
	 */
	public void showBetBox()
	{
		uBoxX = UBOX_INITIAL_X;
		repaint();
	}
	
	/**
	 * Moves user bet box off the GUI
	 */
	public void hideBetBox()
	{
		uBoxX = HIDE_X;
		repaint();
	}
	
	/**
	 * Resets table for a new round
	 */
	public void clearTable()
	{
		table.clear();
		uB = 0;
		pot = 0;
		minBet = 0;
	}
	
	/**
	 * Puts one Player's cards on the table
	 * @param p Player whose Cards will be added
	 * @param corner Specifies which corner of the GUI the cards will go
	 * @param g Graphics object used for GUI
	 */
	public void addPlayerCards(Player p, int corner, Graphics2D g) //1 = top left, 2 = top right, 3 = bottom left, 4 = bottom right
	{
		int x = 0;
		int y = 0;
		switch(corner)
		{
		case(1):
		{
			x = LEFT_PLAYER_X;
			y = TOP_PLAYER_Y;
			break;
		}
		case(2):
		{
			x = LEFT_PLAYER_X;
			y = BOTTOM_PLAYER_Y;
			break;
		}
		case(3):
		{
			x = RIGHT_PLAYER_X;
			y = TOP_PLAYER_Y;
			break;
		}
		case(4):
		{
			x = RIGHT_PLAYER_X;
			y = BOTTOM_PLAYER_Y;
			break;
		}
		}
		if(p.isStillIn())
		{
			int count = 0;
			for(Card c : p.getTwoCards())
			{
				c.setX(x + (count * (Card.getWidth() + TEXT_XBUFFER)));
				c.setY(y);
				c.draw(g);
				count++;
			}
		}
		else
		{
			g.setFont(new Font( "SansSerif", Font.PLAIN, FONT_SIZE4 * 2));
			g.setColor(Color.red);
			g.drawString("FOLDED", x , y + FOLD_BUFFER);
		}
	}
	
	/**
	 * Makes Menu visible
	 */
	public void showMenu()
	{
		m.show();
		repaint();
	}
	
	/**
	 * @return amount user decided to bet
	 */
	public double getUBet()
	{
		return uB;
	}
	
	/**
	 * Increases amount user is to bet
	 */
	public void raise(double max)
	{
		if(uB + RL_AMT <= max)
			uB += RL_AMT;
		else if(uB < max && uB + RL_AMT > max)
			uB = max;
		repaint();
	}
	
	/**
	 * Decreases amount user is to bet
	 */
	public void lower()
	{
		if(uB - RL_AMT >= minBet)
			uB -= RL_AMT;
		else if(uB > minBet && uB - RL_AMT < minBet)
			uB = minBet;
		repaint();
	}
	
	/**
	 * Moves Menu off the GUI
	 */
	public void hideMenu()
	{
		m.hide();
		repaint();
	}
	
	/**
	 * @return List of cards on the table
	 */
	public List<Card> getTable()
	{
		return table;
	}
	
	/**
	 * Sets pot to given amount
	 * @param amt How much the pot will be
	 */
	public void setPotAmt(double amt)
	{
		pot = amt;
		repaint();
	}
	
	/**
	 * Sets minimum bet for user and other players
	 * @param amt Amount of the minimum bet
	 */
	public void setMinBet(double amt)
	{
		minBet = amt;
		uB = amt;
		repaint();
	}
	
	/**
	 * Re-instantiates instance variables to make them tell
	 * the user they won
	 */
	public void showWinText()
	{
		wlText = "You have won, young padawan";
		exitText = "Press SPACE to close the program";
		repaint();
	}
	
	/**
	 * Re-instantiates instance variables to make them tell
	 * the user they lost
	 */
	public void showLoseText()
	{
		wlText = "You lost. Be better please. >:(";
		exitText = "Press SPACE to close the program";
		repaint();
	}
}