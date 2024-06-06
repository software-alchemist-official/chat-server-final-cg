package main.service;

public class MessageQueries {
    public static String SAVE_MESSAGE_QUERY = "INSERT INTO messages (user_id, content) VALUES (?, ?)";
}
