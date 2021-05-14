package view_controller;

import DAO.DBConnection;
import DAO.DBQuery;
import DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.LoggedInUser;
import utils.RecordLogInAttempts;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class controls the login window. It will change languages based on the locale of the user, verify login information,
 * and log any attempts to login to a text file.
 */
public class LogInWindowController {

    @FXML
    private TextField usernameText;

    @FXML
    private TextField passwordText;

    @FXML
    private Label userLocationLabel;

    @FXML
    private Button logInButton;

    private String language;

    @FXML
    private Label infoIncorrect;

    Locale locale;

    private ResourceBundle rb;

    /**
     * Initializes the prompt texts and labels with the correct language
     */
    @FXML
    public void initialize(){
        setLocale();
        usernameText.setPromptText(rb.getString("username"));
        passwordText.setPromptText(rb.getString("password"));
        userLocationLabel.setText(rb.getString("location") + ": " + locale.getCountry());
    }

    /**
     * Gets the locale and pulls the correct resource bundle to use for localization
     */
    public void setLocale(){
        locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("language_files/Scheduling", locale);

    }

    /**
     * Checks login information and if correct, closes the window. If incorrect it displays an error message
     * @throws IOException error opening window
     */
    @FXML
    public void logInClicked() throws IOException {
        if(checkLogIn()){
            logInSuccessful();
            closeWindow();
        }
        else{
            infoIncorrect.setText(rb.getString("loginIncorrect"));
        }
    }

    /**
     * Opens the main window of the application
     * @throws IOException error opening the window
     */
    private void logInSuccessful() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CustomerViewWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Scheduling Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Checks to see if the log in information is correct. It then sets the user logged in if correct, and logs
     * the login attempt
     * @return True if login is successful, false if not
     * @throws IOException error writing log to file
     */
    private boolean checkLogIn() throws IOException {
        boolean isCorrect = false;
        String checkCredentials = "SELECT User_Name, Password FROM users WHERE User_Name= ? AND Password= ?";
        try {
            DBQuery.setPrepareStatement(DBConnection.getConnection(),checkCredentials);
            PreparedStatement ps = DBQuery.getPrepareStatement();
            ps.setString(1, usernameText.getText());
            ps.setString(2, passwordText.getText());
            ResultSet rs = ps.executeQuery();
            isCorrect = rs.next();
            if(isCorrect){
                User loggedIn;
                loggedIn = UserDAO.getUser(usernameText.getText());
                LoggedInUser.setLoggedIn(loggedIn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        RecordLogInAttempts.recordAttempt(usernameText.getText(), isCorrect);
        return isCorrect;
    }

    private void closeWindow(){
        Stage stage = (Stage) infoIncorrect.getScene().getWindow();
        stage.close();
    }

}
