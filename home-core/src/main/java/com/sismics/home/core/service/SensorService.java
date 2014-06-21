package com.sismics.home.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sismics.home.core.constant.SensorSampleType;
import com.sismics.home.core.dao.dbi.SensorDao;
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
        SensorDao sensorDao = new SensorDao();
        List<Sensor> sensorList = sensorDao.findAll();
        Date rawDate = DateTime.now().minuteOfHour().roundFloorCopy().minusMinutes(2).toDate();
        
        for (Sensor sensor : sensorList) {
            // Every minute, compact all (RAW) samples which are 2 minutes old by minutly packs (MINUTE)
            List<SensorSample> rawSampleList = sensorDao.getRawSamplesBefore(sensor.getId(), rawDate);
            Map<Date, List<Float>> minuteSampleMap = Maps.newHashMap();
            List<String> rawSampleIdList = Lists.newArrayList();
            for (SensorSample rawSample : rawSampleList) {
                rawSampleIdList.add(rawSample.getId());
                Date minuteDate = new DateTime(rawSample.getCreateDate()).minuteOfHour().roundFloorCopy().toDate();
                if (minuteSampleMap.containsKey(minuteDate)) {
                    minuteSampleMap.get(minuteDate).add(rawSample.getValue());
                } else {
                    minuteSampleMap.put(minuteDate, Lists.newArrayList(rawSample.getValue()));
                }
            }
            
            // Create new samples
            for (Entry<Date, List<Float>> entry : minuteSampleMap.entrySet()) {
                float mean = 0;
                for (Float value : entry.getValue()) {
                    mean += value;
                }
                mean /= entry.getValue().size();
                SensorSample sensorSample =
                        new SensorSample(null, sensor.getId(), entry.getKey(), mean, SensorSampleType.MINUTE);
                sensorDao.createSample(sensorSample);
            }
            
            // Delete raw samples
            sensorDao.deleteSampleList(rawSampleIdList);
            
            // TODO At 12H and 24H, compact all (MINUTE) samples by hourly packs (HOUR)
            // TODO Execute daily compacting after minute one
        }
    }
}
