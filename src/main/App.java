package main;


import main.model.Message;
import main.service.MessageService;

import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        MessageService messageService = new MessageService();
        List<Message> messages = messageService.getLastMessages();
        messages.forEach(System.out::println);
    }
}
