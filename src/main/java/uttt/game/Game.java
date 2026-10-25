package uttt.game;

import uttt.core.BoardStatus;
import uttt.core.MetaBoard;
import uttt.core.Move;
import uttt.core.Player;
import uttt.core.Position;
import uttt.core.SmallBoard;
import uttt.rules.NextBoardRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Orchestrates turns. OBSERVER PATTERN (subject): notifies registered GameListeners.
 * DEPENDENCY INJECTION: the board, the rule and the listeners are all handed in, so tests
 * can supply a pre-built board, a different rule, or a recording listener.
 */
public class Game implements GameState {
    private final MetaBoard board;
    private final NextBoardRule nextBoardRule;
    private final List<GameListener> listeners = new CopyOnWriteArrayList<>();
    private Player current;
    private Move lastMove;

    public Game(MetaBoard board, NextBoardRule nextBoardRule, Player firstPlayer, List<GameListener> listeners) {
        this.board = board;
        this.nextBoardRule = nextBoardRule;
        this.current = firstPlayer;
        this.listeners.addAll(listeners);
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    public MetaBoard getBoard() {
        return board;
    }

    @Override
    public Player currentPlayer() {
        return current;
    }

    @Override
    public BoardStatus status() {
        return board.getStatus();
    }

    @Override
    public Optional<Player> winner() {
        return board.getWinner();
    }

    @Override
    public Optional<Move> lastMove() {
        return Optional.ofNullable(lastMove);
    }

    @Override
    public List<Move> legalMoves() {
        List<Move> moves = new ArrayList<>();
        if (board.isResolved()) {
            return moves;
        }
        for (Position boardPos : nextBoardRule.allowedBoards(board, lastMove())) {
            SmallBoard small = board.get(boardPos);
            for (Position cellPos : Position.all()) {
                if (small.isCellEmpty(cellPos)) {
                    moves.add(new Move(boardPos, cellPos));
                }
            }
        }
        return moves;
    }

    public void play(Move move) {
        if (board.isResolved()) {
            throw new IllegalMoveException("Game is already over");
        }
        Set<Position> allowed = nextBoardRule.allowedBoards(board, lastMove());
        if (!allowed.contains(move.board())) {
            throw new IllegalMoveException("Must play in one of " + allowed + " but chose " + move.board());
        }
        SmallBoard small = board.get(move.board());
        if (!small.isCellEmpty(move.cell())) {
            throw new IllegalMoveException("Cell " + move.cell() + " is already taken");
        }

        Player mover = current;
        boolean wasResolved = small.isResolved();

        board.place(move, mover);
        lastMove = move;
        if (!board.isResolved()) {
            current = mover.opponent();
        }

        listeners.forEach(l -> l.onMovePlayed(move, mover));
        if (!wasResolved && small.isResolved()) {
            listeners.forEach(l -> l.onSmallBoardResolved(move.board(), small.getWinner()));
        }
        if (board.isResolved()) {
            listeners.forEach(l -> l.onGameOver(board.getWinner()));
        }
    }
}
