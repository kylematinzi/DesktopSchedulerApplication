package DesktopScheduler.Model;

/**
 * This class creates a user object.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class User {

    private int userId;
    private String userName;
    private String password;

    /**
     * This method is used to create a user object
     * @param userId - unique user id
     * @param userName - user's name
     * @param password - user's password
     */
    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    //setters/getters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method is used to format the string output of the user object.
     * @return - formatted string
     */
    @Override
    public String toString(){
        return "["+userId+"]"+" "+userName;
    }
}
