package uttt.core;

/**
 * A move is addressed by which small board to play in, and which cell inside it.
 * The mover is tracked by the Game, not the move, so a Move is pure data.
 */
public record Move(Position board, Position cell) {
    public Move {
        if (board == null || cell == null) {
            throw new IllegalArgumentException("board and cell must not be null");
        }
    }
}
