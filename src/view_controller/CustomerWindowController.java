package view_controller;


import DAO.CustomerDAO;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerWindowController {

    @FXML
    private TableView<Customer> custTable;

    @FXML
    private TableColumn<?, ?> custIdCol;

    @FXML
    private TableColumn<?, ?> custNameCol;

    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private TableColumn<Customer, String> custPostCol;

    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custDivisionCol;

    @FXML
    private TableColumn<Customer, String> custCountryCol;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button appointmentButton;


    @FXML
    public void initialize() throws SQLException {
        populateCustTable();

    }


    private void populateCustTable() throws SQLException {
        FilteredList<Customer> customers = new FilteredList<>(CustomerDAO.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        custTable.setItems(customers);
    }



    @FXML
    public void openAddCustomerWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomerWindow.fxml"));
        Parent root = loader.load();
        AddCustomerWindowController addCustomerWindowController = loader.<AddCustomerWindowController>getController();
        addCustomerWindowController.getMainWindowInstance(this);
        addCustomerWindowController.setUpdateCustomer(false);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer Window");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToAppointments() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AppointmentWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
        closeWindow();
    }

    @FXML void deleteCustomer() throws SQLException {
        Customer custToDelete = custTable.getSelectionModel().getSelectedItem();
        if(custToDelete == null){
            System.out.println("No Customer Selected");
            //ADD ERROR BOX
        }
        else {
            //ADD CONFIRMATION BOX
            CustomerDAO.deleteCustomer(custToDelete);
            updateTable();
        }
    }

    public void updateTable() throws SQLException {
        populateCustTable();
    }

    private void closeWindow(){
        Stage stage = (Stage) appointmentButton.getScene().getWindow();
        stage.close();
    }

}