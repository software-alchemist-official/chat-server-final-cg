package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());

    private static final int PORT = 12345;
    private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());


    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOG.info("Se ha iniciado el servidor en el puerto: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOG.info("Se ha aceptado la conexión con un cliente");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                LOG.log(Level.INFO, "Se ha agregado un cliente a la lista de clientes: " + clients);
                new Thread(clientHandler).start();
                LOG.info("Se ha iniciado un nuevo hilo para un cliente");
            }

        } catch (IOException e) {
            // TODO: mejorar logs
            throw new RuntimeException(e);
        }
    }

    //TODO: Stop server method?

    public static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }
    }

}
