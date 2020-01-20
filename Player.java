/** Player objects have Collections of Cards with which 
 *  it can create its best hand and playe the hold em game
 * 	@author Tom Starkie, Benji Gabay, Ben Carter
 * 	Date May 17, 2017
 * 	Per.2
 */
import java.util.*;
import java.util.List;

public class Player
{
	//class constants
	private final int HAND_SIZE = 5;
	private final int KING_CASE = 13;
	private final int ACE_CASE = 1;
	
	private List<Card> cards; //list of cards available to player
	private List<Card> two; //pair of cards dealt to player
	private List<Card> hand; //hand player submits
	private double money; //total amount of money the player has
	private double totalAmtBet;  //total bet
	private double bet; //player bet
	private boolean isStillIn; //whether or not the player has folded
	
	/**
	 * Instantiates instance variables and sets money
	 * to given amount
	 * @param startAmount Money player starts with
	 */
	public Player(double startAmount)
	{
		cards = new ArrayList<Card>();
		hand = new ArrayList<Card>();
		two = new ArrayList<Card>();
		money = startAmount;
		totalAmtBet = 0;
		bet = 0;
		isStillIn = true;
	}
	
	/**
	 * Takes player out of the game
	 */
	public void fold()
	{
		isStillIn = false;
	}
	
	/**
	 * @return whether the player has folded
	 */
	public boolean isStillIn()
	{
		return isStillIn;
	}
	
	/**
	 * @return best hand as a String (ArrayList's toString)
	 */
	public String toString()
	{
		return createHand();
	}
	
	/**
	 * Adds a card to Player's pair of cards
	 * @param c Card to be added
	 */
	public void addToTwoCard(Card c)
	{
		two.add(c);
	}
	
	/**
	 * Adds a Card to Player's set of Cards from which they can make a hand
	 * @param c Card to be added
	 */
	public void addCard(Card c)
	{
		cards.add(c);
	}
	
	/**
	 * Resets Player by clearing Cards and putting them back in game
	 */
	public void newHand()
	{
		cards.clear();
		hand.clear();
		two.clear();
		if(money > 0)
			isStillIn = true;
		else
			isStillIn = false;
	}
	
	/**
	 * Deducts Player's money and adds it to total bet
	 */
	public void bet()
	{
		money -= bet;
		totalAmtBet += bet;
	}
	
	/**
	 * Sets Player's bet
	 * @param b Amount of bet
	 */
	public void setBet(double b)
	{
		bet = b;
	}
	
	/**
	 * @return Player's bet
	 */
	public double getBet()
	{
		return bet;
	}
  
	/**
	 * @return Total bet
	 */
	public double getAmtBet()
	{
		return totalAmtBet;
	}
	
	/**
	 * Adds pot amount to Player's money
	 * @param amt Amount to be added
	 */
	public void win(double amt)
	{
		money += amt;
	}
	
	/**
	 * @return Player's money
	 */
	public double getMoney()
	{
		return money;
	}
	
	/**
	 * @return Sorted list of Player's cards
	 */
	public List<Card> getCards()
	{
		sortByNum();
		return cards;
	} 
	
	/**
	 * @return Player's pair of Cards
	 */
	public List<Card> getTwoCards()
	{
		return two;
	}
  
	/**
	 * @return Player's best hand
	 */
	public List<Card> getHand()
	{
		createHand();
		return hand;
	}
  
	/**
	 * Resets Player and total bet to 0
	 */
	public void resetBet()
	{
		totalAmtBet = 0;
		bet = 0;
	}
	
