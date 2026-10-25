package uttt.rules;

import org.junit.jupiter.api.Test;
import uttt.core.*;
import uttt.game.GameFactory;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NextBoardRuleTest {
    private static Position p(int r, int c) { return new Position(r, c); }

    @Test
    void classicRuleAllowsAnyBoardOnFirstMove() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        assertEquals(9, new ClassicNextBoardRule().allowedBoards(meta, Optional.empty()).size());
    }

    @Test
    void classicRuleSendsOpponentToBoardMatchingLastCell() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        Optional<Move> last = Optional.of(new Move(p(0, 0), p(2, 1)));
        assertEquals(Set.of(p(2, 1)), new ClassicNextBoardRule().allowedBoards(meta, last));
    }

    @Test
    void classicRuleGivesFreeChoiceWhenTargetBoardIsResolved() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        for (int c = 0; c < 3; c++) meta.place(new Move(p(0, 0), p(0, c)), Player.X); // board (0,0) won
        Optional<Move> last = Optional.of(new Move(p(2, 2), p(0, 0)));                // points at it
        Set<Position> allowed = new ClassicNextBoardRule().allowedBoards(meta, last);
        assertEquals(8, allowed.size());
        assertFalse(allowed.contains(p(0, 0)));
    }

    @Test
    void ruleImplementationsAreInterchangeable() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        Optional<Move> last = Optional.of(new Move(p(0, 0), p(2, 1)));
        NextBoardRule classic = new ClassicNextBoardRule();
        NextBoardRule free = new FreeChoiceRule();
        assertEquals(1, classic.allowedBoards(meta, last).size());
        assertEquals(9, free.allowedBoards(meta, last).size());
    }
}
