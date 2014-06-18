package com.sismics.home.rest;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.filter.TokenBasedSecurityFilter;

/**
 * Exhaustive test of the electricity meter resource.
 * 
 * @author bgamard
 */
public class TestElecMeterResource extends BaseJerseyTest {
    /**
     * Test the electricity meter resource.
     *
     * @throws JSONException
     */
    @Test
    public void testElecMeterResource() {
        // Login admin
        String adminAuthenticationToken = clientUtil.login("admin", "admin", false);

        // List all electricity meters
        JsonObject json = target().path("/elec_meter").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        JsonArray elecMeters = json.getJsonArray("elec_meters");
        Assert.assertEquals(0, elecMeters.size());

        // Create an electricity meter
        json = target().path("/elec_meter").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .put(Entity.form(new Form()
                        .param("name", "main")), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // List all electricity meters
        json = target().path("/elec_meter").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        elecMeters = json.getJsonArray("elec_meters");
        Assert.assertEquals(1, elecMeters.size());
        JsonObject elecMeter0 = elecMeters.getJsonObject(0);
        String elecMeter0Id = elecMeter0.getString("id");
        Assert.assertEquals("main", elecMeter0.getString("name"));

        // Update an electricity meter
        json = target().path("/elec_meter/" + elecMeter0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .post(Entity.form(new Form()
                        .param("name", "heating")), JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // Check the update
        json = target().path("/elec_meter").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        elecMeters = json.getJsonArray("elec_meters");
        Assert.assertEquals(1, elecMeters.size());
        elecMeter0 = elecMeters.getJsonObject(0);
        Assert.assertEquals("heating", elecMeter0.getString("name"));

        // Delete the electricity meter
        json = target().path("/elec_meter/" + elecMeter0Id).request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .delete(JsonObject.class);
        Assert.assertEquals("ok", json.getString("status"));

        // Check the deletion
        json = target().path("/elec_meter").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        elecMeters = json.getJsonArray("elec_meters");
        Assert.assertEquals(0, elecMeters.size());
    }
}
