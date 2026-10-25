package uttt.core;

/** The two participants. Also used as the "mark" placed on a cell. */
public enum Player {
    X, O;

    public Player opponent() {
        return this == X ? O : X;
    }
}
