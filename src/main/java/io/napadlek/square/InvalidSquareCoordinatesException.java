package io.napadlek.square;

public class InvalidSquareCoordinatesException extends RuntimeException {

    InvalidSquareCoordinatesException(String cellCoordinates) {
        super(String.format("Cannot create square from \"%s\" input!", cellCoordinates));
    }

    InvalidSquareCoordinatesException(int column, int row) {
        super(String.format("Cannot create square for column %s and row %s.", column, row));
    }
}
