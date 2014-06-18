package com.sismics.home.core.model.dbi;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Electricity meter sample.
 * 
 * @author bgamard
 */
public class ElecMeterSample {
    /**
     * ID.
     */
    private String id;
    
    /**
     * Electricity meter ID.
     */
    private String elecMeterId;
    
    /**
     * Value.
     */
    private int value;
    
    /**
     * Creation date.
     */
    private Date createDate;
    
    public ElecMeterSample() {
    }

    public ElecMeterSample(String id, String elecMeterId, Date createDate, int value) {
        this.id = id;
        this.elecMeterId = elecMeterId;
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
     * Getter of elecMeterId.
     *
     * @return the elecMeterId
     */
    public String getElecMeterId() {
        return elecMeterId;
    }

    /**
     * Setter of elecMeterId.
     *
     * @param elecMeterId elecMeterId
     */
    public void setElecMeterId(String elecMeterId) {
        this.elecMeterId = elecMeterId;
    }

    /**
     * Getter of value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Setter of value.
     *
     * @param value value
     */
    public void setValue(int value) {
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
