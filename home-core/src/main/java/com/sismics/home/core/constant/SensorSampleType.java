package com.sismics.home.core.constant;

/**
 * Sensor sample type.
 * 
 * @author bgamard
 */
public enum SensorSampleType {

    /**
     * Raw sample from sensor input.
     */
    RAW,
    
    /**
     * Sample compacted from all samples from the same minute.
     */
    MINUTE,
    
    /**
     * Sample compacted from all samples from the same hour.
     */
    HOUR
}
