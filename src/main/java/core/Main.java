package core;

public class Main {
    public static void main(String[] args) {
        String fen = "b1n3q1/p7/R5p1/p7/5K2/P1Nk4/1p3pPP/B7";
        var game = new Game();
        game.initPositionFromFen(fen);
        game.printBoard();
    }
}
