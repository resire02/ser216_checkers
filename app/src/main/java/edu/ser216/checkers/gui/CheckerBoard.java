package edu.ser216.checkers.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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

/**
 * CheckerBoard module of CheckersGUI
 * handles most of the GUI logic and interaction with GameLogic
 * 
 * @author Felix
 * @version 1.0
 */
public class CheckerBoard extends GridPane
{
    //  member variables
    private final int BOARD_SIZE = 8;                               //  size of board
    private final String SQUARE_COLOR_1 = "#1148BC";                //  color of primary square
    private final String SQUARE_COLOR_2 = "#9DE3FF";                //  color of secondary square
    private final String PIECE_COLOR_1 = "#E94848";                 //  color of 'x' piece
    private final String PIECE_COLOR_2 = "#F6E408";                 //  color of 'o' piece
    private Button[][] board;                                       //  2d array of buttons
    private final int width = 50;                                   //  pref width of button
    private final int height = 50;                                  //  pref height of button
    private static CheckersSquare[] move = new CheckersSquare[2];   //  temp array storing move made
    private static int index = 0;                                   //  index tracking buttons pressed
    private char turn;                                              //  currentTurn

    /**
     * Constructor for CheckerBoard
     */
    public CheckerBoard()
    {
        //  initalize variables
        board = new Button[BOARD_SIZE][BOARD_SIZE];
        turn = 'x';

        //  set black border
        Border border = new Border(new BorderStroke(
            Paint.valueOf("#000000"), 
            BorderStrokeStyle.SOLID, 
            null, 
            new BorderWidths(10)
        ));

        setBorder(border);
          
        //  set black background and scaling
        setScaleShape(true);
        setStyle("-fx-background-color: black");

        //  remove visual limits
        setMaxHeight(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);

        //  update checkerBoard
        updateView();
    }

    /**
     * Constructs the checkerBoard
     */
    private void updateView()
    {
        //  clears the board
        getChildren().clear();

        //  populate the grid
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                //  store col and row info
                Button button = new CheckersSquare(CheckersGUI.checkersGame.getSquare(row, col), row, col);

                //  update board array
                board[BOARD_SIZE - row - 1][col] = button;

                //  set listener
                button.setOnAction(new ButtonHandler());

                //  add button to grid
                add(button, col, BOARD_SIZE - row - 1);

                //  stretch button properties
                setFillHeight(button, true);
                setFillWidth(button, true);
                setVgrow(button, Priority.ALWAYS);
                setHgrow(button, Priority.ALWAYS);

                //  set alignment 
                setValignment(button, VPos.CENTER);
                setHalignment(button, HPos.CENTER);
            }
        }
    }

    /**
     * Button handler class for handling turns
    */
    private class ButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Handles turns
         */
        public void handle(ActionEvent event) 
        {
            //  do nothing if gameStyle has not been set
            if(CheckersGUI.checkersGame.getGameStyle() == ' ') return;
            
            //  read soruce
            CheckersSquare source = (CheckersSquare) event.getSource();
            String resultTurn = null;

            //  clear console
            CheckersConsoleLabel.console.setText("");

            //  read button source into move array
            if(index < 2) move[index++] = source;

            //  fails when move array is fully populated
            if(index != 2) return;
            
            index = 0;

            //  parse player turn
            try
            {
                resultTurn = CheckersGUI.checkersGame.handleTurn(move[0].squarePos + "-" + move[1].squarePos);
            }
            catch(IllegalStateException ex)
            {
                CheckersConsoleLabel.console.setText(ex.getMessage());
            }

            //  return if turn is invalid
            if(resultTurn == null) return;
            
            //  change turns
            turn = turn == 'x' ? 'o' : 'x';

            //  toggle turn panels
            ((UserPanel) CheckersGUI.getMainPane().getLeft()).toggleTurn();
            ((UserPanel) CheckersGUI.getMainPane().getRight()).toggleTurn();

            //  update console
            if(turn == 'o')
                CheckersConsoleLabel.console.setText("Your turn, " + CheckersGUI.player2);
            else
                CheckersConsoleLabel.console.setText("Your turn, " + CheckersGUI.player1);
            
            //  redraw board
            updateView();
            CheckersGUI.checkersGame.nextTurn();
            
            //  check if a player has won
            if(checkWin()) return;

            //  do computer move immediately after player
            if(turn == 'o' && CheckersGUI.checkersGame.getGameStyle() == 'c')
            {
                doComputerMove();
            }
        }

        /**
         * Computes the computer move and updates the board
         */
        private void doComputerMove()
        {
            //  no input needs to be sent
            CheckersGUI.checkersGame.handleTurn("");

            turn = turn == 'x' ? 'o' : 'x';

            ((UserPanel) CheckersGUI.getMainPane().getLeft()).toggleTurn();
            ((UserPanel) CheckersGUI.getMainPane().getRight()).toggleTurn();

            if(turn == 'o')
                CheckersConsoleLabel.console.setText("Your turn, " + CheckersGUI.player2);
            else
                CheckersConsoleLabel.console.setText("Your turn, " + CheckersGUI.player1);
            
            updateView();
            CheckersGUI.checkersGame.nextTurn();

            if(checkWin()) return;

        }
        
        /**
         * Check if a player has won
         * @return true if player won, false otherwise
         */
        private boolean checkWin()
        {
            char win = CheckersGUI.checkersGame.getWinningPlayer();

            if(win != '_')
            {
                //  end game
                CheckersGUI.checkersGame.onEnd();

                //  update console to reflect winner
                if(win == 'x')
                    CheckersConsoleLabel.console.setText(CheckersGUI.player1 + " won the game.");
                else 
                    CheckersConsoleLabel.console.setText(CheckersGUI.player2 + " won the game.");

                //  remove listeners
                for(Button[] row : board)
                {
                    for(Button b : row)
                    {
                        b.setOnAction(null);
                    }
                }

                return true;
            }

            return false;
        }
    }

    /**
     * Button class with extra stored info
     */
    private class CheckersSquare extends Button
    {
        //  instance variables stored upon creation
        public String squarePos;
        public int row, col;
        public char piece;
        
        /**
         * Constructor for CheckersSquare
         * 
         * @param piece on specified square
         * @param row index of square
         * @param col index of square
         */
        public CheckersSquare(char piece, int row, int col)
        {            
            this.row = row;
            this.col = col;
            this.piece = piece;
            
            //  create background graphic
            Rectangle shape = new Rectangle(width, height);
            
            //  alternating checkerboard pattern
            if((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0))
                setStyle("-fx-background-color: " + SQUARE_COLOR_1);
            else
                setStyle("-fx-background-color: " + SQUARE_COLOR_2);

            setShape(shape);

            Circle icon;

            //  set piece at location
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

            //  set button bound limits
            setMinHeight(20);
            setMinWidth(20);

            setMaxHeight(Double.MAX_VALUE);
            setMaxWidth(Double.MAX_VALUE);

            setPrefHeight(height + 1);
            setPrefWidth(width + 1);

            //  parse button location
            squarePos = "" + (row+1) + intToChar(col);
        }

        /**
         * Converts int to char
         * 
         * @param num to be converted
         * @return letter representing col index
         */
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
