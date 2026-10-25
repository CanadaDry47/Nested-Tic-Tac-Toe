package uttt.rules;

import uttt.core.Claimable;
import uttt.core.Player;

import java.util.List;
import java.util.Optional;

/** Three in a row, column or diagonal. Drawn / unresolved children have no winner and never match. */
public class StandardLinesWinStrategy implements WinStrategy {
    private static final int[][] LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},   // rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},   // columns
            {0, 4, 8}, {2, 4, 6}               // diagonals
    };

    @Override
    public Optional<Player> findWinner(List<? extends Claimable> cells) {
        if (cells.size() != 9) {
            throw new IllegalArgumentException("Expected 9 cells but got " + cells.size());
        }
        for (int[] line : LINES) {
            Optional<Player> first = cells.get(line[0]).getWinner();
            if (first.isPresent()
                    && first.equals(cells.get(line[1]).getWinner())
                    && first.equals(cells.get(line[2]).getWinner())) {
                return first;
            }
        }
        return Optional.empty();
    }
}
