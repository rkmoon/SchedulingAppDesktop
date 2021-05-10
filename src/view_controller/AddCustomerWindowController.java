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

    private CustomerWindowController customerWindowController;
    private boolean isUpdateCustomer;

    public void initialize() throws SQLException {
        countryBox.setItems(CountryDAO.getAllCountries());
        setNewCustomerID();
        //customerWindowController
    }

    @FXML
    public void onSelectCountry() throws SQLException {
        fldBox.setDisable(false);
        fldBox.setPromptText("Please select State/Province");
        Country countrySelected = countryBox.getValue();
        fldBox.setItems(FLDivisionDAO.getFLDivisionsInCountry(countrySelected.getCountryID()));
    }

    @FXML
    public void onCancelButton(){
        closeWindow();
    }

    @FXML
    public void onConfirmButton() throws SQLException {
        if(checkFields()) {
            createCustomer();
            customerWindowController.updateTable();
            closeWindow();
        }
        else{
            // Add error message
            System.out.println("Form not complete");
        }
    }

    private boolean checkFields(){
        boolean formsComplete;
        if(customerIdText.getText().isEmpty() || nameText.getText().isEmpty() || addressText.getText().isEmpty() ||
                postText.getText().isEmpty() || phoneText.getText().isEmpty() || fldBox.getValue() == null){
            formsComplete = false;
        }
        else {
            formsComplete = true;
        }

        return formsComplete;
    }

    private void createCustomer() throws SQLException {
        Customer newCustomer = new Customer();
        newCustomer.setId(Integer.parseInt(customerIdText.getText()));
        newCustomer.setName(nameText.getText());
        newCustomer.setAddress(addressText.getText());
        newCustomer.setPostCode(postText.getText());
        newCustomer.setPhoneNum(phoneText.getText());
        newCustomer.setDivID(fldBox.getValue().getDivisionID());
        CustomerDAO.insertCustomer(newCustomer);
    }

    private void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void getMainWindowInstance(CustomerWindowController controller){
        this.customerWindowController = controller;
    }


    // This is where the lambda expression is, it is used to sort the customers descending by their ID and then add 1
    // to the greatest ID to ensure uniqueness
    private void setNewCustomerID() throws SQLException {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        Collections.sort(customers, ((o1, o2) -> (o2.getId() - o1.getId())));
        int newCustomerID = customers.get(0).getId() + 1;
        customerIdText.setText(String.valueOf(newCustomerID));

    }

    public void setUpdateCustomer(boolean updateCustomer) {
        isUpdateCustomer = updateCustomer;
    }
}
