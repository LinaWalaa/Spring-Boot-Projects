/*
 *
 * Author: Lina Walaa
 * Date: August 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the ExceptionHelper class that handles all the exceptions thrown by functions
 * in the controller package
 *
 */

package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//files greater than max size were not processed by exception helper
// when controller advise was pointing to the home controller only

//@ControllerAdvice(basePackages = "com.udacity.jwdnd.course1.cloudstorage.controller")
//@ControllerAdvice(assignableTypes = HomeController.class)
@ControllerAdvice()
public class ExceptionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHelper.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler({NumberFormatException.class})
    public String handleNumberFormatException(Exception exception, RedirectAttributes redirectAttributes){

        LOGGER.error("Exception: ", exception.getMessage());

        redirectAttributes.addAttribute("success", false);
        redirectAttributes.addAttribute("exception", "Number Format Exception");

        return "redirect:/result";
    }

    //triggered when trying to upload a file that is greater than maxFileSize
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public String handleFileUploadError(Exception exception, RedirectAttributes redirectAttributes){

        LOGGER.error("Exception: ", exception.getMessage());

        redirectAttributes.addAttribute("success", false);
        redirectAttributes.addAttribute("exception", "Max Upload Size of " + maxFileSize + " was Exceeded");

        return "redirect:/result";
    }

    @ExceptionHandler({NullPointerException.class})
    public String handleMultipartError(Exception exception, RedirectAttributes redirectAttributes){

        LOGGER.error("Exception: ", exception.getMessage());

        redirectAttributes.addAttribute("success", false);
        redirectAttributes.addAttribute("exception", "Null Pointer Exception");

        return "redirect:/result";
    }


    @ExceptionHandler({Exception.class})
    public String handleException(Exception exception, RedirectAttributes redirectAttributes){

        LOGGER.error("Exception: ", exception.getMessage());

//        StackTraceElement stackTraceElement = new StackTraceElement(exception.getStackTrace());
        redirectAttributes.addAttribute("success", false);
        redirectAttributes.addAttribute("exception", exception.toString());

        return "redirect:/result";
//        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
