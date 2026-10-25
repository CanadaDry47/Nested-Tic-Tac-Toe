package uttt.rules;

import uttt.core.Claimable;
import uttt.core.Player;

import java.util.List;
import java.util.Optional;

/**
 * STRATEGY PATTERN #1. How a winner is determined from nine row-major Claimables.
 * Swap in a different implementation (e.g. a 4-in-a-row variant) without touching the boards.
 */
public interface WinStrategy {
    Optional<Player> findWinner(List<? extends Claimable> cells);
}
