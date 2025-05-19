package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.State;

public class AStarSolver extends Algorithm {

    public AStarSolver(RushHourGame game) {
        super(game);
    }

    public List<State> solve() {
        Map<String, Integer> gScores = new HashMap<>();
        
        // priority queue with f(n) = g(n) + h(n) ordering
        PriorityQueue<State> openSet = new PriorityQueue<>((s1, s2) -> {
            int f1 = s1.getCost() + Heuristic.calculateHeuristicAstar(s1, game);
            int f2 = s2.getCost() + Heuristic.calculateHeuristicAstar(s2, game);
            return Integer.compare(f1, f2);
        });
        
        Set<String> closedSet = new HashSet<>();
        
        State startState = new State(cloneVehicleMap(game.getVehicles()), 0, null, "");
        String startStateKey = startState.getStateString();
        gScores.put(startStateKey, 0);
        openSet.add(startState);
        
        while (!openSet.isEmpty()) {
            //state with lowest f score
            State currentState = openSet.poll();
            String currentStateKey = currentState.getStateString();
            
            if (isGoal(currentState, game)) {
                return constructPath(currentState);
            }
            
            // skip kalau sudah pernah
            if (closedSet.contains(currentStateKey)) {
                continue;
            }
            
            // mark as processed
            closedSet.add(currentStateKey);
            
            // neighbor states
            for (State nextState : getNeighbours(currentState)) {
                String nextStateKey = nextState.getStateString();
                
                // Skip if already fully processed
                if (closedSet.contains(nextStateKey)) {
                    continue;
                }
                
                int tentativeGScore = currentState.getCost() + 1;
                
                // cek apakah solusinya lebih bagus
                if (!gScores.containsKey(nextStateKey) || tentativeGScore < gScores.get(nextStateKey)) {
                    // update best known cost to this state
                    gScores.put(nextStateKey, tentativeGScore);
                    
                   
                    openSet.removeIf(s -> s.getStateString().equals(nextStateKey));
                    
                    openSet.add(nextState);
                }
            }
        }
        
        return null;
    }
}