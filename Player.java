import java.util.*;

public class Player {
	private final Map<String, Card> hand;
	private final String name;
	private final Random rand;
	public Game g;
	private boolean canAccuse = true;

	public Player(String name) {
		this.hand = new HashMap<>();
		this.name = name;
		this.rand = new Random();
	}

	public String getName() {
		return name;
	}

	public void giveCard(Card card) { hand.put(card.getName(),card); }

	public boolean playTurn(Game g) {
		boolean receivedValidInput = false;
		this.g = g;
		Scanner scan = g.getScanner();
		printCards();
		while(!receivedValidInput){
			System.out.println("Would you like to see the board? (Y/N): ");
			String userInput = scan.nextLine().toUpperCase();
			if (userInput.equals("Y")) {
				receivedValidInput = true;
				g.drawBoard();
			} else if (userInput.equals("N")) {
				receivedValidInput = true;
			} else {
				System.out.println("Invalid input, please try again.");
			}
		}
		receivedValidInput = false;
		while (!receivedValidInput) {
			System.out.println("Would you like to roll? (Y/N): ");
			String userInput = scan.nextLine().toUpperCase();
			if (userInput.equals("Y")) {
				receivedValidInput = true;
				move();
			} else if (userInput.equals("N")) {
				receivedValidInput = true;
			} else {
				System.out.println("Invalid input, please try again.");
			}
		}

		if (g.checkPlayerInRoom(this)) {
			receivedValidInput = false;
			while (!receivedValidInput) {
				System.out.print("Would you like to suggest? (Y/N): ");
				String userInput = scan.nextLine().toUpperCase();
				if (userInput.equals("N")) {
					receivedValidInput = true;
				} else if (userInput.equals("Y")) {
					Card charCard;
					System.out.print("Character: ");
					charCard = isCard(scan.nextLine(), Card.CardType.CHARACTER);
					if (charCard == null) {
						while (charCard == null) {
							System.out.println("Not a valid Character card, try again: ");
							charCard = isCard(scan.nextLine(), Card.CardType.CHARACTER);
						}
					}

					Card weapCard;
					System.out.print("Weapon: ");
					weapCard = isCard(scan.nextLine(), Card.CardType.WEAPON);
					if (weapCard == null) {
						while (weapCard == null) {
							System.out.println("Not a valid Weapon card, try again: ");
							weapCard = isCard(scan.nextLine(), Card.CardType.WEAPON);
						}
					}

					Card roomCard = g.getCard(g.getPlayerRoom(this).getName());


					CardTuple cardTup = new CardTuple(charCard, weapCard, roomCard);
					g.moveViaSuggestion(cardTup);

					Card refuteCard = g.refutationProcess(this, cardTup);
					if (refuteCard == null) {
						System.out.println("Your suggestion " + cardTup.toString() + " was not refuted!");
					} else {
						System.out.println("Your suggestion " + cardTup.toString() + " was refuted by the card " + refuteCard.getName());
					}
				} else {
					System.out.println("Invalid input, please try again.");
				}
			}
		}

		receivedValidInput = false;
		while (!receivedValidInput) {
			System.out.print("Would you like to accuse? (Y/N): ");
			String userInput = scan.next().toUpperCase();
			if (userInput.equals("Y")) {
				return (accuse(g));
			} else if (userInput.equals("N")) {
				receivedValidInput = true;
			} else {
				System.out.println("Invalid input, please try again.");
			}
		}
		return false;
	}

	/**
	 * Roll two dice and get their sum. Two numbers are generated
	 * to try to mimic real dice.
	 *
	 * @return the total of the two dice
	 */
	private Integer rollDice() {
		int first = rand.nextInt(6) + 1, second = rand.nextInt(6) + 1;
		System.out.println("You rolled a " + first + " and a " + second);
		return first + second;
	}

	private void move(){
		int counter = rollDice();
		Scanner scan = g.getScanner();
		while(counter > 0) {
			System.out.println("Moves left: " + counter +
					" Enter a single move with W, A, S, or D and press enter: ");
			String direction = scan.nextLine().toUpperCase();
			if (direction.equals("W") || direction.equals("A") ||
					direction.equals("S") || direction.equals("D")) {
				if(g.movePlayer(this, direction)){
					if(g.checkPlayerInRoom(this)){
						counter = 0;
						// TODO: place Piece into random non-doorway unused Location in the room
					}
					else{
						counter--;
					}
					g.drawBoard();
				}
				else{
					System.out.println("Cannot move in that direction, please try again.");
				}
			} else {
				System.out.println("Invalid input, please try again.");
			}
		}
	}
	
