package edu.ser216.checkers.core;

/**
 * Core logic for Checkers computer player
 * 
 * @author Felix Leong
 * @version 1.0
 */
public class CheckersComputerPlayer 
{
    //  Member variables
    private final int BOARD_SIZE = 8;                   //  size of game board
    private final int MAX_POSSIBLE_MOVES = 2;           //  maximum possible moves for a piece
    private Move computedMove;                          //  checkers move to be played
    private double weightScore;                         //  weighted score of move
    private CheckersGameLogic game;                     //  reference to game
    private final double ENDANGERMENT_WEIGHT = 0.5;     //  endangerment category weight
    private final double POSITIONAL_WEIGHT = 0.1;       //  positional category weight
    private final double PROTECTION_WEIGHT = 0.4;       //  protection category weight
    private final double JUMPING_WEIGHT = 0.4;          //  jumping category weight

    /**
     * Constructor for CheckersComputerPlayer
     * @param game object reference to game logic
     */
    public CheckersComputerPlayer(final CheckersGameLogic game)
    {
        this.game = game;
    }

    /**
     * Computes the AI's move based on the board
     * @throws IllegalStateException if the AI cannot make a move
     * @return String representation of AI's move
     */
    public String computeMove() throws IllegalStateException
    {        
        //  default values
        computedMove = null;
        weightScore = -1;

        //  loop through every square
        for(int r = BOARD_SIZE - 1; r >= 0; r--)
        {
            for(int c = 0; c < BOARD_SIZE; c++)
            {
                if(game.getSquare(r, c) == 'o')
                {
                    Move[] moves = generateValidMoves(r, c);

                    if(moves == null)
                    {
                        continue;
                    }

                    for(Move move : moves)
                    {
                        //  skip score computation if move doesnt exist
                        if(move == null)
                        {
                            continue;
                        }
                        
                        //  compute score
                        double score = computeWeightScore(move);

                        //  update move if move weight is larger
                        if(score > weightScore)
                        {
                            computedMove = move;
                            weightScore = score;
                        }
                    }
                }
            }
        }

        if(computedMove == null)
        {
            throw new IllegalStateException("No available moves to make.");
        }

        return computedMove.toString();
    }
    
    /**
     * Formula calculation for move weight
     * Calculation based on 4 factors:
     *  - Distance from center of the board                                 (category weight: 10%)
     *  - Whether the move results in danger                                (category weight: 50%)
     *  - Whether the move results in protecting a piece from being jumped  (category weight: 40%)
     *  - If the move results in jumping or sets up a jump                  (category weight: 40%)
     * @param move containing original positions and next positions
     * @return double representing total weighted score
     */
    private double computeWeightScore(Move move)
    {
        return ENDANGERMENT_WEIGHT * weighEndangerment(move) +
            POSITIONAL_WEIGHT * weighPosition(move.nextRow, move.nextCol) +
            PROTECTION_WEIGHT * weighProtection(move) +
            JUMPING_WEIGHT * weighJumping(move);
    }

    /**
     * Calculates the endangerment value of move
     * @param move to be made
     * @return double representing endangerment score which can be 0 or 100
     */
    private double weighEndangerment(Move move)
    {
        return isEndangered(move) ? 0 : 100;
    }

    /**
     * Checks if a move results in endangerment
     * @param move to be made
     * @return true if piece is in danger, false otherwise
     */
    private boolean isEndangered(Move move)
    {
        //  check if piece is at end or beginning of board or check if a piece is along the wall
        if(move.nextRow == 0 || move.nextRow == BOARD_SIZE - 1 || move.nextCol == 0 || move.nextCol == BOARD_SIZE - 1)
        { 
            return false;
        }

        //  check if in danger from left diagonal
        if(game.getSquare(move.nextRow-1, move.nextCol-1) == 'x' && (
            (move.currRow == move.nextRow+1 && move.currCol == move.nextCol+1) ||
            game.getSquare(move.nextRow+1, move.nextCol+1) == '_'))
        {
            return true;
        }

        //  check if in danger from right diagonal
        if(game.getSquare(move.nextRow-1, move.nextCol+1) == 'x' && (
            (move.currRow == move.nextRow+1 && move.currCol == move.nextCol-1) ||
            game.getSquare(move.nextRow+1, move.nextCol-1) == '_'))
        {
            return true;
        }

        return false;
    }
    
    /**
     * Calculates the positional value of move
     * @param row of next move
     * @param col of next move
     * @return double representing positional score from 0 to 100
     */
    private double weighPosition(int row, int col)
    {
        return 100 * (calculatePositionScore(row) + calculatePositionScore(col));
    }

    /**
     * Calculates the individual score for the index
     * based on distance from center using predetermined values
     * @param index to be scored
     * @return double representing value index
     */
    private double calculatePositionScore(int index)
    {
        switch(index)
        {
            case 3: case 4:
                return .5;
            case 2: case 5:
                return .35;
            case 1: case 6:
                return .2;
            case 0: case 7:
                return .05;
            default:
                return 0;
        }
    }
 
