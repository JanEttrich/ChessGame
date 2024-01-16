package util;

import core.Board;
import core.Pieces;
import org.junit.jupiter.api.Test;

import static core.Board.squares;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FenStringReaderTest {
    @Test
    void testReadRandomFen() {
        Board.resetBoard();
        String fen = "b1n3q1/p7/R5p1/p7/5K2/P1Nk4/1p3pPP/B7";
        FenStringReader.read(fen);

        assertTrue(squares[56] != Pieces.NONE);
        assertEquals(Pieces.BISHOP | Pieces.BLACK, squares[56]);

        assertTrue(squares[58] != Pieces.NONE);
        assertEquals(Pieces.KNIGHT | Pieces.BLACK, squares[58]);

        assertTrue(squares[62] != Pieces.NONE);
        assertEquals(Pieces.QUEEN | Pieces.BLACK, squares[62]);

        assertEquals(Pieces.NONE, squares[63]);

        assertTrue(squares[40] != Pieces.NONE);
        assertEquals(Pieces.ROOK | Pieces.WHITE, squares[40]);
    }


}
