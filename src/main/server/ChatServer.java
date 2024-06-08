package main.server;

import main.exceptions.DatabaseException;
import main.model.Message;
import main.service.MessageService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());

    private static final int PORT = 12345;
    private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static final MessageService messageService = new MessageService();
    private static final int MAX_MESSAGE_POOL = 10;
    private static final ExecutorService messageExecutor = Executors.newFixedThreadPool(MAX_MESSAGE_POOL);

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOG.info("Se ha iniciado el servidor en el puerto: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOG.info("Se ha aceptado la conexión con un cliente");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                LOG.log(Level.INFO, "Se ha agregado un cliente a la lista de clientes: " + clients);
                new Thread(clientHandler).start(); //TODO: convertir en executor, es mejor tener un pool administrado y limitado.
                LOG.info("Se ha iniciado un nuevo hilo para un cliente");
            }

        } catch (IOException e) {
            // TODO: mejorar logs
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            messageExecutor.shutdown();
            LOG.info("Se ha detenido el servidor...");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al momento de detener el servidor", e);
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void saveMessage(Message message) {
        messageExecutor.submit(() -> {
            try {
                messageService.saveMessage(message);
            } catch (DatabaseException e) {
                LOG.log(Level.SEVERE, "Error guardando el mensaje: " + message, e);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Ha ocurrido un guardando el mensaje", e);
            }
        });
    }

    public static void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }
    }

}
