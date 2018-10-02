/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.solver;

import puzzle.solver.ui.PuzzlePanel;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import puzzle.solver.models.Puzzle;

/**
 *
 * @author gillab01
 */
public class App extends Application {
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
        System.out.println(puzz);
        Solver solver = new Solver(puzz);
        
        PuzzlePanel puzzPanel = new PuzzlePanel(BOARD_WIDTH, BOARD_HEIGHT);
        puzz.addObserver(puzzPanel);
        
        Group root = new Group();
        root.getChildren().add(puzzPanel);
        
        primaryStage.setTitle("Puzzle Solver");
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
