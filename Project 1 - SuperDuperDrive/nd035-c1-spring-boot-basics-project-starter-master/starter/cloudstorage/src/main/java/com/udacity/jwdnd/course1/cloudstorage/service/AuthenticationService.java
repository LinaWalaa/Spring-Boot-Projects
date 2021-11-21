/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the AuthenticationService class that implements the AuthenticationProvider
 * class
 *
 * authorizes user logins by matching their credentials (username and password)
 * against those stored in the database
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//for classes that we implement we have to override all their methods
@Service
public class AuthenticationService implements AuthenticationProvider {

    //added the below fields and constructor to use them for
    //user lookup in the DB and hashing input passwords respectively
   @Autowired
    private UserMapper userMapper;
   @Autowired
    private HashService hashService;

//    public AuthenticationService(UserMapper userMapper, HashService hashService) {
//        this.userMapper = userMapper;
//        this.hashService = hashService;
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        //select user from DB
        User user = userMapper.selectUser(username);

        //if a user was found and not null
        if(user!=null){
            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password, encodedSalt);

            if (user.getPassword().equals(hashedPassword))
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        }

        return null;
    }

    @Override
    //here we state that the authentication is a user and password validation one
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
