package com.estore.api.estoreapi.Objects;
import java.io.IOException;
import java.util.TreeMap;


/*****************************************************
 * 
 *  File:       ProductListInterface.java
 *  Author:     Christopher Nokes
 *              Samson Zhang
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a general interface for a list of all products stored on the site.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/22/2022   <CRN>   File created, added basic functionality.
 * 09/22/2022   <SZ>    Updated addProduct() and its documentation
 * 09/23/2022   <CRN>   Changed ArrayList methods to TreeMap methods.
 * 09/23/2022   <DCA>   Added updateProduct() and its documentation
 * 09/24/2022   <ZMB>   Added deleteProduct() and its documentation
 * 09/28/2022   <CRN>   Added cart methods and basic documentation.
 * 10/04/2022   <CRN>   Added .equals().
 *****************************************************/

public interface ProductListInterface {

    /**
     * Adds a product to the list.
     * @param product   Product to add to the list.
     * @return          The product added.
     * @throws          IOException
     */
    public Product addProduct(Product product) throws IOException;

    /***
     * gets a product with a given id
     * @param productId A given ID.
     * @return          The product, null if no product exists.
     */
    public Product getProduct(int productId);

    /***
     * gets a product with a given name
     * @param productName   The given name of a product.
     * @return              The product if it exists, null otherwise.
     */
    public Product getProduct(String productName);

    /***
     * gets an array of products that contain a certain string
     * @param name  The string a product name might contain.
     * @return      An array of products with that name; might be empty.
     */
    public Product[] getProductContains(String name);

    /**
     * Get all products in the store.
     * @return returns an arraylist of prodcts.
     */
    public Product[] getProducts();

    // gets an array of products from a user's shopping cart.
    public Product[] getCartProducts(TreeMap<Integer, Integer> shoppingCart);

    /**
     * Updates and saves a {@linkplain Product product}
     * 
     * @param {@link Product product} object to be updated and saved
     * 
     * @return updated {@link Product product} if successful, null if
     * {@link Product product} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Product updateProduct(Product product) throws IOException;

    /**
     * Deletes a {@linkplain Product product} with the given product
     * 
     * @param {@link Product product} object to be deleted
     * 
     * @return true if the {@link Product product} was deleted
     * <br>
     * false if product with the searched id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(int id) throws IOException;
    
    /***
     * checks to see if a list contains a product
     * @param product   A product that may be contained by a list.
     * @return          True if the product is in the list, false otherwise.
     */
    boolean containsValue(Product product);

    /***
     * checks a productlist and an object for equality
     * @param obj   An object to compare with the given productlist
     * @return      True if the objects are equal, false otherwise.
     */
    boolean equals(Object obj);

    // given a user's shopping cart map, where the key is the ID and the value
    // is the quantity, get the value of the cart.
    double getCartPrice(TreeMap<Integer, Integer> shoppingCart);

    // TODO -- ADD METHODS PUT IN PRODUCTLIST.JAVA TO THIS INTERFACE

    public boolean checkoutUser(TreeMap<Integer, Integer> userCart);

    /**
     * Removes products that are no longer in stock from a users cart.
     * @param userCart cart being cleaned
     * @return true if cart sucessfully cleaned.
     */
    public boolean cleanInvalidCartProducts(TreeMap<Integer, Integer> userCart);
    
}
