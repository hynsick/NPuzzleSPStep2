
package View;
//콘솔 화면 출력과 사용자 안내를 담당하는 클래스
public class ConsoleView {

    // 퍼즐 보드 출력
    public void printBoard(int[][] state) {
        System.out.println("\n현재 보드:");

        for (int[] row : state) {
            for (int tile : row) {
                if (tile == 0) {
                    System.out.print("   ");
                } else {
                    System.out.printf("%3d", tile);
                }
            }
            System.out.println();
        }
    }

    // 사용자에게 안내 메시지 출력
    public void printMessage(String message) {
        System.out.println(message);
    }

    // 보드 크기 입력 안내
    public void showSizePrompt() {
        System.out.print("보드 크기를 입력하세요 (예: 3): ");
    }

    // 타일 번호 입력 안내
    public void showMovePrompt() {
        System.out.print("이동할 타일 번호를 입력하세요 (포기: 0): ");
    }

    // 재시작 여부 입력 안내
    public void showRestartPrompt() {
        System.out.print("다시 시작하시겠습니까? (y/n): ");
    }
}
