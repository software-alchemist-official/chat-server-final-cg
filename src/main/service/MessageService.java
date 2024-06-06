package main.service;

import main.db.DBConnection;
import main.db.IDBConnection;
import main.model.Message;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.service.MessageQueries.SAVE_MESSAGE_QUERY;

public class MessageService implements IMessageService {

    private static final Logger LOG = Logger.getLogger(MessageService.class.getName());
    private final IDBConnection dbConnection;

    public MessageService() {
        this.dbConnection = new DBConnection();
    }

    @Override
    public void saveMessage(Message message) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_MESSAGE_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            /*
            El segundo parámetro: Statement.RETURN_GENERATED_KEYS,
            indica que queremos obtener las claves generadas automáticamente después de ejecutar la consulta.
            Esto es útil cuando insertamos registros en una tabla con una columna de clave primaria autoincremental,
            por ejemplo, y queremos obtener el valor de esa clave generada automáticamente después de insertar un nuevo registro.
             */

            statement.setInt(1, message.getUserId());
            statement.setString(2, message.getContent());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }
    }

    @Override
    public Message getMessageById(int id) {
        return null;
    }

    @Override
    public List<Message> getLastMessages() {
        return null;
    }
}
