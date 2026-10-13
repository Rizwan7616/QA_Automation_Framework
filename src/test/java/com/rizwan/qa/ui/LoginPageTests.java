package com.rizwan.qa.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/** UI checks against the local demo page. These are demo-app tests, not production auth tests. */
public class LoginPageTests extends BaseUiTest {
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit = By.id("loginButton");
    private final By message = By.id("message");

    @Test
    public void pageHasExpectedTitle() {
        Assert.assertEquals(driver.getTitle(), "QA Demo Login");
    }

    @Test
    public void pageHasExpectedMainHeading() {
        Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), "Demo account sign-in");
    }

    @Test
    public void pageExplainsItIsASafeLocalTestTarget() {
        Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
            .contains("safe test target for the automation framework"));
    }

    @Test
    public void usernameFieldIsDisplayed() {
        Assert.assertTrue(driver.findElement(username).isDisplayed());
    }

    @Test
    public void passwordFieldIsDisplayed() {
        Assert.assertTrue(driver.findElement(password).isDisplayed());
    }

    @Test
    public void passwordFieldUsesPasswordInputType() {
        Assert.assertEquals(driver.findElement(password).getAttribute("type"), "password");
    }

    @Test
    public void usernameFieldHasUsernameAutocomplete() {
        Assert.assertEquals(driver.findElement(username).getAttribute("autocomplete"), "username");
    }

    @Test
    public void passwordFieldHasCurrentPasswordAutocomplete() {
        Assert.assertEquals(driver.findElement(password).getAttribute("autocomplete"), "current-password");
    }

    @Test
    public void usernameLabelIsAssociatedWithInput() {
        Assert.assertEquals(driver.findElement(By.cssSelector("label[for='username']")).getText(), "Username");
    }

    @Test
    public void passwordLabelIsAssociatedWithInput() {
        Assert.assertEquals(driver.findElement(By.cssSelector("label[for='password']")).getText(), "Password");
    }

    @Test
    public void signInButtonHasExpectedLabel() {
        Assert.assertEquals(driver.findElement(submit).getText(), "Sign in");
    }

    @Test
    public void messageRegionHasStatusRole() {
        Assert.assertEquals(driver.findElement(message).getAttribute("role"), "status");
    }

    @Test
    public void messageRegionUsesPoliteLiveUpdates() {
        Assert.assertEquals(driver.findElement(message).getAttribute("aria-live"), "polite");
    }

    @Test
    public void messageIsInitiallyEmpty() {
        Assert.assertEquals(driver.findElement(message).getText(), "");
    }

    @Test
    public void blankCredentialsShowValidationMessage() {
        driver.findElement(submit).click();
        assertMessage("Please enter both username and password.", "error");
    }

    @Test
    public void blankUsernameShowsValidationMessage() {
        driver.findElement(password).sendKeys("Passw0rd!");
        driver.findElement(submit).click();
        assertMessage("Please enter both username and password.", "error");
    }

    @Test
    public void blankPasswordShowsValidationMessage() {
        driver.findElement(username).sendKeys("qa-demo");
        driver.findElement(submit).click();
        assertMessage("Please enter both username and password.", "error");
    }

    @Test
    public void validCredentialsShowWelcomeMessage() {
        login("qa-demo", "Passw0rd!");
        assertMessage("Welcome, qa-demo!", "success");
    }

    @Test
    public void usernameWithSurroundingSpacesIsTrimmed() {
        login("  qa-demo  ", "Passw0rd!");
        assertMessage("Welcome, qa-demo!", "success");
    }

    @Test
    public void invalidUsernameIsRejected() {
        login("wrong-user", "Passw0rd!");
        assertMessage("Invalid username or password.", "error");
    }

    @Test
    public void invalidPasswordIsRejected() {
        login("qa-demo", "wrong-password");
        assertMessage("Invalid username or password.", "error");
    }

    @Test
    public void usernameComparisonIsCaseSensitive() {
        login("QA-DEMO", "Passw0rd!");
        assertMessage("Invalid username or password.", "error");
    }

    @Test
    public void passwordComparisonIsCaseSensitive() {
        login("qa-demo", "passw0rd!");
        assertMessage("Invalid username or password.", "error");
    }

    @Test
    public void passwordWithSurroundingSpacesIsRejected() {
        login("qa-demo", " Passw0rd! ");
        assertMessage("Invalid username or password.", "error");
    }

    @Test
    public void failedLoginCanBeFollowedBySuccessfulLogin() {
        login("wrong-user", "wrong-password");
        Assert.assertEquals(driver.findElement(message).getAttribute("data-status"), "error");
        driver.findElement(username).clear();
        driver.findElement(password).clear();
        login("qa-demo", "Passw0rd!");
        assertMessage("Welcome, qa-demo!", "success");
    }

    @Test
    public void successfulLoginCanBeFollowedByFailedLogin() {
        login("qa-demo", "Passw0rd!");
        Assert.assertEquals(driver.findElement(message).getAttribute("data-status"), "success");
        driver.findElement(username).clear();
        driver.findElement(password).clear();
        login("qa-demo", "incorrect");
        assertMessage("Invalid username or password.", "error");
    }

    private void login(String user, String pass) {
        driver.findElement(username).clear();
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pass);
        driver.findElement(submit).click();
    }

    private void assertMessage(String expectedText, String expectedStatus) {
        WebElement result = driver.findElement(message);
        Assert.assertEquals(result.getText(), expectedText);
        Assert.assertEquals(result.getAttribute("data-status"), expectedStatus);
    }
}
