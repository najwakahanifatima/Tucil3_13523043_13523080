package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.State;

public class BeamSearchSolver extends Algorithm {
    private int nodeCount = 0;
    private int heuristic = 1;
    private int beamWidth;

    public BeamSearchSolver(RushHourGame game, int heuristic, int beamWidth) {
        super(game);
        this.nodeCount = 0;
        this.heuristic = heuristic;
        this.beamWidth = beamWidth;
    }

    public List<State> solve() {
        nodeCount = 0;
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(s -> Heuristic.calculateHeuristicGreedy(s, game, heuristic)));
        Set<String> explored = new HashSet<>();

        State start = new State(cloneVehicleMap(game.getVehicles()), 0, null, "");
        unexplored.add(start);

        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String stateString = current.getStateString();

            if (explored.contains(stateString)) continue;
            explored.add(stateString);
            nodeCount++;

            if (isGoal(current, game)) {
                System.out.println("Total nodes explored: " + nodeCount);
                return constructPath(current);
            }

            List<State> neighbours = getNeighbours(current);
            
            neighbours.sort(Comparator.comparingInt(s -> Heuristic.calculateHeuristicGreedy(s, game, heuristic)));
            
            for (int i = 0; i < Math.min(beamWidth, neighbours.size()); i++) {
                if (!explored.contains(neighbours.get(i).getStateString())) {
                    unexplored.add(neighbours.get(i));
                }
            }
            
            if (unexplored.size() > beamWidth) {
                PriorityQueue<State> newQueue = new PriorityQueue<>(Comparator.comparingInt(s ->
                    Heuristic.calculateHeuristicGreedy(s, game, heuristic)));
                
                for (int i = 0; i < beamWidth; i++) {
                    if (!unexplored.isEmpty()) {
                        newQueue.add(unexplored.poll());
                    }
                }
                
                unexplored = newQueue;
            }
        }
        
        System.out.println("Total nodes explored: " + nodeCount);
        return null;
    }

    public void setBeamWidth(int beamWidth) {
        this.beamWidth = beamWidth;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}