/* This class contains an array of tiles(so we import "Tile.java" class)
and initialize them with pieces(so we import "Piece.java")
and most of the game's logics for diffrent types of movements. */

package Checkers;
import Checkers.Piece.PieceColor;
import Checkers.Tile.Color;

public class Board {
    //declearing size of board(unchangable)
    private final Tile[][] BOARD;
    //constructor
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
    //for making sure that pieces are inside the decleared area:
    public boolean isValidPosition(int row , int col) {
        return row >= 0 && row < 8
        && col >= 0 && col < 8; 
    }
    //tile getter
    public Tile getTile(int row, int col) {
        if(isValidPosition(row, col))
        return BOARD[row][col];
    return null;
    }
    //Places a piece on a tile and changes the tile's status
    public void placePiece(int row, int col, Piece piece) {
        if (!isValidPosition(row, col))
            return;

        BOARD[row][col].setPiece(piece);
        BOARD[row][col].setStatus(Tile.Status.OCCUPIED);
    }
    //removes a piece from the board and changes the tile's status
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
    //piece getter
    public Piece getPiece(int row, int col) {
        Tile tile = getTile(row, col);
        if(tile == null)
            return null;

        return tile.getPiece();
    }

    //movements' rules:

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        //the position of beginnig and destinition should be valid first:

        if(!isValidPosition(fromRow, fromCol))
            return false;
        if(!isValidPosition(toRow, toCol))
            return false;

        Piece piece = getPiece(fromRow, fromCol);
        //the piece should exist(first condition) on chosen tile(second condition):
        if(piece == null)
            return false;
        if(!isEmpty(toRow, toCol))
            return false;

        //pieces can only be on dark tiles
        if(getTile(toRow, toCol).getColor() != Tile.Color.DARK)
            return false;

        int rowDistance = toRow - fromRow;
        int colDistance = toCol - fromCol;

        //You can not move a checker backwards until it becomes a King
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
    //Basic movement is to move a checker one space diagonally forward
    if(Math.abs(colDistance) != 1)
        return false;
    return true;
    }

    public boolean isValidJump(int fromRow, int fromCol, int toRow, int toCol) {
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
                if(rowDistance != 2)
                return false;
        } else {
            if(rowDistance != -2)
                return false;
        }
    } else {
        if(Math.abs(rowDistance) != 2)
            return false;
    }

    if(Math.abs(colDistance) != 2)
        return false;

    int middleRow = (fromRow + toRow) / 2;
    int middleCol = (fromCol + toCol) / 2;
    //jump can happen when:
    Piece middlePiece = getPiece(middleRow, middleCol);
    //1. there's a piece between beginning and destinition
    if(middlePiece == null)
        return false;
    //2. that's your opponent’s checker
    if(middlePiece.getColor() == piece.getColor())
        return false;

    return true;
    }
    //Moves a piece on the board:
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {

    Piece piece = getPiece(fromRow, fromCol);

    if(piece == null)
        return false;

    //Handles both normal moves and captures:

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

    // Promotes the piece to King if it reaches the last row:
    
    if(piece.getColor() == PieceColor.RED && toRow == 7)
        piece.promote();

    if(piece.getColor() == PieceColor.BLUE && toRow == 0)
        piece.promote();

    return true;
    }

    public boolean canJumpAgain(int row, int col) {

    Piece piece = getPiece(row, col);

    if(piece == null)
        return false;

    if(piece.isKing()) {

        if(isValidJump(row, col, row + 2, col + 2))
            return true;

        if(isValidJump(row, col, row + 2, col - 2))
            return true;

        if(isValidJump(row, col, row - 2, col + 2))
            return true;

        if(isValidJump(row, col, row - 2, col - 2))
            return true;
    } else {

        if(piece.getColor() == PieceColor.RED) {

            if(isValidJump(row, col, row + 2, col + 2))
                return true;

            if(isValidJump(row, col, row + 2, col - 2))
                return true;
        }

        else {

            if(isValidJump(row, col, row - 2, col + 2))
                return true;

            if(isValidJump(row, col, row - 2, col - 2))
                return true;
        }
    }
    return false;
}

    public boolean hasCapture(PieceColor color) {
    for(int row = 0 ; row < 8 ; row++) {
        for(int col = 0 ; col < 8 ; col++) {
            Piece piece = getPiece(row, col);

            if(piece == null)
                continue;

            if(piece.getColor() != color)
                continue;

            if(canJumpAgain(row, col))
                return true;
        }
    }
    return false;
    }

    public boolean hasAnyMove(PieceColor color) {

    for(int row = 0 ; row < 8 ; row++) {
        for(int col = 0 ; col < 8 ; col++) {

            Piece piece = getPiece(row, col);

            if(piece == null)
                continue;

            if(piece.getColor() != color)
                continue;

            if(canJumpAgain(row, col))
                return true;

            if(piece.isKing()) {

                if(isValidMove(row, col, row + 1, col + 1))
                    return true;

                if(isValidMove(row, col, row + 1, col - 1))
                    return true;

                if(isValidMove(row, col, row - 1, col + 1))
                    return true;

                if(isValidMove(row, col, row - 1, col - 1))
                    return true;
            }
            else if(piece.getColor() == PieceColor.RED) {

                if(isValidMove(row, col, row + 1, col + 1))
                    return true;

                if(isValidMove(row, col, row + 1, col - 1))
                    return true;
            }
            else {

                if(isValidMove(row, col, row - 1, col + 1))
                    return true;

                if(isValidMove(row, col, row - 1, col - 1))
                    return true;
            }
        }
    }
    return false;
}
    //for checking wheter the movement was a jump(it will be used in "Game.java"):
    public boolean wasJump(int fromRow, int fromCol, int toRow, int toCol) {
    return Math.abs(toRow - fromRow) == 2 && Math.abs(toCol - fromCol) == 2;
    }
}