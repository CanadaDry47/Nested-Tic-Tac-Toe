package uttt.app;

import uttt.core.Move;
import uttt.core.Player;
import uttt.core.Position;
import uttt.game.GameListener;

import java.util.Optional;

/** OBSERVER PATTERN (concrete observer): narrates game events to stdout. */
public class ConsoleGameListener implements GameListener {
    @Override
    public void onMovePlayed(Move move, Player player) {
        System.out.printf("%s plays board (%d,%d) cell (%d,%d)%n", player,
                move.board().row(), move.board().col(), move.cell().row(), move.cell().col());
    }

    @Override
    public void onSmallBoardResolved(Position boardPosition, Optional<Player> winner) {
        System.out.printf("  -> board (%d,%d) %s%n", boardPosition.row(), boardPosition.col(),
                winner.map(w -> "won by " + w).orElse("drawn"));
    }

    @Override
    public void onGameOver(Optional<Player> winner) {
        System.out.println(winner.map(w -> "GAME OVER: " + w + " wins!").orElse("GAME OVER: draw"));
    }
}
