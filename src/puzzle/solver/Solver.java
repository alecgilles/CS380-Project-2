package puzzle.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import puzzle.solver.models.Puzzle;
import puzzle.solver.models.StateNode;

/**
 *
 * @author gillab01
 */
public class Solver {
    private Puzzle puzzle;
    private StateNode rootState;
    private Queue<StateNode> fringe;
    // Set of deep hashed state arrays that have been visited already
    private HashSet<Long> statesVisited;
    
    private Timer changeTimer = new Timer("CHANGE_TIMER", true);
    private boolean changeTimerRunning = false;
    private Queue<int[][]> changes = new LinkedList<>();

    public Solver(Puzzle puzzle) {
        this.puzzle = puzzle;
        fringe = new LinkedList<>();
        statesVisited = new HashSet<>();
    }

    public void solve() {
        ArrayList<Puzzle> solutionPath = new ArrayList<>();
        StateNode solution = null;
        rootState = new StateNode(null, puzzle.quickCopy());
        fringe.add(rootState);
        int nodesSearched = 0;
        
        while(!fringe.isEmpty()) {
            StateNode current = fringe.remove();
            long currentStateHash = current.getPuzzle().getStateHash();
            
            if(statesVisited.contains(currentStateHash)) {
                continue;
            }
            
            statesVisited.add(currentStateHash);
            
            nodesSearched++;
            
            if(current.getPuzzle().isSolved()) {
                System.out.println("Search Success!");
                System.out.println("Searched: "+nodesSearched+" nodes.");
                solution = current;
                break;
            }
            
            if(current.getPuzzle().canMoveLeft()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveLeft();
                
                StateNode left = new StateNode(current, puzz);
                current.setLeft(left);
                fringe.add(left);
            }
            
            if(current.getPuzzle().canMoveUp()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveUp();
                
                StateNode up = new StateNode(current, puzz);
                current.setUp(up);
                fringe.add(up);
            }
            
            if(current.getPuzzle().canMoveRight()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveRight();
                
                StateNode right = new StateNode(current, puzz);
                current.setRight(right);
                fringe.add(right);
            }
            
            if(current.getPuzzle().canMoveDown()) {
                Puzzle puzz = current.getPuzzle().quickCopy();
                puzz.moveDown();
                
                StateNode down = new StateNode(current, puzz);
                current.setDown(down);
                fringe.add(down);
            }
        }
        
        if(solution != null) {
            StateNode cursor = solution;
            
            while(cursor != null) {
                solutionPath.add(0, cursor.getPuzzle());
                cursor = cursor.getParent();
            }
            
            System.out.println("Steps: " + (solutionPath.size() - 1));
            
            for(Puzzle puzz : solutionPath) {
                queueSolutionStep(puzz.getState());
                System.out.println(puzz);
            }

        } else {
            System.out.println("No solution found.");
        }
    }
    
    private void queueSolutionStep(int[][] step) {
        TimerTask changeTask = new TimerTask(){
                    @Override
                    public void run() {
                        if(!changes.isEmpty()) {
                            puzzle.lock();
                            puzzle.setState(changes.remove());
                        } else {
                            this.cancel();
                            changeTimerRunning = false;
                            puzzle.unlock();
                        }
                    } 
                };
            
            if(!changeTimerRunning && changes.isEmpty()) {
                puzzle.setState(step);
                
                changeTimer.schedule(changeTask, 500, 500);
                changeTimerRunning = true;
            } else {
                changes.add(step);
            }
    }
}
