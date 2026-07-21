package Checkers;
import Checkers.Piece.PieceColor;

// Manages the overall game state and logic
public class Game {
    private Player[] players;
    private Player currentPlayer;
    private boolean gameOver;
    private Board board;
    private Player winner;

    // Initializes a new game session
    public Game()
    {
        board = new Board();
        players = new Player[2];
        players[0] = new Player("Player 1", PieceColor.RED);
        players[1] = new Player("Player 2", PieceColor.BLUE);
        currentPlayer = players[0];
        gameOver = false;
        winner = null;
    }

    public Board getBoard()
    {
        return board;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    // Switches the current player's turn
    public void switchTurn()
    {
        if( currentPlayer == players[0])
        {
            currentPlayer = players[1];
        }
        else 
        {
            currentPlayer = players[0];
        }
    }

    // Checks win/loss conditions (no pieces or no valid moves left)
    public void checkGameOver()
    {
        if(players[0].hasLost())
        {
            gameOver = true;
            winner = players[1];
            return;
        }
        if(players[1].hasLost())
        {
            gameOver = true;
            winner = players[0];
            return;
        }
        if(!board.hasAnyMove(currentPlayer.getColor()))
        {
            gameOver = true;
            winner = (currentPlayer == players[0]) ? players[1] : players[0];
        }
    }

    public Player getWinner()
    {
        return winner;
    }

    // Handles executing a move, validating rules, captures, and turn transitions
    public boolean makeMove( int fromRow, int fromCol, int toRow, int toCol ) 
    {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || piece.getColor() != currentPlayer.getColor()) 
            {
                return false;
            }

        boolean isJumpMove = board.wasJump(fromRow, fromCol, toRow, toCol);

        // Enforces mandatory jump rule if a capture move is available
        if (board.hasCapture(currentPlayer.getColor()) && !isJumpMove) 
            {
                return false;
            }

        if (!board.movePiece(fromRow, fromCol, toRow, toCol)) 
            {
                return false;
            }

        // Handle capture logic and multi-jumps
        if (isJumpMove) 
            {
                Player opponent = (currentPlayer == players[0]) ? players[1] : players[0];
                opponent.losePiece();

                // Grant another turn if double/multi-jump is available
                if (board.canJumpAgain(toRow, toCol)) 
                    {
                        checkGameOver();
                        return true;
                    }
            }

        switchTurn();
        checkGameOver();
        return true;
    }

    // Resets the game to initial state
    public void startGame() 
       {
        this.gameOver = false;
        this.currentPlayer = players[0];
       }
}