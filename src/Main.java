
import algorithms.*;
import java.util.*;
import utils.Position;
import utils.RushHourGame;
import utils.SaveLoad;
import utils.State;


public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();

        // main program
        // testing
        RushHourGame game = new RushHourGame(input);
        // RushHourDebugger debug = new RushHourDebugger(game);
        // debug.analyzeBoard();

        // GreedyBFSSolver solver = new GreedyBFSSolver(game);
        // List<State> result = solver.solve();
        // solver.displaySolution(result);

        UCSSolver solver = new UCSSolver(game);
        List<State> result = solver.solve();
        solver.displaySolution(result);

        // game.displayBoard();
        Position p = new Position(game.getExitPosition().getRow(), game.getExitPosition().getCol());
        p.displayPosition();
        // system.saveMatrixToTxt(game.getBoard(), "test/hasil1.txt");
    }
}
