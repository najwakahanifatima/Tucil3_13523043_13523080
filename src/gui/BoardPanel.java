package gui;

import utils.RushHourGame;
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private RushHourGame game;

    public void setGame(RushHourGame game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game == null) return;

        char[][] board = game.getBoard();
        int cellSize = 50;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char cell = board[i][j];
                int x = j * cellSize;
                int y = i * cellSize;

                // Warna default
                g.setColor(Color.WHITE);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);

                if (cell != '.') {
                    // Warna kendaraan
                    if (cell == 'P') {
                        g.setColor(Color.RED);  // Kendaraan 'P' = merah
                    } else if (game.getLastMovedVehicle() == cell) {
                        g.setColor(Color.YELLOW);  // Kendaraan yang baru bergerak = kuning
                    } else {
                        g.setColor(Color.BLUE);  // Kendaraan lain = biru
                    }

                    g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(cell), x + 20, y + 30);
                }
            }
        }
    }
}