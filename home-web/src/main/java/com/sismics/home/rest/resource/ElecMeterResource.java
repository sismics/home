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

import com.sismics.home.core.dao.dbi.ElecMeterDao;
import com.sismics.home.core.model.dbi.ElecMeter;
import com.sismics.home.core.model.dbi.ElecMeterSample;
import com.sismics.home.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;

/**
 * Electricity meter REST resources.
 * 
 * @author bgamard
 */
@Path("/elec_meter")
public class ElecMeterResource extends BaseResource {
    /**
     * Creates a new electricity meter.
     *
     * @param name Name
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @FormParam("name") String name) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);

        // Create the electricity meter
        ElecMeter elecMeter = new ElecMeter();
        elecMeter.setName(name);

        ElecMeterDao elecMeterDao = new ElecMeterDao();
        elecMeterDao.create(elecMeter);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }

    /**
     * Creates a new electricity meter sample.
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
            @FormParam("value") Integer value) {
        // Check if the electricity meter exists
        ElecMeterDao elecMeterDao = new ElecMeterDao();
        ElecMeter elecMeter = elecMeterDao.getActiveById(id);
        if (elecMeter == null) {
            throw new ClientException("ElecMeterNotFound", "The electricity meter doesn't exist");
        }
        
        // Validate the input data
        ValidationUtil.validateRequired(value, "value");
        Date date = ValidationUtil.validateDate(dateStr, "date", false);

        // Create the electricity meter sample
        ElecMeterSample sample = new ElecMeterSample();
        sample.setCreateDate(date);
        sample.setValue(value);
        sample.setElecMeterId(id);

        elecMeterDao.createSample(sample);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }
    
    /**
     * Updates electricity meter informations.
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
            @FormParam("name") String name) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);

        // Update the electricity meter
        ElecMeterDao elecMeterDao = new ElecMeterDao();
        ElecMeter elecMeter = elecMeterDao.getActiveById(id);
        elecMeter.setName(name);
        elecMeter = elecMeterDao.update(elecMeter);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }
    
    /**
     * Get an electricity meter.
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

        // Check if the electricity meter exists
        ElecMeterDao elecMeterDao = new ElecMeterDao();
        ElecMeter elecMeter = elecMeterDao.getActiveById(id);
        if (elecMeter == null) {
            throw new ClientException("ElecMeterNotFound", "The electricity meter doesn't exist");
        }
        
        // Get all samples
        List<ElecMeterSample> sampleList = elecMeterDao.findAllSample(id);

        JsonArrayBuilder samples = Json.createArrayBuilder();
        for (ElecMeterSample sample : sampleList) {
            samples.add(Json.createObjectBuilder()
                    .add("date", sample.getCreateDate().getTime())
                    .add("value", sample.getValue()));
        }
        
        // Build the output
        JsonObject json = Json.createObjectBuilder()
                .add("id", elecMeter.getId())
                .add("name", elecMeter.getName())
                .add("samples", samples)
                .build();

        // Always return OK
        return Response.ok()
                .entity(json)
                .build();
    }

    /**
     * Deletes an electricity meter.
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

        // Check if the electricity meter exists
        ElecMeterDao elecMeterDao = new ElecMeterDao();
        ElecMeter elecMeter = elecMeterDao.getActiveById(id);
        if (elecMeter == null) {
            throw new ClientException("ElecMeterNotFound", "The electricity meter doesn't exist");
        }

        // Delete the electricity meter
        elecMeterDao.delete(elecMeter.getId());

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }

    /**
     * Returns all active electricity meters.
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

        ElecMeterDao elecMeterDao = new ElecMeterDao();
        List<ElecMeter> elecMeterList = elecMeterDao.findAll();

        JsonObjectBuilder response = Json.createObjectBuilder();
        JsonArrayBuilder items = Json.createArrayBuilder();
        for (ElecMeter elecMeter : elecMeterList) {
            items.add(Json.createObjectBuilder()
                    .add("id", elecMeter.getId())
                    .add("name", elecMeter.getName()));
        }
        response.add("elec_meters", items);

        return Response.ok().entity(response.build()).build();
    }
}
