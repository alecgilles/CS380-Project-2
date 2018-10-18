package puzzle.solver.models;

import java.util.Observable;
import java.util.Observer;

/**
 * Represents an nxn puzzle game with up, down, left, and right operations.
 *
 * @author Alec Gilles
 */
public class Puzzle extends Observable {

    // The board state that is considered solved.
    private final static long SUCCESS_STATE_HASH = getStateHash(new int[][]{
        {1, 2, 3},
        {8, 0, 4},
        {7, 6, 5}
    });

    // When locked is true, the board cannot be modified. Intended to be used to
    // block user interaction during solution demo.
    private boolean locked = false;
    // Current physical state of the board.
    private int[][] state;
    // Edge size n of nxn puzzle.
    int size;
    // Current position of the blank tile in the puzzle.
    private int posCol, posRow;

    private Puzzle() {
    }

    /**
     * Creates a new puzzle with the specified initial state.
     *
     * @param state Must be a non-empty square 2D array.
     */
    public Puzzle(int[][] state) {
        setState(state);
    }

    public boolean canMoveLeft() {
        return posCol > 0 && !locked;
    }

    public boolean canMoveRight() {
        return posCol < size - 1 && !locked;
    }

    public boolean canMoveUp() {
        return posRow > 0 && !locked;
    }

    public boolean canMoveDown() {
        return posRow < size - 1 && !locked;
    }

    /**
     * Moves the empty tile left if possible.
     *
     * @return True if empty tile was moved.
     */
    public boolean moveLeft() {
        if (canMoveLeft()) {
            state[posRow][posCol] = state[posRow][posCol - 1];
            posCol -= 1;
            state[posRow][posCol] = 0;

            setChanged();
            notifyObservers(state);

            return true;
        }

        return false;
    }

    /**
     * Moves the empty tile right if possible.
     *
     * @return True if empty tile was moved.
     */
    public boolean moveRight() {
        if (canMoveRight()) {
            state[posRow][posCol] = state[posRow][posCol + 1];
            posCol += 1;
            state[posRow][posCol] = 0;

            setChanged();
            notifyObservers(state);

            return true;
        }

        return false;
    }

    /**
     * Moves the empty tile up if possible.
     *
     * @return True if empty tile was moved.
     */
    public boolean moveUp() {
        if (canMoveUp()) {
            state[posRow][posCol] = state[posRow - 1][posCol];
            posRow -= 1;
            state[posRow][posCol] = 0;

            setChanged();
            notifyObservers(state);

            return true;
        }

        return false;
    }

    /**
     * Moves the empty tile down if possible.
     *
     * @return True if empty tile was moved.
     */
    public boolean moveDown() {
        if (canMoveDown()) {
            state[posRow][posCol] = state[posRow + 1][posCol];
            posRow += 1;
            state[posRow][posCol] = 0;

            setChanged();
            notifyObservers(state);

            return true;
        }

        return false;
    }

    /**
     * Sets the state of the puzzle to match the specified array.
     *
     * @param state A non-empty square 2D integer array.
     */
    public final void setState(int[][] state) {
        if (state != null && state.length > 0) {
            size = state.length;
            this.state = new int[state.length][];
            for (int i = 0; i < state.length; i++) {
                if (state[i] == null || state.length != state[i].length) {
                    throw new IllegalArgumentException("State must be a non-empty square 2D integer array");
                }
                this.state[i] = new int[state[i].length];
                for (int j = 0; j < state[i].length; j++) {
                    this.state[i][j] = state[i][j];
                    if (state[i][j] == 0) {
                        posRow = i;
                        posCol = j;
                    }
                }
            }

            setChanged();
            notifyObservers(this.state);
        } else {
            throw new IllegalArgumentException("State must be a non-empty 2D integer array");
        }
    }

    /**
     * Get the current physical state of the board.
     *
     * @return A nxn array representing the current physical state of the board.
     */
    public int[][] getState() {
        int[][] copy = new int[size][];

        for (int i = 0; i < state.length; i++) {
            copy[i] = new int[state[i].length];
            System.arraycopy(state[i], 0, copy[i], 0, state[i].length);
        }

        return copy;
    }

    /**
     * A fast copy method that returns an independent copy of the current
     * puzzle. Does not keep lock state, resets to unlocked.
     *
     * @return A copy of this puzzle.
     */
    public Puzzle quickCopy() {
        Puzzle copy = new Puzzle();
        copy.size = size;
        copy.posCol = posCol;
        copy.posRow = posRow;
        copy.state = this.getState();

        return copy;
    }

    /**
     * Locks the puzzle so that no operations are permitted.
     *
     * @return True if the puzzle was not already locked.
     */
    public boolean lock() {
        if (locked) {
            return false;
        }

        locked = true;
        return true;
    }

    /**
     * Unlocks the puzzle so that operations are again permitted.
     */
    public void unlock() {
        locked = false;
    }

    /**
     * Gets the current lock state.
     *
     * @return True if the puzzle is locked.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Checks if the puzzle's current state matches its success state.
     *
     * @return True if puzzle is considered solved.
     */
    public boolean isSolved() {
        return getStateHash() == SUCCESS_STATE_HASH;
    }

    /**
     * Hashes the current state into a long with guaranteed uniqueness.
     *
     * @return A hash of the puzzle's current state.
     */
    public long getStateHash() {
        return getStateHash(this.state);
    }

    /**
     * A naive hash of the specified state array.
     *
     * @param state Array to hash.
     * @return A guaranteed unique hash of the provided state.
     */
    private static long getStateHash(int[][] state) {
        String hash = "";

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                hash += state[i][j];
            }
        }

        return Long.parseUnsignedLong(hash);
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers(state);
    }

    /**
     * Formats the puzzle's state into an easily readable format.
     *
     * @return The puzzle's current state including an nxn matrix of values and
     * the current location of the empty tile.
     */
    @Override
    public String toString() {
        String out = "";

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                out += "[" + state[i][j] + "]";
            }
            out += "\n";
        }

        out += "Blank tile is at (" + (posCol + 1) + ", " + (posRow + 1) + ")";
        return out;
    }
}
