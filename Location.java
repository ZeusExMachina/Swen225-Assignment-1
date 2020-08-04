public class Location {
    Room room;
    boolean northWall;
    boolean eastWall;
    boolean southWall;
    boolean westWall;
    int boardRow;
    int boardColumn;
    Piece piece;

    public Location(Room room, String walls, int boardRow, int boardColumn){
        this.room = room;
        this.northWall = walls.contains("N");
        this.eastWall = walls.contains("E");
        this.southWall = walls.contains("S");
        this.westWall = walls.contains("W");
        this.boardRow = boardRow;
        this.boardColumn = boardColumn;
    }

    public boolean canMoveUp(){
        return !northWall && boardRow > 0;
    }

    public boolean canMoveDown(){
        return !southWall && boardRow < Board.HEIGHT;
    }
    public boolean canMoveRight(){
        return !eastWall && boardColumn > Board.WIDTH;
    }
    public boolean canMoveLeft(){
        return !westWall && boardColumn > 0;
    }
}
