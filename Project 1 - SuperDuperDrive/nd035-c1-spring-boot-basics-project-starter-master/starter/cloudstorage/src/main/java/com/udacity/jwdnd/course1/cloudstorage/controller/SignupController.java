/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the SignupController class that manages all the
 * interactions with signup.html page
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserCreationService userCreationService;

    @GetMapping
    public String viewSignupPage(){
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes){
        String signupError = null;

        //check if condition is correct
        if(!userCreationService.isUsernameUsed(user.getUsername()))
            signupError = "That username is already used!";

        if(signupError==null) {
            int rowsAdded = userCreationService.createUser(user);
            if (rowsAdded < 0)
                signupError = "There was an error signing you up. Please try again later.";
        }

        //if the signupError is still null
        if(signupError==null){
            //no longer needed because moved to login page
//            model.addAttribute("signupSuccess", true);
            redirectAttributes.addAttribute("signupSuccess", true);
            return "redirect:/login";
        }else{
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }
}
