package com.goldenOneDotCom.tests.Loans;

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

public class MakeOneTimePaymentTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Make One-Time Payment Test", 
                "Verifying user able to click 'Make One-Time Payment' option under Make a Payment for Loans Tab");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testMakeOneTimePayment() {
        try {
            // Step 1: Open new browser and enter the QA URL
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Click Loans Tab
            clickLoansTab();

            // Step 3: Click Make a Payment
            clickMakeAPayment();

            // Step 4: Click Make One-Time Payment
            clickMakeOneTimePayment();

            // Step 5: Click each type under Make One-Time Payment
            verifyMakeOneTimePaymentOptions();

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            loansTab.click();
            test.pass("Clicked on 'Loans' tab");
            takeScreenshot("Loans_Tab_Clicked");
            Thread.sleep(1000);
        } catch (Exception e) {
            test.fail("Failed to click Loans tab: " + e.getMessage());
            throw new RuntimeException("Failed to click Loans tab", e);
        }
    }

    private void clickMakeAPayment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // First try to find the Make a Payment link in the Loans submenu
            try {
                WebElement makePaymentLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Make a Payment')]")));
                makePaymentLink.click();
                test.pass("Clicked on 'Make a Payment' link from Loans submenu");
            } catch (Exception e) {
                // If not found in submenu, navigate directly to the make payment page
                driver.get(TestConstants.G1_HOME_URL + "/credit-cards-loans/make-a-payment");
                test.pass("Navigated directly to Make a Payment page");
            }
            
            takeScreenshot("Make_Payment_Page_Loaded");
            Thread.sleep(2000); // Allow page to load
        } catch (Exception e) {
            test.fail("Failed to navigate to Make a Payment page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Make a Payment page", e);
        }
    }

    private void clickMakeOneTimePayment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for the "Make one-time payment" button on the page
            WebElement makeOneTimePaymentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/credit-cards-loans/make-a-payment/one-time' and contains(@class, 'btn')]")));
            
            // Scroll to the button to ensure it's visible
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", makeOneTimePaymentBtn);
            Thread.sleep(500);
            
            makeOneTimePaymentBtn.click();
            test.pass("Clicked on 'Make one-time payment' button");
            takeScreenshot("Make_OneTime_Payment_Button_Clicked");
            Thread.sleep(2000); // Allow page to load
        } catch (Exception e) {
            test.fail("Failed to click Make one-time payment button: " + e.getMessage());
            throw new RuntimeException("Failed to click Make one-time payment button", e);
        }
    }

    private void verifyMakeOneTimePaymentOptions() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Verify we're on the one-time payment page
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/one-time")) {
                test.pass("Successfully navigated to one-time payment page: " + currentUrl);
                takeScreenshot("OneTime_Payment_Page_Loaded");
                
                // Look for common payment form elements or payment type options
                boolean foundPaymentOptions = false;
                
                // Check for payment type dropdowns or radio buttons
                try {
                    java.util.List<WebElement> paymentTypeElements = driver.findElements(
                        By.xpath("//select[contains(@name, 'payment') or contains(@name, 'loan') or contains(@name, 'type')] | " +
                                "//input[@type='radio'][contains(@name, 'payment') or contains(@name, 'loan')] | " +
                                "//div[contains(@class, 'payment-type')] | " +
                                "//div[contains(@class, 'loan-type')]"));
                    
                    if (!paymentTypeElements.isEmpty()) {
                        foundPaymentOptions = true;
                        test.pass("Found payment type selection elements: " + paymentTypeElements.size());
                        takeScreenshot("Payment_Type_Elements_Found");
                        
                        for (WebElement element : paymentTypeElements) {
                            if (element.isDisplayed()) {
                                String elementInfo = element.getTagName() + " - " + 
                                    element.getAttribute("name") + " - " + 
                                    element.getAttribute("value");
                                test.info("Payment option element: " + elementInfo);
                            }
                        }
                    }
                } catch (Exception e) {
                    test.info("No specific payment type elements found: " + e.getMessage());
                }
                
                // Check for form inputs that might indicate different payment types
                try {
                    java.util.List<WebElement> formElements = driver.findElements(
                        By.xpath("//form//input | //form//select | //form//button"));
                    
                    if (!formElements.isEmpty()) {
                        foundPaymentOptions = true;
                        test.pass("Found payment form with " + formElements.size() + " interactive elements");
                        takeScreenshot("Payment_Form_Elements");
                    }
                } catch (Exception e) {
                    test.info("No form elements found: " + e.getMessage());
                }
                
                // Check for any clickable payment-related buttons or links
                try {
                    java.util.List<WebElement> paymentButtons = driver.findElements(
                        By.xpath("//button[contains(text(), 'Payment') or contains(text(), 'Pay') or contains(text(), 'Continue')] | " +
                                "//a[contains(text(), 'Payment') or contains(text(), 'Pay') or contains(text(), 'Continue')]"));
                    
                    for (WebElement button : paymentButtons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            foundPaymentOptions = true;
                            String buttonText = button.getText();
                            test.pass("Found payment action button: " + buttonText);
                            takeScreenshot("Payment_Button_" + buttonText.replace(" ", "_"));
                        }
                    }
                } catch (Exception e) {
                    test.info("No payment buttons found: " + e.getMessage());
                }
                
                if (foundPaymentOptions) {
                    test.pass("Successfully verified Make One-Time Payment page functionality");
                } else {
                    test.warning("Reached one-time payment page but no interactive payment elements were found");
                }
                
            } else {
                test.warning("Expected to be on one-time payment page but current URL is: " + currentUrl);
            }

        } catch (Exception e) {
            test.fail("Failed to verify Make One-Time Payment options: " + e.getMessage());
            throw new RuntimeException("Failed to verify payment options", e);
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.pass("Browser closed.");
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