// import utils.Position;
// import utils.RushHourGame;
// import utils.SaveLoad;


// public class Main {
//     public static void main(String[] args) {
//         SaveLoad system = new SaveLoad();
//         String input = system.Load();

//         // main program
//         // testing
//         RushHourGame game = new RushHourGame(input );
//         game.displayBoard();
//         Position p = new Position(game.getExitPosition().getRow(), game.getExitPosition().getCol());
//         p.displayPosition();
//         system.saveMatrixToTxt(game.getBoard(), "hasil1");

//     }
// }

import java.util.List;
import utils.Move;
import utils.RushHourGame;
import utils.RushHourSolver;
import utils.SaveLoad;

public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();
        RushHourGame game = new RushHourGame(input);
        
        List<Move> solution = RushHourSolver.solveWithAStar(game);
        
        if (solution != null) {
            System.out.println("Solution found in " + solution.size() + " moves:");
            for (Move move : solution) {
                System.out.println(move.getName() + " " + move.getDirection() + " " + move.getCount());
            }
        } else {
            System.out.println("No solution exists for this puzzle.");
        }

        // print hasil
        for (Move move : solution) {
            System.out.println(move.getName() + " " + move.getDirection() + " " + move.getCount());
            if (move.getDirection() == "UP" || move.getDirection() == "LEFT"){
                game.moveVehicle(move.getName(), -1);
            }
            else{
                game.moveVehicle(move.getName(), 1);
            }
            game.displayBoard();
        }




        
    }
}