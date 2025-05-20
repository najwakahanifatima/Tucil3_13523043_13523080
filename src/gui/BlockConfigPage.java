package gui;

import java.util.Vector;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BlockConfigPage {
    private VBox layout;
    private Block[] blocks;

    public BlockConfigPage(MainApp app, int numberOfBlocks, int rows, int cols) {
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        blocks = new Block[numberOfBlocks+1];
        VBox inputContainer = new VBox(20);

        Vector<Integer> options = new Vector<>();
        for (int k = 2; k <= rows || k <= cols; k++) {
            options.add(k);
        }

        // input for exit gate
        ComboBox<String> positionExit = new ComboBox<>();
        positionExit.getItems().addAll("Above", "Bottom", "Left", "Right");
        positionExit.setValue("Right");
        Label confExit = new Label("Enter row/col of exit gate: ");
        TextField rowColExit = new TextField();
        HBox exitInput = new HBox(10, new Label("Exit gate configuration"), positionExit, confExit, rowColExit);
        inputContainer.getChildren().add(exitInput);
        exitInput.setAlignment(Pos.CENTER);

        // input for P block (target block)
        ComboBox<Integer> lengthTarget = new ComboBox<>();
        lengthTarget.getItems().addAll(options);
        lengthTarget.setValue(options.get(0));
        ComboBox<String> orientTarget = new ComboBox<>();
        orientTarget.getItems().addAll("Horizontal", "Vertical");
        orientTarget.setValue("Horizontal");
        HBox targetInput = new HBox(10, new Label("Target input configuration"), lengthTarget, orientTarget);
        inputContainer.getChildren().add(targetInput);
        targetInput.setAlignment(Pos.CENTER);

        // block 0 always P block (target)
        blocks[0] = new Block();
        lengthTarget.setOnAction(e -> {
            blocks[0].length = lengthTarget.getValue();
        });
        orientTarget.setOnAction(e -> {
            blocks[0].orientation = orientTarget.getValue();
        });
        
        for (int i = 1; i <= numberOfBlocks; i++) {
            blocks[i] = new Block();
    
            ComboBox<Integer> lengthBox = new ComboBox<>();
            lengthBox.getItems().addAll(options);
            lengthBox.setValue(options.get(0));

            ComboBox<String> orientationBox = new ComboBox<>();
            orientationBox.getItems().addAll("Horizontal", "Vertical");
            orientationBox.setValue("Horizontal");

            HBox row = new HBox(10, new Label("Block " + (i + 1)), lengthBox, orientationBox);
            row.setAlignment(Pos.CENTER);

            int index = i;
            lengthBox.setOnAction(e -> {
                blocks[index].length = lengthBox.getValue();
            });
            orientationBox.setOnAction(e -> {
                blocks[index].orientation = orientationBox.getValue();
            });

            inputContainer.getChildren().add(row);
        }

        Button placeBlocks = new Button("Place Blocks on Board");
        placeBlocks.setOnAction(e -> {
            try {
                int inputConfExit = Integer.parseInt(rowColExit.getText());
                String posExit = positionExit.getValue();
                if (posExit.equals("Bottom") || posExit.equals("Above")) {
                    if (inputConfExit < 0 || inputConfExit >= cols)
                        throw new IllegalArgumentException("Exit row/col is out of bounds for columns.");
                } else {
                    if (inputConfExit < 0 || inputConfExit >= rows)
                        throw new IllegalArgumentException("Exit row/col is out of bounds for rows.");
                }
                app.goToBoard(blocks, rows, cols, posExit, inputConfExit);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Please enter a valid number for row/col.").showAndWait();
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        layout.getChildren().addAll(new Label("Configure your blocks:"), inputContainer, placeBlocks);
    }

    public VBox getLayout() {
        return layout;
    }
}