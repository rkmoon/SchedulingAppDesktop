package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {

    private static final String tableName = "contacts";

    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while(rs.next()){
            contacts.add(fillContact(rs));
        }
        return contacts;
    }

    private static Contact fillContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setContactID(rs.getInt("Contact_ID"));
        contact.setContactName(rs.getString("Contact_Name"));
        contact.setEmail(rs.getString("Email"));
        return contact;
    }
}
