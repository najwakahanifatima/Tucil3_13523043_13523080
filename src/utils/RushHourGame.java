package utils;

import java.util.HashMap;
import java.util.Map;

public class RushHourGame {
    private int rows;
    private int cols;
    private int numVehicles;
    private char[][] board;
    public Map<Character, Vehicle> vehicles;
    private char targetVehicle;
    private Position exitPosition;

    
    public RushHourGame(String input) {
        parseInput(input);
        identifyVehicles();
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
                } else if (c == 'P') {
                    targetVehicle = 'P';
                }
            }
        }

        System.err.println("Board awal:" + rows + " x " + cols);
        displayBoard();
    }

    private void identifyVehicles() {
        vehicles = new HashMap<>();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char current = board[i][j];
                if (current != '.' && current != 'K' && !vehicles.containsKey(current)) {
                    // identifikasi arah kendaraan (horizontal atau vertikal)
                    boolean isHorizontal = (j + 1 < cols && board[i][j + 1] == current);
                    
                    // cari panjang dan posisi kendaraan
                    int length = 1;
                    int startRow = i;
                    int startCol = j;
                    
                    if (isHorizontal) {
                        int col = j + 1;
                        while (col < cols && board[i][col] == current) {
                            length++;
                            col++;
                        }
                    } else { // vertikal
                        int row = i + 1;
                        while (row < rows && board[row][j] == current) {
                            length++;
                            row++;
                        }
                    }
                    
                    vehicles.put(current, new Vehicle(current, startRow, startCol, length, isHorizontal));
                }
            }
        }
    }
    
    public Position getExitPosition() {
        return exitPosition;
    }
    
    public char getTargetVehicle() {
        return targetVehicle;
    }

    public char[][] getBoard(){
        return this.board;
    }

    public void displayBoard(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
    }

    public Map<Character, Vehicle> vehicles() {
        return vehicles;
    }
    
}
