package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "inputFirstName")
    private WebElement firstnameField;

    @FindBy(id = "inputLastName")
    private WebElement lastnameField;

    @FindBy(id = "signupButton")
    private WebElement signupButton;

    @FindBy(id = "error-msg")
    private WebElement errorMsgField;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

//    String username, String password, String firstname, String lastname
    public void signup(String username, String password, String firstname, String lastname){
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        firstnameField.sendKeys(firstname);
        lastnameField.sendKeys(lastname);
        signupButton.click();
    }

    public String getErrorMsg(){
        return errorMsgField.getText();
    }
}
