package main.utils;

import main.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.isNull;

public class UserUtils {
    public static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

    public static void validateUser(User user) {

        validateUsername(user.getUsername());
        validateEmail(user.getEmail());

        String password = user.getPassword(); // <-- Nosotros debemos inventar las reglas para validar la contraseña.
        if (isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser null o vacía");
        }

    }


    public static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID: " + id + " no es un ID válido.");
        }
    }

    public static void validateUsername(String username) {
        if (isNull(username) || username.isEmpty()) {
            throw new IllegalArgumentException("El username no puede ser null o vacío");
        }
    }

    public static void validateEmail(String email) {
        if (isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser null o vacío");
        }
    }
}
