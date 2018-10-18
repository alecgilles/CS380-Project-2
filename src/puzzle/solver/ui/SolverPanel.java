package puzzle.solver.ui;

import java.util.Observable;
import java.util.Observer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import puzzle.solver.Solver;

/**
 * Allows the user to control and view the status of the Solver.
 *
 * @author Alec Gilles
 */
public class SolverPanel extends HBox implements Observer {

    private Solver solver;
    private final Text status;

    /**
     * Creates a solver panel associated with the provided Solver.
     *
     * @param solver The solver instance to link with this panel.
     * @param height The desired height of this panel.
     */
    public SolverPanel(Solver solver, double height) {
        super.setMinHeight(height);
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
        String statusString = "";
        switch (solver.getCurrentState()) {
            case SEARCHING:
                statusString += "Searching for solution...";
                break;
            case SUCCESS:
                statusString += "Solution found!\n";
                statusString += "Searched: " + solver.getNumberStatesSearched() + " nodes\n";
                statusString += "Steps: " + solver.getNumberSolutionSteps();
                break;
            case FAILURE:
                statusString += "No solution was found.";
                break;
        }

        status.setText(statusString);
    }

}
