
public class Piece {
	private String displayIcon;
	private Location location;
	
	public Piece(String icon, Location loc) {
		this.displayIcon = icon;
		this.location = loc;
	}
	
	public String icon() { return displayIcon; }
	
	public Location location() { return location; }
}
