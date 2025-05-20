package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.RushHourGame;

import java.util.*;

public class MainBoardPage {
    private RushHourGame game;
    private BorderPane layout;
    private final Integer CELL_SIZE = 60;

    public MainBoardPage(RushHourGame game) {
        this.game = game;

        // Step 1: Collect unique chars from the board except dot
        Set<Character> uniqueChars = new HashSet<>();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                char ch = game.getChar(row, col);
                if (ch != '.') {
                    uniqueChars.add(ch);
                }
            }
        }

        // Step 2: Prepare color map with fixed colors for P and K
        Map<Character, Color> colorMap = new HashMap<>();
        colorMap.put('P', Color.RED);
        colorMap.put('K', Color.LIMEGREEN);

        // Colors to assign to other characters
        List<Color> otherColors = Arrays.asList(
            Color.LIGHTBLUE, Color.ORANGE, Color.PURPLE, Color.DARKCYAN,
            Color.GOLD, Color.DARKMAGENTA, Color.DARKORANGE, Color.DEEPSKYBLUE,
            Color.MEDIUMVIOLETRED, Color.DARKOLIVEGREEN
        );

        int colorIndex = 0;
        for (char ch : uniqueChars) {
            if (ch != 'P' && ch != 'K') {
                colorMap.put(ch, otherColors.get(colorIndex % otherColors.size()));
                colorIndex++;
            }
        }

        // Step 3: Create the GridPane for the board
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                char ch = game.getChar(row, col);

                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);

                if (ch == '.') {
                    rect.setFill(Color.WHITE);
                } else {
                    rect.setFill(colorMap.getOrDefault(ch, Color.GRAY));
                }
                rect.setStroke(Color.BLACK);

                Text label = new Text(ch == '.' ? "" : String.valueOf(ch));
                label.setFont(Font.font(20));
                label.setFill(Color.BLACK);

                StackPane cell = new StackPane(rect, label);
                grid.add(cell, col, row);
            }
        }

        // Step 4: Add the grid to the layout pane
        layout = new BorderPane();
        layout.setCenter(grid);
    }

    public BorderPane getLayout() {
        return this.layout;
    }
}
