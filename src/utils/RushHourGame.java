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

        
        // cek baris di atas papan 
        if (lines.length > 2) {
            String topLine = lines[2];
            for (int j = 0; j < topLine.length(); j++) {
                if (topLine.charAt(j) == 'K') {
                    // K di atas papan
                    exitPosition = new Position(-1, j);
                    System.out.println("K ditemukan di atas papan: exitPosition=-1," + j);
                    break;
                }
            }
        }
        
        // cek baris di bawah papan 
        if (lines.length > rows + 2) {
            String bottomLine = lines[rows + 2];
            for (int j = 0; j < bottomLine.length(); j++) {
                if (bottomLine.charAt(j) == 'K') {
                    // K di bawah papan 
                    exitPosition = new Position(rows, j);
                    System.out.println("K ditemukan di bawah papan: exitPosition=" + rows + "," + j);
                    break;
                }
            }
        }
        
        // isi papan dengan karakter non-K
        int boardStartLine = 0;
        
        if (lines.length > 2 && lines[2].contains("K")) {
            boardStartLine = 1; 
        }
        
        for (int i = 0; i < rows; i++) {
            int lineIndex = i + boardStartLine + 2; 
            if (lineIndex >= lines.length) break;
            
            String line = lines[lineIndex];
            int boardCol = 0;
            
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == ' ') continue;
                
                if (c == 'K') continue;
                
                if ((c >= 'A' && c <= 'Z') || c == '.') {
                    if (boardCol < cols) {
                        board[i][boardCol++] = c;
                    }
                }
            }
            
            // K di kiri dan kanan
            if (line.length() > 0 && line.charAt(0) == 'K') {
                exitPosition = new Position(i, -1);
                System.out.println("K ditemukan di sisi kiri: exitPosition=" + i + ",-1");
            }
            
            if (boardCol == cols && line.length() > cols && line.charAt(cols) == 'K') {
                exitPosition = new Position(i, cols);
                System.out.println("K ditemukan di sisi kanan: exitPosition=" + i + "," + cols);
            }
        }
        
        int lastBoardRow = Math.min(rows - 1, lines.length - 3);
        if (lastBoardRow >= 0) {
            String lastLine = lines[lastBoardRow + 2];
            for (int j = 0; j < lastLine.length(); j++) {
                if (lastLine.charAt(j) == 'K' && (j >= cols || j == lastLine.length() - 1)) {
                    exitPosition = new Position(lastBoardRow, cols);
                    System.out.println("K ditemukan di sisi kanan terakhir: exitPosition=" + lastBoardRow + "," + cols);
                    break;
                }
            }
        }
        
        if (exitPosition == null) {
            throw new IllegalArgumentException("Pintu keluar (K) tidak ditemukan dalam input");
        }
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
    
}