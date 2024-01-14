package core;

import core.move.Move;
import core.move.MoveMaker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// positions from https://www.chessprogramming.org/Perft_Results
class MoveGenerationTest {
    private Game game;

    @ParameterizedTest
    @CsvSource({"1,0", "20,1", "400,2", "8902,3", "197281,4", "4865609,5"})
    void testStartPosition(int expectedPositionCount, int depth) {
        game = new Game(null, true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "48,1", "2039,2", "97862,3", "4085603,4"})
    void testPos2(int expectedPositionCount, int depth) {
        game = new Game("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R", true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "14,1", "191,2", "2812,3", "43238,4", "674624,5"})
    void testPos3(int expectedPositionCount, int depth) {
        game = new Game("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8", true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "6,1", "264,2", "9467,3", "422333,4"})
    void testPos4(int expectedPositionCount, int depth) {
        game = new Game("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1", true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "44,1", "1486,2", "62379,3", "2103487,4"})
    void testPos5(int expectedPositionCount, int depth) {
        game = new Game("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R", true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }

    @ParameterizedTest
    @CsvSource({"1,0", "46,1", "2079,2", "89890,3", "3894594,4"})
    void testPos6(int expectedPositionCount, int depth) {
        game = new Game("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1", true);
        assertEquals(expectedPositionCount, positionCounter(depth));
    }


    private int positionCounter(int depth) {
        if (depth == 0) {
            return 1;
        }

        List<Move> moves = game.generate();
        int numPositions = 0;
        Player activePlayer = game.getActivePlayer();

        for (Move move : moves) {
            MoveMaker.makeMove(move, activePlayer.isWhite(), game.getBoard());
            boolean updated = game.updateCastlingRights(move);
            game.setActivePlayer(game.getActivePlayer() == GameState.playerWhite ? GameState.playerBlack :
                    GameState.playerWhite);

            numPositions += positionCounter(depth - 1);

            MoveMaker.unmakeMove(move, activePlayer.isWhite(), game.getBoard());
            game.setActivePlayer(game.getActivePlayer() == GameState.playerWhite ? GameState.playerBlack :
                    GameState.playerWhite);
            if (updated) {
                game.unmakeCastlingRightUpdate(move);
            }
        }
        return numPositions;
    }
}
