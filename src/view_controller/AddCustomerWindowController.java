package view_controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FLDivisionDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FLDivision;

import java.sql.SQLException;
import java.util.Collections;

public class AddCustomerWindowController {

    @FXML
    private TextField customerIdText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField postText;

    @FXML
    private TextField phoneText;

    @FXML
    private ComboBox<Country> countryBox;

    @FXML
    private ComboBox<FLDivision> fldBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    public void initialize() throws SQLException {
        countryBox.setItems(CountryDAO.getAllCountries());
        setNewCustomerID();

    }

    @FXML
    public void onSelectCountry() throws SQLException {
        fldBox.setDisable(false);
        fldBox.setPromptText("Please select State/Province");
        Country countrySelected = countryBox.getValue();
        fldBox.setItems(FLDivisionDAO.getFLDivisionsInCountry(countrySelected.getCountryID()));
    }

    @FXML
    public void onConfirmButton() {

        closeWindow();
    }

    @FXML
    public void onCancelButton(){
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    // This is where the lambda expression is, it is used to sort the customers descending by their ID and then add 1
    // to the greatest ID to ensure uniqueness
    private void setNewCustomerID() throws SQLException {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        Collections.sort(customers, ((o1, o2) -> (o2.getId() - o1.getId())));
        int newCustomerID = customers.get(0).getId() + 1;
        customerIdText.setText(String.valueOf(newCustomerID));

    }

}
