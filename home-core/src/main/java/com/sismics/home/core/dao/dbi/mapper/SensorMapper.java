package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import com.sismics.home.core.model.dbi.Sensor;
import com.sismics.util.dbi.BaseResultSetMapper;

/**
 * Sensor result set mapper.
 *
 * @author bgamard
 */
public class SensorMapper extends BaseResultSetMapper<Sensor> {
    @Override
    public String[] getColumns() {
        return new String[] {
            "SEN_ID_C",
            "SEN_NAME_C",
            "SEN_CREATEDATE_D",
            "SEN_DELETEDATE_D"
        };
    }
    
    @Override
    public Sensor map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final String[] columns = getColumns();
        int column = 0;
        return new Sensor(
                r.getString(columns[column++]),
                r.getString(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getTimestamp(columns[column++]));
    }
}
