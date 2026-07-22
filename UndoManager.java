package Checkers;

import java.util.Stack;

// Manages the undo history and state restoration
public class UndoManager {

    // Internal snapshot of the game state at a specific turn
    private static class GameState 
    {
        private final Piece[][] boardState;
        private final Player currentPlayer;
        private final int player1Pieces;
        private final int player2Pieces;

        // Constructor creating a deep copy of the current board and player stats
        public GameState( Board board, Player currentPlayer, Player player1, Player player2 )
        {
            this.currentPlayer = currentPlayer;
            this.player1Pieces = player1.getRemainingPieces();
            this.player2Pieces = player2.getRemainingPieces();

            this.boardState = new Piece[8][8];
            for ( int r = 0; r < 8; r++ ) 
            {
                for ( int c = 0; c < 8; c++ ) 
                {
                    Piece original = board.getPiece(r, c);
                    if ( original != null ) 
                    {
                        Piece copy = new Piece(original.getColor());
                        if ( original.isKing() ) 
                        {
                            copy.promote();
                        }
                        this.boardState[r][c] = copy;
                    }
                }
            }
        }

        public Piece[][] getBoardState()
        {
            return boardState;
        }

        public Player getCurrentPlayer()
        {
            return currentPlayer;
        }

        public int getPlayer1Pieces()
        {
            return player1Pieces;
        }

        public int getPlayer2Pieces()
        {
            return player2Pieces;
        }
    }

    private final Stack<GameState> history;

    // Initializes the undo history stack
    public UndoManager()
    {
        this.history = new Stack<>();
    }

    // Saves current state before a move is made
    public void saveState( Board board, Player currentPlayer, Player player1, Player player2 )
    {
        history.push(new GameState(board, currentPlayer, player1, player2));
    }

    // Checks if any previous states are available to undo
    public boolean canUndo()
    {
        return !history.isEmpty();
    }

    // Clears the history stack
    public void clearHistory()
    {
        history.clear();
    }

    // Reverts the game to its previous state
    public boolean undo( Game game )
    {
        if ( !canUndo() ) 
        {
            return false;
        }

        GameState previous = history.pop();
        Board board = game.getBoard();

        for ( int r = 0; r < 8; r++ ) 
        {
            for ( int c = 0; c < 8; c++ ) 
            {
                Piece savedPiece = previous.getBoardState()[r][c];
                if ( savedPiece == null ) 
                {
                    board.removePiece(r, c);
                } 
                else 
                {
                    board.placePiece(r, c, savedPiece);
                }
            }
        }

        game.restoreState( previous.getCurrentPlayer(), previous.getPlayer1Pieces(), previous.getPlayer2Pieces());
        return true;
    }
}