package com.eisgroup.aparovich.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractPage {
    protected WebDriver driver;
    protected WebDriverWait driverWait;

    public AbstractPage(WebDriver driver ) {
        this.driver = driver;
        driverWait = new WebDriverWait(driver, 20);
    }
}
