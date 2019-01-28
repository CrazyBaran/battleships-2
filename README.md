### Prerequisites
In order to run this program you'll need
* maven > 3.3.9
* java 11 (get openjdk java here https://jdk.java.net/11/)

Make sure your `JAVA_HOME` is set to jdk11

### Testing
You can run all tests simply by running `mvn clean test`

### Packaging
An executable jar can be created by running `mvn clean package`

### Running
You can launch the packaged jar with `java -jar target/battleships-1.0.0-SNAPSHOT.jar` after packaging.
You can also simply run the program from the IDE of choice by starting the `main` method.

### Interacting with the game
The game is a console based battleships version. You can hit the squares by inputting the coordinates with `A1` - `J10` format.
When the ship is sunken all the adjacent squares will be marked as hit automatically.
Game ends when all the ships are destroyed.