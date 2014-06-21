package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import com.sismics.home.core.constant.SensorSampleType;
import com.sismics.home.core.model.dbi.SensorSample;
import com.sismics.util.dbi.BaseResultSetMapper;

/**
 * Sensor sample result set mapper.
 *
 * @author bgamard
 */
public class SensorSampleMapper extends BaseResultSetMapper<SensorSample> {
    @Override
    public String[] getColumns() {
        return new String[] {
            "SES_ID_C",
            "SES_IDSEN_C",
            "SES_CREATEDATE_D",
            "SES_VALUE_N",
            "SES_TYPE_C"
        };
    }
    
    @Override
    public SensorSample map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final String[] columns = getColumns();
        int column = 0;
        return new SensorSample(
                r.getString(columns[column++]),
                r.getString(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getFloat(columns[column++]),
                SensorSampleType.valueOf(r.getString(columns[column++])));
    }
}
