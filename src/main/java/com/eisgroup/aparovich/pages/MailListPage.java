package com.eisgroup.aparovich.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MailListPage extends AbstractPage {

    @FindBy(xpath = "//div[@gh='cm']")
    WebElement composeLetterButton;

    @FindBy(name = "to")
    WebElement recipientField;

    @FindBy(name = "subjectbox")
    WebElement subjectField;

    @FindBy(xpath = "//div[@role='textbox']")
    WebElement messageBody;

    @FindBy(xpath = "//*[@role='group']//td[1]//div[@role='button']")
    WebElement sendButton;

    @FindBy(xpath = "//div[@role='tabpanel']//tr")
    List<WebElement> inboxLettersList;

    @FindBy(xpath = "//div[@role='checkbox']")
    List<WebElement> incomingLettersCheckboxList;

    @FindBy(xpath = "//div[not(contains(@style, 'display: none'))]/*[@role='button'][3]/div")
    WebElement deleteLetterButton;

    @FindBy(xpath = "//span[@class='CJ']")
    WebElement moreButton;

    @FindBy(xpath = "//a[contains(@href, 'https://mail.google.com/mail/u/0/#trash')]")
    WebElement binButton;

    @FindBy(xpath = "//div[@role='main']//tr")
    List<WebElement> deletedLettersList;

    @FindBy(xpath = "//*[@role='main']//*[@role='button']")
    WebElement emptyBinButton;

    @FindBy(xpath = "//*[@role='alertdialog' and not(contains(@style, 'display: none'))]//*[@name='ok']")
    WebElement okButton;

    @FindBy(xpath = "//a[@href='https://mail.google.com/mail/u/0/#inbox']")
    WebElement inboxLink;

    public MailListPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public boolean isUserCorrect(String email) {
        List<WebElement> elementList = driver.findElements(By.xpath("//a[contains(@aria-label," + "'(" + email + ")')]"));
        if (elementList.size() == 1) {
            return true;
        }
        return false;
    }

    public void composeAndSendLetter(String recipientEmail, String subject, String textMessage)  {
        composeLetterButton.click();
        driverWait.until(ExpectedConditions.visibilityOf(recipientField)).sendKeys(recipientEmail);
        subjectField.sendKeys(subject);
        messageBody.sendKeys(textMessage);
        sendButton.click();
    }

    public void clickOnIncomingLetters() throws InterruptedException {
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("v1")));
        driverWait.until(ExpectedConditions.visibilityOf(inboxLink)).click();
        Thread.sleep(3000);
    }

    public void emptyBin() throws InterruptedException {
        moreButton.click();
        binButton.click();
        try {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driverWait.until(ExpectedConditions.visibilityOf(emptyBinButton)).click();
            okButton.click();
            driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[not(contains(@style, 'display: none'))]/*[@role='alertdialog']")));
        } catch (WebDriverException e) {
            return;
        }
    }

    public boolean isDeliveredLetter(String subject, String recipientEmail) {

        String email;
        String subjectText;

        try {
            email = inboxLettersList.get(0).findElement(By.xpath(".//div[2]/span/span[@email='" + recipientEmail + "']")).getAttribute("email");
            subjectText = inboxLettersList.get(0).findElement(By.xpath(".//span[@class='bog']/span[@class='bqe']")).getText();
        } catch (WebDriverException e) {
            return false;
        }

        if (subjectText.equals(subject) && email.equals(recipientEmail)) {
            return true;
        }
        return false;
    }

    public boolean clickOnIncomingLetterIfPresent() {
        if (incomingLettersCheckboxList.size() > 0) {
            incomingLettersCheckboxList.get(0).click();
            return true;
        }
        return false;
    }

    public void deleteIncomingLetter() {
        deleteLetterButton.click();
    }

    public boolean checkIfIncomingLetterDeleted(String subject, String recipientEmail) {
        moreButton.click();
        binButton.click();
        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("v1")));
        driverWait.until(ExpectedConditions.visibilityOfAllElements(deletedLettersList));

        String emailText;
        String subjectText;

        try {
            emailText = deletedLettersList.get(0).findElement(By.xpath(".//div[2]//span[@email='" + recipientEmail + "']")).getAttribute("email");
            subjectText = deletedLettersList.get(0).findElement(By.xpath(".//span[@class='bog']/span[@class='bqe']")).getText();
        } catch (WebDriverException e) {
            return false;
        }

        if (emailText.equals(recipientEmail) && subjectText.equals(subject)) {
            return true;
        }
        return false;
    }
}
