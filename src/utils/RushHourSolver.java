
package utils;

import java.util.*;

/**
 * Main solver class that provides methods to solve Rush Hour puzzles
 * using various algorithms.
 */
public class RushHourSolver {
    
    /**
     * Solves a Rush Hour puzzle using the selected algorithm
     * 
     * @param initialState The initial game state
     * @param algorithm The algorithm to use (e.g., "astar", "bfs", "dfs")
     * @return List of moves that lead to the solution, or null if no solution found
     */
    public static List<String> solve(RushHourGame initialState, String algorithm) {
        SearchAlgorithm searchAlgorithm;
        
        switch (algorithm.toLowerCase()) {
            case "astar":
                searchAlgorithm = new AStarAlgorithm();
                break;
            // Add more algorithms as needed
            // case "bfs":
            //     searchAlgorithm = new BFSAlgorithm();
            //     break;
            // case "dfs":
            //     searchAlgorithm = new DFSAlgorithm();
            //     break;
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        
        return searchAlgorithm.solve(initialState);
    }
    
    /**
     * Convenience method to solve using A* algorithm
     */
    public static List<String> solveWithAStar(RushHourGame initialState) {
        return solve(initialState, "astar");
    }
    
    /**
     * Measures performance metrics when solving a puzzle
     * 
     * @param initialState The initial game state
     * @param algorithm The algorithm to use
     * @return A map containing performance metrics
     */
    public static Map<String, Object> solveWithMetrics(RushHourGame initialState, String algorithm) {
        long startTime = System.currentTimeMillis();
        List<String> solution = solve(initialState, algorithm);
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("timeMs", endTime - startTime);
        metrics.put("solutionFound", solution != null);
        metrics.put("solutionLength", solution != null ? solution.size() : 0);
        metrics.put("solution", solution);
        
        return metrics;
    }
}