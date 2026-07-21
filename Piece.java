package Checkers;

// Represents a checker piece in the game
public class Piece {
    public enum PieceColor 
    {
        BLUE,
        RED 
    }
    private PieceColor color;
    private boolean king;

    // Constructor to initialize piece color and king status
    public Piece( PieceColor color )
    {
        this.color = color;
        king = false;
    }

    // Promotes the piece to a King
    public void promote()
    {
        king = true;
    }

    public boolean isKing()
    {
        return king;
    }

    public PieceColor getColor()
    {
        return color;
    }
}