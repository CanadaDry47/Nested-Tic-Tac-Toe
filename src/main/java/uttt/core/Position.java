package uttt.core;

import java.util.ArrayList;
import java.util.List;

/** An immutable (row, col) coordinate on a 3x3 grid. Used for both the big and small boards. */
public record Position(int row, int col) {
    public static final int SIZE = 3;

    public Position {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Position out of range: (" + row + "," + col + ")");
        }
    }

    /** Row-major index 0..8. */
    public int index() {
        return row * SIZE + col;
    }

    public static Position fromIndex(int index) {
        return new Position(index / SIZE, index % SIZE);
    }

    public static List<Position> all() {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            positions.add(fromIndex(i));
        }
        return positions;
    }
}
