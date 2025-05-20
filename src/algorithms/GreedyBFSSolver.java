package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.State;

public class GreedyBFSSolver extends Algorithm {
    private int nodeCount = 0;
    public GreedyBFSSolver(RushHourGame game) {
        super(game);
        this.nodeCount = 0;
    }

    public List<State> solve() {
        nodeCount = 0;
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(s -> Heuristic.calculateHeuristicGreedy(s, game, 1)));
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
            neighbours.sort(Comparator.comparingInt(s -> Heuristic.calculateHeuristicGreedy(s, game, 1)));
            unexplored.addAll(neighbours);

            // debugUnexplored(unexplored);
        }
        System.out.println("Total nodes explored: " + nodeCount);
        return null;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}