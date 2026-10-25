package uttt.core;

import uttt.rules.WinStrategy;

import java.util.ArrayList;
import java.util.List;

/** One of the nine inner tic-tac-toe boards. */
public class SmallBoard extends AbstractGridBoard<Cell> {

    public SmallBoard(WinStrategy winStrategy) {
        super(createCells(), winStrategy);
    }

    private static List<Cell> createCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < Position.SIZE * Position.SIZE; i++) {
            cells.add(new Cell());
        }
        return cells;
    }

    public boolean isCellEmpty(Position position) {
        return get(position).isEmpty();
    }

    public void place(Position position, Player player) {
        if (isResolved()) {
            throw new IllegalStateException("Small board is already " + getStatus());
        }
        get(position).mark(player);
        refresh();
    }
}
