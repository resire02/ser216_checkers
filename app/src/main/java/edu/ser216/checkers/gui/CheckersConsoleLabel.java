package edu.ser216.checkers.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Represents the side panel module of CheckersGUI
 * 
 * @author Felix
 * @version 1.0
 */
public class CheckersConsoleLabel extends HBox
{
    protected static Label console;
    private Button b1, b2;

    /**
     * Constructor for CheckersConsoleLabel
     */
    public CheckersConsoleLabel()
    {
        console = new Label("");

        //  set custom text formatting
        console.setAlignment(Pos.CENTER);
        console.setTextFill(Color.WHITE);
        console.setFont(Font.font("Verdana", 20));
        console.setTextAlignment(TextAlignment.CENTER);
        console.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);

        b1 = new Button("Player");
        b2 = new Button("Computer");

        b1.setOnAction(new PromptHandler());
        b2.setOnAction(new PromptHandler());

        getChildren().addAll(console, b1, b2);

        //  align parent
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20);
    }

    /**
     * PromptHandler class for determining gameStyle
     */
    private class PromptHandler implements EventHandler<ActionEvent>
    {
        /**
         * read gameStyle and delete button afterward
         */
        public void handle(ActionEvent event) 
        {
            Button source = (Button) event.getSource();

            if(source.getText().equals("Player"))
            {
                CheckersGUI.checkersGame.setGameStyle('p');
            }
            else
            {
                //  set player2 name to "Computer"
                CheckersGUI.checkersGame.setGameStyle('c');
                CheckersGUI.player2 = "Computer";
                ((UserPanel) CheckersGUI.getMainPane().getRight()).setPlayerName("Computer");
            }

            removeButtons();

            CheckersConsoleLabel.console.setText("Begin Game.");

            ((UserPanel) CheckersGUI.getMainPane().getLeft()).toggleTurn();
        }
    }

    private void removeButtons()
    {
        getChildren().removeAll(b1, b2);
    }
}
