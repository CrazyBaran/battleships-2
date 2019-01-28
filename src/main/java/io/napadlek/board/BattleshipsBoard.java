package io.napadlek.board;

import io.napadlek.ship.Ship;
import io.napadlek.square.Square;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class BattleshipsBoard {

    private final Map<Square, Ship> shipMap = new HashMap<>(40);

    private final Set<Square> squaresHit = new HashSet<>(100);

    private final Set<Square> squaresOccupied = new HashSet<>(100);

    private int sunkenShips = 0;

    @Getter
    private final BoardRenderer boardRenderer;

    @Getter
    private BoardState boardState = BoardState.NEW;

    public void addShip(Ship ship) {
        validateBoardStateForShipPlacement();
        validatePlacement(ship);
        squaresOccupied.addAll(ship.getShipSquares());
        squaresOccupied.addAll(ship.getNeighbourSquares());
        ship.getShipSquares().forEach(sq -> shipMap.put(sq, ship));
    }

    public void hit(Square square) {
        validateBoardStateForHit();
        validateHit(square);
        squaresHit.add(square);
        if (shipMap.containsKey(square)) {
            boardRenderer.addHit(square);
            trySinkShip(square);
        } else {
            boardRenderer.addMiss(square);
        }
    }

    private void trySinkShip(Square square) {
        var ship = shipMap.get(square);
        Set<Square> shipSquares = new HashSet<>(ship.getShipSquares());
        shipSquares.removeAll(squaresHit);
        if (shipSquares.isEmpty()) {
            sunkenShips++;
            ship.getNeighbourSquares().forEach(neighbour -> {
                squaresHit.add(neighbour);
                boardRenderer.addMiss(neighbour);
            });
            checkIfFinished();
        }
    }

    private void validatePlacement(Ship ship) {
        Set<Square> shipSquaresCopy = new HashSet<>(ship.getShipSquares());
        shipSquaresCopy.retainAll(squaresOccupied);
        if (!shipSquaresCopy.isEmpty()) {
            throw new InvalidShipPlacementException(shipSquaresCopy);
        }
    }

    private void checkIfFinished() {
        if (sunkenShips == shipMap.values().stream().distinct().count()) {
            this.boardState = BoardState.FINISHED;
        }
    }

    private void validateHit(Square square) {
        if (squaresHit.contains(square)) {
            throw new SquareAlreadyHitException(square);
        }
    }

    private void validateBoardStateForShipPlacement() {
        if (!BoardState.NEW.equals(boardState)) {
            throw new IllegalBoardStateException(boardState);
        }
    }

    private void validateBoardStateForHit() {
        if (BoardState.FINISHED.equals(boardState)) {
            throw new IllegalBoardStateException(boardState);
        } else if (BoardState.NEW.equals(boardState)) {
            if (shipMap.isEmpty()) {
                throw new MissingShipException();
            }
            this.boardState = BoardState.STARTED;
        }
    }
}
