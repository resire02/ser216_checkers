package edu.ser216.checkers.core;

/**
 * An interface that abstractly defines a checkers game board with logic.
 *
 * @author Acuna
 * @version 1.0
 */
public interface CheckersGame {

    /**
     * Returns the square at a specific location on the game board.
     *
     * For example, given the following board:
     *   8|_|o|_|o|_|o|_|o|
     *   7|o|_|o|_|o|_|o|_|
     *   6|_|o|_|o|_|o|_|o|
     *   5|_|_|_|_|_|_|_|_|
     *   4|_|_|_|_|_|_|_|_|
     *   3|x|_|x|_|x|_|x|_|
     *   2|_|x|_|x|_|x|_|x|
     *   1|x|_|x|_|x|_|x|_|
     *   a b c d e f g h
       getSquare(0,0) would return 'x'.

     * @param row Row.
     * @param column Column.
     * @return Contents of game board at position.
     */
    public char getSquare(int row, int column);

    /**
     * Sets the contents ('x', 'o', or '_') of the game board.
     * @param row Row.
     * @param column Column.
     * @param content Contents.
     */
    public void setSquare(int row, int column, char content);

    /**
     * Returns 'x' if player X  has won, 'o' if player O has won, and '_' otherwise. May be called
     * at any time.
     *
     * @return A character representing the winter.
     */
    public char getWinningPlayer();

    /**
     * Indicates to the game that it is the next player's turn. To read input from the keyboard,
     * this method must make use of the Scanner object passed into the constructor of this
     * CheckersGame. Valid inputs must look like "3a-4b".
     */
    public void nextTurn();

    /**
     * Indicates to the game that a players turn should occur (i.e., read a move and act upon it).
     */
    public void doTurn();

    /**
     * Indicates to the game that it is over. Used to do things like display the winner.
     */
    public void onEnd();
}
