package puzzle.solver;

import java.util.HashSet;
import puzzle.solver.models.Puzzle;
import puzzle.solver.models.StateNode;

/**
 *
 * @author gillab01
 */
public class Solver {
    private Puzzle puzzle;
    private StateNode rootState;
    private HashSet<Integer> statesVisited;
    
    public Solver(Puzzle puzzle) {
        this.puzzle = new Puzzle(puzzle.getState());
    }
}
