
import algorithms.AStarSolver;
import algorithms.GreedyBFSSolver;
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

        AStarSolver solver = new AStarSolver(game);
        List<State> result = solver.solve();
        solver.displaySolution(result);
        
        
    }
}