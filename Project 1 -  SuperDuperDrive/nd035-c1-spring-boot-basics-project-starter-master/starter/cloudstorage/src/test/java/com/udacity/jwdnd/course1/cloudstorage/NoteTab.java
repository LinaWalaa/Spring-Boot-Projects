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

public class NoteTab{

    //tab
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    //Notes section
    @FindBy(id = "noteid")
    private WebElement noteId;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    //same button used for add and edit
    @FindBy(id = "saveNote")
    private WebElement noteSaveButton;

    @FindBy(id = "editNote")
    private WebElement editNoteButton;

//    @FindBy(id = "note-delete")
    @FindBy(id = "deleteNote")
    private WebElement deleteNoteButton;

    @FindBy(id = "addNote")
    private WebElement addNoteButton;

    @FindBy(id = "emptyNoteMsg" )
    private WebElement emptyNoteMsg;

    private WebDriverWait wait;

    public NoteTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addNote(WebDriver driver, String notetitle, String notedescription){

        wait = new WebDriverWait(driver,10);

        navNotesTab.click();

        //wait some time for when we are on the note tab
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.id("addNote"))).click();
        wait.until(ExpectedConditions.visibilityOf(addNoteButton));

        addNoteButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.sendKeys(notetitle);
        noteDescriptionField.sendKeys(notedescription);

        noteSaveButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));

    }

    public void editNote(WebDriver driver, String notetitle, String notedescription){

        wait = new WebDriverWait(driver,10);

        wait.until(ExpectedConditions.visibilityOf(navNotesTab));

        navNotesTab.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(editNoteButton));

        editNoteButton.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));

        //I will clear all what's currently in field and add the new value
        noteTitleField.clear();
        noteTitleField.sendKeys(notetitle);

        //I will clear all what's currently in field and add the new value
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(notedescription);

        noteSaveButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));
    }

    public String deleteNote(WebDriver driver){
        wait = new WebDriverWait(driver, 10);

        navNotesTab.click();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(deleteNoteButton));

        //run a javascript code to get the text value of the hidden element noteid
        // (returns the first found element with id="noteid")
        String id = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", noteId);
//        String id = (String) ((JavascriptExecutor)driver).executeScript("return document. getElementById('noteid'). value", noteId);


        //this will delete the first note on the list
        deleteNoteButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultSection")));

        //returns the note's id
        return id;
    }

    public boolean isNoteListEmpty(WebDriver driver){

        wait = new WebDriverWait(driver, 10);

        navNotesTab.click();

        //that should return true if there is a note available
        try {
            //this is only true when there are no notes

            //is displayed is not functioning correctly
//            if (!emptyNoteMsg.isDisplayed()){
//                return true;
//            }

            wait.until(ExpectedConditions.visibilityOf(emptyNoteMsg));
            return true;

            //if we couldn't locate the emptyNoteMsg then there are notes displayed
        }catch (Exception e){
            return false;
        }

//        return false;
    }

    public ArrayList<String> viewFirstNoteDetails(WebDriver driver){
        wait = new WebDriverWait(driver,10);

        navNotesTab.click();
        wait.until(ExpectedConditions.visibilityOf(addNoteButton));

        //get all the note table rows
        List<WebElement> elements = driver.findElements(By.id("noteData"));

        //get first note table row i.e. the last added note
        WebElement firstNote = elements.get(0);

        //get the note row's child note title and note description columns
        WebElement noteTitle = firstNote.findElement(By.id("notetitle"));
        WebElement noteDescription = firstNote.findElement(By.id("notedescription"));

        //return both values
        return new ArrayList<>(Arrays.asList(noteTitle.getText(), noteDescription.getText()));
    }

    public boolean isNoteDeleted(WebDriver driver, String id){
        wait = new WebDriverWait(driver,10);

        navNotesTab.click();
        //this is just to ensure that the note page is loaded
        wait.until(ExpectedConditions.visibilityOf(addNoteButton));

        //get all the note table rows
        List<WebElement> elements = driver.findElements(By.id("noteData"));

        //if there are no notes at all that still means that the note
        // we're looking for doesn't exist
        if (elements==null)
            return true;

        //loop through all notes
        for (WebElement element: elements) {
            //check for each note id if it is equal to the deleted note id
            //if found return false i.e. note not deleted
//            if (id.equals(element.findElement(By.id("noteid")).getText())){
//                System.out.println(element.findElement(By.id("noteid")).getText());
//                System.out.println( "noteid ("+ element.findElement(By.id("noteid")).getText() + ")");
//
//                return false;
//
//            }
//
            if (id.equals(((JavascriptExecutor)driver).executeScript("return arguments[0].innerText", noteId))){
                return false;
            }
        }
        return true;
    }
}
