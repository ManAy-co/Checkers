package Checkers;
import Checkers.Tile.Color;

public class Board {

    private final Tile[][] BOARD;
    public Board() {
        BOARD = new Tile[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        for(int i = 0 ; i < 8 ; i++) {
            for(int j = 0 ; j < 8 ; j++){
            Color color;
            if((i+j) % 2 == 0)
                color = Color.DARK;
            else
                color = Color.LIGHT;
            BOARD[i][j] = new Tile(color, new Tile.Position(i , j), Tile.Status.EMPTY);
            }
        }
    }

    public Tile getTile(int row, int col) {
        
    }
}
