package database;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;

/**
 * Manages queries sent to the database.
 * This class is a singleton.
 * @author Ruben Eekhof - rubeneekhof@gmail.com
 */
public class DatabaseManager {

    private static DatabaseManager instance;

    private final String connectionUrl = "jdbc:sqlite:C:/sqlite/IdleAway.db";

    public final String createCommandUsageTable = "CREATE TABLE IF NOT EXISTS command_usage (CommandName " +
            "varchar(255), amount int);";

    /**
     * The type of query you want to send.
     * UPDATE has no return value.
     * RETURN has a return value.
     */
    public enum QueryTypes {UPDATE,RETURN}

    /**
     * A private constructor since this class is a singleton.
     */
    private DatabaseManager() {

    }

    /**
     * Returns the DatabaseManager and makes an instance of it if needed.
     * @return The DatabaseManger object
     */
    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Makes a connection to the database.
     * @return the connection as a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Send a query to the database.
     * @param query - The query as a String
     * @param types - The type of query as a QueryTypes
     * @param args - Optional arguments
     * @return ArrayList<String> with the result of the query, null if no result
     */
    public ArrayList<String> query(String query, @NotNull QueryTypes types, String... args) {
        ArrayList<String> result = null;
        try {
            Connection conn = connect();
            Statement stmt;
            if(types.equals(QueryTypes.RETURN)) {
                stmt = conn.prepareStatement(query, args);
                ResultSet results = ((PreparedStatement) stmt).executeQuery();

                ResultSetMetaData rsmd = results.getMetaData();
                int columnCount = rsmd.getColumnCount();
                result =  new ArrayList<>(columnCount);

                while (results.next()) {
                    int i = 1;
                    while(i <= columnCount) {
                        result.add(results.getString(i++));
                    }
                }
            } else {
                stmt = conn.createStatement();
                if(args.length == 0) {
                    stmt.executeUpdate(query);
                } else {
                    stmt.executeUpdate(query, args);
                }
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Creates a new database.
     */
    public void createNewDatabase() {
        String url = "jdbc:sqlite:C:/sqlite/" + "IdleAway.db";
        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates all tables.
     */
    public void createAndFillAllTables() {
        query(createCommandUsageTable, QueryTypes.UPDATE);
    }
}