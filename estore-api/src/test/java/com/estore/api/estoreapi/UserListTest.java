/*****************************************************
 * 
 *  File:       UserListTest.java
 *  Author:     Diogo Almeida
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Test UserList class.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 10/10/2022   <DCA>   created tests for all public methods in UserList.java
 *****************************************************/

package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.User.User;
import com.estore.api.estoreapi.User.UserList;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The unit test suite for the UserList class
 * 
 * @author Diogo Almeida
 */
@Tag("Model-Tier")
public class UserListTest {
    UserList userList = new UserList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testusers.json", new ObjectMapper());

    @Test
    public void testCreateNewCustomer() {
        userList = new UserList("hiufesgipg", null);
        userList = new UserList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testusers.json", new ObjectMapper());

        // Setup
        Random rand = new Random();
        User customer = null;
        String password = "testPassword";
        String accountName = null;

        // generates a new customer name until one is developed that doesn't already exist
        while(customer == null){
            accountName = ((Double) rand.nextDouble()).toString();
            customer = userList.createNewCustomer(accountName, password);
        }
        assertTrue(customer.isPassword(password));
        assertFalse(customer.isAdmin());
        assertEquals(accountName, customer.getAccountName());

        assertEquals(null, userList.createNewCustomer("admin", "password"));
        assertEquals(null, userList.createNewCustomer("", "password"));
        assertEquals(null, userList.createNewCustomer(null, "password"));
        assertEquals(null, userList.createNewCustomer(accountName, "password"));

        String newCustomerName = "test";
        while(userList.getUser(newCustomerName) != null) {
            newCustomerName = ((Double) rand.nextDouble()).toString();
        }

        assertEquals(null, userList.attemptLogin(newCustomerName, "password"));
        assertEquals(null, userList.attemptLogin("ADMIN", "NOTTHEPASSWORD"));
        assertEquals(customer, userList.attemptLogin(accountName, password));

    }
}