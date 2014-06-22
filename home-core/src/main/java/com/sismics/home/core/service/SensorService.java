package com.sismics.home.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sismics.home.core.constant.SensorSampleType;
import com.sismics.home.core.dao.dbi.SensorDao;
import com.sismics.home.core.dao.dbi.SensorSampleDao;
import com.sismics.home.core.model.dbi.Sensor;
import com.sismics.home.core.model.dbi.SensorSample;
import com.sismics.home.core.util.TransactionUtil;

/**
 * Sensor service.
 * 
 * @author bgamard
 */
public class SensorService extends AbstractScheduledService {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(SensorService.class);

    public SensorService() {
    }
    
    @Override
    protected void startUp() {
    }

    @Override
    protected void shutDown() {
    }

    @Override
    protected void runOneIteration() {
        try {
            TransactionUtil.handle(new Runnable() {
                @Override
                public void run() {
                    compactSensors();
                }
            });
        } catch (Exception e) {
            // Swallow exceptions, otherwise the service will stop forever
            log.error("SensorService iteration error", e);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Compact sensors data.
     */
    public void compactSensors() {
        compactSensors(Duration.standardMinutes(2), DateTimeFieldType.minuteOfHour(), SensorSampleType.RAW, SensorSampleType.MINUTE);
        compactSensors(Duration.standardHours(6), DateTimeFieldType.hourOfDay(), SensorSampleType.MINUTE, SensorSampleType.HOUR);
        compactSensors(Duration.standardDays(30), DateTimeFieldType.dayOfMonth(), SensorSampleType.HOUR, SensorSampleType.DAY);
    }

    /**
     * Compact sensors data.
     * 
     * @param beforeDuration Compact all sample before this duration
     * @param dateTimeFieldType Field type to round on
     * @param fromSensorSampleType Sample type to compact
     * @param toSensorSampleType Sample type to create
     */
    private void compactSensors(Duration beforeDuration, DateTimeFieldType dateTimeFieldType, SensorSampleType fromSensorSampleType, SensorSampleType toSensorSampleType) {
        SensorDao sensorDao = new SensorDao();
        SensorSampleDao sensorSampleDao = new SensorSampleDao();
        List<Sensor> sensorList = sensorDao.findAll();
        Date beforeDate = DateTime.now().property(dateTimeFieldType).roundFloorCopy().minus(beforeDuration).toDate();
        log.info("Compacting all " + fromSensorSampleType + " samples before: " + beforeDate);
        
        for (Sensor sensor : sensorList) {
            // Compact all fromSensorSampleType samples which are beforeDuration old by toSensorSampleType packs
            List<SensorSample> sampleList = sensorSampleDao.findAllBefore(sensor.getId(), beforeDate, fromSensorSampleType);
            log.info(sampleList.size() + " " + fromSensorSampleType + " samples to compact for sensor: " + sensor);
            
            Map<Date, List<Float>> sampleMap = Maps.newHashMap();
            List<String> sampleIdList = Lists.newArrayList();
            for (SensorSample sample : sampleList) {
                sampleIdList.add(sample.getId());
                Date date = new DateTime(sample.getCreateDate()).property(dateTimeFieldType).roundFloorCopy().toDate();
                if (sampleMap.containsKey(date)) {
                    sampleMap.get(date).add(sample.getValue());
                } else {
                    sampleMap.put(date, Lists.newArrayList(sample.getValue()));
                }
            }
            
            // Create new samples
            log.info(fromSensorSampleType + " samples compacted into " + sampleMap.size() + " " + toSensorSampleType + " samples");
            for (Entry<Date, List<Float>> entry : sampleMap.entrySet()) {
                float mean = 0;
                for (Float value : entry.getValue()) {
                    mean += value;
                }
                mean /= entry.getValue().size();
                SensorSample sensorSample =
                        new SensorSample(null, sensor.getId(), entry.getKey(), mean, toSensorSampleType);
                sensorSampleDao.create(sensorSample);
            }
            
            // Delete fromSensorSampleType samples
            sensorSampleDao.deleteList(sampleIdList);
        }
    }
}
