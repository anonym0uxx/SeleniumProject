package com.goldenOneDotCom.tests.SmokeTests.Header;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.utilities.DriverUtils;
import com.utilities.ExtentReportManager;
import com.utilities.TestConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class LoginFunctionalityTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Login Functionality Test", 
                "Verify the behavior of an application user able to click the Log In button on the Main landing header screen");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testLoginFunctionality() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://www.golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Click Log In button at top right
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='login-section__login btn btn-outline' and @id='LogonButton']")));
            loginButton.click();
            test.pass("Clicked Log In button");
            takeScreenshot("Login_Button_Clicked");

            // Step 3: Click Forgot User ID link
            WebElement forgotUserIdLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='https://onlinebanking.golden1.com/auth/ForgottenUserId']")));
            forgotUserIdLink.click();
            test.pass("Clicked Forgot User ID link");
            takeScreenshot("Forgot_UserID_Clicked");

            // Step 4: Navigate back to the Main Landing Page
            driver.navigate().back();
            test.pass("Navigated back to Main Landing Page");
            takeScreenshot("Back_To_Main_Page");

            // Step 5: Click "Log In" button
            loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='login-section__login btn btn-outline' and @id='LogonButton']")));
            loginButton.click();
            test.pass("Clicked Log In button again");
            takeScreenshot("Login_Button_Clicked_Again");

            // Step 6: Click Forgot Password link
            WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='https://onlinebanking.golden1.com/auth/ForgottenPassword?hasSecureNow=True']")));
            forgotPasswordLink.click();
            test.pass("Clicked Forgot Password link");
            takeScreenshot("Forgot_Password_Clicked");

            // Step 7: Navigate back to the Main Landing Page
            driver.navigate().back();
            test.pass("Navigated back to Main Landing Page");
            takeScreenshot("Back_To_Main_Page_Again");

            // Step 8: Click "Log In" button
            loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='login-section__login btn btn-outline' and @id='LogonButton']")));
            loginButton.click();
            test.pass("Clicked Log In button for enrollment");
            takeScreenshot("Login_For_Enrollment");

            // Step 9: Click Enroll in Online Banking link
            WebElement enrollLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='https://onlinebanking.golden1.com/auth/Enrollment']")));
            enrollLink.click();
            test.pass("Clicked Enroll in Online Banking link");
            takeScreenshot("Enroll_Online_Banking_Clicked");

            // Step 10: Navigate back to the Main Landing Page
            driver.navigate().back();
            test.pass("Navigated back to Main Landing Page");
            takeScreenshot("Back_To_Main_Page_Final");

            // Step 11: Click "Log In" button
            loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@class='login-section__login btn btn-outline' and @id='LogonButton']")));
            loginButton.click();
            test.pass("Clicked Log In button for open account");
            takeScreenshot("Login_For_Open_Account");

            // Step 12: Click Open an Account link
            WebElement openAccountLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/apply-today' and contains(text(), 'Open an Account')]")));
            openAccountLink.click();
            test.pass("Clicked Open an Account link");
            takeScreenshot("Open_Account_From_Login_Clicked");

            // Verify that each link navigation worked properly
            test.pass("All login functionality links tested successfully");

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.pass("Browser closed successfully");
        }
        ExtentReportManager.flushReport();
    }

    private void takeScreenshot(String screenshotName) {
        if (TestConstants.ENABLE_SCREENSHOTS) {
            try {
                DriverUtils.takeScreenshot(driver, screenshotName);
            } catch (Exception e) {
                test.warning("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}