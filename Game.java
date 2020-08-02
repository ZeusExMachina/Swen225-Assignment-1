import java.util.*;

/**
 * Takes care of game setup and turn order for the Cluedo game. WIP
 * 
 * @author Elijah Guarina
 */

public class Game {
	private final Set<Card> murderConditions = new HashSet<Card>();
	private final List<Card> allCards = new ArrayList<Card>();
	private final List<Player> players = new ArrayList<Player>();
	private Board board;
	
	public Game(int playerCount) {
		setup(playerCount);
	}
	
	// ------------ PRE-GAME SETUP ---------------
	
	private void setup(int playerCount) {
		board = new Board();
		// Create the cards and decide on the murder/win conditions
		createAllCards();
		Collections.shuffle(allCards);
		setUpMurder();
		// Now create and deal to all players
		createPlayers(playerCount);
	}
	
	private void createAllCards() {
		allCards.clear();
		allCards.add(new Card("Miss Scarlet", Card.CardType.CHARACTER));
		allCards.add(new Card("Colonel Mustard", Card.CardType.CHARACTER));
		allCards.add(new Card("Mrs. White", Card.CardType.CHARACTER));
		allCards.add(new Card("Mr. Green", Card.CardType.CHARACTER));
		allCards.add(new Card("Mrs. Peacock", Card.CardType.CHARACTER));
		allCards.add(new Card("Professor Plum", Card.CardType.CHARACTER));
		allCards.add(new Card("Candlestick", Card.CardType.WEAPON));
		allCards.add(new Card("Dagger", Card.CardType.WEAPON));
		allCards.add(new Card("Lead Pipe", Card.CardType.WEAPON));
		allCards.add(new Card("Revolver", Card.CardType.WEAPON));
		allCards.add(new Card("Rope", Card.CardType.WEAPON));
		allCards.add(new Card("Spanner", Card.CardType.WEAPON));
		// TODO: add room cards as well
	}
	
	private void setUpMurder() {
		murderConditions.clear();
		for (int i = 0; i < 3; i++) {
			if (i == 0) { getMurderCard(Card.CardType.CHARACTER); }
			if (i == 1) { getMurderCard(Card.CardType.WEAPON); }
			if (i == 2) { getMurderCard(Card.CardType.ROOM); }
		}
	}
		
	private void getMurderCard(Card.CardType type)  {
		// Get a random card of a particular type (given by the "type" parameter)
		Card murderCard = null;
		for (Card card : allCards) { if (card.type() == type) { murderCard = card; break; } }
		// Check if a card was selected at all.
		// If not, there must not be any cards of that type in allCards
		if (murderCard == null) { throw new NullPointerException("Murder card for type " + type + " not found. Check that cards of all types are added to the list of all cards."); }
		else { murderConditions.add(murderCard); }
	}
	
	private void createPlayers(int playerCount) {
		for (int i = 0; i < playerCount; i++) {
			
		}
	}
	
	private static Integer getPlayerCount() {
		// Give a greeting and ask for the number of players
		Scanner inputScanner = new Scanner(System.in);
		System.out.print("Welcome to Cluedo!\nHow many players? (3-6 allowed).\nNumber of players: ");
		String answer = inputScanner.nextLine();
		while (!answer.matches("[3456]")) {
			System.out.print("Invalid input. Please enter the number of players (3-6 players only).\nNumber of players: ");
			answer = inputScanner.nextLine();
		}
		inputScanner.close();
		return Integer.parseInt(answer);
	}
	
	public static void main(String[] args) {
		int playerCount = getPlayerCount();
		System.out.println("playerCount = " + playerCount);
		//Game game = new Game(playerCount);
	}
}
