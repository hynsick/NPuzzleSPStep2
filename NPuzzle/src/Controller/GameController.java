package Controller;

import Model.AStar;
import Model.BFS;
import Model.Node;
import Model.PuzzleBoard;
import Model.SolveResult;
import View.ConsoleView;

import java.util.List;
import java.util.Scanner;
//사용자 입력과 게임 전체 흐름을 제어하는 클래스
public class GameController {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleView view = new ConsoleView();

    // 게임 전체 진행
    public void run() {
        boolean restart = true;

        while (restart) {
            int size = readBoardSize();

            PuzzleBoard board = new PuzzleBoard(size);
            board.shuffleBoard();

            playGame(board);

            view.showRestartPrompt();
            String answer = scanner.nextLine().trim();
            restart = answer.equalsIgnoreCase("y");
        }

        scanner.close();
        view.printMessage("프로그램을 종료합니다.");
    }

    // 보드 크기 입력
    private int readBoardSize() {
        while (true) {
            view.showSizePrompt();
            String input = scanner.nextLine().trim();

            try {
                int size = Integer.parseInt(input);

                if (size >= 3) {
                    return size;
                }

                view.printMessage("보드 크기는 3 이상이어야 합니다.");

            } catch (NumberFormatException e) {
                view.printMessage("숫자를 입력해 주세요.");
            }
        }
    }

// 한 판의 게임 진행(사용자 입력에 따라 타일 이동, 퍼즐 재배치, 게임 포기 등을 처리)
private void playGame(PuzzleBoard board) {
    while (true) {
        view.printBoard(board.getBoard());

        if (board.isSolved()) {
            view.printMessage("퍼즐을 완성했습니다!");
            return;
        }

        view.showMovePrompt();
        view.printMessage("R: 보드 다시 섞기 / 0: 포기 및 자동 풀이");

        String input = scanner.nextLine().trim();

        // 보드 다시 섞기
        if (input.equalsIgnoreCase("r")) {
            board.shuffleBoard();
            view.printMessage("보드를 다시 섞었습니다!");
            continue;
        }

        int target;

        try {
            target = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            view.printMessage("올바른 숫자 또는 R을 입력해 주세요.");
            continue;
        }

        // 포기 및 자동 풀이
        if (target == 0) {
            handleGiveUp(board);
            return;
        }

        // 입력한 번호의 타일 이동
        moveTileByNumber(board, target);
    }
}


// 선택한 타일이 빈칸과 인접한지 확인하고 이동을 처리
private void moveTileByNumber(PuzzleBoard board, int target) {

    int[][] state = board.getBoard();
    int size = state.length;

    int targetRow = -1;
    int targetCol = -1;
    int emptyRow = -1;
    int emptyCol = -1;

 
    for (int r = 0; r < size; r++) {
        for (int c = 0; c < size; c++) {

            if (state[r][c] == target) {
                targetRow = r;
                targetCol = c;
            }

            if (state[r][c] == 0) {
                emptyRow = r;
                emptyCol = c;
            }
        }
    }

 
    if (targetRow == -1) {
        view.printMessage("해당 번호의 타일이 없습니다.");
        return;
    }

    int distance =
            Math.abs(targetRow - emptyRow)
            + Math.abs(targetCol - emptyCol);

    if (distance != 1) {
        view.printMessage("이 타일은 이동할 수 없습니다.");
        return;
    }


    board.moveTile(targetRow, targetCol);
}

// 포기 및 자동 풀이 처리
private void handleGiveUp(PuzzleBoard board) {

    int[][] currentState = board.getBoard();
    int size = currentState.length;

    view.printMessage(
            "\n자동 풀이 및 성능 비교 연산을 시작합니다...");

    // 1. A* 실행
    AStar aStar = new AStar();
    SolveResult aStarResult = aStar.solve(currentState);

    // 2. BFS 실행: 4x4 이상은 생략
    SolveResult bfsResult = null;

    if (size >= 4) {
        view.printMessage(
                "4x4 이상은 BFS 메모리 초과 우려로 생략합니다.");
    } else {
        BFS bfs = new BFS();
        bfsResult = bfs.solve(currentState);
    }

    // 3. 성능 비교 결과 출력
    view.printMessage("\n===========================");
    view.printMessage("[알고리즘 성능 비교 결과]");

    // A* 결과
    view.printMessage("\n[A* 탐색]");

    if (aStarResult.getStatus() == SolveResult.Status.SOLVED) {
        System.out.printf(
                "- 해답 이동 횟수: %d회%n" +
                "- 소요 시간: %d ms%n" +
                "- 확장 노드 수: %d%n",
                aStarResult.getMoveCount(),
                aStarResult.getElapsedMs(),
                aStarResult.getExpandedNodes()
        );
    } else {
        view.printMessage(
                "A* 해답 탐색 실패: " + aStarResult.getStatus());
    }

    // BFS 결과
    view.printMessage("\n[BFS 완전 탐색]");

    if (bfsResult == null) {
        view.printMessage("BFS 실행을 생략했습니다.");
    } else if (bfsResult.getStatus() == SolveResult.Status.SOLVED) {
        System.out.printf(
                "- 해답 이동 횟수: %d회%n" +
                "- 소요 시간: %d ms%n" +
                "- 확장 노드 수: %d%n",
                bfsResult.getMoveCount(),
                bfsResult.getElapsedMs(),
                bfsResult.getExpandedNodes()
        );
    } else {
        view.printMessage(
                "BFS 해답 탐색 실패: " + bfsResult.getStatus());
    }

    view.printMessage("===========================\n");

    // 4. A*가 찾은 경로로 자동 풀이 진행
    if (aStarResult.getStatus() != SolveResult.Status.SOLVED) {
        view.printMessage(
                "A*가 해답을 찾지 못해 자동 풀이를 진행할 수 없습니다.");
        return;
    }

    List<Node> path = aStarResult.getPath();

    view.printMessage("[A* 자동 풀이 시작]");
    view.printMessage("총 이동 횟수: "
            + aStarResult.getMoveCount() + "회");

    // 시작 상태부터 정답 상태까지 한 단계씩 출력
    for (int i = 0; i < path.size(); i++) {

        Node step = path.get(i);

        if (i == 0) {
            view.printMessage("\n[시작 상태]");
        } else {
            view.printMessage("\n[이동 " + i + "회]");
        }

        view.printBoard(step.getState());

        // 보드가 한 단계씩 보이도록 잠시 대기
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            view.printMessage("자동 풀이가 중단되었습니다.");
            return;
        }
    }

    view.printMessage("\n퍼즐 자동 풀이가 완료되었습니다!");
}

}

