
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
        long startTime = System.currentTimeMillis();
        List<State> result = solver.solve();
        long endTime = System.currentTimeMillis();
        solver.displaySolution(result);

        // output 
        if (result != null) {
            System.out.println("Solution found in " + (endTime - startTime) + " ms");
            System.out.println("Total nodes explored: " + solver.getNodeCount());
        } else {
            System.out.println("No solution found.");
        }
    }
}