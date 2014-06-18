package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import com.sismics.home.core.model.dbi.ElecMeter;
import com.sismics.util.dbi.BaseResultSetMapper;

/**
 * Electricity meter result set mapper.
 *
 * @author bgamard
 */
public class ElecMeterMapper extends BaseResultSetMapper<ElecMeter> {
    @Override
    public String[] getColumns() {
        return new String[] {
            "EMR_ID_C",
            "EMR_NAME_C",
            "EMR_CREATEDATE_D",
            "EMR_DELETEDATE_D"
        };
    }
    
    @Override
    public ElecMeter map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final String[] columns = getColumns();
        int column = 0;
        return new ElecMeter(
                r.getString(columns[column++]),
                r.getString(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getTimestamp(columns[column++]));
    }
}
