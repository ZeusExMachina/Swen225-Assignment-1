import java.util.*;
import java.awt.Point;

/**
 * Takes care of game setup and turn order for the Cluedo game. WIP
 * 
 * @author Elijah Guarina
 */

public class Game {
	private final Set<Card> murderConditions = new HashSet<Card>();
	private final List<Card> allCards = new ArrayList<Card>();
	private final List<String> characters = Arrays.asList("Miss Scarlet", "Colonel Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Professor Plum");
	private Map<Integer,Player> players = new TreeMap<Integer,Player>();
	private Board board;
	private Random rand;
	private boolean gameOver;
	
	public Game(int playerCount) {
		rand = new Random();
		setup(playerCount);
	}
	
	// ------------ WHILE GAME RUNS --------------
	
	public void play() {
		while (!gameOver) {
			for (Map.Entry<Integer,Player> player : players.entrySet()) {
				// TODO: Tell the player to choose where to move, maybe display all possible board options
				
			}
		}
	}
	
	private Integer rollDice() {
		System.out.println("Rolling...");
		int first = rand.nextInt(6) + 1, second = rand.nextInt(6) + 1;
		System.out.println("Rolled a " + first + " and a " + second);
		return first + second;
	}
	
	public void movePiece(Point newPos) {
		// TODO: Tell the board to move the piece
	}
	
	// ------------ PRE-GAME SETUP ---------------
	
	private void setup(int playerCount) {
		board = new Board();
		gameOver = false;
		// Create the cards and decide on the murder/win conditions
		createAllCards();
		Collections.shuffle(allCards);
		setUpMurder();
		// Now create players and deal the rest of the cards to them
		createPlayers(playerCount);
		dealCards();
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
		allCards.add(new Card("Kitchen", Card.CardType.ROOM));
		allCards.add(new Card("Ball Room", Card.CardType.ROOM));
		allCards.add(new Card("Conservatory", Card.CardType.ROOM));
		allCards.add(new Card("Dining Room", Card.CardType.ROOM));
		allCards.add(new Card("Billiard Room", Card.CardType.ROOM));
		allCards.add(new Card("Library", Card.CardType.ROOM));
		allCards.add(new Card("Lounge", Card.CardType.ROOM));
		allCards.add(new Card("Hall", Card.CardType.ROOM));
		allCards.add(new Card("Study", Card.CardType.ROOM));
	}
	
	private void setUpMurder() {
		murderConditions.clear();
		for (int i = 0; i < 3; i++) {
			if (i == 0) { getMurderCard(Card.CardType.CHARACTER); }
			if (i == 1) { getMurderCard(Card.CardType.WEAPON); }
			if (i == 2) { getMurderCard(Card.CardType.ROOM); }
		}
		//for (Card card : murderConditions) { System.out.println("murder Card: " + card.toString()); }
	}
		
	private void getMurderCard(Card.CardType type)  {
		// Get a random card of a particular type (given by the "type" parameter)
		Card murderCard = null;
		for (Card card : allCards) { if (card.type() == type) { murderCard = card; break; } }
		// Check if a card was selected at all.
		// If not, there must not be any cards of that type in allCards
		if (murderCard == null) { throw new NullPointerException("Murder card for type " + type + " not found. Check that cards of all types are added to the list of all cards."); }
		else { 
			murderConditions.add(murderCard);
			allCards.remove(murderCard);
		}
	}
	
	private void createPlayers(int playerCount) {
		players.clear();
		List<String> charactersNames = new ArrayList<String>(characters);
		Collections.shuffle(charactersNames);
		String charName = "";
		for (int i = 0; i < playerCount; i++) {
			charName = charactersNames.get(i);
			players.put(characters.indexOf(charName), new Player(charName));
		}
		// Now assign each character to a player number (e.g. Player 1)
		Map<Integer,Player> playersReplacement = new TreeMap<Integer,Player>();
		int playerNum = 1;
		for (Map.Entry<Integer,Player> player : players.entrySet()) { 
			playersReplacement.put(playerNum,player.getValue());
			playerNum++;
		}
		players = playersReplacement;
		//for (Map.Entry<Integer,Player> player : players.entrySet()) { System.out.println(player.toString()); }
	}
	
	private void dealCards() {
		while (allCards.size() > 0) {
			for (Map.Entry<Integer,Player> player : players.entrySet()) {
				player.getValue().giveCard(allCards.get(allCards.size()-1));
				allCards.remove(allCards.size()-1);
				if (allCards.size() < 1) { break; }
			}
		}
		//for (Map.Entry<Integer,Player> player : players.entrySet()) { System.out.println(player.getValue().toString()); }
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
		Game game = new Game(playerCount);
	}
}
