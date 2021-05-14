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
import utils.Errors;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class controls the Customer window. It can be used for both updating and creating a customer. It will then either
 * update or add the customer to the database according to how it was launched.
 */
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


    /**
     * Sets fields with customer information if updating, sets new customer ID otherwise. Also sets country combo box.
     * @throws SQLException
     */
    public void setAddOrUpdate() throws SQLException {
        countryBox.setItems(CountryDAO.getAllCountries());
        if(isUpdateCustomer){
            setFields();
        }
        else {
            setNewCustomerID();
        }

    }

    /**
     * Executes when a country is selected. It enables the division combo box and fills it with the divisions in that
     * country allowing the user to select the division the customer belongs to.
     * @throws SQLException error with the DB
     */
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

    /**
     * Checks to see if all fields are filled, then either adds or updates the customer based on the information entered.
     * Closes the window afterwards.
     * @throws SQLException error with the DB
     * @throws IOException error opening the error window
     */
    @FXML
    public void onConfirmButton() throws SQLException, IOException {
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
            Errors.openErrorMenu(Errors.getNoSelection());
        }
    }

    /**
     * Checks to see if all of the fields are not empty
     * @return True if all fields are filled, false otherwise
     */
    private boolean checkFields() {
        boolean formsComplete;
        formsComplete = !customerIdText.getText().isEmpty() && !nameText.getText().isEmpty() && !addressText.getText().isEmpty() &&
                !postText.getText().isEmpty() && !phoneText.getText().isEmpty() && fldBox.getValue() != null;
        return formsComplete;
    }

    /**
     * Fills all of the fields with the customer's previous information if updating.
     * @throws SQLException error with the DB
     */
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

    /**
     * Takes the information filled in the forms and creates a new Customer Object
     * @return new Customer created
     */
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

    /**
     * Gets an instance of the main window. Used to update the table in the main window after adding or updating a
     * customer
     * @param controller controller this controller is opened from
     */
    public void getMainWindowInstance(CustomerViewWindowController controller) {
        this.customerViewWindowController = controller;
    }


    /**
     * Sets a new unique customer ID by finding the highest ID and adding 1 to it. The lambda expression
     * is used to sort the customers descending by their ID and then add 1 to the greatest ID to ensure uniqueness
     * @throws SQLException error with the DB
     */
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

    /**
     * Sets the country combobox to the correct country according to the division ID of the customer when updating the
     * customer The lambda expressions are used here to search the lists for corresponding IDs
     * @param divID the division ID of the customer
     * @throws SQLException error with the DB
     */
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

    /**
     * Sets the division combobox to be the correct one when updating a customer based on the division ID of the customer.
     * The lambda expression is to go through each division and find the division with the corresponding division ID.
     * @param divID the division ID of the customer
     * @throws SQLException error with the DB
     */
    private void setDivisionBox(int divID) throws SQLException {
        ObservableList<FLDivision> flDivisions = FLDivisionDAO.getFLDivisionsInCountry(countryBox.getValue().getCountryID());
        flDivisions.forEach(flDivision -> {
            if(flDivision.getDivisionID() == divID){
                fldBox.setValue(flDivision);
            }
        });
    }


}
