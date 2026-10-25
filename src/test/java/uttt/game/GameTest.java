package uttt.game;

import org.junit.jupiter.api.Test;
import uttt.core.*;
import uttt.rules.ClassicNextBoardRule;
import uttt.rules.FreeChoiceRule;
import uttt.rules.StandardLinesWinStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static Position p(int r, int c) { return new Position(r, c); }
    private static Move m(int br, int bc, int cr, int cc) { return new Move(p(br, bc), p(cr, cc)); }

    /** Test double that records every event it receives. */
    static class RecordingListener implements GameListener {
        final List<String> events = new ArrayList<>();
        @Override public void onMovePlayed(Move move, Player player) { events.add("move:" + player); }
        @Override public void onSmallBoardResolved(Position b, Optional<Player> w) { events.add("board:" + b + ":" + w.orElse(null)); }
        @Override public void onGameOver(Optional<Player> w) { events.add("over:" + w.orElse(null)); }
    }

    @Test
    void playersAlternateStartingWithX() {
        Game game = GameFactory.standard().newGame();
        assertEquals(Player.X, game.currentPlayer());
        game.play(m(1, 1, 0, 0));
        assertEquals(Player.O, game.currentPlayer());
    }

    @Test
    void classicRuleRestrictsNextMoveToTheMatchingBoard() {
        Game game = GameFactory.standard().newGame();
        game.play(m(1, 1, 2, 0));
        List<Move> legal = game.legalMoves();
        assertEquals(9, legal.size());
        assertTrue(legal.stream().allMatch(mv -> mv.board().equals(p(2, 0))));
        assertThrows(IllegalMoveException.class, () -> game.play(m(0, 0, 0, 0)));
    }

    @Test
    void occupiedCellIsRejected() {
        Game game = new GameFactory(new StandardLinesWinStrategy(), new FreeChoiceRule()).newGame();
        game.play(m(0, 0, 0, 0));
        assertThrows(IllegalMoveException.class, () -> game.play(m(0, 0, 0, 0)));
    }

    @Test
    void listenersAreToldWhenASmallBoardIsWon() {
        RecordingListener rec = new RecordingListener();
        Game game = new GameFactory(new StandardLinesWinStrategy(), new FreeChoiceRule()).newGame(rec);
        game.play(m(0, 0, 0, 0)); // X
        game.play(m(1, 1, 0, 0)); // O
        game.play(m(0, 0, 0, 1)); // X
        game.play(m(1, 1, 0, 1)); // O
        game.play(m(0, 0, 0, 2)); // X wins board (0,0)
        assertTrue(rec.events.contains("board:Position[row=0, col=0]:X"));
        assertEquals(5, rec.events.stream().filter(e -> e.startsWith("move:")).count());
    }

    @Test
    void gameEndsWhenThreeBoardsInALineAreWon_andNoMoreMovesAllowed() {
        // Dependency injection: hand the Game a pre-built, nearly finished board.
        GameFactory factory = new GameFactory(new StandardLinesWinStrategy(), new FreeChoiceRule());
        MetaBoard meta = factory.createMetaBoard();
        for (int c = 0; c < 3; c++) {
            meta.place(new Move(p(0, 0), p(0, c)), Player.X);
            meta.place(new Move(p(0, 1), p(0, c)), Player.X);
        }
        meta.place(new Move(p(0, 2), p(0, 0)), Player.X);
        meta.place(new Move(p(0, 2), p(0, 1)), Player.X);

        RecordingListener rec = new RecordingListener();
        Game game = new Game(meta, new FreeChoiceRule(), Player.X, List.of(rec));
        game.play(m(0, 2, 0, 2));

        assertEquals(BoardStatus.WON, game.status());
        assertEquals(Player.X, game.winner().orElseThrow());
        assertTrue(rec.events.contains("over:X"));
        assertTrue(game.legalMoves().isEmpty());
        assertThrows(IllegalMoveException.class, () -> game.play(m(2, 2, 2, 2)));
    }
}
