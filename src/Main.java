import algorithms.Algorithm;
import java.util.Scanner;
import utils.RushHourGame;
import utils.SaveLoad;

public class Main {
    public static void main(String[] args) {

        System.out.println("██████  ██    ██ ███████ ██   ██     ██   ██  ██████  ██    ██ ██████      ███████  ██████  ██      ██    ██ ███████ ██████ ");
        System.out.println("██   ██ ██    ██ ██      ██   ██     ██   ██ ██    ██ ██    ██ ██   ██     ██      ██    ██ ██      ██    ██ ██      ██   ██");
        System.out.println("██████  ██    ██ ███████ ███████     ███████ ██    ██ ██    ██ ██████      ███████ ██    ██ ██      ██    ██ █████   ██████ ");
        System.out.println("██   ██ ██    ██      ██ ██   ██     ██   ██ ██    ██ ██    ██ ██   ██          ██ ██    ██ ██       ██  ██  ██      ██   ██");
        System.out.println("██   ██  ██████  ███████ ██   ██     ██   ██  ██████   ██████  ██   ██     ███████  ██████  ███████   ████   ███████ ██   ██");
        
        SaveLoad system = new SaveLoad();
        String input = system.Load();
        RushHourGame game = new RushHourGame(input);
        int algorithm;
        int heuristic;
        int beamWidth = 0;

        Scanner scanner = new Scanner(System.in);

        // Main program
        while (true) { 
            System.out.println("===== Choose algorithm =====");
            System.out.println(" 1. Uniform Cost Search");
            System.out.println(" 2. Greedy Best First Search");
            System.out.println(" 3. A* Algorithm");
            System.out.println(" 4. Beam Search");

            System.out.print("Your choice (number): ");
            algorithm = scanner.nextInt();
            scanner.nextLine();
            if (algorithm > 4 || algorithm < 1) {
                System.out.println();
                continue;
            }
            
            if (algorithm == 4) {
                System.out.print("Enter beam width: ");
                beamWidth = scanner.nextInt();
                scanner.nextLine();
            }

            System.out.println("===== Choose heuristic =====");
            System.out.println(" 1. Blocking Vehicles");
            System.out.println(" 2. Manhattan Distance");
            System.out.print("Your choice (number): ");
            heuristic = scanner.nextInt();
            scanner.nextLine();
            if (heuristic > 2 || heuristic < 1) {
                System.out.println();
                continue;
            }
            break;
        }

        game.displayBoard();

        // solve game based on heuristic
        game.solveGame(algorithm, heuristic, beamWidth);

        // Output ke console
        if (game.solution != null) {
            // ouput file
            System.out.println("Masukkan output filename (without .txt): ");
            String filePath = scanner.nextLine();
            System.out.println("Total steps : " + game.steps);
            System.out.println("Total nodes explored: " + game.nodes);
            System.out.println("Solution found in " + (game.endTime - game.startTime) + " ms");
            
            // Output ke file
            String outputFilename = "test/" + filePath + ".txt";
            Algorithm.writeSolutionToFile(
                outputFilename, 
                game.solution, 
                game.steps, 
                game.nodes, 
                (game.endTime - game.startTime)
            );
            System.out.println("Solution saved in " + outputFilename);
        } else {
            System.out.println("No solution found.");
        }

        scanner.close();
    }
}