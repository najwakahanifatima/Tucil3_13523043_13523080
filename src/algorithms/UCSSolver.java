package algorithms;

import java.util.*;
import utils.RushHourGame;
import utils.Vehicle;
import utils.Position;
import utils.State;

public class UCSSolver {
    private RushHourGame game;

    public UCSSolver(RushHourGame game) {
        this.game = game;
    }

    public List<State> solve() {
        // pakai priority queue dengan prio cost terendah untuk main logic nya
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        // explored
        Set<String> explored = new HashSet<>();

        // buat initial state
        State start = new State(cloneVehicleMap(game.vehicles), 0, null, "");
        frontier.add(start);

        while (!frontier.isEmpty()) {
            State current = frontier.poll(); // ambil initial state
            String hash = current.getHash(); // ambil string dari state vehicles

            // klo state ini sudah dieksplor, skip
            if (explored.contains(hash)) continue;
            explored.add(hash); // klo belum, add ke hash

            // klo sudah sampai goal, construct path
            if (isGoal(current)) {
                return reconstructPath(current);
            }

            // menambahkan all neighbours state dari current
            frontier.addAll(generateNeighbors(current));
        }

        return null;
    }

    private boolean isGoal(State State) {
        Vehicle target = State.vehicles.get('P'); // cek target
        Position exit = game.getExitPosition(); // cek exit position

        if (target.isHorizontal()) {
            return target.getRow() == exit.getRow() && 
                   (target.getCol() + target.getLength() - 1 == exit.getCol() || 
                    canReachExit(target, exit, State.vehicles));
        } else {
            return target.getCol() == exit.getCol() && 
                   target.getRow() + target.getLength() - 1 == exit.getRow();
        }
    }
    
    private boolean canReachExit(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
        int row = target.getRow();
        int targetRightEdge = target.getCol() + target.getLength();
        
        for (int col = targetRightEdge; col <= exit.getCol(); col++) {
            // cek apakah ada vehicle yg menghalangi
            for (Vehicle v : vehicles.values()) {
                if (v.getId() == target.getId()) continue; // skip klo target
                
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

    private List<State> generateNeighbors(State State) {
        List<State> neighbors = new ArrayList<>();
        char[][] board = buildBoardFromVehicles(State.vehicles);

        for (Vehicle v : State.vehicles.values()) {
            for (int direction : new int[]{-1, 1}) {
                if (v.canMove(direction, board)) {
                    Map<Character, Vehicle> newVehicles = cloneVehicleMap(State.vehicles);
                    Vehicle movedVehicle = newVehicles.get(v.getId());
                    movedVehicle.move(direction);

                    String dirString = getDirectionString(v, direction);
                    String moveDesc = v.getId() + " moves " + dirString;

                    State newState = new State(newVehicles, State.cost + 1, State, moveDesc);
                    neighbors.add(newState);
                }
            }
        }

        return neighbors;
    }

    private String getDirectionString(Vehicle v, int direction) {
        if (v.isHorizontal()) {
            return direction == 1 ? "right" : "left";
        } else {
            return direction == 1 ? "down" : "up";
        }
    }

    private Map<Character, Vehicle> cloneVehicleMap(Map<Character, Vehicle> original) {
        Map<Character, Vehicle> copy = new HashMap<>();
        for (Map.Entry<Character, Vehicle> entry : original.entrySet()) {
            copy.put(entry.getKey(), new Vehicle(entry.getValue()));
        }
        return copy;
    }

    private char[][] buildBoardFromVehicles(Map<Character, Vehicle> vehicles) {
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
                board[r][c] = v.getId();
            }
        }

        Position exit = game.getExitPosition();
        if (board[exit.getRow()][exit.getCol()] == '.') {
            board[exit.getRow()][exit.getCol()] = 'K';
        }

        return board;
    }

    private List<State> reconstructPath(State goal) {
        List<State> path = new ArrayList<>();
        while (goal != null) {
            path.add(goal);
            goal = goal.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public void displaySolution(List<State> path) {
        int step = 0;
        for (State State : path) {
            System.out.println("Step " + step++);
            if (State.move != null && !State.move.isEmpty()) {
                System.out.println("Move: " + State.move);
            } else {
                System.out.println("Initial state");
            }

            printBoard(State.vehicles);
            System.out.println("------------------------");
        }
    }

    private void printBoard(Map<Character, Vehicle> vehicles) {
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
                board[r][c] = v.getId();
            }
        }

        Position exit = game.getExitPosition();
        if (board[exit.getRow()][exit.getCol()] == '.') {
            board[exit.getRow()][exit.getCol()] = 'K';
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }
}