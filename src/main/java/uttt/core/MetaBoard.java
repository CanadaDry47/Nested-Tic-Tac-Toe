package uttt.core;

import uttt.rules.WinStrategy;

import java.util.List;

/**
 * The big board. Each of its "cells" is a whole SmallBoard. When a small board is won, it
 * automatically counts as that player's tile here, because SmallBoard.getWinner() is exactly
 * what the WinStrategy reads (Composite pattern in action).
 */
public class MetaBoard extends AbstractGridBoard<SmallBoard> {

    public MetaBoard(List<SmallBoard> smallBoards, WinStrategy winStrategy) {
        super(smallBoards, winStrategy);
    }

    public void place(Move move, Player player) {
        get(move.board()).place(move.cell(), player);
        refresh();
    }
}
