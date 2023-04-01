package edu.ser216.checkers.core;

import java.util.Scanner;

/**
 * Implementation of CheckersGame, handles core game logic and game eventflow
 * 
 * @author Felix, Baron
 * @version 1.1
 */
public class CheckersGameLogic implements CheckersGame
{
    //  Final members
    private final int BOARD_SIZE = 8;

    //  instance varables
    private char[][] board;                     //  2D array of char representing game board
    private int totalMoves;                     //  total moves played
    private boolean boardContainsX;             //  board presence of x pieces
    private boolean boardContainsO;             //  board presence of o pieces
    private boolean playerXHasValidMoves;       //  whether x pieces have valid moves
    private boolean playerOHasValidMoves;       //  whether o pieces have valid moves
    private char currentPlayerTurn;             //  represents currrent turn as char
    private CheckersGameMove currentTurn;       //  stores pre and post coordinates
    private Scanner scanner;                    //  reads input
    private char gameStyle;                     //  determines PvP or PvC
    private CheckersComputerPlayer computer;    //  computer player

    /**
     * Constructor for CheckersGameLogic, sets up the game board
     * 
     * @param scanner object to read input
     */
    public CheckersGameLogic(Scanner scanner)
    {
        //  assign variable values
        this.scanner = scanner;
        currentPlayerTurn = 'x';
        currentTurn = null;
        totalMoves = 0;
        computer = null;
        gameStyle = ' ';

        //  setup the board
        setupBoard();
    }
    
    /**
     * Retrieves the content at the specified position of the board
     * with getSquare(0,0) representing the bottom left of the board
     * 
     * @param row index of position
     * @param column index of position
     * @throws IndexOutOfBoundsException if provided position is not valid
     * @return the char content at the specified position
     */
    public char getSquare(int row, int column) throws IndexOutOfBoundsException
    {
        //  check if provided indices are in range
        if(row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE)
        {
            throw new IndexOutOfBoundsException("("+row+","+column+") is not a valid index.");
        }

        return board[BOARD_SIZE - row - 1][column];
    }

    /**
     * Sets the content at the specified position of the board to the provided content
     * with setSquare(0,0,...) representing the bottom left of the board
     * 
     * @param row index of position
     * @param column index of position
     * @param content char to be placed
     * @throws IndexOutOfBoundsException if given index is not in range
     * @throws IllegalArgumentException if provided content is not a valid piece or empty square
     */
    public void setSquare(int row, int column, char content) 
        throws IndexOutOfBoundsException, IllegalArgumentException
    {
        if(row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE) 
        {
            throw new IndexOutOfBoundsException("("+row+","+column+") is not a valid index.");
        }
        if(content != 'o' && content != 'x' && content != '_')
        {
            throw new IllegalArgumentException("Content does not match: 'o', 'x', or '_'.");
        }

        board[BOARD_SIZE - row - 1][column] = content;
    }

