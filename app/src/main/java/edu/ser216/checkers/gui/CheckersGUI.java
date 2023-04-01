package edu.ser216.checkers.gui;

import java.util.Scanner;
import edu.ser216.checkers.core.CheckersGameLogic;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * CheckersGUI class for GUI module of CheckersGame
 * 
 * @author Felix
 * @version 1.0
 */
public class CheckersGUI extends Application
{
    //  member variables
    private final String APP_NAME = "Checkers";         //  application name
    protected static CheckersGameLogic checkersGame;    //  copy of CheckersGame
    private static BorderPane mainPane;                 //  main pane container
    protected static final int WIDTH = 700;             //  starting width dimension
    protected static final int HEIGHT = 550;            //  starting height dimension
    protected static String player1 = "You";            //  player1 name
    protected static String player2 = "Opponent";       //  player2 name

    /**
     * Launches the application
     * 
     * @param args command line arguments
     */
    public static void launchApp(String[] args)
    {
        launch(args);
    }

    /**
     * Retrieves the main container for Scene
     * 
     * @return BorderPane contain all visual nodes
     */
    protected static BorderPane getMainPane()
    {
        return mainPane;
    }

    /**
     * Sets up the main panel
     * 
     * @return Parent object in Scene
     */
    private static Parent setupBorderPanel()
    {
        //  initialize sub panels
        mainPane = new BorderPane();

        UserPanel leftPanel = new UserPanel(player1, "player.png");
        UserPanel rightPanel = new UserPanel(player2, "player.png");
        CheckerBoard board = new CheckerBoard();
        CheckersConsoleLabel console = new CheckersConsoleLabel();
        HBox topContainer = new HBox();

        //  set up title label
        Label title = new Label("Checkers");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Verdana", 45));
        title.setPadding(new Insets(10, 0, 10, 20));

        topContainer.getChildren().add(title);
        topContainer.setAlignment(Pos.CENTER);

        mainPane.setLeft(leftPanel);
        mainPane.setRight(rightPanel);
        mainPane.setCenter(board);
        mainPane.setBottom(console);
        mainPane.setTop(topContainer);

        //  set background
        mainPane.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf(
            "linear-gradient(from 0% 0% to 100% 100%, #011FD8 0%, #2F51B2 50%, #F2938A 100%)"),
            CornerRadii.EMPTY,
            null
        )));

        return mainPane;
    }
    
    /**
     * Application launch method
     * 
     * @param stage to be displayed
     */
    public void start(Stage stage)
    {
        //  setup checkers game
        Scanner scan = new Scanner(System.in);
        checkersGame = new CheckersGameLogic(scan);
        
        //  setup scene
        Scene mainGUI = new Scene(setupBorderPanel(), WIDTH, HEIGHT);

        //  display scene
        stage.setScene(mainGUI);
        stage.setTitle(APP_NAME);
        stage.show();
    }
}
