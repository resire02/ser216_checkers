package edu.ser216.checkers.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * UserPanel module for CheckersGUI
 */
public class UserPanel extends VBox
{
    private Label playerName;
    private ImageView playerImage;
    private Label turn;

    /**
     * Sets up the userPanel
     * @param name of player
     * @param imageLink to image to be used
     */
    public UserPanel(final String name, final String imageLink)
    {
        playerName = new Label(name);
        Image image = new Image(imageLink);        
        playerImage = new ImageView(image);
        turn = new Label();

        turn.setTextFill(Color.WHITE);
        turn.setAlignment(Pos.CENTER);
        turn.setFont(Font.font("Verdana", 10));
        turn.setTextAlignment(TextAlignment.CENTER);
        turn.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);

        playerName.setTextFill(Color.WHITE);
        playerName.setAlignment(Pos.CENTER);
        playerName.setFont(Font.font("Verdana", 20));
        playerName.setTextAlignment(TextAlignment.CENTER);
        playerName.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);

        playerImage.setFitHeight(100);
        playerImage.setFitWidth(100);

        setMargin(playerImage, new Insets(10, 10, 10, 10));
        setSpacing(5);
        getChildren().addAll(playerImage, this.playerName, turn);
        setAlignment(Pos.CENTER);
    }

    /**
     * Toggles the "your turn" prompt for users
     */
    protected void toggleTurn()
    {
        if(turn.getText().length() == 0)
            turn.setText("Your Turn.");
        else
            turn.setText("");
    }

    protected void setPlayerName(String name)
    {
       playerName.setText(name);
    }


}
