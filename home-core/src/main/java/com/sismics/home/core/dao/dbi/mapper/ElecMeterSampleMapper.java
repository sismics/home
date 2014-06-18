package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.sismics.home.core.model.dbi.ElecMeterSample;

/**
 * Electricity meter sample result set mapper.
 *
 * @author bgamard
 */
public class ElecMeterSampleMapper implements ResultSetMapper<ElecMeterSample> {
    @Override
    public ElecMeterSample map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new ElecMeterSample(
                r.getString("EMS_ID_C"),
                r.getString("EMS_IDEMR_C"),
                r.getDate("EMS_CREATEDATE_D"),
                r.getInt("EMS_VALUE_N"));
    }
}
