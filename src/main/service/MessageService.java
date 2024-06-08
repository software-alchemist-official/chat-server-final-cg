package main.service;

import main.db.DBConnection;
import main.db.IDBConnection;
import main.exceptions.DatabaseException;
import main.model.Message;
import main.repository.IMessageService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.db.queries.MessageQueries.*;
import static main.utils.MessageUtils.validateMessage;
import static main.utils.MessageUtils.validateMessageId;

public class MessageService implements IMessageService {

    private static final Logger LOG = Logger.getLogger(MessageService.class.getName());
    private final IDBConnection dbConnection;

    private static final int DEFAULT_LAST_MESSAGES = 10;

    public MessageService() {
        this.dbConnection = new DBConnection();
    }

    @Override
    public void saveMessage(Message message) {

        validateMessage(message);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_MESSAGE_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setInt(1, message.getUserId());
            statement.setString(2, message.getContent());
            statement.executeUpdate();
            LOG.log(Level.INFO, "Se ha guardado un mensaje en la bd: " + message);
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Error al guardar el mensaje");
                }
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error guardando el mensaje: " + message, e);
            throw new DatabaseException("Error al guardar mensaje, no se obtuvo un ID.");
        }
    }


    @Override
    public Optional<Message> getMessageById(int id) {

        validateMessageId(id);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MESSAGE_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultToMessage(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al obtener el mensaje con ID: " + id, e);
            throw new DatabaseException("Error al obtener el mensaje con ID: " + id, e);
        }

    }

    @Override
    public List<Message> getLastMessages() {

        List<Message> messages = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_LAST_MESSAGES)) {

            statement.setInt(1, DEFAULT_LAST_MESSAGES);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = mapResultToMessage(resultSet);
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al recuperar los mensajes ", e);
            throw new DatabaseException("Error al recuperar los mensajes", e);
        }

        return messages;
    }

    private Message mapResultToMessage(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getInt("id"));
        message.setUserId(resultSet.getInt("user_id"));
        message.setContent(resultSet.getString("content"));
        message.setTimestamp(resultSet.getTimestamp("timestamp"));
        return message;
    }
}
