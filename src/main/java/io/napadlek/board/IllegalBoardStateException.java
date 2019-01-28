package io.napadlek.board;

class IllegalBoardStateException extends RuntimeException {

    IllegalBoardStateException(BoardState boardState) {
        super(String.format("State %s is invalid in this context!", boardState));
    }
}
