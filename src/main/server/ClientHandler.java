package main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    private static final String CLEAR_CHARACTER = "\033[H\033[2J";

    private static final String EXIT_WORD = "exit";

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            welcome();
            //TODO: tal vez aquí podríamos hacer el login/registro
            out.print("Escribe tu nombre de usuario: ");
            out.flush();
            username = in.readLine();
            clearScreen();

            LOG.info("Ha ingresado " + username + " al servidor");

            String connectionMessage = String.format("[SERVER]: %s ha ingresado al servidor", username);
            ChatServer.broadcastMessage(connectionMessage, this);

            String message;

            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase(EXIT_WORD)) {
                    break;
                }
//                message = String.format("[%s]: " + message, username);
                message = String.format("[%s]: %s", username, message);
                ChatServer.broadcastMessage(message, this);
                //TODO: tal vez es buen momento para almacenar en la base de datos el mensaje.
            }


        } catch (IOException e) {
            //TODO: mejorar manejo excepciones, custom + logs
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                //TODO: mejorar esto custom + logs
                e.printStackTrace();
            }

            ChatServer.removeClient(this);
            LOG.info(username + " se ha desconectado ");
            ChatServer.broadcastMessage(username + " se ha desconectado del chat", this);
        }
    }

    private void welcome() throws IOException {
        clearScreen();
        out.println(ServerConstants.BANNER);
        out.print("¡Bienvnid@!, recuerda ser amigable y respetuoso!, presiona ENTER para continuar...");
        out.flush();
        in.readLine();
        clearScreen();
    }

    private void clearScreen() {
        out.print(CLEAR_CHARACTER);
        out.flush();
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
}
