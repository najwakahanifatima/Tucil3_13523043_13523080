package gui;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;
import utils.Position;

import java.util.*;

import algorithms.Algorithm;

public class MainBoardPage {
    private RushHourGame game;
    private BorderPane layout;
    private final Integer CELL_SIZE = 60;
    private final List<String> algorithms = Arrays.asList("Uniform Cost Search", "Greedy Best First Search", "A* Algorithm");
    private final List<String> heuristics = Arrays.asList("Blocking Vehicles", "Manhattan Distance");
    private final List<Color> otherColors = Arrays.asList(
            Color.LIGHTBLUE, Color.ORANGE, Color.PURPLE, Color.DARKCYAN,
            Color.GOLD, Color.DARKMAGENTA, Color.DARKORANGE, Color.DEEPSKYBLUE,
            Color.MEDIUMVIOLETRED, Color.DARKOLIVEGREEN
    );
    private String chosenAlgorithm = algorithms.get(0);
    private String chosenHeuristic = heuristics.get(0);

    public MainBoardPage(RushHourGame game, MainApp app) {
        this.game = game;
        layout = new BorderPane();

        // SET BOTTOM
        Label lblDuration = new Label("Duration: -");
        Label lblSteps = new Label("Steps: -");
        Label lblNodes = new Label("Nodes: -");
        Button backButton = new Button("Back or Upload");

        backButton.setOnAction(e -> {
            app.showLandingPage();
        });

        HBox statsBox = new HBox(15, lblDuration, lblSteps, lblNodes, backButton);
        statsBox.setAlignment(Pos.CENTER);
        layout.setBottom(statsBox);
        statsBox.getStyleClass().add("stats-bar");

        // SET TOP
        ComboBox<String> algorithmOptions = new ComboBox<>();
        algorithmOptions.getItems().addAll(algorithms);
        algorithmOptions.setValue(algorithms.get(0));
        algorithmOptions.setOnAction(e -> {
            chosenAlgorithm = algorithmOptions.getValue();
        });

        ComboBox<String> heuristicOptions = new ComboBox<>();
        heuristicOptions.getItems().addAll(heuristics);
        heuristicOptions.setValue(heuristics.get(0));
        heuristicOptions.setOnAction(e -> {
            chosenHeuristic = heuristicOptions.getValue();
        });

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(e -> {
            int algo = 1, heu = 1;
            if (chosenAlgorithm.equals(algorithms.get(0))) algo = 1;
            if (chosenAlgorithm.equals(algorithms.get(1))) algo = 2;
            if (chosenAlgorithm.equals(algorithms.get(2))) algo = 3;

            if (chosenHeuristic.equals(heuristics.get(0))) heu = 1;
            if (chosenHeuristic.equals(heuristics.get(1))) heu = 2;

            game.solveGame(algo, heu);

            if (game.solution != null) {
                // update stats labels
                lblDuration.setText("Duration: " + ( game.endTime - game.startTime) + " ms");
                lblSteps.setText("Steps: " + game.steps);
                lblNodes.setText("Nodes: " + game.nodes);

                animateSolution(game.solution);
            } else {
                new Alert(Alert.AlertType.ERROR, "No Solution Found.").showAndWait();
            }
        });

        HBox temp = new HBox(15, algorithmOptions, heuristicOptions, solveButton);
        temp.setAlignment(Pos.CENTER);
        
        Label header = new Label("Choose the algorithm and heuristic");
        header.getStyleClass().add("game-header");
        VBox option = new VBox(15, header, temp);

        option.setAlignment(Pos.CENTER);
        layout.setTop(option);

        // SET CENTER
        layout.setCenter(setBoard());        
    }

    private GridPane setBoard() {
        char[][] board = new char[game.getRows()][game.getCols()];
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                board[i][j] = game.getChar(i, j);
            }
        }
        return buildGridFromCharBoard(board);
    }

    private char[][] generateBoardFromState(State state) {
        int rows = game.getRows();
        int cols = game.getCols();
        char[][] board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '.');
        }

        for (Map.Entry<Character, Vehicle> entry : state.vehicles.entrySet()) {
            char id = entry.getKey();
            Vehicle v = entry.getValue();
            int r = v.getRow();
            int c = v.getCol();

            for (int i = 0; i < v.getLength(); i++) {
                if (v.isHorizontal()) {
                    board[r][c + i] = id;
                } else {
                    board[r + i][c] = id;
                }
            }
        }

        return board;
    }

    private GridPane buildGridFromCharBoard(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        Set<Character> uniqueChars = new HashSet<>();
        for (char[] row : board) {
            for (char ch : row) {
                if (ch != '.') {
                    uniqueChars.add(ch);
                }
            }
        }

        Map<Character, Color> colorMap = new HashMap<>();
        colorMap.put('P', Color.RED);
        colorMap.put('K', Color.LIMEGREEN);

        int colorIndex = 0;
        for (char ch : uniqueChars) {
            if (ch != 'P' && ch != 'K') {
                colorMap.put(ch, otherColors.get(colorIndex % otherColors.size()));
                colorIndex++;
            }
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Position exit = game.getExitPosition();

        for (int r = -1; r <= rows; r++) {
            for (int c = -1; c <= cols; c++) {
                char ch;

                boolean isExit = (exit != null && r == exit.getRow() && c == exit.getCol());

                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    ch = board[r][c];
                } else {
                    ch = '.';
                }

                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);

                if (isExit) {
                    rect.setFill(Color.LIMEGREEN);
                } else if (r == -1 || c == -1 || r == rows || c == cols) {
                    rect.setFill(Color.BLACK);
                } else if (ch == '.') {
                    rect.setFill(Color.WHITE);
                } else {
                    rect.setFill(colorMap.getOrDefault(ch, Color.GRAY));
                }

                rect.setStroke(Color.BLACK);

                Text label = new Text(isExit ? "K" : (ch == '.' ? "" : String.valueOf(ch)));
                label.setFont(Font.font(20));
                label.setFill(Color.BLACK);

                StackPane cell = new StackPane(rect, label);

                grid.add(cell, c + 1, r + 1);
            }
        }

        return grid;
    }


    public void animateSolution(List<State> path) {
        if (path == null || path.isEmpty()) return;

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        for (int i = 0; i < path.size(); i++) {
            final int index = i;
            KeyFrame frame = new KeyFrame(Duration.seconds(index * 0.5), e -> {
                char[][] board = generateBoardFromState(path.get(index));
                layout.setCenter(buildGridFromCharBoard(board));
            });
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(e -> {
            // Delay 1 second after animation ends
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(ev -> {
                Platform.runLater(() -> {
                    TextInputDialog dialog = new TextInputDialog("solusi.txt");
                    dialog.setTitle("Save Solution");
                    dialog.setHeaderText("Rush Hour Solved!");
                    dialog.setContentText("Enter filename to save steps (with extension):");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(filename -> {
                        String outputFilename = "test/" + filename;
                        Algorithm.writeSolutionToFile(
                            outputFilename, 
                            game.solution, 
                            game.steps, 
                            game.nodes, 
                            (game.endTime - game.startTime)
                        );
                    });
                });
            });
            delay.play();
        });

        timeline.play();
    }



    public BorderPane getLayout() {
        return this.layout;
    }
} 
