package com.sismics.home.rest;

import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.filter.TokenBasedSecurityFilter;

/**
 * Exhaustive test of the sensor resource.
 * 
 * @author bgamard
 */
public class TestSensorResource extends BaseJerseyTest {
    /**
     * Test the sensor resource.
     *
     * @throws JSONException
     */
    @Test
    public void testSensorResource() {
        // Login admin
        String adminAuthenticationToken = clientUtil.login("admin", "admin", false);

        // List all sensors
        JsonObject json = target().path("/sensor").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        JsonArray sensors = json.getJsonArray("sensors");
        Assert.assertEquals(2, sensors.size());

        // Create a sensor
        json = target().path("/sensor").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .put(Entity.form(new Form()
                        .param("name", "First sensor")
                        .param("type", "ELECTRICITY")), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // List all sensors
        json = target().path("/sensor").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        sensors = json.getJsonArray("sensors");
        Assert.assertEquals(3, sensors.size());
        JsonObject sensor0 = sensors.getJsonObject(0);
        String sensor0Id = sensor0.getString("id");
        Assert.assertEquals("First sensor", sensor0.getString("name"));
        Assert.assertEquals("ELECTRICITY", sensor0.getString("type"));

        // Update a sensor
        json = target().path("/sensor/" + sensor0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .post(Entity.form(new Form()
                        .param("name", "Temp meter")
                        .param("type", "TEMPERATURE")), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // Check the update
        json = target().path("/sensor/" + sensor0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        Assert.assertEquals("Temp meter", json.getString("name"));
        Assert.assertEquals("TEMPERATURE", json.getString("type"));

        // Add a sample to the sensor
        long date = new Date().getTime();
        json = target().path("/sensor/" + sensor0Id + "/sample").request()
                .put(Entity.form(new Form()
                        .param("date", "" + date)
                        .param("value", "254.5")), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));
        
        // Get the sensor
        json = target().path("/sensor/" + sensor0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        Assert.assertEquals("Temp meter", json.getString("name"));
        JsonArray samples = json.getJsonArray("samples");
        Assert.assertEquals(1, samples.size());
        Assert.assertEquals(date, samples.getJsonObject(0).getJsonNumber("date").longValue());
        Assert.assertEquals(254, samples.getJsonObject(0).getInt("value"));
        
        // Delete the sensor
        json = target().path("/sensor/" + sensor0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .delete(JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // Check the deletion
        json = target().path("/sensor").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        sensors = json.getJsonArray("sensors");
        Assert.assertEquals(2, sensors.size());
    }
}
