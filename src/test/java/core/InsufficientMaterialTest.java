package core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsufficientMaterialTest {

    private Game game;

    @ParameterizedTest
    @CsvSource({"8/8/3k4/8/8/3K4/8/8,true", // King vs King
            "8/8/3k4/8/3P4/3K4/8/8,false", // King & Pawn vs King
            "8/8/3k4/8/2N5/3K4/8/8,true", // King & Knight vs King
            "8/6n1/3k4/8/8/N4K2/8/8, true", // King & Knight vs King & Knight
            "8/1K6/8/B7/4N3/8/8/k7,false", // King & Bishop vs Knight & King
            "8/8/8/4K3/8/2N5/b7/k7, true" // King & Night vs King & Bishop
    })
    void testMaterial(String fen, boolean insufficientMaterial) {
        game = new Game(fen, true);
        assertEquals(insufficientMaterial, game.isInsufficientMaterial());
    }

}
