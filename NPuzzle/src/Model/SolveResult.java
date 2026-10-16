package Model;

import java.util.List;

public class SolveResult {
    public enum Status { SOLVED, TIMEOUT, CANCELLED }
    
    private Status status;
    private List<Node> path;
    private long expandedNodes;
    private long elapsedMs;

    public SolveResult(Status status, List<Node> path, long expandedNodes, long elapsedMs) {
        this.status = status;
        this.path = path;
        this.expandedNodes = expandedNodes;
        this.elapsedMs = elapsedMs;
    }
    
    public Status getStatus() { return status; }
    public List<Node> getPath() { return path; }
}