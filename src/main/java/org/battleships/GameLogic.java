package org.battleships;

import java.util.Random;

public class GameLogic {

    public static String myCoordinates;

    public static String generateShot() {
        Random random = new Random();
        int row = random.nextInt(10);
        int col = random.nextInt(10);
        System.out.println("Random coordinates: " + row + ", " + col);
        return convertCoordinatesToString(row, col);
    }

    private static String convertCoordinatesToString(int row, int col) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) (col + 'A'));
        sb.append((row + 1));
        System.out.println("Converted coordinates: (string) " + sb.toString());
        return sb.toString();
    }

    private static int[] convertStringtoCoordinates(String input){
        if (input.length() < 2) {
            throw new IllegalArgumentException("Invalid input format");
        }
        char letter = input.charAt(0);
        String digit = input.substring(1);
        if (digit.length() < 1) {
            throw new IllegalArgumentException("Invalid input format");
        }
        return new int[] {Integer.parseInt(digit) - 1, Character.toUpperCase(letter) - 'A'};
    }

    private static boolean checkIfSunk(int[] coordinates, char[][] board) {

        int row = coordinates[0];
        int col = coordinates[1];
        if ((row > 0 && board[row - 1][col] == '#') || (row < (Board.BOARD_SIZE - 1) && board[row + 1][col] == '#')) {
            return false;
        }
        if ((col > 0 && board[row][col - 1] == '#') || (col < (Board.BOARD_SIZE - 1) && board[row][col + 1] == '#')) {
            return false;
        }
        return true;
    }

    private static boolean lastSunk(char[][] board) {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board[i][j] == '#') return false;
            }
        }
        return true;
    }

    private static void revealNeighbors(int row, int col, char[][] board) {
        if (row > 0) board[row - 1][col] = '.';
        if (row < Board.BOARD_SIZE - 1) board[row + 1][col] = '.';
        if (col > 0) board[row][col - 1] = '.';
        if (col < Board.BOARD_SIZE - 1) board[row][col + 1] = '.';
    }

    // odkrywanie planszy przeciwnika - jego feedback
    private static void processCommand(String command, int[] coordinates, char[][] board) {

        int row = coordinates[0];
        int col = coordinates[1];

        switch(command) {
            case "start":
                break;
            case "pudło":
                board[row][col] = '.';
                break;
            case "trafiony":
                board[row][col] = '#';
                break;
            case "trafiony zatopiony":
                board[row][col] = '#';
                revealNeighbors(row, col, board);
                break;
            case "ostatni zatopiony":
                board[row][col] = '#';
                BattleshipGame.status = Status.WON;
                break;
            default:
                System.out.println("Unrecognized command! Try again.");
                break;
        }
    }

    // obsługa strzału przeciwnika
    private static String processShot(int[] coordinates, char[][] board) {

        String result = new String();
        char shot = board[coordinates[0]][coordinates[1]];
        System.out.println("Processing shot: " + shot);

        switch(shot) {
            case '.':
            case'~':
                result = "pudło";
                board[coordinates[0]][coordinates[1]] = '~';
                break;
            case '#':
            case '@':
                if (lastSunk(BattleshipGame.gamerBoard)) {
                    result = "ostatni zatopiony";
                    board[coordinates[0]][coordinates[1]] = '@';
                    BattleshipGame.status = Status.LOST;
                    break;
                }
                result = checkIfSunk(coordinates, board) ? "trafiony zatopiony" : "trafiony";
                board[coordinates[0]][coordinates[1]] = '@';
                break;
            default:
                System.out.println("Unrecognized char. What is that?");
                break;
        }
        return result;
    }

    public static String processMove(String move) {
        String[] parts = move.split(";");
        String command = parts[0];

        System.out.println("Obtained command: " + command);

        if (command.equals("ostatni zatopiony")) {
            return new String("wygrana");
        }

        int[] coordinates = convertStringtoCoordinates(parts[1]);
        System.out.println("Processed coordinates: " + coordinates[0] + ", " + coordinates[1]);
        System.out.println("My previous coordinates: "+ myCoordinates);

        if (!(command.equals("start"))) {
            processCommand(command, convertStringtoCoordinates(myCoordinates), BattleshipGame.enemyBoard);
        }
        String result = processShot(coordinates, BattleshipGame.gamerBoard);
        System.out.println("Result: " + result);
        return result;
    }
}
