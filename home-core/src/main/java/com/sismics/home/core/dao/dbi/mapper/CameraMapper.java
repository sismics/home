package com.sismics.home.core.dao.dbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import com.sismics.home.core.model.dbi.Camera;
import com.sismics.util.dbi.BaseResultSetMapper;

/**
 * Camera result set mapper.
 *
 * @author bgamard
 */
public class CameraMapper extends BaseResultSetMapper<Camera> {
    @Override
    public String[] getColumns() {
        return new String[] {
            "CAM_ID_C",
            "CAM_NAME_C",
            "CAM_CREATEDATE_D",
            "CAM_DELETEDATE_D",
            "CAM_FOLDER_C",
            "CAM_CURRENT_C"
        };
    }
    
    @Override
    public Camera map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        final String[] columns = getColumns();
        int column = 0;
        return new Camera(
                r.getString(columns[column++]),
                r.getString(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getTimestamp(columns[column++]),
                r.getString(columns[column++]),
                r.getString(columns[column++]));
    }
}
