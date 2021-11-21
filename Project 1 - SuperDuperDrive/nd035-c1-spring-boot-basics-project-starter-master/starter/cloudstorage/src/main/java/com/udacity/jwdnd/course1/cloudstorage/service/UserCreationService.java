/*
 *
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the UserCreationService class that
 * 1- creates users
 * 2- checks if a user with a given username already exists
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserCreationService {

    //auto wired is instead of adding the below fields to a constructor
    // the below fields are used to lookup user in the DB
    // and hashing input passwords respectively
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HashService hashService;

    public Boolean isUsernameUsed(String username){
        return userMapper.selectUser(username)==null;
    }

    public Object getUserIfExist(String username){
        
        //returns true if user doesn't exist
        if (isUsernameUsed(username))
            return "User " + username + " doesn't exist";

        //select user from database
        return userMapper.selectUser(username);
    }

    public int createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        return userMapper.insertUser(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstname(), user.getLastname()));
    }

    //creates my default testing user on application run
    // now I added the pre-created user in the schema file
    @PostConstruct
    public void iniDataForTesting(){
//        createUser(new User(-1, "root", "", "root0000", "Lina", "Walaa"));
    }
}
