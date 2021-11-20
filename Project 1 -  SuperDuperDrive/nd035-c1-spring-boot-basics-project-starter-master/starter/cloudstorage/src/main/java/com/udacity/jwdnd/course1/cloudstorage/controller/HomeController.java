/*
*
* Author: Lina Walaa
* Date: August 2021
* Course: Java Web Developer Nanodegree Program by Udacity
*
* This is the HomeController class that manages all the
* interactions with home.html page
*
*/

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
@ControllerAdvice
@RequestMapping("/home")
public class HomeController {

    //autowired was not recommended here
    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private FileService fileService;

    @GetMapping()
    public String viewHomePage(Authentication authentication, Model model){

        //get all the notes, credentials, and files saved for this user
        // and add it to the model to be access by thymeleaf
        model.addAttribute("notes", this.noteService.getNotes(authentication.getName()));
        model.addAttribute("credentials", this.credentialService.getCredentials(authentication.getName()));
        model.addAttribute("files",this.fileService.getAllFiles(authentication.getName()));

        //success is true unless was changed below when a failure do any action
        //this value will later sent to the result page to show either
        // the success or error message
        model.addAttribute("success", true);

        //to solve the binding issue, either add the object to the model
        // or injected it to this controller method (get home page)
        //for solving binding issue
        model.addAttribute("noteForm", new NoteForm());
        model.addAttribute("credentialForm", new CredentialForm());

        return "home";
    }

    @PostMapping("/addOrEditNote")
    public String addOrEditNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, Model model, RedirectAttributes redirectAttributes){

        //by default the noteid is an empty string
        //if the value stored in the noteid is parsable to an int
        //then we're editing an existing note else we are adding a new note

        String msg;

        //editing or adding a new note requires sending the username and
        //the note form to the service function

        //Edit Note: if there is an ID
        try {
            Integer.parseInt(noteForm.getId());
            msg = noteService.editNote(authentication.getName(), noteForm);

            //Add Note
        } catch (Exception e) {
            msg = noteService.addNote(authentication.getName(), noteForm);
        }

        //if msg is not "Added"
        //then the returned msg will be the description of the
        // error that occurred

        //error is added to the exception attribute and success will be false
        if (!msg.equals("Added")){

            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", msg);

            //if msg = "Added" i.e. item added/edited successfully in db
            //update the notes attribute and success will be true
        }else{

            model.addAttribute("notes", this.noteService.getNotes(authentication.getName()));
            redirectAttributes.addAttribute("success", true);
        }

        return "redirect:/result";
    }

//    @PostMapping("/deleteNote")
//    public String deleteNote(Authentication authentication, @ModelAttribute("noteid") String noteid, Model model, RedirectAttributes redirectAttributes){
//
//            //we're getting the noteid value from the name attribute on the
//           // input tag we have name="noteid"
//            noteService.deleteNote(Integer.parseInt(noteid));
//            model.addAttribute("notes", this.noteService.getNotes(authentication.getName()));
//
//            redirectAttributes.addAttribute("success", true);
//
//            return "redirect:/result";
//
//    }

    @GetMapping("/deleteNote/{noteid}")
    public String deleteNote(Authentication authentication, @PathVariable("noteid") Integer noteid, Model model, RedirectAttributes redirectAttributes){

        //delete the given note that belongs to this username
        String msg = noteService.deleteNote(authentication.getName(), noteid);

        //if msg is not "Deleted"
        //then the returned msg will be the description of the
        // error that occurred

        //error is added to the exception attribute and success will be false
        if(!msg.equals("Deleted")){
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", msg);

            //if msg =="Deleted"i.e. item deleted successfully in db
            //update the notes attribute and success will be true
        }else{
            model.addAttribute("notes", this.noteService.getNotes(authentication.getName()));
            redirectAttributes.addAttribute("success", true);
        }

        return "redirect:/result";
    }

    @PostMapping("/addOrEditCredential")
    public String addOrEditCredential(Authentication authentication, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model, RedirectAttributes redirectAttributes){

        //by default the credentialid is an empty string
        //if the value stored in the credentialid is parsable to an int
        //then we're editing an existing note else we are adding a new note

        String msg;

        //editing or adding a new credential requires sending the username and
        //the credential form to the service function

        //edit: we will only have a value in the credentialid if it is an edit
        try{
            Integer.parseInt(credentialForm.getId());
            msg = credentialService.editCredential(authentication.getName(), credentialForm);

        //add
        }catch(Exception e){
            msg = credentialService.addCredential(authentication.getName(), credentialForm);
        }

        //if msg is not "Added"
        //then the returned msg will be the description of the
        // error that occurred

        //error is added to the exception attribute and success will be false
        if (!msg.equals("Added")){

            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", msg);

            //if msg = "Added" i.e. item added/edited successfully in db
            //update the credentials attribute and success will be true
        }else{

            model.addAttribute("credentials", this.credentialService.getCredentials(authentication.getName()));
            redirectAttributes.addAttribute("success", true);
        }

        return "redirect:/result";
    }

