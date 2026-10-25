package uttt.player;

import org.junit.jupiter.api.Test;
import uttt.core.BoardStatus;
import uttt.core.Move;
import uttt.core.Player;
import uttt.game.Game;
import uttt.game.GameFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomPlayerStrategyTest {

    @Test
    void randomPlayersAlwaysProduceLegalMovesAndGameTerminates() {
        for (long seed = 0; seed < 25; seed++) {
            Game game = GameFactory.standard().newGame();
            PlayerStrategy x = new RandomPlayerStrategy(new Random(seed));
            PlayerStrategy o = new RandomPlayerStrategy(new Random(seed + 100));
            int moves = 0;
            while (game.status() == BoardStatus.IN_PROGRESS) {
                PlayerStrategy who = game.currentPlayer() == Player.X ? x : o;
                Move move = who.chooseMove(game);
                assertTrue(game.legalMoves().contains(move));
                game.play(move);
                assertTrue(++moves <= 81, "a game can never exceed 81 moves");
            }
            assertNotEquals(BoardStatus.IN_PROGRESS, game.status());
        }
    }
}