	private Card isCard(String card, Card.CardType ct) {
		Card returnCard = g.getCard(card);
		if(returnCard != null) {
			if(returnCard.getType().equals(ct)) {
				return returnCard;
			}
			return returnCard;
		}
		return returnCard;
	}
	
	public void clearHand() { hand.clear(); }
	
	public String toString() { return "name: " + name + ", in hand: " + hand.toString(); }
	
	public Card suggest(Game game) {
		Scanner scan = g.getScanner();
		System.out.println("Make a suggestion - type in 3 cards:");
		Card firstCard = askForCard(game);
		Card secondCard = askForCard(game);
		Card thirdCard = askForCard(game);
		CardTuple suggestion = new CardTuple(firstCard, secondCard, thirdCard);
		return game.refutationProcess(this, suggestion);
	}
	
	public Card askForCard(Game game) {
		Scanner scan = g.getScanner();
		System.out.print("Card: ");
		Card card = game.getCard(scan.nextLine());
		while (card == null) {
			System.out.println("Invalid card. Try again.\nCard:");
			card = game.getCard(scan.nextLine());
		}
		return card;
	}
	
	public Card refute(CardTuple tup) {
		Set<Card> refuteOptions = new HashSet<Card>();
		Card refuteCard = null;
		
		for(Card c : hand.values()) {
			if(tup.characterCard().equals(c)) {
				refuteOptions.add(c);
			}
			if(tup.weaponCard().equals(c)) {
				refuteOptions.add(c);;
			}
			if(tup.roomCard().equals(c)) {
				refuteOptions.add(c);
			}
			
			
		}
		
		if(!refuteOptions.isEmpty()) {
			Scanner scan = new Scanner(System.in);
			System.out.println("What card would you like to refute with? (W/C/R)");
			for(Card c : refuteOptions) {
				System.out.println(c.getName());
			}
			
			while(refuteCard == null) {
				String choice = scan.nextLine().toUpperCase();
				
				if(choice.equals("W") && tup.weaponCard() != null) {
					refuteCard = tup.weaponCard();
				}
				
				if(choice.equals("C") && tup.weaponCard() != null) {
					refuteCard =  tup.characterCard();
				}
				
				if(choice.equals("R") && tup.weaponCard() != null) {
					refuteCard = tup.roomCard();
				}
				
				else { System.out.println("Not a valid option please type either W,C or R");}
			}
			
		}

		return refuteCard;
	}
	
	public void printCards() {
		for(Card c : hand.values()) {
			System.out.println(c.getName());
		}
	}
	
	public boolean accuse(Game game) {
		if (!canAccuse) {
			System.out.println("You have already made an accusation before, and cannot make another.");
			return false;
		} else {
			System.out.println("Make a suggestion - type in 3 cards:");
			CardTuple accusation = getThreeCards(game);
			canAccuse = false;
			return game.checkAccusation(accusation);
		}
	}
	
	public CardTuple getThreeCards(Game game) {
		Scanner scan = new Scanner(System.in);
		Card charCard;
		System.out.print("Character: ");
	    charCard = isCard(scan.nextLine(),Card.CardType.CHARACTER);
	    if(charCard == null) {
	    	while(charCard == null) {
	    		System.out.println("Not a valid Character card try again: ");
	    		charCard = isCard(scan.nextLine(),Card.CardType.CHARACTER);
	    	}
	    }
	    
	    Card weapCard;
		System.out.print("Weapon: ");
	    weapCard = isCard(scan.nextLine(),Card.CardType.WEAPON);
	    if(weapCard == null) {
	    	while(weapCard == null) {
	    		System.out.println("Not a valid Weapon card try again: ");
	    		weapCard = isCard(scan.nextLine(),Card.CardType.WEAPON);
	    	}
	    }
	    
	    Card roomCard;
		System.out.print("Room: ");
	    roomCard = isCard(scan.nextLine(),Card.CardType.ROOM);
	    if(roomCard == null) {
	    	while(roomCard == null) {
	    		System.out.println("Not a valid room card try again: ");
	    		roomCard = isCard(scan.nextLine(),Card.CardType.ROOM);
	    	}
	    }
	    
	    roomCard = g.getCard(g.getPlayerRoom(this).getName());
	    return new CardTuple(charCard,weapCard,roomCard);
		
	}
}
