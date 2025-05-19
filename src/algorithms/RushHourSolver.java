


package algorithms;
import java.util.*;
import utils.Move;
import utils.RushHourGame;

public class RushHourSolver {
    
    public static List<Move> solve(RushHourGame initialState, String algorithm) {
        SearchAlgorithm searchAlgorithm;
        
        switch (algorithm.toLowerCase()) {
            case "astar":
                searchAlgorithm = new AStarAlgorithm();
                break;
            
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }

        List<?> result = searchAlgorithm.solve(initialState);
        List<Move> moves = new ArrayList<>();
        if (result != null) {
            for (Object obj : result) {
                moves.add((Move) obj);
            }
        }
        return moves;
    }
    
    
    public static List<Move> solveWithAStar(RushHourGame initialState) {
        return solve(initialState, "astar");
    }
    
   
    public static Map<String, Object> solveWithMetrics(RushHourGame initialState, String algorithm) {
        long startTime = System.currentTimeMillis();
        List<Move> solution = solve(initialState, algorithm);
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("timeMs", endTime - startTime);
        metrics.put("solutionFound", solution != null);
        metrics.put("solutionLength", solution != null ? solution.size() : 0);
        metrics.put("solution", solution);
        
        return metrics;
    }
}