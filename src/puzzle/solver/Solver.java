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
 *
 * @author gillab01
 */
public class Solver extends Observable {
    
    private final static long SOLVE_VISUALIZATION_STEP_DELAY = 500;
    
    public static enum State {
        IDLE,
        SEARCHING,
        SUCCESS,
        FAILURE
    }
    
    private Puzzle puzzle;
    private StateNode rootState;
    private Queue<StateNode> fringe;
    // Set of deep hashed state arrays that have been visited already
    private HashSet<Long> statesVisited;
    
    private State state = State.IDLE;
    
    private Timer changeTimer = new Timer("CHANGE_TIMER", true);
    private boolean changeTimerRunning = false;
    private Queue<int[][]> changes = new LinkedList<>();

    public Solver(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void solve() {
        state = State.SEARCHING;
        setChanged();
        notifyObservers();
        
        fringe = new LinkedList<>();
        statesVisited = new HashSet<>();
        ArrayList<Puzzle> solutionPath = new ArrayList<>();
        StateNode solution = null;
        int nodesSearched = 0;
        
        rootState = new StateNode(null, puzzle.quickCopy());
        fringe.add(rootState);
        
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
            
            state = State.SUCCESS;
            setChanged();
            notifyObservers();
            
            System.out.println("Steps: " + (solutionPath.size() - 1));
            
            for(Puzzle puzz : solutionPath) {
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
                
                changeTimer.schedule(changeTask, SOLVE_VISUALIZATION_STEP_DELAY, SOLVE_VISUALIZATION_STEP_DELAY);
                changeTimerRunning = true;
            } else {
                changes.add(step);
            }
    }
    
    public State getCurrentState() {
        return state;
    }
    
    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers();
    }
}
