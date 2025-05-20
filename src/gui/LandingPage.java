package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.RushHourGame;
public class LandingPage {
    private VBox layout;

    public LandingPage(MainApp app) {

        layout = new VBox(20);

        // set bg
        Image bgImage = new Image(getClass().getResource("/gui/bg.png").toExternalForm());

        BackgroundImage backgroundImage = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, false)
        );
        layout.setBackground(new Background(backgroundImage));
        layout.setPrefSize(600, 600);

        // set button
        Label intro = new Label("Up load your configuration!");
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

        Label credit = new Label("This program is made by Najwa K. F. (13523043) and Diyah S. N. (13523080)");

        layout.setAlignment(Pos.CENTER);
        // layout.getChildren().add(startButtonManual);
        layout.getChildren().add(intro);
        layout.getChildren().add(startButtonUpload);
        layout.getChildren().add(credit);

        credit.getStyleClass().add("credit");
        intro.getStyleClass().add("intro");
        startButtonUpload.getStyleClass().add("button-start");
        layout.getStyleClass().add("landing");
    }

    public VBox getLayout() {
        return layout;
    }
}
