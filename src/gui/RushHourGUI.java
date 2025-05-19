package gui;

import algorithms.RushHourSolver;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import utils.Move;
import utils.RushHourGame;
import utils.SaveLoad;

public class RushHourGUI extends JFrame {
    private RushHourGame game;
    private BoardPanel boardPanel;
    private List<Move> solution;
    private int currentStep = 0;

    public RushHourGUI() {
        setTitle("Rush Hour Solver");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk input file dan algoritma
        JPanel controlPanel = new JPanel();
        JButton loadButton = new JButton("Load Board from File");
        JComboBox<String> algorithmComboBox = new JComboBox<>(new String[]{"A*", "BFS", "DFS"});
        JButton solveButton = new JButton("Solve");
        JButton nextButton = new JButton("Next Step");

        controlPanel.add(loadButton);
        controlPanel.add(algorithmComboBox);
        controlPanel.add(solveButton);
        controlPanel.add(nextButton);
        add(controlPanel, BorderLayout.NORTH);

        // Panel untuk papan Rush Hour
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Action: Load file
        loadButton.addActionListener(e -> {
            SaveLoad system = new SaveLoad();
            String input = system.Load();
            if (input != null) {
                game = new RushHourGame(input);
                boardPanel.setGame(game);
                boardPanel.repaint();
                currentStep = 0;
            }
        });

        // Action: Solve
        solveButton.addActionListener(e -> {
            if (game == null) {
                JOptionPane.showMessageDialog(this, "Load a board first!");
                return;
            }

            String selectedAlgo = (String) algorithmComboBox.getSelectedItem();
            switch (selectedAlgo) {
                case "A*":
                    solution = RushHourSolver.solveWithAStar(game);
                    break;
                case "BFS":
                    solution = RushHourSolver.solveWithAStar(game);
                    break;
                case "DFS":
                    solution = RushHourSolver.solveWithAStar(game);
                    break;
            }

            if (solution != null) {
                JOptionPane.showMessageDialog(this, "Solution found in " + solution.size() + " moves!");
                currentStep = 0;
            } else {
                JOptionPane.showMessageDialog(this, "No solution exists!");
            }
        });

        // Action: Next Step
        nextButton.addActionListener(e -> {
            if (solution == null || currentStep >= solution.size()) {
                JOptionPane.showMessageDialog(this, "No more steps!");
                return;
            }

            Move move = solution.get(currentStep);
            if (move.getDirection().equals("UP") || move.getDirection().equals("LEFT")) {
                game.moveVehicle(move.getName(), -1);
            } else {
                game.moveVehicle(move.getName(), 1);
            }

            boardPanel.repaint();
            currentStep++;
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RushHourGUI gui = new RushHourGUI();
            gui.setVisible(true);
        });
    }
}