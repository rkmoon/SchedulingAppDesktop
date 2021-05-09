package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FLDivision;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FLDivisionDAO {

    private static final String tableName = "first_level_divisions";

    public static ObservableList<FLDivision> getAllFLDivisions() throws SQLException {
        ObservableList<FLDivision> flDivisions = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while (rs.next()) {
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
