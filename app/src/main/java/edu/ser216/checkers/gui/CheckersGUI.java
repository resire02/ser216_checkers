package edu.ser216.checkers.gui;

import java.util.Scanner;

import edu.ser216.checkers.core.CheckersGame;
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

public class CheckersGUI extends Application
{
    private final String APP_NAME = "Checkers";
    protected static CheckersGameLogic checkersGame;
    private BorderPane mainPane;
    protected static final int WIDTH = 700;
    protected static final int HEIGHT = 550;

    public static void launchApp(String[] args)
    {
        launch(args);
    }

    private Parent setupBorderPanel()
    {
        mainPane = new BorderPane();

        UserPanel leftPanel = new UserPanel("You", "player.png");
        UserPanel rightPanel = new UserPanel("Opponent", "player.png");
        CheckerBoard board = new CheckerBoard(checkersGame);
        CheckersConsoleLabel console = new CheckersConsoleLabel();
        HBox topContainer = new HBox();
        
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

        mainPane.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf(
            "linear-gradient(from 0% 0% to 100% 100%, #011FD8 0%, #2F51B2 50%, #F2938A 100%)"),
            CornerRadii.EMPTY,
            null
        )));

        return mainPane;
    }
    
    public void start(Stage stage)
    {
        Scanner scan = new Scanner(System.in);

        checkersGame = new CheckersGameLogic(scan, 'a');
        
        Scene mainGUI = new Scene(setupBorderPanel(), WIDTH, HEIGHT);

        // stage.minWidthProperty().bind(mainGUI.heightProperty().multiply(1.5));
        // stage.minHeightProperty().bind(mainGUI.widthProperty().divide(1.5));

        stage.setScene(mainGUI);
        stage.setTitle(APP_NAME);
        stage.show();
    }
}
