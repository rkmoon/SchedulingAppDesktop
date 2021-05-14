package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to handle all queries to the database for the Country table
 */
public class CountryDAO {

    private static final String tableName = "countries";

    /**
     * Retrieves all countries from the database
     * @return list of all countries
     * @throws SQLException error with the DB
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while (rs.next()) {
            countries.add(fillCountry(rs));
        }
        return countries;
    }

    /**
     * Fills a Country object using the resultset given
     * @param rs resultset to create the Country class from
     * @return Country filled by the resultset data
     * @throws SQLException error with the DB
     */
    private static Country fillCountry(ResultSet rs) throws SQLException {
        Country country = new Country();
        country.setCountryID(rs.getInt("Country_ID"));
        country.setCountry(rs.getString("Country"));
        country.setCreateDate(rs.getTimestamp("Create_Date"));
        country.setCreatedBy(rs.getString("Created_By"));
        country.setLastUpdate(rs.getTimestamp("Last_Update"));
        country.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        return country;
    }
}
