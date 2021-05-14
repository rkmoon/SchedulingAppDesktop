package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class used to perform all queries related to the contacts table
 */
public class ContactDAO {

    private static final String tableName = "contacts";

    /**
     * Retrieves all contacts from the database
     * @return list of all contacts
     * @throws SQLException error with the DB
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while(rs.next()){
            contacts.add(fillContact(rs));
        }
        return contacts;
    }

    /**
     * Takes the resultset and turns it into a Contact Object
     * @param rs resultset to retrieve information from
     * @return Contact from the resultset
     * @throws SQLException error with the DB
     */
    private static Contact fillContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setContactID(rs.getInt("Contact_ID"));
        contact.setContactName(rs.getString("Contact_Name"));
        contact.setEmail(rs.getString("Email"));
        return contact;
    }
}
