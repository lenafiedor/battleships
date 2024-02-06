package org.battleships;

public class Main {
    public static void main(String[] args) {

        String mode = null;
        String server = null;
        int port = 0;
        String mapFile = null;

        for (int i = 0; i < args.length; i += 2) {
            String option = args[i];
            String value = args[i+1];

            switch (option) {
                case "-mode":
                    mode = value;
                    break;
                case "-server":
                    server = value;
                    break;
                case "-port":
                    port = Integer.parseInt(value);
                    break;
                case "-map":
                    mapFile = value;
                    break;
                default:
                    System.out.println("Invalid option:" + option);
                    return;
            }
        }

        if (mode == null || (mode.equals("client") && (server == null || port == 0)) || port == 0 || mapFile == null) {
            System.out.println("Usage: -mode [server|client] -server [server_address] -port [port_number] -map [map_file]");
            return;
        }

        BattleshipGame.initializeBoards(mapFile);

        if (mode.equals("server")) {
            BattleshipGame.startServer(port);
        }
        else if (mode.equals("client")) {
            BattleshipGame.startClient(server, port);
        }
        else {
            System.out.println("Unknown mode! Try again.");
        }
    }
}
