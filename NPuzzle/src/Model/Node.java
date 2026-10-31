package Model;
//탐색 과정에서 퍼즐 상태와 비용 정보를 저장하는 클래스
public class Node implements Comparable<Node> {
    private int[][] state;
    private int g; 
    private int h;
    private Node parent;
    //퍼즐 상태와 탐색 비용, 부모 노드 정보를 저장하는 객체 생성
    public Node(int[][] state, int g, int h, Node parent) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }
    
    //A*알고리즘 사용 함수
    //f(n)을 구하는 메서드
    public int getF() { return g + h; }
    
    // 두 노드의 f값을 비교하는 메서드(f값이 작은 노드를 먼저 꺼내기 위해 사용)
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }
    
    //현재 노드의 퍼즐 상태 반환
    public int[][] getState() { return state; }
    // 시작 상태부터 현재 상태까지 이동한 비용 반환
    public int getG() { return g; }
    //이전 노드 반환하여 경로 복원에 사용
    public Node getParent() { return parent; }
}