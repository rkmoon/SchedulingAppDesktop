package utils;

import model.User;

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
}
