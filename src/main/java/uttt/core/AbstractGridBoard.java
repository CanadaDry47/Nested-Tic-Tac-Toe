package uttt.core;

import uttt.rules.WinStrategy;

import java.util.List;
import java.util.Optional;

/**
 * COMPOSITE PATTERN (composite base class) + dependency injection of a WinStrategy.
 *
 * A 3x3 grid of Claimable children. SmallBoard is a grid of Cells; MetaBoard is a grid of
 * SmallBoards. The status/winner calculation is written once here and works for both because
 * it depends only on the Claimable abstraction (generics + coding to an interface).
 */
public abstract class AbstractGridBoard<T extends Claimable> implements Claimable {
    private static final int CELL_COUNT = Position.SIZE * Position.SIZE;

    private final List<T> children;
    private final WinStrategy winStrategy;
    private BoardStatus status = BoardStatus.IN_PROGRESS;
    private Player winner;

    protected AbstractGridBoard(List<T> children, WinStrategy winStrategy) {
        if (children.size() != CELL_COUNT) {
            throw new IllegalArgumentException("A grid needs exactly " + CELL_COUNT + " children");
        }
        this.children = List.copyOf(children);
        this.winStrategy = winStrategy;
    }

    public T get(Position position) {
        return children.get(position.index());
    }

    public List<T> getChildren() {
        return children;
    }

    /** Subclasses call this after mutating a child so this board's own status stays current. */
    protected final void refresh() {
        Optional<Player> found = winStrategy.findWinner(children);
        if (found.isPresent()) {
            status = BoardStatus.WON;
            winner = found.get();
        } else if (children.stream().allMatch(Claimable::isResolved)) {
            status = BoardStatus.DRAW;
        } else {
            status = BoardStatus.IN_PROGRESS;
        }
    }

    @Override
    public BoardStatus getStatus() {
        return status;
    }

    @Override
    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
