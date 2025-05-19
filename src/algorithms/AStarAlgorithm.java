
package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import utils.Move;
import utils.RushHourGame;
import utils.SearchNode;
/**
 * A* uses f(n) = g(n) + h(n) where:
 * - g(n) is the cost so far to reach node n
 * - h(n) is the estimated cost from n to the goal
 */

public class AStarAlgorithm extends SearchAlgorithm {
    
    @Override
    public List<Move> solve(RushHourGame initialState) {
        
        // priority queue for A* algorithm (sorted by f(n) = g(n) + h(n))
        PriorityQueue<SearchNode> openSet = new PriorityQueue<>();
        
        // track visited states and avoid cycles
        Set<String> closedSet = new HashSet<>();
        
        // track the best known path to each state
        Map<String, Integer> gScores = new HashMap<>();
        
        // Initial node setup
        String initialStateKey = getStateKey(initialState);
        gScores.put(initialStateKey, 0);
        
        SearchNode initialNode = new SearchNode(
            initialState,                       
            0,  // g(n) - cost so far
            initialState.calculateHeuristic(),  // h(n) - heuristic estimate
            new ArrayList<>()   // path so far
        );
        openSet.add(initialNode);
        maxQueueSize = 1;


        while (!openSet.isEmpty()) {
            SearchNode currentNode = openSet.poll();
            RushHourGame currentState = currentNode.getState();
            
            String currentStateKey = getStateKey(currentState);
            
            // check if we've reached the goal
            if (currentState.isSolved()) {
                return currentNode.getPath();
            }
            
            // skip if we've already found a better path to this state
            if (closedSet.contains(currentStateKey)) {
                continue;
            }
            
            // mark this state as processed
            closedSet.add(currentStateKey);
            nodesExpanded++;
            
            // explore all possible next states
            for (RushHourGame nextState : generateNextStates(currentState)) {
                String nextStateKey = getStateKey(nextState);
                
                int tentativeGScore = currentNode.getGScore() + 1; 
                
                if (closedSet.contains(nextStateKey) && 
                    gScores.containsKey(nextStateKey) && 
                    tentativeGScore >= gScores.get(nextStateKey)) {
                    continue;
                }
                
                boolean needsUpdate = !gScores.containsKey(nextStateKey) || 
                                     tentativeGScore < gScores.get(nextStateKey);
                
                if (needsUpdate) {
                    // update path
                    gScores.put(nextStateKey, tentativeGScore);
                    
                    // create the new path
                    List<Move> newPath = new ArrayList<>(currentNode.getPath());

                    newPath.add(nextState.getLastMove());
                    
                    // Calculate f(n) = g(n) + h(n)
                    int hScore = nextState.calculateHeuristic();
                    
                    openSet.add(new SearchNode(nextState, tentativeGScore, hScore, newPath));
                    
                    maxQueueSize = Math.max(maxQueueSize, openSet.size());
                }
            }
            
            
        }

        return null; // no solution found
    }
    
    private String getStateKey(RushHourGame state) {
        char[][] board = state.getBoard();
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            sb.append(new String(row));
        }
        return sb.toString();
    }
}