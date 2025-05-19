package algorithms;

import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class Heuristic {
    
    public static int calculateHeuristicAstar(State state, RushHourGame game) {
        Vehicle playerVehicle = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();
        
        // distance calculation
        int playerEndCol = playerVehicle.getCol() + playerVehicle.getLength() - 1;
        int distanceToExit = Math.max(0, exitPosition.getCol() - playerEndCol);
        
        // count blocking vehicles
        int blockingCount = 0;
        int playerRow = playerVehicle.getRow();
        
        for (Vehicle vehicle : state.getVehicle().values()) {
            if (vehicle.getId() == 'P') continue;
            
            if (vehicle.isHorizontal()) {
                if (vehicle.getRow() == playerRow && 
                    vehicle.getCol() > playerEndCol) {
                    blockingCount++;
                }
            } else {
                int vehicleStartRow = vehicle.getRow();
                int vehicleEndRow = vehicleStartRow + vehicle.getLength() - 1;
                
                if (vehicle.getCol() > playerEndCol &&
                    vehicleStartRow <= playerRow && vehicleEndRow >= playerRow) {
                    blockingCount++;
                }
            }
        }
        
        return blockingCount + distanceToExit;
    }

    // belum berhasil
    public static int calculateHeuristicGreedy(State state, RushHourGame game) {
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
}