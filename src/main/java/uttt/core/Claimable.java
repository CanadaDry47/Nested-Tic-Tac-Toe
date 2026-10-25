package uttt.core;

import java.util.Optional;

/**
 * COMPOSITE PATTERN (component interface).
 * Anything that can end up "owned" by a player: a single Cell, a SmallBoard, or the MetaBoard.
 * The win-detection logic only ever sees this abstraction, so the same code decides who
 * owns a row of cells AND a row of small boards.
 */
public interface Claimable {
    BoardStatus getStatus();

    /** Present only when getStatus() == WON. */
    Optional<Player> getWinner();

    default boolean isResolved() {
        return getStatus() != BoardStatus.IN_PROGRESS;
    }
}
