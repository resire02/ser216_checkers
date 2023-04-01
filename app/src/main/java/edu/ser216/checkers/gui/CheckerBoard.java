package edu.ser216.checkers.gui;


import edu.ser216.checkers.core.CheckersGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.beans.binding.Bindings;

public class CheckerBoard extends GridPane
{
    private final int BOARD_SIZE = 8;
    private final String SQUARE_COLOR_1 = "#1148BC";
    private final String SQUARE_COLOR_2 = "#9DE3FF";
    private final String PIECE_COLOR_1 = "#E94848";
    private final String PIECE_COLOR_2 = "#F6E408";
    private Button[][] board;
    private final int width = 50;
    private final int height = 50;
    private static CheckersSquare[] move = new CheckersSquare[2];
    private static int index = 0;
    private CheckersGame game;

    public CheckerBoard(final CheckersGame game)
    {
        board = new Button[BOARD_SIZE][BOARD_SIZE];
        this.game = game;

        Border border = new Border(new BorderStroke(
            Paint.valueOf("#000000"), 
            BorderStrokeStyle.SOLID, 
            null, 
            new BorderWidths(10)
        ));

        setBorder(border);
          
        setScaleShape(true);
        setStyle("-fx-background-color: black");

        setMaxHeight(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);

        updateView();
    }

    private void updateView()
    {
        getChildren().clear();

        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                Button button = new CheckersSquare(game.getSquare(row, col), row, col);

                board[BOARD_SIZE - row - 1][col] = button;

                button.setOnAction(new ButtonHandler());

                add(button, col, BOARD_SIZE - row - 1);

                setFillHeight(button, true);
                setFillWidth(button, true);

                setVgrow(button, Priority.ALWAYS);
                setHgrow(button, Priority.ALWAYS);

                setValignment(button, VPos.CENTER);
                setHalignment(button, HPos.CENTER);
            }
        }
    }

    private class ButtonHandler implements EventHandler<ActionEvent>
    {
        public void handle(ActionEvent event) 
        {
            CheckersSquare source = (CheckersSquare) event.getSource();
            String resultTurn = null;

            CheckersConsoleLabel.console.setText("");

            if(index < 2) move[index++] = source;

            if(index != 2) return;
            
            index = 0;
            
            try
            {
                resultTurn = CheckersGUI.checkersGame.handleTurn(move[0].squarePos + "-" + move[1].squarePos);
            }
            catch(IllegalStateException ex)
            {
                CheckersConsoleLabel.console.setText(ex.getMessage());
            }

            if(resultTurn != null)
            {
                CheckersConsoleLabel.console.setText(resultTurn);

                if(Math.abs(move[1].col - move[0].col) == 2 && Math.abs(move[1].row - move[0].row) == 2)
                {
                    Node piece = board[move[0].row][move[0].col].getGraphic();
                    board[(move[1].row + move[0].row) / 2]
                        [(move[1].col + move[0].col) / 2].setGraphic(null);
                    board[move[1].row][move[0].col].setGraphic(piece);
                }
                else
                {
                    Node piece = board[move[0].row][move[0].col].getGraphic();
                    board[move[0].row][move[0].col].setGraphic(null);
                    board[move[1].row][move[1].col].setGraphic(piece);
                }

                updateView();

                CheckersGUI.checkersGame.nextTurn();
            }
        }
        
    }

    private class CheckersSquare extends Button
    {
        public String squarePos;
        public int row, col;
        public char piece;
        
        public CheckersSquare(char piece, int row, int col)
        {            
            this.row = row;
            this.col = col;
            this.piece = piece;
            
            Rectangle shape = new Rectangle(width, height);
            
            if((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0))
                setStyle("-fx-background-color: " + SQUARE_COLOR_1);
            else
                setStyle("-fx-background-color: " + SQUARE_COLOR_2);

            setShape(shape);

            Circle icon;

            if(piece == 'x')
            {
                icon = new Circle(width / 2, Paint.valueOf(PIECE_COLOR_1));
                setGraphic(icon);

                icon.radiusProperty().bind(Bindings.min(this.heightProperty(), this.widthProperty()).divide(3));
            }
            else if(piece == 'o')
            {
                icon = new Circle(width / 2, Paint.valueOf(PIECE_COLOR_2));
                setGraphic(icon);

                icon.radiusProperty().bind(Bindings.min(this.heightProperty(), this.widthProperty()).divide(3));
            }

            setMinHeight(20);
            setMinWidth(20);

            setMaxHeight(Double.MAX_VALUE);
            setMaxWidth(Double.MAX_VALUE);

            setPrefHeight(height + 1);
            setPrefWidth(width + 1);

            squarePos = "" + (row+1) + intToChar(col);
        }

        private char intToChar(int num)
        {
            switch(num)
            {
                case 0:
                    return 'a';
                case 1:
                    return 'b';
                case 2:
                    return 'c';
                case 3:
                    return 'd';
                case 4:
                    return 'e';
                case 5:
                    return 'f';
                case 6:
                    return 'g';
                case 7:
                    return 'h';
                default:
                    return '_';
            }
        }
    }
}
