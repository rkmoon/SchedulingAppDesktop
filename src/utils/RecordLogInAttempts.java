package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class is responsible for writing each login attempt to the file login_activity.txt, located in the root of the
 * project
 */
public class RecordLogInAttempts {
    /**
     * Records the login attempt
     * @param username username used to try and log in
     * @param successful if the login attempt was successful
     * @throws IOException error writing to file
     */
    public static void recordAttempt(String username, boolean successful) throws IOException {
        String success = "";
        File logFile = new File("login_activity.txt");
        if(!logFile.exists()){
            logFile.createNewFile();
        }
        FileWriter writer = new FileWriter("login_activity.txt", true);
        if(successful){
            success = "Successful";
        }
        else {
            success = "Unsuccessful";
        }
        writer.write(username + " " + LocalDateTime.now() + " " + success + "\n");
        writer.close();

    }
}
