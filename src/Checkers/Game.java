package Checkers;

public class Game {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean gameOver;
    private Board board;
    public Game()
    {
        board = new Board();
        player1 = new Player("Player 1", PieceColor.RED);
        player2 = new Player("Player 2", PieceColor.BLUE);
        currentPlayer = player1;
        gameOver = false;
    }
    public Board getBoard()
    {
        return board;
    }
    public Player  getCurrentPlayer()
    {
        return currentPlayer;
    }
    public boolean isGameOver()
    {
        return gameOver;
    }
    public void switchTurn()
    {
        if( currentPlayer == player1)
        {
            currentPlayer = player2;
        }
        else 
        {
            currentPlayer = player1;
        }
    }
    public void checkGameOver()
    {
        if(player1.hasLost())
        {
            gameOver = true;
            return;
        }
        if(player2.hasLost())
        {
            gameOver = true;
            return;
        }
        if(!board.hasAnyMove(currentPlayer.getColor()))
        {
            gameOver = true;
        }
    }
    public Player getWinner()
    {
    if(player1.hasLost())
        {
            return player2;
        }

    if(player2.hasLost())
        {
            return player1;
        }
        return null;
    }
    public boolean makeMove( int fromRow, int fromCol, int toRow, int toCol )
    {
        Piece piece = board.getPiece(fromRow, fromCol);
        if(piece == null)
            {
                return false;
            }

        if(piece.getColor() != currentPlayer.getColor())
            {
                return false;
            }
        boolean capture = board.wasJump( fromRow, fromCol, toRow, toCol );
        if(!board.movePiece(fromRow, fromCol, toRow, toCol))
            {
                return false;
            }
        if(capture)
            {
                if(currentPlayer == player1)
                    {
                        player2.losePiece();
                    }
                else
                    {
                        player1.losePiece();
                    }
        if(board.canJumpAgain(toRow, toCol))
            {
                checkGameOver();
                return true;
            }
    }

    switchTurn();
    checkGameOver();
    return true;
}
}
