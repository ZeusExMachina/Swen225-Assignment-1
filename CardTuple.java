import java.util.*;

/**
 * Holds a tuple of 3 Cards, with each card being of a 
 * different type.
 * 
 * @author Elijah Guarina
 */
public class CardTuple {
	private Card charCard, weapCard, roomCard;
	
	public CardTuple(Card first, Card second, Card third) {
		for (int i = 0; i < 3; i++) {
			if (i == 0) { setCard(first); }
			if (i == 1) { setCard(second); }
			if (i == 2) { setCard(third); }
		}
	}
	
	public Card characterCard() { return charCard; }
	
	public Card weaponCard() { return weapCard; }
	
	public Card roomCard() { return roomCard; }
	
	/**
	 * Set a given card as one of the cards for this tuple.
	 * 
	 * @param card is the card to set
	 */
	private void setCard(Card card) {
		if (card.getType() == Card.CardType.CHARACTER) { 
			if (charCard != null) { throw new IllegalArgumentException("Duplicate card types found. CardTuple only accepts 1 card of each type. Cards type: " + card.getType()); }
			charCard = card;
		}
		if (card.getType() == Card.CardType.WEAPON) { 
			if (weapCard != null) { throw new IllegalArgumentException("Duplicate card types found. CardTuple only accepts 1 card of each type. Cards type: " + card.getType()); }
			weapCard = card;
		}
		if (card.getType() == Card.CardType.ROOM) {
			if (roomCard != null) { throw new IllegalArgumentException("Duplicate card types found. CardTuple only accepts 1 card of each type. Cards type: " + card.getType()); }
			roomCard = card;
		}
	}
	
	/**
	 * Checks whether the cards given each have a different 
	 * type (i.e. 1 character, 1 weapon, and 1 room card)
	 * 
	 * @return whether the cards are valid for a tuple
	 */
	public static boolean validTuple(Card first, Card second, Card third) {
		Set<Card.CardType> cardTypesSeen = new HashSet<Card.CardType>();
		for (int i = 0; i < 3; i++) {
			if (i == 0) { cardTypesSeen.add(first.getType()); }
			if (i == 1) { cardTypesSeen.add(second.getType()); }
			if (i == 2) { cardTypesSeen.add(third.getType()); }
		}
		if (cardTypesSeen.size() < 3) { return false; }
		else { return true; }
	}
	
	public String toString() {
		return "character: " + charCard.toString() + ", weapon: " + weapCard.toString() + ", roomCard: " + roomCard.toString();
	}
}
