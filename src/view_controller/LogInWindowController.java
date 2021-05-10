package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import DAO.DBConnection;
import DAO.DBQuery;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

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

    @FXML
    public void initialize(){
        setLocale();
        usernameText.setPromptText(rb.getString("username"));
        passwordText.setPromptText(rb.getString("password"));
        userLocationLabel.setText(rb.getString("location") + ": " + locale.getCountry());
    }

    public void setLocale(){
        locale = Locale.getDefault();
        //locale = Locale.FRANCE;
        rb = ResourceBundle.getBundle("language_files/Scheduling", locale);

    }

    @FXML
    public void logInClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        if(checkLogIn()){
            System.out.println("Correct Log In");
            logInSuccessful();
            closeWindow();
        }
        else{
            infoIncorrect.setText(rb.getString("loginIncorrect"));
        }
    }

    private void logInSuccessful() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Choose an Option");
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkLogIn(){
        //return DBQuery.CheckLogIn(usernameText.getText(), passwordText.getText());
        boolean isCorrect = false;
        String checkCredentials = "SELECT User_Name, Password FROM users WHERE User_Name= ? AND Password= ?";
        // "SELECT User_Name, Password FROM users(User_Name, Password) VALUES(?,?)";
        try {
            DBQuery.setPrepareStatement(DBConnection.getConnection(),checkCredentials);
            PreparedStatement ps = DBQuery.getPrepareStatement();
            ps.setString(1, usernameText.getText());
            ps.setString(2, passwordText.getText());
            ResultSet rs = ps.executeQuery();
            isCorrect = rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isCorrect;
    }

    private void closeWindow(){
        Stage stage = (Stage) infoIncorrect.getScene().getWindow();
        stage.close();
    }

}
