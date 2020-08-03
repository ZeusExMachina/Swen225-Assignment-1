import java.util.*;
import java.awt.Point;

/**
 * Sets up the game and holds the connection between players 
 * and the board. WIP
 * 
 * @author Elijah Guarina
 */

public class Game {
	/** 
	 * A set to store all the cards used for the murder condition.
	 * (suggestion: maybe make a new class for 3-card tuples for easy comparing)
	 */
	private final Set<Card> murderConditions = new HashSet<Card>();
	/**
	 * A list of all the cards in the game. Only used during setup.
	 */
	private final List<Card> allCards = new ArrayList<Card>();
	/**
	 * A list of the names of all characters from Cluedo. Order goes clockwise starting from Miss Scarlet.
	 */
	private final List<String> characters = Arrays.asList("Miss Scarlet", "Colonel Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Professor Plum");
	/**
	 * A map of all players, and the player number they are associated with. Is implemented as a TreeMap to always maintain ordering of the key.
	 */
	private Map<Integer,Player> players = new TreeMap<Integer,Player>();
	/**
	 * The board associated with this game.
	 */
	private Board board;
	/**
	 * For generating random numbers, e.g. when rolling dice.
	 */
	private Random rand;
	/**
	 * A flag for if the game is over or not.
	 */
	private boolean gameOver;
	
	/**
	 * Game constructor.
	 * 
	 * @param playerCount is the number of players
	 */
	public Game(int playerCount) {
		rand = new Random();
		setup(playerCount);
	}
	
	// ----------------- WHILE GAME RUNS -------------------
	
	/**
	 * Play through the game until a player correctly guesses 
	 * the murder condition, in which case they win and the 
	 * game is over.
	 */
	public void play() {
		while (!gameOver) {
			for (Map.Entry<Integer,Player> player : players.entrySet()) {
				// TODO: Tell the player to choose where to move, maybe display all possible board options
				
			}
		}
	}
	
	/**
	 * Roll two dice and get their sum. Two numbers are generated 
	 * to try to mimic real dice.
	 * 
	 * @return the total of the two dice
	 */
	private Integer rollDice() {
		System.out.println("Rolling...");
		int first = rand.nextInt(6) + 1, second = rand.nextInt(6) + 1;
		System.out.println("Rolled a " + first + " and a " + second);
		return first + second;
	}
	
	/**
	 * Move a piece on the board to a new postion
	 * 
	 * @param pieceName is the name of the piece to move
	 * @param newPos is the new position of the piece
	 */
	public void movePiece(String pieceName, Point newPos) {
		// TODO: Tell the board to move the piece
	}
	
	// ----------------- PRE-GAME SETUP --------------------
	
	/**
	 * Set up a new Cluedo game.
	 * 
	 * @param playerCount is the number of players 
	 * 		  in the game
	 */
	private void setup(int playerCount) {
		board = new Board();
		gameOver = false;
		// Create the cards and decide on the murder/win conditions
		createAllCards();
		setUpMurder();
		// Now create players and deal the rest of the cards to them
		createPlayers(playerCount);
		dealCards();
	}
	
	/**
	 * Manually create all the cards of the Cluedo Game.
	 */
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
	
	/**
	 * Create the murder conditions (winning combination) by 
	 * randomly selecting one Character, Weapon, and Room card.
	 */
	private void setUpMurder() {
		murderConditions.clear();
		Collections.shuffle(allCards);
		for (int i = 0; i < 3; i++) {
			if (i == 0) { getMurderCard(Card.CardType.CHARACTER); }
			if (i == 1) { getMurderCard(Card.CardType.WEAPON); }
			if (i == 2) { getMurderCard(Card.CardType.ROOM); }
		}
		//for (Card card : murderConditions) { System.out.println("murder Card: " + card.toString()); }
	}
	
	/**
	 * Select the first card from allCards of a certain type.
	 * 
	 * @param type is the type of card to look for
	 */
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
	
	/**
	 * Create some new players and  assign them a character 
	 * randomly.
	 * 
	 * @param playerCount is the number of players to create
	 */
	private void createPlayers(int playerCount) {
		selectPlayerCharacters(playerCount);
		assignCharacters();
		//for (Map.Entry<Integer,Player> player : players.entrySet()) { System.out.println(player.toString()); }
	}
	
	/**
	 * Randomly select all characters that will be used by the players
	 * 
	 * @param playerCount is the number of characters to select
	 */
	private void selectPlayerCharacters(int playerCount) {
		players.clear();
		List<String> charactersNames = new ArrayList<String>(characters);
		Collections.shuffle(charactersNames);
		String charName = "";
		for (int i = 0; i < playerCount; i++) {
			charName = charactersNames.get(i);
			players.put(characters.indexOf(charName), new Player(charName));
		}
	}
	
	/**
	 * Assign each character to a player number (e.g. Player 1)
	 */
	private void assignCharacters() {
		Map<Integer,Player> playersReplacement = new TreeMap<Integer,Player>();
		int playerNum = 1;
		for (Map.Entry<Integer,Player> player : players.entrySet()) { 
			playersReplacement.put(playerNum,player.getValue());
			playerNum++;
		}
		players = playersReplacement;
	}
	
	/**
	 * Deal the all cards (except murder cards) to evenly between 
	 * each player. Some players may end up with more than others.
	 */
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
	
	/**
	 * Greet the players to the game and ask how many people will 
	 * play.
	 * 
	 * @return the number of players
	 */
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
