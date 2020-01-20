/**	This class creates a pop up screen welcoming the user to the game and provides 
 * 	instructions to the Texas Hold 'Em.
 * 	@author Tom Starkie, Benji Gabay, Ben Carter
 * 	Date May 17, 2017
 * 	Per.2
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Menu 
{
	//class constants
	private final int WIDTH = TableComponent.TABLE_WIDTH - 200;
	private final int HEIGHT = TableComponent.TABLE_HEIGHT - 200;
	private final String INTRO1 = "Welcome to the Casino!";
	private final String INTRO2 = "Instructions for Texas Hold 'Em:";
	private final int INTRO_BUFFERX = 45;
	private final int INTRO_BUFFERY = 50;
	private final int TEXT_BUFFERX = 10;
	private final int TEXT_BUFFERY = 25;
	private final int TEXT_INITY = 150;
	private final int FINAL_TEXT_YBUFF = 315;
	private final int FINAL_TEXT_XBUFF1 = 145;
	private final int FINAL_TEXT_XBUFF2 = 80;
	private final int FONT_SIZE1 = 40;
	private final int FONT_SIZE2 = 25;
	private final int FONT_SIZE3 = 18;
	private final int FONT_SIZE4 = 23;
	private final int HIDE_X = 3000;
	private final int XY_INIT = 100;
	//integer values for the location and dimensions of the window
	private int xPos;
	private int yPos;
	//variables to determine what text is on the screen
	private String[][] displayTexts;
	private int index;

	/**	The constructor sets the dimensions and location of the window, as well as
	 * 	the text (instructions and welcoming)
	 */
	public Menu()
	{
		displayTexts = new String[6][7];
		index = 1;
		xPos = XY_INIT;
		yPos = XY_INIT;
		populateDisplayTexts();
	}
	
	/**	Draws all of the graphics with the instructions and welcome
	 * @param g2 graphics component
	 */
	public void draw(Graphics2D g2)
	{
		g2.setColor(new Color(0,100,0));
		g2.fill(new Rectangle(xPos - XY_INIT, yPos - XY_INIT, TableComponent.TABLE_WIDTH,TableComponent.TABLE_HEIGHT));
		g2.setColor(Color.red);
		Rectangle box = new Rectangle(xPos, yPos, WIDTH, HEIGHT);
		g2.fill(box);
		g2.setColor(Color.BLACK);
		g2.draw(box);
		g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE1));
		g2.drawString(INTRO1, xPos + INTRO_BUFFERX, yPos + INTRO_BUFFERY);
		g2.setColor(Color.white);
		g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE2));
		g2.drawString(INTRO2, xPos + (INTRO_BUFFERX * 2) - TEXT_BUFFERY, yPos + (INTRO_BUFFERY * 2));
		g2.setFont(new Font(Font.MONOSPACED, 0, FONT_SIZE3));
		g2.drawString(displayTexts[index][0], xPos + TEXT_BUFFERX, yPos + TEXT_INITY);
		g2.drawString(displayTexts[index][1], xPos + TEXT_BUFFERX, yPos + TEXT_INITY + (1 * TEXT_BUFFERY));
		g2.drawString(displayTexts[index][2], xPos + TEXT_BUFFERX, yPos + TEXT_INITY + (2 * TEXT_BUFFERY));
		g2.drawString(displayTexts[index][3], xPos + TEXT_BUFFERX, yPos + TEXT_INITY + (3 * TEXT_BUFFERY));
		g2.drawString(displayTexts[index][4], xPos + TEXT_BUFFERX, yPos + TEXT_INITY + (4 * TEXT_BUFFERY));
		g2.drawString(displayTexts[index][5], xPos + TEXT_BUFFERX, yPos + TEXT_INITY + (5 * TEXT_BUFFERY));
		g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE4));
		if(index == 5)
			g2.drawString(displayTexts[index][6], xPos + FINAL_TEXT_XBUFF1, yPos + FINAL_TEXT_YBUFF);
		else
			g2.drawString(displayTexts[index][6], xPos + FINAL_TEXT_XBUFF2, yPos + FINAL_TEXT_YBUFF);
	}
	
	/**	This method makes the graphics go away
	 */
	public void hide()
	{
		xPos = HIDE_X;
	}
	
	/**	This method makes the graphics reappear 
	 */
	public void show()
	{
		xPos = XY_INIT;
		yPos = XY_INIT;
	}
	
	/**
	 * Displays next page of instructions
	 */
	public void advance()
	{
		if(index < displayTexts.length - 1)
			index++;
	}
	
	/**
	 * Displays previous page of instructions
	 */
	public void goBack()
	{
		if(index > 1)
			index--;
	}
	
	/**
	 * Fills displayTexts with appropriate instructions
	 * for the various screens
	 */
	private void populateDisplayTexts()
	{
		displayTexts[0][0] = "Five cards are placed on the table and each player";
		displayTexts[0][1] = "has two in hand. There are four rounds of betting";
		displayTexts[0][2] = "in which a player can either check, raise, fold,";
		displayTexts[0][3] = "or call the current bet. At the end of the betting,";
		displayTexts[0][4] = "the player with the best hand using their two cards";
		displayTexts[0][5] = "plus the five on the table wins the pot.";
		displayTexts[0][6] = "Navigate Menu with <Arrow> Keys";
		displayTexts[1][0] = "The game begins with an ante of $5 for each player,";
		displayTexts[1][1] = "which increases by $5 every five rounds. There are";
		displayTexts[1][2] = "four rounds in which the players are given cards.";
		displayTexts[1][3] = "";
		displayTexts[1][4] = "";
		displayTexts[1][5] = "";
		displayTexts[1][6] = "Navigate Menu with <Arrow> Keys";
		displayTexts[2][0] = "First every player is given two cards (the opponents'";
		displayTexts[2][1] = "cards are hidden), then three cards will be displayed";
		displayTexts[2][2] = "on the table, then a fourth card will be displayed on";
		displayTexts[2][3] = "the table, and finally a fifth and final card will";
		displayTexts[2][4] = "be displayed on the table.";
		displayTexts[2][5] = "";
		displayTexts[2][6] = "Navigate Menu with <Arrow> Keys";
		displayTexts[3][0] = "There is a round of betting after each of these card";
		displayTexts[3][1] = "rounds in which each player can call the current bet";
		displayTexts[3][2] = "(which will be displayed), raise to a bet of their";
		displayTexts[3][3] = "choosing, or fold (cease betting for the rest of the";
		displayTexts[3][4] = "round). There can only be three raises perround";
		displayTexts[3][5] = "of betting.";
		displayTexts[3][6] = "Navigate Menu with <Arrow> Keys";
		displayTexts[4][0] = "Each player can use any combination of their two";
		displayTexts[4][1] = "cards and the five on the table to make their hand of";
		displayTexts[4][2] = "the best five cards. The hierachy of best poker hands";
		displayTexts[4][3] = "is Straight Flush, Four of a Kind, Full House, Flush,";
		displayTexts[4][4] = "straight, Three of a Kind, Two Pair, Pair, and";
		displayTexts[4][5] = "additional high cards if needed.";
		displayTexts[4][6] = "Navigate Menu with <Arrow> Keys";
		displayTexts[5][0] = "Additional instructions for betting and restarting";
		displayTexts[5][1] = "the round will be displayed on the screen.";
		displayTexts[5][2] = "Press 'Q' at anytime to pull up this instruction";
		displayTexts[5][3] = "screen again.";
		displayTexts[5][4] = "";
		displayTexts[5][5] = "";
		displayTexts[5][6] = "Press 'Enter' to play";
	}
}