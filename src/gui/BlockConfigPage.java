package gui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BlockConfigPage {
    private VBox layout;
    private Block[] blocks;

    public BlockConfigPage(MainApp app, int numberOfBlocks, int rows, int cols) {
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        blocks = new Block[numberOfBlocks];
        VBox inputContainer = new VBox(20);

        for (int i = 0; i < numberOfBlocks; i++) {
            blocks[i] = new Block();
    
            ComboBox<Integer> lengthBox = new ComboBox<>();
            lengthBox.getItems().addAll(2, 3, 4, 5);
            lengthBox.setValue(2);

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
        placeBlocks.setOnAction(e -> app.goToBoard(blocks, rows, cols));

        layout.getChildren().addAll(new Label("Configure your blocks:"), inputContainer, placeBlocks);
    }

    public VBox getLayout() {
        return layout;
    }
}