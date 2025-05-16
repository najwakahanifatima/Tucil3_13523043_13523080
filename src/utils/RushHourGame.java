import java.util.HashMap;
import java.util.Map;

public class RushHourGame {
    private int rows;
    private int cols;
    private int numVehicles;
    private char[][] board;
    private Map<Character, Vehicle> vehicles;
    private char targetVehicle;
    private Position exitPosition;

    
    public RushHourGame(String input) {
        parseInput(input);
        identifyVehicles();
    }

    private void parseInput(String input) {
        String[] lines = input.split("\n");
        String[] dimensions = lines[0].split(" ");
        rows = Integer.parseInt(dimensions[0]);
        cols = Integer.parseInt(dimensions[1]);
        numVehicles = Integer.parseInt(lines[1]);
        
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = lines[i + 2];
            for (int j = 0; j < Math.min(cols, line.length()); j++) {
                board[i][j] = line.charAt(j);
                if (board[i][j] == 'P') {
                    targetVehicle = 'P';
                }
            }
            
            // cek posisi K, di posisi kanan
            if (line.length() > cols && line.charAt(cols) == 'K') {
                exitPosition = new Position(i, cols); 
            }
        }
        
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

    public void displayBoard(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
    }

    
}
