import java.io.*;
import java.util.*;

public class Board {
    public static final int WIDTH = 24;
    public static final int HEIGHT = 25;

    Location[][] currentBoard = new Location[HEIGHT][WIDTH];
    Map<String, Room> rooms = new HashMap<>();

    public Board(){
        setupRooms();
        loadBoard();
        System.out.println("Finished loading the board :)");
        this.draw();
    }

    private void setupRooms(){
        String[] defaultRoomNames = {"Kitchen", "Ball Room", "Conservatory",
                                    "Billiard Room", "Dining Room", "Library",
                                    "Hall", "Lounge", "Study", "Passageway"};
        for(String roomName : defaultRoomNames){
            rooms.put(roomName, new Room(roomName));
        }
    }

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
                }
            }
        }
        catch (IOException e){System.out.println("Error reading file");}
    }

    public Location[][] getCurrentBoard() {
        return currentBoard;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void draw(){
        drawWalls();
        drawLabels();
        drawPieces();
    }

    private void drawWalls(){
        for(int row = 0; row < HEIGHT; row++){
            System.out.println();
            for(int col = 0; col < WIDTH; col++){
                Location location = currentBoard[row][col];
                if(wallIsAdjacent(location) || location.northWall || location.westWall){
                    System.out.print("+");
                }
                else{
                    System.out.print(" ");
                }
                if(location.northWall){
                    System.out.print("###");
                }
                else{
                    System.out.print("   ");
                }
                if(location.point.x == 23){
                    if(wallIsAdjacent(location) || location.northWall || location.eastWall){
                        System.out.print("+");
                    }
                    else{
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
            for(int col = 0; col < WIDTH; col++){
                Location location = currentBoard[row][col];
                if(location.westWall){
                    System.out.print("#   ");
                }
                else{
                    System.out.print("    ");
                }
                if(location.point.x == 23){
                    if(location.eastWall){
                        System.out.print("#");
                    }
                    else{
                        System.out.print(" ");
                    }
                }
                if(location.point.y == 24){

                }
            }
        }
    }

    private void drawLabels(){

    }

    private void drawPieces(){

    }

    private boolean wallIsAdjacent(Location location){
        int x = location.point.x;
        int y = location.point.y;
        if(x > 0 && y > 0){
            Location upperRightLocation = currentBoard[y-1][x-1];
            return upperRightLocation.southWall || upperRightLocation.eastWall;
        }
        return false;
    }
}
