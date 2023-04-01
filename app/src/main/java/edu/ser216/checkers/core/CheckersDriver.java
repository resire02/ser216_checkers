package edu.ser216.checkers.core;

import edu.ser216.checkers.ui.CheckersTextConsole;
import edu.ser216.checkers.ui.CheckersViewer;
import edu.ser216.checkers.gui.CheckersGUI;
import java.util.Scanner;

/**
 * This class provides the entry to run the checkers game. It also provides the main game play loop
 * that calls methods from the two user-created classes (CheckersGameLogic and CheckersTextConsole).
 *
 * The contents of this file should not be changed.
 *
 * @author Acuna, Baron
 * @version 1.0
 */
public class CheckersDriver 
{
    public static void main(String[] args) 
    {
        char appType;
        Scanner scan = new Scanner(System.in);
        CheckersViewer console = new CheckersTextConsole();

        System.out.println("Select preferred interface: \"console\" or \"app\" (\"app\" is default.):");

        switch(scan.nextLine().toLowerCase().trim())
        {
            case "console":
                appType = 'c';
                break;
            case "app": default:
                appType = 'a';
        }

        if(appType == 'a') CheckersGUI.launchApp(args);

        if(appType == 'c')
        {
            CheckersGame game = new CheckersGameLogic(scan, appType);

            while (game.getWinningPlayer() == '_') {
                if(appType == 'c') System.out.println(console.printBoard(game));
                game.doTurn();   
                game.nextTurn();
            }
            game.onEnd();
        }
    }
}
