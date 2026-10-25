package uttt.app;

import uttt.game.Game;
import uttt.game.GameFactory;
import uttt.player.PlayerStrategy;
import uttt.player.RandomPlayerStrategy;
import uttt.core.BoardStatus;
import uttt.core.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/** Composition root: wires everything together. Demo = two seeded random AIs. */
public class Main {
    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : 42L;
        Game game = GameFactory.standard().newGame(new ConsoleGameListener());

        Map<Player, PlayerStrategy> players = new EnumMap<>(Player.class);
        players.put(Player.X, new RandomPlayerStrategy(new Random(seed)));
        players.put(Player.O, new RandomPlayerStrategy(new Random(seed + 1)));

        BoardRenderer renderer = new BoardRenderer();
        while (game.status() == BoardStatus.IN_PROGRESS) {
            game.play(players.get(game.currentPlayer()).chooseMove(game));
        }
        System.out.println(renderer.render(game.getBoard()));
    }
}
