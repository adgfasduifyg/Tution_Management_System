package Ultility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnector {
    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;"
            + "database=Northwind;"
            + "integratedSecurity=true;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    // Get a connection to SQL Server
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL);
    }
}