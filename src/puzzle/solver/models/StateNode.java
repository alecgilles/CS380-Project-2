package puzzle.solver.models;

/**
 *
 * @author gillab01
 */
public class StateNode {
    private StateNode up;
    private StateNode down;
    private StateNode left;
    private StateNode right;
    
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
}
