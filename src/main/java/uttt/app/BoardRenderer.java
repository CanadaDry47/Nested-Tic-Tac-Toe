package uttt.app;

import uttt.core.Cell;
import uttt.core.MetaBoard;
import uttt.core.Position;
import uttt.core.SmallBoard;

/** Plain-text rendering of the 9x9 board. Kept separate from the model (single responsibility). */
public class BoardRenderer {

    public String render(MetaBoard meta) {
        StringBuilder sb = new StringBuilder();
        for (int bigRow = 0; bigRow < Position.SIZE; bigRow++) {
            for (int smallRow = 0; smallRow < Position.SIZE; smallRow++) {
                for (int bigCol = 0; bigCol < Position.SIZE; bigCol++) {
                    SmallBoard small = meta.get(new Position(bigRow, bigCol));
                    for (int smallCol = 0; smallCol < Position.SIZE; smallCol++) {
                        Cell cell = small.get(new Position(smallRow, smallCol));
                        sb.append(cell.getWinner().map(Enum::name).orElse("."));
                        if (smallCol < Position.SIZE - 1) sb.append(' ');
                    }
                    if (bigCol < Position.SIZE - 1) sb.append(" | ");
                }
                sb.append('\n');
            }
            if (bigRow < Position.SIZE - 1) sb.append("------+-------+------\n");
        }
        return sb.toString();
    }
}
