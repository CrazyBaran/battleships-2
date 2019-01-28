package io.napadlek;

public class GameInitializationException extends Exception {

    GameInitializationException() {
        super("Could not initialize a starting position.");
    }
}
