package puzzle.solver.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author gillab01
 */
public class TreeNode {
    private ArrayList<TreeNode> children;
    
    public TreeNode() {
        children = new ArrayList<>();
    }
    
    public void add(TreeNode child) {
        children.add(child);
    }
    
    public void addAll(Collection children) {
        children.addAll(children);
    }
    
    public ArrayList<TreeNode> getChildren() {
        return children;
    }
}
