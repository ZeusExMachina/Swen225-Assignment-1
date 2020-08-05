import java.awt.*;

public class Location {
    Room room;
    boolean northWall;
    boolean eastWall;
    boolean southWall;
    boolean westWall;
    Point point;

    public Location(Room room, String walls, int boardRow, int boardColumn){
        this.room = room;
        this.northWall = walls.contains("N");
        this.eastWall = walls.contains("E");
        this.southWall = walls.contains("S");
        this.westWall = walls.contains("W");
        this.point = new Point(boardColumn, boardRow);
    }

    public boolean canMoveUp(){
        return !northWall && point.y > 0;
    }

    public boolean canMoveDown(){
        return !southWall && point.y < Board.HEIGHT;
    }
    public boolean canMoveRight(){
        return !eastWall && point.x > Board.WIDTH;
    }
    public boolean canMoveLeft(){
        return !westWall && point.x > 0;
    }
}
