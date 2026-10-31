
package Model;

import java.util.*;
//A* 탐색 알고리즘을 구현한 클래스
public class AStar {

    private static final long TIME_LIMIT_MS = 10_000;

    // 맨해튼 거리와 우선순위 큐를 이용하여 퍼즐의 해결 경로를 탐색
    // 탐색 결과와 성능 정보를 SolveResult로 반환
    public SolveResult solve(int[][] initialState) {

        long startTime = System.currentTimeMillis();
        long expandedNodes = 0;

        int size = initialState.length;

        PriorityQueue<Node> openList = new PriorityQueue<>();

        Set<String> closedList = new HashSet<>();

        Set<String> openSet = new HashSet<>();

        int[][] initialCopy = copyBoard(initialState, size);
        int hCost = calculateManhattan(initialCopy, size);

        Node startNode = new Node(initialCopy, 0, hCost, null);

        openList.offer(startNode);
        openSet.add(boardToString(initialCopy));

        while (!openList.isEmpty()) {

            long elapsedMs = System.currentTimeMillis() - startTime;

            if (elapsedMs >= TIME_LIMIT_MS) {
                return new SolveResult(
                        SolveResult.Status.TIMEOUT,
                        null,
                        expandedNodes,
                        elapsedMs
                );
            }

            Node currentNode = openList.poll();

            String currentKey = boardToString(currentNode.getState());
            openSet.remove(currentKey);

            if (closedList.contains(currentKey)) {
                continue;
            }

            expandedNodes++;

            if (isGoalState(currentNode.getState(), size)) {

                List<Node> path = reconstructPath(currentNode);

                long elapsedMs2 =
                        System.currentTimeMillis() - startTime;

                return new SolveResult(
                        SolveResult.Status.SOLVED,
                        path,
                        expandedNodes,
                        elapsedMs2
                );
            }

            closedList.add(currentKey);

            for (Node neighbor : generateNeighbors(currentNode, size)) {

                String neighborKey =
                        boardToString(neighbor.getState());

                if (closedList.contains(neighborKey)) {
                    continue;
                }

                if (openSet.contains(neighborKey)) {
                    continue;
                }

                openList.offer(neighbor);
                openSet.add(neighborKey);
            }
        }

        long elapsedMs = System.currentTimeMillis() - startTime;

        return new SolveResult(
                SolveResult.Status.CANCELLED,
                null,
                expandedNodes,
                elapsedMs
        );
    }

    // 각 타일의 현재 위치와 목표 위치 사이의 맨해튼 거리 합계를 계산
    private int calculateManhattan(int[][] state, int size) {

        int distance = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                int value = state[i][j];

                if (value != 0) {

                    int targetRow = (value - 1) / size;
                    int targetCol = (value - 1) % size;

                    distance += Math.abs(i - targetRow)
                              + Math.abs(j - targetCol);
                }
            }
        }

        return distance;
    }

    // 빈칸을 상하좌우로 이동시켜 탐색 가능한 다음 노드들을 생성
    private List<Node> generateNeighbors(Node current, int size) {

        List<Node> neighbors = new ArrayList<>();

        int[][] state = current.getState();

        int emptyRow = -1;
        int emptyCol = -1;

        outer:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (state[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    break outer;
                }
            }
        }

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {

            int nr = emptyRow + dr[i];
            int nc = emptyCol + dc[i];

            if (nr < 0 || nr >= size ||
                nc < 0 || nc >= size) {
                continue;
            }

            int[][] newState = copyBoard(state, size);

            newState[emptyRow][emptyCol] = newState[nr][nc];
            newState[nr][nc] = 0;

            int g = current.getG() + 1;
            int h = calculateManhattan(newState, size);

            neighbors.add(
                    new Node(newState, g, h, current)
            );
        }

        return neighbors;
    }

    // 목표 상태 여부 판별
    private boolean isGoalState(int[][] state, int size) {

        int value = 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

            	if (i == size - 1 && j == size - 1) {
                    return state[i][j] == 0;
                }

                if (state[i][j] != value++) {
                    return false;
                }
            }
        }

        return true;
    }

    // 부모 노드를 따라가며 경로 복원
    private List<Node> reconstructPath(Node node) {

        List<Node> path = new ArrayList<>();

        while (node != null) {
            path.add(node);
            node = node.getParent();
        }

        Collections.reverse(path);

        return path;
    }

    // 2차원 배열 복사
    private int[][] copyBoard(int[][] src, int size) {

        int[][] dest = new int[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, size);
        }

        return dest;
    }

    // 보드 상태를 문자열로 변환
    private String boardToString(int[][] state) {

        StringBuilder sb = new StringBuilder();

        for (int[] row : state) {
            for (int val : row) {
                sb.append(val).append(",");
            }
        }

        return sb.toString();
    }
}

