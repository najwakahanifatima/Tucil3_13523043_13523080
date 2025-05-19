package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class LandingPage {
    private VBox layout;

    public LandingPage(MainApp app) {

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> app.showMainPage());

        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(startButton);

        layout.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        layout.getStyleClass().add("landing");
    }

    public VBox getLayout() {
        return layout;
    }
}
