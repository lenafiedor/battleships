package org.battleships;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class BattleshipGame {

    private static final int BOARD_SIZE = 10;

    public static char[][] gamerBoard;
    public static char[][] enemyBoard;

    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    public static PrintWriter out;
    public static BufferedReader in;

    public static Status status = Status.PLAYING;

    public static void initializeBoards(String mapFile) {
        gamerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        // Board.generateBoard(mapFile);
        Board.loadBoardFromFile(gamerBoard, mapFile);
        enemyBoard = new char[BOARD_SIZE][BOARD_SIZE];
        Board.initializeEnemyBoard();
        Board.printBoards();
    }

    public static void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Waiting for connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Connection estabilished!");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            handleCommunication();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startClient(String server, int port) {
        try {
            clientSocket = new Socket(server, port);
            System.out.println("Connected to the server!");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            GameLogic.myCoordinates = GameLogic.generateShot();
            out.println("start;" + GameLogic.myCoordinates);
            System.out.println("My move: start;" + GameLogic.myCoordinates);

            handleCommunication();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleCommunication() {
        try {
            while (status == Status.PLAYING) {
                System.out.println("Waiting for the opponent's move...");
                String move = in.readLine();

                if (move == null) {
                    System.out.println("Opponent disconnected or communication issue.");
                    break;
                }

                System.out.println("Opponent's move: " + move);

                String command = GameLogic.processMove(move);
                GameLogic.myCoordinates = GameLogic.generateShot();

                if (command.equals("ostatni zatopiony")) {
                    out.println(command);
                    System.out.println("Przegrana");
                    status = Status.LOST;
                    break;
                }
                if (command.equals("wygrana")) {
                    out.println(command);
                    System.out.println("Wygrana");
                    status = Status.WON;
                    break;
                }
                
                System.out.println("Sending message: " + command + ';' + GameLogic.myCoordinates);
                out.println(command + ';' + GameLogic.myCoordinates);

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Board.printBoards();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Connection.closeConnection();
        }
    }
}
