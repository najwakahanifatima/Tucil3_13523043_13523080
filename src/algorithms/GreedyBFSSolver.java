package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.State;

public class GreedyBFSSolver extends Algorithm {

    public GreedyBFSSolver(RushHourGame game) {
        super(game);
    }

    public List<State> solve() {

        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(Heuristic::calculateHeuristicGreedy));
        Set<String> explored = new HashSet<>();

        State start = new State(cloneVehicleMap(game.getVehicles()), 0, null, "");
        unexplored.add(start);

        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String stateString = current.getStateString();

            if (explored.contains(stateString)) continue;
            explored.add(stateString);

            if (isGoal(current, game)) {
                return constructPath(current);
            }

            List<State> neighbours = getNeighbours(current);
            neighbours.sort(Comparator.comparingInt(Heuristic::calculateHeuristicGreedy));
            unexplored.addAll(neighbours);

            // debugUnexplored(unexplored);
        }

        return null;
    }
}