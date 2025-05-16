import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();

        // main program
        // testing
        RushHourGame game = new RushHourGame(input);
        game.displayBoard();
        game.getExitPosition().displayPosition();
        

    }
}
