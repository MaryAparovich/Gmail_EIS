package com.eisgroup.aparovich.tests;

import com.eisgroup.aparovich.driver.DriverSingleton;
import com.eisgroup.aparovich.pages.LoginPage;
import com.eisgroup.aparovich.pages.MailListPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class GmailTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private MailListPage mailListPage;
    private static final String EMAIL = "test.automation45651@gmail.com";
    private static final String PASSWORD = "automation12345";
    private static final String RECIPIENT_EMAIL = "test.automation45651@gmail.com";
    private static final String SUBJECT = "Test";
    private static final String TEXT_MESSAGE = "Hello! How are you?";

    @BeforeMethod(groups = {"login"}, description = "Init browser")
    public void setUp() {
        driver = DriverSingleton.getDriver();
        loginPage = new LoginPage(driver);
        mailListPage = new MailListPage(driver);
        loginPage.openPage();
    }

    @Test(groups = {"login"})
    public void testLoginToEmail() {
        loginPage.login(EMAIL, PASSWORD);
        assertTrue(mailListPage.isUserCorrect(EMAIL), "You are not inside of test mail");
    }

    @Test(groups = {"sending"})
    public void testSuccessfulSendingLetter() throws InterruptedException {
        loginPage.login(EMAIL, PASSWORD);
        mailListPage.composeAndSendLetter(RECIPIENT_EMAIL, SUBJECT, TEXT_MESSAGE);
        mailListPage.clickOnIncomingLetters();
        assertTrue(mailListPage.isDeliveredLetter(SUBJECT, RECIPIENT_EMAIL), "Your letter wasn't delivered");
    }

    @Test(groups = {"removal"})
    public void testSuccessfulRemovalLetter() throws InterruptedException {
        loginPage.login(EMAIL, PASSWORD);
        mailListPage.emptyBin();
        mailListPage.clickOnIncomingLetters();
        assertTrue(mailListPage.clickOnIncomingLetterIfPresent(), "Incoming message is not found");
        mailListPage.deleteIncomingLetter();
        assertTrue(mailListPage.checkIfIncomingLetterDeleted(SUBJECT, RECIPIENT_EMAIL), "Your message is not deleted");
    }

    @AfterMethod(description = "Stop Browser")
    public void stopBrowser() {
        DriverSingleton.closeDriver();
    }
}
