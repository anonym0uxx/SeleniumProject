package goldenOneDotCom;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class SearchIndexFeature {

    static ExtentTest Golden1SearchTest;
    private WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        ExtentReports extentReport = ExtentReportManager.getInstance();
        Golden1SearchTest = extentReport.createTest("Golden1.com Search Test", "Tests the search functionality on Golden1.com");

        driver = DriverUtils.getDriver(browser);
        Golden1SearchTest.log(Status.INFO, "Browser started and maximized: " + browser);
    }

    @Test
    public void testGolden1SearchFunctionality() {
        try {
            if (driver != null && driver.getWindowHandles().size() > 0) {
                driver.get(TestConstants.G1_HOME_URL);
                Golden1SearchTest.pass("Navigated to " + TestConstants.G1_HOME_URL);
                DriverUtils.takeScreenshot(driver, "SearchFunctionality_HomePage");

                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, TestConstants.EXPECTED_TITLE, "Homepage title mismatch!");
                Golden1SearchTest.pass("Homepage title validated successfully");

                // Locate the search box using the correct ID and perform search
                WebElement searchBox = driver.findElement(By.id("site-search"));
                searchBox.sendKeys("Checking");
                searchBox.sendKeys(Keys.RETURN);
                Golden1SearchTest.pass("Entered 'Checking' into the search box and pressed Enter");
                DriverUtils.takeScreenshot(driver, "SearchFunctionality_AfterSearch");
            } else {
                Golden1SearchTest.fail("Browser window is already closed or session was lost.");
            }
        } catch (Exception e) {
            Golden1SearchTest.fail("Test failed due to exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentReportManager.flushReport();
        Golden1SearchTest.info("Test completed");
    }
}
