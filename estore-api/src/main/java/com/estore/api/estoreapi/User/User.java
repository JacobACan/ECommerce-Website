/*****************************************************
 * 
 *  File:       User.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define the abstract User superclass.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/27/2022   <CRN>   File created, stubbed.
 * 09/28/2022   <CRN>   File filled out.
 * 09/30/2022   <CRN>   Documentation added.
 * 10/04/2022   <CRN>   Added toString
 * 10/16/2022   <CRN>   Made passwordHash a json property, added save feature.
 * 10/18/2022   <CRN>   Generalized shopping cart as integer treemap.
 *****************************************************/

package com.estore.api.estoreapi.User;

import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    public static final String STRING_FORMAT = "User: [name=%s]";
    
    @JsonProperty("name")
    protected String accountName = ""; 
    @JsonProperty("isAdmin")
    protected boolean admin = this.isAdmin();
    @JsonProperty("passwordHash")
    protected int passwordHash;
    @JsonProperty("cart") 
    protected TreeMap<Integer, Integer> cart;

    @JsonCreator
    public User(@JsonProperty("name") String accountName, @JsonProperty("isAdmin") boolean admin,
                @JsonProperty("passwordHash") int passwordHash, @JsonProperty("cart") TreeMap<Integer, Integer> cart) {
        this.accountName = accountName;
        this.admin = admin;
        this.passwordHash = passwordHash;
        this.cart = cart;
    }

    /***
     * creates a user
     * @param accountName       Name to create an account of.
     * @param passwordString    Password to give the account.
     */
    @JsonIgnore
    public User(String accountName, String passwordString) {
        this.accountName = accountName;
        passwordHash = passwordString.hashCode();
    }

    /***
     * returns whether or not the password is correct
     * @param passwordInput Attempted password input.
     * @return              If the input password was correct.
     */
    public boolean isPassword(String passwordInput) {
        return passwordHash == passwordInput.hashCode();
    }

    /***
     * Gets the name of an account.
     * @return The account name.
     */
    public String getAccountName() { return accountName; }

    /***
     * Returns whether or not an individual is an admin
     * @return  True if admin, false otherwise.
     */
    public boolean isAdmin() { return false; };

    /***
     * toString, mostly for debugging
     * @return  A user as a string.
     */
    public String toString() { return String.format(STRING_FORMAT, accountName); }
}
