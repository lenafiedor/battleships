package org.battleships;

import java.io.IOException;

public class Connection {

    public static void closeConnection() {
        try {
            if (BattleshipGame.out != null) BattleshipGame.out.close();
            if (BattleshipGame.in != null) BattleshipGame.in.close();
            if (BattleshipGame.clientSocket != null) BattleshipGame.clientSocket.close();
            if (BattleshipGame.serverSocket != null) BattleshipGame.serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
