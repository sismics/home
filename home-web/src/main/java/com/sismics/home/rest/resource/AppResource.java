package com.sismics.home.rest.resource;

import java.net.URI;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

import com.sismics.home.core.util.ConfigUtil;
import com.sismics.home.core.util.dbi.PaginatedList;
import com.sismics.home.core.util.dbi.PaginatedLists;
import com.sismics.home.rest.constant.BaseFunction;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import com.sismics.util.db.DbUtil;
import com.sismics.util.log4j.LogCriteria;
import com.sismics.util.log4j.LogEntry;
import com.sismics.util.log4j.MemoryAppender;

/**
 * General app REST resource.
 * 
 * @author jtremeaux
 */
@Path("/app")
public class AppResource extends BaseResource {
    /**
     * Return the information about the application.
     * 
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        ResourceBundle configBundle = ConfigUtil.getConfigBundle();
        String currentVersion = configBundle.getString("api.current_version");
        String minVersion = configBundle.getString("api.min_version");

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("current_version", currentVersion.replace("-SNAPSHOT", ""))
                .add("min_version", minVersion)
                .add("total_memory", Runtime.getRuntime().totalMemory())
                .add("free_memory", Runtime.getRuntime().freeMemory());
        return Response.ok().entity(response.build()).build();
    }
    
    /**
     * Retrieve the application logs.
     * 
     * @param level Filter on logging level
     * @param tag Filter on logger name / tag
     * @param message Filter on message
     * @param limit Page limit
     * @param offset Page offset
     * @return Response
     */
    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public Response log(
            @QueryParam("level") String level,
            @QueryParam("tag") String tag,
            @QueryParam("message") String message,
            @QueryParam("limit") Integer limit,
            @QueryParam("offset") Integer offset) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get the memory appender
        Logger logger = Logger.getRootLogger();
        Appender appender = logger.getAppender("MEMORY");
        if (appender == null || !(appender instanceof MemoryAppender)) {
            throw new ServerException("ServerError", "MEMORY appender not configured");
        }
        MemoryAppender memoryAppender = (MemoryAppender) appender;
        
        // Find the logs
        LogCriteria logCriteria = new LogCriteria();
        logCriteria.setLevel(StringUtils.stripToNull(level));
        logCriteria.setTag(StringUtils.stripToNull(tag));
        logCriteria.setMessage(StringUtils.stripToNull(message));
        
        PaginatedList<LogEntry> paginatedList = PaginatedLists.create(limit, offset);
        memoryAppender.find(logCriteria, paginatedList);
        JsonObjectBuilder response = Json.createObjectBuilder();
        JsonArrayBuilder logs = Json.createArrayBuilder();
        for (LogEntry logEntry : paginatedList.getResultList()) {
            logs.add(Json.createObjectBuilder()
                    .add("date", logEntry.getTimestamp())
                    .add("level", logEntry.getLevel())
                    .add("tag", logEntry.getTag())
                    .add("message", logEntry.getMessage()));
        }
        response.add("total", paginatedList.getResultCount());
        response.add("logs", logs);
        
        return Response.ok().entity(response.build()).build();
    }

    /**
     * Start the DB console.
     *
     * @return Response
     * @throws Exception
     */
    @GET
    @Path("db")
    public Response db() throws Exception {
        if (!DbUtil.isStarted()) {
            DbUtil.start();
        }

        if (DbUtil.isStarted()) {
            return Response.seeOther(new URI(DbUtil.getUrl())).build();
        } else {
            return Response.serverError().build();
        }
    }
}
