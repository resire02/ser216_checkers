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

public class CheckersConsoleLabel extends HBox
{
    protected static Label console;
    private Button b1, b2;

    public CheckersConsoleLabel()
    {
        console = new Label("");

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

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20);
    }

    private class PromptHandler implements EventHandler<ActionEvent>
    {
        public void handle(ActionEvent event) 
        {
            Button source = (Button) event.getSource();

            if(source.getText().equals("Player"))
            {
                CheckersGUI.checkersGame.setGameStyle('p');
            }
            else
            {
                CheckersGUI.checkersGame.setGameStyle('c');
            }

            removeButtons();

            CheckersConsoleLabel.console.setText("Begin Game.");
        }
    }

    private void removeButtons()
    {
        getChildren().removeAll(b1, b2);
    }
}
