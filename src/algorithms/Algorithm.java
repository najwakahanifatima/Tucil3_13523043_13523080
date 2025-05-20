package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class Algorithm {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[43m";  // kuning
    public static final String ANSI_RED = "\u001B[41m";      // merah
    public static final String ANSI_GREEN = "\u001B[32m";  // hijau
    protected RushHourGame game;

    public Algorithm(RushHourGame game) {
        this.game = game;
    }
    
    protected boolean isGoal(State state, RushHourGame game) {
        Vehicle target = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();

        if (target.isHorizontal()) {
            int row = target.getRow();
            int tailCol = target.getCol();
            int headCol = target.getCol() + target.getLength() - 1;

            return ((headCol + 1 == exitPosition.getCol() || tailCol - 1 == exitPosition.getCol())
                    && row == exitPosition.getRow());
        } else {
            int col = target.getCol();
            int tailRow = target.getRow();
            int headRow = target.getRow() + target.getLength() - 1;

            return (headRow + 1 == exitPosition.getRow() || tailRow - 1 == exitPosition.getRow())
                    && col == exitPosition.getCol();
        }
    }


    protected boolean canExitHorizontal(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
        int row = target.getRow();
        int startCol, endCol;

        if (exit.getCol() < target.getCol()) {
            // exit is on the left side
            startCol = exit.getCol() + 1;
            endCol = target.getCol() - 1;
        } else {
            // exit is on the right side
            startCol = target.getCol() + target.getLength();
            endCol = exit.getCol();
        }

        for (int col = startCol; col <= endCol; col++) {
            for (Vehicle v : vehicles.values()) {
                if (v.getId() == target.getId()) continue;

                if (v.isHorizontal()) {
                    if (v.getRow() == row && col >= v.getCol() && col < v.getCol() + v.getLength()) {
                        return false;
                    }
                } else {
                    if (col == v.getCol() && row >= v.getRow() && row < v.getRow() + v.getLength()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


protected boolean canExitVertical(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
    int col = target.getCol();
    int startRow, endRow;

    if (exit.getRow() < target.getRow()) {
        // exit is on the top side
        startRow = exit.getRow() + 1;
        endRow = target.getRow() - 1;
        
        // Khusus untuk kasus pintu keluar di atas dan kendaraan di baris 0
        if (target.getRow() == 0) {
            // Kendaraan sudah di baris paling atas, langsung cek apakah kendaraan target
            // bisa bergerak keluar (tidak ada yang menghalangi di sepanjang lintasan keluar)
            if (exit.getRow() == -1 && exit.getCol() == col) {
                // Pastikan pintu keluar sejajar dengan kendaraan
                return true;
            } else {
                return false;
            }
        }
    } else {
        // exit is on the bottom side
        startRow = target.getRow() + target.getLength();
        endRow = exit.getRow();
    }

    // Jika interval invalid (startRow > endRow), langsung kembalikan false
    if (startRow > endRow) {
        return false;
    }

    // Periksa apakah ada kendaraan lain menghalangi jalur keluar
    for (int row = startRow; row <= endRow; row++) {
        for (Vehicle v : vehicles.values()) {
            if (v.getId() == target.getId()) continue;

            if (v.isHorizontal()) {
                if (row == v.getRow() && col >= v.getCol() && col < v.getCol() + v.getLength()) {
                    return false;
                }
            } else {
                if (col == v.getCol() && row >= v.getRow() && row < v.getRow() + v.getLength()) {
                    return false;
                }
            }
        }
    }

    return true;
}


    protected List<State> getNeighbours(State state) {
        List<State> neighbours = new ArrayList<>();
        char[][] board = buildBoardFromVehicles(state.vehicles);

        for (Vehicle v : state.vehicles.values()) {
            for (int direction : new int[]{-1,1}) {
                if (v.canMove(direction, board)) {
                    Map<Character, Vehicle> newVehicles = cloneVehicleMap(state.vehicles);
                    Vehicle movedVehicle = newVehicles.get(v.getId());
                    movedVehicle.move(direction);

                    String dirString = getDirectionString(v, direction);
                    String moveDesc = v.getId() + " moves " + dirString;
                    
                    State newState = new State(newVehicles, state.cost+1, state, moveDesc);
                    neighbours.add(newState);
                }
            }
        }
        return neighbours;
    }

    protected String getDirectionString(Vehicle v, int direction) {
        if (v.isHorizontal()) {
            return direction == 1 ? "right" : "left";
        } else {
            return direction == 1 ? "down" : "up";
        }
    }

    protected Map<Character, Vehicle> cloneVehicleMap(Map<Character, Vehicle> original) {
        Map<Character, Vehicle> copy = new HashMap<>();
        for (Map.Entry<Character, Vehicle> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Vehicle(entry.getValue()));
        }
        return copy;
    }

    protected char[][] buildBoardFromVehicles(Map<Character, Vehicle> vehicles) {
        int rows = game.getBoard().length;
        int cols = game.getBoard()[0].length;
        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '.');
        }

        for (Vehicle v : vehicles.values()) {
            for (int i = 0; i < v.getLength(); i++) {
                int r = v.getRow() + (v.isHorizontal() ? 0 : i);
                int c = v.getCol() + (v.isHorizontal() ? i : 0);
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    board[r][c] = v.getId();
                }
            }
        }

        return board;
    }

    protected List<State> constructPath(State goal) {
        List<State> path = new ArrayList<>();
        while (goal != null) {
            path.add(goal);
            goal = goal.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public int displaySolution(List<State> path) {
        if (path == null || path.isEmpty()) return 0;

        int step = 0;
        System.out.println("Step " + step++ + " - Initial state");
        printBoard(path.get(0).vehicles, '-');
        System.out.println("------------------------");

        if (path.size() == 1) return 0;

        char currentVehicle = '-';
        State lastState = path.get(0);
        String lastMove = "";

        for (int i = 1; i < path.size(); i++) {
            State state = path.get(i);
            char vehicle = state.move.charAt(0);
            
            if (vehicle != currentVehicle) {
                // cetak gerakan sebelumnya jika ada
                if (!lastMove.isEmpty()) {
                    System.out.println("Step " + step++);
                    System.out.println("Move: " + lastMove);
                    printBoard(lastState.vehicles, currentVehicle);
                    System.out.println("------------------------");
                }
                currentVehicle = vehicle;
            }
            
            lastState = state;
            lastMove = state.move;
        }

        // cetak gerakan terakhir
        if (!lastMove.isEmpty()) {
            System.out.println("Step " + step);
            System.out.println("Move: " + lastMove);
            printBoard(lastState.vehicles, currentVehicle);
            System.out.println("------------------------");
        }

        return step;
    }

    protected void printBoard(Map<Character, Vehicle> vehicles, char id) {
        int rows = game.getBoard().length;
        int cols = game.getBoard()[0].length;
        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], '.');
        }

        for (Vehicle v : vehicles.values()) {
            for (int i = 0; i < v.getLength(); i++) {
                int r = v.getRow() + (v.isHorizontal() ? 0 : i);
                int c = v.getCol() + (v.isHorizontal() ? i : 0);
                if (r >= 0 && r < rows && c >= 0 && c < cols) {  
                    board[r][c] = v.getId();
                }
            }
        }

        Position exit = game.getExitPosition();

        // cetak 'K' di luar papan jika exit position negatif
        if (exit.getRow() == -1) { // exit di atas papan
            for (int j = 0; j < exit.getCol(); j++) {
                System.out.print("  ");
            }
            System.out.println(ANSI_GREEN + "K" + ANSI_RESET + " ");
        } else if (exit.getCol() == -1) { // exit di kiri papan
            // System.out.print(ANSI_GREEN + "K" + ANSI_RESET + " ");
        }

        // cetak papan
        for (int i = 0; i < rows; i++) {
            //  'K' di kiri jika exit di baris ini
            if (exit.getCol() == -1 && i == exit.getRow()) {
                System.out.print(ANSI_GREEN + "K" + ANSI_RESET + " ");
            }
            else if(exit.getCol() == -1) { //iya?
                System.out.print("  ");
                
            }
            
            for (int j = 0; j < cols; j++) {
                //  'K' di dalam papan jika posisi exit valid
                if (i == exit.getRow() && j == exit.getCol() && 
                    exit.getRow() >= 0 && exit.getCol() >= 0) {
                    System.out.print(ANSI_GREEN + "K" + ANSI_RESET + " ");
                    continue;
                }
                
                // Cetak kendaraan dengan warna
                if (board[i][j] == id) {
                    System.out.print(ANSI_YELLOW + board[i][j] + ANSI_RESET + " ");
                } else if (board[i][j] == 'P') {
                    System.out.print(ANSI_RED + board[i][j] + ANSI_RESET + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            
            //  'K' di kanan jika exit di baris ini
            if (exit.getCol() == cols && i == exit.getRow()) {
                System.out.print(ANSI_GREEN + "K" + ANSI_RESET + " ");
            }
            System.out.println();
        }

        //  'K' di bawah papan jika exit position di bawah
        if (exit.getRow() == rows) {
            for (int j = 0; j < exit.getCol(); j++) {
                System.out.print("  ");
            }
            System.out.println(ANSI_GREEN + "K" + ANSI_RESET + " ");
        }
    }

    protected void debugUnexplored(PriorityQueue<State> unexplored) {
        for (State s : unexplored) {
            System.out.println("move: " + s.move + " --- h(n): " + Heuristic.calculateHeuristicGreedy(s, game, 1) + " --- f(n): " +
            (s.cost + Heuristic.calculateHeuristicGreedy(s, game, 1)));
        }
        System.out.println();
    }
}
