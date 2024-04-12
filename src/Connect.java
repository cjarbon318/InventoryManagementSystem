
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    // Method to establish connection to SQLite database and return the connection object
    
    public static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Database URL (database location)
            String url = "jdbc:sqlite:/Users/carliarbon/infosys.db";

            // Create a connection to the database
            conn = DriverManager.getConnection(url);
            // Print a message to indicate that the connection was successful
            System.out.println("Connection to SQLite database has been established.");

            // Return the connection object  to the calling method and print out error messages if cannot connect to the database or the JDBC driver is not found
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
            // Close the connection
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection.");
                e.printStackTrace();
            }
        }
    }
}
