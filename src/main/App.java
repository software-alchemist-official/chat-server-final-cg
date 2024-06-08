package main;


import main.model.Message;
import main.server.ChatServer;
import main.service.MessageService;

import java.util.List;

public class App {
    public static void main(String[] args) {
        new ChatServer().startServer();
    }
}
