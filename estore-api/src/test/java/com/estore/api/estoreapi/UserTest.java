/*****************************************************
 * 
 *  File:       Product.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a Product object.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 10/04/2022   <CRN>   File created, created tests for starting code coverage.
 * 10/05/2022   <CRN>   Finished 100% code coverage.
 * 10/06/2022   <CRN>   Added documentation.
 * 10/18/2022   <CRN>   Account for user changes of integer maps.
 *****************************************************/

package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Objects.Product;
import com.estore.api.estoreapi.User.Customer;
import com.estore.api.estoreapi.User.User;

@Tag("Model-tier")
public class UserTest {
    
    @Test
    public void testCustomer() {
        Customer customer = new Customer("name", "password");
        Product product = new Product("Test", 2, null, 10, 
                                      null, null, "Carbon", 1, 1, true);
        assertEquals(true, customer.addToCart(product));
        assertEquals(true, customer.addToCart(product));
        product.setQuantity(0);
        assertEquals(false, customer.addToCart(product));
        assertEquals(true, customer.removeFromCart(product.getId()));
        assertEquals(true, customer.removeFromCart(product.getId()));
        assertEquals(false, customer.removeFromCart(product.getId()));
        customer.clearCart();
        assertEquals(new TreeMap<Integer, Integer>(), customer.getCart());
    }

    @Test
    public void testUser(){
        User user = new User("name", "password");
        assertEquals(false, user.isAdmin());
        assertEquals(user.toString(), user.toString());
    }
}
