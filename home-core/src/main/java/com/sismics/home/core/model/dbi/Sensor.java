package com.sismics.home.core.model.dbi;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Sensor.
 * 
 * @author bgamard
 */
public class Sensor {
    
    /**
     * Sensor type.
     * 
     * @author bgamard
     */
    public enum Type {
        ELECTRICITY,
        
        TEMPERATURE,
        
        HUMIDITY,
        
        SOUND,
        
        LIGHT
    }
    
    /**
     * ID.
     */
    private String id;
    
    /**
     * Name.
     */
    private String name;
    
    /**
     * Type.
     */
    private Type type;
    
    /**
     * Creation date.
     */
    private Date createDate;
    
    /**
     * Deletion date.
     */
    private Date deleteDate;

    public Sensor() {
    }

    public Sensor(String id, String name, Date createDate, Date deleteDate, Type type) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
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
     * Getter of name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of name.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
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
     * Getter of deleteDate.
     *
     * @return deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * Setter of deleteDate.
     *
     * @param deleteDate deleteDate
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
    
    /**
     * Getter of type.
     * @return type
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter of type.
     * @param type type
     */
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("type", type)
                .toString();
    }
}
