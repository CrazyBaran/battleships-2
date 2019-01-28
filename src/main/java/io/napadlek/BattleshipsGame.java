package io.napadlek;

import io.napadlek.board.BattleshipsBoard;
import io.napadlek.board.BoardRenderer;
import io.napadlek.board.BoardState;
import io.napadlek.board.InvalidShipPlacementException;
import io.napadlek.ship.Ship;
import io.napadlek.square.Square;

import java.util.Scanner;

public class BattleshipsGame {

    public static void main(String... args) throws GameInitializationException {
        BattleshipsBoard battleshipsBoard = initializeBoard();
        var scanner = new Scanner(System.in);

        while (!BoardState.FINISHED.equals(battleshipsBoard.getBoardState())) {
            System.out.println((battleshipsBoard.getBoardRenderer().render()));
            System.out.println("Type square coordinates and press enter to hit (type Q to quit):");
            var input = scanner.next();
            if (input.strip().equalsIgnoreCase("q")) {
                break;
            }
            try {
                var square = Square.ofTextCoordinates(input);
                battleshipsBoard.hit(square);
            } catch (RuntimeException e) {
                clearConsole();
                System.err.println(e.getMessage());
                continue;
            }
            clearConsole();
        }
        System.out.println(battleshipsBoard.getBoardRenderer().render());
        System.out.println("Game finished!");
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static BattleshipsBoard initializeBoard() throws GameInitializationException {
        var initializationCounter = 0;
        while (true) {
            try {
                var battleshipsBoard = new BattleshipsBoard(new BoardRenderer());
                battleshipsBoard.addShip(Ship.randomize(5));
                battleshipsBoard.addShip(Ship.randomize(4));
                battleshipsBoard.addShip(Ship.randomize(4));
                return battleshipsBoard;
            } catch (InvalidShipPlacementException e) {
                initializationCounter++;
                if (initializationCounter > 1_000) {
                    throw new GameInitializationException();
                }
            }
        }
    }
}
