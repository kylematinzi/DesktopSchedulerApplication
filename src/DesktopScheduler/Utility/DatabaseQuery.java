package DesktopScheduler.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This method will query the database
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class DatabaseQuery {

    private static PreparedStatement preparedStatement;

    /**
     * This method sets the prepared statement for the database.
     * @param conn - connection
     * @param sqlStatement - statement for the database
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {

        preparedStatement = conn.prepareStatement(sqlStatement);

    }

    /**
     * This method will get the prepared statement.
     * @return - prepared statement
     */
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }

}
