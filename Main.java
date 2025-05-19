
import algorithms.GreedyBFSSolver;
import java.util.List;
import utils.RushHourGame;
import utils.SaveLoad;
import utils.State;

public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();
        RushHourGame game = new RushHourGame(input);

        GreedyBFSSolver solver = new GreedyBFSSolver(game);
        List<State> result = solver.solve();
        solver.displaySolution(result);
        
        
    }
}