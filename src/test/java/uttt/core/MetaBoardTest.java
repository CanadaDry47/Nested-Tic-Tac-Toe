package uttt.core;

import org.junit.jupiter.api.Test;
import uttt.game.GameFactory;

import static org.junit.jupiter.api.Assertions.*;

class MetaBoardTest {
    private static Position p(int r, int c) { return new Position(r, c); }

    /** Win the entire top row of one small board for the given player. */
    static void winSmallBoard(MetaBoard meta, Position boardPos, Player player) {
        for (int c = 0; c < 3; c++) {
            meta.place(new Move(boardPos, p(0, c)), player);
        }
    }

    @Test
    void winningASmallBoardClaimsItForThatPlayerOnTheBigBoard() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        winSmallBoard(meta, p(1, 1), Player.O);
        assertEquals(BoardStatus.WON, meta.get(p(1, 1)).getStatus());
        assertEquals(Player.O, meta.get(p(1, 1)).getWinner().orElseThrow());
        assertEquals(BoardStatus.IN_PROGRESS, meta.getStatus());
    }

    @Test
    void winningThreeSmallBoardsInALineWinsTheGame() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        winSmallBoard(meta, p(0, 0), Player.X);
        winSmallBoard(meta, p(1, 1), Player.X);
        assertEquals(BoardStatus.IN_PROGRESS, meta.getStatus());
        winSmallBoard(meta, p(2, 2), Player.X);
        assertEquals(BoardStatus.WON, meta.getStatus());
        assertEquals(Player.X, meta.getWinner().orElseThrow());
    }

    @Test
    void mixedOwnersDoNotWin() {
        MetaBoard meta = GameFactory.standard().createMetaBoard();
        winSmallBoard(meta, p(0, 0), Player.X);
        winSmallBoard(meta, p(0, 1), Player.O);
        winSmallBoard(meta, p(0, 2), Player.X);
        assertEquals(BoardStatus.IN_PROGRESS, meta.getStatus());
    }
}
