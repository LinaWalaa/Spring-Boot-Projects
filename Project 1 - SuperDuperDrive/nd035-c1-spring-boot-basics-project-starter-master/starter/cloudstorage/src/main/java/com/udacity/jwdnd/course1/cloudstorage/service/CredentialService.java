/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the CredentialService class that
 * 1- maps credentialForm object to a credential object
 * 2- add/creates credentials
 * 3- edits or deletes existing credentials
 * 4- gets all credential records stored for certain user
 *
 * 5- decrypts the password of a given credential record and
 * returns the value of the decrypted password
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class CredentialService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserCreationService userCreationService;

    public boolean isAnyFieldEmpty(CredentialForm credentialForm, String action){

        if (action.equals("edit") && credentialForm.getId().equals(""))
            return false;

        if (credentialForm.getPassword()==null || credentialForm.getUrl()==null || credentialForm.getUsername()==null)
            return true;

        return false;
    }

    public String validateOwner(User user, int credentialid) {
        if (credentialMapper.selectCredential(credentialid)==null)
            return "Credential ID " + credentialid + " doesn't exist";

        if (!credentialMapper.selectCredential(credentialid).getUserid().equals(user.getUserid()))
            return "Note ID " + credentialid + " doesn't belong to this user";

        return "Valid";
    }

    public String validateForm(User user, CredentialForm credentialForm, String action){

        if(isAnyFieldEmpty(credentialForm, action))
            return "At least one of the fields of the note form is empty!";

        if (action.equals("add"))
            return "Valid";

        //if credential id is not empty but it doesn't exist in the database
        if (credentialForm.getId()!=null && credentialMapper.selectCredential(Integer.parseInt(credentialForm.getId()))==null)
            return "Credential ID " + credentialForm.getId() + " doesn't exist";

        //check if current user is the owner of the selected credential,
        // returns valid if belongs
        //else returns error msg
        return validateOwner(user, Integer.parseInt(credentialForm.getId()));
    }

    //function maps the values of a given credentialForm to a credential object
    public Object formToObject(String username, CredentialForm credentialForm, String action){

        //if a text is returned rather than a user object that means that
        //there is no user with that username
        if(userCreationService.getUserIfExist(username) instanceof String)
            return userCreationService.getUserIfExist(username);
        User user = (User) userCreationService.getUserIfExist(username);

        String msg = validateForm(user, credentialForm, action);

        if (!msg.equals("Valid"))
            return msg;

        Credential credential;

        //try for edit
        try {
            //select existing credential because we won't change the key
            credential = credentialMapper.selectCredential(Integer.parseInt(credentialForm.getId()));

            credential.setUrl(credentialForm.getUrl());
            credential.setUsername(credentialForm.getUsername());
            credential.setPassword(encryptionService.encryptValue(credentialForm.getPassword(), credential.getKey()));

//            newCredential = new Credential(
//                    user.getUserid(),
//                    Integer.parseInt(credentialForm.getId()),
//                    credentialForm.getUrl(),
//                    credentialForm.getUsername(),
//                    encryptionService.encryptValue(credentialForm.getPassword(), currentCredenetial.getKey()),
//                    currentCredenetial.getKey()
//            );

        //catch for add because we have no credentialid yet
        } catch (Exception e){
            
            //creating the encoded key and encrypting the password
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);

            String encodedKey = Base64.getEncoder().encodeToString(key);

            String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);

            credential = new Credential(
                    user.getUserid(),
                    credentialForm.getUrl(),
                    credentialForm.getUsername(),
                    encodedKey,
                    encryptedPassword
            );
        }
        return credential;
    }

    public String addCredential(String username, CredentialForm credentialForm){

        //return the String error message if the returned is not a credential object
        if (formToObject(username, credentialForm, "add") instanceof String)
            return (String) formToObject(username, credentialForm, "add");

        Credential credential = (Credential) formToObject(username, credentialForm, "add");

        try {
            credentialMapper.insertCredential(credential);
            return "Added";
        }catch (Exception e){
            return "Failed to add Credential. Please try again later!";
        }
    }

    public String editCredential(String username, CredentialForm credentialForm){

        //return the String error message if the returned is not a credential object
        if (formToObject(username, credentialForm, "edit") instanceof String)
            return (String) formToObject(username, credentialForm, "edit");

        Credential credential = (Credential) formToObject(username, credentialForm, "edit");

        try {
            credentialMapper.updateCredential(credential);
            return "Added";
        }catch (Exception e){
            return "Failed to edit note. Please try again later!";
        }
    }

    public String deleteCredential(String username, Integer credentialid){

        //if a text is returned rather than a user object that means that
        //there is no user with that username
        if(userCreationService.getUserIfExist(username) instanceof String)
            return (String) userCreationService.getUserIfExist(username);

        User user = (User) userCreationService.getUserIfExist(username);

        //if the user doesn't own this credential, return error msg
        if (!validateOwner(user, credentialid).equals("Valid"))
            return validateOwner(user, credentialid);

        try {
            credentialMapper.deleteCredential(credentialid);
            return "Deleted";
        }catch (Exception e){
            return "Failed to delete note. Please try again later!";
        }

    }

    //get a certain credential
    public Credential getCredential(String credentialid){
        return credentialMapper.selectCredential(Integer.parseInt(credentialid));
    }

    //decrypts the password of a given credential record and returns
    // the value of the decrypted password
    public String decryptPassword(String credentialid){

        try{
            Credential credential = credentialMapper.selectCredential(Integer.parseInt(credentialid));

            //get the decrypted password value based on the stored
            //encrypted password and encoded salt
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            return decryptedPassword;

        }catch(Exception e){
            return "failed";
        }

    }

    //  gets all credential records stored for certain user
    public ArrayList<Credential> getCredentials(String username){

        User user = userMapper.selectUser(username);
        return credentialMapper.selectAllUserCredentials(user);
    }

}
