import java.util.Set;
import java.util.HashSet;

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
}
