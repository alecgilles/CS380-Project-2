package puzzle.solver.models;

/**
 *
 * @author gillab01
 */
public class StateNode {
    private StateNode parent;
    private StateNode up;
    private StateNode down;
    private StateNode left;
    private StateNode right;
    private Puzzle puzzle;
    
    public StateNode(StateNode parent, Puzzle puzzle) {
        this.parent = parent;
        this.puzzle = puzzle;
    }
    
    public void setParent(StateNode parent) {
        this.parent = parent;
    }
    
    public void setUp(StateNode up) {
        this.up = up;
    }
    
    public void setDown(StateNode down) {
        this.down = down;
    }
    
    public void setLeft(StateNode left) {
        this.left = left;
    }
    
    public void setRight(StateNode right) {
        this.right = right;
    }
    
    public StateNode getParent() {
        return parent;
    }
    
    public StateNode getUp() {
        return up;
    }
    
    public StateNode getDown() {
        return down;
    }
    
    public StateNode getLeft() {
        return left;
    }
    
    public StateNode getRight() {
        return right;
    }
    
    public Puzzle getPuzzle() {
        return puzzle;
    }
}