//    @PostMapping("/deleteCredential")
//    public String deleteCredential(Authentication authentication, @ModelAttribute("credentialid")String credentialid, Model model, RedirectAttributes redirectAttributes){
//
//        credentialService.deleteCredential(Integer.parseInt(credentialid));
//        model.addAttribute("credentials", this.credentialService.getCredentials(authentication.getName()));
//
//        redirectAttributes.addAttribute("success", true);
//
//        return "redirect:/result";
//    }

    @GetMapping("/deleteCredential/{credentialid}")
    public String deleteCredential(Authentication authentication, @PathVariable("credentialid")Integer credentialid, Model model, RedirectAttributes redirectAttributes){

        //delete the given credential that belongs to this username
        String msg = credentialService.deleteCredential(authentication.getName(), credentialid);

        //if msg is not "Deleted"
        //then the returned msg will be the description of the
        // error that occurred

        //error is added to the exception attribute and success will be false
        if(!msg.equals("Deleted")){
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", msg);

            //if msg =="Deleted"i.e. item deleted successfully in db
            //update the notes attribute and success will be true
        }else{
            model.addAttribute("credentials", this.credentialService.getCredentials(authentication.getName()));
            redirectAttributes.addAttribute("success", true);
        }

        return "redirect:/result";
    }

    //created this bean to be able to access the credentialService
    // inside thymeleaf and js
    @Bean(name = "credentialServices")
    public CredentialService credentialService(){
        return new CredentialService();
    }

    //this function takes the credentialid from the ajax function call and returns the
    //decrypted password value to be display it on the credential edit form
    @PostMapping("/decryptPassword")
    @ResponseBody
    public String decryptPassword(@Param("credentialid") String credentialid){
        return credentialService.decryptPassword(credentialid);
    }

//    @GetMapping("/decryptPassword/{credentialid}")
//    public String decryptPassword(Authentication authentication, @PathVariable("credentialid") Integer credentialid, Model model){
////        return credentialService.decryptPassword(credentialid.toString());
//        model.addAttribute("password", credentialService.decryptPassword(credentialid.toString()));
//        return "home";
//    }

    @PostMapping("/addFile")
    public String addFile(Authentication authentication, @RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes){

        //adding a new file requires sending the username and file
        //to the service function
        String msg = fileService.uploadFile(authentication.getName(), file);

        //if msg = "Uploaded" i.e. item added successfully in db
        //update the files attribute and success will be true
        if (msg.equals("Uploaded")){
            model.addAttribute("files",this.fileService.getAllFiles(authentication.getName()));
            redirectAttributes.addAttribute("success", true);

            //if msg is not "Uploaded"
            //then the returned msg will be the description of the
            // error that occurred

            //error is added to the exception attribute and success will be false
        }else{
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", msg);
        }

        return "redirect:/result";
    }

    @GetMapping("/deleteFile/{fileid}")
    public String deleteFile(Authentication authentication, @PathVariable("fileid")Integer fileId, Model model, RedirectAttributes redirectAttributes){

            String msg = fileService.deleteFile(authentication.getName(), fileId);

            if (msg.equals("Deleted")){
                model.addAttribute("files", this.fileService.getAllFiles(authentication.getName()));
                redirectAttributes.addAttribute("success", true);
            }else{
                redirectAttributes.addAttribute("success", false);
                redirectAttributes.addAttribute("exception", msg);
            }

        return "redirect:/result";
    }

    @GetMapping("/downloadFile/{fileid}")
    public Object downladFile(Authentication authentication, @PathVariable("fileid") Integer fileId, Model model, RedirectAttributes redirectAttributes){
        //return type was ResponseEntity<ByteArrayResource>
        File file = null;

        //downloading a file requires sending the username and fileid
        //to the service function
        Object returnedValue = fileService.downloadFile(authentication.getName(), fileId);

        try{

            //when returned object is not a file to download
            //error is added to the exception attribute and success will be false

            if (returnedValue instanceof String){
                redirectAttributes.addAttribute("success", false);
                redirectAttributes.addAttribute("exception", returnedValue);
                return "redirect:/result";
            }

            file = (File) returnedValue;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContenttype()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+ file.getFilename()+"\"")
                    .body(new ByteArrayResource(file.getFiledata()));

            //for technical download issues when the file exists
        }catch (Exception e){

            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("exception", e.toString());
            return "redirect:/result";
        }
    }
}
