package utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Node class for the search algorithm that stores state, path information, and A* specific metrics
 */
class SearchNode implements Comparable<SearchNode> {
    private final RushHourGame state;
    private final int gScore;     // g(n): cost from start to this node
    private final int hScore;     // h(n): heuristic estimate from this node to the goal
    private final int fScore;     // f(n): g(n) + h(n)
    private final List<Move> path;

    public SearchNode(RushHourGame state, int gScore, int hScore, List<Move> path) {
        this.state = new RushHourGame(state);  // Deep copy
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = gScore + hScore;         // f(n) = g(n) + h(n)
        this.path = new ArrayList<>(path);
    }

    public RushHourGame getState() {
        return state;
    }

    public int getGScore() {
        return gScore;
    }

    public int getHScore() {
        return hScore;
    }

    public int getFScore() {
        return fScore;
    }

    public List<Move> getPath() {
        return path;
    }

    @Override
    public int compareTo(SearchNode other) {
        // Compare by f-score first
        int fComparison = Integer.compare(this.fScore, other.fScore);
        if (fComparison != 0) {
            return fComparison;
        }
        
        // Break ties using h-score (prefer nodes closer to the goal)
        return Integer.compare(this.hScore, other.hScore);
    }
}