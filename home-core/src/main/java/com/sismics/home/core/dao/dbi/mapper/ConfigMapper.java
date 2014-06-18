package com.sismics.home.core.dao.dbi.mapper;

import com.sismics.home.core.constant.ConfigType;
import com.sismics.home.core.model.dbi.Config;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Config result set mapper.
 *
 * @author jtremeaux
 */
public class ConfigMapper implements ResultSetMapper<Config> {
    @Override
    public Config map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Config(
                ConfigType.valueOf(r.getString("CFG_ID_C")),
                r.getString("CFG_VALUE_C"));
    }
}
