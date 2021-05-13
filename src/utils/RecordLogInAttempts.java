package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class RecordLogInAttempts {
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
