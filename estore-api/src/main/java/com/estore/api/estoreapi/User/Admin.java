/*****************************************************
 * 
 *  File:       Admin.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define an admin subclass of User.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/27/2022   <CRN>   File created, stubbed.
 *****************************************************/

package com.estore.api.estoreapi.User;

public class Admin extends User {
    /***
     * creates an admin account, name is ALWAYS ADMIN
     * @param password  The admin's password.
     */
    public Admin(String password) {
        super("ADMIN", password);
    }

    /***
     * {@inheritDoc}
     */
    @Override
    public boolean isAdmin() { return true; }
    
}