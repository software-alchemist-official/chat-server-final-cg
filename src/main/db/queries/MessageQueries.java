package main.db.queries;

public class MessageQueries {
    public final static String SAVE_MESSAGE_QUERY = "INSERT INTO messages (user_id, content) VALUES (?, ?)";
    public final static String GET_MESSAGE_BY_ID = "SELECT * FROM messages WHERE id = ?";
    public final static String GET_LAST_MESSAGES = "SELECT * FROM messages ORDER BY timestamp DESC LIMIT ?";

}
