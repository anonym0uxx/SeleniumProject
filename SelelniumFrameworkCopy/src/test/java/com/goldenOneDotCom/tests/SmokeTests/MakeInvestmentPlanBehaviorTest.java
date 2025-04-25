package com.goldenOneDotCom.tests.SmokeTests;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonSerializable.Base;
import com.utilities.BaseUtilities;
import com.utilities.DriverUtilsTest;
import com.utilities.ExtentReportManagerTest;
import com.utilities.TestConstantsTest;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MakeInvestmentPlanBehaviorTest {

    private WebDriver driver;
    private ExtentTest test;

    // This section is going to be for the locators
    private final By brokerCheckLink = By.xpath("//a[text()=\"BrokerCheck\"]");
    private final By finraLink = By.xpath("//a[text()=\"FINRA\"]");
    private final By aboutToLeaveG1Div = By.xpath("//div[text()=\"YOU ARE ABOUT TO LEAVE GOLDEN 1 WEBSITE\"]");
    private final By proceedToFinraButton = By.xpath("//a[text()=\"Proceed to FINRA\"]");
    private final By sipcButton = By.xpath("//a[text()=\"SIPC\"]");
    private final By proceedToSipc = By.xpath("//a[text()=\"Proceed to SIPC\"]");
    private final By downloadForIosLink = By.xpath("//a[text()=\"Download for iOS\"]");
    private final By downloadForAndroidLink = By.xpath("//a[text()=\"Download for Android\"]");

    @Parameters("browser")
    @BeforeTest

    public void setUp(@Optional("chrome") String browser, ITestContext context) {
        // We capture the suite name
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        test = extentReport.createTest("Smoke Tests - Investing Tab - Make Investing Plan Section Behaviors",
                "Validate that all the elements of the Investing Tab redirects to the expected sections and the expected content is displayed");
        driver = DriverUtilsTest.getDriver(browser);
        test.log(Status.INFO, "Browser started and maximized" + browser);
        System.out.println("Starting the setup");
    }

    @Test
    public void testInvestingMakeInvestmentPlan() {
        try {
            // Step 1: First we navigate to the website
            driver.get(TestConstantsTest.G1_HOME_URL);
            Thread.sleep(1000);
            // Step 2: We click the Make Investment Plan Link
            clickMakeInvestmentPlanLink();
            // Step 2.1: We validate the information in the Investment Services Screen
            validateInvestmentServicesScreenContent(TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    "investmentServicesSectionContent");
            // Step 3: Click "Find and Advisor" link under Lets Work Together
            clickFindAnAdvisorLink("Find an Advisor", TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    "investmentServicesSectionContent");
            // Step 3.1: Check it navigates to Advisor list
            findAdvisorLetsWorkTogether("div", "data-analytics-page-component-id");
            // Step 4: Click on "Find Advisor in your area" and check it navigates to
            // advisor list
            clickFindAdvisorInYourArea("Find an Advisor in Your Area", "a", "div", "data-analytics-page-component-id");
            // Step 5: Scroll and click "Digital Investing - Guided Wealth Portfolios" Link
            clickDigitalInvestingPortfolios();
            // Step 5.1: "Digital Investing - Guided Wealth Portfolios" Link should navigate
            // to Guided Wealth Portfolios Page
            validateDigitalInvestingPortfoliosLinkRedirection(TestConstantsTest.GUIDED_WEALTH_PORTFOLIOS_URL);
            // Step 6: Navigate back to Investment services page and click "LPL Account
            // view"
            clickLplAccountView("LPL Account View");
            // Step 7: Go to LPL Account view website
            proceedToAccountViewWebsite(driver);
            // Step 7.1: Now we check that we are in the LPL Account View Website
            checkLplAccountViewWebsite(driver);
            // Step 8: Navigate back to the Investment services page and click the video
            // under "Your goals are within reach"
            clickCaliforniaInsuranceLicenseInformation("California Insurance License Information",
                    "investmentServicesSectionContent");
            // Step 8.1: Validate that the PDF is displayed
            validateCaliforniaLicensePdfIsDisplayed();
            // Step 9: Navigate back and click and reproduce the video under "YOUR GOALS ARE
            // WITHIN REACH"
            playYourGoalsAreWithinReachVideo("playButton", "investmentServicesInteractableElements");
            // Step 9.2: Check Your Goals Within Reach video is playing
            checkYourGoalsWithinReachVideoPlaying(driver);
            // Step 10: Select location from the location dropdown E.g Bay Area And click Go
            // button and validate advisor list by each location
            validateAdvisorListFromLocationDropdownOption(driver);
            // Step 11: Click into a + from an advisor list and check profile is displayed
            validateProfileTriggerDisplaysAdvisorProfile();
            // Step 12: Click on "-" button an check that Advisor Bio gets closed
            validateCloseAdvisorBioButton();
            // Step 13: Click "BrokerCheck" link should navigate to BrokerCheck screen
            validateBrokerCheckLink();
            // Step 14/15: Click FINRA link and validate it redirects to You are about to
            // leave
            // and then validate it redirects to FINRA website
            validateFinraLink();
            // Step 16/17: Click "SIPC" button and validate it redirects to SIPC screen
            validateSipcLink();
            // Step 18/19 validate Download for Android/iOS
            validateDownloadForAndroidIosLinks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateDownloadForAndroidIosLinks() {
        try {
            // first we locate the Element Download For Android
            WebElement downloadForAndroidButton = BaseUtilities.findElementByXpath(driver, this.downloadForAndroidLink);
            // Now we are going to scroll to android button
            BaseUtilities.ScrollToElement(driver, downloadForAndroidButton);
            takeScreenshot("CLICK_DOWNLOAD_FOR_ANDROID");
            // Now we click on the Android Download Button
            downloadForAndroidButton.click();
            // We puse to give enough time to the website to load
            BaseUtilities.waitForWebsiteToLoad(driver);
            // Now we do the assertion to validate we got redirected to the right website
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.DOWNLOAD_FOR_ANDROID_URL));
            // If the previous assertion pass we are good to take a screenshot
            takeScreenshot("DOWNLOAD_FOR_ANDROID_WEBSITE");
            // Now we need to navigate back
            BaseUtilities.navigateBack(driver);
            // We wait for the website to load as well
            BaseUtilities.waitForWebsiteToLoad(driver);
            // Now we locate the Download For iOS button
            WebElement downloadForIosButton = BaseUtilities.findElementByXpath(driver, this.downloadForIosLink);
            // Now we are going to scroll fot DownloadForIos button
            BaseUtilities.ScrollToElement(driver, downloadForIosButton);
            // Lets take the screenshot before clicking
            takeScreenshot("CLICK_DOWNLOAD_FOR_IOS");
            // now we click into the download for iOS button
            downloadForIosButton.click();
            // Now we need to change tab since this one opens a new tab
            BaseUtilities.changeTab(driver);
            // We wait again for the website to load
            BaseUtilities.waitForWebsiteToLoad(driver);
            Thread.sleep(5000);
            // Now we do the asertion to check we are in the right website
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.DOWNLOAD_FOR_IOS_URL));
            // If the previous assertion pass then we can pass the test and take the last
            // screenshot
            takeScreenshot("DOWNLOAD_FOR_IOS_WEBSITE");
            test.pass("Bot Download for Android/iOS buttons works as expected");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Download for Android/iOS buttons are not working");
            test.fail("Download for Android/iOS are not working");
        }
    }

    private void validateSipcLink() {
        try {
            // First we locate the only visible SIPC link
            List<WebElement> sipcLink = BaseUtilities.getVisibleElements(driver, this.sipcButton);
            BaseUtilities.ScrollToElement(driver, BaseUtilities.getFirstVisible(sipcLink));
            // We capture a screenshot before making click on the SIPC link
            takeScreenshot("CLICKING_SIPC_LINK");
            // We get the first visible link and click on it
            BaseUtilities.getFirstVisible(sipcLink).click();
            // Now we have to change tab
            BaseUtilities.changeTab(driver);
            // Validate About to Leave G1 is displayed
            Assert.assertTrue(BaseUtilities.findElementByXpath(driver, aboutToLeaveG1Div).isDisplayed());
            // take the screenshot
            takeScreenshot("PROCEED_TO_SIPC");
            // If we are in this screen then we can click on proceed button
            BaseUtilities.findElementByXpath(driver, this.proceedToSipc).click();
            ;
            // Now we validate that we are in the correct URL
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.SIPC_WEBSITE_URL));
            // If the previous validation works then we can pass the test
            test.pass("SIPC link works as expected");
            takeScreenshot("SIPC_LINK_VALIDATED");
            // And finally we close the recently opened tab and go back to main one
            BaseUtilities.closeRecentTabAndReturnMainTab(driver);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SIPC button is not working");
            test.fail("SIPC button is not working as expected");
        }
    }

    private void validateFinraLink() {
        try {
            // First we locate the FINRA link in the website
            List<WebElement> finraLink = BaseUtilities.getVisibleElements(driver, this.finraLink);
            BaseUtilities.ScrollToElement(driver, BaseUtilities.getFirstVisible(finraLink));
            // We caputre a screenshot before the click
            takeScreenshot("CLICK_INTO_FINRA_LINK");
            BaseUtilities.getFirstVisible(finraLink).click();
            // Now we need to change to the new opened tab
            BaseUtilities.changeTab(driver);
            // Now we are in the About to leave g1 screen
            WebElement aboutToLeaveG1 = BaseUtilities.findElementByXpath(driver, this.aboutToLeaveG1Div);
            WebElement proceedToFinraButton = BaseUtilities.findElementByXpath(driver, this.proceedToFinraButton);
            // We check if the about to leave G1 is displayed
            if (aboutToLeaveG1.isDisplayed()) {
                // If its displayed we click on the proceed button
                proceedToFinraButton.click();
            }
            // Now we validate that we have landed in the correct URL
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.FINRA_WEBSITE_URL));
            // If the previous assertion works we can pass the test
            test.pass("Finra link works as expected");
            takeScreenshot("FINRA_LINK_VALIDATED");
            BaseUtilities.closeRecentTabAndReturnMainTab(driver);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FINRA link is not working properly");
            test.fail("FINRA LINK is not working properly");
        }
    }

    private void validateBrokerCheckLink() {
        try {
            // First we locate the Broker check link by using the locator we have declared
            // on top
            WebElement brokerCheckLink = BaseUtilities.findElementByXpath(driver, this.brokerCheckLink);
            BaseUtilities.ScrollToElement(driver, brokerCheckLink);
            // We capture a screnshot before the click
            takeScreenshot("CLIKING_BROKER_CHECK_LINK");
            brokerCheckLink.click();
            // Now we need to change tab because it opens a new one
            BaseUtilities.changeTab(driver);
            // Now we validate that we are in the right URL and if this throws true we are
            // good to pass the test
            Assert.assertTrue(TestConstantsTest.BROKER_CHECK_URL.equals(BaseUtilities.getCurrentUrl(driver)));
            test.pass("Broker Check link working as expected");
            takeScreenshot("BROKER_CHECK_LINK_VALIDATED");
            // Now we close the current tab and go back to the original one
            BaseUtilities.closeRecentTabAndReturnMainTab(driver);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Brokercheck link is broken");
            test.fail("Brokercheck link is not working properly");
        }
    }

    private void validateCloseAdvisorBioButton() {
        try {
            // We define the locators for Close profile button and Bio div element
            String advisorBio = "//div[@class=\"m15-bio\"]";
            String closeAdvisorBioButton = "a.profile-open";

            List<WebElement> advisorBioDiv = BaseUtilities.getVisibleElements(driver, By.xpath(advisorBio));
            WebElement closeAdvisorProfileButton = BaseUtilities.findElementByCssClassSelector(driver,
                    closeAdvisorBioButton);
            // Now we check that the close Bio button is displayed
            Assert.assertTrue(closeAdvisorProfileButton.isDisplayed());
            // We check as well that the Bio of the Advisor is displayed
            Assert.assertTrue(BaseUtilities.getFirstVisible(advisorBioDiv).isDisplayed());
            // If both are displayed now we Scroll to the Close Bio button
            BaseUtilities.ScrollToElement(driver, closeAdvisorProfileButton);
            closeAdvisorProfileButton.click();
            takeScreenshot("CLOSING_ADVISOR_BIO");
            Thread.sleep(1000);
            // Now we check again that the bio is not displayed anymore
            if (!BaseUtilities.getFirstVisible(advisorBioDiv).isDisplayed()) {
                // Now we locate the expand button
                String expandProfileLocator = "//img[@src=\"/assets/images/icon-plus.png\"]";
                List<WebElement> expandProfileImages = BaseUtilities.getVisibleElements(driver,
                        By.xpath(expandProfileLocator));
                // So if the + img icon is displayed that means that close button has changed is
                // state
                Assert.assertTrue(BaseUtilities.getFirstVisible(expandProfileImages).isDisplayed());
                BaseUtilities.ScrollToElement(driver, BaseUtilities.getFirstVisible(expandProfileImages));
                takeScreenshot("ADVISOR_BIO_CLOSED");
                test.pass("Advisor Bio is closed successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The close Advisor Bio button does not work as expected");
            test.fail("Advisor Bio could not be closed");
        }

    }

    private void validateProfileTriggerDisplaysAdvisorProfile() {
        try {
            // We define the Xpath for the expand button
            String locator = "//div[@class=\"m15-profile-trigger\"]/a";
            // We get all visible profile triggers buttons
            List<WebElement> profileTriggerVisibleElements = BaseUtilities.getVisibleElements(driver,
                    By.xpath(locator));
            // We scroll to the element to give a better visibility of the upcoming action
            BaseUtilities.ScrollToElement(driver, BaseUtilities.getFirstVisible(profileTriggerVisibleElements));
            takeScreenshot("CLICK_INTO_EXPAND_ADVISOR_PROFILE");
            BaseUtilities.clickFirstVisible(profileTriggerVisibleElements);
            String collapseProfileButtonLocator = "//span[text()=\"Close\"]";
            List<WebElement> collapseProfileBioButton = BaseUtilities.getVisibleElements(driver,
                    By.xpath(collapseProfileButtonLocator));
            // We check that the expand button has changed is state to collapse so now the
            // collapse button must be displayed
            Assert.assertTrue(BaseUtilities.getFirstVisible(collapseProfileBioButton).isDisplayed());
            // This is the locator for the main bio after the click this must be displayed
            // with the advisor information
            String mainBioLocator = "//div[@class=\"m15-main-bio\"]";
            // We look for all the visible elements of main bios
            List<WebElement> advisorMainBio = BaseUtilities.getVisibleElements(driver, By.xpath(mainBioLocator));
            takeScreenshot("ADVISOR_BIO_IS_DISPLAYED");
            // We do the assertion just to double check that is displayed
            Assert.assertTrue(BaseUtilities.getFirstVisible(advisorMainBio).isDisplayed());
            test.pass("Expand advisor profile button works as expected");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Expand Advisor profile button does not work");
            test.fail("Expand profile button does not work properly");
        }
    }

    private void validateAdvisorListFromLocationDropdownOption(WebDriver driver) throws IOException {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            Map<String, List<String>> advisorMap = BaseUtilities
                    .getAdvisorsByLocationFromJson(TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON);
            String rootNode = "investmentServicesInteractableElements";
            String jsonFilePath = TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON;
            String tag = BaseUtilities.getDesiredPropertyText(jsonFilePath, "locationDropdown",
                    "investmentServicesInteractableElements", "tag");
            String name = BaseUtilities.getDesiredPropertyText(jsonFilePath, "locationDropdown",
                    "investmentServicesInteractableElements", "name");
            // We identify the location dropdown
            Select locationDropdown = new Select(BaseUtilities.findElementByName(driver, tag, name));
            // Now we are going to define the properties for the profile search button
            String profileSearchButtonTag = BaseUtilities.getDesiredPropertyText(jsonFilePath, "profileSearchButton",
                    rootNode, "tag");
            String profileSearchButtonId = BaseUtilities.getDesiredPropertyText(jsonFilePath, "profileSearchButton",
                    rootNode, "id");
            WebElement profileSearchButton = BaseUtilities.findElementById(driver, profileSearchButtonTag,
                    profileSearchButtonId);
            // We define the advisor name tag
            String advisorNameTag = "h3";
            for (Map.Entry<String, List<String>> entry : advisorMap.entrySet()) {
                String location = entry.getKey();
                List<String> expectedAvisors = entry.getValue();

                // Now we are going to scroll to the profile search button
                BaseUtilities.ScrollToElement(driver, profileSearchButton);
                // Now we select a location from the area dropdown
                locationDropdown.selectByVisibleText(location);
                // We take an screenshot of the selected location
                System.out.println("CURRENT LOCATION:" + location);
                if (location.contains("Napa")) {
                    String[] locationName = location.split("/");
                    StringBuilder locationMixResult = new StringBuilder();
                    for (String word : locationName) {
                        locationMixResult.append(word);
                    }
                    takeScreenshot("LOCATION_SELECTED_" + locationMixResult);
                } else {
                    takeScreenshot("LOCATION_SELECTED_" + location);
                }
                BaseUtilities.ScrollToElement(driver, profileSearchButton);
                // Now we click on GO
                profileSearchButton.click();
                // With this line we define that we wait until the advisor profiles are
                // displayed
                wait.until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath("//div[@class=\"m15-profile-content\"]/h3")));

                // Now we validate that the names that belong to that location are present
                for (String advisor : expectedAvisors) {
                    try {
                        WebElement matches = BaseUtilities.findElementByTagWithText(driver, advisorNameTag, advisor);
                        // We scroll to every single advisor name
                        BaseUtilities.ScrollToElement(driver, matches);
                        Assert.assertNotNull(matches, "Advisor not found " + advisor + " in location: " + location);
                        takeScreenshot("ADVISOR_FOUND_" + advisor);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Advisor coult not be found " + e.getLocalizedMessage());
                    }
                }
            }
            test.pass("Advisor List validated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("None advisor list could be found in any of the selected locations");
            test.fail("Advisor list by location need to be checked");
        }
    }

    private void checkYourGoalsWithinReachVideoPlaying(WebDriver driver) {
        try {
            // We use the method we have created to check if the video is actually playing
            BaseUtilities.checkVideoIsPlaying(driver);
            // Now we can pass the test and take the screenshot
            test.pass("The video under Your goals are within reach is playing as expected");
            takeScreenshot("YOUR_GOALS_ARE_WITHIN_REACH_VIDEO_PLAYING_CORRECTLY");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sorry the video is not playing" + e.getLocalizedMessage());
        }
    }

    private void playYourGoalsAreWithinReachVideo(String expectedText, String rootNode) throws IOException {
        try {
            // First we need to navigate back
            driver.navigate().back();
            // Now we wait until the entire website is loaded
            new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS)).until(
                    d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            // Now we neeed to find the play button to play the video
            String playButtonClass = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON, expectedText, rootNode, "class");
            // We get the play button tag
            String playButtonTag = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    expectedText, rootNode, "tag");
            // We wait until the element is clickable
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            // Now we identify the WebElement for the play button
            WebElement playButton = BaseUtilities.findElementByTagAndProperty(driver, playButtonTag, "class",
                    playButtonClass);
            // Now we scroll to the play button
            BaseUtilities.ScrollToElement(driver, playButton);
            // We make the wait now
            wait.until(ExpectedConditions.elementToBeClickable(playButton));
            // Now we click on Play
            playButton.click();
            takeScreenshot("YOUR_GOALS_WITHIN_REACH_VIDEO_PLAY_BUTTON_CLICKED");
            test.pass("Clicked play button on Your Goals are Within Reach Video");
        } catch (Exception e) {
            System.out.println("We could not found the Play video button");
            e.printStackTrace();
            test.fail("Play button could not be found");
        }
    }

    private void validateCaliforniaLicensePdfIsDisplayed() {
        try {
            // First we wait until the entire website is loaded
            new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS)).until(
                    d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            // First we obtain the current URL
            String currentURL = driver.getCurrentUrl();
            // First we see if the url contains .pdf extension because that means we are
            // inside a PDF
            if (currentURL.contains(".pdf")) {
                // Is the previous validation is true then we do the assertion of the URL
                // againts the one we have saved already
                Assert.assertTrue(currentURL.equals(TestConstantsTest.CALIFORNIA_INSURANCE_LICENSE_INFORMATION_PDF));
                // And now we can pass the test
                test.pass("PDF California Insurance License PDF is correctly displayed");
                takeScreenshot("CALIFORNIA_LICENSE_INSURANCE_PDF_DISPLAYED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PDF is not displayed");
            test.fail("PDF is not displayed correctly" + e.getLocalizedMessage());
        }
    }

    private void clickCaliforniaInsuranceLicenseInformation(String elementText, String rootNode) throws IOException {
        try {
            String californiaLicensePdfTag = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON, elementText, rootNode, "tag");
            WebElement californiaInsuranceLicensePdfLink = BaseUtilities.findElementByTagWithText(driver,
                    californiaLicensePdfTag, elementText);
            BaseUtilities.ScrollToElement(driver, californiaInsuranceLicensePdfLink);
            takeScreenshot("CALIFORNIA_LICENSE_INSURANCE_LINK_CLICKED");
            californiaInsuranceLicensePdfLink.click();
            test.pass("California License Insurance PDF Link Clicked successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not found the Insurance License PDF Link");
            test.fail("Could not found the Insurance License PDF Link");
        }
    }

    private void checkLplAccountViewWebsite(WebDriver driver) throws IOException {
        try {
            String rootNode = "lplAccountView";
            String desiredProperty = "url";
            // We first need to get the LPL account view URL that we have stored in our json
            // file
            String lplAccountViewWebsiteUrl = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    "accountViewWebsite", rootNode, desiredProperty);
            // Now we validate the current url matches the LPL Account view URL
            String currentUrl = BaseUtilities.getCurrentUrl(driver);

            if (lplAccountViewWebsiteUrl.equals(currentUrl)) {
                test.pass("LPL Account view Website reached");
                Thread.sleep(1000);
                takeScreenshot("LPL_ACCOUNT_VIEW_LOGIN_PAGE");
                // After taking the screenshot for evidence we close the opened tab and return
                // to the main tab
                BaseUtilities.closeRecentTabAndReturnMainTab(driver);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("We are not in the Lpl Account view website");
        }
    }

    // Method to make the process to go to LPL Account view Website
    private void proceedToAccountViewWebsite(WebDriver driver) throws IOException {
        try {
            // First we need to change to the new tab so we are going to use our change tab
            // method
            BaseUtilities.changeTab(driver);
            // And now we wait for the webiste to be completely loaded
            BaseUtilities.waitForWebsiteToLoad(driver);
            // We declare the rootNode from our json file that contains the related
            // information to lpl account view
            String rootNodeArray = "lplAccountView";
            // Now we are going to check that we are in the About to Leave G1 page
            // So first we need the element tag
            String aboutToLeaveG1HeaderText = "YOU ARE ABOUT TO LEAVE GOLDEN 1 WEBSITE";
            String aboutToLeaveG1HeaderTag = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON, aboutToLeaveG1HeaderText,
                    rootNodeArray, "tag");
            WebElement aboutToLeaveG1Header = BaseUtilities.findElementByTagWithText(driver, aboutToLeaveG1HeaderTag,
                    aboutToLeaveG1HeaderText);
            if (aboutToLeaveG1Header.isDisplayed()) {
                String proceedToAccountViewButtonText = "Proceed to Account View";
                String proceedToAccountViewButtonTag = BaseUtilities.getDesiredPropertyText(
                        TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON, proceedToAccountViewButtonText,
                        rootNodeArray, "tag");
                WebElement proceedToAccountViewLink = BaseUtilities.findElementByTagWithText(driver,
                        proceedToAccountViewButtonTag, proceedToAccountViewButtonText);

                if (proceedToAccountViewLink.isDisplayed()) {
                    takeScreenshot("PROCEED_TO_ACCOUNT_VIEW_LINK_CLICKED");
                    Thread.sleep(1000);
                    proceedToAccountViewLink.click();
                    test.pass("Proceed to Account View button clicked");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not proceed to LPL Account view Website");
        }
    }

    // Method to click the LPL Account View
    private void clickLplAccountView(String expectedText) {
        try {
            // First we navigate back to the invesment services page
            BaseUtilities.navigateBack(driver);
            // Now we are going to get every parameter that we are going to use to find the
            // LPL account view link
            String rootNodeTag = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    expectedText,
                    "investmentServicesSectionContent", "rootNodeTag");
            String rootNodeClass = BaseUtilities.getDesiredPropertyText(
                    TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON, expectedText,
                    "investmentServicesSectionContent", "rootNodeClass");
            String parentTag = BaseUtilities.getDesiredPropertyText(TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    expectedText,
                    "investmentServicesSectionContent", "parentTag");
            String tag = BaseUtilities.getDesiredPropertyText(TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    expectedText,
                    "investmentServicesSectionContent", "tag");

            // Now we have to find the LPL Account view Link
            WebElement lplAccountViewLink = BaseUtilities.findElementFromRootNodeWithTextAndClass(driver, rootNodeTag,
                    rootNodeClass, parentTag, tag, expectedText);
            // Now that we have captured the element we scroll to it to dont have any
            // inteference
            BaseUtilities.ScrollToElement(driver, lplAccountViewLink);
            // now finally we click into the LPL Account View Link
            lplAccountViewLink.click();
            // Now we put the status to passed
            test.pass("LPL Account View Link Clicked Successfully");
            takeScreenshot("LPL_ACCOUNT_VIEW_LINK_CLICKED");
            // We do a little pause in the execution to wait for the new tab to be opened
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Sorry we couldn't find the LPL Account View button/Link");
            e.printStackTrace();
            test.fail("LPL Account View Link could not be found");
        }
    }

    // Method to validate the URL of the Guided Wealth Portfiolios
    private void validateDigitalInvestingPortfoliosLinkRedirection(String guidedWealthPortfoliosUrl)
            throws InterruptedException {
        // First we pause the execution a little bit to give the page time to load the
        // new page
        Thread.sleep(1000);
        // We use the method that we have created in our base utilities class to
        // validate the current url matches the guided wealth portfolios page
        BaseUtilities.validateCurrentURL(guidedWealthPortfoliosUrl, driver);
    }

    // Method to click on Digital Investing Portiolfios Link
    private void clickDigitalInvestingPortfolios() throws IOException {
        try {
            // This is the text of the element that we want to look for
            String linkText = "Digital Investing - Guided Wealth Portfolios";
            // We define which property we need
            String property = "tag";
            // We obtain the tag by using the text of the element from our json file
            String tag = BaseUtilities.getDesiredPropertyText(TestConstantsTest.INVESTMENT_SERVICES_HEADERS_JSON,
                    linkText,
                    "investmentServicesSectionContent", property);
            // Now we search for the Webelement
            WebElement digitalInvestingPortfolioLink = BaseUtilities.findElementByTagWithText(driver, tag, linkText);
            // Now we move the view to the element to avoid any interception
            BaseUtilities.ScrollToElement(driver, digitalInvestingPortfolioLink);
            takeScreenshot("DIGITAL_INVESTING_PORTFOLIO_LINK_CLICKED");
            // Now we click into the Digital Investing Portfolio link
            digitalInvestingPortfolioLink.click();
            test.pass("Digital Investing Portfolio Link was clicked");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("We could not reach the Digital Investing Portfolio Link");
            test.fail("Digital Investing Portfolio link could not be reached");
        }
    }

    // Method to click on Find Advisor in Your Area Link
    private void clickFindAdvisorInYourArea(String expectedText, String tagLink, String tagAdvisorList,
            String property) {
        try {
            WebElement findAvisorInYourAreaLink = BaseUtilities.findElementByTagWithText(driver, tagLink, expectedText);
            BaseUtilities.ScrollToElement(driver, findAvisorInYourAreaLink);
            takeScreenshot("FIND_ADVISOR_IN_YOUR_AREA_CLICKED");
            findAvisorInYourAreaLink.click();
            String urlAfterClickFindAdvisor = driver.getCurrentUrl();
            System.out.println("CURRENT URL: " + urlAfterClickFindAdvisor);
            String[] urlHashValue = urlAfterClickFindAdvisor.split("#");
            System.out.println("HASH VALUE: " + urlHashValue[1]);
            WebElement advisorList = BaseUtilities.findElementByTagAndProperty(driver, tagAdvisorList, property,
                    urlHashValue[1]);
            // We pause the execution a little bit
            Thread.sleep(1000);
            if (advisorList.isDisplayed()) {
                System.out.println("ADVISOR LIST DISPLAYED");
                test.pass("Find Advisor in Your Area Link navigates to Advisor List");
                takeScreenshot("ADVISOR_IN_YOUR_AREA_LINK_NAVIGATES_TO_ADVISOR_LIST");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("find advisor in your area link does not navigates to advisor list....");
            test.fail("find advisor in your area link does not navigates to advisor list....");
        }
    }

    // Method to click on Find Advisor Link
    private void clickFindAnAdvisorLink(String expectedText, String jsonFilePath, String rootNodeArray)
            throws IOException {
        try {
            // We define the properties that we want
            String[] properties = { "tag", "childTag" };
            // First we create the string to get the parent tag
            String parentTag = BaseUtilities.getDesiredPropertyText(jsonFilePath, expectedText, rootNodeArray,
                    properties[0]);
            System.out.println("PARENT TAG: " + parentTag);
            // Now we create the string that will save the child tag
            String childTag = BaseUtilities.getDesiredPropertyText(jsonFilePath, expectedText, rootNodeArray,
                    properties[1]);
            System.out.println("CHILD TAG: " + childTag);
            // And now we try to find the element based on the tags and the text
            WebElement advisorLinkElement = BaseUtilities.findElementByTagsWithText(driver, parentTag, childTag,
                    expectedText);

            if (advisorLinkElement.isDisplayed()) {
                BaseUtilities.ScrollToElement(driver, advisorLinkElement);
                takeScreenshot("FIND_ADVISOR_LINK_UNDER_LETS_WORK_TOGETHER_CLICKED");
                advisorLinkElement.click();
                test.pass("Find An Advisor link clicked");
                System.out.println("Find Advisor under lets work together navigates to advisor list");
            }
        } catch (Exception e) {
            System.out.println("No advisorLinkElement was found: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    // This method will validate that after clicking find an advisor link navigates
    // to the advisors list
    private void findAdvisorLetsWorkTogether(String tag, String property) {
        try {
            String urlAfterClickFindAdvisor = driver.getCurrentUrl();
            System.out.println("CURRENT URL: " + urlAfterClickFindAdvisor);
            String[] urlHashValue = urlAfterClickFindAdvisor.split("#");
            System.out.println("HASH VALUE: " + urlHashValue[1]);
            WebElement advisorList = BaseUtilities.findElementByTagAndProperty(driver, tag, property, urlHashValue[1]);
            // We pause the execution a little bit
            Thread.sleep(1000);
            if (advisorList.isDisplayed()) {
                System.out.println("ADVISOR LIST DISPLAYED");
                test.pass("Find Advisor Link under Lets work together navigates to advisor list");
                takeScreenshot("ADVISOR_LINK_LETS_WORK_TOGETHER_ADVISOR_LIST");
            }
        } catch (Exception e) {
            System.out.println("The link didn't navigated to the advisor list");
            test.fail("The Find Advisor link didnt navigated to the advisor list");
            e.printStackTrace();
        }
    }

    // Method to validate
    private void validateInvestmentServicesScreenContent(String jsonFilePath, String rootNodeArray) throws IOException {
        BaseUtilities.validateElementByTag(driver, jsonFilePath, rootNodeArray);
        test.pass("Investment Services screen headers, titles, link are correctly displayed");
        takeScreenshot("MAKE_INVESTMENT_PLAN_HEADERS_VALIDATED");
    }

    // Method to click on Make Investment Plan link
    private void clickMakeInvestmentPlanLink() {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            WebElement investingNavButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(),'Investing') and @role='button']")));
            investingNavButton.click();
            WebElement makeInvestmentPlanLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(),'Make an Investment Plan')]")));
            makeInvestmentPlanLink.click();
            String expectedUrl = "https://www.golden1.com/planning-and-investing/investment-services";

            if (driver.getCurrentUrl().equals(expectedUrl)) {
                test.pass("We have reached the invesment services section as expected");
                takeScreenshot("INVESTMENT_SERVICES_SECTION");
            }
        } catch (Exception e) {
            test.fail("An unexpected error ocurred while trying to open the Investing Tab..." + e.getMessage());
        }
    }

    // Method to shut down the browser and close it when finish execution
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            // Closes the browser
            driver.quit();
            test.pass("Browser closed");
        }
        // Sends the report to the desired location
        ExtentReportManagerTest.flushReport();
    }

    // Method to take an screenshot at the end of the test
    private void takeScreenshot(String screenshotName) {
        if (TestConstantsTest.ENABLE_SCREENSHOTS) {
            try {
                DriverUtilsTest.takeScreenshot(driver, screenshotName);
            } catch (Exception e) {
                System.out.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}