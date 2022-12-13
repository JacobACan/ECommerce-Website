/*****************************************************
 * 
 *  File:       Customer.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a customer subclass of User.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/27/2022   <CRN>   File created, stubbed.
 * 09/28/2022   <CRN>   Added shopping cart.
 * 09/29/2022   <CRN>   Added documentation.
 * 09/30/2022   <CRN>   Added getCart
 * 10/04/2022   <CRN>   Added add to cart function.
 * 10/18/2022   <CRN>   Generalized shopping cart to User.java.
 *****************************************************/

package com.estore.api.estoreapi.User;

import java.util.TreeMap;

import com.estore.api.estoreapi.Objects.Product;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer extends User {

    public Customer(String accountName, String password) {
        super(accountName, password);
        cart = new TreeMap<Integer, Integer>(); // < Product_Id(foreign key), Amount of Product>
    }

    @JsonCreator
    public Customer(@JsonProperty("name") String accountName, @JsonProperty("isAdmin") boolean admin,
                @JsonProperty("passwordHash") int passwordHash, @JsonProperty("cart") TreeMap<Integer, Integer> cart) {
        super(accountName, admin, passwordHash, cart);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public boolean isAdmin() { return false; }
    
    /***
     * Get the shopping cart of a user.
     * @return  The shopping cart of the user.
     */
    public TreeMap<Integer, Integer> getCart() { return cart; }

    /***
     * Adds a product to the cart.
     * @param product   The product to add to the cart.
     * @return          True if the product was added, false otherwise.
     */
    public boolean addToCart(Product product) {
        Integer compareVal = cart.get(product.getId());
        if(compareVal == null) compareVal = 0;
        if(product.getQuantity() > compareVal) {
            if(cart.containsKey(product.getId())) {
                cart.put(product.getId(), cart.get(product.getId()) + 1);
            } else {
                cart.put(product.getId(), 1);
            } return true;
        } return false;
    }
    
    /**
     * Removes an item from users cart and deletes it if quantity of item reaches 0.
     * @param productId id of product being removed from users cart
     * @return  true    : if item removed
     *          false   : if item not removed
     */
    public boolean removeFromCart(int productId) {
        Integer amountProductInCart = this.cart.get(productId);
        if(amountProductInCart == null) return false;
        if( amountProductInCart > 1) {
            cart.put(productId, cart.get(productId) - 1);
            return true;
        } else if ( amountProductInCart == 1){
            this.cart.remove(productId);
            return true;
        }
        return false;
    }

    /**
     * Deletes every item in a users cart
     */
    public void clearCart() {
        this.cart.clear();
    }
}