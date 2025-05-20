package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.State;

public class AStarSolver extends Algorithm {
    private int nodeCount = 0;
    private int heuristic = 1;

    public AStarSolver(RushHourGame game, int heu) {
        super(game);
        this.nodeCount = 0;
        this.heuristic = heu;
    }

    public List<State> solve() {
        nodeCount = 0;
        Map<String, Integer> gScores = new HashMap<>();
        
        // priority queue with f(n) = g(n) + h(n) ordering
        
        // sesuain nanti opsinya
        PriorityQueue<State> openSet = new PriorityQueue<>((s1, s2) -> {
            int f1 = s1.getCost() + Heuristic.calculateHeuristicAstar(s1, game, heuristic);
            int f2 = s2.getCost() + Heuristic.calculateHeuristicAstar(s2, game, heuristic);
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
                System.out.println("Total nodes explored: " + nodeCount);
                return constructPath(currentState);
            }
            
            // skip kalau sudah pernah
            if (closedSet.contains(currentStateKey)) {
                continue;
            }
            nodeCount++;
            
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
        System.out.println("Total nodes explored: " + nodeCount);
        return null;
    }

    public int getNodeCount() {
        return nodeCount;
    }   
}