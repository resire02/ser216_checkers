package edu.ser216.checkers.ui;

import edu.ser216.checkers.core.CheckersGame;

/**
 * An interface that defines a class to display the current board state of a CheckersGame object.
 *
 * @author Acuna, Baron
 * @version 1.0
 */
public interface CheckersViewer {
    /**
     * Returns string representation of the board in a CheckersGame. A 'o' will represent a piece
     * for player O, and an 'x' will represent a piece for player X. A '_' will represent an
     * unoccupied space. The axis will be labeled with numbers and letters as shown below, a
     * vertical bar will separate columns.
     *
     * For example:
     *   8|_|o|_|o|_|o|_|o|
     *   7|o|_|o|_|o|_|o|_|
     *   6|_|o|_|o|_|o|_|o|
     *   5|_|_|_|_|_|_|_|_|
     *   4|_|_|_|_|_|_|_|_|
     *   3|x|_|x|_|x|_|x|_|
     *   2|_|x|_|x|_|x|_|x|
     *   1|x|_|x|_|x|_|x|_|
     *   a b c d e f g h
     * @param game Game to visualize.
     * @return String representation of board.
     */
    public String printBoard(final CheckersGame game);
}
