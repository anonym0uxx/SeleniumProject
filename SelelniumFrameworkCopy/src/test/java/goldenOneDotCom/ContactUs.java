package goldenOneDotCom;

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
import utilities.DriverUtils;
import utilities.ExtentReportManager;
import utilities.TestConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class ContactUs {

    private WebDriver driver;
    private ExtentTest test;
    @Parameters("browser")
    @BeforeTest
    
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        test = extentReport.createTest("Golden1.com Contact Us Test", "Tests the Contact Us functionality and Appointment scheduling.");
        driver = DriverUtils.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testContactUs() {
        try {
            // Step 1: Navigate to Golden1.com
            driver.get(TestConstants.G1_HOME_URL);
            Thread.sleep(1000); // Static wait to slow down execution
            test.pass("Navigated to " + TestConstants.G1_HOME_URL);
            takeScreenshot("Navigated_To_HomePage");

            // Step 2: Click on Contact Us
            clickContactUs();

            // Step 3: Fill out the feedback form
            fillFeedbackForm();

            // Step 4: Navigate to Appointment page
            navigateToAppointment();

            // Step 5: Fill out the Appointment form
            fillAppointmentForm();

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            takeScreenshot("Test_Failed");
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void clickContactUs() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement contactUsLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Careers'])[1]/following::a[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contactUsLink);
            Thread.sleep(1000); // Static wait
            contactUsLink.click();
            test.pass("Clicked 'Contact Us' link.");
            takeScreenshot("Clicked_ContactUs");

        } catch (Exception e) {
            test.fail("Exception occurred while clicking 'Contact Us': " + e.getMessage());
            takeScreenshot("ContactUs_Click_Failed");
            Assert.fail("Exception occurred while clicking 'Contact Us': " + e.getMessage());
        }
    }

    private void fillFeedbackForm() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));

            // Scroll slightly to ensure the "Email" link is visible
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
            Thread.sleep(1000);  // Static wait

            // Locate and click the Email element
            WebElement emailOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Email']")));
            try {
                emailOption.click();
                test.pass("Selected 'Email' option.");
            } catch (Exception e) {
                // Fallback to JavaScript click if regular click doesn't work
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", emailOption);
                test.pass("Selected 'Email' option using JavaScript click.");
            }
            takeScreenshot("Selected_Email_Option");
            Thread.sleep(1000);  // Static wait

            // Scroll and click on the compliment wizard element
            WebElement complimentWizard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wizard-question-answer-B448172F_48EB_45C0_AE1C_1113EA8B2FA8")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", complimentWizard);
            Thread.sleep(1000);  // Static wait
            wait.until(ExpectedConditions.elementToBeClickable(complimentWizard)).click();
            test.pass("Clicked on 'Compliment' in the wizard.");
            takeScreenshot("Clicked_Compliment_Wizard");

            // Select another element as necessary
            WebElement anotherOption = wait.until(ExpectedConditions.elementToBeClickable(By.id("wizard-question-answer-CC708F75_F573_470C_814D_4C49E7CFB63E")));
            anotherOption.click();
            test.pass("Clicked another option in the wizard.");
            takeScreenshot("Clicked_Another_Option");

            // Fill out the form fields
            WebElement commentsBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Comments")));
            commentsBox.sendKeys("Your online banking platform rocks!");
            test.pass("Filled out the Comments field.");
            takeScreenshot("Filled_Comments");

            WebElement firstNameField = driver.findElement(By.id("FirstName"));
            firstNameField.sendKeys("Test");
            test.pass("Filled out First Name.");
            takeScreenshot("Filled_FirstName");

            WebElement lastNameField = driver.findElement(By.id("LastName"));
            lastNameField.sendKeys("User");
            test.pass("Filled out Last Name.");
            takeScreenshot("Filled_LastName");

            WebElement phoneField = driver.findElement(By.name("Phone"));
            phoneField.sendKeys("9161111111");
            test.pass("Filled out Phone.");
            takeScreenshot("Filled_Phone");

            WebElement emailField = driver.findElement(By.name("Email"));
            emailField.sendKeys("916testuser2024@gmail.com");
            test.pass("Filled out Email.");
            takeScreenshot("Filled_Email");

            WebElement confirmEmailField = driver.findElement(By.id("ConfirmEmail"));
            confirmEmailField.sendKeys("916testuser2024@gmail.com");
            test.pass("Confirmed Email.");
            takeScreenshot("Confirmed_Email");

            // Submit the form
            WebElement submitButton = driver.findElement(By.id("formEE76D0D2_6956_4C65_AB3A_E57CAFD42127"));
            submitButton.click();
            test.pass("Submitted the Feedback form.");
            takeScreenshot("Feedback_Form_Submitted");

            Thread.sleep(1000); // Static wait before navigating back
            // Go back to Contact Us page to proceed with Appointments
            driver.navigate().back();
            test.pass("Navigated back to 'Contact Us' page.");
            takeScreenshot("Navigated_Back_To_ContactUs");

        } catch (Exception e) {
            test.fail("Exception occurred while filling feedback form: " + e.getMessage());
            takeScreenshot("Feedback_Form_Failure");
            Assert.fail("Exception occurred while filling feedback form: " + e.getMessage());
        }
    }

    private void navigateToAppointment() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement appointmentLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Appointment")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", appointmentLink);
            Thread.sleep(1000); // Static wait
            appointmentLink.click();
            test.pass("Clicked 'Appointment' link.");
            takeScreenshot("Clicked_Appointment");

            // Switch to the new tab
            String originalWindow = driver.getWindowHandle();
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            test.pass("Switched to the new tab for Appointments.");
            takeScreenshot("Switched_To_Appointment_Tab");

            // Handle external redirect modal
            handleExternalRedirectModal();

        } catch (Exception e) {
            test.fail("Exception occurred while navigating to Appointment page: " + e.getMessage());
            takeScreenshot("Navigate_Appointment_Failure");
            Assert.fail("Exception occurred while navigating to Appointment page: " + e.getMessage());
        }
    }

    private void handleExternalRedirectModal() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));

            // Check if the modal appears and handle it; if not, skip this step.
            if (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[text()='Proceed']"))).size() > 0) {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Proceed']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", proceedButton);
                Thread.sleep(1000); // Static wait
                proceedButton.click();
                test.pass("Clicked 'Proceed' button on external redirect modal.");
                takeScreenshot("Proceed_Clicked");
            } else {
                test.info("No external redirect modal appeared, proceeding to the next step.");
            }
        } catch (Exception e) {
            test.info("No external redirect modal appeared or exception occurred, skipping modal handling: " + e.getMessage());
            takeScreenshot("External_Redirect_Failure");
        }
    }

    private void fillAppointmentForm() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));

            // Scroll and click the first service category card
            WebElement serviceCategoryCard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/bb-journey/section/main/div/div[2]/ui-view/bb-services-page/ng-include/section/bb-service-list-categories-tree/div/div[1]/div[2]/bb-service-list-categories/div/ol/li/bb-card/div/div[2]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", serviceCategoryCard);
            Thread.sleep(1000); // Static wait
            serviceCategoryCard.click();
            test.pass("Clicked on the service category card.");
            takeScreenshot("Clicked_Service_Category");

            // Scroll and click the second service card
            WebElement serviceCard = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#journey__content > main > div > div.ng-scope > ui-view > bb-services-page > ng-include > section > bb-service-list > div > ol > li:nth-child(1) > bb-service-list-item > bb-card > div")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", serviceCard);
            Thread.sleep(1000); // Static wait
            serviceCard.click();
            test.pass("Clicked on the service card.");
            takeScreenshot("Clicked_Service_Card");

            // Wait for the location input field to be visible and interactable
            WebElement locationInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-location-input")));
            Thread.sleep(1000); // Static wait
            locationInput.sendKeys("Sacramento, CA");
            test.pass("Filled location with 'Sacramento, CA'.");
            takeScreenshot("Filled_Location");

            WebElement searchButton = driver.findElement(By.id("search-location-button"));
            Thread.sleep(1000); // Static wait
            searchButton.click();
            test.pass("Clicked 'Search' button.");
            takeScreenshot("Clicked_Search");

            // Wait for the location result to be visible and interactable, then click the first result
            WebElement locationResult = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#location-page__search-loader-container > div.location-page__results.ng-scope > bb-location-detailed-list > ul > li:nth-child(1) > bb-menu-item")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", locationResult);
            Thread.sleep(1000); // Static wait
            locationResult.click();
            test.pass("Clicked the first location result.");
            takeScreenshot("Clicked_Location_Result");

            // Wait for the QuickBook button to be visible and interactable, then click it
            WebElement quickBookButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#journey__content > main > div > div.ng-scope > ui-view > bb-location-page > ng-include > div.location-page__two-columns.ng-scope > div.location-page__two-columns__main > div.ng-scope > bb-location-detailed-card > div > div:nth-child(2) > button")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", quickBookButton);
            Thread.sleep(1000); // Static wait
            quickBookButton.click();
            test.pass("Clicked the 'QuickBook' button on the location page.");
            takeScreenshot("Clicked_QuickBook");

            // After redirect, wait for the first name field to appear and send keys "Test"
            WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#firstName")));
            Thread.sleep(1000); // Static wait
            firstNameField.sendKeys("Test");
            test.pass("Entered 'Test' in the First Name field.");
            takeScreenshot("Entered_First_Name");

            DriverUtils.takeScreenshot(driver, "Appointment_Form_Filled");

        } catch (Exception e) {
            test.fail("Exception occurred while filling out the appointment form: " + e.getMessage());
            takeScreenshot("Appointment_Form_Failure");
            Assert.fail("Exception occurred while filling out the appointment form: " + e.getMessage());
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
