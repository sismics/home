package com.sismics.home.core.dao.dbi;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import com.sismics.home.core.dao.dbi.mapper.CameraMapper;
import com.sismics.home.core.model.dbi.Camera;
import com.sismics.util.context.ThreadLocalContext;

/**
 * Electricity meter DAO.
 * 
 * @author bgamard
 */
public class CameraDao {
    /**
     * Creates a new camera.
     * 
     * @param camera Camera to create
     * @return Camera ID
     */
    public String create(Camera camera) {
        // Init camera data
        camera.setId(UUID.randomUUID().toString());
        camera.setCreateDate(new Date());

        // Create camera
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                " T_CAMERA(CAM_ID_C, CAM_NAME_C, CAM_CREATEDATE_D, CAM_FOLDER_C, CAM_CURRENT_C)" +
                " values(:id, :name, :createDate, :folder, :current)")
                .bind("id", camera.getId())
                .bind("name", camera.getName())
                .bind("createDate", camera.getCreateDate())
                .bind("folder", camera.getFolder())
                .bind("current", camera.getCurrent())
                .execute();

        return camera.getId();
    }
    
    /**
     * Updates a camera.
     * 
     * @param camera Camera to update
     * @return Updated camera
     */
    public Camera update(Camera camera) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_CAMERA e set " +
                " e.CAM_NAME_C = :name, " +
                " e.CAM_FOLDER_C = :folder, " +
                " e.CAM_CURRENT_C = :current " +
                " where e.CAM_ID_C = :id and e.CAM_DELETEDATE_D is null")
                .bind("id", camera.getId())
                .bind("name", camera.getName())
                .bind("folder", camera.getFolder())
                .bind("current", camera.getCurrent())
                .execute();

        return camera;
    }
    
    /**
     * Gets a camera by its ID.
     * 
     * @param id Camera ID
     * @return Camera
     */
    public Camera getActiveById(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        Query<Camera> q = handle.createQuery("select " + new CameraMapper().getJoinedColumns("e") +
                "  from T_CAMERA e" +
                "  where e.CAM_ID_C = :id and e.CAM_DELETEDATE_D is null")
                .bind("id", id)
                .mapTo(Camera.class);
        return q.first();
    }
    
    /**
     * Deletes a camera.
     * 
     * @param id Camera's ID
     */
    public void delete(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update T_CAMERA e" +
                "  set e.CAM_DELETEDATE_D = :deleteDate" +
                "  where e.CAM_ID_C = :id and e.CAM_DELETEDATE_D is null")
                .bind("id", id)
                .bind("deleteDate", new Date())
                .execute();
    }

    /**
     * Returns the list of all cameras.
     *
     * @return List of cameras
     */
    public List<Camera> findAll() {
        Handle handle = ThreadLocalContext.get().getHandle();
        Query<Camera> q = handle.createQuery("select " + new CameraMapper().getJoinedColumns("e") +
                "  from T_CAMERA e " +
                "  where e.CAM_DELETEDATE_D is null" +
                "  order by e.CAM_NAME_C")
                .mapTo(Camera.class);
        return q.list();
    }
}
