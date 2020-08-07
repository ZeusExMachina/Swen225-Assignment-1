import java.util.*;

/**
 * The Room class represents the various Rooms on the Cluedo
 * board.
 *
 * @author Jared Boult
 */
public class Room {
    private String name;
    private Set<Location> locations = new HashSet<>();

    /**
     * Constructor for the Room class
     *
     * @param name The name of the Room
     */
    Room(String name){
        this.name = name;
    }

    /**
     * Getter for the name of the Room
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a Location in the Set of Locations that
     * belong to this Room.
     *
     * @param location A Location that exists within this Room
     */
    public void addLocation(Location location){
        locations.add(location);
    }

    /**
     * Get a random unoccupied Location within this Room, usually
     * this is called when a Piece is suggested and needs to move
     * to this Room.
     *
     * @return - A random unoccupied Location
     */
    public Location getRandomRoomLocation(){
        List<Location> allLocations = new ArrayList<>(locations);
        Collections.shuffle(allLocations);
        for (Location loc : allLocations) {
            if (!loc.occupied) {
                return loc;
            }
        }
        return null;
    }
}
