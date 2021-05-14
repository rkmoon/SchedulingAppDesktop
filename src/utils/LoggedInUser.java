package utils;

import model.User;

/**
 * This class contain a reference to the user logged in after a successful login attempt. It can be pulled for any
 * operation requiring needing the user ID.
 */
public class LoggedInUser {
    private static User loggedIn;

    public static User getLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(User loggedIn) {
        LoggedInUser.loggedIn = loggedIn;
    }

    public static String getUserName(){
        return loggedIn.getUsername();
    }

    public static Integer getUserID(){return loggedIn.getUserID();}
}