    /**
     * Returns a value representing the winning player
     * 
     * @return 'x' if player x has won, 'o' if player o has won, 
     *  otherwise returns '_'
     */
    public char getWinningPlayer()
    {
        //  default winning conditions
        boardContainsO = false;
        boardContainsX = false;
        playerOHasValidMoves = false;
        playerXHasValidMoves = false;

        //  iterate through the checkerboard
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                // check if there are 'o' pieces and if they can move
                if(getSquare(row, col) == 'o')
                {
                    boardContainsO = true;

                    if(generateValidMoves(row, col) != null)
                    {
                        playerOHasValidMoves = true;
                    }
                }

                // check if there are 'x' pieces and if they can move
                if(getSquare(row, col) == 'x')
                {
                    boardContainsX = true;

                    if(generateValidMoves(row, col) != null)
                    {
                        playerXHasValidMoves = true;
                    }
                }
                
            }
        }

        //  return 'x' if x has moves & pieces and o doesnt have moves or pieces
        if((boardContainsX || playerXHasValidMoves) && !(boardContainsO && playerOHasValidMoves))
        {
            return 'x';
        }

        //  return 'o' if o has moves & pieces and x doesnt have moves or pieces
        if((boardContainsO || playerOHasValidMoves) && !(boardContainsX && playerXHasValidMoves))
        {
            return 'o';
        }

        //  return the active player if there are no pieces on the board
        if(!(boardContainsX && playerXHasValidMoves) && !(boardContainsO && playerOHasValidMoves))
        {
            return currentPlayerTurn;
        }

        return '_';
    }

    /**
     * Switches the turn to the non-active player's turn.
     * If it is 'x' turn, then it switches to 'o' turn and vice versa.
     */
    public void nextTurn()
    {
        if(currentPlayerTurn == 'x')
        {
            currentPlayerTurn = 'o';
        }
        else
        {
            currentPlayerTurn = 'x';
        }
    }

    /**
     * Handles turns for CheckersGUI
     * 
     * @param turn to be made
     * @throws IllegalStateException if inputted turn is not valid
     * @return String representing valid turn that was made
     */
    public String handleTurn(String turn) throws IllegalStateException
    {
        //  check if gameStyle was set
        if(gameStyle == ' ') return null;

        //  initalize computer
        if(gameStyle == 'c' && totalMoves == 0) computer = new CheckersComputerPlayer(this);

        //  read player turn
        try 
        {
            //  compute player or computer move
            if(currentPlayerTurn == 'o' && gameStyle == 'c')
                currentTurn = new CheckersGameMove(computer.computeMove());
            else
                currentTurn = new CheckersGameMove(turn);

            //  exit method early if not valid
            if(!validateMove()) throw new IllegalStateException("Not a valid move");

            doPlayerTurn();

            totalMoves++;

            return turn;
        }
        catch(IllegalStateException ex)
        {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /**
     * Handles the next player's turn and updates the board accordingly.
     */
    public void doTurn()
    {
        //  print message at beginning of game
        if(totalMoves == 0)
        {
            System.out.println("Begin Game. Enter \'P\' if you want to play against another player; " + 
                "enter \'C\' to play against computer.");

            switch(scanner.nextLine().trim().toUpperCase())
            {
                case "P":
                    gameStyle = 'p';
                    break;
                case "C":
                    gameStyle = 'c';
                    break;
                default:
                    gameStyle = 'c';
            }

            if(gameStyle == 'c')
            {
                System.out.println("Start game against computer. You are Player X and Computer is Player O.");
                computer = new CheckersComputerPlayer(this);
            }
            
            System.out.print("Begin Game. ");           
        }

        //  print prompt message
        System.out.println("Player " + Character.toUpperCase(currentPlayerTurn) + " - your turn.");
        System.out.println("Choose a cell position of piece to be moved " + 
            "and the new position. e.g., 3a-4b");
        boolean isValid = false;

        //  check if it is computer turn
        if(currentPlayerTurn == 'o' && gameStyle == 'c')
        {   
            String computerMove = "unknown";

            try
            {
                //  get the computers move
                computerMove = computer.computeMove();
                
                //  output the computers move
                System.out.println(computerMove);

                //  read computers move
                currentTurn = new CheckersGameMove(computerMove);
            }
            catch(IllegalStateException ex)
            {
                throw new IllegalStateException("Game continued when a player should've won: " + ex.getMessage());
            }
            catch(IllegalArgumentException ex)
            {
                throw new IllegalArgumentException("Computer's inputted move was not formatted correctly: " + ex.getMessage());
            }

            //  update the board
            doPlayerTurn();

            return;
        }

        //  loop until player enters a valid move
        do
        {
            try
            {
                //  read the input and validate the move
                String s = scanner.nextLine();        
                currentTurn = new CheckersGameMove(s.trim());
                isValid = validateMove();

                if(!isValid)
                {
                    System.out.println("Inputted move is not valid. Try again.");
                }
            }
            catch(IllegalArgumentException ex)
            {
                System.out.println(ex.getMessage());
            }
            catch(IllegalStateException ex)
            {
                System.out.println(ex.getMessage());
            }
        } while(!isValid);
        
        doPlayerTurn(); //  update the board

        totalMoves++;
    }

    /**
     * Handles the end of the game and resets game to original state
     */
    public void onEnd()
    {
        System.out.println("\nPlayer " + Character.toUpperCase(currentPlayerTurn) + 
            " Won the Game");
        System.out.println("Total Turns: " + totalMoves);
        
        // reset the game
        setupBoard();
        currentPlayerTurn = 'x';
        totalMoves = 0;
    }

    /**
     * Setups the starting position of all pieces on the board
     */
    private void setupBoard()
    {
        board = new char[BOARD_SIZE][BOARD_SIZE];

        /*
         * Iterate through the entire board
         * all pieces are either on even indices or odd indices
         */
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                if((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0))
                {
                    if(row < 3)
                    {
                        setSquare(row, col, 'x');
                    }
                    else if(row > 4)
                    {
                        setSquare(row, col, 'o');   
                    }
                    else 
                    {
                        setSquare(row, col, '_');
                    }
                }
                else 
                {
                    setSquare(row, col, '_');
                }
            }
        }
    }

    /**
     * Generates an String array of valid new positions based on the 
     * provided position
     * 
     * @param row index of specified position
     * @param column index of specified position
     * @return String array of valid moves in the format of [row],[col] or null
     *  if the specified position is not valid or does not have possible moves
     */
    private String[] generateValidMoves(int row, int col)
    {
        //  is the specified square on the board?
        if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) 
        {
            return null;
        }

        char playerPiece = getSquare(row, col);

        if(getSquare(row, col) == '_')
        {
            return null;
        }

        String validMoves = "";
        char piece; // temp variable

        //  check what piece is at the position
        if(playerPiece == 'x') 
        {
            //  is the piece at the furthest point of the board?
            if(row == BOARD_SIZE - 1) 
            {
                return null;
            }

            //  generate left diagonal moves
            if(col - 1 >= 0 && row + 1 < BOARD_SIZE)
            {
                piece = getSquare(row+1, col-1);

                /*
                 * Is the left diagonal empty?
                 * If it isn't empty, is there an opponent piece there 
                 *  and is the square diagonal to the opponent piece empty?
                 */
                if(piece == '_') 
                {
                    validMoves += (row + 1) + "," + (col - 1) + "\n";
                }
                else if(
                    piece == 'o' && 
                    row + 2 < BOARD_SIZE && 
                    col - 2 >= 0 && 
                    getSquare(row+2,col-2) == '_')
                {
                    validMoves += (row + 2) + "," + (col - 2) + "\n";
                }
            }
            
            //  generate right diagonal moves
            if(col + 1 < BOARD_SIZE && row + 1 < BOARD_SIZE)
            {
                piece = getSquare(row+1, col+1);

                /*
                 * Is the right diagonal empty?
                 * If it isn't empty, is there an opponent piece there 
                 * and is the square diagonal to the opponent piece empty?
                 */
                if(piece == '_')
                {
                    validMoves += (row + 1) + "," + (col + 1) + "\n";
                }
                else if(
                    piece == 'o' && 
                    row + 2 < BOARD_SIZE && 
                    col + 2 < BOARD_SIZE 
                    && getSquare(row+2,col+2) == '_')
                {
                    validMoves += (row + 2) + "," + (col + 2) + "\n";
                }
            }

            if(validMoves.length() == 0)
            {
                return null;
            }

            return validMoves.split("\n");
        }
        else if(playerPiece == 'o')
        {
            //  is the piece at the furthest point of the board?
            if(row == 0) 
            {
                return null;
            }

            //  check right diagonal moves
            if(col - 1 >= 0 && row - 1 >= 0)    
            {
                piece = getSquare(row-1, col-1);
                
                /*
                 * Is the right diagonal empty?
                 * If it isn't empty, is there an opponent piece there 
                 *  and is the square diagonal to the opponent piece empty?
                 */
                if(piece == '_')    // is the right diagonal empty?
                {
                    validMoves += (row - 1) + "," + (col - 1) + "\n";
                }
                else if(
                    piece == 'x' && 
                    row -2 >= 0 && 
                    col - 2 >= 0 && 
                    getSquare(row-2,col-2) == '_')
                {
                    validMoves += (row - 2) + "," + (col - 2) + "\n";
                }
            }

            //  check left diagonal moves
            if(col + 1 < BOARD_SIZE && row - 1 >= 0)
            {
                piece = getSquare(row-1, col+1);

                /*
                 * Is the left diagonal empty?
                 * If it isn't empty, is there an opponent piece there 
                 *  and is the square diagonal to the opponent piece empty?
                 */
                if(piece == '_')    // is the left diagonal empty?
                {
                    validMoves += (row - 1) + "," + (col + 1) + "\n";
                }
                else if(
                    piece == 'x' && 
                    row - 2 >= 0 && 
                    col + 2 < BOARD_SIZE && 
                    getSquare(row-2,col+2) == '_')
                {
                    validMoves += (row - 2) + "," + (col + 2) + "\n";
                }
            }

            if(validMoves.length() == 0)
            {
                return null;
            }
            
            return validMoves.split("\n");
        }
        
        return null;    //  specified location is empty or invalid
    }

    /**
     * Performs the players turn and updates the board accordingly
     * Assumes player's turn has already been validated
     */
    private void doPlayerTurn()
    {
        //  calculate change in position
        int rowChange = Math.abs(currentTurn.getPreRow() - currentTurn.getPostRow());
        int colChange = Math.abs(currentTurn.getPreCol() - currentTurn.getPostCol());

        if(rowChange == 1 && colChange == 1)    // piece is being moved
        {
            //  clear current position and update new position
            setSquare(currentTurn.getPreRow(), currentTurn.getPreCol(), '_');
            setSquare(currentTurn.getPostRow(), currentTurn.getPostCol(), currentPlayerTurn);
        }
        else if(rowChange == 2 && colChange == 2)   //  piece is taking another piece
        {
            setSquare(currentTurn.getPreRow(), currentTurn.getPreCol(), '_');
            setSquare(
                (currentTurn.getPreRow() + currentTurn.getPostRow()) / 2, 
                (currentTurn.getPreCol() + currentTurn.getPostCol()) / 2, 
                '_'
            );
            setSquare(currentTurn.getPostRow(), currentTurn.getPostCol(), currentPlayerTurn);
        }
    
    }

    /**
     * Validates the current players move, assuming they made a move
     * 
     * @throws IllegalStateException if the current player attempted to move an opponents piece
     * @return boolean representing if the move is valid or not
     */
    private boolean validateMove() throws IllegalStateException
    {
        if(currentTurn == null)
        {
            throw new IllegalStateException("You have not made a move yet!");
        }
        
        //  generate valid moves based on current position
        String[] validMoves = generateValidMoves(
            currentTurn.getPreRow(), currentTurn.getPreCol());

        //  check if player has valid moves and that player is moving their own piece
        if(validMoves != null)
        {
            if(getSquare(currentTurn.getPreRow(), currentTurn.getPreCol()) == currentPlayerTurn)
            {
                for(String move : validMoves)
                {      
                    //  is the player move valid?
                    if(currentTurn.getPostCoordinates().equals(move)) 
                    {   
                        return true;
                    }
                }
            }
            else
            {
                throw new IllegalStateException("You cannot move another player's piece!");
            }
        }
        
        return false;
    }

    /**
     * Sets the game style of the game
     * @param gameStyle of game, acceptable values are 'p' and 'c'
     */
    public void setGameStyle(char gameStyle)
    {
        if(gameStyle == 'c' || gameStyle == 'p')
            this.gameStyle = gameStyle;
    }

    /**
     * Retrieves the game style of the game
     * @return char representing gameStyle
     */
    public char getGameStyle()
    {
        return gameStyle;
    }

    /**
     * Retrieves the current players turn
     * @return
     */
    public char getCurrentTurn()
    {
        return currentPlayerTurn;
    }

    /**
     * Represents a possible move a player can make on the checker board
     * 
     * @author Felix
     * @version 1.0
     */
    private class CheckersGameMove
    {
        //  instance member variables
        private int preRow;
        private int preCol;
        private int postRow;
        private int postCol;

        /**
         * Constructor for CheckersGameMove which takes inputs in the form of "3a-4b"
         * 
         * @throws IllegalArgumentException if the inputted String does not match "3a-4b" format
         * @param String representing the move to be made
         */
        public CheckersGameMove(final String move) throws IllegalArgumentException
        {
            /*
             * Check for
             * 1) is the pass string not null?
             * 2) is the length of the string 5 letters?
             * 3) does the middle of the string contain a dash?
             * 4) are the characters at index 0 and 3 numbers?
             * 5) are both the numbers between the range of 1 to 8?
             * 6) are the letters are index 1 and 4 letter?
             * 7) are the letters lowercase?
             * 6) are the characters between a and h?
             */
            if(move != null && move.length() == 5 && move.charAt(2) == '-' && 
            Character.isDigit(move.charAt(0)) && Character.isDigit(move.charAt(3)) &&
            Integer.valueOf(move.substring(0,1)) > 0 && Integer.valueOf(move.substring(0,1)) < 9 &&
            Integer.valueOf(move.substring(3,4)) > 0 && Integer.valueOf(move.substring(3,4)) < 9 &&
            Character.isLetter(move.charAt(1)) && Character.isLowerCase(move.charAt(1)) && 
            Integer.valueOf(move.charAt(1)) >= 97 && Integer.valueOf(move.charAt(1)) <= 104 &&
            Character.isLetter(move.charAt(4)) && Character.isLowerCase(move.charAt(4)) &&
            Integer.valueOf(move.charAt(4)) >= 97 && Integer.valueOf(move.charAt(4)) <= 104)
            {
                preRow = Integer.valueOf(move.substring(0,1)) - 1;
                preCol = charToNumber(move.charAt(1));
                postRow = Integer.valueOf(move.substring(3,4)) - 1;
                postCol = charToNumber(move.charAt(4));
            }
            else
            {
               throw new IllegalArgumentException(
                "Input does not match appropiate format: \"[1-8][a-h]-[1-8][a-h]\".");
            }
        }

        /**
         * Retrieves the row index of the piece location
         * 
         * @return row index of piece
         */
        public int getPreRow()
        {
            return preRow;
        }

        /**
         * Retrieves the column index of the piece location
         * 
         * @return column index of piece
         */
        public int getPreCol()
        {
            return preCol;
        }

        /**
         * Retrieves the row index of the target position
         * 
         * @return row index of target
         */
        public int getPostRow()
        {
            return postRow;
        }

        /**
         * Retrieves the column index of the target position
         * 
         * @return column index of target
         */
        public int getPostCol()
        {
            return postCol;
        }

        /**
         * Retrieves the post position indices of move
         * 
         * @return post move position in the form of "[row],[col]"
         */
        public String getPostCoordinates()
        {
            return postRow+","+postCol;
        }

        /**
         * Converts the given column letter index into an integer index
         * 
         * @param letter value to be converted
         * @return int representing column index
         */
        private int charToNumber(char letter)
        {
            switch(letter)
            {
                case 'a':
                    return 0;
                case 'b':
                    return 1;
                case 'c':
                    return 2;
                case 'd':
                    return 3;
                case 'e':
                    return 4;
                case 'f':
                    return 5;
                case 'g':
                    return 6;
                case 'h':
                    return 7;
                default:
                    return -1;
            }
        }

        /**
         * Used for debugging purposes
         * 
         * @return String representation of CheckersGameMove
         */
        @Override public String toString()
        {
            return preRow+","+preCol+"->"+postRow+","+postCol;
        }
    }
}
