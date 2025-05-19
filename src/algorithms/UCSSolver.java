package algorithms;

import java.util.*;
import utils.*;

public class UCSSolver {
    private RushHourGame game;

    public UCSSolver(RushHourGame game) {
        this.game = game;
    }

    public List<State> solve() {
        // cek priority queue dengan cost terendah
        PriorityQueue<State> unexplored = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        // explored state
        Set<String> explored = new HashSet<>();

        // initial state
        State start = new State(cloneVehicleMap(game.vehicles), 0, null, "");
        unexplored.add(start);

        // iterate sampai ketemu goals atau semua kemungkinan state sudah habis dicek
        while (!unexplored.isEmpty()) {
            State current = unexplored.poll();
            String stateString = current.getStateString();

            // klo state sudah dieksplor, skip
            if (explored.contains(stateString)) continue;
            explored.add(stateString);

            // klo udah sampai goal, buat path
            if (isGoal(current)) {
                return constructPath(current);
            }

            // klo blm sampai goal, eksplor all neighbour state
            unexplored.addAll(getNeighbours(current));
        }

        return null;
    }

    private boolean isGoal(State state) {
        Vehicle target = state.vehicles.get('P'); // cek target
        Position exit = game.getExitPosition(); // cek exit position

        if (target.isHorizontal()) {
            return target.getRow() == exit.getRow() && 
            (target.getCol() + target.getLength() - 1 == exit.getCol() || canExitHorizontal(target, exit, state.vehicles));
        } else {
            return target.getCol() == exit.getCol() &&
            (target.getRow() + target.getLength() - 1 == exit.getRow() || canExitVertical(target, exit, state.vehicles));
        }
    }

    private boolean canExitHorizontal(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
        int row = target.getRow();
        int edge = target.getCol() + target.getLength();

        for (int col = edge; col <= exit.getCol(); col++) {
            //cek apakah ada vehicle yang menghalangi
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

    private boolean canExitVertical(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
        int col = target.getCol();
        int targetBottomEdge = target.getRow() + target.getLength();
        
        for (int row = targetBottomEdge; row <= exit.getRow(); row++) {
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

    private List<State> getNeighbours(State state) {
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

    private List<State> constructPath(State goal) {
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
        for (State state : path) {
            System.out.println("Step " + step++);
            if (state.move != null && !state.move.isEmpty()) {
                System.out.println("Move: " + state.move);
            } else {
                System.out.println("Initial state");
            }

            printBoard(state.vehicles);
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