	/**
	 * Creates Player's best possible hand
	 */
	private String createHand()
	{
		if(hasStraightFlush() != null)
		{
			hand = hasStraightFlush();
			return "a Straight Flush";
		}
		else if(hasFourOfAKind() != null)
		{
			hand = hasFourOfAKind();
			return "Four of a Kind";
		}
		else if(hasFullHouse() != null)
		{
			hand = hasFullHouse();
			return "a Full House";
		}
		else if(hasFlush() != null)
		{
			hand = hasFlush();
			return "a Flush";
		}
		else if(hasStraight() != null)
		{
			hand = hasStraight();
			return "a Straight";
		}
		else if(hasThreeOfAKind() != null)
		{
			hand = hasThreeOfAKind();
			return "Three of a Kind";
		}
		else if(hasTwoPair() != null)
		{
			hand = hasTwoPair();
			return "Two Pair";
		}
		else if(hasPair() != null)
		{
			hand = hasPair();
			return "Pair";
		}
		else
		{
			hand = highestCards();
			return "the Highest Five Cards";
		}
	}
	
	/**
	 * Sorts Player's cards by case
	 */
	public void sortByNum()
	{
		TreeSet<Card> temp = new TreeSet<Card>();
		for(Card c: cards)
			temp.add(c);
		cards.clear();
		for(Card c : temp)
			cards.add(c);
	}
	
	/**
	 * Returns a sorted version of given List of Cards
	 * @param tempCards List to be sorted
	 * @return sorted List
	 */
	public List<Card> sortByNum(List<Card> tempCards)
	{
		TreeSet<Card> temp = new TreeSet<Card>();
		for(Card c: tempCards)
			temp.add(c);
		tempCards.clear();
		for(Card c : temp)
			tempCards.add(c);
		return tempCards;
	}
	
	/**
	 * Sorts Player's cards by suit in the order: heart, diamond, club, spade
	 */
	public void sortBySuit()
	{
		sortByNum();
		ArrayList<Card> temp = new ArrayList<>();
		for(Card c : cards)
		{
			if(c.getSuit().equals("heart"))
				temp.add(c);
		}
		for(Card c : cards)
		{
			if(c.getSuit().equals("diamond"))
				temp.add(c);
		}
		for(Card c : cards)
		{
			if(c.getSuit().equals("club"))
				temp.add(c);
		}
		for(Card c : cards)
		{
			if(c.getSuit().equals("spade"))
				temp.add(c);
		}
		cards = temp;
	}
	
	/**
	 * @return List of straight flush if Player has one, null otherwise
	 */
	public List<Card> hasStraightFlush()
	{
		List<Card> f = flushHelper("heart");
		if(f == null)
			f = flushHelper("diamond");
		if(f == null)
			f = flushHelper("club");
		if(f == null)
			f = flushHelper("spade");
		if(f != null)
		{
			f = sortByNum(f);
			if(straightHelper(f) != null)
				return straightHelper(f);
		}
		return null;
	}
	
