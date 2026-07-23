/*GUI Class اandles the graphical user interface for Checkers game and
Extends JFrame to create the main window.*/

package Checkers;

import Checkers.Piece.PieceColor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class GUI extends JFrame {

    //Size of each tile in pixels
    private static final int TILE_SIZE = 75;

    //Game logic and leaderboard objects
    private final Game game;
    private final LeaderBoard leaderBoard;

    //GUI Components
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;
    private final JLabel scoreLabel;

    //Player Names
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    //Selected piece position (-1 means no selection)
    private int selectedRow = -1;
    private int selectedCol = -1;

    //Constructor to set up the window
    public GUI() {
        game = new Game();
        leaderBoard = new LeaderBoard("leaderboard.txt");

        // Ask player names
        getPlayerNames();

        //Window properties
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Status and Score Labels (Top Panel)
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        //Center Game Board Panel
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        //Bottom Action Buttons Panel
        add(createButtonPanel(), BorderLayout.SOUTH);

        //Start game logic and update display
        game.startGame();
        updateLabels();

        pack(); //Resize window automatically
        setLocationRelativeTo(null); //Center window on screen
        setResizable(false);
    }

    //Creates bottom panel containing control buttons
    private JPanel createButtonPanel() {
        JButton undoButton = new JButton("Undo");
        JButton newGameButton = new JButton("New Game");
        JButton leaderboardButton = new JButton("Leaderboard");

        //Event listeners for buttons
        undoButton.addActionListener(e -> undoMove());
        newGameButton.addActionListener(e -> resetGame());
        leaderboardButton.addActionListener(e -> displayLeaderboard());

        JPanel panel = new JPanel();
        panel.add(undoButton);
        panel.add(newGameButton);
        panel.add(leaderboardButton);
        return panel;
    }

    //method for recieving players' names
    private void getPlayerNames() {
        String p1 = JOptionPane.showInputDialog(this, "Enter Player 1 (RED) Name:", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (p1 != null && !p1.trim().isEmpty()) {
            player1Name = p1.trim();
        }

        String p2 = JOptionPane.showInputDialog(this, "Enter Player 2 (BLUE) Name:", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (p2 != null && !p2.trim().isEmpty()) {
            player2Name = p2.trim();
        }
    }

    //Undoes the last move
    private void undoMove() {
        if (game.undoLastMove()) {
            resetSelection();
            updateLabels();
            boardPanel.repaint();
            showMessage("Undo Success", "Last move was undone!");
        } else {
            showError("Undo Error", "No moves available to undo!");
        }
    }

    //Resets and starts a new game session
    private void resetGame() {
        getPlayerNames();
        game.startGame();
        resetSelection();
        updateLabels();
        boardPanel.repaint();
    }

    //Shows current leaderboard scores
    private void displayLeaderboard() {
        StringBuilder sb = new StringBuilder("*** LEADERBOARD RANKINGS ***\n\n");
        int rank = 1;
        for (LeaderBoard.PlayerScore score : leaderBoard.getRankedScores()) {
            sb.append(String.format("%d. %-15s - %d Wins%n", rank++, score.getPlayerId(), score.getWinCount()));
        }
        if (rank == 1) {
            sb.append("No records found yet.");
        }
        showMessage("Leaderboard", sb.toString());
    }

    //Updates top text labels (Current turn and remaining pieces)
    private void updateLabels() {
        Player current = game.getCurrentPlayer();
        statusLabel.setText(getPlayerName(current.getColor()) + "'s Turn (" + current.getColor() + ")");

        Board board = game.getBoard();
        int redCount = 0;
        int blueCount = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null) continue;
                if (p.getColor() == PieceColor.RED) {
                    redCount++;
                } else {
                    blueCount++;
                }
            }
        }
        scoreLabel.setText(player1Name + " (RED): " + redCount + "  |  " + player2Name + " (BLUE): " + blueCount);
    }

    //Helper method to get player name from piece color
    private String getPlayerName(PieceColor color) {
        return (color == PieceColor.RED) ? player1Name : player2Name;
    }

    //Clears the selected tile coordinates
    private void resetSelection() {
        selectedRow = -1;
        selectedCol = -1;
    }

    //Dialog message helpers
    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    //Inner class representing the drawing panel for the checkerboard.
    private class BoardPanel extends JPanel {

        public BoardPanel() {
            setPreferredSize(new Dimension(8 * TILE_SIZE, 8 * TILE_SIZE));
            //Add mouse click listener to handle clicks on the board
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleBoardClick(e.getX(), e.getY());
                }
            });
        }

        //Handles user mouse clicks on board pixels
        private void handleBoardClick(int pixelX, int pixelY) {
            if (game.isGameOver()) {
                showWarning("Game Over", "The game is already over! Click 'New Game' to play again.");
                return;
            }

            int col = pixelX / TILE_SIZE;
            int row = pixelY / TILE_SIZE;

            //first: Select piece
            if (selectedRow == -1) {
                selectPiece(row, col);
            } 
            //second: Target tile movement
            else {
                movePiece(row, col);
            }

            repaint(); // Redraw board
        }

        //Tries to select a piece at (row, col)
        private void selectPiece(int row, int col) {
            Piece piece = game.getBoard().getPiece(row, col);

            if (piece == null) {
                showWarning("Invalid Selection", "Selected tile is empty! Choose one of your pieces.");
            } else if (piece.getColor() != game.getCurrentPlayer().getColor()) {
                showWarning("Wrong Turn", "It is not your turn! Current turn: " + getPlayerName(game.getCurrentPlayer().getColor()));
            } else {
                selectedRow = row;
                selectedCol = col;
            }
        }

        //Tries to move selected piece to target (row, col)
        private void movePiece(int row, int col) {
            // Unselect if clicked on the same piece
            if (row == selectedRow && col == selectedCol) {
                resetSelection();
                return;
            }

            boolean moved = game.makeMove(selectedRow, selectedCol, row, col);

            if (!moved) {
                showMoveError(row, col);
            }

            resetSelection();
            updateLabels();

            if (game.isGameOver()) {
                handleGameOver();
            }
        }

        //Explains why a move failed
        private void showMoveError(int targetRow, int targetCol) {
            boolean isJump = game.getBoard().wasJump(selectedRow, selectedCol, targetRow, targetCol);
            boolean mustCapture = game.getBoard().hasCapture(game.getCurrentPlayer().getColor());

            if (mustCapture && !isJump) {
                showError("Rule Violation", "Mandatory Capture Rule: You MUST capture the opponent piece when available!");
            } else {
                showError("Invalid Move", "Invalid move! Please follow legal diagonal Checkers rules.");
            }
        }

        //Handles victory or draw when game finishes
        private void handleGameOver() {
            Player winner = game.getWinner();
            if (winner != null) {
                String winnerName = getPlayerName(winner.getColor());
                leaderBoard.recordWin(winnerName);
                showMessage("Victory!", " GAME OVER!\nWinner: " + winnerName);
            } else {
                showMessage("Draw", "GAME OVER!\nThe game ended in a draw.");
            }
        }

        //Rendering tiles and pieces
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    drawTile(g2d, r, c);
                    Piece piece = game.getBoard().getPiece(r, c);
                    if (piece != null) {
                        drawPiece(g2d, r, c, piece);
                    }
                }
            }
        }

        //Draws a single square tile
        private void drawTile(Graphics2D g2d, int row, int col) {
            boolean isDark = (row + col) % 2 == 0;
            g2d.setColor(isDark ? new Color(35, 39, 42) : new Color(245, 245, 245));
            g2d.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            //Highlight selected tile
            if (row == selectedRow && col == selectedCol) {
                g2d.setColor(new Color(215, 255, 253, 180));
                g2d.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        //Draws a checker piece and its king crown if promoted
        private void drawPiece(Graphics2D g2d, int row, int col, Piece piece) {
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;
            int padding = 12;
            int diameter = TILE_SIZE - (2 * padding);

            //Piece base color (Red/Blue)
            g2d.setColor(piece.getColor() == PieceColor.RED ? new Color(220, 20, 60) : new Color(30, 144, 255));
            g2d.fillOval(x + padding, y + padding, diameter, diameter);

            //Border lines
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x + padding, y + padding, diameter, diameter);
            g2d.drawOval(x + padding + 5, y + padding + 5, diameter - 10, diameter - 10);

            //Crown icon for King pieces
            if (piece.isKing()) {
                g2d.setColor(new Color(255, 215, 0));
                g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
                FontMetrics fm = g2d.getFontMetrics();
                String crown = "👑";
                int stringX = x + (TILE_SIZE - fm.stringWidth(crown)) / 2;
                int stringY = y + (TILE_SIZE + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(crown, stringX, stringY);
            }
        }
    }
}