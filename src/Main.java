
import algorithms.UCSSolver;
import java.util.List;
import utils.RushHourGame;
import utils.SaveLoad;
import utils.State;

public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();
        RushHourGame game = new RushHourGame(input);
        game.displayBoard();
        System.out.println(game.getExitPosition().getRow() + " " + game.getExitPosition().getCol());

        UCSSolver solver = new UCSSolver(game);
        List<State> result = solver.solve();
        solver.displaySolution(result);
    }
}