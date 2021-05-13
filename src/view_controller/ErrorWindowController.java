package view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorWindowController {

    @FXML
    private Label errorLabel;

    public void changeMessage(String error){
        errorLabel.setText(error);
    }

}
