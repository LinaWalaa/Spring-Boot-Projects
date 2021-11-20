package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
public class FileService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    public Object getUserIfExist(String username){

        //select user from database
        if (userMapper.selectUser(username)==null)
            return "User " + username + " doesn't exist";

        return userMapper.selectUser(username);
    }

    public Object checkIfFileIfExist(Integer fileId){

        //select file from database
        if (fileMapper.selectFileById(fileId)==null)
            return "File ID " + fileId + " doesn't exist";

        return true;
    }

    public boolean isValidFileName(User user, String filename) {
        //returns true if this user has no file with the same provided name
        return fileMapper.selectFileByName(user.getUserid(), filename)==null;
    }

    public String uploadFile(String username, MultipartFile file) {

        //will never be triggered because the Request method 'GET' not supported for "/home/addFile"
        //does user exist in database
        //is true when user is null
        if (getUserIfExist(username) instanceof String)
            return (String) getUserIfExist(username);

        User user = (User) getUserIfExist(username);

        //if no file is uploaded
        if(file.getOriginalFilename()=="")
            return "No file uploaded";

        if (!isValidFileName(user, file.getOriginalFilename()))
            return "File name already used before! Please use another name.";

        File fileObject;

        try {
            fileObject = new File(
                    user.getUserid(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getBytes()
            );

            fileMapper.insertFile(fileObject);

        } catch (Exception e) {
            return e.toString();
        }
        return "Uploaded";
    }

    public Object downloadFile(String username, Integer fileId){

        //does user exist in database
        //is true when user is null
        if (getUserIfExist(username) instanceof String)
            return (String) getUserIfExist(username);
        User user = (User) getUserIfExist(username);

        //does file exist in database
        //is true when file doesn't exist
        if (!checkIfFileIfExist(fileId).equals(true))
            return checkIfFileIfExist(fileId);

        //does file belong to current user
        if (fileMapper.selectFileById(fileId).getUserid().equals(user.getUserid())){
            return fileMapper.selectFileById(fileId);
        }else{
            return "File ID " + fileId + " doesn't belong to this user";
        }

    }

    public String deleteFile(String username, Integer fileId){

        //does user exist in database
        //is true when user is null
        if (getUserIfExist(username) instanceof String)
            return (String) getUserIfExist(username);
        User user = (User) getUserIfExist(username);

        //is true when file doesn't exist in database
        if (!checkIfFileIfExist(fileId).equals(true))
            return (String) checkIfFileIfExist(fileId);

        //does file belong to current user
        if (fileMapper.selectFileById(fileId).getUserid().equals(user.getUserid())){
            fileMapper.deleteFile(fileId);
            return "Deleted";
        }else{
            return "File ID " + fileId + " doesn't belong to this user";
        }

    }

    public ArrayList<File> getAllFiles(String username){

        //returns null if not found so no need to validate,
        // otherwise I would have to change handle controller to handle the error messages as well
        User user = userMapper.selectUser(username);
        return fileMapper.selectAllUserFiles(user);
    }

}
