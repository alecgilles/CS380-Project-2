package puzzle.solver;

import java.util.Arrays;
import java.util.HashSet;
import puzzle.solver.models.Puzzle;
import puzzle.solver.models.StateNode;

/**
 *
 * @author gillab01
 */
public class Solver {

    private final static int[][] SUCCESS_STATE = {
        {2, 8, 6},
        {1, 3, 4},
        {7, 0, 5}
    };
    
    private Puzzle puzzle;
    private StateNode rootState;
    // Set of deep hashed state arrays that have been visited already
    private HashSet<Integer> statesVisited;

    public Solver(Puzzle puzzle) {
        this.puzzle = new Puzzle(puzzle.getState());
        rootState = new StateNode();
        statesVisited = new HashSet<>();

        setStateVisited();
    }

    public void solve() {
        
    }

    private void setStateVisited() {
        statesVisited.add(Arrays.deepHashCode(puzzle.getState()));
    }
}
