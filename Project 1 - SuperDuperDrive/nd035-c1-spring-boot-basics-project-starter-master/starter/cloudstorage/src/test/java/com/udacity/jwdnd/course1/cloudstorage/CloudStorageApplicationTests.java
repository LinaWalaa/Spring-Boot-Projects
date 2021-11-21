package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private SignupPage signupPage;
	private LoginPage loginPage;
	private ResultPage resultPage;

	private HomePage homePage;
	private NoteTab noteTab;
	private CredentialTab credentialTab;
	private FileTab fileTab;

	private String credentialPassword = "";

	private static String imagesLocation;

	@BeforeAll
	public static void beforeAll() {
		//only once/before anything setup the chrome driver
		WebDriverManager.chromedriver().setup();

		imagesLocation = CloudStorageApplicationTests.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20"," ");
		imagesLocation = imagesLocation.substring(0, imagesLocation.indexOf("/target/test-classes/"))+"/src/main/resources/static/images/";
	}

	@BeforeEach
	public void beforeEach() {

		//before each test  reinitialize the driver
		this.driver = new ChromeDriver();

		//go to the signup page
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage = new SignupPage(driver);

		//create 2 users in the DB at the beginning
		signupPage.signup("sanem", "123","Sanem", "Aydin");
//		signupPage.signup("can", "29995","Can", "Divit");

		credentialPassword = "";
	}

	@AfterEach
	public void afterEach() {
		//after each test quit the driver
		if (this.driver != null) {
			driver.quit();
		}
	}

	//repetitive signup action
	public SignupPage goToSignUpPage(){
		//go to the signup page
		driver.get("http://localhost:" + this.port + "/signup");
		return new SignupPage(driver);
	}

	//repetitive login action
	public LoginPage goToLoginPage(){
		//go to the login page
		driver.get("http://localhost:" + this.port + "/login");
		return new LoginPage(driver);
	}

	//repetitive login the access home page action
	public Object goToHomePage(String tab){
		//go to the login page
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);

		//login with the user created on the before each
		loginPage.login("sanem", "123");

		//will return the page object based on the given string tab
		switch (tab){
			case "notes":
				return new NoteTab(driver);
			case "credentials":
				return new CredentialTab(driver);
			case "files":
				return new FileTab(driver);
			default:
				return new HomePage(driver);
		}
	}

	//adds a note, credential,a file if none exists
	public void initializeUser(String item) {

		switch (item){

			case "Note":
				//if there are no existing notes, add one
				if(noteTab.isNoteListEmpty(driver)){
					//login and go to the notes tab
					noteTab = (NoteTab) goToHomePage("notes");

					//add the below note details then save
					noteTab.addNote(driver,"test-", "<3 <3 <3");
				}
				break;

			case "Credential":
				//if there are no existing credentials, add one
				if (credentialTab.isCredentialListEmpty(driver)){
					//login and go to home page
					credentialTab = (CredentialTab) goToHomePage("credentials");

					//add the credential details and save
					credentialTab.addCredential(driver,"https://www.youtube.com", "banoota", "548");
					credentialPassword = "548";
				}
				break;

			case "File":

				//if there are no existing files, add one
				if (fileTab.isFileListEmpty(driver)){
					//login and go to home page
					fileTab = (FileTab) goToHomePage("files");

					//click on the file tab and upload a file size < 200KB
					fileTab.uploadFile(driver, imagesLocation + "Features_of_Spring_Boot.png");
				}
				break;

			default:
				break;
		}
	}

	//node for server side validation
