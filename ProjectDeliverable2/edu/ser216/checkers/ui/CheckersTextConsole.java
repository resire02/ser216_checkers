package edu.ser216.checkers.ui;

import edu.ser216.checkers.core.CheckersGame;

/**
 * Implementation of CheckersViewer, handles console UI for CheckersGame
 * 
 * @author Felix, Baron
 * @version 1.0
 */
public class CheckersTextConsole implements CheckersViewer
{
    /**
     * Returns a String representation of the board
     * 
     * @param game object containing the board data
     * @return String representation of the Checkers board
     */
    public String printBoard(final CheckersGame game)
    {
        String output = "";
        
        for(int row = 7; row >= 0; row--)
        {
            output += (row + 1) + "|";

            for(int col = 0; col < 8; col++)
            {
                output += game.getSquare(row, col) + "|";
            }
            
            output += "\n";
        }
        
        output += "  a b c d e f g h";

        return output;
    }
}