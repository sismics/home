package com.sismics.home.core.model.dbi;

import java.util.Date;

import com.google.common.base.Objects;
import com.sismics.home.core.constant.SensorSampleType;

/**
 * Sensor sample.
 * 
 * @author bgamard
 */
public class SensorSample {
    /**
     * ID.
     */
    private String id;
    
    /**
     * Sensor ID.
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
    
    /**
     * Type.
     */
    private SensorSampleType type;
    
    public SensorSample() {
    }

    public SensorSample(String id, String sensorId, Date createDate, float value, SensorSampleType type) {
        this.id = id;
        this.sensorId = sensorId;
        this.createDate = createDate;
        this.value = value;
        this.type = type;
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
    public void setType(SensorSampleType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("value", value)
                .add("createDate", createDate)
                .add("type", type)
                .toString();
    }
}
