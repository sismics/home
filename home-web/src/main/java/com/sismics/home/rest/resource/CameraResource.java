package com.sismics.home.rest.resource;

import java.io.File;
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

import com.sismics.home.core.dao.dbi.CameraDao;
import com.sismics.home.core.model.dbi.Camera;
import com.sismics.home.rest.constant.BaseFunction;
import com.sismics.home.rest.util.JsonUtil;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.util.ValidationUtil;

/**
 * Camera REST resources.
 * 
 * @author bgamard
 */
@Path("/camera")
public class CameraResource extends BaseResource {
    /**
     * Creates a new camera.
     *
     * @param name Name
     * @param folder Folder
     * @return Response
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @FormParam("name") String name,
            @FormParam("folder") String folder) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100);
        ValidationUtil.validateDirectory(folder, "folder", false);

        // Create the camera
        Camera camera = new Camera();
        camera.setName(name);
        camera.setFolder(folder);

        CameraDao cameraDao = new CameraDao();
        String cameraId = cameraDao.create(camera);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder()
                        .add("status", "ok")
                        .add("id", cameraId)
                        .build())
                .build();
    }

    /**
     * Updates camera informations.
     *
     * @param id ID
     * @param name Name
     * @param folder Folder
     * @param current Current
     * @return Response
     */
    @POST
    @Path("{id: [a-z0-9\\-]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") String id,
            @FormParam("name") String name,
            @FormParam("folder") String folder,
            @FormParam("current") String current) {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Validate the input data
        name = ValidationUtil.validateLength(name, "name", 1, 100, true);
        ValidationUtil.validateDirectory(folder, "folder", true);
        current = ValidationUtil.validateLength(current, "current", 1, 1000, true);

        // Update the camera
        CameraDao cameraDao = new CameraDao();
        Camera camera = cameraDao.getActiveById(id);
        if (name != null) {
            camera.setName(name);
        }
        if (folder != null) {
            camera.setFolder(folder);
        }
        if (current != null) {
            camera.setCurrent(current);
        }
        camera = cameraDao.update(camera);

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }
    
    /**
     * Get a camera.
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
        
        // Check if the camera exists
        CameraDao cameraDao = new CameraDao();
        Camera camera = cameraDao.getActiveById(id);
        if (camera == null) {
            throw new ClientException("CameraNotFound", "The camera doesn't exist");
        }
        
        // Build the output
        JsonObject json = Json.createObjectBuilder()
                .add("id", camera.getId())
                .add("name", camera.getName())
                .add("folder", camera.getFolder())
                .add("current", JsonUtil.nullable(camera.getCurrent()))
                .build();

        // Always return OK
        return Response.ok()
                .entity(json)
                .build();
    }
    
    /**
     * Get a camera picture.
     *
     * @param id ID
     * @return Response
     */
    @GET
    @Path("{id: [a-z0-9\\-]+}/picture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response picture(@PathParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        
        // Check if the camera exists
        CameraDao cameraDao = new CameraDao();
        Camera camera = cameraDao.getActiveById(id);
        if (camera == null) {
            throw new ClientException("CameraNotFound", "The camera doesn't exist");
        }
        
        File pictureFile = new File(camera.getFolder() + File.separator + camera.getCurrent());
        
        // Always return OK
        return Response.ok()
                .entity(pictureFile)
                .build();
    }

    /**
     * Deletes a camera.
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

        // Check if the camera exists
        CameraDao cameraDao = new CameraDao();
        Camera camera = cameraDao.getActiveById(id);
        if (camera == null) {
            throw new ClientException("CameraNotFound", "The camera doesn't exist");
        }

        // Delete the camera
        cameraDao.delete(camera.getId());

        // Always return OK
        return Response.ok()
                .entity(Json.createObjectBuilder().add("status", "ok").build())
                .build();
    }

    /**
     * Returns all active cameras.
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

        CameraDao cameraDao = new CameraDao();
        List<Camera> cameraList = cameraDao.findAll();

        JsonObjectBuilder response = Json.createObjectBuilder();
        JsonArrayBuilder items = Json.createArrayBuilder();
        for (Camera camera : cameraList) {
            items.add(Json.createObjectBuilder()
                    .add("id", camera.getId())
                    .add("name", camera.getName())
                    .add("folder", camera.getFolder())
                    .add("current", JsonUtil.nullable(camera.getCurrent())));
        }
        response.add("cameras", items);

        return Response.ok().entity(response.build()).build();
    }
}
