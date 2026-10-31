package Puzzle;

import Controller.GameController;
//N-Puzzle 프로그램의 시작 클래스
public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController();
        //run() 메서드를 호출하여 게임시작 
        controller.run();
    }
}
