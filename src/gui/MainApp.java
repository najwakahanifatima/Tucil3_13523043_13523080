package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLandingPage();
    }

    public void showLandingPage() {
        LandingPage landingPage = new LandingPage(this);
        Scene scene = new Scene(landingPage.getLayout(), 600, 400);
        primaryStage.setTitle("Landing Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showMainPage() {
        MainPage mainPage = new MainPage(this);
        Scene scene = new Scene(mainPage.getLayout(), 600, 400);
        primaryStage.setTitle("Main Page");
        primaryStage.setScene(scene);
    }

    public void goToBlockConfig(int numberOfBlocks, int rows, int cols) {
        BlockConfigPage configPage = new BlockConfigPage(this, numberOfBlocks, rows, cols);
        Scene scene = new Scene(configPage.getLayout(), 600, 400);
        primaryStage.setScene(scene);
    }

    public void goToBoard(Block[] blocks, int rows, int cols) {
        // debug
        int i = 0;
        for (Block b : blocks){
            System.out.println("B" + i + " Length: " + b.length + " - Orient: " + b.orientation);
            i++;
        }

        BoardPage boardPage = new BoardPage(blocks, rows, cols);
        Scene scene = new Scene(boardPage.getLayout(), 600, 400);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
