/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the ResultController class that manages all the
 * interactions with result.html page
 *
 * upon most of the form submissions/button clicks on the home.html page the user is
 * redirected to the result.html page and presented with a message to indicate
 * whether the change was successfully recorded into the database or not
 *
 */
package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RestController
@RequestMapping()
public class ResultController {

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/result")
    public String viewResultPage(@RequestParam(value = "success", required = false) boolean success, @RequestParam(value = "exception", required = false) String exception, Model model){
        model.addAttribute("success", success);
//        model.addAttribute("tab", tab);
        model.addAttribute("exception", exception);
//        System.out.println(model.getAttribute("tab"));
        return "result";
    }

}
