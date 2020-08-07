import java.util.*;

public class Room {
    private String name;
    private Set<Location> locations = new HashSet<>();

    Room(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location){
        locations.add(location);
    }

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
