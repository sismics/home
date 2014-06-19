package com.sismics.home.core.model.dbi;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Electricity meter sample.
 * 
 * @author bgamard
 */
public class SensorSample {
    /**
     * ID.
     */
    private String id;
    
    /**
     * Electricity meter ID.
     */
    private String sensorId;
    
    /**
     * Value.
     */
    private float value;
    
    /**
     * Creation date.
     */
    private Date createDate;
    
    public SensorSample() {
    }

    public SensorSample(String id, String elecMeterId, Date createDate, float value) {
        this.id = id;
        this.sensorId = elecMeterId;
        this.createDate = createDate;
        this.value = value;
    }

    /**
     * Getter of id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter of id.
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter of createDate.
     *
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Setter of createDate.
     *
     * @param createDate createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
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
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Getter of value.
     *
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * Setter of value.
     *
     * @param value value
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("value", value)
                .toString();
    }
}
