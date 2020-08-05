import java.util.*;

public class Player {
	private final Set<Card> hand;
	private final String name;
	
	public Player(String name) {
		this.hand = new HashSet<Card>();
		this.name = name;
	}
	
	public void giveCard(Card card) { hand.add(card); }
	
	public void clearHand() { hand.clear(); }
	
	public String toString() { return "name: " + name + ", in hand: " + hand.toString(); }
	
	public Card suggest(Game game) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Make a suggestion - type in 3 cards:");
		Card firstCard = askForCard(game, scan);
		Card secondCard = askForCard(game, scan);
		Card thirdCard = askForCard(game, scan);
		CardTuple suggestion = new CardTuple(firstCard, secondCard, thirdCard);
		return game.refutationProcess(this, suggestion);
	}
	
	public Card askForCard(Game game, Scanner scan) {
		System.out.print("Card: ");
		Card card = game.getCard(scan.nextLine());
		while (card == null) {
			System.out.println("Invalid card. Try again.\nCard:");
			card = game.getCard(scan.nextLine());
		}
		return card;
	}
}
