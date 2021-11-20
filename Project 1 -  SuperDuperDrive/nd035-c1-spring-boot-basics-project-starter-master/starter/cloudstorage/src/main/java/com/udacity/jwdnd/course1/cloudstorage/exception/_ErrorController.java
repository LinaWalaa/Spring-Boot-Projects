/*
 *
 * Author: Lina Walaa
 * Date: November 2021
 * Course: Java Web Developer Nanodegree Program by Udacity
 *
 * This is the _ErrorController.java file that has the handles the most common http errors
 *
 * The class handles: 403, 404, and 500 HTTP status code error
 * and redirects the user to a custom made error page
 *
 */

package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class _ErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(_ErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError (HttpServletRequest request, Model model){

        String errorPage = "error";
        String pageTitle = "error";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status!=null){
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode==HttpStatus.NOT_FOUND.value()){
                pageTitle = "Page Not Found";
                errorPage = "error/404";
                LOGGER.error("Error 404");

            }else if (statusCode==HttpStatus.INTERNAL_SERVER_ERROR.value()){
                pageTitle = "Internal Server Error";
                errorPage = "error/500";
                LOGGER.error("Error 500");

            }else if (statusCode==HttpStatus.FORBIDDEN.value()){
                pageTitle = "Forbidden Error";
                errorPage = "error/403";
                LOGGER.error("Error 403");
            }

        }

        model.addAttribute("pageTitle", pageTitle);
        return errorPage;
    }


}
