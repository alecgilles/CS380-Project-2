package puzzle.solver.ui;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import puzzle.solver.models.StateNode;

/**
 *
 * @author Alec Gilles
 */
public class SearchVisualizationPanel extends Canvas implements Observer {

    private StateNode rootNode;
    private int count;

    public SearchVisualizationPanel(double displayWidth, double displayHeight) {
        super(displayWidth, displayHeight);
        draw();
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.save();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, getWidth(), getHeight());

        if (rootNode != null) {
            count = 0;
            drawTree(rootNode, gc);
            System.out.println(count);
        }

        gc.restore();
    }

    private void drawTree(StateNode root, GraphicsContext gc) {
        if (root == null) {
            return;
        }

        count++;

        drawTree(root.getLeft(), gc);
        drawTree(root.getUp(), gc);
        drawTree(root.getRight(), gc);
        drawTree(root.getDown(), gc);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof StateNode) {
            rootNode = (StateNode) arg;
        }

        draw();
    }
}
