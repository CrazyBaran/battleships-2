package io.napadlek.board;

public class MissingShipException extends RuntimeException {

    MissingShipException() {
        super("Cannot start the game without any ship on the board!");
    }
}