//	@Test
	public void NoJS_javascript_disable() throws InterruptedException {

		//Disable JS
//		System.setProperty("webdriver.chrome.driver", "/Applications");

		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.managed_default_content_settings.javascript", 2);
		options.setExperimentalOption("prefs", prefs);

//		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//		desiredCapabilities.setJavascriptEnabled(false);
//		ChromeOptions options = new ChromeOptions();
//		options.merge(desiredCapabilities);

		WebDriver driver = new ChromeDriver(options);

		//try using google after JS is disabled
//		driver.get("https://www.google.com/imghp");
//		driver.findElement(By.name("q")).sendKeys("flowers");
//		driver.findElement(By.className("Tg7LZd")).click();
//		Thread.sleep(5000);
//
//		driver.quit();
	}

	//Test 1: part 2
	@Test
	public void validSignup(){

		//access the server
		signupPage = goToSignUpPage();

		//go to the signup page and an signup using the below
		signupPage.signup("emre", "456","Emre", "Divit");

		//assert that there are no errors
		assertThrows(Exception.class,()-> {
			signupPage.getErrorMsg();
		});

		//assert that the user is redirected to the login page
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void invalidSignup(){
		//access the server
		signupPage = goToSignUpPage();

		//go to the signup page and signup using an existing username
		signupPage.signup("emre", "29995","Can", "Divit");

		//assert that the below error is returned
		assertEquals("That username is already used!", signupPage.getErrorMsg() );
	}

	//Test 1: part 1
	@Test
	public void isVisitorAllowedAccess() {
		//assert that trying to access any link whether it exists or not
		// when not logged in and that is not
		// the /login or /signup page the user will be redirected to the
		//login page
		driver.get("http://localhost:" + this.port + "/home");

		//asserts that before logging in any path is redirecting to the login page
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");

		//asserts that before logging in any path is redirecting to the login page
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");

		//asserts that before logging in any path is redirecting to the login page
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void validLogin(){
		//access the server and signup
		loginPage = goToLoginPage();

		//go to the login page and login with valid credentials
		loginPage.login("sanem", "123");

		//assert that there is no error msg
		assertThrows(Exception.class, ()->{
			loginPage.getErrorMsg();
		});

		//asserts that after logging the user is redirected to the home page
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void invalidLogin(){
		//access the server and signup
		loginPage = goToLoginPage();

		//go to the login page and login with invalid credentials
		loginPage.login("sanem", "12");

		//assert that the below error message will be displayed
		assertEquals("Invalid username or password", loginPage.getErrorMsg());
	}

	//Test 1: part 2
	@Test
	public void logout(){
		//go to the login page
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);

		//put the correct user credentials
		loginPage.login("sanem", "123");

		//we're redirected to homepage automatically
		homePage = new HomePage(driver);

		Assertions.assertEquals("Home", driver.getTitle());

		//click on the logout button
		homePage.logout();

		//assert that the logout msg is displayed
		assertEquals("You have been logged out", loginPage.getLogoutMsg());

		//asserts that after logging out the user is redirected to the login page
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");

		//asserts that after logging out when the user tries to access the homepage
		// they are redirected to the login page
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void addNote(){
		//login and go to home page
		noteTab = (NoteTab) goToHomePage("notes");

		//add the below note details then save
		noteTab.addNote(driver,"noteA", "I love you");

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert note is created
		assertTrue(resultPage.isSuccess());

		noteTab = (NoteTab) goToHomePage("notes");

		//validate note Title
		assertEquals("noteA", noteTab.viewFirstNoteDetails(driver).get(0));
		//validate note description
		assertEquals("I love you", noteTab.viewFirstNoteDetails(driver).get(1));
	}

	@Test
	public void editNote(){
		//login and go to home page
		noteTab = (NoteTab) goToHomePage("notes");

		//soln 1
//		//adds a note if there is none
		initializeUser("Note");

//	//soln 2
////		add the below note details then save
//		noteTab.addNote(driver,"noteA", "I love you");
//		resultPage = new ResultPage(driver);
//
//		//assert note is created
//		assertTrue(resultPage.isSuccess());
//
////		go back to home page by clicking on the hyperlink
//		resultPage.goToHomePage();

		//go back to home page
		noteTab = (NoteTab) goToHomePage("notes");

		//change the values of the first note
		noteTab.editNote(driver, "noteA", "I love you222");

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert note is created
		assertTrue(resultPage.isSuccess());

		//go back to home page
		noteTab = (NoteTab) goToHomePage("notes");

		//validate note Title
		assertEquals("noteA", noteTab.viewFirstNoteDetails(driver).get(0));
		//validate note description
		assertEquals("I love you222", noteTab.viewFirstNoteDetails(driver).get(1));
	}

	@Test
	public void deleteNote(){
		//login and go to home page
		noteTab = (NoteTab) goToHomePage("notes");

		//soln 1
		//will add a note if none exists
		initializeUser("Note");

		//soln 2
//		//add the below note details then save
//		noteTab.addNote(driver,"noteA", "I love you");
//		resultPage = new ResultPage(driver);
//
//		//assert note is created
//		assertTrue(resultPage.isSuccess());
//		//go back to home page by clicking on the hyperlink
//		resultPage.goToHomePage();

		//delete the first note

		//delete the first note on the list and returns its id

		//go back to home page
		noteTab = (NoteTab) goToHomePage("notes");

		String noteid = noteTab.deleteNote(driver);

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert note is deleted
		assertTrue(resultPage.isSuccess());

		//go back to home page because was on result page
		noteTab = (NoteTab) goToHomePage("notes");

		//assert that the above noteid is deleted
		assertTrue(noteTab.isNoteDeleted(driver, noteid));
	}

	@Test
	public void addCredential(){
		//login and go to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//add the credential details and save
		credentialTab.addCredential(driver,"https://www.google.com", "girl", "test");
		credentialPassword = "test";

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert credential is created
		assertTrue(resultPage.isSuccess());

		//go back to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//validate credential Url
		assertEquals("https://www.google.com", credentialTab.viewFirstCredentialDetails(driver).get(0));
		//validate credential username
		assertEquals("girl", credentialTab.viewFirstCredentialDetails(driver).get(1));

		//validate credential password
		// and validate that the displayed value is not the decrypted one
//		assertEquals("test", credentialTab.decryptedPassword(driver, credentialTab.viewFirstCredentialDetails(driver).get(3)));
		assertNotEquals("test", credentialTab.viewFirstCredentialDetails(driver).get(2));
	}

	@Test
	public void editCredential(){
		//login and go to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//soln 1
		//adds a credential if none exists
		initializeUser("Credential");

		//soln 2
		//		//add the credential details and save
//		credentialTab.addCredential(driver,"https://www.best.com", "fun", "test");
//		resultPage = new ResultPage(driver);
//
//		//assert credential is created
//		assertTrue(resultPage.isSuccess());
//		//go back to home page by clicking on the hyperlink
//		resultPage.goToHomePage();

		//edit the credential created

		//go to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//add the new values and returns the value of the original password to validate that not encrypted
		assertEquals(credentialPassword, credentialTab.editCredential(driver, "https://www.facebook.com", "boy", "different"));

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert credential is edited successfully
		assertTrue(resultPage.isSuccess());

		//go back to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//validate credential Url
		assertEquals("https://www.facebook.com", credentialTab.viewFirstCredentialDetails(driver).get(0));
		//validate credential username
		assertEquals("boy", credentialTab.viewFirstCredentialDetails(driver).get(1));
		//validate credential password
		assertNotEquals("different", credentialTab.viewFirstCredentialDetails(driver).get(2));
	}

	@Test
	public void deleteCredential(){
		//login and go to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//soln 1
		//add a credential if there is none
		initializeUser("Credential");

		//soln 2
//		//click on the credential tab and add a new credenetial
//		credentialTab.addCredential(driver,"https://www.google.com", "girl", "test");
//
//		resultPage = new ResultPage(driver);
//
//		//assert credential is created
//		assertTrue(resultPage.isSuccess());
//
//		//click on the hyperlink that will take us to the home page
//		//we should be back on the same tab
//		resultPage.goToHomePage();

		//go to home page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//click on the delete in front of the first credential
		String credentialid =  credentialTab.deleteCredential(driver);

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert credential is deleted
		assertTrue(resultPage.isSuccess());

		//go back to home page because was on result page
		credentialTab = (CredentialTab) goToHomePage("credentials");

		//assert that the above noteid is deleted
		assertTrue(credentialTab.isCredentialDeleted(driver, credentialid));
	}

	@Test
	public void uploadSmallFile(){
		//login and go to home page
		fileTab = (FileTab) goToHomePage("files");

		//click on the file tab and upload a file size < 200KB
		fileTab.uploadFile(driver, imagesLocation + "Features_of_Spring_Boot.png");

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert file is uploaded successfully
		assertTrue(resultPage.isSuccess());

		//goes back to home page from result
		fileTab = (FileTab) goToHomePage("files");

		//assert new file is listed
		assertEquals("Features_of_Spring_Boot.png", fileTab.viewFirstFileName(driver));
	}

	@Test
	public void uploadBigFile(){
		//login and go to home page
		fileTab = (FileTab) goToHomePage("files");

		//click on the file tab and upload a file size > 200KB (not acceptable)
		fileTab.uploadFile(driver, imagesLocation +"Spring-Boot-flow-architecture.jpeg");

		//assert that the error msg is displayed because file is > 200KB
		assertEquals(false, fileTab.isErrorMsgHidden(driver));

		//assert that the upload button is disabled
		assertEquals("true", fileTab.isUploadButtonDisabled(driver));
	}

	@Test
	public void downloadFile(){
		//login and go to home page
		fileTab = (FileTab) goToHomePage("files");

		//soln 1
		//adds a file if there is none
		initializeUser("File");

		//soln 2
//		//click on the file tab and upload a file size < 200MB
//		fileTab.uploadFile(driver, "/Users/linawalaa/Downloads/nd-term-invoice.pdf");
//
//		//assert redirection to result page
//		assertEquals("Result", driver.getTitle());
//
//		resultPage = new ResultPage(driver);
//
//		//assert file is uploaded
//		assertTrue(resultPage.isSuccess());
//
//		//click on the home hyperlink,
//		// that should return to the last opened tab
//		resultPage.goToHomePage();

		//download the first file on the page and assert that when
		// downloading a new file
		//a new tab is opened by comparing the home url with
		// the new url after clicking the download button
//		System.out.println(fileTab.downloadFile(driver)); //http://localhost:"+port+"/home/downloadFile1
//		System.out.println("http://localhost:"+port+"/home");

		//goes back to home page from result
		fileTab = (FileTab) goToHomePage("files");

		ArrayList<String> fileDetails = fileTab.downloadFile(driver);

		//works for docs and images because they are opened in a new tab
		// unlike other types of files that will trigger a window to pop up to
		// choose download location in a new empty tab
		//asserts that a new download page will be opened and
		// that the url won't be that of home page
		assertEquals("http://localhost:"+port+"/home/downloadFile/"+fileDetails.get(1), fileDetails.get(0));

		//for testing when a file is not doc or image or opened in browser
//		assertTrue(fileDetails.get(0).equals("http://localhost:"+port+"/home/downloadFile/"+fileDetails.get(1)) || fileDetails.get(0).equals(""));
	}

	@Test
	public void deleteFile(){
		//login and go to home page
		fileTab = (FileTab) goToHomePage("files");

		//soln 1
		//adds a file if there is none
		initializeUser("File");

		//soln 2
//		//click on the file tab and upload a file
//		fileTab.uploadFile(driver, "/Users/linawalaa/Downloads/nd-term-invoice.pdf");
//
//		//assert redirection to result page based on the page title
//		assertEquals("Result", driver.getTitle());
//
//		resultPage = new ResultPage(driver);
//
//		//assert file is uploaded based on the success message displayed
//		assertTrue(resultPage.isSuccess());
//
//		//go to home page
//		resultPage.goToHomePage();

		//go to home page

		//go to home page
		fileTab = (FileTab) goToHomePage("files");

		//delete the first file on the table
		String fileid = fileTab.deleteFile(driver);

		//assert redirection to result page
		assertEquals("Result", driver.getTitle());

		resultPage = new ResultPage(driver);

		//assert file is deleted
		assertTrue(resultPage.isSuccess());

		//go back to home page because was on result page
		fileTab = (FileTab) goToHomePage("files");

		//assert that the above fileid is deleted
		assertTrue(fileTab.isFileDeleted(driver, fileid));
	}
}
