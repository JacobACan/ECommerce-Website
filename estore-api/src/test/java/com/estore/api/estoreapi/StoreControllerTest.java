package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.estore.api.estoreapi.Objects.Product;
import com.estore.api.estoreapi.User.Customer;

@Tag("ViewModel-tier")
public class StoreControllerTest {
    @Test
    void testController() throws IOException{
        StoreController storeController = new StoreController();
        storeController.setTest();
        assertEquals(HttpStatus.NOT_FOUND, storeController.getProducts().getStatusCode());

        Product product = new Product("Test", 2, null, 10, 
                                      null, null, "Carbon", 1, 1, true);
        Product secondProduct = new Product("Product 2", 2, null, 10, 
                                      null, null, "Carbon", 1, 1, true);
                                      
        assertEquals(HttpStatus.CREATED, storeController.createProduct(product).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, storeController.createProduct(product).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.updateProduct(product).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.updateProduct(secondProduct).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.getProduct(product.getId()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.getProduct(-999).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.getProducts().getStatusCode());
        assertEquals(HttpStatus.OK, storeController.getProductContains("").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.getProductContains("DOES NOT EXIST").getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, storeController.getCart("ADMIN").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.getCart("A USER WITH THIS NAME WILL NOT EXIST").getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, storeController.getUser("A USER WITH THIS NAME DOES NOT EXIST", "bruh").getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, storeController.getUser("ADMIN", "not admin password").getStatusCode());
        assertEquals(HttpStatus.ACCEPTED, storeController.getUser("ADMIN", "admin").getStatusCode());

        assertEquals(HttpStatus.NOT_FOUND, storeController.getCartPrice("ADMIN").getStatusCode());
        assertEquals(HttpStatus.CONFLICT, storeController.addToCart("ADMIN", product).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, storeController.removeFromCart("ADMIN", product).getStatusCode());

        Random rand = new Random();
        Customer user = new Customer(((Double) rand.nextDouble()).toString(), "PASSWORD");
        while(storeController.getUser(user.getAccountName(), "PASSWORD").getStatusCode().equals(HttpStatus.ACCEPTED)){
            user = new Customer(((Double) rand.nextDouble()).toString(), "PASSWORD");
        }

        assertEquals(HttpStatus.NOT_FOUND, storeController.getCart(user.getAccountName()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.getCartPrice(user.getAccountName()).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, storeController.addToCart(user.getAccountName(), product).getStatusCode());

        assertEquals(HttpStatus.ACCEPTED, storeController.addUser(user.getAccountName(), "PASSWORD").getStatusCode());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, storeController.addUser(user.getAccountName(), "PASSWORD").getStatusCode());

        assertEquals(HttpStatus.OK, storeController.getCart(user.getAccountName()).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.getCartPrice(user.getAccountName()).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.addToCart(user.getAccountName(), product).getStatusCode());

        assertEquals(HttpStatus.OK, storeController.addToCart(user.getAccountName(), product).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.removeFromCart(user.getAccountName(), product).getStatusCode());
        assertEquals(HttpStatus.OK, storeController.removeFromCart(user.getAccountName(), product).getStatusCode());
        assertEquals(HttpStatus.CONFLICT, storeController.removeFromCart("USER DOES NOT EXIST", product).getStatusCode()); 
        assertEquals(HttpStatus.NOT_ACCEPTABLE, storeController.removeNullCartItems("ADMIN").getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, storeController.removeNullCartItems(user.getAccountName()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.removeNullCartItems("USER DOES NOT EXIST").getStatusCode()); 

        assertEquals(HttpStatus.NOT_ACCEPTABLE, storeController.checkout("ADMIN").getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.checkout("USER DOES NOT EXIST").getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, storeController.checkout(user.getAccountName()).getStatusCode());

        assertEquals(HttpStatus.OK, storeController.deleteProduct(product.getId()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.deleteProduct(product.getId()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, storeController.deleteProduct(-999).getStatusCode());
    } 
}