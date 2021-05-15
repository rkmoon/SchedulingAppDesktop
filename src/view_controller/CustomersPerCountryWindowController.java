package view_controller;

import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Customer;

import java.sql.SQLException;

/**
 * This class controls the Customer per Country window. It goes through each customer and displays the amount in each country.
 */
public class CustomersPerCountryWindowController {

    @FXML
    private Label usCountLabel;

    @FXML
    private Label ukCountLabel;

    @FXML
    private Label canadaCountLabel;

    private int usCount;
    private int ukCount;
    private int canadaCount;

    private final String usString = "U.S";
    private final String ukString = "UK";
    private final String canadaString = "Canada";


    /**
     * Initializes the window by first counting the customers per country and then filling the labels with that information.
     * @throws SQLException error with the db
     */
    @FXML
    public void initialize() throws SQLException {
        countCustomers();
        fillLabels();
    }

    /**
     * Closes the window
     */
    @FXML
    void closeWindow() {
        Stage stage = (Stage) usCountLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Gets each customer and adds them to the total number of customers in that country. The lambda function is used
     * to go through each customer.
     * @throws SQLException error in the DB
     */
    private void countCustomers() throws SQLException {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        customers.forEach(customer -> {
            addOne(customer.getCountry());
        });

    }

    /**
     * Fills the labels with the total number of customers in each country
     */
    private void fillLabels(){
        usCountLabel.setText(String.valueOf(usCount));
        ukCountLabel.setText(String.valueOf(ukCount));
        canadaCountLabel.setText(String.valueOf(canadaCount));
    }

    /**
     * Adds one to the total of number of customers in each country
     * @param country country that the customer is in
     */
    private void addOne(String country){
        if(country.equals(usString)){
            usCount+=1;
        }
        if(country.equals(ukString)){
            ukCount+=1;
        }
        if(country.equals(canadaString)){
            canadaCount += 1;
        }
    }

}
