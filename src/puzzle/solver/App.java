package puzzle.solver;

import puzzle.solver.ui.PuzzlePanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import puzzle.solver.models.Puzzle;
import puzzle.solver.ui.SolverPanel;

/**
 * JavaFX Application based class that bootstraps the program.
 *
 * @author Alec Gilles
 */
public class App extends Application {

    private static final String WINDOW_TITLE = "Puzzle Solver";
    private static final int BOARD_WIDTH = 600, BOARD_HEIGHT = 600;
    private static final int[][] INITIAL_STATE = {
        {0, 5, 4},
        {2, 8, 3},
        {6, 7, 1}
    };/* Alternate unsolvable initial state
        {2,8,6},
        {1,3,4},
        {7,0,5}
    };*/

    /**
     * Called by the JavaFX bootstrap process and initializes the Application.
     *
     * Builds GUI and instantiates major application components.
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Puzzle puzz = new Puzzle(INITIAL_STATE);
        Solver solver = new Solver(puzz);

        System.out.println("Initial Puzzle:");
        System.out.println(puzz);

        BorderPane root = new BorderPane();
        BorderPane canvasPane = new BorderPane();
        canvasPane.setPadding(new Insets(10, 10, 10, 10));

        PuzzlePanel puzzPanel = new PuzzlePanel(BOARD_WIDTH, BOARD_HEIGHT);
        puzz.addObserver(puzzPanel);
        canvasPane.setLeft(puzzPanel);

        SolverPanel solverPanel = new SolverPanel(solver, 100);
        solver.addObserver(solverPanel);

        // This component was originally intended to visually show the solution path in a graph, but I ran out of time to implement it.
        //SearchVisualizationPanel searchVis = new SearchVisualizationPanel(BOARD_WIDTH, BOARD_HEIGHT);
        //solver.addObserver(searchVis);
        //canvasPane.setRight(searchVis);
        root.setTop(canvasPane);
        root.setBottom(solverPanel);

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Set up keyboard controls for manually manipulating the puzzle.
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            switch (key.getCode()) {
                case LEFT:
                    puzz.moveLeft();
                    break;
                case UP:
                    puzz.moveUp();
                    break;
                case RIGHT:
                    puzz.moveRight();
                    break;
                case DOWN:
                    puzz.moveDown();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Program entry point.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
