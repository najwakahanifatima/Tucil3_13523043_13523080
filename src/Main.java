
import algorithms.AStarSolver;
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
        game.displayBoard();
        long startTime = System.currentTimeMillis();
        List<State> result = solver.solve();
        long endTime = System.currentTimeMillis();
        int steps = solver.displaySolution(result); // display sama jumlah steps

        // output 
        if (result != null) {
            System.out.println("Total steps : " + steps);
            System.out.println("Total nodes explored: " + solver.getNodeCount());
            System.out.println("Solution found in " + (endTime - startTime) + " ms");
        } else {
            System.out.println("No solution found.");
        }
    }
}