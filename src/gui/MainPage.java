package gui;

import javafx.TextField;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainPage {
    private VBox layout;

    public MainPage(MainApp app) {
        Label labelRow = new Label("Enter number of rows:");
        Label labelCol = new Label("Enter number of cols:");
        TextField inputRows = new TextField();
        TextField inputCols = new TextField();

        Label labelRowExit = new Label("Enter exit row:");
        Label labelColExit = new Label("Enter exit col:");
        TextField inputRowExit = new TextField();
        TextField inputColExit = new TextField();

        Label labelNumOfBlocks = new Label("Enter number of blocks:");
        TextField inputNumOfBlocks = new TextField();
        inputNumOfBlocks.setPromptText("e.g., 3");
        Button nextButton = new Button("Next");

        nextButton.setOnAction(e -> {
            try {
                // TO DO: add throw exception if num of blocks not valid based on rows and cols
                int rows = Integer.parseInt(inputRows.getText());
                int cols = Integer.parseInt(inputCols.getText());
                int numBlocks = Integer.parseInt(inputNumOfBlocks.getText());
                if (numBlocks <= 0 || numBlocks >= 25) throw new NumberFormatException();
                app.goToBlockConfig(numBlocks, rows, cols);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a number between 1 and 24.");
                alert.showAndWait();
            }
        });

        HBox box1 = new HBox(15, labelRow, inputRows, labelCol, inputCols);
        HBox box2 = new HBox(15, labelNumOfBlocks, inputNumOfBlocks, nextButton);
        box1.setAlignment(Pos.CENTER);
        box2.setAlignment(Pos.CENTER);
        layout = new VBox(20, box1, box2);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout() {
        return layout;
    }
}