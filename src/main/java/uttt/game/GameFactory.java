package uttt.game;

import uttt.core.MetaBoard;
import uttt.core.Player;
import uttt.core.Position;
import uttt.core.SmallBoard;
import uttt.rules.ClassicNextBoardRule;
import uttt.rules.NextBoardRule;
import uttt.rules.StandardLinesWinStrategy;
import uttt.rules.WinStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * FACTORY PATTERN. Hides the wiring of nine SmallBoards -> MetaBoard -> Game, and injects the
 * chosen strategies consistently into every board it builds.
 */
public class GameFactory {
    private final WinStrategy winStrategy;
    private final NextBoardRule nextBoardRule;

    public GameFactory(WinStrategy winStrategy, NextBoardRule nextBoardRule) {
        this.winStrategy = winStrategy;
        this.nextBoardRule = nextBoardRule;
    }

    public static GameFactory standard() {
        return new GameFactory(new StandardLinesWinStrategy(), new ClassicNextBoardRule());
    }

    public SmallBoard createSmallBoard() {
        return new SmallBoard(winStrategy);
    }

    public MetaBoard createMetaBoard() {
        List<SmallBoard> boards = new ArrayList<>();
        for (int i = 0; i < Position.SIZE * Position.SIZE; i++) {
            boards.add(createSmallBoard());
        }
        return new MetaBoard(boards, winStrategy);
    }

    public Game newGame(GameListener... listeners) {
        return new Game(createMetaBoard(), nextBoardRule, Player.X, List.of(listeners));
    }
}
