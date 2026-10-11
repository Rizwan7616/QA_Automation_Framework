package com.rizwan.qa.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {
    protected WebDriver driver;

    @BeforeMethod
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
        Path page = Paths.get("src/test/resources/demo-site.html").toAbsolutePath();
        driver.get(page.toUri().toString());
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
