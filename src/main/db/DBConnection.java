package main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection implements IDBConnection {

    //TODO: retirar o usar.
    private static final Logger LOG = Logger.getLogger(DBConnection.class.getName());


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getURLConnection(), USER, PASSWORD);
    }

    private static String getURLConnection() {
        return String.format("jdbc:mysql://%s:%s/%s", HOSTNAME, PORT, DB_NAME);
    }

}
