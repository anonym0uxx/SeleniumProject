package goldenOneDotCom;

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
import utilities.DriverUtils;
import utilities.ExtentReportManager;
import utilities.TestConstants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;

public class TabFunctionality {

    static ExtentTest Golden1TabClickTest;
    private WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        Golden1TabClickTest = extentReport.createTest("Golden1.com Tab Click Test", "Clicks each tab and validates they are clickable");

        driver = DriverUtils.getDriver(browser);
        Golden1TabClickTest.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testClickingEachTab() {
        try {
            if (driver != null && driver.getWindowHandles().size() > 0) {
                driver.get(TestConstants.G1_HOME_URL);
                Golden1TabClickTest.pass("Navigated to " + TestConstants.G1_HOME_URL);
                DriverUtils.takeScreenshot(driver, "TabFunctionality_HomePage");

                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, TestConstants.EXPECTED_TITLE, "Homepage title mismatch!");
                Golden1TabClickTest.pass("Homepage title validated successfully");

                String[] tabs = {"Checking", "Savings", "Home Loans", "Credit Cards", "Loans", "Investing", "Community"};
                for (String tabName : tabs) {
                    validateTabIsClickable(tabName);
                }
            } else {
                Golden1TabClickTest.fail("Browser window is already closed or session was lost.");
            }
        } catch (Exception e) {
            Golden1TabClickTest.fail("Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void validateTabIsClickable(String tabName) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConstants.DEFAULT_WAIT_TIME_SECONDS));
            WebElement tabElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(tabName)));
            tabElement.click();
            Golden1TabClickTest.pass("Clicked on '" + tabName + "' tab");
            DriverUtils.takeScreenshot(driver, "TabFunctionality_" + tabName);

        } catch (Exception e) {
            Golden1TabClickTest.fail("Failed to click '" + tabName + "' tab: " + e.getMessage());
            Assert.fail("Exception occurred while clicking '" + tabName + "' tab: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentReportManager.flushReport();
        Golden1TabClickTest.info("Test completed");
    }
}
