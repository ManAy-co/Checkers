package Chackers;

public class Piece {
    enum PieceColor 
    {
        BLUE,
        RED 
    }
    private PieceColor color;
    private boolean king;

    public Piece( PieceColor color )
    {
        this.color = color;
        king = false;
    }
    
    void promote()
    {
        if( ! king )
        {
                king = true;
        }
    }

    boolean isking()
    {
        return king;
    }

    PieceColor getColor()
    {
        return color;
    }
}
