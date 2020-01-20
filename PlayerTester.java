import static java.lang.System.*;

public class PlayerTester
{
	private static Card[] deck;
	private static boolean[] cardsUsed;
	
	public static void main(String[] args) 
	{
		fillDeck();
		Player pTest = new Player(0);
//		for(int i = 0; i < 7; i++)
//			pTest.addCard(randCard());
		pTest.addCard(new Card(4,"heart",0,0));
		pTest.addCard(new Card(3,"heart",0,0));
		pTest.addCard(new Card(5,"heart",0,0));
		pTest.addCard(new Card(6,"heart",0,0));
		pTest.addCard(new Card(7,"club",0,0));
		pTest.addCard(new Card(6,"spade",0,0));
		pTest.addCard(new Card(7,"diamond",0,0));
		out.println(pTest.getCards());
		out.println(pTest.toString() + pTest.getHand());
	}
	
	private static void fillDeck()
	{
		deck = new Card[52];
		cardsUsed = new boolean[52];
		int index = 0;
		for(int i = 1; i <= 13; i++)
		{
			deck[index] = new Card(i,"heart",0,0);
			index++;
		}
		for(int j = 1; j <= 13; j++)
		{
			deck[index] = new Card(j,"diamond",0,0);
			index++;
		}
		for(int k = 1; k <= 13; k++)
		{
			deck[index] = new Card(k,"spade",0,0);
			index++;
		}
		for(int l = 1; l <= 13; l++)
		{
			deck[index] = new Card(l,"club",0,0);
			index++;
		}
		for(int m = 0; m < 52; m++)
		{
			cardsUsed[m] = true;
		}
	}
	
	private static Card randCard()
	{
		int c = (int) (Math.random() * 52);
		if(cardsUsed[c])
		{
			cardsUsed[c] = false;
			return deck[c];
		}	
		return randCard();
	}

}
