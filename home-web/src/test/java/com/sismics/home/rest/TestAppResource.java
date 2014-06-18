package com.sismics.home.rest;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.filter.TokenBasedSecurityFilter;

/**
 * Test the app resource.
 * 
 * @author jtremeaux
 */
public class TestAppResource extends BaseJerseyTest {
    /**
     * Test the API resource.
     * 
     * @throws JSONException
     */
    @Test
    public void testAppResource() {
        // Check the application info
        JsonObject json = target().path("/app").request().get(JsonObject.class);
        String currentVersion = json.getString("current_version");
        Assert.assertNotNull(currentVersion);
        String minVersion = json.getString("min_version");
        Assert.assertNotNull(minVersion);
        Long freeMemory = json.getJsonNumber("free_memory").longValue();
        Assert.assertTrue(freeMemory > 0);
        Long totalMemory = json.getJsonNumber("total_memory").longValue();
        Assert.assertTrue(totalMemory > 0 && totalMemory > freeMemory);
    }
    
    /**
     * Test the log resource.
     * 
     * @throws JSONException
     */
    @Test
    public void testLogResource() {
        // Login admin
        String adminAuthenticationToken = clientUtil.login("admin", "admin", false);
        
        // Check the logs (page 1)
        JsonObject json = target().path("/app/log").queryParam("level", "DEBUG").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        JsonArray logs = json.getJsonArray("logs");
        Assert.assertTrue(logs.size() == 10);
        Long date1 = logs.getJsonObject(0).getJsonNumber("date").longValue();
        Long date2 = logs.getJsonObject(9).getJsonNumber("date").longValue();
        Assert.assertTrue(date1 > date2);
        
        // Check the logs (page 2)
        json = target().path("/app/log").queryParam("offset",  "10").queryParam("level", "DEBUG").request()
                .cookie(TokenBasedSecurityFilter.COOKIE_NAME, adminAuthenticationToken)
                .get(JsonObject.class);
        logs = json.getJsonArray("logs");
        Assert.assertTrue(logs.size() == 10);
        Long date3 = logs.getJsonObject(0).getJsonNumber("date").longValue();
        Long date4 = logs.getJsonObject(9).getJsonNumber("date").longValue();
        Assert.assertTrue(date3 > date4);
    }
}
