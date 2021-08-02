package DesktopScheduler.Utility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to connect to the given database.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class DatabaseConnection {

    //Database connection Parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dataBaseName = "WJ06kdn";
    private static final String jdbcUrl = protocol + vendorName + ipAddress + dataBaseName;

    //Driver reference
    private static final String jdbcDriver = "com.mysql.jdbc.Driver";
    private static Connection connection = null;
    private static final String userName = "U06kdn"; // this is username
    private static final String password = "53688792165";


    /**
     * This method start the connection
     *
     * @return - connection
     */
    public static Connection startConnection(){
        try{
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl,userName,password);
            System.out.println("Working");
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This method will close an open connection
     */
    public static void closeConnection(){
        try{
            connection.close();
        }
        catch (Exception e){

        }
    }

    /**
     * This method will get an open connection.
     * @return - connection
     */
    public static Connection getConnection(){
        return connection;
    }
}