	/**
	 * @return List of four of a kind if Player has one, null otherwise
	 */
	public List<Card> hasFourOfAKind()
	{
		sortByNum();
		List<Card> ret = new ArrayList<>();
		for(int i = 3; i < cards.size(); i++)
		{
			int tempCase = cards.get(i).getCase();
			if(tempCase == cards.get(i-1).getCase())
			{
				if(tempCase == cards.get(i-2).getCase())
				{
					if(tempCase == cards.get(i-3).getCase())
					{
						ret.add(cards.get(i));
						ret.add(cards.get(i-1));
						ret.add(cards.get(i-2));
						ret.add(cards.get(i-3));
						return ret;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @return List of full house if Player has one, null otherwise
	 */
	public List<Card> hasFullHouse()
	{
		List<Card> ret = new ArrayList<>();
		List<Integer> threeNums = allThreeKindNums();
		if(threeNums.size() > 0)
		{
			List<Integer> pairNums = allPairNums();
			int over = threeNums.get(0);
			pairNums.remove(new Integer(over));
			if(pairNums.size() > 0)
			{
				int under = pairNums.get(0);
				for(int i = 0; i < cards.size(); i++)
					if(cards.get(i).getCase() == over)
						ret.add(cards.get(i));
				for(int i = 0; i < cards.size(); i++)
				{
					if(cards.get(i).getCase() == under)
					{
						ret.add(cards.get(i));
						if(ret.size() == HAND_SIZE)
							return ret;
					}
				}		
			}
		}
		return null;
	}
	
	/**
	 * @return List of flush if Player has one, null otherwise
	 */
	public List<Card> hasFlush()
	{
		List<Card> temp = flushHelper("heart");
		if(temp == null)
			temp = flushHelper("diamond");
		if(temp == null)
			temp = flushHelper("club");
		if(temp == null)
			temp = flushHelper("spade");
		if(temp == null)
			return null;
		List<Card> ret = new ArrayList<>();
		while(ret.size() < HAND_SIZE)
			ret.add(temp.get(ret.size()));
		return ret;
	}
	
	/**
	 * Returns ArrayList of Cards that have given suit
	 * @param cas Suit to search for
	 * @return List of Cards with that suit
	 */
	public List<Card> flushHelper(String cas)
	{
		sortByNum();
		List<Card> ret = new ArrayList<>();
		for(int i = cards.size() - 1; i >= 0; i--)
		{
			Card c = cards.get(i);
			if(c.getSuit().equals(cas))
				if(c.getCase() == 1)
					ret.add(0, c);
				else
					ret.add(c);
		}
		if(ret.size() >= HAND_SIZE)
			return ret;
		else
			return null;
	}
	
	/**
	 * @return List of straight if Player has one, null otherwise
	 */
	public List<Card> hasStraight()
	{
		sortByNum();
		return straightHelper(cards);
	}
  
	/**
	 * @return List of highest possible straight from the given cards; null if
	 * 	no straight exists
	 */
	private List<Card> straightHelper(List<Card> tempCards)
	{
		List<Card> ret = new ArrayList<>();
		List<Card> temp = new ArrayList<>();
		for(int i = tempCards.size() - 1; i >= 0; i--)
			if(temp.isEmpty() || tempCards.get(i).getCase() != temp.get(temp.size() - 1).getCase())
				temp.add(tempCards.get(i));		
		int count = 0;
		for(int i = 0; i < temp.size(); i++)
		{
			count++;
			int cardNum = temp.get(i).getCase();
			while(i + count < temp.size() && temp.get(i + count).getCase() == cardNum - 1)
			{
				cardNum = temp.get(i + count).getCase();
				count++;
			}
			if(temp.get(i).getCase() == KING_CASE && temp.get(temp.size() - 1).getCase() == ACE_CASE)
				count++;
			if(count >= 5)
			{
				if(temp.get(i).getCase() == KING_CASE && temp.get(temp.size() - 1).getCase() == ACE_CASE)
					ret.add(temp.get(temp.size() - 1));
				for(int j = i; j < temp.size(); j++)
				{
					ret.add(temp.get(j));
					if(ret.size() == HAND_SIZE)
						return ret;
				}
			}
			count = 0;
		}
		return null;
	}
	
	/**
	 * @return List containing three of a kind if Player has one, null otherwise
	 */
	public List<Card> hasThreeOfAKind()
	{
		List<Card> ret = new ArrayList<>();
		List<Integer> nums = allThreeKindNums();
		if(nums.size() > 0)
		{
			List<Card> temp = new ArrayList<>();
			for(int i = cards.size() - 1; i >= 0; i--)
				temp.add(cards.get(i));
			int num = nums.get(0);
			for(int i = 0; i < cards.size(); i++)
			{
				if(cards.get(i).getCase() == num)
				{
					ret.add(cards.get(i));
					temp.remove(cards.get(i));
				}
			}
			if(temp.size() > 0 && temp.get(temp.size() - 1).getCase() == ACE_CASE)
			{
				ret.add(temp.get(temp.size() - 1));
				temp.remove(temp.size() - 1);
			}	
			int i = 0;
			while(ret.size() < HAND_SIZE && i < temp.size())
			{
				ret.add(temp.get(i));
				i++;
			}
			return ret;
		}		
		return null;
	}
	
	/**
	 * @return List containing two pair if Player has one, null otherwise
	 */
	public List<Card> hasTwoPair()
	{
		List<Card> ret = new ArrayList<>();
		List<Integer> nums = allPairNums();
		if(nums.size() > 1)
		{
			List<Card> temp = new ArrayList<>();
			for(int i = cards.size() - 1; i >= 0; i--)
				temp.add(cards.get(i));
			int num = nums.get(0);
			for(int i = 0; i < cards.size(); i++)
			{
				if(cards.get(i).getCase() == num)
				{
					ret.add(cards.get(i));
					temp.remove(cards.get(i));
				}
			}
			num = nums.get(1);
			for(int i = 0; i < cards.size(); i++)
			{
				if(cards.get(i).getCase() == num)
				{
					ret.add(cards.get(i));
					temp.remove(cards.get(i));
				}
			}
			if(temp.size() > 0 && temp.get(temp.size() - 1).getCase() == ACE_CASE)
			{
				ret.add(temp.get(temp.size() - 1));
				temp.remove(temp.size() - 1);
			}	
			int i = 0;
			while(ret.size() < HAND_SIZE && i < temp.size())
			{
				ret.add(temp.get(i));
				i++;
			}
			return ret;
		}		
		return null;
	}
	
	/**
	 * @return List containing pair if Player has one, null otherwise
	 */
	public List<Card> hasPair()
	{
		List<Card> ret = new ArrayList<>();
		List<Integer> nums = allPairNums();
		if(nums.size() == 1)
		{
			List<Card> temp = new ArrayList<>();
			for(int i = cards.size() - 1; i >= 0; i--)
				temp.add(cards.get(i));
			int num = nums.get(0);
			for(int i = 0; i < cards.size(); i++)
			{
				if(cards.get(i).getCase() == num)
				{
					ret.add(cards.get(i));
					temp.remove(cards.get(i));
				}
			}
			if(temp.size() > 0 && temp.get(temp.size() - 1).getCase() == ACE_CASE)
			{
				ret.add(temp.get(temp.size() - 1));
				temp.remove(temp.size() - 1);
			}		
			int i = 0;
			while(ret.size() < HAND_SIZE && i < temp.size())
			{
				ret.add(temp.get(i));
				i++;
			}
			return ret;
		}		
		return null;
	}
	
	/**
	 * @return List of Player's highest cards
	 */
	public List<Card> highestCards()
	{
		sortByNum();
		List<Card> ret = new ArrayList<>();
		if(cards.get(0).getCase() == ACE_CASE)
			ret.add(cards.get(0));
		int end = cards.size() - 1;
		while(ret.size() < 5 && end >= 0)
		{
			if(cards.get(end).getCase() != ACE_CASE)
				ret.add(cards.get(end));
			end--;
		}
		return ret;
	}
	
	/**
	 * @return List of every number of which there exists a pair
	 */
	private List<Integer> allPairNums()
	{
		sortByNum();
		List<Integer> pairNums = new ArrayList<>();
		for(int i = cards.size() - 1; i > 0; i--)
		{
			int num = cards.get(i).getCase();
			if(num == cards.get(i-1).getCase() && !pairNums.contains(num))
				if(num == 1)
					pairNums.add(0, num);
				else
					pairNums.add(num);
		}
		return pairNums;
	}
	
	/**
	 * @return List of every number of which there exists three of a kind
	 */
	private List<Integer> allThreeKindNums()
	{
		sortByNum();
		List<Integer> threeKindNums = new ArrayList<>();
		for(int i = cards.size() - 1; i > 1; i--)
		{
			int num = cards.get(i).getCase();
			if(num == cards.get(i-1).getCase())
				if(num == cards.get(i-2).getCase())
					if(!threeKindNums.contains(num))
						if(num == 1)
							threeKindNums.add(0, num);
						else
							threeKindNums.add(num);
		}
		return threeKindNums;
	}
}