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
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerWindowController {

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

    private CustomerViewWindowController customerViewWindowController;
    private boolean isUpdateCustomer;
    private Customer customerToUpdate;

    public void initialize() {
    }

    public void setAddOrUpdate() throws SQLException {
        countryBox.setItems(CountryDAO.getAllCountries());
        if(isUpdateCustomer){
            setFields();
        }
        else {
            setNewCustomerID();
        }

    }
    @FXML
    public void onSelectCountry() throws SQLException {
        fldBox.setDisable(false);
        fldBox.setValue(null);
        fldBox.setPromptText("State/Province");
        Country countrySelected = countryBox.getValue();
        fldBox.setItems(FLDivisionDAO.getFLDivisionsInCountry(countrySelected.getCountryID()));
    }

    @FXML
    public void onCancelButton() {
        closeWindow();
    }

    @FXML
    public void onConfirmButton() throws SQLException {
        if (checkFields()) {
            Customer customer = createCustomer();
            if(isUpdateCustomer)
            {
                CustomerDAO.updateCustomer(customer);
            }
            else {

                CustomerDAO.insertCustomer(customer);
            }
            customerViewWindowController.updateTable();

            closeWindow();
        } else {
            // Add error message
            System.out.println("Form not complete");
        }
    }

    private boolean checkFields() {
        boolean formsComplete;
        formsComplete = !customerIdText.getText().isEmpty() && !nameText.getText().isEmpty() && !addressText.getText().isEmpty() &&
                !postText.getText().isEmpty() && !phoneText.getText().isEmpty() && fldBox.getValue() != null;
        return formsComplete;
    }

    private void setFields() throws SQLException {
        customerIdText.setText(String.valueOf(customerToUpdate.getId()));
        nameText.setText(customerToUpdate.getName());
        addressText.setText(customerToUpdate.getAddress());
        postText.setText(customerToUpdate.getPostCode());
        phoneText.setText(customerToUpdate.getPhoneNum());
        setCountryBoxToDivision(customerToUpdate.getDivID());
        onSelectCountry();
        setDivisionBox(customerToUpdate.getDivID());
        confirmButton.setText("Update");

    }

    private Customer createCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setId(Integer.parseInt(customerIdText.getText()));
        newCustomer.setName(nameText.getText());
        newCustomer.setAddress(addressText.getText());
        newCustomer.setPostCode(postText.getText());
        newCustomer.setPhoneNum(phoneText.getText());
        newCustomer.setDivID(fldBox.getValue().getDivisionID());
        return newCustomer;
    }


    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void getMainWindowInstance(CustomerViewWindowController controller) {
        this.customerViewWindowController = controller;
    }


    // Lambda expression, it is used to sort the customers descending by their ID and then add 1
    // to the greatest ID to ensure uniqueness
    private void setNewCustomerID() throws SQLException {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        customers.sort(((o1, o2) -> (o2.getId() - o1.getId())));
        int newCustomerID = customers.get(0).getId() + 1;
        customerIdText.setText(String.valueOf(newCustomerID));

    }

    public void setUpdateCustomer(boolean updateCustomer) {
        this.isUpdateCustomer = updateCustomer;
    }

    public void setCustomerToUpdate(Customer customerToUpdate) {
        this.customerToUpdate = customerToUpdate;
    }

    //Lambda expressions used here to search the lists for corresponding IDs
    private void setCountryBoxToDivision(int divID) throws SQLException {
        ObservableList<Country> countries = countryBox.getItems();
        ObservableList<FLDivision> flDivisions = FLDivisionDAO.getAllFLDivisions();
        AtomicInteger countryID = new AtomicInteger();
        flDivisions.forEach((flDivision -> {
            if (flDivision.getDivisionID() == divID) {
                countryID.set(flDivision.getCountryID());
            }
        }));
        countries.forEach((country -> {
            if (countryID.get() == country.getCountryID()) {
                countryBox.setValue(country);
            }
        }));
    }

    //Lambdas used here to search for corresponding IDs
    private void setDivisionBox(int divID) throws SQLException {
        ObservableList<FLDivision> flDivisions = FLDivisionDAO.getFLDivisionsInCountry(countryBox.getValue().getCountryID());
        flDivisions.forEach(flDivision -> {
            if(flDivision.getDivisionID() == divID){
                fldBox.setValue(flDivision);
            }
        });
    }


}
