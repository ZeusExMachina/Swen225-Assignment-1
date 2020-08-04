
public class Card {
	private final String name;
	private final CardType type;
	
	public static enum CardType { CHARACTER, WEAPON, ROOM; }
	
	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
	}
	
	public String name() { return name; }
	public CardType type() { return type; }
	
	public String toString() { return "name: " + name + ", type: " + type; }
}
