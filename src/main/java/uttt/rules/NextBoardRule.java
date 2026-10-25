package uttt.rules;

import uttt.core.MetaBoard;
import uttt.core.Move;
import uttt.core.Position;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * STRATEGY PATTERN #2. Decides which small boards the next player may play in.
 * This is the signature rule of the game, and the most likely thing to be varied.
 */
public interface NextBoardRule {
    Set<Position> allowedBoards(MetaBoard meta, Optional<Move> lastMove);

    /** Shared helper: every small board that is still in progress. */
    static Set<Position> anyOpenBoard(MetaBoard meta) {
        Set<Position> open = new LinkedHashSet<>();
        for (Position p : Position.all()) {
            if (!meta.get(p).isResolved()) {
                open.add(p);
            }
        }
        return open;
    }
}
