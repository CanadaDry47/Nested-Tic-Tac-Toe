package uttt.player;

import uttt.core.Move;
import uttt.game.GameState;

import java.util.List;
import java.util.Random;

/** Simple AI. The Random is injected so tests can seed it for repeatable games. */
public class RandomPlayerStrategy implements PlayerStrategy {
    private final Random random;

    public RandomPlayerStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Move chooseMove(GameState state) {
        List<Move> moves = state.legalMoves();
        if (moves.isEmpty()) {
            throw new IllegalStateException("No legal moves available");
        }
        return moves.get(random.nextInt(moves.size()));
    }
}
