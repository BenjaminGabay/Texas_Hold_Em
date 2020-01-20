/** Runs Texas Hold 'Em game
 * 	@author Tom Starkie, Benji Gabay, Ben Carter
 * 	Date May 17, 2017
 * 	Per.2
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;

public class CasinoRunner 
{
	private static final int BASE_ANTE = 5;
	private static final int PLAYER_START_AMT = 500;
	private static final int NUM_PLAYERS = 4;
	private static final int NUM_ROUNDS = 3;
	private static final int CARDS_IN_DECK = 52;
	private static final int CARDS_PER_SUIT = 13;
	private static final int BET_DELAY = 1500; // Milliseconds between timer ticks
	private static final int MAX_BETS = 3;
	private static final int ANTE_DIVIDER = 5;
	private static final int NUM_TO_DEAL = 2;
	private static final int CARDS_IN_HAND = 5;
	private static final int LARGE_RAISE = 20;
	private static final int SMALL_RAISE = 10;
	private static final int WORTHWHILE_DIFFERENCE = 4;
	private static final double L_RAISE_PROB = .2;
	private static final double S_RAISE_PROB = .25;
	private static final double FOLD_PROB = .2;
	private static final double MATCH_PROB = .8;
	private static final int SCREEN_BUFFERX = 25;
	private static final int SCREEN_BUFFERY = 70;
	
	private static JFrame frame; //frame displaying GUI
	private static Card[] deck; //deck of cards
	private static boolean[] cardsUsed; //indexes correspond to deck, 
										//determines if card has been drawn
	private static Player p1; //user player
	private static Player p2; //CPU player
	private static Player p3; //CPU player
	private static Player p4; //CPU player
	private static List<Card> table; //Cards on the table
	private static List<Player> players; //all players
	private static List<Player> remainingPlayers; //players who have not folded
	private static double pot; //amount of money in pot
	private static int gamesPlayed; //number of games played
	private static TableComponent t; //TableComponent object
	private static volatile boolean playerHasBet; //if player has bet
	private static volatile boolean restart; //if game should restart
	private static volatile boolean gameOver; //if game is over
	private static boolean allIn; //if player went all in
	private static Timer t1; //game timer
	
	public static void main(String[] args) 
	{
		//instantiation of instance variables
		pot = 0;
		gamesPlayed = 0;
		remainingPlayers = new ArrayList<Player>();
		players = new ArrayList<Player>();
		p1 = new Player(PLAYER_START_AMT);
		p2 = new Player(PLAYER_START_AMT);
		p3 = new Player(PLAYER_START_AMT);
		p4 = new Player(PLAYER_START_AMT);
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		frame = new JFrame();
		table = new ArrayList<Card>();
		t = new TableComponent(players);
		//graphics preparation
		fillDeck();
		frame.add(t);
		frame.setSize(TableComponent.TABLE_WIDTH + SCREEN_BUFFERX, 
				TableComponent.TABLE_HEIGHT + SCREEN_BUFFERY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//allows user to get rid of menu and bring it back up
		KeyListener menuListener = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case (KeyEvent.VK_ENTER):
						t.hideMenu();
						break;
					case (KeyEvent.VK_Q):
						t.showMenu();
						break;
					case (KeyEvent.VK_RIGHT):
						t.advanceMenu();
						break;
					case (KeyEvent.VK_LEFT):
						t.goBackMenu();
						break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}	
		};
		frame.addKeyListener(menuListener);
		//starts game
		holdEm();
	}

	/**
	 * Fills deck with 52 Cards like a standard playing deck
	 */
	private static void fillDeck()
	{
		deck = new Card[CARDS_IN_DECK];
		cardsUsed = new boolean[CARDS_IN_DECK];
		int index = 0;
		for(int i = 1; i <= CARDS_PER_SUIT; i++)
		{
			deck[index] = new Card(i,"heart",0,0);
			index++;
		}
		for(int j = 1; j <= CARDS_PER_SUIT; j++)
		{
			deck[index] = new Card(j,"diamond",0,0);
			index++;
		}
		for(int k = 1; k <= CARDS_PER_SUIT; k++)
		{
			deck[index] = new Card(k,"spade",0,0);
			index++;
		}
		for(int l = 1; l <= CARDS_PER_SUIT; l++)
		{
			deck[index] = new Card(l,"club",0,0);
			index++;
		}
		for(int m = 0; m < CARDS_IN_DECK; m++)
		{
			cardsUsed[m] = true;
		}
	}
	
	/**
	 * @return random Card from deck
	 */
	private static Card randCard()
	{
		int c = (int) (Math.random() * CARDS_IN_DECK);
		if(cardsUsed[c])
		{
			cardsUsed[c] = false;
			return deck[c];
		}	
		return randCard();
	}
	
	/**
	 * Shows restart text and restarts game when user is ready
	 */
	private static void restart()
	{
		t.showRestartText();
		restart = false;
		KeyListener restartListener = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case (KeyEvent.VK_SPACE):
						restart = true;
						break;
					case (KeyEvent.VK_P):
						restart = true;
						System.exit(0);
						break;
				}	
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}	
		};
		frame.addKeyListener(restartListener);
		while(!restart);
		frame.removeKeyListener(restartListener);
		t.hideRestartText();
		holdEm();
	}
  
	/**
	 * Clears every variable in preparation for a new game
	 */
	private static void resetGame()
	{
		remainingPlayers.clear();
		for(Player p : players)
		{
			p.newHand();
			if(p.isStillIn())
				remainingPlayers.add(p);
		}
		table.clear();
		t.clearTable();
		pot = 0;
		fillDeck();
		t.setLogText("");
		allIn = false;
	}
	
	/**
	 * Runs a game of Texas Hold 'Em
	 */
	private static void holdEm()
	{
		resetGame();
		ante();
		dealHoldEm();
		if(!allIn)
			bet();
		int numRounds = NUM_ROUNDS;
		if(!allIn && remainingPlayers.size() > 1)
		{
			addThreeToTable();
			bet();
			numRounds--;
		}
		if(!allIn && remainingPlayers.size() > 1)
		{
			addOneToTable();
			bet();
			numRounds--;
		}
		if(!allIn && remainingPlayers.size() > 1)
		{
			addOneToTable();
			bet();
			numRounds--;
		}
		if(allIn && numRounds > 0)
		{
			if(numRounds == NUM_ROUNDS)
			{
				addThreeToTable();
				addOneToTable();
				addOneToTable();
			}
			else if(numRounds == NUM_ROUNDS - 1)
			{
				addOneToTable();
				addOneToTable();
			}
			else
				addOneToTable();
		}
		for(Player p : players)
			for(Card c : p.getCards())
				c.faceUp();
		t.repaint();
		List<Player> winners = getWinner();
		displayWinner(winners);
		t.setPotAmt(0);
		gamesPlayed++;
		if(p1.getMoney() == 0)
			endGame(0);	
		else if(p1.getMoney() == PLAYER_START_AMT * NUM_PLAYERS)
			endGame(1);
		else
			restart();
	}
	
	/**
	 * Ends game based on whether or not user won
	 * @param wl Determines if user won or lost. 0 if they lose, 1 if they won
	 */
	private static void endGame(int wl)
	{
		if(wl == 0)
			t.showLoseText();
		else
			t.showWinText();
		gameOver = false;
		KeyListener restartListener = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case (KeyEvent.VK_SPACE):
						gameOver = true;
						break;
				}	
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}	
		};
		frame.addKeyListener(restartListener);
		while(!gameOver);
		frame.removeKeyListener(restartListener);
		System.exit(0);
	}
	
	/**
	 * Fills log box with text explaining outcome of the game
	 * @param winners List of Players that won the round
	 */
	private static void displayWinner(List<Player> winners)
	{
		String str = "";
		int numAnd = winners.size() - 1;
		for(int i = 0; i < players.size(); i++)
		{
			if(winners.contains(players.get(i)))
			{
				str += "Player " + (i + 1);
				if(numAnd > 0)
				{
					str += " & ";
					numAnd--;
				}
			}	
		}
		if(winners.size() == 1)
			str += " wins with ";
		else
			str += " win with ";
		str += winners.get(0);
		t.setLogText(str);
	}
	
	/**
	 * Iterations of betting for each player
	 */
	private static void bet()
	{
		for(Player p : players)
			p.resetBet();	
		double currentBet = 0;
		boolean done = false;
		allIn = false;
		int numBets = 0;
		while(!done)
		{	
			done = true;
			for(int i = 0; i < players.size(); i++)
			{
				t.setMinBet(currentBet - p1.getAmtBet());
				Player p = players.get(i);
				if(p.isStillIn() && (currentBet == 0 || p.getAmtBet() < currentBet))
				{
					if(p.equals(p1))
						betHelper(currentBet);
					else
						behaviorCPU(p, currentBet);
					
					if(p.getBet() == -1)
					{
						//Player can't fold if there is no bet, they "check"
						if(currentBet == 0)
							p.setBet(0);
						else
						{
							p.fold();
							remainingPlayers.remove(p);
						}
					}	
					else
					{
						if(allIn || numBets >= MAX_BETS)
							p.setBet(currentBet - p.getAmtBet());
						p.bet();
						if(p.getMoney() == 0 || p.getAmtBet() == maxBet())
							allIn = true;
						pot += p.getBet();
						t.setPotAmt(pot);
						double totalBet = p.getAmtBet();
						if(totalBet > currentBet)
						{
							done = false;
							numBets++;
						}
						currentBet = totalBet;
					}
					displayBet(p, i + 1);
					while(t1.isRunning());
				}
			}
		}
		t.setLogText("");
	}
	
	/**
	 * Allows user to bet desires amount
	 * @param currentBet Minimum amount user can bet
	 */
	private static void betHelper(double currentBet)
	{
		t.showBetBox();
		playerHasBet = false;
		KeyListener betListener = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case (KeyEvent.VK_Z):
						p1.setBet(t.getUBet());
						playerHasBet = true;
						break;
					case (KeyEvent.VK_X):
						p1.setBet(-1);
						playerHasBet = true;
						break;
					case (KeyEvent.VK_UP):
						t.raise(maxBet());
						break;
					case (KeyEvent.VK_DOWN):
						t.lower();
						break;
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}	
		};
		frame.addKeyListener(betListener);
		while(!playerHasBet);
		frame.removeKeyListener(betListener);
		t.hideBetBox();
	}
	
	/**
	 * Fills log box with text explaining who bet and how much
	 * @param p Player that bet
	 * @param pNum Player 1/2/3/4 
	 */
	private static void displayBet(Player p, int pNum)
	{
		if(!p.isStillIn())
			t.setLogText("Player " + pNum + " folded");
		else
			t.setLogText("Player " + pNum + " bet $" + p.getBet());
		class DisplayTimer implements ActionListener
	  	{
	  		public void actionPerformed(ActionEvent event){}
	  	}
		ActionListener display = new DisplayTimer();
		t1 = new Timer(BET_DELAY, display);
		t1.setRepeats(false);
		t1.start();
	}
	
	/**
	 * @return Maximum amount player can bet 
	 */
	private static double maxBet()
	{
		double max = p1.getMoney() + p1.getAmtBet();
		for(Player p : remainingPlayers)
			if(p.getMoney() + p.getAmtBet() < max)
				max = p.getMoney() + p.getAmtBet();
		return max;
	}
	
	/**
	 * Takes ante from players at beginning of game
	 */
	private static void ante()
	{
		double amt = BASE_ANTE * ((gamesPlayed / ANTE_DIVIDER) + 1);
		if(amt > maxBet())
		{
			amt = maxBet();
			allIn = true;
		}
		pot += remainingPlayers.size() * amt;
		t.setPotAmt(pot);
		for(Player p : remainingPlayers)
		{
			p.setBet(amt);
			p.bet();
			p.resetBet();
		}	
	}
	
	/**
	 * Gives each player 2 cards
	 */
	private static void dealHoldEm()
	{
		for(int i = 0; i < NUM_TO_DEAL; i++)
		{
			for(Player p : players)
			{
				Card c = randCard();
				if(p.equals(p2) || p.equals(p3) || p.equals(p4))
					c.faceDown();
				p.addCard(c);
				p.addToTwoCard(c);
			}
		}		
	}
	
	/**
	 * Puts 3 Cards on the table
	 */
	private static void addThreeToTable()
	{
		Card[] carTemp = {randCard(), randCard(), randCard()};
		for(Card c : carTemp)
		{
			table.add(c);
			t.addToTable(c);
			for(Player p : players)
				p.addCard(c);
		}		
	}
	
	/**
	 * Puts one Card on the table
	 */
	private static void addOneToTable()
	{
		Card c = randCard();
		table.add(c);
		t.addToTable(c);
		for(Player p : players)
			p.addCard(c);
	}
  
	/**
	 * @return List of Players who won the game
	 */
	private static List<Player> getWinner()
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : players)
			if(p.isStillIn())
				temp.add(p);
		List<Player> winners = new ArrayList<>();
		if(temp.size() == 1)
			winners.add(temp.get(0));
		if(winners.isEmpty())
			winners = pWithStFlu(temp);
		if(winners.isEmpty())	
			winners = pWith4Kind(temp);				
		if(winners.isEmpty())	
			winners = pWithFullH(temp);
		if(winners.isEmpty())	
			winners = pWithFlu(temp);	
		if(winners.isEmpty())	
			winners = pWithStr(temp);
		if(winners.isEmpty())	
			winners = pWith3Kind(temp);
		if(winners.isEmpty())	
			winners = pWith2Pair(temp);
		if(winners.isEmpty())	
			winners = pWithPair(temp);
		if(winners.isEmpty())
			winners = pWithHighCards(temp);
		int  num = winners.size();
		for(Player p: winners)	
			p.win(pot / num);
		return winners;
	}
	
	/**
	 * Determines which players from given list had a Straight Flush
	 * @param ppl List of Players to search
	 * @return List of Players with Straight Flush
	 */
	private static List<Player> pWithStFlu(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasStraightFlush() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasStraightFlush()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasStraightFlush()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasStraightFlush().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had 4 of a kind
	 * @param ppl List of Players to search
	 * @return List of Players with 4 of a kind
	 */
	private static List<Player> pWith4Kind(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasFourOfAKind() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasFourOfAKind()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasFourOfAKind()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasFourOfAKind().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had a full house
	 * @param ppl List of Players to search
	 * @return List of Players with full house
	 */
	private static List<Player> pWithFullH(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasFullHouse() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasFullHouse()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasFullHouse()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasFullHouse().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had a Flush
	 * @param ppl List of Players to search
	 * @return List of Players with Flush
	 */
	private static List<Player> pWithFlu(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasFlush() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasFlush()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasFlush()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasFlush().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had a Straight
	 * @param ppl List of Players to search
	 * @return List of Players with Straight
	 */
	private static List<Player> pWithStr(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasStraight() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasStraight()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasStraight()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasStraight().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had 3 of a kind
	 * @param ppl List of Players to search
	 * @return List of Players with 3 of a kind
	 */
	private static List<Player> pWith3Kind(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasThreeOfAKind() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		int highCard = (temp.get(0).hasThreeOfAKind()).get(0).getCase();
		for(int i = 1; i < temp.size(); i++)
		{
			int hCard = (temp.get(i).hasThreeOfAKind()).get(0).getCase();
			if(hCard > highCard || hCard == 1)
				highCard = hCard;
		}
		List<Player> temp2 = new ArrayList<>();
		for(Player p : temp)
			if(p.hasThreeOfAKind().get(0).getCase() == highCard)
				temp2.add(p);
		return temp2;
	}
	
	/**
	 * Determines which players from given list had 2 pair
	 * @param ppl List of Players to search
	 * @return List of Players with 2 pair
	 */
	private static List<Player> pWith2Pair(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasTwoPair() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		for(int i = 0; i < CARDS_IN_HAND; i++)
		{
			temp = highCardHelper(temp, i);
			if(temp.size() == 1)
				return temp;
		}
		return temp;
	}
	
	/**
	 * Determines which players from given list had a pair
	 * @param ppl List of Players to search
	 * @return List of Players with pair
	 */
	private static List<Player> pWithPair(List<Player> ppl)
	{
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.hasPair() != null)
				temp.add(p);
		if(temp.isEmpty() || temp.size() == 1)
			return temp;
		for(int i = 0; i < CARDS_IN_HAND; i++)
		{
			temp = highCardHelper(temp, i);
			if(temp.size() == 1)
				return temp;
		}
		return temp;
	}
	
	/**
	 * Determines which players from given list had the highest cards
	 * @param ppl List of Players to search
	 * @return List of Players with the highest Cards
	 */
	private static List<Player> pWithHighCards(List<Player> ppl)
	{		
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			temp.add(p);
		for(int i = 0; i < CARDS_IN_HAND; i++)
		{
			temp = highCardHelper(temp, i);
			if(temp.size() == 1)
				return temp;
		}
		return temp;
	}
	
	/**
	 * Finds players with highest possible card from given index
	 * @param ppl List to search through
	 * @param cardIndex index to start at
	 * @return List of players with highest card at a specific index
	 */
	private static List<Player> highCardHelper(List<Player> ppl, int cardIndex)
	{
		int highC = (ppl.get(0).getHand()).get(cardIndex).getCase();
		for(int i = 1; i < ppl.size(); i++)
		{
			int c = (ppl.get(i).getHand()).get(cardIndex).getCase();
			if(c > highC || c == 1)
				highC = c;
		}
		List<Player> temp = new ArrayList<>();
		for(Player p : ppl)
			if(p.getHand().get(cardIndex).getCase() == highC)
				temp.add(p);
		return temp; 
	}
	
	/**
	 * Determines Player's move if Player is not controlled by user
	 * @param p Player to move
	 * @param bet current bet
	 */
	private static void behaviorCPU(Player p, double bet)
	{
		t.setMinBet(bet - p.getAmtBet());
		List<Card> cards = p.getCards();
		if(cards.size() == 2)
			behaviorHelper2LessCards(p,bet);
		else
			behaviorHelper(p,bet);
	}
	
	/**
	 * Determines what a player will do when they have 2 or less cards
	 * @param p Player to move
	 * @param bet current bet
	 */
	private static void behaviorHelper2LessCards(Player p, double bet)
	{
		List<Card> cards = p.getCards();
		if(cards.get(0).getCase() == cards.get(1).getCase() 
				|| cards.get(0).getSuit().equals(cards.get(1).getSuit()))
		{
			if(Math.random() < L_RAISE_PROB)
			{
				if(bet - p.getAmtBet() + LARGE_RAISE > maxBet())
					p.setBet(maxBet());
				else
					p.setBet(bet - p.getAmtBet() + LARGE_RAISE);
			}	
			else
				p.setBet(bet - p.getAmtBet());
			return;
		}
		if(Math.abs(cards.get(0).getCase() - cards.get(1).getCase()) > WORTHWHILE_DIFFERENCE)
		{
			if(Math.random() < FOLD_PROB)
				p.setBet(-1);
			else
				p.setBet(bet - p.getAmtBet());
			return;
		}
		else
		{
			if(Math.random() > S_RAISE_PROB)
			{
				if(bet - p.getAmtBet() + SMALL_RAISE > maxBet())
					p.setBet(maxBet());
				else
					p.setBet(bet - p.getAmtBet() + SMALL_RAISE);
			}
			else
			{
				p.setBet(bet - p.getAmtBet());
			}
		}
	}
	
	/**
	 * Determines what Player will do when they have more than 2 cards
	 * @param p Player to move
	 * @param bet current bet
	 */
	private static void behaviorHelper(Player p, double bet)
	{
		if(Math.random() < hasSomething(p) * 3 / 4)
		{
			if(bet - p.getAmtBet() + LARGE_RAISE > maxBet())
				p.setBet(maxBet());
			else
				p.setBet(bet - p.getAmtBet() + LARGE_RAISE);
		}
		else if(Math.random() < hasSomething(p))
		{
			if(bet - p.getAmtBet() + SMALL_RAISE > maxBet())
				p.setBet(maxBet());
			else
				p.setBet(bet - p.getAmtBet() + SMALL_RAISE);
		}
		else if(Math.random() < MATCH_PROB)
			p.setBet(bet - p.getAmtBet());
		else
			p.setBet(-1);
	}
	
	/**
	 * Determines value of Player's hand and uses that to determine
	 * probability that Player will raise
	 * @param p Player to move
	 * @return probability Player raises based on hand they have
	 */
	private static double hasSomething(Player p)
	{
		if(p.hasStraightFlush() != null)
			return .9;
		if(p.hasFourOfAKind() != null)
			return .8;
		if(p.hasFullHouse() != null)
			return .7;
		if(p.hasFlush() != null)
			return .6;
		if(p.hasStraight() != null)
			return .5;
		if(p.hasThreeOfAKind() != null)
			return .4;
		if(p.hasTwoPair() != null)
			return .3;
		if(p.hasPair() != null)
			return .2;
		return 0;
	}
}