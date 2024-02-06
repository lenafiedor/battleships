package org.battleships;

import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Board {

    static final short BOARD_SIZE = 10;

    public static void printBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void saveBoardToFile(char[][] board, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for(int j = 0; j < BOARD_SIZE; j++) {
                    writer.write(board[i][j]);
                }
                writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBoardFromFile(char[][] board, String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            String[] rows =content.split("\\r?\\n");

            if (rows.length == BOARD_SIZE){
                for (int i = 0; i < BOARD_SIZE; i++) {
                    if (rows[i].length() == BOARD_SIZE) {
                        for (int j = 0; j < BOARD_SIZE; j++) {
                            board[i][j] = rows[i].charAt(j);
                        }
                    }
                    else {
                        System.out.println("Invalid board size.");
                        return;
                    }
                }
            }
            else {
                System.out.println("Invalid number of rows.");
                return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printBoards() {
        System.out.println("Your board:");
        Board.printBoard(BattleshipGame.gamerBoard);

        System.out.println("Enemy board:");
        Board.printBoard(BattleshipGame.enemyBoard);
    }

    public static void generateBoard(String filename) {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(board);
        placeShips(board);
        saveBoardToFile(board, filename);
    }

    public static void initializeEnemyBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                BattleshipGame.enemyBoard[i][j] = '?';
            }
        }
    }

    private static void initializeBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '.';
            }
        }
    }

    private static void placeShips(char[][] board) {
        Random rand = new Random();
        int[] ships = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        for (int length : ships) {
            int row = rand.nextInt(10);
            int col = rand.nextInt(10);
            int direction = rand.nextInt(2);
            while (!canPlaceShip(board, row, col, direction, length)) {
                row = rand.nextInt(10);
                col = rand.nextInt(10);
                direction = rand.nextInt(2);
            }
            placeShip(board, row, col, direction, length);
        }
    }

    private static boolean canPlaceShip(char[][] board, int row, int col, int direction, int length) {
        if (direction == 0) {
            if (row + length > BOARD_SIZE) {
                return false;
            }
            for (int x = row - 1; x <= row + length; x++) {
                for (int y = col - 1; y <= col + 1; y++) {
                    if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == '#') {
                        return false;
                    }
                }
            }
        }
        else {
            if (col + length > BOARD_SIZE) {
                return false;
            }
            for (int x = row - 1; x <= row + 1; x++) {
                for (int y = col - 1; y <= col + length; y++) {
                    if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == '#') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void placeShip(char[][] board, int row, int col, int direction, int length) {
        if (direction == 0) {
            for (int y = 0; y < length; y++) {
                board[row + y][col] = '#';
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                board[row][col + i] = '#';
            }
        }
    }
}
