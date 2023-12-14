package util;

import core.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenStringReaderTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testReadRandomFen() {
        String fen = "b1n3q1/p7/R5p1/p7/5K2/P1Nk4/1p3pPP/B7";
        FenStringReader.read(fen, board);

        assertTrue(board.getSquares()[0][0].isOccupied());
        assertFalse(board.getPieceFromSquare(0, 0).isWhite());
        assertEquals("b", board.getPieceFromSquare(0, 0).getDisplay());

        assertTrue(board.getSquares()[0][2].isOccupied());
        assertFalse(board.getPieceFromSquare(0, 2).isWhite());
        assertEquals("n", board.getPieceFromSquare(0, 2).getDisplay());

        assertTrue(board.getSquares()[0][6].isOccupied());
        assertFalse(board.getPieceFromSquare(0, 6).isWhite());
        assertEquals("q", board.getPieceFromSquare(0, 6).getDisplay());

        assertFalse(board.getSquares()[0][7].isOccupied());

        assertTrue(board.getSquares()[2][0].isOccupied());
        assertTrue(board.getPieceFromSquare(2, 0).isWhite());
        assertEquals("R", board.getPieceFromSquare(2, 0).getDisplay());
    }


}
