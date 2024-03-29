/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.ser216.checkers.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.junit.jupiter.api.RepeatedTest;

class CheckersGameLogicTest
{
    ByteArrayInputStream input;
    Scanner scanner;
    CheckersGameLogic game;

    public CheckersGameLogicTest()
    {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
    }

    @Test void testGetSquare_ValidIndices()
    {
        game = new CheckersGameLogic(scanner);
        assertEquals('x', game.getSquare(1, 1));
        assertEquals('o', game.getSquare(7, 7));
    }
    
    @Test void testGetSquare_OutOfRangeIndices()
    {
        game = new CheckersGameLogic(scanner);
        assertThrows(IndexOutOfBoundsException.class, () -> game.getSquare(11, 11));
    }

    @Test void testGetSquare_NegativeIndices()
    {
        game = new CheckersGameLogic(scanner);
        assertThrows(IndexOutOfBoundsException.class, () -> game.getSquare(-20, -20));
    }

    @RepeatedTest(3) void testSetSquare_ValidIndices()
    {
        game = new CheckersGameLogic(scanner);
        int r = (int) (Math.random()*8);
        int c = (int) (Math.random()*8);
        char ch = '_';
        game.setSquare(r, c, ch);
        assertEquals(ch, game.getSquare(r, c));
    }

    @RepeatedTest(3) void testSetSquare_OutOfRangeIndices()
    {
        game = new CheckersGameLogic(scanner);
        int r = (int) (Math.random()*10)+8;
        int c = (int) (Math.random()*10)+8;
        assertThrows(IndexOutOfBoundsException.class, () -> game.setSquare(r, c, '_'));
    }

    @Test void testSetSquare_NegativeIndices()
    {
        game = new CheckersGameLogic(scanner);
        assertThrows(IndexOutOfBoundsException.class, () -> game.setSquare(-1, -1, '_'));
    }

    @Test void testSetSquare_ValidCharacter()
    {
        game = new CheckersGameLogic(scanner);
        game.setSquare(1, 1, 'o');
        assertEquals('o', game.getSquare(1, 1));
        game.setSquare(4, 3, 'x');
        assertEquals('x', game.getSquare(4, 3));
        game.setSquare(7, 2, '_');
        assertEquals('_', game.getSquare(7, 2));
    }

    @Test void testSetSquare_InvalidCharacter()
    {
        game = new CheckersGameLogic(scanner);
        assertThrows(IllegalArgumentException.class, () -> game.setSquare(2, 2, 'a'));
        assertThrows(IllegalArgumentException.class, () -> game.setSquare(2, 2, '3'));
        assertThrows(IllegalArgumentException.class, () -> game.setSquare(2, 2, 'X'));
    }

    @Test void testGetWinningPlayer_StartingBoard()
    {
        game = new CheckersGameLogic(scanner);
        assertEquals('_', game.getWinningPlayer());
    }

    @Test void testGetWinningPlayer_NoPieces()
    {
        game = new CheckersGameLogic(scanner);
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                game.setSquare(r, c, '_');
        game.setSquare(2, 2, 'o');
        assertEquals('o', game.getWinningPlayer());

        game.setSquare(2,2,'x');
        assertEquals('x', game.getWinningPlayer());
    }

    @Test void testGetWinningPlayer_NoMoves()
    {
        game = new CheckersGameLogic(scanner);
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                if(r == 7) 
                    game.setSquare(r, c, 'x');    
                else 
                    game.setSquare(r, c, '_');
        game.setSquare(2, 4, 'o');
        assertEquals('o', game.getWinningPlayer());

        for(int c = 0; c < 8; c++)
        {
            game.setSquare(7, c, '_');
            game.setSquare(0, c, 'o');
        }
        game.setSquare(2, 4, '_');
        game.setSquare(4, 3, 'x');
        assertEquals('x', game.getWinningPlayer());
    }

    @Test void testGetWinningPlayer_EmptyBoard()
    {
        game = new CheckersGameLogic(scanner);
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                game.setSquare(r, c, '_');
        assertEquals('x', game.getWinningPlayer());
    }

    @Test void testGetWinningPlayer_InvalidBoards()
    {
        game = new CheckersGameLogic(scanner);
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                if(r < 4)    
                    game.setSquare(r, c, 'x');
                else
                    game.setSquare(r, c, 'o');
        assertEquals('x', game.getWinningPlayer());
    }
    
    @Test void testDoTurn_ValidGameStyle()
    {
        String userInput = "c\n3a-4b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);

        game.doTurn();

        String[] s = out.toString().split("\n");

        assertTrue(s[1].contains("Start game against computer."));
        
    } 

