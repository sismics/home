package com.sismics.home.core.dao.dbi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import com.google.common.collect.Maps;
import com.sismics.home.core.constant.SensorSampleType;
import com.sismics.home.core.dao.dbi.criteria.SensorSampleCriteria;
import com.sismics.home.core.dao.dbi.mapper.SensorSampleMapper;
import com.sismics.home.core.model.dbi.SensorSample;
import com.sismics.util.context.ThreadLocalContext;

/**
 * Sensor sample DAO.
 * 
 * @author bgamard
 */
public class SensorSampleDao {
    /**
     * Creates a new sensor sample.
     * 
     * @param sample Sensor sample to create
     */
    public String create(SensorSample sample) {
        // Init sensor sample data
        sample.setId(UUID.randomUUID().toString());

        // Create sensor sample
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_SENSOR_SAMPLE(SES_ID_C, SES_IDSEN_C, SES_VALUE_N, SES_CREATEDATE_D, SES_TYPE_C)" +
                " values(:id, :sensorId, :value, :createDate, :type)")
                .bind("id", sample.getId())
                .bind("sensorId", sample.getSensorId())
                .bind("value", sample.getValue())
                .bind("type", sample.getType().name())
                .bind("createDate", new Timestamp(sample.getCreateDate().getTime()))
                .execute();

        return sample.getId();
    }
    
    /**
     * Returns samples before a specified date.
     * 
     * @param id Sensor ID
     * @param date Date
     * @param type Sample type
     */
    public List<SensorSample> findAllBefore(String id, Date date, SensorSampleType type) {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<SensorSample> q = handle.createQuery("select " + new SensorSampleMapper().getJoinedColumns("e") +
                "  from T_SENSOR_SAMPLE e " +
                "  where e.SES_TYPE_C = :type " +
                " and e.SES_IDSEN_C = :id " +
                " and e.SES_CREATEDATE_D < :date " +
                "  order by e.SES_CREATEDATE_D")
                .bind("id", id)
                .bind("date", new Timestamp(date.getTime()))
                .bind("type", type.name())
                .mapTo(SensorSample.class);
        return q.list();
    }

    /**
     * Delete a list of samples.
     * 
     * @param idList List of samples IDs
     */
    public void deleteList(List<String> idList) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        Map<String, Object> args = bind(idList);
        handle.createStatement("delete from T_SENSOR_SAMPLE e" +
                "  where e.SES_ID_C in (" + join(args.keySet()) + ")")
                .bindFromMap(args)
                .execute();
    }
    
    // TODO Refactor this :(
    private String join(Set<String> args) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = args.iterator(); it.hasNext();) {
            sb.append(":").append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    // TODO And this!
    private Map<String, Object> bind(List<? extends Object> list) {
        Map<String, Object> params = Maps.newHashMap();
        int i = 0;
        for (Object obj : list) {
            params.put("bind_" + i++, obj);
        }
        return params;
    }

    /**
     * Find samples by criteria.
     * 
     * @param criteria Criteria
     * @return Sample list
     */
    public List<SensorSample> findByCriteria(SensorSampleCriteria criteria) {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<SensorSample> q = handle.createQuery("select " + new SensorSampleMapper().getJoinedColumns("e") +
                " from T_SENSOR_SAMPLE e " +
                " where e.SES_IDSEN_C = :id" +
                " and e.SES_TYPE_C = :type " +
                " order by e.SES_CREATEDATE_D")
                .bind("id", criteria.getSensorId())
                .bind("type", criteria.getType().name())
                .mapTo(SensorSample.class);
        return q.list();
    }
}
