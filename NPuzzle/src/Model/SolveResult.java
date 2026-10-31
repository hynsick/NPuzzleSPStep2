package Model;

import java.util.List;
//알고리즘의 탐색 결과와 성능 정보를 저장하는 클래스
public class SolveResult {
	//알고리즘 실행 결과 구분
    public enum Status { 
    	SOLVED, 	//정답을 찾은 겅우
    	TIMEOUT, 	//제한 시간을 초과한 경우
    	CANCELLED 	//팀색이 완료되지 못한 경우
    }
    private Status status;
    private List<Node> path;
    private long expandedNodes;
    private long elapsedMs;
    
    //알고리즘 실행 결과를 저장
    public SolveResult(Status status, List<Node> path, long expandedNodes, long elapsedMs) {
        this.status = status;
        this.path = path;
        this.expandedNodes = expandedNodes;
        this.elapsedMs = elapsedMs;
    }
    //알고리즘 실행 상태 변환
    public Status getStatus() { return status; }
    //알고리즘이 찾은 풀이 경로 반환
    public List<Node> getPath() { return path; }
    
    //탐색한 노드 수 반환
    public long getExpandedNodes() { return expandedNodes; }
    //실행 시간 반환
    public long getElapsedMs() { return elapsedMs; }
    //움직임 횟수 카운트
    public int getMoveCount() {
    if (status != Status.SOLVED || path == null || path.isEmpty()) {
        return -1;
    }

    return path.size() - 1;
}
}