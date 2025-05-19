package algorithms;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Move;
import utils.RushHourGame;


public abstract class SearchAlgorithm {
    // metrics tracking
    protected int nodesExpanded = 0;
    protected int nodesGenerated = 0;
    protected int maxQueueSize = 0;
    
   
    public abstract List<Move> solve(RushHourGame initialState);
    
    
    protected List<RushHourGame> generateNextStates(RushHourGame state) {
        List<RushHourGame> nextStates = state.generateNextStates();
        nodesGenerated += nextStates.size();
        return nextStates;
    }
    
    public Map<String, Integer> getMetrics() {
        Map<String, Integer> metrics = new HashMap<>();
        metrics.put("nodesExpanded", nodesExpanded);
        metrics.put("nodesGenerated", nodesGenerated);
        metrics.put("maxQueueSize", maxQueueSize);
        return metrics;
    }
}