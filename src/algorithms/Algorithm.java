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
    protected RushHourGame game;

    public Algorithm(RushHourGame game) {
        this.game = game;
    }
    
    protected boolean isGoal(State state, RushHourGame game) {
        Vehicle target = state.getVehicle().get('P');
        Position exitPosition = game.getExitPosition();

        if (target.isHorizontal()) {
            int headCol = target.getCol() + target.getLength() - 1;

            return headCol + 1 == exitPosition.getCol() && target.getRow() == exitPosition.getRow();
        } else {
            int headRow = target.getRow() + target.getLength() - 1;

            return headRow + 1 == exitPosition.getRow() && target.getCol() == exitPosition.getCol();
        }
    }

    protected boolean canExitHorizontal(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
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

    protected boolean canExitVertical(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
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

    protected void printBoard(Map<Character, Vehicle> vehicles) {
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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j]);
                if ((i+1 == exit.getRow() && j == exit.getCol())|| (j+1 == exit.getCol() && i == exit.getRow())) {
                    System.out.print("K");
                }
            }
            System.out.println();
        }
    }

    private void debugUnexplored(PriorityQueue<State> unexplored) {
        for (State s : unexplored) {
            System.out.println("move: " + s.move + " --- h(n): " + Heuristic.calculateHeuristicGreedy(s));
        }
    }
}
