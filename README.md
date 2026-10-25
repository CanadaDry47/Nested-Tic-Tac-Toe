# Ultimate Tic-Tac-Toe: Java Framework

A 3x3 grid of tic-tac-toe boards. Win a small board and that tile belongs to you on the big board; win three big tiles in a line to win the game. The cell you play in decides which small board your opponent must play in next.

Run tests: `mvn test` (JUnit 5). Run the demo: `mvn compile exec:java -Dexec.mainClass=uttt.app.Main` or run `uttt.app.Main` from your IDE.

## Architecture

```
uttt.core    Player, Position, Move, BoardStatus, Claimable, Cell, AbstractGridBoard, SmallBoard, MetaBoard
uttt.rules   WinStrategy, StandardLinesWinStrategy, NextBoardRule, ClassicNextBoardRule, FreeChoiceRule
uttt.game    Game, GameState, GameListener, GameFactory, IllegalMoveException
uttt.player  PlayerStrategy, RandomPlayerStrategy
uttt.app     Main (composition root), BoardRenderer, ConsoleGameListener
```

## Design patterns (4)

### 1. Composite: `Claimable` / `Cell` / `AbstractGridBoard` / `SmallBoard` / `MetaBoard`
**Where:** `Claimable` is the component interface. `Cell` is the leaf. `SmallBoard` (a grid of Cells) and `MetaBoard` (a grid of SmallBoards) are composites that extend `AbstractGridBoard<T extends Claimable>`.
**How:** the game is naturally recursive: a small board is to cells what the big board is to small boards. Because every level exposes `getStatus()` / `getWinner()`, "winning a small board places a tile on the big board" needs no special code: the big board just asks each child who owns it. Win/draw logic is written once in `AbstractGridBoard.refresh()`.

### 2. Strategy: `WinStrategy`, `NextBoardRule`, `PlayerStrategy`
**Where / how:** three independent points of variation are pulled behind interfaces.
- `WinStrategy`: how a winner is found among nine Claimables (`StandardLinesWinStrategy` = 8 lines). Used identically by cells-in-a-board and boards-in-the-metaboard.
- `NextBoardRule`: which small boards the next player may use (`ClassicNextBoardRule` is the official "send them to the matching board" rule; `FreeChoiceRule` is an easy variant).
- `PlayerStrategy`: how a participant picks a move (`RandomPlayerStrategy` now; a human console/GUI or minimax AI later).

### 3. Observer: `GameListener` / `Game` / `ConsoleGameListener`
**Where / how:** `Game` is the subject and fires `onMovePlayed`, `onSmallBoardResolved` and `onGameOver`. UIs, loggers, sound effects or a network layer subscribe without the game knowing they exist. The listener interface uses default no-op methods, so implementers override only what they need. Tests use a `RecordingListener` to assert on events.

### 4. Factory: `GameFactory`
**Where / how:** builds the nine `SmallBoard`s, the `MetaBoard` and the `Game`, injecting the same `WinStrategy` into every board and the chosen `NextBoardRule` into the game. `GameFactory.standard()` gives the official game; `new GameFactory(myWin, myRule)` gives a variant.

## OO principles

- **Coding to abstractions:** `Game` depends on `NextBoardRule`, `MetaBoard` depends on `WinStrategy`, strategies take `Claimable` and `GameState`, never concrete classes. `GameState` is a read-only view so AIs can't mutate the game.
- **Polymorphism:**
  - `StandardLinesWinStrategy.findWinner(List<? extends Claimable>)` is called with `Cell`s and with `SmallBoard`s through the same code path (tested in `WinStrategyTest.drawnSmallBoardsNeverCountTowardsALine`).
  - `NextBoardRule` and `PlayerStrategy` implementations are swapped at runtime (`ruleImplementationsAreInterchangeable`).
- **Dependency injection (constructor-based):** `Game(board, rule, firstPlayer, listeners)`, `MetaBoard(boards, winStrategy)`, `RandomPlayerStrategy(Random)`, `GameFactory(winStrategy, rule)`. `Main` is the only composition root. A test hands `Game` a pre-built, nearly-won board to test game-over cheaply.
- **Encapsulation / SRP:** `Position` validates itself; `Cell` refuses double-marking; `SmallBoard` refuses play after it is resolved; rendering lives in `BoardRenderer`, not the model.

## Tests (21 across 6 classes)

| Class | What it proves |
|---|---|
| `SmallBoardTest` | win, draw, occupied cell rejected, no play after win, bad coordinates rejected |
| `MetaBoardTest` | small-board win claims a big tile; 3 in a line wins; mixed owners don't |
| `WinStrategyTest` | diagonal detection; drawn boards never count; wrong size rejected |
| `NextBoardRuleTest` | first move free; last cell targets the board; resolved target gives free choice; rules are interchangeable |
| `GameTest` | turn alternation; classic restriction enforced; occupied cell rejected; observer events; game-over via injected board |
| `RandomPlayerStrategyTest` | 25 seeded random games always make legal moves and terminate in 81 moves or fewer |

## Extension ideas
`HumanConsoleStrategy`, a minimax `PlayerStrategy`, an undo stack (Command/Memento), a Swing/JavaFX `GameListener`, an "early draw" `WinStrategy`.
