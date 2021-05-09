package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FLDivision;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FLDivisionDAO {

    private static final String tableName = "first_level_divisions";
    private static final String countryIdName = "COUNTRY_ID";

    public static ObservableList<FLDivision> getAllFLDivisions() throws SQLException {
        ObservableList<FLDivision> flDivisions = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while (rs.next()) {
            flDivisions.add(fillFLDivision(rs));
        }
        return flDivisions;
    }

    public static ObservableList<FLDivision> getFLDivisionsInCountry(int countryID) throws SQLException{
        ObservableList<FLDivision> flDivisions = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectFromTable(countryIdName + " = " + countryID, tableName);
        while (rs.next()){
            flDivisions.add(fillFLDivision(rs));
        }
        return flDivisions;
    }

    private static FLDivision fillFLDivision(ResultSet rs) throws SQLException {
        FLDivision flDivision = new FLDivision();
        flDivision.setDivisionID(rs.getInt("Division_ID"));
        flDivision.setDivision(rs.getString("Division"));
        flDivision.setCreateDate(rs.getTimestamp("Create_Date"));
        flDivision.setCreatedBy(rs.getString("Created_By"));
        flDivision.setLastUpdate(rs.getTimestamp("Last_Update"));
        flDivision.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        flDivision.setCountryID(rs.getInt("Country_ID"));
        return flDivision;
    }
}
