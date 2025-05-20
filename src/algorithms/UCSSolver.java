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
        // cek priority queue dengan cost terendah
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        // explored state
        Set<String> explored = new HashSet<>();

        // initial state
        State start = new State(cloneVehicleMap(game.getVehicles()), 0, null, "");
        unexplored.add(start);

        // iterate sampai ketemu goals atau semua kemungkinan state sudah habis dicek
        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String stateString = current.getStateString();
            
            // klo state sudah dieksplor, skip
            if (explored.contains(stateString)) continue;
            explored.add(stateString);
            nodeCount++;
            
            // klo udah sampai goal, buat path
            if (isGoal(current, game)) {
                System.out.println("Total nodes explored: " + nodeCount);
                return constructPath(current);
            }

            // klo blm sampai goal, eksplor all neighbour state
            unexplored.addAll(getNeighbours(current));

            // debugUnexplored(unexplored);
        }
        System.out.println("Total nodes explored: " + nodeCount);
        return null;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}