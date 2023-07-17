package edu.ser216.checkers.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckersComputerPlayerTest {
    CheckersComputerPlayer cpu;
    CheckersGameLogic game;

    @BeforeEach void refresh()
    {
        game = new CheckersGameLogic(new Scanner(System.in));
    }

    @Test void testComputeMove_NoMoves()
    {
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                game.setSquare(r,c,'_');
        cpu = new CheckersComputerPlayer(game);
        assertThrows(IllegalStateException.class, () -> cpu.computeMove());
    }

    @Test void testComputeMove_RegularMoves()
    {
        char[][] board = {
            {'o','_','_','_','_','_','_','o'},
            {'_','_','_','_','o','_','_','_'},
            {'_','o','_','_','_','_','o','_'},
            {'x','_','x','_','_','x','_','_'},
            {'_','_','_','_','o','_','_','_'},
            {'_','o','_','o','_','x','_','_'},
            {'o','_','o','_','x','_','_','_'},
            {'o','_','_','_','_','o','_','o'},
        };
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
            game.setSquare(8-r-1, c, board[r][c]);
        cpu = new CheckersComputerPlayer(game);
        assertDoesNotThrow(() -> cpu.computeMove());
    }

    
    @Test void testComputeMove_JumpingMoves()
    {
        char[][] board = {
            {'o','_','_','_','_','_','_','o'},
            {'_','x','_','_','_','_','x','_'},
            {'_','_','_','_','_','_','o','_'},
            {'_','o','_','_','_','_','_','x'},
            {'x','_','o','_','_','_','_','_'},
            {'_','x','_','x','_','_','_','o'},
            {'o','_','_','_','_','_','x','_'},
            {'_','_','_','_','_','_','_','_'},
        };
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                game.setSquare(8-r-1, c, board[r][c]);
        cpu = new CheckersComputerPlayer(game);
        assertDoesNotThrow(() -> cpu.computeMove());
    }

    @Test void testComputeMove_MiscMoves()
    {
        char[][] board = {
            {'_','o','o','o','_','_','_','_'},
            {'_','_','o','_','_','_','_','_'},
            {'o','_','_','_','x','_','_','_'},
            {'_','_','_','o','o','o','_','_'},
            {'_','_','_','_','_','x','_','_'},
            {'_','_','x','_','o','_','_','_'},
            {'_','_','_','_','_','o','_','_'},
            {'_','_','_','_','_','_','x','_'},
        };
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                game.setSquare(8-r-1, c, board[r][c]);
        cpu = new CheckersComputerPlayer(game);

        
    }

}
