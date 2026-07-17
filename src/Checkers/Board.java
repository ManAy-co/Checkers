package Checkers;
import Checkers.Tile.*;
import Checkers.Piece.*;

public class Board {

    private final Tile[][] BOARD;
    public Board() {
        BOARD = new Tile[8][8];
        initializeBoard();
        initializePieces();
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

    private void initializePieces() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                if (BOARD[i][j].getColor() == Color.DARK)
                    placePiece(i , j , new Piece(PieceColor.RED));
        }
    }
    for(int i = 5 ; i < 8 ; i++) {
        for(int j = 0 ; j < 8 ; j++) {
            if(BOARD[i][j].getColor() == Color.DARK)
                placePiece(i, j, new Piece(PieceColor.BLUE));
        }
    }
}

    public boolean isValidPosition(int row , int col) {
        return row >= 0 && row < 8
        && col >= 0 && col < 8; 
    }

    public Tile getTile(int row, int col) {
        if(isValidPosition(row, col))
        return BOARD[row][col];
    return null;
    }

    public void placePiece(int row, int col, Piece piece) {
        if (!isValidPosition(row, col))
            return;

        BOARD[row][col].setPiece(piece);
        BOARD[row][col].setStatus(Tile.Status.OCCUPIED);
    }

    public void removePiece(int row, int col) {
        if (!isValidPosition(row, col))
            return;

        BOARD[row][col].setPiece(null);
        BOARD[row][col].setStatus(Tile.Status.EMPTY);
    }

    public boolean isEmpty(int row, int col) {
        Tile tile = getTile(row, col);
        if(tile == null)
            return false;

        return tile.getStatus() == Tile.Status.EMPTY;
    }

    public Piece getPiece(int row, int col) {
        Tile tile = getTile(row, col);
        if(tile == null)
            return null;

        return tile.getPiece();
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if(!isValidPosition(fromRow, fromCol))
            return false;
        if(!isValidPosition(toRow, toCol))
            return false;

        Piece piece = getPiece(fromRow, fromCol);

        if(piece == null)
            return false;

        if(!isEmpty(toRow, toCol))
            return false;

        if(getTile(toRow, toCol).getColor() != Tile.Color.DARK)
            return false;

        int rowDistance = toRow - fromRow;
        int colDistance = toCol - fromCol;

        if(!piece.isKing()) {
            if(piece.getColor() == PieceColor.RED) {
                if(rowDistance != 1)
                return false;
        } else {
            if(rowDistance != -1)
                return false;
        }
    } else {
        if(Math.abs(rowDistance) != 1)
            return false;
    }

    if(Math.abs(colDistance) != 1)
        return false;

    int middleRow = (fromRow + toRow) / 2;
    int middleCol = (fromCol + toCol) / 2;

    Piece middlePiece = getPiece(middleRow, middleCol);

    if(middlePiece == null)
        return false;

    if(middlePiece.getColor() == piece.getColor())
        return false;

    return true;
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {

    Piece piece = getPiece(fromRow, fromCol);

    if(piece == null)
        return false;

    if(isValidMove(fromRow, fromCol, toRow, toCol)) {
        removePiece(fromRow, fromCol);
        placePiece(toRow, toCol, piece);
        }
    else if(isValidJump(fromRow, fromCol, toRow, toCol)) {
        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;
        removePiece(fromRow, fromCol);
        removePiece(middleRow, middleCol);
        placePiece(toRow, toCol, piece);
        }
    else
        return false;

    if(piece.getColor() == PieceColor.RED
            && toRow == 7)
        piece.promote();

    if(piece.getColor() == PieceColor.BLUE
            && toRow == 0)
        piece.promote();

    return true;
    }
}