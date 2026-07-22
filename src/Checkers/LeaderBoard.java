package Checkers;

import java.io.*;
import java.util.*;

public class LeaderBoard {

    public static class PlayerScore {
        private final String playerId;
        private int winCount;

        public PlayerScore(String playerId, int winCount) {
            this.playerId = playerId;
            this.winCount = winCount;
        }

        public String getPlayerId() {
            return playerId;
        }

        public int getWinCount() {
            return winCount;
        }

        public void incrementWinCount() {
            winCount++;
        }
    }

    private final ArrayList<PlayerScore> scores;
    private final String filePath;

    public LeaderBoard(String filePath) {
        this.filePath = filePath;
        this.scores = new ArrayList<>();
        loadFile();
    }

    private void loadFile() {
        File file = new File(filePath);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    scores.add(new PlayerScore(id, count));
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading leader board file " + e.getMessage());
        }
    }

    private void saveFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PlayerScore entry : scores) {
                writer.write(entry.getPlayerId() + "," + entry.getWinCount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving leader board file " + e.getMessage());
        }
    }

    public void recordWin(String playerId) {
        for (PlayerScore entry : scores) {
            if (entry.getPlayerId().equals(playerId)) {
                entry.incrementWinCount();
                saveFile();
                return;
            }
        }
        scores.add(new PlayerScore(playerId, 1));
        saveFile();
    }

    public ArrayList<PlayerScore> getRankedScores() {
        ArrayList<PlayerScore> sorted = new ArrayList<>(scores);
        sorted.sort((a, b) -> b.getWinCount() - a.getWinCount());
        return sorted;
    }
}