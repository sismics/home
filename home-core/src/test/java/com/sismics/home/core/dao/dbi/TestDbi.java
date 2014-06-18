package com.sismics.home.core.dao.dbi;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.home.BaseTransactionalTest;
import com.sismics.home.core.model.dbi.User;
import com.sismics.home.core.util.TransactionUtil;

/**
 * Tests the persistance layer.
 * 
 * @author jtremeaux
 */
public class TestDbi extends BaseTransactionalTest {
    @Test
    public void testJpa() throws Exception {
        // Create a user
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUsername("username");
        user.setEmail("toto@home.com");
        user.setRoleId("user");
        String id = userDao.create(user);
        
        TransactionUtil.commit();

        // Search a user by his ID
        user = userDao.getActiveById(id);
        Assert.assertNotNull(user);
        Assert.assertEquals("toto@home.com", user.getEmail());
    }
}
