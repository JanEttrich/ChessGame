package frontend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveResource {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
}
