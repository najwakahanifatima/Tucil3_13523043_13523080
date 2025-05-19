package utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implementation of the A* search algorithm for Rush Hour
 * A* uses f(n) = g(n) + h(n) where:
 * - g(n) is the cost so far to reach node n
 * - h(n) is the estimated cost from n to the goal
 */

public class AStarAlgorithm extends SearchAlgorithm {
    
    @Override
    public List<Move> solve(RushHourGame initialState) {
        
        // Priority queue for A* algorithm (sorted by f(n) = g(n) + h(n))
        PriorityQueue<SearchNode> openSet = new PriorityQueue<>();
        
        // To track visited states and avoid cycles
        Set<String> closedSet = new HashSet<>();
        
        // To track the best known path to each state
        Map<String, Integer> gScores = new HashMap<>();
        
        // Initial node setup
        String initialStateKey = getStateKey(initialState);
        gScores.put(initialStateKey, 0);
        
        SearchNode initialNode = new SearchNode(
            initialState,                       // current state
            0,                                  // g(n) - cost so far
            initialState.calculateHeuristic(),  // h(n) - heuristic estimate
            new ArrayList<>()                   // path so far
        );
        openSet.add(initialNode);
        maxQueueSize = 1;


        while (!openSet.isEmpty()) {
            SearchNode currentNode = openSet.poll();
            RushHourGame currentState = currentNode.getState();
            
            String currentStateKey = getStateKey(currentState);
            
            // Check if we've reached the goal
            if (currentState.isSolved()) {
                return currentNode.getPath();
            }
            
            // Skip if we've already found a better path to this state
            if (closedSet.contains(currentStateKey)) {
                continue;
            }
            
            // Mark this state as processed
            closedSet.add(currentStateKey);
            nodesExpanded++;
            
            // Explore all possible next states
            for (RushHourGame nextState : generateNextStates(currentState)) {
                String nextStateKey = getStateKey(nextState);
                // nextState.displayBoard();   
                // Calculate the tentative g score for this neighbor
                int tentativeGScore = currentNode.getGScore() + 1; // Cost of one move
                
                // Skip if we already found a better path to this neighbor
                if (closedSet.contains(nextStateKey) && 
                    gScores.containsKey(nextStateKey) && 
                    tentativeGScore >= gScores.get(nextStateKey)) {
                    continue;
                }
                
                // This is the best path so far to this neighbor
                boolean needsUpdate = !gScores.containsKey(nextStateKey) || 
                                     tentativeGScore < gScores.get(nextStateKey);
                
                if (needsUpdate) {
                    // Update the best known path
                    gScores.put(nextStateKey, tentativeGScore);
                    
                    // Create the new path
                    List<Move> newPath = new ArrayList<>(currentNode.getPath());

                    newPath.add(nextState.getLastMove());
                    
                    // Calculate f(n) = g(n) + h(n)
                    int hScore = nextState.calculateHeuristic();
                    
                    // Add to open set with updated scores
                    openSet.add(new SearchNode(nextState, tentativeGScore, hScore, newPath));
                    
                    // Track max queue size
                    maxQueueSize = Math.max(maxQueueSize, openSet.size());
                }
            }
            
            
        }

        return null; // No solution found
    }
    
    /**
     * Creates a unique string representation of the game state for hash comparison
     */
    private String getStateKey(RushHourGame state) {
        char[][] board = state.getBoard();
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(new String(row));
        }
        return sb.toString();
    }
}