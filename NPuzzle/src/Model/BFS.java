package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
//너비 우선 탐색 알고리즘을 구현한 클래스
public class BFS {

    private static final int[][] DIRECTIONS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    private static final long TIME_LIMIT_MS = 10_000;
    // Queue를 이용한 너비 우선 탐색으로 퍼즐의 해결 경로를 탐색
    // 탐색 시간과 확장 노드 수를 측정하여 SolveResult로 반환
    public SolveResult solve(int[][] initialState) {
        long startTime = System.currentTimeMillis();
        long expandedNodes = 0;
        
        Queue<Node> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();

        Node start = new Node(
                copyState(initialState),
                0,
                0,
                null
        );

        queue.offer(start);
        visited.add(serialize(initialState));

        while (!queue.isEmpty()) {

            long elapsedMs = System.currentTimeMillis() - startTime;

            if (elapsedMs >= TIME_LIMIT_MS) {
                return new SolveResult(
                        SolveResult.Status.TIMEOUT,
                        null,
                        expandedNodes,
                        elapsedMs
                );
            }

            Node current = queue.poll();
            expandedNodes++;

            if (isGoal(current.getState())) {

                List<Node> path = new ArrayList<>();

                Node temp = current;

                while (temp != null) {
                    path.add(temp);
                    temp = temp.getParent();
                }

                Collections.reverse(path);

                long elapsedMs2 =
                        System.currentTimeMillis() - startTime;

                return new SolveResult(
                        SolveResult.Status.SOLVED,
                        path,
                        expandedNodes,
                        elapsedMs2
                );
            }

            int emptyRow = 0;
            int emptyCol = 0;

            outer:
            for (int i = 0; i < current.getState().length; i++) {
                for (int j = 0; j < current.getState().length; j++) {

                    if (current.getState()[i][j] == 0) {
                        emptyRow = i;
                        emptyCol = j;
                        break outer;
                    }
                }
            }


            for (int[] dir : DIRECTIONS) {

                int newRow = emptyRow + dir[0];
                int newCol = emptyCol + dir[1];

                if (newRow < 0 ||
                    newRow >= current.getState().length ||
                    newCol < 0 ||
                    newCol >= current.getState().length) {
                    continue;
                }

                int[][] nextState = copyState(current.getState());

                int temp = nextState[emptyRow][emptyCol];

                nextState[emptyRow][emptyCol] =
                        nextState[newRow][newCol];

                nextState[newRow][newCol] = temp;

                String key = serialize(nextState);

                if (visited.contains(key)) {
                    continue;
                }

                visited.add(key);

                queue.offer(
                        new Node(
                                nextState,
                                current.getG() + 1,
                                0,
                                current
                        )
                );
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

    // 목표 상태 검사
    private boolean isGoal(int[][] state) {

        int value = 1;
        int size = state.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                // 마지막 칸은 빈칸(0)
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

    // 2차원 배열 복사
    private int[][] copyState(int[][] state) {

        int[][] copy = new int[state.length][state.length];

        for (int i = 0; i < state.length; i++) {
            System.arraycopy(
                    state[i],
                    0,
                    copy[i],
                    0,
                    state.length
            );
        }

        return copy;
    }

    // 퍼즐 상태를 문자열로 반환하여 방문 여부를 확인 하는데 사용
    private String serialize(int[][] state) {

        StringBuilder sb = new StringBuilder();

        for (int[] row : state) {
            for (int num : row) {
                sb.append(num).append(',');
            }
        }

        return sb.toString();
    }
}
