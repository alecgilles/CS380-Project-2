package puzzle.solver.ui;

import java.util.Observable;
import java.util.Observer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import puzzle.solver.Solver;

/**
 *
 * @author gillab01
 */
public class SolverPanel extends HBox implements Observer {
    
    private Solver solver;
    
    public SolverPanel(Solver solver) {
        this.solver = solver;
        
        setPadding(new Insets(15, 15, 15, 15));
        setStyle("-fx-background-color: #adadad;");
        
        Button solveButton = new Button("Solve");

        super.getChildren().add(solveButton);
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }
    
}
