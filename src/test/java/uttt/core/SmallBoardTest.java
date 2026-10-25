package uttt.core;

import org.junit.jupiter.api.Test;
import uttt.rules.StandardLinesWinStrategy;

import static org.junit.jupiter.api.Assertions.*;

class SmallBoardTest {
    private static Position p(int r, int c) { return new Position(r, c); }

    private SmallBoard newBoard() {
        return new SmallBoard(new StandardLinesWinStrategy());
    }

    @Test
    void threeInARowWinsTheBoard() {
        SmallBoard b = newBoard();
        b.place(p(0, 0), Player.X);
        b.place(p(1, 0), Player.O);
        b.place(p(0, 1), Player.X);
        b.place(p(1, 1), Player.O);
        assertEquals(BoardStatus.IN_PROGRESS, b.getStatus());
        b.place(p(0, 2), Player.X);
        assertEquals(BoardStatus.WON, b.getStatus());
        assertEquals(Player.X, b.getWinner().orElseThrow());
    }

    @Test
    void fullBoardWithNoLineIsADraw() {
        SmallBoard b = newBoard();
        // X O X / X O O / O X X  -> no winner
        Player[][] layout = {
                {Player.X, Player.O, Player.X},
                {Player.X, Player.O, Player.O},
                {Player.O, Player.X, Player.X}};
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                b.place(p(r, c), layout[r][c]);
        assertEquals(BoardStatus.DRAW, b.getStatus());
        assertTrue(b.getWinner().isEmpty());
    }

    @Test
    void cannotMarkAnOccupiedCell() {
        SmallBoard b = newBoard();
        b.place(p(1, 1), Player.X);
        assertThrows(IllegalStateException.class, () -> b.place(p(1, 1), Player.O));
    }

    @Test
    void cannotPlaceOnceBoardIsWon() {
        SmallBoard b = newBoard();
        b.place(p(0, 0), Player.X);
        b.place(p(0, 1), Player.X);
        b.place(p(0, 2), Player.X);
        assertThrows(IllegalStateException.class, () -> b.place(p(2, 2), Player.O));
    }

    @Test
    void positionRejectsOutOfRangeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Position(3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
    }
}
