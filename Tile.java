/*This class declears posibilities of Tiles' color(Which is imported in Board.java class),
coordinate position(row and column),
and their status whether they include a piece or not.*/

package Checkers;
import Checkers.Piece.*;

public class Tile {
    //fields(attributes):
    private final Color color;
    private final Position position;
    private Status status;
    private Piece piece;

    //fields' declearing(enums and methods):

    public enum Color {
        LIGHT,
        DARK
    }

    public static class Position {
        private final int row;
        private final int col;

        //Position's getter methods:
        public int getRow() {
            return row;
        }
        public int getCol() {
            return col;
        }
        
        //Position's constructor:
        public Position(int row , int col) {
            this.col = col;
            this.row = row;
        }
    }

    public enum Status {
        EMPTY,
        OCCUPIED
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tile(Color color , Position position , Status status) {
        this.color = color;
        this.position = position;
        this.status = status;
    }

    public Piece getPiece() {
        return  piece;
    }

    public void setPiece(Piece piece) {
    this.piece = piece;
    if(piece == null)
        status = Status.EMPTY;
    else
        status = Status.OCCUPIED;
    }
<<<<<<< HEAD:src/Checkers/Tile.java
}
=======
}
>>>>>>> add7790a35c110647309e967dd08be64f6e11391:Tile.java
