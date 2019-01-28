package io.napadlek.ship;

import io.napadlek.square.Square;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class Ship {

    private static Random RANDOM;

    static {
        try {
            RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            RANDOM = new Random(System.currentTimeMillis());
        }
    }

    private static Supplier<Boolean> VERTICAL_RANDOM_SUPPLIER = RANDOM::nextBoolean;

    private static Function<Integer, Integer> COORDINATE_RANDOM_SUPPLIER = RANDOM::nextInt;

    private static final int UPPER_BOUND = Square.UPPER_BOUND;

    private final Set<Square> shipSquares;

    public static Ship randomize(int length) {
        Boolean vertical = VERTICAL_RANDOM_SUPPLIER.get();
        var startColumnBound = vertical ? UPPER_BOUND : UPPER_BOUND - length;
        var startRowBound = vertical ? UPPER_BOUND - length : UPPER_BOUND;
        return create(COORDINATE_RANDOM_SUPPLIER.apply(startColumnBound), COORDINATE_RANDOM_SUPPLIER.apply(startRowBound), length, vertical);
    }

    public Set<Square> getNeighbourSquares() {
        return shipSquares.stream().flatMap(s -> s.getNeighbours().stream()).filter(s -> !shipSquares.contains(s)).collect(Collectors.toUnmodifiableSet());
    }

    private static Ship create(int startColumn, int startRow, int length, boolean vertical) {
        return vertical ?
                new Ship(IntStream.range(startRow, startRow + length)
                        .mapToObj(i -> Square.ofCoordinates(startColumn, i))
                        .collect(Collectors.toUnmodifiableSet())) :
                new Ship(IntStream.range(startColumn, startColumn + length)
                        .mapToObj(i -> Square.ofCoordinates(i, startRow))
                        .collect(Collectors.toUnmodifiableSet()));
    }
}
