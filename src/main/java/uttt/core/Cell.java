package uttt.core;

import java.util.Objects;
import java.util.Optional;

/** COMPOSITE PATTERN (leaf). A single square; "won" simply means "has a mark". */
public class Cell implements Claimable {
    private Player mark;

    public boolean isEmpty() {
        return mark == null;
    }

    public void mark(Player player) {
        Objects.requireNonNull(player, "player");
        if (mark != null) {
            throw new IllegalStateException("Cell already marked by " + mark);
        }
        mark = player;
    }

    @Override
    public BoardStatus getStatus() {
        return mark == null ? BoardStatus.IN_PROGRESS : BoardStatus.WON;
    }

    @Override
    public Optional<Player> getWinner() {
        return Optional.ofNullable(mark);
    }
}
