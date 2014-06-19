package com.sismics.home.core.dao.dbi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import com.sismics.home.core.dao.dbi.mapper.SensorMapper;
import com.sismics.home.core.dao.dbi.mapper.SensorSampleMapper;
import com.sismics.home.core.model.dbi.Sensor;
import com.sismics.home.core.model.dbi.SensorSample;
import com.sismics.util.context.ThreadLocalContext;

/**
 * Electricity meter DAO.
 * 
 * @author bgamard
 */
public class SensorDao {
    /**
     * Creates a new sensor.
     * 
     * @param sensor Sensor to create
     * @return Sensor ID
     */
    public String create(Sensor sensor) {
        // Init sensor data
        sensor.setId(UUID.randomUUID().toString());
        sensor.setCreateDate(new Date());

        // Create sensor
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_SENSOR(SEN_ID_C, SEN_NAME_C, SEN_TYPE_C, SEN_CREATEDATE_D)" +
                " values(:id, :name, :type, :createDate)")
                .bind("id", sensor.getId())
                .bind("name", sensor.getName())
                .bind("type", sensor.getType().name())
                .bind("createDate", sensor.getCreateDate())
                .execute();

        return sensor.getId();
    }
    
    /**
     * Creates a new sensor sample.
     * 
     * @param sample Sensor sample to create
     */
    public String createSample(SensorSample sample) {
        // Init sensor sample data
        sample.setId(UUID.randomUUID().toString());

        // Create sensor sample
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_SENSOR_SAMPLE(SES_ID_C, SES_IDSEN_C, SES_VALUE_N, SES_CREATEDATE_D)" +
                " values(:id, :sensorId, :value, :createDate)")
                .bind("id", sample.getId())
                .bind("sensorId", sample.getSensorId())
                .bind("value", sample.getValue())
                .bind("createDate", new Timestamp(sample.getCreateDate().getTime()))
                .execute();

        return sample.getId();
    }
    
    /**
     * Updates a sensor.
     * 
     * @param sensor Sensor to update
     * @return Updated sensor
     */
    public Sensor update(Sensor sensor) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_SENSOR e set " +
                " e.SEN_NAME_C = :name, " +
                " e.SEN_TYPE_C = :type " +
                " where e.SEN_ID_C = :id and e.SEN_DELETEDATE_D is null")
                .bind("id", sensor.getId())
                .bind("name", sensor.getName())
                .bind("type", sensor.getType().name())
                .execute();

        return sensor;
    }
    
    /**
     * Gets a sensor by its ID.
     * 
     * @param id Sensor ID
     * @return Sensor
     */
    public Sensor getActiveById(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        Query<Sensor> q = handle.createQuery("select " + new SensorMapper().getJoinedColumns("e") +
                "  from T_SENSOR e" +
                "  where e.SEN_ID_C = :id and e.SEN_DELETEDATE_D is null")
                .bind("id", id)
                .mapTo(Sensor.class);
        return q.first();
    }
    
    /**
     * Deletes a sensor.
     * 
     * @param id Sensor's ID
     */
    public void delete(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_SENSOR e" +
                "  set e.SEN_DELETEDATE_D = :deleteDate" +
                "  where e.SEN_ID_C = :id and e.SEN_DELETEDATE_D is null")
                .bind("id", id)
                .bind("deleteDate", new Date())
                .execute();
        
        // TODO Delete samples
    }

    /**
     * Returns the list of all sensors.
     *
     * @return List of sensors
     */
    public List<Sensor> findAll() {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<Sensor> q = handle.createQuery("select " + new SensorMapper().getJoinedColumns("e") +
                "  from T_SENSOR e " +
                "  where e.SEN_DELETEDATE_D is null" +
                "  order by e.SEN_NAME_C")
                .mapTo(Sensor.class);
        return q.list();
    }
    
    /**
     * Returns all samples from a sensor.
     * 
     * @param id ID
     * @return Samples from a sensor
     */
    public List<SensorSample> findAllSample(String id) {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<SensorSample> q = handle.createQuery("select " + new SensorSampleMapper().getJoinedColumns("e") +
                "  from T_SENSOR_SAMPLE e " +
                "  where e.SES_IDSEN_C = :id" +
                "  order by e.SES_CREATEDATE_D")
                .bind("id", id)
                .mapTo(SensorSample.class);
        return q.list();
    }
}
