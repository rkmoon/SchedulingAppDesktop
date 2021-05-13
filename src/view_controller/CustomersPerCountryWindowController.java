package view_controller;

import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Customer;

import java.sql.SQLException;

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



    @FXML
    public void initialize() throws SQLException {
        countCustomers();
        fillLabels();
    }
    @FXML
    void closeWindow() {
        Stage stage = (Stage) usCountLabel.getScene().getWindow();
        stage.close();
    }

    private void countCustomers() throws SQLException {
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        customers.forEach(customer -> {
            addOne(customer.getCountry());
        });

    }

    private void fillLabels(){
        usCountLabel.setText(String.valueOf(usCount));
        ukCountLabel.setText(String.valueOf(ukCount));
        canadaCountLabel.setText(String.valueOf(canadaCount));
    }

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
