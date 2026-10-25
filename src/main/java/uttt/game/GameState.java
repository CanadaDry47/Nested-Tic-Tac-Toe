package uttt.game;

import uttt.core.BoardStatus;
import uttt.core.Move;
import uttt.core.Player;

import java.util.List;
import java.util.Optional;

/**
 * A read-only view of the game. Player strategies and UIs depend on this abstraction
 * instead of the concrete Game, so they can't accidentally mutate it.
 */
public interface GameState {
    Player currentPlayer();

    BoardStatus status();

    Optional<Player> winner();

    List<Move> legalMoves();

    Optional<Move> lastMove();
}
