package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RushHourGame {
    private int rows;
    private int cols;
    private int numVehicles;
    private char[][] board;
    private Map<Character, Vehicle> vehicles;
    private Vehicle targetVehicle;
    private Position exitPosition;
    private String lastMove; // Track the last move made


    
    public RushHourGame(String input) {
        parseInput(input);
        identifyVehicles();
        this.lastMove = "Initial";
    }

    public RushHourGame(RushHourGame other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.numVehicles = other.numVehicles;
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, cols);
        }
        this.vehicles = new HashMap<>();
        for (Map.Entry<Character, Vehicle> entry : other.vehicles.entrySet()) {
            this.vehicles.put(entry.getKey(), new Vehicle(entry.getValue()));
        }
        this.targetVehicle = new Vehicle(other.targetVehicle);
        this.exitPosition = new Position(other.exitPosition);
        this.lastMove = other.lastMove;
    }

    private void parseInput(String input) {
        String[] lines = input.split("\n");
        String[] dimensions = lines[0].split(" ");
        int specifiedRows = Integer.parseInt(dimensions[0]);
        int specifiedCols = Integer.parseInt(dimensions[1]);
        numVehicles = Integer.parseInt(lines[1]);
        
        // menentukan dimensi board sesuai dengan letak K
        int actualRows = specifiedRows;
        int actualCols = specifiedCols;
        
        // konfigurasi board
        for (int i = 2; i < lines.length; i++) {
            String line = lines[i];

            if (line.length() > actualCols) {
                actualCols = line.length();
            }
        }
        
        // additional row
        if (lines.length > specifiedRows + 2) {
            actualRows = lines.length - 2; 
        }
        
        // board dengan dimensi yang telah disesuaikan
        rows = actualRows;
        cols = actualCols;
        board = new char[rows][cols];
        
        // inisialisasi
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = ' ';
            }
        }
        
        // parsing input
        for (int i = 0; i < rows && i + 2 < lines.length; i++) {
            String line = lines[i + 2];
            for (int j = 0; j < cols && j < line.length(); j++) {
                char c = line.charAt(j);
                board[i][j] = c;
                
                if (c == 'K') {
                    exitPosition = new Position(i, j);
                } 
            }
        }
    }

    private void identifyVehicles() {
    vehicles = new HashMap<>();

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            char current = board[i][j];
            if (current != '.' && current != 'K' && !vehicles.containsKey(current)) {
                // Identify direction of the vehicle (horizontal or vertical)
                boolean isHorizontal = (j + 1 < cols && board[i][j + 1] == current);

                // Find length and starting position of the vehicle
                int length = 1;
                int startRow = i;
                int startCol = j;

                if (isHorizontal) {
                    int col = j + 1;
                    while (col < cols && board[i][col] == current) {
                        length++;
                        col++;
                    }
                } else { // Vertical
                    int row = i + 1;
                    while (row < rows && board[row][j] == current) {
                        length++;
                        row++;
                    }
                }

                Vehicle vehicle = new Vehicle(current, startRow, startCol, length, isHorizontal);
                if (current == 'P') {
                    targetVehicle = vehicle; // Update targetVehicle reference
                }

                vehicles.put(current, vehicle);
            }
        }
    }
}

    
    public Position getExitPosition() {
        return exitPosition;
    }
    
    public char[][] getBoard(){
        return this.board;
    }

    public Map<Character, Vehicle> getVehicles() {
        return vehicles;
    }

    public void displayBoard(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("\n");
    }

    // heuristic
    public int blockingHeuristic() {
        // Cari posisi kendaraan target
        Vehicle target = targetVehicle;
        int blockingCount = 0;
        
        // Asumsikan kendaraan target horizontal dan exit di sebelah kanan
        if (target.isHorizontal()) {
            int row = target.getRow();
            int endCol = target.getCol() + target.getLength() - 1;

            for (int col = endCol + 1; col < cols; col++) {
                if (board[row][col] != '.') {
                    blockingCount++;
                }
            }
        }
        // Jika vertikal, perlu disesuaikan
        else {
            int col = target.getCol();
            int endRow = target.getRow() + target.getLength() - 1;
            
            for (int row = endRow + 1; row < rows; row++) {
                if (board[row][col] != '.') {
                    blockingCount++;
                }
            }
        }
        
        return blockingCount;
    }

    public boolean isSolved() {
    Vehicle target = targetVehicle;
    if (target.isHorizontal()) {
        // For horizontal vehicles, the head is at the rightmost position when the vehicle is P
        int headCol = target.getCol() + target.getLength() - 1;

        // System.out.println("headCol: " + headCol);
        // System.out.println("exitPosition: " + exitPosition.getCol());
        
        // Check if the head is right behind the exit position (K)
        // Assuming K is to the right of the puzzle
        return headCol + 1 == exitPosition.getCol() && target.getRow() == exitPosition.getRow();
    } else {
        // For vertical vehicles, the head is at the bottom position when the vehicle is P
        int headRow = target.getRow() + target.getLength() - 1;
        
        // Check if the head is right above the exit position (K)
        // Assuming K is at the bottom of the puzzle
        return headRow + 1 == exitPosition.getRow() && target.getCol() == exitPosition.getCol();
    }
}

    public List<RushHourGame> generateNextStates() {
    List<RushHourGame> nextStates = new ArrayList<>();
    for (Vehicle vehicle : vehicles.values()) {
        // Try moving left/up
        if (canMove(vehicle, -1)) {
            RushHourGame newState = new RushHourGame(this);
            newState.moveVehicle(vehicle.getId(), -1);
            newState.lastMove = vehicle.getId() + (vehicle.isHorizontal() ? "-LEFT" : "-UP");
            
            // // Debug
            // if (vehicle.getId() == 'P') {
            //     System.out.println("Generated new state for P (after moving LEFT/UP):");
            //     newState.displayBoard();
            // }
            
            nextStates.add(newState);
        }
        
        // Try moving right/down
        if (canMove(vehicle, 1)) {
            RushHourGame newState = new RushHourGame(this);
            newState.moveVehicle(vehicle.getId(), 1);
            newState.lastMove = vehicle.getId() + (vehicle.isHorizontal() ? "-RIGHT" : "-DOWN");
            
            // // Debug
            // if (vehicle.getId() == 'P') {
            //     System.out.println("Generated new state for P (after moving RIGHT/DOWN):");
            //     newState.displayBoard();
            // }
            
            nextStates.add(newState);
        }
    }
    return nextStates;
}


    boolean canMove(Vehicle vehicle, int direction) {
        if (vehicle.isHorizontal()) {
            if (direction < 0) { // Left
                int newCol = vehicle.getCol() - 1;
                return newCol >= 0 && board[vehicle.getRow()][newCol] == '.';
            } else { // Right
                int newCol = vehicle.getCol() + vehicle.getLength();
                return newCol < cols && board[vehicle.getRow()][newCol] == '.';
            }
        } else {
            if (direction < 0) { // Up
                int newRow = vehicle.getRow() - 1;
                return newRow >= 0 && board[newRow][vehicle.getCol()] == '.';
            } else { // Down
                int newRow = vehicle.getRow() + vehicle.getLength();
                return newRow < rows && board[newRow][vehicle.getCol()] == '.';
            }
        }
    }

    void moveVehicle(char vehicleId, int direction) {
        Vehicle vehicle = vehicles.get(vehicleId);
        int row = vehicle.getRow();
        int col = vehicle.getCol();
        int length = vehicle.getLength();
        
        // Clear current position on the board
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < length; i++) {
                board[row][col + i] = '.';
            }

            // Update vehicle's position
            if (direction < 0) {
                vehicle.setCol(col - 1);
            } else {
                vehicle.setCol(col + 1);
            }
        } else { // Vertical vehicle
            for (int i = 0; i < length; i++) {
                board[row + i][col] = '.';
            }

            // Update vehicle's position
            if (direction < 0) {
                vehicle.setRow(row - 1);
            } else {
                vehicle.setRow(row + 1);
            }
        }

        // Set new position on the board
        row = vehicle.getRow();
        col = vehicle.getCol();
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < length; i++) {
                board[row][col + i] = vehicleId;
            }
        } else {
            for (int i = 0; i < length; i++) {
                board[row + i][col] = vehicleId;
            }
        }

        // Synchronize targetVehicle if 'P' is moved
        if (vehicleId == 'P') {
            targetVehicle = new Vehicle(vehicle); // Create a new instance to reflect updates
        }

        // if (vehicleId == 'P') {
        //     System.out.println("Updated P's position: (" + targetVehicle.getRow() + ", " + targetVehicle.getCol() + ")");
        //     System.out.println("New board state:");
        //     displayBoard();
        // }

    }


    // Heuristic functions
    public int calculateHeuristic() {
        return blockingHeuristic() + distanceToExit();
    }

    private int distanceToExit() {
        Vehicle target = targetVehicle;
        if (target.isHorizontal()) {
            int endCol = target.getCol() + target.getLength() - 1;
            return exitPosition.getCol() - endCol;
        } else {
            int endRow = target.getRow() + target.getLength() - 1;
            return exitPosition.getRow() - endRow;
        }
    }

    public String getLastMove() {
        return lastMove;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RushHourGame)) return false;
        RushHourGame other = (RushHourGame) obj;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result = 31 * result + board[i][j];
            }
        }
        return result;
    }
    
}
