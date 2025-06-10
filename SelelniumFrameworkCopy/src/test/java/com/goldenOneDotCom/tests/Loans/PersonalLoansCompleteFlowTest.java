package com.goldenOneDotCom.tests.Loans;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.util.List;

public class PersonalLoansCompleteFlowTest {

    private WebDriver driver;
    private ExtentTest test;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Personal Loans Complete Flow Test", 
            "Test Case 2: Verify the behavior of an application user able to click Personal Loans and navigate through all options");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testPersonalLoansCompleteFlow() {
        try {
            // Step 1: Open new browser and enter the QA URL: https://golden1.com/
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000);
            test.pass("Step 1: Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("HomePage_Loaded");

            // Step 2: Click on Loans tab
            clickLoansTab();

            // Step 3: Click the Personal Loans link under Loans tab
            clickPersonalLoansLink();

            // Step 4: Click Apply Today button
            clickApplyTodayButton();

            // Step 5: Navigate back to the Personal Loans Page
            navigateBackToPersonalLoansPage();

            // Step 6: Click Personal loans filter
            clickPersonalLoanFilter();

            // Step 7: Click Personal Line of Credit filter
            clickPersonalLineOfCreditFilter();

            // Step 8: Click Credit Starter Loan filter
            clickCreditStarterLoanFilter();

            // Step 9: Click Certificate and Savings Secured Loan filter
            clickCertificateAndSavingsSecuredLoanFilter();

            // Step 10: Click View All filter
            clickViewAllFilter();

            // Step 11: Click each Question under FAQ
            clickEachFAQQuestion();

            test.pass("Test Case 2 completed successfully: Personal Loans complete flow executed");

        } catch (Exception e) {
            test.fail("Test Case 2 failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test Case 2 failed due to exception: " + e.getMessage());
        }
    }

    private void clickLoansTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Loans') and @role='button']")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loansTab);
            Thread.sleep(1000);
            loansTab.click();
            
            test.pass("Step 2: Successfully clicked on Loans tab");
            takeScreenshot("Loans_Tab_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Loans tab: " + e.getMessage());
            takeScreenshot("Loans_Tab_Click_Failed");
            Assert.fail("Exception occurred while clicking Loans tab: " + e.getMessage());
        }
    }

    private void clickPersonalLoansLink() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement personalLoansLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Personal Loans')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", personalLoansLink);
            Thread.sleep(1000);
            personalLoansLink.click();
            
            test.pass("Step 3: Successfully clicked Personal Loans link");
            takeScreenshot("Personal_Loans_Link_Clicked");
            
            // Verify we're on the Personal Loans page
            wait.until(ExpectedConditions.urlContains("personal-loans"));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("personal-loans"), "Should be on Personal Loans page");
            test.pass("Successfully navigated to Personal Loans page");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Personal Loans link: " + e.getMessage());
            takeScreenshot("Personal_Loans_Link_Failed");
            Assert.fail("Exception occurred while clicking Personal Loans link: " + e.getMessage());
        }
    }

    private void clickApplyTodayButton() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Look for Apply Today button (could be "Apply today" or "Apply Now")
            WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Apply today') or contains(text(),'Apply Now')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", applyButton);
            Thread.sleep(1000);
            applyButton.click();
            
            test.pass("Step 4: Successfully clicked Apply Today button");
            takeScreenshot("Apply_Today_Button_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Apply Today button: " + e.getMessage());
            takeScreenshot("Apply_Today_Button_Failed");
            Assert.fail("Exception occurred while clicking Apply Today button: " + e.getMessage());
        }
    }

    private void navigateBackToPersonalLoansPage() {
        try {
            driver.navigate().back();
            Thread.sleep(2000); // Wait for page to load
            
            // Verify we're back on Personal Loans page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            wait.until(ExpectedConditions.urlContains("personal-loans"));
            
            test.pass("Step 5: Successfully navigated back to Personal Loans page");
            takeScreenshot("Navigated_Back_To_Personal_Loans");

        } catch (Exception e) {
            test.fail("Exception occurred while navigating back: " + e.getMessage());
            takeScreenshot("Navigate_Back_Failed");
            Assert.fail("Exception occurred while navigating back: " + e.getMessage());
        }
    }

    private void clickPersonalLoanFilter() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement personalLoanFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='Personal Loan' or contains(text(),'Personal Loan')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", personalLoanFilter);
            Thread.sleep(1000);
            personalLoanFilter.click();
            
            test.pass("Step 6: Successfully clicked Personal Loan filter");
            takeScreenshot("Personal_Loan_Filter_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Personal Loan filter: " + e.getMessage());
            takeScreenshot("Personal_Loan_Filter_Failed");
            Assert.fail("Exception occurred while clicking Personal Loan filter: " + e.getMessage());
        }
    }

    private void clickPersonalLineOfCreditFilter() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement personalLineOfCreditFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='Personal Line of Credit' or contains(text(),'Personal Line of Credit')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", personalLineOfCreditFilter);
            Thread.sleep(1000);
            personalLineOfCreditFilter.click();
            
            test.pass("Step 7: Successfully clicked Personal Line of Credit filter");
            takeScreenshot("Personal_Line_Of_Credit_Filter_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Personal Line of Credit filter: " + e.getMessage());
            takeScreenshot("Personal_Line_Of_Credit_Filter_Failed");
            Assert.fail("Exception occurred while clicking Personal Line of Credit filter: " + e.getMessage());
        }
    }

    private void clickCreditStarterLoanFilter() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement creditStarterLoanFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='Credit Starter Loan' or contains(text(),'Credit Starter Loan')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", creditStarterLoanFilter);
            Thread.sleep(1000);
            creditStarterLoanFilter.click();
            
            test.pass("Step 8: Successfully clicked Credit Starter Loan filter");
            takeScreenshot("Credit_Starter_Loan_Filter_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Credit Starter Loan filter: " + e.getMessage());
            takeScreenshot("Credit_Starter_Loan_Filter_Failed");
            Assert.fail("Exception occurred while clicking Credit Starter Loan filter: " + e.getMessage());
        }
    }

    private void clickCertificateAndSavingsSecuredLoanFilter() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement certificateAndSavingsFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='Certificate and Savings Secured Loan' or contains(text(),'Certificate and Savings Secured Loan')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", certificateAndSavingsFilter);
            Thread.sleep(1000);
            certificateAndSavingsFilter.click();
            
            test.pass("Step 9: Successfully clicked Certificate and Savings Secured Loan filter");
            takeScreenshot("Certificate_And_Savings_Secured_Loan_Filter_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking Certificate and Savings Secured Loan filter: " + e.getMessage());
            takeScreenshot("Certificate_And_Savings_Secured_Loan_Filter_Failed");
            Assert.fail("Exception occurred while clicking Certificate and Savings Secured Loan filter: " + e.getMessage());
        }
    }

    private void clickViewAllFilter() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            WebElement viewAllFilter = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='View All' or contains(text(),'View All')]")));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewAllFilter);
            Thread.sleep(1000);
            viewAllFilter.click();
            
            test.pass("Step 10: Successfully clicked View All filter");
            takeScreenshot("View_All_Filter_Clicked");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking View All filter: " + e.getMessage());
            takeScreenshot("View_All_Filter_Failed");
            Assert.fail("Exception occurred while clicking View All filter: " + e.getMessage());
        }
    }

    private void clickEachFAQQuestion() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            
            // Scroll to FAQ section
            WebElement faqSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'FAQ')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", faqSection);
            Thread.sleep(1000);
            
            test.pass("Successfully scrolled to FAQ section");
            takeScreenshot("FAQ_Section_Located");
            
            // Find all FAQ question buttons
            List<WebElement> faqQuestions = driver.findElements(
                By.xpath("//button[contains(@class,'accordion_title')]"));
            
            test.info("Found " + faqQuestions.size() + " FAQ questions to interact with");
            
            // Click each FAQ question
            for (int i = 0; i < faqQuestions.size(); i++) {
                try {
                    // Re-find the elements to avoid stale element reference
                    List<WebElement> currentFaqQuestions = driver.findElements(
                        By.xpath("//button[contains(@class,'accordion_title')]"));
                    
                    if (i < currentFaqQuestions.size()) {
                        WebElement question = currentFaqQuestions.get(i);
                        String questionText = question.findElement(By.tagName("h3")).getText();
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
                        Thread.sleep(500);
                        
                        // Check if already expanded
                        String ariaExpanded = question.getAttribute("aria-expanded");
                        if (!"true".equals(ariaExpanded)) {
                            question.click();
                            Thread.sleep(1000);
                            test.pass("Successfully clicked FAQ question " + (i + 1) + ": " + questionText);
                            takeScreenshot("FAQ_Question_" + (i + 1) + "_Expanded");
                        } else {
                            test.info("FAQ question " + (i + 1) + " already expanded: " + questionText);
                        }
                    }
                } catch (Exception e) {
                    test.warning("Could not interact with FAQ question " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            test.pass("Step 11: Successfully completed interaction with all FAQ questions");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking FAQ questions: " + e.getMessage());
            takeScreenshot("FAQ_Questions_Failed");
            Assert.fail("Exception occurred while clicking FAQ questions: " + e.getMessage());
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