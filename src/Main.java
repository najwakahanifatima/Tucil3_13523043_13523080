import algorithms.AStarSolver;
import algorithms.Algorithm;
import java.util.List;
import java.util.Scanner;
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
        int steps = solver.displaySolution(result);

        // ouput file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Masukkan path folder: ");
        String filePath = scanner.nextLine();

        // Output ke console
        if (result != null) {
            System.out.println("Total steps : " + steps);
            System.out.println("Total nodes explored: " + solver.getNodeCount());
            System.out.println("Solution found in " + (endTime - startTime) + " ms");
            
            // Output ke file
            String outputFilename = "test/" + filePath + ".txt";
            Algorithm.writeSolutionToFile(
                outputFilename, 
                result, 
                steps, 
                solver.getNodeCount(), 
                (endTime - startTime)
            );
            System.out.println("Solution saved in " + outputFilename);
        } else {
            System.out.println("No solution found.");
        }
    }
}