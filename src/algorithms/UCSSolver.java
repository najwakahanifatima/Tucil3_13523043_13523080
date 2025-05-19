package algorithms;

import java.util.*;
import utils.*;

public class UCSSolver extends Algorithm {
    
    public UCSSolver(RushHourGame game) {
        super(game);
    }

    public List<State> solve() {
        // cek priority queue dengan cost terendah
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        // explored state
        Set<String> explored = new HashSet<>();

        // initial state
        State start = new State(cloneVehicleMap(game.vehicles), 0, null, "");
        unexplored.add(start);

        // iterate sampai ketemu goals atau semua kemungkinan state sudah habis dicek
        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String stateString = current.getStateString();

            // klo state sudah dieksplor, skip
            if (explored.contains(stateString)) continue;
            explored.add(stateString);

            // klo udah sampai goal, buat path
            if (isGoal(current)) {
                return constructPath(current);
            }

            // klo blm sampai goal, eksplor all neighbour state
            unexplored.addAll(getNeighbours(current));
        }

        return null;
    }
}