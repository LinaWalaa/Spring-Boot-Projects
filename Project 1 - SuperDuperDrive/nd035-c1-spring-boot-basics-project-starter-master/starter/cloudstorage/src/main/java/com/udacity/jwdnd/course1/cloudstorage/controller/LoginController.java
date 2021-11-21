/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the LoginController class that manages all the
 * interactions with login.html page
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String viewLoginPage(@RequestParam(value = "signupSuccess", required = false) boolean signupSuccess, Model model){
        model.addAttribute("signupSuccess", signupSuccess);
        return "login";

    }
}
