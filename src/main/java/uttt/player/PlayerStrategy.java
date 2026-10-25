package uttt.player;

import uttt.core.Move;
import uttt.game.GameState;

/**
 * STRATEGY PATTERN #3. How a participant picks a move. Humans (console/GUI) and AIs all
 * implement this one method, so the game loop never cares who is playing (polymorphism).
 */
public interface PlayerStrategy {
    Move chooseMove(GameState state);
}
