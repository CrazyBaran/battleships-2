package io.napadlek.ship

import io.napadlek.square.Square
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

class ShipTest extends Specification {

    @Unroll
    def "should create proper ship for random input vertical=#vertical, column=#column, row=#row, length=#shipLength"() {
        given:
        Ship.VERTICAL_RANDOM_SUPPLIER = { vertical }
        def randomMock = Mock(Function)
        randomMock.apply(_) >>> [column, row]
        Ship.COORDINATE_RANDOM_SUPPLIER = randomMock

        when:
        def randomShip = Ship.randomize(shipLength)

        then:
        randomShip.getShipSquares() == expectedShip.collect(Square.&ofTextCoordinates).toSet()

        where:
        vertical | column | row | shipLength | expectedShip
        true     | 0      | 2   | 4          | ["A3", "A4", "A5", "A6"]
        false    | 0      | 2   | 5          | ["A3", "B3", "C3", "D3", "E3"]
        true     | 5      | 5   | 1          | ["F6"]
        true     | 5      | 5   | 2          | ["F6", "F7"]
        false    | 5      | 5   | 2          | ["F6", "G6"]
    }

    @Unroll
    def "should return proper neighbour squares for ship #shipSquares"() {
        given:
        def ship = new Ship(shipSquares.collect { Square.ofTextCoordinates(it) }.toSet())

        when:
        def neighbourSquares = ship.getNeighbourSquares()

        then:
        neighbourSquares == expectedNeighbourSquares.collect(Square.&ofTextCoordinates).toSet()

        where:
        shipSquares        | expectedNeighbourSquares
        ["A1", "A2"]       | ["A3", "B1", "B2", "B3"]
        ["B2", "B3", "B4"] | ["A1", "A2", "A3", "A4", "A5", "B5", "C5", "C4", "C3", "C2", "C1", "B1"]
    }
}
