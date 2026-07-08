package Checkers;

import Checkers.Piece.PieceColor;

public class Player {
    private String id;
    private PieceColor color;
    private int remainingPieces;

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

    public void losePiece()
    {
        if ( remainingPieces > 0 )
        {
            remainingPieces--;
        }
    }
    public boolean hasLost()
    {
         return remainingPieces == 0;
    }
}
