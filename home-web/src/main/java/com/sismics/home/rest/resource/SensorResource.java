package com.sismics.home.rest.resource;

import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sismics.home.core.constant.SensorSampleType;
import com.sismics.home.core.dao.dbi.SensorDao;
import com.sismics.home.core.model.dbi.Sensor;
import com.sismics.home.core.model.dbi.Sensor.Type;
import com.sismics.home.core.model.dbi.SensorSample;
import com.sismics.home.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;

/**
 * Sensor REST resources.
 * 
 * @author bgamard
 */
@Path("/sensor")
public class SensorResource extends BaseResource {
    /**
     * Creates a new sensor.
     *
     * @param name Name
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @FormParam("name") String name,
            @FormParam("type") String typeStr) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);
        ValidationUtil.validateRequired(typeStr, "type");
        Type type = Type.valueOf(typeStr);

        // Create the electricity meter
        Sensor sensor = new Sensor();
        sensor.setName(name);
        sensor.setType(type);

        SensorDao sensorDao = new SensorDao();
        String sensorId = sensorDao.create(sensor);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder()
                        .add("status", "ok")
                        .add("id", sensorId)
                        .build())
                .build();
    }

    /**
     * Creates a new sensor sample.
     *
     * @param name Name
     * @return Response
     */
    @PUT
    @Path("{id: [a-z0-9\\-]+}/sample")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSample(
            @PathParam("id") String id,
            @FormParam("date") String dateStr,
            @FormParam("value") Float value) {
        // Check if the sensor exists
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getActiveById(id);
        if (sensor == null) {
            throw new ClientException("SensorNotFound", "The sensor doesn't exist");
        }
        
        // Validate the input data
        ValidationUtil.validateRequired(value, "value");
        Date date = ValidationUtil.validateDate(dateStr, "date", false);

        // Create the sensor sample
        SensorSample sample = new SensorSample();
        sample.setCreateDate(date);
        sample.setValue(value);
        sample.setSensorId(id);
        sample.setType(SensorSampleType.RAW);

        sensorDao.createSample(sample);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }
    
    /**
     * Updates sensor informations.
     *
     * @param id ID
     * @param name Name
     * @return Response
     */
    @POST
    @Path("{id: [a-z0-9\\-]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("type") String typeStr) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);
        ValidationUtil.validateRequired(typeStr, "type");
        Type type = Type.valueOf(typeStr);

        // Update the sensor
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getActiveById(id);
        sensor.setName(name);
        sensor.setType(type);
        sensor = sensorDao.update(sensor);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }
    
    /**
     * Get a sensor.
     *
     * @param id ID
     * @return Response
     */
    @GET
    @Path("{id: [a-z0-9\\-]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        // Check if the sensor exists
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getActiveById(id);
        if (sensor == null) {
            throw new ClientException("SensorNotFound", "The sensor doesn't exist");
        }
        
        // Get all samples
        List<SensorSample> sampleList = sensorDao.findAllSample(id);

        JsonArrayBuilder samples = Json.createArrayBuilder();
        for (SensorSample sample : sampleList) {
            samples.add(Json.createObjectBuilder()
                    .add("date", sample.getCreateDate().getTime())
                    .add("value", sample.getValue()));
        }
        
        // Build the output
        JsonObject json = Json.createObjectBuilder()
                .add("id", sensor.getId())
                .add("name", sensor.getName())
                .add("type", sensor.getType().name())
                .add("samples", samples)
                .build();

        // Always return OK
        return Response.ok()
                .entity(json)
                .build();
    }

    /**
     * Deletes a sensor.
     *
     * @param id ID
     * @return Response
     */
    @DELETE
    @Path("{id: [a-z0-9\\-]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Check if the sensor exists
        SensorDao sensorDao = new SensorDao();
        Sensor sensor = sensorDao.getActiveById(id);
        if (sensor == null) {
            throw new ClientException("SensorNotFound", "The sensor doesn't exist");
        }

        // Delete the sensor
        sensorDao.delete(sensor.getId());

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }

    /**
     * Returns all active sensors.
     *
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        SensorDao sensorDao = new SensorDao();
        List<Sensor> sensorList = sensorDao.findAll();

        JsonObjectBuilder response = Json.createObjectBuilder();
        JsonArrayBuilder items = Json.createArrayBuilder();
        for (Sensor sensor : sensorList) {
            items.add(Json.createObjectBuilder()
                    .add("id", sensor.getId())
                    .add("name", sensor.getName())
                    .add("type", sensor.getType().name()));
        }
        response.add("sensors", items);

        return Response.ok().entity(response.build()).build();
    }
}
