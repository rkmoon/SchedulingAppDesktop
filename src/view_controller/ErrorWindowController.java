package view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class controls the error window. It changes the message the user receives based on the error chosen and given to
 * the utils.Errors class.
 */
public class ErrorWindowController {

    @FXML
    private Label errorLabel;

    /**
     * Changes the error message of the error window based on the error.
     * @param error error to display
     */
    public void changeMessage(String error){
        errorLabel.setText(error);

    }

    public void closeWindow(){
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

}
