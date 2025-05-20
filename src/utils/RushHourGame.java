package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RushHourGame {
    // warna ANSI untuk terminal
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[43m";  // kuning
    public static final String ANSI_RED = "\u001B[41m";      // merah

    private int rows;
    private int cols;
    private int numVehicles;
    private char[][] board;
    private Map<Character, Vehicle> vehicles;
    private Vehicle targetVehicle;
    private Position exitPosition;


    
    public RushHourGame(String input) {
        parseInput(input);
        identifyVehicles();
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
    }

private void parseInput(String input) {
    String[] lines = input.split("\n");
    String[] dimensions = lines[0].trim().split(" ");
    rows = Integer.parseInt(dimensions[0]);
    cols = Integer.parseInt(dimensions[1]);
    numVehicles = Integer.parseInt(lines[1].trim());
    
    board = new char[rows][cols];
    for (char[] row : board) {
        Arrays.fill(row, ' ');
    }

    for (int i = 0; i < lines.length - 2; i++) { 
        String line = lines[i + 2];
        int boardCol = 0;
        
        for (int j = 0; j < line.length(); j++) {
            char c = line.charAt(j);

            if (c == 'K') {
                if (i == rows) { // bawah
                    System.out.println("bawah");
                    exitPosition = new Position(rows, j); 
                } else if (j == 0) { // Kiri
                    System.out.println("kiri");
                    exitPosition = new Position(i, -1);
                } else if (i == 0) { // Atas
                    System.out.println("atas");
                    exitPosition = new Position(-1, j);
                } else if (j == line.length() - 1) { // Kanan
                    System.out.println("kanan");
                    exitPosition = new Position(i, cols);
                }
            } else if ((c >= 'A' && c <= 'Z') || c == '.') {
                if (i < rows && boardCol < cols) {
                    board[i][boardCol++] = c;
                }
            }
        }
    }

    // displayBoard();
}

    private void identifyVehicles() {
    vehicles = new HashMap<>();

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            char current = board[i][j];
            if (current != '.' && current != 'K' && !vehicles.containsKey(current)) {
                // identify direction (horizontal or vertical)
                boolean isHorizontal = (j + 1 < cols && board[i][j + 1] == current);

                // starting position of the vehicle
                int length = 1;
                int startRow = i;
                int startCol = j;

                if (isHorizontal) {
                    int col = j + 1;
                    while (col < cols && board[i][col] == current) {
                        length++;
                        col++;
                    }
                } else { 
                    // vertical
                    int row = i + 1;
                    while (row < rows && board[row][j] == current) {
                        length++;
                        row++;
                    }
                }

                Vehicle vehicle = new Vehicle(current, startRow, startCol, length, isHorizontal);
                if (current == 'P') {
                    targetVehicle = vehicle;
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

    public Vehicle getTargetVehicle() {
        return targetVehicle;
    }
    public Map<Character, Vehicle> getVehicles() {
        return vehicles;
    }

    public void displayBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char cell = board[i][j];
                if (cell == '.') {
                    System.out.print(". ");
                } else {
                    // cek apakah kendaraan ini baru bergerak atau 'P'
                    if (cell == lastMovedVehicle) {
                        System.out.print(ANSI_YELLOW + cell + ANSI_RESET + " ");  // kuning
                    } else if (cell == 'P') {
                        System.out.print(ANSI_RED + cell + ANSI_RESET + " ");     // merah
                    } else {
                        System.out.print(cell + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // heuristic
    public int blockingHeuristic() {
        // cari posisi kendaraan target
        Vehicle target = targetVehicle;
        int blockingCount = 0;
        
        // asumsikan kendaraan target horizontal dan exit di sebelah kanan
        if (target.isHorizontal()) {
            int row = target.getRow();
            int endCol = target.getCol() + target.getLength() - 1;

            for (int col = endCol + 1; col < cols; col++) {
                if (board[row][col] != '.') {
                    blockingCount++;
                }
            }
        }
        // jika vertikal, perlu disesuaikan
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
            int headCol = target.getCol() + target.getLength() - 1;

            return headCol + 1 == exitPosition.getCol() && target.getRow() == exitPosition.getRow();
        } else {
            int headRow = target.getRow() + target.getLength() - 1;

            return headRow + 1 == exitPosition.getRow() && target.getCol() == exitPosition.getCol();
        }
    }


    boolean canMove(Vehicle vehicle, int direction) {
        if (vehicle.isHorizontal()) {
            if (direction < 0) { // left
                int newCol = vehicle.getCol() - 1;
                return newCol >= 0 && board[vehicle.getRow()][newCol] == '.';
            } else { // right
                int newCol = vehicle.getCol() + vehicle.getLength();
                return newCol < cols && board[vehicle.getRow()][newCol] == '.';
            }
        } else {
            if (direction < 0) { // up
                int newRow = vehicle.getRow() - 1;
                return newRow >= 0 && board[newRow][vehicle.getCol()] == '.';
            } else { // down
                int newRow = vehicle.getRow() + vehicle.getLength();
                return newRow < rows && board[newRow][vehicle.getCol()] == '.';
            }
        }
    }

    private char lastMovedVehicle = '\0';  // menyimpan ID kendaraan terakhir yang bergerak

    public void moveVehicle(char vehicleId, int direction) {
        Vehicle vehicle = vehicles.get(vehicleId);
        int row = vehicle.getRow();
        int col = vehicle.getCol();
        int length = vehicle.getLength();

        // clear current position
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < length; i++) {
                board[row][col + i] = '.';
            }
        } else {
            for (int i = 0; i < length; i++) {
                board[row + i][col] = '.';
            }
        }

        // update position
        if (vehicle.isHorizontal()) {
            vehicle.setCol(direction < 0 ? col - 1 : col + 1);
        } else {
            vehicle.setRow(direction < 0 ? row - 1 : row + 1);
        }

        // set new position
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

        lastMovedVehicle = vehicleId;  
        if (vehicleId == 'P') {
            targetVehicle = new Vehicle(vehicle);
        }
    }


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