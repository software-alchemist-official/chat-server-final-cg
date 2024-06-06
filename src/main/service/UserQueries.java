package main.service;

public class UserQueries {
    public static String SAVE_USER_QUERY = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    public static String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    public static String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    public static String GET_USER_BY_USERNAME_QUERY = "SELECT * FROM users WHERE username = ?";
    public static String GET_USER_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
}
