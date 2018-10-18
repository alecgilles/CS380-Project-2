package puzzle.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import puzzle.solver.models.Puzzle;
import puzzle.solver.models.StateNode;

/**
 * Capable of finding and displaying solutions for a given solvable Puzzle using
 * a breadth-first search.
 *
 * @author Alec Gilles
 */
public class Solver extends Observable {

    // Time to wait before displaying the next step when demonstrating solution on GUI.
    private final static long SOLVE_VISUALIZATION_STEP_DELAY = 500;

    /**
     * Used to represent the Solver's current state.
     *
     * IDLE: Solver has not performed any tasks. SEARCHING: Solver is currently
     * looking for a solution. SUCCESS: Solver found a solution. FAILURE: Solver
     * tried to find a solution but failed.
     */
    public static enum State {
        IDLE,
        SEARCHING,
        SUCCESS,
        FAILURE
    }

    private Puzzle puzzle;
    // Root node of the internal search tree.
    private StateNode rootState;
    // Nodes on the edge of the current search tree that have yet to be searched.
    private Queue<StateNode> fringe;
    // Set of hashed state arrays that have been visited already
    private HashSet<Long> statesVisited;
    // A simple list of states that lead from initial state to solution state.
    private ArrayList<Puzzle> solutionPath;

    private State state = State.IDLE;

    // These are used to demonstrate the solution on the GUI.
    private Timer changeTimer = new Timer("CHANGE_TIMER", true);
    private boolean changeTimerRunning = false;
    private Queue<int[][]> changes = new LinkedList<>();

    /**
     * Create a Solver linked to a specific Puzzle.
     *
     * @param puzzle This Puzzle will be linked to the Solver and cannot be
     * changed.
     */
    public Solver(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * Finds a solution for the Puzzle associated with this Solver.
     *
     * Uses a breadth-first search to find a path to the solution if one exists.
     * If a solution is found, its path is printed to the console and shown on
     * the GUI. Otherwise, the console and GUI both show that no solution was
     * found.
     */
    public void solve() {
        state = State.SEARCHING;
        setChanged();
        notifyObservers();

        // Initialize search related objects.
        fringe = new LinkedList<>();
        statesVisited = new HashSet<>();
        solutionPath = new ArrayList<>();
        StateNode solution = null;

        rootState = new StateNode(null, puzzle.quickCopy());
        fringe.add(rootState);

        setChanged();
        notifyObservers(rootState);

        // Main search loop. Runs until a solution is found, or there are no more nodes to search.
        while (!fringe.isEmpty()) {
            StateNode current = fringe.remove();
            long currentStateHash = current.getPuzzle().getStateHash();

            // Skip this node if it was already visited.
            if (statesVisited.contains(currentStateHash)) {
                continue;
            }

            statesVisited.add(currentStateHash);

            // Check if this node is the solution. If it is, stop searching.
            if (current.getPuzzle().isSolved()) {
                System.out.println("Search Success!");
                System.out.println("Searched: " + statesVisited.size() + " nodes.");
                solution = current;
                break;
            }

            // The following blocks add states neighboring the current state
            // into the fringe to be searched later.
            if (current.getPuzzle().canMoveLeft()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveLeft();

                StateNode left = new StateNode(current, puzz);
                current.setLeft(left);
                fringe.add(left);
            }

            if (current.getPuzzle().canMoveUp()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveUp();

                StateNode up = new StateNode(current, puzz);
                current.setUp(up);
                fringe.add(up);
            }

            if (current.getPuzzle().canMoveRight()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveRight();

                StateNode right = new StateNode(current, puzz);
                current.setRight(right);
                fringe.add(right);
            }

            if (current.getPuzzle().canMoveDown()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveDown();

                StateNode down = new StateNode(current, puzz);
                current.setDown(down);
                fringe.add(down);
            }
        }

        // A solution was found, build the path to solution and then print and
        // demonstrate it.
        if (solution != null) {
            StateNode cursor = solution;

            while (cursor != null) {
                solutionPath.add(0, cursor.getPuzzle());
                cursor = cursor.getParent();
            }

            state = State.SUCCESS;
            setChanged();
            notifyObservers();

            System.out.println("Steps: " + (solutionPath.size() - 1));

            for (Puzzle puzz : solutionPath) {
                queueSolutionStep(puzz.getState());
                System.out.println(puzz);
            }

        } else {
            System.out.println("No solution found.");
            state = State.FAILURE;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Used to demonstrate the solution on the GUI slow enough that a human can
     * observe it.
     *
     * @param step The next step to add to the demonstration queue.
     */
    private void queueSolutionStep(int[][] step) {
        TimerTask changeTask = new TimerTask() {
            @Override
            public void run() {
                if (!changes.isEmpty()) {
                    puzzle.lock();
                    puzzle.setState(changes.remove());
                } else {
                    this.cancel();
                    changeTimerRunning = false;
                    puzzle.unlock();
                }
            }
        };

        if (!changeTimerRunning && changes.isEmpty()) {
            puzzle.setState(step);

            changeTimer.schedule(changeTask, SOLVE_VISUALIZATION_STEP_DELAY, SOLVE_VISUALIZATION_STEP_DELAY);
            changeTimerRunning = true;
        } else {
            changes.add(step);
        }
    }

    public State getCurrentState() {
        return state;
    }

    /**
     * Get the total number of states that were searched while finding a
     * solution.
     *
     * @return
     */
    public int getNumberStatesSearched() {
        return statesVisited.size();
    }

    /**
     * Get the number of steps to get from the initial state to the solution
     * state.
     *
     * @return
     */
    public int getNumberSolutionSteps() {
        return solutionPath.size() - 1;
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers();
    }
}
