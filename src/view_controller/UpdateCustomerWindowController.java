package view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UpdateCustomerWindowController {
    @FXML
    private TextField nameText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField postText;

    @FXML
    private TextField phoneText;

    @FXML
    private ComboBox<?> countryBox;

    @FXML
    private ComboBox<?> fldBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;
}
