package algorithms;

import java.util.*;
import utils.*;

public class UCSSolver extends Algorithm {
    private int nodeCount = 0;
    public UCSSolver(RushHourGame game) {
        super(game);
        this.nodeCount = 0;
    }

    public List<State> solve() {
        nodeCount = 0;
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
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

            unexplored.addAll(getNeighbours(current));

        }
        System.out.println("Total nodes explored: " + nodeCount);
        return null;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}