package Checkers;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        LeaderBoard leaderBoard = new LeaderBoard(LEADERBOARD_FILE);

        System.out.println("      Welcome to Checkers Game!     ");
        boolean exit = false;

        while (!exit) 
        {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Start New Game");
            System.out.println("2. View Leaderboard");
            System.out.println("3. Exit");
            System.out.print("Select an option (1-3): ");

            if (!scanner.hasNextInt()) 
            {
                System.out.println("\n[ERROR] Invalid input! Please enter a number.");
                scanner.next(); 
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) 
            {
                case 1:
                    playGame(scanner, leaderBoard);
                    break;
                case 2:
                    displayLeaderboard(leaderBoard);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option! Please choose 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    // Handles the complete gameplay flow
    private static void playGame(Scanner scanner, LeaderBoard leaderBoard) 
    {
        Game game = new Game();
        game.startGame();

        System.out.print("Enter Player 1 (RED) Name: ");
        String p1Name = scanner.nextLine().trim();
        System.out.print("Enter Player 2 (BLUE) Name: ");
        String p2Name = scanner.nextLine().trim();

        System.out.println("- Enter row and column numbers (0-7).");
        System.out.println("- Enter '-1' for row to UNDO the last move.");

        // Game loop
        while (!game.isGameOver()) 
        {
            printBoard(game.getBoard());

            Player current = game.getCurrentPlayer();
            String currentName = (current.getColor() == Piece.PieceColor.RED) ? p1Name : p2Name;

            System.out.println("Current Turn: " + currentName + " (" + current.getColor() + ")");
            System.out.println("Remaining Pieces -> " + current.getRemainingPieces());

            System.out.print("\nEnter starting row (or -1 to UNDO): ");
            int fromRow = scanner.nextInt();

            // Check for UNDO
            if (fromRow == -1) 
            {
                if (game.undoLastMove()) 
                {
                    System.out.println("\n[SUCCESS] Last move undone!");
                } 
                else 
                {
                    System.out.println("\n[ERROR] No moves available to undo!");
                }
                continue;
            }

            System.out.print("Enter starting col: ");
            int fromCol = scanner.nextInt();
            System.out.print("Enter target row: ");
            int toRow = scanner.nextInt();
            System.out.print("Enter target col: ");
            int toCol = scanner.nextInt();

            boolean moveSuccessful = game.makeMove(fromRow, fromCol, toRow, toCol);
            if (!moveSuccessful) 
            {
                System.out.println("\n[INVALID MOVE] Check rules or mandatory jumps!");
            }
        }

        // Game finished
        printBoard(game.getBoard());
        System.out.println("             GAME OVER!             ");

        Player winner = game.getWinner();
        if (winner != null) 
        {
            String winnerName = (winner.getColor() == Piece.PieceColor.RED) ? p1Name : p2Name;
            System.out.println(" Winner: " + winnerName);

            // Record win in Leaderboard file
            leaderBoard.recordWin(winnerName);
            System.out.println("[INFO] Win recorded in Leaderboard!");
        } 
        else 
        {
            System.out.println(" The game ended in a draw.");
        }

        // Display updated leaderboard
        displayLeaderboard(leaderBoard);
    }

    // Renders the Leaderboard rankings
    private static void displayLeaderboard(LeaderBoard leaderBoard) 
    {
        ArrayList<LeaderBoard.PlayerScore> scores = leaderBoard.getRankedScores();

        System.out.println("\n          LEADERBOARD          ");
        if (scores.isEmpty()) 
        {
            System.out.println("No records found yet.");
        } 
        else 
        {
            System.out.printf("%-5s %-20s %-10s%n", "Rank", "Player Name", "Wins");
            int rank = 1;
            for (LeaderBoard.PlayerScore score : scores) 
            {
                System.out.printf("%-5d %-20s %-10d%n", rank++, score.getPlayerId(), score.getWinCount());
            }
        }
    }

    // Renders the board in visual text format
    private static void printBoard(Board board) 
    {
        System.out.println("\n   0  1  2  3  4  5  6  7 (Col)");
        System.out.println("  -------------------------");

        for (int r = 0; r < 8; r++) 
        {
            System.out.print(r + " ");
            for (int c = 0; c < 8; c++) 
            {
                Piece piece = board.getPiece(r, c);
                if (piece == null) 
                {
                    System.out.print("[ ]");
                } 
                else 
                {
                    char symbol;
                    if (piece.getColor() == Piece.PieceColor.RED) 
                    {
                        symbol = piece.isKing() ? 'R' : 'r';
                    } 
                    else 
                    {
                        symbol = piece.isKing() ? 'B' : 'b';
                    }
                    System.out.print("[" + symbol + "]");
                }
            }
            System.out.println();
        }
    }
}