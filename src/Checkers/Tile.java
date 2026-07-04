package Checkers;

public class Tile {

    private final Color color;
    private final Position position;
    private Status status;

    public enum Color {
        LIGHT,
        DARK
    }

    public static class Position {
        private final int row;
        private final int col;

        //getter methodes:
        public int getRow() {
            return row;
        }
        public int getCol() {
            return col;
        }
        

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
}
