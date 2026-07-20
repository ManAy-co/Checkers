package Checkers;

public class Piece {
    private PieceColor color;
    private boolean king;

    public Piece( PieceColor color )
    {
        this.color = color;
        king = false;
    }

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
