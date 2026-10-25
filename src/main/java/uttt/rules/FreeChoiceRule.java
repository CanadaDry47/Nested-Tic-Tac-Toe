package uttt.rules;

import uttt.core.MetaBoard;
import uttt.core.Move;
import uttt.core.Position;

import java.util.Optional;
import java.util.Set;

/** Easy-mode variant: always free to play in any open board. */
public class FreeChoiceRule implements NextBoardRule {
    @Override
    public Set<Position> allowedBoards(MetaBoard meta, Optional<Move> lastMove) {
        return NextBoardRule.anyOpenBoard(meta);
    }
}
