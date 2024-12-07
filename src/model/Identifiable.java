package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface representing an entity that can be uniquely identified by an ID.
 * Any class implementing this interface should provide a unique ID.
 */
public interface Identifiable {
    /**
     * Gets the unique ID of the entity.
     * @return the ID of the entity
     */
    Integer getID();

    String toCsvFormat();

    static <T> T fromCsvFormat(String csvLine) {
        return null;
    }

    void toPreparedStatement(PreparedStatement stmt) throws SQLException;

    static <T> T fromResultSet(ResultSet rs) throws SQLException {
        return null;
    }
}
