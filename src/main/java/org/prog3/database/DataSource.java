package org.prog3.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private final static int defaultPort = 5432;
    private final String hostname = System.getenv("DATABASE_HOST");
    private final String user = System.getenv("DATABASE_USER");
    private final String password = System.getenv("DATABASE_PASSWORD");
    private final String database = System.getenv("DATABASE_NAME");
    private final String urlDataBase;
    //instance for datasource because urldatabase is final and give it in construstor instead inside getconnection
    public DataSource() {
        urlDataBase = "jdbc:postgresql://" + hostname + ":" + defaultPort + "/" + database;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(urlDataBase, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

