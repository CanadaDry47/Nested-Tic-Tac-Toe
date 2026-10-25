package uttt.game;

import uttt.core.Move;
import uttt.core.Player;
import uttt.core.Position;

import java.util.Optional;

/**
 * OBSERVER PATTERN (observer interface). Default no-op methods let a listener
 * override only the events it cares about.
 */
public interface GameListener {
    default void onMovePlayed(Move move, Player player) { }

    /** Fired when a small board becomes won or drawn. */
    default void onSmallBoardResolved(Position boardPosition, Optional<Player> winner) { }

    /** Empty winner means the whole game was drawn. */
    default void onGameOver(Optional<Player> winner) { }
}
