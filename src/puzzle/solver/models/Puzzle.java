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

    private int[][] state;
    int size;
    private int posCol, posRow;

    public Puzzle(int[][] state) {
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
        } else {
            throw new IllegalArgumentException("State must be a non-empty 2D integer array");
        }
    }
    
    public boolean canMoveLeft() {
        return posCol > 0;
    }
    
    public boolean canMoveRight() {
        return posCol < size - 1;
    }
    
    public boolean canMoveUp() {
        return posRow > 0;
    }
    
    public boolean canMoveDown() {
        return posRow < size - 1;
    }
    
    public void moveLeft() {
        if(canMoveLeft()) {
            state[posRow][posCol] = state[posRow][posCol - 1];
            posCol -= 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
        }
    }
    
    public void moveRight() {
        if(canMoveRight()) {
            state[posRow][posCol] = state[posRow][posCol + 1];
            posCol += 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
        }
    }
    
    public void moveUp() {
        if(canMoveUp()) {
            state[posRow][posCol] = state[posRow - 1][posCol];
            posRow -= 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
        }
    }
    
    public void moveDown() {
        if(canMoveDown()) {
            state[posRow][posCol] = state[posRow + 1][posCol];
            posRow += 1;
            state[posRow][posCol] = 0;
            
            setChanged();
            notifyObservers(state);
        }
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
