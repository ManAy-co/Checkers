package Checkers;

import Checkers.Piece.PieceColor;

// Represents a player in the game
public class Player {
    private String id;
    private PieceColor color;
    private int remainingPieces;

    // Constructor initializing player details and starting piece count
    public Player( String id , PieceColor color )
    {
        this.id = id;
        this.color = color;
        remainingPieces = 12;
    }

    public String getId()
    {
        return id;
    }

    public PieceColor getColor()
    {
        return color;
    }

    public int getRemainingPieces()
    {
        return remainingPieces;
    }

    // Reduces the player's remaining piece count when captured
    public void losePiece()
    {
        if ( remainingPieces > 0 )
        {
            remainingPieces--;
        }
    }

    // Checks if the player has lost all pieces
    public boolean hasLost()
    {
         return remainingPieces == 0;
    }
    public void setRemainingPieces(int remainingPieces) 
    {
        this.remainingPieces = remainingPieces;
    }
}