package io.napadlek.board

import io.napadlek.ship.Ship
import io.napadlek.square.Square
import spock.lang.Specification
import spock.lang.Unroll

class BattleshipsBoardTest extends Specification {

    @Unroll
    def "should throw exceptions for overlapping ship combinations"() {
        given:
        def shipsList = ships.collect { new Ship(it.collect(Square.&ofTextCoordinates).toSet()) }
        def board = new BattleshipsBoard()

        when:
        shipsList.each board.&addShip

        then:
        thrown(InvalidShipPlacementException)

        where:
        ships                                                         | _
        [["A1", "A2", "A3"], ["B4", "C4"]]                            | _
        [["A1"], ["B2"]]                                              | _
        [["C2", "C3", "C4"], ["B7", "B8", "B9", "B10"], ["B5", "A5"]] | _
    }

    @Unroll
    def "should properly add ship combinations"() {
        given:
        def shipsList = ships.collect { new Ship(it.collect(Square.&ofTextCoordinates).toSet()) }
        def board = new BattleshipsBoard()

        when:
        shipsList.each board.&addShip

        then:
        board.boardState == BoardState.NEW

        where:
        ships                                                         | _
        [["A1", "A2", "A3"], ["B5", "C5"]]                            | _
        [["A1"], ["B3"]]                                              | _
        [["C2", "C3", "C4"], ["B7", "B8", "B9", "B10"], ["E5", "F5"]] | _
    }

    def "should return started state after first hit"() {
        given:
        def rendererMock = Mock(BoardRenderer)
        def board = new BattleshipsBoard(rendererMock)
        def ship = Ship.randomize(4)
        def coordinates = Square.ofTextCoordinates("A1")

        when:
        board.addShip(ship)
        board.hit(coordinates)

        then:
        board.boardState == BoardState.STARTED
        1 * rendererMock./add.*/(coordinates)
    }

    def "should return state finished for single ship board"() {
        given:
        def rendererMock = Mock(BoardRenderer)
        def board = new BattleshipsBoard(rendererMock)
        def ship = Ship.create(0, 0, 1, true)

        when:
        board.addShip(ship)
        board.hit(Square.ofTextCoordinates("J10"))
        board.hit(Square.ofTextCoordinates("A1"))

        then:
        board.boardState == BoardState.FINISHED
        1 * rendererMock.addHit(Square.ofTextCoordinates("A1"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("J10"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("A2"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B2"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B1"))
    }

    def "should return state FINISHED for multiple ships board"() {
        given:
        def rendererMock = Mock(BoardRenderer)
        def board = new BattleshipsBoard(rendererMock)
        def ship = Ship.create(0, 0, 3, true)
        def secondShip = Ship.create(5, 5, 2, false)

        when:
        board.addShip(ship)
        board.addShip(secondShip)
        board.hit(Square.ofTextCoordinates("A1"))
        board.hit(Square.ofTextCoordinates("A2"))
        board.hit(Square.ofTextCoordinates("A3"))

        then:
        board.boardState == BoardState.STARTED
        1 * rendererMock.addHit(Square.ofTextCoordinates("A1"))
        1 * rendererMock.addHit(Square.ofTextCoordinates("A2"))
        1 * rendererMock.addHit(Square.ofTextCoordinates("A3"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B1"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B2"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B3"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("B4"))
        1 * rendererMock.addMiss(Square.ofTextCoordinates("A4"))

        when:
        board.hit(Square.ofTextCoordinates("G6"))
        board.hit(Square.ofTextCoordinates("E6"))
        board.hit(Square.ofTextCoordinates("F6"))

        then:
        board.boardState == BoardState.FINISHED
        1 * rendererMock.addHit(Square.ofTextCoordinates("G6"))
        1 * rendererMock.addHit(Square.ofTextCoordinates("F6"))
        (8.._) * rendererMock.addMiss(_ as Square)

        when:
        board.hit(Square.ofTextCoordinates("C7"))

        then:
        thrown(IllegalBoardStateException)
    }

    def "should not permit adding ships to started game"() {
        given:
        def rendererMock = Mock(BoardRenderer)
        def board = new BattleshipsBoard(rendererMock)
        def ship = Ship.create(0, 0, 3, true)
        def secondShip = Ship.create(5, 5, 2, false)

        when:
        board.addShip(ship)
        board.hit(Square.ofTextCoordinates("A1"))
        board.addShip(secondShip)

        then:
        board.boardState == BoardState.STARTED
        thrown(IllegalBoardStateException)
    }

    def "should not start game with empty board"() {
        given:
        def rendererMock = Mock(BoardRenderer)
        def board = new BattleshipsBoard(rendererMock)

        when:
        board.hit(Square.ofTextCoordinates("A2"))

        then:
        thrown(MissingShipException)
    }
}
