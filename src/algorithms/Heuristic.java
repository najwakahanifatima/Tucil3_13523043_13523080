package algorithms;

import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class Heuristic {
    
    // belum berhasil

    public static int manhattanDistance(State state, RushHourGame game) {
        Vehicle playerVehicle = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();
        
        int playerEndRow = playerVehicle.getRow();
        int playerEndCol = playerVehicle.getCol() + playerVehicle.getLength() - 1;
    
        int horizontalDistance = Math.abs(exitPosition.getCol() - playerEndCol);
        int verticalDistance = Math.abs(exitPosition.getRow() - playerEndRow);
        
        int manhattanDistance = horizontalDistance + verticalDistance;
        
        return manhattanDistance;
    }

    public static int blockingCount(State state, RushHourGame game) {
        Vehicle playerVehicle = state.getVehicle().get('P');
        int playerEndCol = playerVehicle.getCol() + playerVehicle.getLength() - 1;
        
        // count blocking vehicles
        int blockingCount = 0;
        int playerRow = playerVehicle.getRow();
        
        for (Vehicle vehicle : state.getVehicle().values()) {
            if (vehicle.getId() == 'P') continue;
            
            if (vehicle.isHorizontal()) {
                // horizontal
                if (vehicle.getRow() == playerRow && 
                    vehicle.getCol() > playerEndCol) {
                    blockingCount++;
                }
            } else {
                // vertical
                int vehicleStartRow = vehicle.getRow();
                int vehicleEndRow = vehicleStartRow + vehicle.getLength() - 1;
                
                if (vehicle.getCol() > playerEndCol &&
                    vehicleStartRow <= playerRow && vehicleEndRow >= playerRow) {
                    blockingCount++;
                }
            }
        }
        
        return blockingCount;
    }
    
    public static int calculateHeuristicGreedy(State state, RushHourGame game, int opsi) {
        if (opsi == 1) {
            return blockingCount(state, game);
        } else {
            return manhattanDistance(state, game);
        }
    }
    
    public static int calculateHeuristicAstar(State state, RushHourGame game, int opsi) {
        Vehicle playerVehicle = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();
        
        // distance calculation
        int playerEndCol = playerVehicle.getCol() + playerVehicle.getLength() - 1;
        int distanceToExit = Math.max(0, exitPosition.getCol() - playerEndCol);
        
        if (opsi == 1) {
            return distanceToExit + blockingCount(state, game);
        } else {
            return distanceToExit + manhattanDistance(state, game);
        }
    }

}