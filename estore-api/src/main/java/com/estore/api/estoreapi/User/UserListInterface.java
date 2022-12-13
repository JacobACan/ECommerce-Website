/*****************************************************
 * 
 *  File:       UserList.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a list of users as a TreeMap.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/27/2022   <CRN>   File created, stubbed.
 *****************************************************/

package com.estore.api.estoreapi.User;

public interface UserListInterface {

    /** creates a new customer
     * @param accountName   String name of the account
    *  @param password      Account password
    *  @return              new user created.
     */
    public User createNewCustomer(String accountName, String password);

    /**
     * return a user from the list by name
     * @param accountName   name of the account being looked for
     * @return              the user with the given name, or null if it was not found
     */
    public User getUser(String accountName);

    /**
     * attempt to log in to a user with a username and password
     * @param accountName   name of the account to try and log in to
     * @param password      attempted password
     * @return              the user if login was successful, null otherwise
     */
    public User attemptLogin(String accountName, String password);
}
