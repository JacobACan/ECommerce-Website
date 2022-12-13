/*****************************************************
 * 
 *  File:       StoreController.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a store to handle server requests;
 *              has as little logic as possible, simply prints
 *              to the log when necessary and passes all functions
 *              through to the userlist and productlist.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 10/27/2022   <CRN>   File created, stubbed.
 * 10/30/2022   <CRN>   Simplified checkout, handled cart.
 * 11/4/2022    <SZ>    Added updateRating() method.
 *****************************************************/

package com.estore.api.estoreapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.Objects.Product;
import com.estore.api.estoreapi.Objects.ProductList;
import com.estore.api.estoreapi.Objects.ProductListInterface;
import com.estore.api.estoreapi.User.Customer;
import com.estore.api.estoreapi.User.User;
import com.estore.api.estoreapi.User.UserList;
import com.estore.api.estoreapi.User.UserListInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("")
public class StoreController {

    private static final Logger LOG = Logger.getLogger(StoreController.class.getName());
    private static ProductListInterface productList = new ProductList("data/products.json", new ObjectMapper());
    private static UserListInterface userList = new UserList("data/users.json", new ObjectMapper());
    private static final String ADMIN_NAME = "admin";
    
    /***
     * attempt to log in as a user; if a user doesn't exist, create a user. Return the user logged in
     * or created; if a user exists and the password is wrong, return an error code.
     * @param name      Account to log in to.
     * @param password  Password attempt
     * @return          The user, if the password was successful; NOT_ACCEPTABLE otherwise.
     */
    @GetMapping("/users/user/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name, @RequestParam String password) {
        LOG.info("Logging in customer: " + name);
        User user = userList.attemptLogin(name, password);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

        /***
     * Get the cart of a user with a given name.
     * @param name  The name of the account.
     * @return      OK          :   list of items in cart as a product array
     *              NOT_FOUND   :   no user is logged in
     */
    @GetMapping("/users/cart/{name}")
    public ResponseEntity<Product[]> getCart(@PathVariable String name){
        LOG.info("Getting customer cart: " + name);
        if(!name.equalsIgnoreCase(ADMIN_NAME)) {
            Customer user = (Customer) userList.getUser(name);
            if (user != null) {
                return new ResponseEntity<Product[]>(productList.getCartProducts(user.getCart()),HttpStatus.OK);
            }
        } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Gets the price of a users cart.
     * @param name
     * @return  OK          :   return: Price of all items in cart
     *          NOT_FOUND   :   reason: User or Users Cart does not exist
     */
    @GetMapping("/users/cart/price/{name}")
    public ResponseEntity<Double> getCartPrice(@PathVariable String name) {
        LOG.info("Getting customer cart price: " + name);
        if(!name.equalsIgnoreCase(ADMIN_NAME)) {
            Customer user = (Customer) userList.getUser(name);
            if (user != null) return new ResponseEntity<Double>(productList.getCartPrice(user.getCart()), HttpStatus.OK);
        } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Adds one of a product to a specified user cart.
     * @param name userName of cart holder.
     * @param product product from inventory being removed from cart.
     * @return number of product in cart after addition,  Not found if product, or user does not exist in database.
     */
    @PutMapping("/users/cart/add/{name}")
    public ResponseEntity<Integer> addToCart(@PathVariable String name, @RequestBody Product product) {
        LOG.info("Adding product #" + product.getId() + " to " + name + "'s cart.");
        if(!name.equalsIgnoreCase(ADMIN_NAME) && userList.getUser(name) != null) {
            Customer user = (Customer) userList.getUser(name);
            if(user.addToCart(product)) return new ResponseEntity<Integer>(user.getCart().get(product.getId()),HttpStatus.OK);
        } return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * Adds one of a product to a specified user cart.
     * @param name userName of cart holder.
     * @param product product from inventory being added to cart.
     * @return product from inventory being added to cart,  Not found if product, or user does not exist in database.
     */
    @PutMapping("/users/cart/remove/{name}")
    public ResponseEntity<Integer> removeFromCart(@PathVariable String name, @RequestBody Product product) {
        LOG.info("Removing product #" + product.getId() + " from " + name + "'s cart.");
        if(!name.equalsIgnoreCase(ADMIN_NAME)) {
            Customer user = (Customer) userList.getUser(name);
            if (user != null) {
                int productId = product.getId();
                user.removeFromCart(productId);
                if (user.getCart().containsKey(productId)) {
                    return new ResponseEntity<Integer>(user.getCart().get(productId), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Integer>(0, HttpStatus.OK);
                }
            }
        }  
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * Checks out a user's cart and updates the Inventory
     * @param name holder of cart
     * @return  OK          :   return: Price of Cart
     *          NOT_FOUND   :   reason: items in cart or inventory not found
     *          BAD_REQUEST :   reason: cart has more items then what is in stock
     * 
     */
    @PostMapping("/users/cart/{name}")
    public ResponseEntity<Double> checkout(@PathVariable String name) {
        LOG.info("Checking out cart: " + name);
        if(!name.equalsIgnoreCase(ADMIN_NAME)) {
            Customer user = (Customer) userList.getUser(name);
            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            TreeMap<Integer, Integer> userCart = user.getCart();
            if(!productList.checkoutUser(userCart)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            user.clearCart();
            return new ResponseEntity<Double>(productList.getCartPrice(userCart), HttpStatus.OK);
        } return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Checks out a user's cart and updates the Inventory
     * @param name holder of cart
     * @return  OK          :   return: True the items were removed
     *          BAD_REQUEST   :   reason: no null items in cart
     *          Not_Acceptable :   reason: Admin cannot use cart
     * 
     */
    @PostMapping("/users/cart/{name}/clean")
    public ResponseEntity<Boolean> removeNullCartItems(@PathVariable String name) {
        LOG.info("Removing Invalid items from cart: " + name);
        if(!name.equalsIgnoreCase(ADMIN_NAME)) {
            Customer user = (Customer) userList.getUser(name);
            if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            TreeMap<Integer, Integer> userCart = user.getCart();
            if(!productList.cleanInvalidCartProducts(userCart)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    
    
    /***
     * create a user with a given username and password
     * @param name      The user's name.
     * @param password  The user's password.
     * @return          The user, if it was successfully made.
     */
    @PutMapping("/users")
    public ResponseEntity<User> addUser(@RequestParam String name, @RequestParam String password) {
        LOG.info("Create customer: " + name);
        User user = userList.createNewCustomer(name, password);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
    }

    /**
     * Get product from this product list.
     * @param productIdNum
     * @return  OK                      :   return: product from productList
     *          NOT_FOUND               :   if product does not exist.
     *          INTERNAL_SERVER_ERROR   :   if the file does not exist when saving to it.
     */
    @GetMapping("/products/{productIdNum}")
    public ResponseEntity<Product> getProduct(@PathVariable int productIdNum) {
        LOG.info("GET /product/" + productIdNum);
        try {
            Product product = productList.getProduct(productIdNum);
            if(product != null) return new ResponseEntity<Product>(product,HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e) { 
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a list of all procucts in inventory.
     * @return Response Entity of all products in inventory.
     */
    @GetMapping("/products")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET /products");
        Product[] products = productList.getProducts();
        if(products.length > 0) {
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // TODO Documentation
    @GetMapping("/products/")
    public ResponseEntity<Product[]> getProductContains(@RequestParam String container) {
        LOG.info("GET /products?name=" + container);
        try {
            Product[] products = productList.getProductContains(container);
            if(products.length > 0) return new ResponseEntity<Product[]>(products,HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e) { 
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Product product} with the provided product object
     * 
     * @param product - The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product product} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Product product} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        LOG.info("POST /product" + product);
        try{
            Product newProduct = productList.addProduct(product);
            if (newProduct != null) return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
            else return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (IOException e){ 
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product product} with the provided {@linkplain Product product} object, if it exists
     * 
     * @param product The {@link Product product} to update
     * 
     * @return ResponseEntity with updated {@link Product product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        LOG.info("PUT /products " + product);
        try {
            Product updatedProduct = productList.updateProduct(product);
            if(updatedProduct != null) return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) { 
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the provided {@linkplain Product product} object
     * 
     * @param product the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id) throws IOException {
        LOG.info("DELETE /product/" + id);
        try {
            Product product = productList.getProduct(id);
            if(product != null && productList.deleteProduct(id)) return new ResponseEntity<>(product, HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setTest(){
        userList = new UserList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testusers.json", new ObjectMapper());
        productList = new ProductList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testproducts.json", new ObjectMapper());
    }
   
}
