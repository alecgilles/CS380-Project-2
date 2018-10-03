package puzzle.solver;

import puzzle.solver.ui.PuzzlePanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import puzzle.solver.models.Puzzle;
import puzzle.solver.ui.SolverPanel;

/**
 *
 * @author gillab01
 */
public class App extends Application {
    private static final String WINDOW_TITLE = "Puzzle Solver";
    private static final int  BOARD_WIDTH = 600, BOARD_HEIGHT = 600;
    
    @Override
    public void start(Stage primaryStage) {
        
        int[][] state = {
            {0,5,4},
            {2,8,3},
            {6,7,1}
        };/*
        int[][] state = {
            {2,8,6},
            {1,3,4},
            {7,0,5}
        };*/
        
        Puzzle puzz = new Puzzle(state);
        Solver solver = new Solver(puzz);
        
        System.out.println("Initial Puzzle:");
        System.out.println(puzz);
        
        BorderPane root = new BorderPane();
        
        PuzzlePanel puzzPanel = new PuzzlePanel(BOARD_WIDTH, BOARD_HEIGHT);
        puzz.addObserver(puzzPanel);
        root.setCenter(puzzPanel);
        
        SolverPanel solverPanel = new SolverPanel();
        solver.addObserver(solverPanel);
        root.setBottom(solverPanel);
        
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        
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
                case ENTER:
                    solver.solve();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
