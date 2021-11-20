package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "success-msg")
    private WebElement successMsgField;

    @FindBy(id = "error-msg")
    private WebElement errorMsgField;

    @FindBy(tagName = "a")
    private WebElement homePageLink;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public boolean isSuccess(){
        return successMsgField.getText()==null?false:true;
    }

    public boolean isError(){
        return errorMsgField.getText()==null?false:true;
    }

    public void goToHomePage(){
        homePageLink.click();
    }
}