    @Test void testDoTurn_InvalidGameStyle()
    {
        String userInput = "vvv\n3a-4b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        
        game.doTurn();

        String[] s = out.toString().split("\n");

        assertTrue(s[1].contains("Start game against computer."));
    }

    @Test void testDoTurn_ValidInput()
    {
        String userInput = "p\n3a-4b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        
        game.doTurn();

        String[] s = out.toString().split("\n");

        assertNotEquals("Inputted move is not valid. Try again.", s[s.length-1]);
    }

    @Test void testDoTurn_InvalidInput()
    {
        String userInput = "p\n4a-6b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        
        assertThrows(NoSuchElementException.class, () -> game.doTurn());
    }

    @Test void testDoTurn_ImproperlyFormattedInput()
    {
        String userInput = "p\n3a_4b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        
        assertThrows(NoSuchElementException.class, () -> game.doTurn());
    }

    @Test void testDoTurn_JumpingMove1()
    {
        String userInput = "p\n3a-5c";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        game.setSquare(3, 1, 'o');

        game.doTurn();

        String[] s = out.toString().split("\n");

        assertNotEquals("Inputted move is not valid. Try again.", s[s.length-1]);

        assertEquals('_', game.getSquare(2, 0));
        assertEquals('_', game.getSquare(3, 1));
        assertEquals('x', game.getSquare(4, 2));
    }

    @Test void testDoTurn_JumpingMove2()
    {
        String userInput = "p\n3c-5a";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        game.setSquare(3, 1, 'o');

        game.doTurn();

        String[] s = out.toString().split("\n");

        assertNotEquals("Inputted move is not valid. Try again.", s[s.length-1]);

        assertEquals('_', game.getSquare(2, 2));
        assertEquals('_', game.getSquare(3, 1));
        assertEquals('x', game.getSquare(4, 0));
    }

    @Test void testDoTurn_MovingOpponentPiece()
    {
        String userInput = "p\n6c-5b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);
        
        assertThrows(NoSuchElementException.class, () -> game.doTurn());
    }

    @Test void testDoTurn_GenerateComputerMove()
    {
        String userInput = "c\n3a-4b";
        InputStream input = new ByteArrayInputStream(userInput.getBytes());
        Scanner custom = new Scanner(input);
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(custom);

        game.doTurn();
        game.nextTurn();
        game.doTurn();

        CheckersGameLogic g = new CheckersGameLogic(scanner);

        boolean changed = false;

        for(int r = 4; r < 8; r++)
            for(int c = 0; c < 8; c++)
                if(g.getSquare(r, c) != game.getSquare(r, c)) changed = true;
        
        assertTrue(changed);
    }

    @Test void testHandleTurn_NoGameStyle()
    {
        game = new CheckersGameLogic(scanner);
        assertNull(null, game.handleTurn(""));
    }

    @Test void testHandleTurn_ValidInput()
    {
        game = new CheckersGameLogic(scanner);
        game.setGameStyle('p');
        
        assertNotNull(game.handleTurn("3a-4b"));
    }

    @Test void testHandleTurn_InvalidInput()
    {
        game = new CheckersGameLogic(scanner);
        game.setGameStyle('p');
        
        assertThrows(IllegalStateException.class, () -> game.handleTurn("3a-3a"));
    }

    @Test void testHandleTurn_ImproperFormat()
    {
        game = new CheckersGameLogic(scanner);
        game.setGameStyle('p');
        
        assertThrows(IllegalArgumentException.class, () -> game.handleTurn("3a9b"));
    }

    @Test void testSetGameStyle_InvalidInput()
    {
        game = new CheckersGameLogic(scanner);
        
        char g = game.getGameStyle();
        game.setGameStyle('3');
        assertEquals(g, game.getGameStyle());
    }

    @Test void testOnEnd()
    {
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game = new CheckersGameLogic(scanner);
        game.onEnd();

        assertTrue(!out.toString().isEmpty());

        CheckersGame g = new CheckersGameLogic(scanner);

        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                assertEquals(g.getSquare(r, c), game.getSquare(r, c));
        
        assertEquals('x', game.getCurrentTurn());
    }

    @Test void testNextTurn()
    {
        game = new CheckersGameLogic(scanner);
        assertEquals('x', game.getCurrentTurn());
        game.nextTurn();
        assertEquals('o', game.getCurrentTurn());
        game.nextTurn();
        assertEquals('x', game.getCurrentTurn());
    }
}
