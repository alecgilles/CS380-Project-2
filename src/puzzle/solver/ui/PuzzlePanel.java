package puzzle.solver.ui;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import puzzle.solver.models.Puzzle;

/**
 *
 * @author gillab01
 */
public class PuzzlePanel extends Canvas implements Observer {

    private static final double BOX_PADDING_PERCENT = 0.05;

    public PuzzlePanel(double displayWidth, double displayHeight) {
        super(displayWidth, displayHeight);
    }

    private void draw(Puzzle puzzle, int[][] state) {
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
                    if(puzzle.isSolved()) {
                        gc.setFill(Color.LIMEGREEN);
                    } else {
                        gc.setFill(Color.rgb(51,102,153));
                    }
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
        if(o != null && arg != null) {
            draw((Puzzle) o, (int[][])arg);
        }
    }
}
