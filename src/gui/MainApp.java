package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.RushHourGame;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLandingPage();
    }

    public void showLandingPage() {
        LandingPage landingPage = new LandingPage(this);
        Scene scene = new Scene(landingPage.getLayout(), 600, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Rush Hour Game Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showMainBoard(RushHourGame game) {
        MainBoardPage mainBoard = new MainBoardPage(game, this);
        Scene scene = new Scene(mainBoard.getLayout(), 600, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Rush Hour Game Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
