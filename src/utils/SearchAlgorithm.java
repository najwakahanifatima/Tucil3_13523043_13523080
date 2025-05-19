package utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class defining the interface for search algorithms
 */
abstract class SearchAlgorithm {
    // Metrics tracking
    protected int nodesExpanded = 0;
    protected int nodesGenerated = 0;
    protected int maxQueueSize = 0;
    
   
    public abstract List<Move> solve(RushHourGame initialState);
    
    
    protected List<RushHourGame> generateNextStates(RushHourGame state) {
        List<RushHourGame> nextStates = state.generateNextStates();
        nodesGenerated += nextStates.size();
        return nextStates;
    }
    
    /**
     * Get metrics collected during search
     * 
     * @return Map of metrics
     */
    public Map<String, Integer> getMetrics() {
        Map<String, Integer> metrics = new HashMap<>();
        metrics.put("nodesExpanded", nodesExpanded);
        metrics.put("nodesGenerated", nodesGenerated);
        metrics.put("maxQueueSize", maxQueueSize);
        return metrics;
    }
}