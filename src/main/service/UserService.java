package main.service;

import main.db.DBConnection;
import main.db.IDBConnection;
import main.exceptions.DatabaseException;
import main.model.User;
import main.repository.IUserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.db.queries.UserQueries.*;
import static main.utils.UserUtils.*;

public class UserService implements IUserService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    private final IDBConnection dbConnection;


    public UserService() {
        dbConnection = new DBConnection();
    }

    @Override
    public void saveUser(User user) {

        validateUser(user);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new DatabaseException("Error al crear el usuario, no se obtuvo un ID");
                }
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
            throw new DatabaseException("Error al guardar al usuario: " + user, e);
        }
    }

    @Override
    public Optional<User> getUserById(int id) {

        validateId(id);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_QUERY)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al recuperar al usuario con ID: " + id, e);
            throw new DatabaseException("Error al recuperar al usuario con ID: " + id, e);
        }

    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_QUERY)) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al recuperar a los usuarios", e);
            throw new DatabaseException("Error al recuperar a los usuarios", e);
        }
        return Collections.unmodifiableList(users);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {

        validateUsername(username);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al recuperar el usuario con username: " + username, e);
            throw new DatabaseException("Error al recuperar el usuario con username: " + username, e);
        }
    }


    @Override
    public Optional<User> getUserByEmail(String email) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL_QUERY)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al recuperar al usuario con email : " + email, e);
            throw new DatabaseException("Error al recuperar al usuario con email : " + email, e);
        }

    }


}
