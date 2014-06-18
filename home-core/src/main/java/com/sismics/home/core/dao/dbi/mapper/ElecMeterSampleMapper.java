package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import com.sismics.home.core.model.dbi.ElecMeterSample;
import com.sismics.util.dbi.BaseResultSetMapper;

/**
 * Electricity meter sample result set mapper.
 *
 * @author bgamard
 */
public class ElecMeterSampleMapper extends BaseResultSetMapper<ElecMeterSample> {
    @Override
    public String[] getColumns() {
        return new String[] {
            "EMS_ID_C",
            "EMS_IDEMR_C",
            "EMS_CREATEDATE_D",
            "EMS_VALUE_N"
        };
    }
    
    @Override
    public ElecMeterSample map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final String[] columns = getColumns();
        int column = 0;
        return new ElecMeterSample(
                r.getString(columns[column++]),
                r.getString(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getInt(columns[column++]));
    }
}
