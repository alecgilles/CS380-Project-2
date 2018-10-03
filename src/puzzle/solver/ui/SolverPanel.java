package puzzle.solver.ui;

import java.util.Observable;
import java.util.Observer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import puzzle.solver.Solver;

/**
 *
 * @author gillab01
 */
public class SolverPanel extends HBox implements Observer {
    
    private Solver solver;
    private Text status;
    
    public SolverPanel(Solver solver) {
        this.solver = solver;
        
        setPadding(new Insets(15, 15, 15, 15));
        setSpacing(15);
        setStyle("-fx-background-color: #adadad;");
        
        Button solveButton = new Button("Solve");
        solveButton.setDefaultButton(true);
        solveButton.setOnAction(event -> {
            this.solver.solve();
        });
        super.getChildren().add(solveButton);
        
        status = new Text();
        super.getChildren().add(status);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        switch(solver.getCurrentState()) {
            case SUCCESS:
                status.setText("Solution found!");
                break;
            case FAILURE:
                status.setText("No solution was found.");
                break;
        }
    }
    
}
