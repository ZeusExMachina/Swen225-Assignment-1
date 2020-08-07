import java.awt.*;

/**
 * This Location class holds all the information about each
 * square on the Cludeo board.
 *
 * @author Jared Boult
 */
public class Location {
    Room room;
    boolean northWall;
    boolean eastWall;
    boolean southWall;
    boolean westWall;
    Point point;
    boolean occupied;

    public Location(Room room, String walls, int boardRow, int boardColumn){
        this.room = room;
        this.northWall = walls.contains("N");
        this.eastWall = walls.contains("E");
        this.southWall = walls.contains("S");
        this.westWall = walls.contains("W");
        this.point = new Point(boardColumn, boardRow);
    }

    public boolean canMoveUp(Board board){
        if(point.y <= 0){
            return false;
        }
        boolean squareIsFree = board.currentBoard[point.y-1][point.x].occupied;
        return !northWall && squareIsFree;
    }

    public boolean canMoveDown(Board board){
        if(point.y >= Board.HEIGHT){
            return false;
        }
        boolean squareIsFree = board.currentBoard[point.y+1][point.x].occupied;
        return !southWall && squareIsFree;
    }

    public boolean canMoveRight(Board board){
        if(point.x <= Board.WIDTH){
            return false;
        }
        boolean squareIsFree = board.currentBoard[point.y][point.x+1].occupied;
        return !eastWall && squareIsFree;
    }

    public boolean canMoveLeft(Board board){
        if(point.x <= 0){
            return false;
        }
        boolean squareIsFree = board.currentBoard[point.y][point.x-1].occupied;
        return !westWall && squareIsFree;
    }
}
