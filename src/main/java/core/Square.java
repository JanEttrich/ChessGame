package core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Square {
    private boolean occupied;
    private Piece piece;

    public Square(boolean occupied) {
        this.occupied = occupied;
    }
}
