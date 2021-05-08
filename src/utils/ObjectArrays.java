package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Appointment;
import main.Customer;

public class ObjectArrays {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static void addCustomer(Customer newCustomer){
        allCustomers.add(newCustomer);
    }

    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }

    public static void addAppointment(Appointment newAppointment){
        allAppointments.add(newAppointment);
    }

    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }

}
