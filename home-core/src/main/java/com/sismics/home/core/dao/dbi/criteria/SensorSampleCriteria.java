package com.sismics.home.core.dao.dbi.criteria;

import com.sismics.home.core.constant.SensorSampleType;

/**
 * Sensor sample criteria.
 *
 * @author bgamard
 */
public class SensorSampleCriteria {
    /**
     * Sensor ID.
     */
    private String sensorId;
    
    /**
     * Sensor sample type.
     */
    private SensorSampleType type;

    /**
     * Getter of sensorId.
     *
     * @return the sensorId
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Setter of sensorId.
     *
     * @param sensorId sensorId
     */
    public SensorSampleCriteria setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    /**
     * Getter of type.
     *
     * @return the type
     */
    public SensorSampleType getType() {
        return type;
    }

    /**
     * Setter of type.
     *
     * @param type type
     */
    public SensorSampleCriteria setType(SensorSampleType type) {
        this.type = type;
        return this;
    }
}
