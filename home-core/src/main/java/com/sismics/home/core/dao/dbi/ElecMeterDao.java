package com.sismics.home.core.dao.dbi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import com.sismics.home.core.dao.dbi.mapper.ElecMeterMapper;
import com.sismics.home.core.dao.dbi.mapper.ElecMeterSampleMapper;
import com.sismics.home.core.model.dbi.ElecMeter;
import com.sismics.home.core.model.dbi.ElecMeterSample;
import com.sismics.util.context.ThreadLocalContext;

/**
 * Electricity meter DAO.
 * 
 * @author bgamard
 */
public class ElecMeterDao {
    /**
     * Creates a new electricity meter.
     * 
     * @param elecMeter Electricity meter to create
     * @return Electricity meter ID
     */
    public String create(ElecMeter elecMeter) {
        // Init electricity meter data
        elecMeter.setId(UUID.randomUUID().toString());
        elecMeter.setCreateDate(new Date());

        // Create electricity meter
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_ELEC_METER(EMR_ID_C, EMR_NAME_C, EMR_CREATEDATE_D)" +
                " values(:id, :name, :createDate)")
                .bind("id", elecMeter.getId())
                .bind("name", elecMeter.getName())
                .bind("createDate", elecMeter.getCreateDate())
                .execute();

        return elecMeter.getId();
    }
    
    /**
     * Creates a new electricity meter sample.
     * 
     * @param sample Electricity meter sample to create
     */
    public String createSample(ElecMeterSample sample) {
        // Init electricity meter sample data
        sample.setId(UUID.randomUUID().toString());

        // Create electricity meter sample
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_ELEC_METER_SAMPLE(EMS_ID_C, EMS_IDEMR_C, EMS_VALUE_N, EMS_CREATEDATE_D)" +
                " values(:id, :elecMeterId, :value, :createDate)")
                .bind("id", sample.getId())
                .bind("elecMeterId", sample.getElecMeterId())
                .bind("value", sample.getValue())
                .bind("createDate", new Timestamp(sample.getCreateDate().getTime()))
                .execute();

        return sample.getId();
    }
    
    /**
     * Updates an electricity meter.
     * 
     * @param elecMeter Electricity meter to update
     * @return Updated electricity meter
     */
    public ElecMeter update(ElecMeter elecMeter) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_ELEC_METER e set " +
                " e.EMR_NAME_C = :name " +
                " where e.EMR_ID_C = :id and e.EMR_DELETEDATE_D is null")
                .bind("id", elecMeter.getId())
                .bind("name", elecMeter.getName())
                .execute();

        return elecMeter;
    }
    
    /**
     * Gets an electricity meter by its ID.
     * 
     * @param id Electricity meter ID
     * @return Electricity meter
     */
    public ElecMeter getActiveById(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        Query<ElecMeter> q = handle.createQuery("select " + new ElecMeterMapper().getJoinedColumns("e") +
                "  from T_ELEC_METER e" +
                "  where e.EMR_ID_C = :id and e.EMR_DELETEDATE_D is null")
                .bind("id", id)
                .mapTo(ElecMeter.class);
        return q.first();
    }
    
    /**
     * Deletes an electricity meter.
     * 
     * @param id Electricity meter's ID
     */
    public void delete(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_ELEC_METER e" +
                "  set e.EMR_DELETEDATE_D = :deleteDate" +
                "  where e.EMR_ID_C = :id and e.EMR_DELETEDATE_D is null")
                .bind("id", id)
                .bind("deleteDate", new Date())
                .execute();
    }

    /**
     * Returns the list of all electricity meters.
     *
     * @return List of electricity meters
     */
    public List<ElecMeter> findAll() {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<ElecMeter> q = handle.createQuery("select " + new ElecMeterMapper().getJoinedColumns("e") +
                "  from T_ELEC_METER e " +
                "  where e.EMR_DELETEDATE_D is null" +
                "  order by e.EMR_NAME_C")
                .mapTo(ElecMeter.class);
        return q.list();
    }
    
    /**
     * Returns all samples from an electricity meter.
     * 
     * @param id ID
     * @return Samples from an electricity meter
     */
    public List<ElecMeterSample> findAllSample(String id) {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<ElecMeterSample> q = handle.createQuery("select " + new ElecMeterSampleMapper().getJoinedColumns("e") +
                "  from T_ELEC_METER_SAMPLE e " +
                "  where e.EMS_IDEMR_C = :id" +
                "  order by e.EMS_CREATEDATE_D")
                .bind("id", id)
                .mapTo(ElecMeterSample.class);
        return q.list();
    }
}
