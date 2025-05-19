package algorithms;

import java.util.*;
import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class GreedyBFSSolver extends Algorithm {

    public GreedyBFSSolver(RushHourGame game) {
        super(game);
    }

    public List<State> solve() {

        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(this::calculateHeuristic));
        Set<String> explored = new HashSet<>();

        State start = new State(cloneVehicleMap(game.vehicles), 0, null, "");
        unexplored.add(start);

        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String hash = current.getStateString();

            if (explored.contains(hash)) continue;
            explored.add(hash);

            if (isGoal(current)) {
                return constructPath(current);
            }

            List<State> neighbours = getNeighbours(current);
            neighbours.sort(Comparator.comparingInt(this::calculateHeuristic));
            unexplored.addAll(neighbours);
        }

        return null;
    }

    // nanti ubah aja sesuai heuristik
    private int calculateHeuristic(State State) {
        Vehicle target = State.vehicles.get(game.getTargetVehicle());
        Position exit = game.getExitPosition();

        if (target.getRow() != exit.getRow()) {
            return 1000;
        }

        int blockingCount = 0;
        int distanceToExit = exit.getCol() - (target.getCol() + target.getLength() - 1);

        for (Vehicle v : State.vehicles.values()) {
            if (v.getId() == target.getId()) continue;
    
            if (v.isHorizontal()) {
                if (v.getRow() == target.getRow() && 
                    v.getCol() > target.getCol() + target.getLength() - 1 &&
                    v.getCol() <= exit.getCol()) {
                    blockingCount++;
                }
            } else {
                if (v.getCol() > target.getCol() + target.getLength() - 1 &&
                    v.getCol() <= exit.getCol() &&
                    target.getRow() >= v.getRow() && 
                    target.getRow() < v.getRow() + v.getLength()) {
                    blockingCount++;
                }
            }
        }
        return blockingCount + distanceToExit;
    }
}
