package io.napadlek.square;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
public class Square {

    private static final String COORDINATES_PATTERN = "[A-Ja-j](?:[1-9]|10)";

    private static final int LOWER_BOUND = 0;

    public static final int UPPER_BOUND = 10;

    private final int column;

    private final int row;

    public static Square ofTextCoordinates(String coordinates) {
        validateCoordinatesInput(coordinates);
        var normalizedInput = coordinates.strip().toUpperCase();
        var column = Character.getNumericValue(normalizedInput.charAt(0)) - 10;
        var row = Integer.valueOf(normalizedInput.substring(1)) - 1;
        return new Square(column, row);
    }

    public static Square ofCoordinates(int column, int row) {
        validateCoordinatesInput(column, row);
        return new Square(column, row);
    }

    public Set<Square> getNeighbours() {
        var neighbours = new HashSet<Square>();
        IntStream.range(Math.max(column - 1, LOWER_BOUND), Math.min(column + 2, UPPER_BOUND)).forEach(col ->
                IntStream.range(Math.max(row - 1, LOWER_BOUND), Math.min(row + 2, UPPER_BOUND)).forEach(row ->
                        neighbours.add(new Square(col, row))
                )
        );
        neighbours.remove(this);
        return neighbours;
    }

    private static void validateCoordinatesInput(int column, int row) {
        if (column < LOWER_BOUND || column >= UPPER_BOUND || row < LOWER_BOUND || row >= UPPER_BOUND) {
            throw new InvalidSquareCoordinatesException(column, row);
        }
    }

    private static void validateCoordinatesInput(String coordinates) {
        if (coordinates == null || coordinates.isBlank() || !Pattern.matches(COORDINATES_PATTERN, coordinates.strip())) {
            throw new InvalidSquareCoordinatesException(coordinates);
        }
    }

    public String toStringRepresentation() {
        char col = (char) (column + 65);
        return col + String.valueOf(row + 1);
    }
}
