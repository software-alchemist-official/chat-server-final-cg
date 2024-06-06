package main.service;

import main.db.DBConnection;
import main.db.IDBConnection;
import main.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.service.UserQueries.*;

public class UserService implements IUserService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    private final IDBConnection dbConnection;


    public UserService() {
        dbConnection = new DBConnection();
    }

    // Guardar un usuario en la base de datos
    @Override
    public void saveUser(User user) {


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }
    }

    // Recuperar un usuario usando el ID
    @Override
    public User getUserById(int id) {

        try (Connection connection = dbConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_QUERY)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }

        return null;

    }

    // Recuperar todos los usuarios
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_QUERY)) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }
        return users;
    }

    // Recuperar el usuario por username
    @Override
    public User getUserByUsername(String username) {
        try (Connection connection = dbConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }

        return null;
    }

    // Recuperar el usuario por email
    @Override
    public User getUserByEmail(String email) {
        try (Connection connection = dbConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL_QUERY)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error: ", e);
        }

        return null;
    }

    // Método auxiliar para mapear el ResultSet a un objeto User
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }
}
