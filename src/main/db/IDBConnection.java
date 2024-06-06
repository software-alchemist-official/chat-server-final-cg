package main.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnection {
    String HOSTNAME = "localhost";
    String USER = "fernando";
    String PASSWORD = "fernando";
    String PORT = "3306";
    String DB_NAME = "chat_db";

    Connection getConnection() throws SQLException;
}
