import java.util.HashSet;
import java.util.Set;

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
}
