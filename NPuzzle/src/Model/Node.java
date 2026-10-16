package Model;

public class Node implements Comparable<Node> {
	
    private int[][] state;
    private int g; // 시작점부터 현재까지 이동 횟수
    private int h; // 맨해튼 거리 휴리스틱 예상 비용
    private Node parent;

    public Node(int[][] state, int g, int h, Node parent) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    public int getF() { return g + h; }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }

    public int[][] getState() { return state; }
    public int getG() { return g; }
    public Node getParent() { return parent; }

}