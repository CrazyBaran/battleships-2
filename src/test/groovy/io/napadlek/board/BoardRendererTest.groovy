package io.napadlek.board

import io.napadlek.square.Square
import spock.lang.Specification

class BoardRendererTest extends Specification {

    def "should render empty board"() {
        given:
        def renderer = new BoardRenderer()

        when:
        def output = renderer.render()

        then:
        output == """     A  B  C  D  E  F  G  H  I  J 
   ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
  1┃                              ┃
  2┃                              ┃
  3┃                              ┃
  4┃                              ┃
  5┃                              ┃
  6┃                              ┃
  7┃                              ┃
  8┃                              ┃
  9┃                              ┃
 10┃                              ┃
   ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"""
    }

    def "should render miss and hit"() {
        given:
        def renderer = new BoardRenderer()

        when:
        renderer.addHit(Square.ofTextCoordinates("C2"))
        renderer.addHit(Square.ofTextCoordinates("D2"))
        renderer.addMiss(Square.ofTextCoordinates("B7"))
        def output = renderer.render()

        then:
        output == """     A  B  C  D  E  F  G  H  I  J 
   ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
  1┃                              ┃
  2┃       ■  ■                   ┃
  3┃                              ┃
  4┃                              ┃
  5┃                              ┃
  6┃                              ┃
  7┃    .                         ┃
  8┃                              ┃
  9┃                              ┃
 10┃                              ┃
   ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"""
    }

    def "should render only once when hit and miss multiple time on the same place"() {
        given:
        def renderer = new BoardRenderer()

        when:
        10.times {
            renderer.addHit(Square.ofTextCoordinates("J10"))
        }
        10.times {
            renderer.addMiss(Square.ofTextCoordinates("A1"))
        }
        def output = renderer.render()

        then:
        output == """     A  B  C  D  E  F  G  H  I  J 
   ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
  1┃ .                            ┃
  2┃                              ┃
  3┃                              ┃
  4┃                              ┃
  5┃                              ┃
  6┃                              ┃
  7┃                              ┃
  8┃                              ┃
  9┃                              ┃
 10┃                            ■ ┃
   ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"""
    }
}
