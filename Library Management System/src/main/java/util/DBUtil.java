package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:/Users/ceciliadinesh/Desktop/Library Management System/src/main/resources/db/library.db";

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("🔗 Connected to: " + DB_URL);
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
