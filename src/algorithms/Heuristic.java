package algorithms;

import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class Heuristic {
    private static int blockingHeuristic(State State, RushHourGame game) {
        Vehicle target = State.getVehicle().get('P');
        Position exit = game.getExitPosition();

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

    public static int calculateHeuristicAstar(State state, RushHourGame game) {
        return blockingHeuristic(state, game) + distanceToExit(state, game);
    }

    private static int distanceToExit(State state, RushHourGame game) {
        Vehicle target = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();
        if (target.isHorizontal()) {
            int endCol = target.getCol() + target.getLength() - 1;
            return exitPosition.getCol() - endCol;
        } else {
            int endRow = target.getRow() + target.getLength() - 1;
            return exitPosition.getRow() - endRow;
        }
    }
    
}