    /**
     * Calculates the jumping value of move
     * @param move to be made
     * @return double representing jumping score which can be 0 or 100
     */
    private double weighJumping(Move move)
    {
        return canJump(move) ? 100 : 0;
    }

    /**
     * Checks if the current move is jumping or if the piece can move to a position where 
     * it can jump another piece
     * @param move to be made
     * @return true if it can jump within 2 moves, otherwise false
     */
    private boolean canJump(Move move)
    {    
        //  calculate move distance
        int rowChange = Math.abs(move.nextRow - move.currRow);
        int colChange = Math.abs(move.nextCol - move.currCol);

        //  check if move is already jumping
        if(rowChange == 2 && colChange == 2)
            return true;

        //  check if possible to jump
        if(move.nextRow <= 1)
            return false;

        //  check if forwawrd left diagonal results in a jump
        if(move.nextRow-2 >= 0 && move.nextCol-2 >= 0 &&
            game.getSquare(move.nextRow-1, move.nextCol-1) == 'x' &&
            game.getSquare(move.nextRow-2, move.nextCol-2) == '_')
        {
            return true;
        }

        //  check if forwawrd right diagonal results in a jump
        if(move.nextRow-2 >= 0 && move.nextCol+2 < BOARD_SIZE &&
            game.getSquare(move.nextRow-1, move.nextCol+1) == 'x' &&
            game.getSquare(move.nextRow-2, move.nextCol+2) == '_')
        {
            return true;
        }

        return false;
    }

    /**
     * Calculates the protection value of move
     * where protection results in preventing a piece from being jumped from a certain angle
     * @param move to be made
     * @return double representing protection score which can be 0 or 100
     */
    private double weighProtection(Move move)
    {
        return canProtect(move) ? 100 : 0;
    }

    /**
     * Checks if a current move can protect a piece
     * @param move to be made
     * @return true if a piece can be protected, false otherwise
     */
    private boolean canProtect(Move move)
    {
        //  check if protected piece can even be jumped
        if(move.nextRow == 0 || move.nextCol - 1 < 0 || move.nextCol + 1 >= BOARD_SIZE)    
        {
            return false;
        }
        
        //  check if there is a piece to be protected
        if(game.getSquare(move.nextRow-1, move.nextCol-1) == 'o' || 
            game.getSquare(move.nextRow-1, move.nextCol+1) == 'o')
        {
            return true;
        }
            

        return false;
    }

    /**
     * Generates a set of valid moves given the indices of the piece
     * Assumes there is a friendly piece ('o') at specified index
     * @param row index
     * @param col index
     * @return array sized 2 of class Move representing possible moves
     */
    private Move[] generateValidMoves(int row, int col)
    {
        //  can piece be moved?
        if(row == 0)
        {
            return null;
        }

        char piece;
        Move[] possibleMoves = new Move[MAX_POSSIBLE_MOVES];
        int index = 0;

        //  check left diagonal moves
        if(col - 1 >= 0 && row - 1 >= 0)    
        {
            piece = game.getSquare(row-1, col-1);

            if(piece == '_')  
            {
                possibleMoves[index++] = new Move(row, col, row-1, col-1);
            }
            else if(
                piece == 'x' && 
                row -2 >= 0 && 
                col - 2 >= 0 && 
                game.getSquare(row-2,col-2) == '_')
            {
                possibleMoves[index++] = new Move(row, col, row-2, col-2);
            }
        }

        //  check right diagonal moves
        if(col + 1 < BOARD_SIZE && row - 1 >= 0)
        {
            piece = game.getSquare(row-1, col+1);

            if(piece == '_')
            {
                possibleMoves[index++] = new Move(row, col, row-1, col+1);
            }
            else if(
                piece == 'x' && 
                row - 2 >= 0 && 
                col + 2 < BOARD_SIZE && 
                game.getSquare(row-2,col+2) == '_')
            {
                possibleMoves[index++] = new Move(row, col, row-2, col+2);
            }
        }
        
        return possibleMoves;
    }
    
    /**
     * Class representation of a move
     * 
     * @author Felix Leong
     * @version 1.0
     */
    private class Move
    {
        public int currRow;
        public int currCol;
        public int nextRow;
        public int nextCol;

        /**
         * Constructor for Move
         * @param currRow
         * @param currCol
         * @param nextRow
         * @param nextCol
         */
        public Move(int currRow, int currCol, int nextRow, int nextCol)
        {
            this.currRow = currRow;
            this.currCol = currCol;
            this.nextRow = nextRow;
            this.nextCol = nextCol;
        }

        /**
         * String represention in the form of "3a-4b"
         */
        @Override public String toString()
        {
            return "" + (currRow + 1) + numToChar(currCol) + "-" + (nextRow + 1) + numToChar(nextCol);
        }

        /**
         * Converts number to corresponding character
         * @param num to be converted
         * @return char value between 'a' and 'h', returns '-' if input is invalid
         */
        private char numToChar(int num)
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
                    return '-';
            }
        }
    }
}
