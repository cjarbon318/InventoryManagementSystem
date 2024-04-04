
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    // Method to establish connection to SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            
            Class.forName("org.sqlite.JDBC");

            // Database URL
            String url = "jdbc:sqlite:/Users/carliarbon/infosys.db";

            // Create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite database has been established.");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database.");
            e.printStackTrace();
        }
        return conn;
    }

    // Main method to test the connection
    public static void main(String[] args) {
        // Attempt to establish connection
        Connection connection = connect();

        // Perform operations with the connection if it's not null
        if (connection != null) {
            // You can perform database operations here
            // For example:
            try {
                // Close the connection when done
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection.");
                e.printStackTrace();
            }
        }
    }
}
