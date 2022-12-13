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
 * 09/29/2022   <CRN>   Changed integer map to String map
 * 10/10/2022   <SZ>    Updated createNewCustomer to first check if accountName is
 *                      an invalid String (null/blank)
 * 10/10/2022   <SZ>    Updated attemptLogin to first check if user is in userList
 * 10/14/2022   <CRN>   Fixed default constructors.
 * 10/16/2022   <CRN>   Cleaned up.
 * 10/18/2022   <CRN>   Added manual admin creation in initial userlist constructor; merged sign up and log in.
 *****************************************************/

package com.estore.api.estoreapi.User;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserList implements UserListInterface {

    // list of all users; uses the username to locate them in the map
    private TreeMap<String, User> userMap;
    private String filename;
    private ObjectMapper objectMapper;
    private static final String ADMIN_NAME_UPPER = "ADMIN";
    private static final String ADMIN_NAME_LOWER = "admin";

    @Autowired
    public UserList(@Value("${users.file}") String filenameIN, ObjectMapper objectMapperIN) {
        this.objectMapper = objectMapperIN;
        this.filename = filenameIN;

        userMap = new TreeMap<String, User>();
        Admin admin = new Admin(ADMIN_NAME_LOWER);
        userMap.put(ADMIN_NAME_UPPER, admin);
        // read the file, loop through it, add everything to the list
        try {
            Customer[] userArray = objectMapper.readValue(new File(filename),Customer[].class);
            for(int i = 0; i < userArray.length; i++) {
                if(!userArray[i].getAccountName().equalsIgnoreCase(ADMIN_NAME_LOWER)) {
                    userMap.put(userArray[i].getAccountName(), userArray[i]);
                }
            }
        } catch(Exception e) {
            System.err.println("Internal error reading userlist from file: " + e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createNewCustomer(String accountName, String password) {
        // check if name is null, empty string, or just white spaces
        if (accountName == null || accountName.isBlank() || accountName.equalsIgnoreCase(ADMIN_NAME_LOWER))
            return null;
        // check if name is already in user list
        if(getUser(accountName) != null)
            return null;

        // create user
        Customer user = new Customer(accountName, password);
        userMap.put(accountName, user);
        save();
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String accountName) {
        return userMap.get(accountName);
    }

    /**
     * {@inheritDoc}
     */
    public User attemptLogin(String accountName, String password) {
        if ((getUser(accountName)) != null){
            User user = userMap.get(accountName);
            if(user.isPassword(password)) return user;
        }
        return null;
    }

    /**
     * Saves a userlist to json file.
     * @return  true    : if saved
     *          false   : if not saved
     */
    private boolean save() {
        // convert map to array
        User[] list = new User[userMap.size()];
        int i = 0;
        for(User j : userMap.values()) {
            list[i++] = j;
        }
        // write to file
        try{
            objectMapper.writeValue(new File(filename), list);
        } catch(IOException e) {
            return false;
        }
        return true;
    }
}
