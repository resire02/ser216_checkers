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

public class UserPanel extends VBox
{
    private Label playerName;
    private ImageView playerImage;

    public UserPanel(final String name, final String imageLink)
    {
        playerName = new Label(name);
        Image image = new Image(imageLink);        
        playerImage = new ImageView(image);

        playerName.setTextFill(Color.WHITE);
        playerName.setAlignment(Pos.CENTER);
        playerName.setFont(Font.font("Verdana", 20));
        playerName.setTextAlignment(TextAlignment.CENTER);
        playerName.setTextOverrun(OverrunStyle.WORD_ELLIPSIS);

        playerImage.setFitHeight(100);
        playerImage.setFitWidth(100);

        setMargin(playerImage, new Insets(10, 10, 10, 10));

        getChildren().addAll(playerImage, this.playerName);
        setAlignment(Pos.CENTER);
    }


}
