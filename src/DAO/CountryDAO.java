package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDAO {

    private static final String tableName = "countries";

    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while (rs.next()) {
            countries.add(fillCountry(rs));
        }
        return countries;
    }

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
