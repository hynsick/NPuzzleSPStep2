package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//퍼즐의 생성, 이동, 정답 판정 등을 관리하는 클래스
public class PuzzleBoard {

    private int[][] board;
    private int size;
    private int emptyRow;
    private int emptyCol;

    // 입력받은 크기에 맞춰 퍼즐 배열을 생성
    public PuzzleBoard(int size) {
        this.size = size;
        this.board = new int[size][size];
    }

    // 숫자를 무작위로 배치하여 해결 가능한 초기 퍼즐을 생성
    public void shuffleBoard() {
        List<Integer> tiles = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            tiles.add(i);
        }

        do {
        	// 리스트의 숫자들을 무작위로 섞음
            Collections.shuffle(tiles);
            //리스트에 숫자를 꺼낼 위치
            int idx = 0;
            //섞인 숫자를 2차원 배열에 배치
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    board[i][j] = tiles.get(idx++);
                    //0은 빈칸이므로 위치를 별도로 저장
                    if (board[i][j] == 0) {
                        emptyRow = i;
                        emptyCol = j;
                    }
                }
            }
            //해결 불가능하거나 이미 완성된 퍼즐이면 다시 섞음
        } while (!isSolvable() || isSolved());
    }

    //특정 위치의 타이을 빈칸으로 이동 시키는 메서드
    public void moveTile(int row, int col) {
        board[emptyRow][emptyCol] = board[row][col];
        board[row][col] = 0;
        emptyRow = row;
        emptyCol = col;
    }
    //현재 퍼즐이 해결 가능한 상태인지 검사하는 메서드
    private boolean isSolvable() {
        int[] flat = new int[size * size];
        int idx = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flat[idx++] = board[i][j];
            }
        }
        int inversions = 0;
        for (int i = 0; i < flat.length; i++) {
            for (int j = i + 1; j < flat.length; j++) {
                if (flat[i] != 0 && flat[j] != 0 && flat[i] > flat[j]) {
                    inversions++;
                }
            }
        }
        if (size % 2 != 0) {
            return inversions % 2 == 0;
        } else {
            int emptyRowFromBottom = size - emptyRow;
            if (emptyRowFromBottom % 2 == 0) {
                return inversions % 2 != 0;
            } else {
                return inversions % 2 == 0;
            }
        }
    }
    // 모든 숫자가 정답 순서로 배치되었는지 확인
    public boolean isSolved() {
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) return board[i][j] == 0;
                if (board[i][j] != count++) return false;
            }
        }
        return true;
    }
    // 현재 퍼즐의 2차원 배열을 반환
    public int[][] getBoard() { return board; }
    // 현재 빈칸의 행 위치를 반환
    public int getEmptyRow() { return emptyRow; }
    // 현재 빈칸의 열 위치를 반환
    public int getEmptyCol() { return emptyCol; }
}
