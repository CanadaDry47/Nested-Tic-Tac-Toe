package uttt.rules;

import uttt.core.MetaBoard;
import uttt.core.Move;
import uttt.core.Position;

import java.util.Optional;
import java.util.Set;

/**
 * The official rule: the cell you just played picks the board your opponent must play in.
 * If that board is already won or full, the opponent may play in any open board.
 */
public class ClassicNextBoardRule implements NextBoardRule {
    @Override
    public Set<Position> allowedBoards(MetaBoard meta, Optional<Move> lastMove) {
        if (lastMove.isPresent()) {
            Position target = lastMove.get().cell();
            if (!meta.get(target).isResolved()) {
                return Set.of(target);
            }
        }
        return NextBoardRule.anyOpenBoard(meta);
    }
}
