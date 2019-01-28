package io.napadlek.square

import spock.lang.Specification
import spock.lang.Unroll

class SquareTest extends Specification {


    @Unroll
    def "should throw exception when for \"#input\" input"() {
        when:
        Square.ofTextCoordinates(input)

        then:
        thrown(InvalidSquareCoordinatesException)

        where:
        input             | _
        "G11"             | _
        "A0"              | _
        "u1"              | _
        "K5"              | _
        "b11"             | _
        "C99"             | _
        "K9"              | _
        "G01"             | _
        null              | _
        ""                | _
        "  "              | _
        "inv4lid rubbish" | _
    }

    @Unroll
    def "should create square with \"#input\" coordinates"() {
        when:
        def cell = Square.ofTextCoordinates(input)

        then:
        cell.column == expectedColumn
        cell.row == expectedRow

        where:
        input     | expectedColumn | expectedRow
        "a1"      | 0              | 0
        "B1"      | 1              | 0
        "A10"     | 0              | 9
        "j1"      | 9              | 0
        "J10"     | 9              | 9
        "E5"      | 4              | 4
        "  I8   " | 8              | 7
    }

    @Unroll
    def "should return proper neighbours of square #square"() {
        when:
        def neighbours = Square.ofTextCoordinates(square).getNeighbours()

        then:
        neighbours == expectedNeighbours.collect(Square.&ofTextCoordinates).toSet()

        where:
        square | expectedNeighbours
        "A1"   | ["A2", "B1", "B2"]
        "B2"   | ["A1", "A2", "A3", "B1", "B3", "C1", "C2", "C3"]
        "A5"   | ["A4", "B4", "B5", "A6", "B6"]
        "J10"  | ["I10", "I9", "J9"]
    }

    def "should return proper textual representation"() {

    }
}
