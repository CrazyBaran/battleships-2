package io.napadlek.board;

import io.napadlek.square.Square;

import java.util.Set;
import java.util.stream.Collectors;

public class InvalidShipPlacementException extends RuntimeException {

    InvalidShipPlacementException(Set<Square> occupiedSquares) {
        super(String.format("Can't place the ship on the board because squares %s are taken or adjacent to another ship.", occupiedSquares.stream()
                .map(Square::toStringRepresentation).collect(Collectors.toList())));
    }
}
