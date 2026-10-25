package uttt.rules;

import org.junit.jupiter.api.Test;
import uttt.core.*;
import uttt.game.GameFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WinStrategyTest {
    private static Position p(int r, int c) { return new Position(r, c); }

    @Test
    void detectsDiagonalWin() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 9; i++) cells.add(new Cell());
        cells.get(2).mark(Player.O);
        cells.get(4).mark(Player.O);
        cells.get(6).mark(Player.O);
        assertEquals(Player.O, new StandardLinesWinStrategy().findWinner(cells).orElseThrow());
    }

    @Test
    void drawnSmallBoardsNeverCountTowardsALine() {
        // Polymorphism: same strategy, but the children are SmallBoards instead of Cells.
        GameFactory factory = GameFactory.standard();
        SmallBoard drawn = factory.createSmallBoard();
        Player[][] layout = {
                {Player.X, Player.O, Player.X},
                {Player.X, Player.O, Player.O},
                {Player.O, Player.X, Player.X}};
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) drawn.place(p(r, c), layout[r][c]);

        List<SmallBoard> boards = List.of(drawn, drawn, drawn,
                factory.createSmallBoard(), factory.createSmallBoard(), factory.createSmallBoard(),
                factory.createSmallBoard(), factory.createSmallBoard(), factory.createSmallBoard());
        assertTrue(new StandardLinesWinStrategy().findWinner(boards).isEmpty());
    }

    @Test
    void rejectsWrongNumberOfCells() {
        assertThrows(IllegalArgumentException.class,
                () -> new StandardLinesWinStrategy().findWinner(List.of(new Cell())));
    }
}
