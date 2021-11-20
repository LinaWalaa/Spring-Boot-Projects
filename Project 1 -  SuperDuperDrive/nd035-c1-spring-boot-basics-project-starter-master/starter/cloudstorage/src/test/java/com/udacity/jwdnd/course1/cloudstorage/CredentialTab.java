package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CredentialTab {

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    //credential section
    @FindBy(id = "cred-id")
    private WebElement credentialid;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    //same button used for add and edit
    @FindBy(id = "saveCredential")
    private WebElement credentialSaveButton;

    @FindBy(id = "editCredential")
    private WebElement editCredentialButton;

//    @FindBy(id = "credential-delete")
    @FindBy(id = "deleteCredential")
    private WebElement deleteCredentialButton;

    @FindBy(id = "addCredential")
    private WebElement addCredentialButton;

    @FindBy(id = "emptyCredentialMsg" )
    private WebElement emptyCredentialMsg;

    private WebDriverWait wait;

    private CredentialService credentialService;

    public CredentialTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        credentialService = new CredentialService();
    }

    public void addCredential(WebDriver driver, String url, String username, String password){

        wait = new WebDriverWait(driver, 10);

        navCredentialsTab.click();
        wait.until(ExpectedConditions.visibilityOf(addCredentialButton));
        //wait some time for when we are on the credentials tab
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.id("addNote"))).click();

        addCredentialButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(credentialUrlField));

        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(password);

        credentialSaveButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));
    }

    public String editCredential(WebDriver driver, String url, String username, String password){

        wait = new WebDriverWait(driver, 10);

        navCredentialsTab.click();
//        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(editCredentialButton));

        editCredentialButton.click();
//        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(credentialUrlField));

        //I will clear all what's currently in each field and add the new value

        credentialUrlField.clear();
        credentialUrlField.sendKeys(url);

        credentialUsernameField.clear();
        credentialUsernameField.sendKeys(username);

        String originalPassword = credentialPasswordField.getText();
        credentialPasswordField.clear();
        credentialPasswordField.sendKeys(password);

        credentialSaveButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));
        return originalPassword;
    }

    public String deleteCredential(WebDriver driver){
        wait = new WebDriverWait(driver, 10);

        navCredentialsTab.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(deleteCredentialButton));

        //run a javascript code to get the text value of the hidden element credentialid
        // (returns the first found element with id="credentialid")
        String id = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", credentialid);

        deleteCredentialButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));
        return id;
    }

    public boolean isCredentialListEmpty(WebDriver driver){

        wait = new WebDriverWait(driver, 10);

        navCredentialsTab.click();

        //that should return true if there is a credential available
        try {
            //this is only true when there are no credentials

            //is displayed is not functioning correctly
//            if (emptyCredentialMsg.isDisplayed()){
//                return true;
//            }

            wait.until(ExpectedConditions.visibilityOf(emptyCredentialMsg));
            return true;

            //if we couldn't locate the emptyFileMsg then there are credentials displayed
        }catch (Exception e){
            return false;
        }
//        return true;
    }

    public ArrayList<String> viewFirstCredentialDetails(WebDriver driver) {

        wait = new WebDriverWait(driver,10);

        navCredentialsTab.click();
        wait.until(ExpectedConditions.visibilityOf(addCredentialButton));

        //get all the credential table rows
        List<WebElement> elements = driver.findElements(By.id("credentialData"));

        //get first credential table row i.e. the last added note
        WebElement firstCredential = elements.get(0);

        //get the credential row's child usl, username, password, and id columns
        ArrayList<String> credential = new ArrayList<>();
        credential.add(firstCredential.findElement(By.id("cred-url")).getText());
        credential.add(firstCredential.findElement(By.id("cred-username")).getText());
        credential.add(firstCredential.findElement(By.id("cred-password")).getText());
        credential.add((String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", credentialid));

        //get the credential row's child usl, username, password, and id columns
//        WebElement credentialUrl = firstCredential.findElement(By.id("cred-url"));
//        WebElement credentialUsername = firstCredential.findElement(By.id("cred-username"));
//        WebElement credentialPassword = firstCredential.findElement(By.id("cred-password"));

//        WebElement credentialID = firstCredential.findElement(By.id("cred-id"));
//        String credentialID = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", credentialid);;


        //return all 3 values
//        return new ArrayList<>(Arrays.asList(credentialUrl.getText(), credentialUsername.getText(), credentialPassword.getText(), credentialID.getText()));
        return credential;
    }

    public boolean isCredentialDeleted(WebDriver driver, String id) {
        wait = new WebDriverWait(driver,10);

        navCredentialsTab.click();
        //this is just to ensure that the credential page is loaded
        wait.until(ExpectedConditions.visibilityOf(addCredentialButton));

        //get all the credential table rows
        List<WebElement> elements = driver.findElements(By.id("credentialData"));

        //if there are no credentials at all that still means that the credential
        // we're looking for doesn't exist
        if (elements==null)
            return true;

        //loop through all notes
        for (WebElement element: elements) {
            //check for each credential id if it is equal to the deleted credential id

            //if found return false i.e. credential not deleted
            if (id.equals(((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", credentialid))){
                return false;
            }
        }
        return true;
    }

    public String decryptedPassword(WebDriver driver, String id){
        return credentialService.decryptPassword(id);
    }
}
