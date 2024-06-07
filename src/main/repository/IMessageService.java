package main.repository;

import main.model.Message;

import java.util.List;
import java.util.Optional;

public interface IMessageService {

    void saveMessage(Message message);

    Optional<Message> getMessageById(int id);

    List<Message> getLastMessages();

}
