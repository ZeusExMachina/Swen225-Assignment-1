import java.io.*;
import java.util.*;

/**
 * Represents the Board in which the pieces and players move around.
 *
 * @author Jared Boult
 */
public class Board {
    public static final int WIDTH = 24;
    public static final int HEIGHT = 25;

    Location[][] currentBoard = new Location[HEIGHT][WIDTH];
    StringBuilder[] printableBoard = new StringBuilder[HEIGHT*2+1];
    Map<String, Room> rooms = new HashMap<>();
    Map<String, Piece> pieces = new HashMap<>();

    /**
     * Constructor for the game's Board
     */
    public Board(){
        setupRooms();
        loadBoard();
        setupPieces();
        calculateRoomEntrancesAndExits();
    }

    /**
     * Creates the Room objects for the default Cluedo board
     */
    private void setupRooms(){
        String[] defaultRoomNames = {"Kitchen", "Ball Room", "Conservatory",
                "Billiard Room", "Dining Room", "Library",
                "Hall", "Lounge", "Study", "Passageway"};
        for(String roomName : defaultRoomNames){
            rooms.put(roomName, new Room(roomName));
        }
    }

    /**
     * Reads the Board data from a text file and constructs
     * a 2D array of Location objects that contain each square's
     * Room, wall and Piece information.
     */
    private void loadBoard(){
        try {
            File file = new File("standard_board.txt");
            Scanner scan = new Scanner(file);
            scan.useDelimiter(",");
            for (int row = 0; row < HEIGHT; row++) {
                for(int col = 0; col < WIDTH; col++) {
                    String cellData = scan.next();
                    String roomDescription = cellData;
                    String walls = "";
                    if (cellData.contains("_")) {
                        String[] temp = cellData.split("_");
                        roomDescription = temp[0];
                        walls = temp[1];
                    }

                    Room currentRoom = null;
                    if (roomDescription.equals("P")) {
                        currentRoom = rooms.get("Passageway");
                    } else if (!roomDescription.equals("X")) {
                        currentRoom = rooms.get(roomDescription);
                    }

                    currentBoard[row][col] = new Location(currentRoom, walls, row, col);
                    if(currentRoom != null){
                        currentRoom.addLocation(currentBoard[row][col]);
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("Error reading file \"standard_board.txt\".");
        }
    }

    /**
     * Creates the pieces that can be moved around the Board during the game.
     */
    private void setupPieces(){
        // The characters start at the same position every game.
        pieces.put("Miss Scarlet", new Piece("S", currentBoard[24][7]));
        pieces.put("Colonel Mustard", new Piece("M", currentBoard[17][0]));
        pieces.put("Mrs White", new Piece("W", currentBoard[0][9]));
        pieces.put("Mr Green", new Piece("G", currentBoard[0][14]));
        pieces.put("Mrs Peacock", new Piece("P", currentBoard[6][23]));
        pieces.put("Professor Plum", new Piece("L", currentBoard[19][23]));

        // The weapons start in random positions every game.
        List<Location> startLocations = getStartLocationsForWeapons();
        pieces.put("Candlestick", new Piece("c", startLocations.get(0)));
        pieces.put("Dagger", new Piece("d", startLocations.get(1)));
        pieces.put("Lead Pipe", new Piece("l", startLocations.get(2)));
        pieces.put("Revolver", new Piece("g", startLocations.get(3)));
        pieces.put("Rope", new Piece("r", startLocations.get(4)));
        pieces.put("Spanner", new Piece("s", startLocations.get(5)));
    }

    /**
     * A helper function that provides a list of unused Room
     * locations in a random order for the initial placement
     * of weapon pieces.
     *
     * @return A list containing one random unused location
     *         inside each Room (minus the Passageway)
     */
    private List<Location> getStartLocationsForWeapons(){
        List<Location> starting = new ArrayList<>();
        List<Room> allRooms = new ArrayList<Room>(rooms.values());
        Collections.shuffle(allRooms);
        for(Room room : allRooms){
            if(!room.getName().equals("Passageway")) {
                starting.add(room.getRandomRoomLocation());
            }
        }
        return starting;
    }

    /**
     * Places the piece icon onto the Board according
     * to the piece's Location.
     */
    private void placePieces(){
        for (Piece p : pieces.values()){
            int boardRow = p.location().point.y;
            int boardColumn = p.location().point.x;
            int charIndex = 4 * (boardColumn+1) - 2;
            int charRow = 2*boardRow+1;
            printableBoard[charRow].replace(charIndex, charIndex+1, p.icon());
        }
    }

    /**
     * Print the Board out to the console
     */
    public void draw(){
        placeWallsAndRooms();
        placePieces();
        for(StringBuilder s : printableBoard){
            System.out.println(s);
        }
    }

    /**
     * Interprets the wall data for every Location in the
     * 2D array to construct a visual representation of
     * the Rooms out of "+" and "#" characters
     */
    private void placeWallsAndRooms(){
        for(int row = 0; row < HEIGHT; row++){
            int row1 = 2*row;
            int row2 = row1 + 1;
            int row3 = row2 + 1;
            printableBoard[row1] = new StringBuilder();
            for(int col = 0; col < WIDTH; col++){
                Location location = currentBoard[row][col];
                if(wallIsAdjacent(location) || location.northWall || location.westWall){
                    printableBoard[row1].append("+");
                }
                else{
                    printableBoard[row1].append(" ");
                }
                if(location.northWall){
                    printableBoard[row1].append("###");
                }
                else{
                    printableBoard[row1].append("   ");
                }
                if(location.point.x == WIDTH - 1){
                    if(wallIsAdjacent(location) || location.northWall || location.eastWall){
                        printableBoard[row1].append("+");
                    }
                    else{
                        printableBoard[row1].append(" ");
                    }
                }
            }
            printableBoard[row2] = new StringBuilder();
            for(int col = 0; col < WIDTH; col++){
                Location location = currentBoard[row][col];
                if(location.westWall){
                    printableBoard[row2].append("#   ");
                }
                else{
                    printableBoard[row2].append("    ");
                }
                if(location.point.x == WIDTH - 1){
                    if(location.eastWall){
                        printableBoard[row2].append("#");
                    }
                    else{
                        printableBoard[row2].append(" ");
                    }
                }
            }
            if(row == HEIGHT - 1){
                printableBoard[row3] = new StringBuilder();
                for(int col = 0; col < WIDTH; col++){
                    Location location = currentBoard[row][col];
                    if(location.southWall || location.westWall){
                        printableBoard[row3].append("+");
                    }
                    else{
                        printableBoard[row3].append(" ");
                    }
                    if(location.southWall){
                        printableBoard[row3].append("###");
                    }
                    else{
                        printableBoard[row3].append("   ");
                    }
                    if(location.point.x == WIDTH - 1){
                        if(wallIsAdjacent(location) || location.southWall || location.eastWall){
                            printableBoard[row3].append("+");
                        }
                        else{
                            printableBoard[row3].append(" ");
                        }
                    }
                }
            }
        }
    }

    /**
     * Place the name of the Rooms onto the Board
     */
    private void drawLabels(){}

    /**
     * Helper function for placeWallsAndRooms(), determines
     * if the corner of an upper-left Locations needs to be drawn.
     * @param location
     * @return True if the upper-left Location has a south or east Wall
     */
    private boolean wallIsAdjacent(Location location){
        if(location.room != null && location.room.getName().equals("Passageway")){
            return true;
        }
        int x = location.point.x;
        int y = location.point.y;
        if(x > 0 && y > 0){
            Location upperRightLocation = currentBoard[y-1][x-1];
            return upperRightLocation.southWall || upperRightLocation.eastWall;
        }
        return false;
    }

    public void printLegend(){
        System.out.println("--- Legend ---");
        for(Map.Entry<String, Piece> entry : pieces.entrySet()){
            System.out.println(entry.getValue().icon()+" : " + entry.getKey());
        }
    }

    /**
     * Moves
     * @param suggestion
     */
    public void movePiece(CardTuple suggestion){
        Piece suggestedCharacter = pieces.get(suggestion.characterCard().getName());
        Room suggestedRoom = rooms.get(suggestion.roomCard().getName());
        Piece suggestedWeapon = pieces.get(suggestion.weaponCard().getName());
        if(suggestedCharacter.location().room != suggestedRoom){
            suggestedCharacter.setLocation(suggestedRoom.getRandomRoomLocation());
        }
        if(suggestedWeapon.location().room != suggestedRoom){
            suggestedCharacter.setLocation(suggestedRoom.getRandomRoomLocation());
        }
    }

    /**
     * Store the entrance and exit squares between a room like
     * the Study and the Passageway within each Room.
     */
    public void calculateRoomEntrancesAndExits(){
        for(Room room : rooms.values()){
            for(Location loc : room.getLocations()){
                int col = loc.point.x;
                int row = loc.point.y;
                if(!loc.northWall && row > 0){
                    Location possibleExit = currentBoard[row-1][col];
                    if(possibleExit.room != null &&
                            possibleExit.room.getName().equals("Passageway")){
                        room.addEntrance(loc);
                        room.addExit(possibleExit);
                    }
                }
                if(!loc.eastWall && col < WIDTH){
                    Location possibleExit = currentBoard[row][col+1];
                    if(possibleExit.room != null &&
                            possibleExit.room.getName().equals("Passageway")) {
                        room.addEntrance(loc);
                        room.addExit(possibleExit);
                    }
                }
                if(!loc.southWall && row < HEIGHT){
                    Location possibleExit = currentBoard[row+1][col];
                    if(possibleExit.room != null &&
                            possibleExit.room.getName().equals("Passageway")){
                        room.addEntrance(loc);
                        room.addExit(possibleExit);
                    }
                }
                if(!loc.westWall && col > 0){
                    Location possibleExit = currentBoard[row][col-1];
                    if(possibleExit.room != null &&
                            possibleExit.room.getName().equals("Passageway")){
                        room.addEntrance(loc);
                        room.addExit(possibleExit);
                    }
                }
            }
        }
    }


    /**
     * Performs a move and returns true, if the move is illegal
     * no move is performed and it returns false.
     *
     * @param player The player to move
     * @param direction String containing one of W,A,S,D to indicate
     *                  direction of movement
     * @return True if the move was completed
     */
    public boolean movePlayer(Player player, String direction){
        Piece playerPiece = pieces.get(player.getName());
        Location playerLocation = playerPiece.location();
        int x = playerLocation.point.x;
        int y = playerLocation.point.y;

        switch(direction){
            case "W":
                if(playerLocation.canMoveUp(this)){
                    Location destination = currentBoard[y-1][x];
                    // Ensure a piece is never blocking another piece from entering the room
                    if(!destination.room.getName().equals("Passageway")){
                        destination = destination.room.getRandomRoomLocation();
                    }
                    playerPiece.setLocation(destination);
                    return true;
                }
                break;
            case "A":
                if(playerLocation.canMoveLeft(this)){
                    Location destination = currentBoard[y][x-1];
                    if(!destination.room.getName().equals("Passageway")){
                        destination = destination.room.getRandomRoomLocation();
                    }
                    playerPiece.setLocation(destination);
                    return true;
                }
                break;
            case "S":
                if(playerLocation.canMoveDown(this)){
                    Location destination = currentBoard[y+1][x];
                    if(!destination.room.getName().equals("Passageway")){
                        destination = destination.room.getRandomRoomLocation();
                    }
                    playerPiece.setLocation(destination);
                    return true;
                }
                break;
            case "D":
                if(playerLocation.canMoveRight(this)){
                    Location destination = currentBoard[y][x+1];
                    if(!destination.room.getName().equals("Passageway")){
                        destination = destination.room.getRandomRoomLocation();
                    }
                    playerPiece.setLocation(destination);
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Returns if a player is in one of the Rooms which is not
     * a passageway.
     * @param player The player
     * @return True if the player is in a Room
     */
    public boolean checkPlayerInRoom(Player player){
        Room playerRoom = pieces.get(player.getName()).location().room;
        return playerRoom != null && !playerRoom.equals(rooms.get("Passageway"));
    }

    /**
     * Returns the Room a player is currently in
     * @param player The player
     * @return A Room object
     */
    public Room getPlayerRoom(Player player){
        return pieces.get(player.getName()).location().room;
    }

}
