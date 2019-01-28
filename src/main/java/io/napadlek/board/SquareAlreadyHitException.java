package io.napadlek.board;

import io.napadlek.square.Square;

public class SquareAlreadyHitException extends RuntimeException {

    SquareAlreadyHitException(Square square) {
        super(String.format("Square %s was already hit in this game.", square.toStringRepresentation()));
    }
}
