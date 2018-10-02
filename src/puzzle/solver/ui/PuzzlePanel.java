package puzzle.solver.ui;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author gillab01
 */
public class PuzzlePanel extends Canvas implements Observer {

    private static final double BOX_PADDING_PERCENT = 0.05;
    
    private Timer changeTimer = new Timer("CHANGE_TIMER", true);
    private boolean timerRunning = false;
    private Queue<int[][]> changes = new LinkedList<>();

    public PuzzlePanel(double displayWidth, double displayHeight) {
        super(displayWidth, displayHeight);
    }

    private void draw(int[][] state) {
        int size = state.length;
        
        GraphicsContext gc = getGraphicsContext2D();
        gc.save();

        double xPadding = BOX_PADDING_PERCENT * this.getWidth();
        double yPadding = BOX_PADDING_PERCENT * this.getHeight();

        double boxOffsetX = (this.getWidth() - xPadding) / size;
        double boxOffsetY = (this.getHeight() - yPadding) / size;

        double boxWidth = boxOffsetX - xPadding;
        double boxHeight = boxOffsetY - yPadding;

        double fontHeight = boxHeight * 0.8;

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(fontHeight));
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double xPos = j * boxOffsetX + xPadding;
                double yPos = i * boxOffsetY + yPadding;
                
                if (state[i][j] != 0) {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.setStroke(Color.BLACK);
                    gc.fillRect(xPos, yPos, boxWidth, boxHeight);
                    gc.strokeRect(xPos, yPos, boxWidth, boxHeight);

                    gc.setFill(Color.WHITE);
                    gc.fillText(state[i][j] + "", xPos + boxWidth / 2, yPos + fontHeight);
                } else {
                    gc.setStroke(Color.BLACK);
                    gc.strokeRect(xPos, yPos, boxWidth, boxHeight);
                    
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillText("X", xPos + boxWidth / 2, yPos + fontHeight);
                }
            }
        }

        gc.restore();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null) {
            TimerTask changeTask = new TimerTask(){
                    @Override
                    public void run() {
                        if(!changes.isEmpty()) {
                            draw(changes.remove());
                        } else {
                            this.cancel();
                            timerRunning = false;
                        }
                    } 
                };
            
            if(!timerRunning && changes.isEmpty()) {
                draw((int[][])arg);
                
                changeTimer.schedule(changeTask, 500, 500);
                timerRunning = true;
            } else {
                changes.add((int[][])arg);
            }
        }
    }
}
