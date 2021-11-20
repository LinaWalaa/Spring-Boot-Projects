package com.udacity.jwdnd.course1.cloudstorage;

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
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class FileTab {

    //tab
    @FindBy(id = "nav-files-tab")
    private WebElement navFilesTab;

    @FindBy(id = "validatedCustomFile")
    private WebElement browseFileButton;

    @FindBy(id = "uploadFile")
    private WebElement uploadFileButton;

    @FindBy(id="fileError")
    private WebElement errorMsg;

    @FindBy(id = "downloadFile")
    private WebElement downloadFileButton;

    @FindBy(id = "deleteFile")
    private WebElement deleteFileButton;

    @FindBy(id = "emptyFileMsg" )
    private WebElement emptyFileMsg;

    @FindBy(id = "fileid")
    private WebElement fileid;

    private WebDriverWait wait;

    public FileTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void uploadFile(WebDriver driver, String path){

        navFilesTab.click();
        //wait some time for when we are on the files tab
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        browseFileButton.sendKeys(path);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        uploadFileButton.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public ArrayList<String> downloadFile(WebDriver driver){

        wait = new WebDriverWait(driver,10);

        //click on the files tab
        navFilesTab.click();
        //wait some time for when we are on the files tab
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //we should only have one tab open
        //get the current window handle
//        String currentTab =  driver.getWindowHandle();
        String currentWindowHandle =  driver.getWindowHandle();

        //click on the download button and wait
        downloadFileButton.click();

        //this ensures that a second window is actually opened
        //if 2 is more than is opened an exception will occur so
        // we're always sure that there are 2 opened windows
        wait.until(numberOfWindowsToBe(2));

//        try {
//            wait.until(numberOfWindowsToBe(2));
//
//        }catch (Exception e){
//            System.out.println("not 2");
//        }

//        //get all the window handles
//        ArrayList<String> newTabs = new ArrayList<>(driver.getWindowHandles());
//
//        //remove the current tab from array list
//        newTabs.remove(currentTab);
////        String newTab = String.valueOf(driver.switchTo().window(newTabs.get(0)));
//
//        //switch to the first available tab
//        driver.switchTo().window(newTabs.get(0));

        //loop on available window handles aka open tabs
        for (String windowHandle : driver.getWindowHandles()) {

            //I expect to see to handles one that is the website and
            // the other one is the one triggered by the download button

            //switch to the other tab
            //if the current tab's content is not the same as the one being looped on
            if(!currentWindowHandle.contentEquals(windowHandle)) {

                //switch to this handle/tab
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        String url;
        String id;

        try{
            url = driver.getCurrentUrl();
            id = url.substring(url.lastIndexOf("/")+1, url.length());

        }catch (Exception e){
            url="";
            id="";
        }

        //return the new tab's url
//        return driver.getCurrentUrl();
        return new ArrayList<>(Arrays.asList(url, id));
    }

    public String deleteFile(WebDriver driver){

        navFilesTab.click();
        //wait some time for when we are on the files tab
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //run a javascript code to get the text value of the hidden element fileid
        // (returns the first found element with id="fileid")
        String id = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", fileid);

        deleteFileButton.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return id;
    }

    public boolean isErrorMsgHidden (WebDriver driver){

        //checks if the error msg div has the class "d-none"
        // i.e. div is hidden or not?
        return errorMsg.getAttribute("class").contains("d-none");
    }

    public String isUploadButtonDisabled(WebDriver driver){

        //checks if the upload file button is disabled or not
        //i.e. can you click on it or not?
        return uploadFileButton.getAttribute("disabled");
    }

    public boolean isFileListEmpty(WebDriver driver){

        wait = new WebDriverWait(driver, 10);

        navFilesTab.click();
//        wait.until(ExpectedConditions.visibilityOf(emptyFileMsg));

        //that should return true if there is a file available
        try {
            //this is only true when there are no files
            if (emptyFileMsg.isDisplayed()){
                return true;
            }

            //if we couldn't locate the emptyFileMsg then there are files displayed
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public String viewFirstFileName(WebDriver driver) {
        wait = new WebDriverWait(driver,10);

        navFilesTab.click();
        wait.until(ExpectedConditions.visibilityOf(uploadFileButton));

        //get all the file table rows
        List<WebElement> elements = driver.findElements(By.id("fileData"));

        //get first file table row i.e. the last added note
        WebElement firstFile = elements.get(0);

        //get the note row's child note title and note description columns
        WebElement filename = firstFile.findElement(By.id("filename"));

        //return file name
        return filename.getText();
    }

    public boolean isFileDeleted(WebDriver driver, String id) {
        wait = new WebDriverWait(driver,10);

        navFilesTab.click();
        //this is just to ensure that the file page is loaded
        wait.until(ExpectedConditions.visibilityOf(uploadFileButton));

        //get all the file table rows
        List<WebElement> elements = driver.findElements(By.id("fileData"));

        //if there are no files at all that still means that the file
        // we're looking for doesn't exist
        if (elements==null)
            return true;

        //loop through all notes
        for (WebElement element: elements) {
            //check each file id if it is equal to the deleted file id

            //if found return false i.e. file not deleted
            if (id.equals(((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", fileid))){
                return false;
            }
        }
        return true;
    }
}
