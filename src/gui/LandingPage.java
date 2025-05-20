package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import algorithms.UCSSolver;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.RushHourGame;
import utils.State;

public class LandingPage {
    private VBox layout;

    public LandingPage(MainApp app) {

        Button startButtonManual = new Button("Start Manual");
        startButtonManual.setOnAction(e -> app.showBoardConfigPage());

        Button startButtonUpload = new Button("Start Upload");
        startButtonUpload.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Puzzle File");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );

            File file = fileChooser.showOpenDialog(layout.getScene().getWindow());
            if (file != null) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    System.out.println("Uploaded file content:\n" + content);

                    // create new game
                    RushHourGame game = new RushHourGame(content);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Load Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("File loaded successfully!");
                    successAlert.showAndWait();
                    
                    // pass to board scene
                    app.showMainBoard(game);


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(startButtonManual);
        layout.getChildren().add(startButtonUpload);

        System.out.println("CSS path = " + getClass().getResource("style.css"));
        layout.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        layout.getStyleClass().add("landing");
    }

    public VBox getLayout() {
        return layout;
    }
}
