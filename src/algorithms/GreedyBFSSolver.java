package algorithms;

import java.util.*;
import utils.Position;
import utils.RushHourGame;
import utils.State;
import utils.Vehicle;

public class GreedyBFSSolver {
    private RushHourGame game;

    public GreedyBFSSolver(RushHourGame game) {
        this.game = game;
    }

    public List<State> solve() {

        PriorityQueue<State> frontier = new PriorityQueue<>(
            Comparator.comparingInt(this::calculateHeuristic)
        );
        Set<String> explored = new HashSet<>();

        State start = new State(cloneVehicleMap(game.vehicles), 0, null, "");
        frontier.add(start);

        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            String hash = current.getHash();

            if (explored.contains(hash)) continue;
            explored.add(hash);

            if (isGoal(current)) {
                return reconstructPath(current);
            }

            List<State> neighbors = generateNeighbors(current);
            
            neighbors.sort(Comparator.comparingInt(this::calculateHeuristic));
            
            frontier.addAll(neighbors);
        }

        return null;
    }

    // nanti ubah aja sesuai heuristik
    private int calculateHeuristic(State State) {
        Vehicle target = State.vehicles.get(game.getTargetVehicle());
        Position exit = game.getExitPosition();
        
        if (target.getRow() != exit.getRow()) {
            return 1000;
        }
        
        int blockingCount = 0;
        int distanceToExit = exit.getCol() - (target.getCol() + target.getLength() - 1);
        
        for (Vehicle v : State.vehicles.values()) {
            if (v.getId() == target.getId()) continue;
            
            if (v.isHorizontal()) {
                if (v.getRow() == target.getRow() && 
                    v.getCol() > target.getCol() + target.getLength() - 1 &&
                    v.getCol() <= exit.getCol()) {
                    blockingCount++;
                }
            } else {
                if (v.getCol() > target.getCol() + target.getLength() - 1 &&
                    v.getCol() <= exit.getCol() &&
                    target.getRow() >= v.getRow() && 
                    target.getRow() < v.getRow() + v.getLength()) {
                    blockingCount++;
                }
            }
        }
        return blockingCount * 10 + distanceToExit;
    }

    private boolean isGoal(State State) {
        Vehicle target = State.vehicles.get(game.getTargetVehicle());
        Position exit = game.getExitPosition();

        if (target.isHorizontal()) {
            return target.getRow() == exit.getRow() && canReachExit(target, exit, State.vehicles);
        } else {
            return target.getCol() == exit.getCol() && 
                   (target.getRow() + target.getLength() - 1 == exit.getRow() ||
                    canReachExitVertical(target, exit, State.vehicles));
        }
    }
    
    private boolean canReachExit(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
        int row = target.getRow();
        int targetRightEdge = target.getCol() + target.getLength();
        
        for (int col = targetRightEdge; col <= exit.getCol(); col++) {
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
    
    private boolean canReachExitVertical(Vehicle target, Position exit, Map<Character, Vehicle> vehicles) {
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

                    // cost ignored, cuma perhatiin heuristik
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
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    board[r][c] = v.getId();
                }
            }
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
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    board[r][c] = v.getId();
                }
            }
        }

        Position exit = game.getExitPosition();
        if (exit.getRow() >= 0 && exit.getRow() < rows && 
            exit.getCol() >= 0 && exit.getCol() < cols && 
            board[exit.getRow()][exit.getCol()] == '.') {
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