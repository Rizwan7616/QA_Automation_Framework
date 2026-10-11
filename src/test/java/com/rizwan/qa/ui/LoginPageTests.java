package com.rizwan.qa.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTests extends BaseUiTest {

    @Test
    public void validCredentialsShowWelcomeMessage() {
        driver.findElement(By.id("username")).sendKeys("qa-demo");
        driver.findElement(By.id("password")).sendKeys("Passw0rd!");
        driver.findElement(By.id("loginButton")).click();

        String message = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(message, "Welcome, qa-demo!");
        Assert.assertEquals(
            driver.findElement(By.id("message")).getAttribute("data-status"),
            "success"
        );
    }

    @Test
    public void emptyCredentialsShowValidationMessage() {
        driver.findElement(By.id("loginButton")).click();

        WebElement message = driver.findElement(By.id("message"));
        Assert.assertEquals(
            message.getText(),
            "Please enter both username and password."
        );
        Assert.assertEquals(message.getAttribute("data-status"), "error");
    }

    @Test
    public void invalidCredentialsAreRejected() {
        driver.findElement(By.id("username")).sendKeys("wrong-user");
        driver.findElement(By.id("password")).sendKeys("wrong-password");
        driver.findElement(By.id("loginButton")).click();

        WebElement message = driver.findElement(By.id("message"));
        Assert.assertEquals(message.getText(), "Invalid username or password.");
        Assert.assertEquals(message.getAttribute("data-status"), "error");
    }
}
