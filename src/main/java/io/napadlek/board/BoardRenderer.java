package io.napadlek.board;

import io.napadlek.square.Square;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BoardRenderer {

    private static final char SHIP_REPRESENTATION = '■';

    private static final char MISS_REPRESENTATION = '.';

    private static final char SQUARE_REPRESENTATION = ' ';

    private final char[][] gameState = new char[10][10];

    public BoardRenderer() {
        for (var row : gameState) {
            Arrays.fill(row, SQUARE_REPRESENTATION);
        }
    }

    void addHit(Square square) {
        this.gameState[square.getRow()][square.getColumn()] = SHIP_REPRESENTATION;
    }

    void addMiss(Square square) {
        this.gameState[square.getRow()][square.getColumn()] = MISS_REPRESENTATION;
    }

    public String render() {
        StringBuilder sb = renderHead();
        var rowCounter = 1;
        for (var line : gameState) {
            renderRow(sb, rowCounter, line);
            rowCounter++;
        }
        renderFoot(sb);
        return sb.toString();
    }

    private void renderFoot(StringBuilder sb) {
        sb.append("   ┗").append("━".repeat(30)).append("┛");
    }

    private StringBuilder renderHead() {
        var sb = new StringBuilder("    ");
        IntStream.range(0, 10).forEach(i -> sb.append(' ').append(((char) (((char) i) + 65))).append(' '));
        sb.append("\n   ┏").append("━".repeat(30)).append("┓\n");
        return sb;
    }

    private void renderRow(StringBuilder sb, int rowCounter, char[] line) {
        sb.append(String.format("%" + 3 + "s", rowCounter)).append('┃');
        for (var square : line) {
            sb.append(' ').append(square).append(' ');
        }
        sb.append('┃').append("\n");
    }
}
