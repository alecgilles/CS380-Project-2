/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.solver.models;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author gillab01
 */
public class Puzzle extends Observable {
    
    private final static long SUCCESS_STATE_HASH = getStateHash( new int[][] {
        {1, 2, 3},
        {8, 0, 4},
        {7, 6, 5}
    });

    private boolean locked = false;
    private int[][] state;
    int size;
    private int posCol, posRow;

    private Puzzle() {}
    
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
    
    public boolean moveLeft() {
        if(canMoveLeft()) {
            state[posRow][posCol] = state[posRow][posCol - 1];
            posCol -= 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
            
            return true;
        }
        
        return false;
    }
    
    public boolean moveRight() {
        if(canMoveRight()) {
            state[posRow][posCol] = state[posRow][posCol + 1];
            posCol += 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
            
            return true;
        }
        
        return false;
    }
    
    public boolean moveUp() {
        if(canMoveUp()) {
            state[posRow][posCol] = state[posRow - 1][posCol];
            posRow -= 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
            
            return true;
        }
        
        return false;
    }
    
    public boolean moveDown() {
        if(canMoveDown()) {
            state[posRow][posCol] = state[posRow + 1][posCol];
            posRow += 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
            
            return true;
        }
        
        return false;
    }
    
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
    
    public int[][] getState() {
        int[][] copy = new int[size][];
        
        for(int i = 0; i < state.length; i++) {
            copy[i] = new int[state[i].length];
            System.arraycopy(state[i], 0, copy[i], 0, state[i].length);
        }
        
        return copy;
    }
    
    public Puzzle quickCopy() {
        Puzzle copy = new Puzzle();
        copy.size = size;
        copy.posCol = posCol;
        copy.posRow = posRow;
        copy.state = this.getState();
          
        return copy;
    }
    
    public boolean lock() {
        if(locked) {
            return false;
        }
        
        locked = true;
        return true;
    }
    
    public void unlock() {
        locked = false;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public boolean isSolved() {
        return getStateHash() == SUCCESS_STATE_HASH;
    }
    
    public long getStateHash() {
        return getStateHash(this.state);
    }
    
    private static long getStateHash(int[][] state) {
        String hash = "";
        
        for(int i = 0; i < state.length; i++) {
            for(int j = 0; j < state[i].length; j++) {
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
