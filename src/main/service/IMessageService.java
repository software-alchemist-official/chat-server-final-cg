package main.service;

import main.model.Message;

import java.util.List;

public interface IMessageService {

    void saveMessage(Message message);

    Message getMessageById(int id);

    List<Message> getLastMessages();

}
