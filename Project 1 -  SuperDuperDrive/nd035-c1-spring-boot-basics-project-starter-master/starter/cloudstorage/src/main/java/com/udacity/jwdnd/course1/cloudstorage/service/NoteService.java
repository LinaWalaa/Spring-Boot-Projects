/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the NoteService class that
 * 1- maps noteForm object to a note object
 * 2- add/creates notes
 * 3- edits or deletes existing notes
 * 4- gets all note records stored for certain user
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCreationService userCreationService;

    public boolean isAnyFieldEmpty(NoteForm noteForm, String action){

        if (action.equals("edit") && noteForm.getId().equals(""))
            return false;

        if (noteForm.getTitle()==null || noteForm.getDescription()==null)
            return true;

        return false;
    }

    public String validateOwner(User user, Integer noteid){

        if (noteMapper.selectNote(noteid)==null)
            return "Note ID " + noteid + " doesn't exist";

        if (!noteMapper.selectNote(noteid).getUserid().equals(user.getUserid()))
            return "Note ID " + noteid + " doesn't belong to this user";

        return "Valid";
    }

    public String validateForm(User user, NoteForm noteForm, String action){

        if(isAnyFieldEmpty(noteForm,action))
            return "At least one of the fields of the note form is empty!";

        if (action.equals("add"))
            return "Valid";

        //if note id is not empty but it doesn't exist in the database
        if (noteForm.getId()!=null && noteMapper.selectNote(Integer.parseInt(noteForm.getId()))==null)
            return "Note ID " + noteForm.getId() + " doesn't exist";

        //check if current user is the owner of the selected note, returns valid if belongs
        //else returns error msg
        return validateOwner(user, Integer.parseInt(noteForm.getId()));
    }

    //map the note form to a note object
    public Object FormToObject(String username, NoteForm noteForm, String action){

        //if a text is returned rather than a user object that means that
        //there is no user with that username
        if(userCreationService.getUserIfExist(username) instanceof String)
            return userCreationService.getUserIfExist(username);
        User user = (User) userCreationService.getUserIfExist(username);

        String msg = validateForm(user, noteForm, action);

        if (!msg.equals("Valid"))
            return msg;

        Note note;

        //for edit the parsing will work, because there is a noteid already
        try{
            note = new Note(
                    Integer.parseInt(noteForm.getId()),
                    user.getUserid(),
                    noteForm.getTitle(),
                    noteForm.getDescription()
            );

        //for when adding a note we won't have the noteid, will be auto-created by DB
        }catch(Exception e){

            note = new Note(
                    user.getUserid(),
                    noteForm.getTitle(),
                    noteForm.getDescription()
            );
        }

        return note;
    }

    public String addNote(String username, NoteForm noteForm){

        //return the String error message if the returned is not a note object
        if (FormToObject(username, noteForm, "add") instanceof String)
            return (String) FormToObject(username, noteForm, "add");

        Note note = (Note) FormToObject(username, noteForm, "add");

        try {
            noteMapper.insertNote(note);
            return "Added";
        }catch (Exception e){
            return "Failed to add note. Please try again later!";
        }

    }

    public String editNote(String username, NoteForm noteForm){

        //return the String error message if the returned is not a note object
        if (FormToObject(username, noteForm, "edit") instanceof String)
            return (String) FormToObject(username, noteForm, "edit");

        Note note = (Note) FormToObject(username, noteForm, "edit");

        try {
            noteMapper.updateNote(note);
            return "Added";
        }catch (Exception e){
            return "Failed to edit note. Please try again later!";
        }
    }

    public String deleteNote(String username, Integer noteid){

        //if a text is returned rather than a user object that means that
        //there is no user with that username
        if(userCreationService.getUserIfExist(username) instanceof String)
            return (String) userCreationService.getUserIfExist(username);

        User user = (User) userCreationService.getUserIfExist(username);

        //if the user doesn't own this note, return error msg
        if (!validateOwner(user, noteid).equals("Valid"))
            return validateOwner(user, noteid);

        try {
            noteMapper.deleteNote(noteid);
            return "Deleted";
        }catch (Exception e){
            return "Failed to delete note. Please try again later!";
        }
    }

    public ArrayList<Note> getNotes(String username) {

        User user = userMapper.selectUser(username);
        return noteMapper.selectAllUserNotes(user);
    }
}
