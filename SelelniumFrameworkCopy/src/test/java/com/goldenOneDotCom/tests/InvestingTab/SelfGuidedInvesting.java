package com.goldenOneDotCom.tests.InvestingTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.testng.annotations.Parameters;

import com.utilities.BaseUtilities;
import com.utilities.DriverUtilsTest;
import com.utilities.ExtentReportManagerTest;
import com.utilities.TestConstantsTest;

public class SelfGuidedInvesting {

    private WebDriver driver;
    private ExtentTest test;

    //Selectors
    private final By investingTabButton = By.xpath("//a[text()=\"Investing\"]");
    private final By selfGuidedInvestingButton = By.xpath("//a[text()=\"Self-Guided Investing\"]");
    private final By investingWithGuidedWealthPortfoliosVideo = By.cssSelector("a.video");
    private final By investingGuidedWealthPortfoliosVideoPoster = By.cssSelector("div.video-react-fluid");
    private final By closeVideoButton = By.xpath("//img[@alt=\"Close Popup\"]");
    private final By getStartedWithBasicButton = By.xpath("//a[text()=\"Get Started with Basic\"]");
    private final By getStartedWithPlusButton = By.xpath("//a[text()=\"Get Started with PLUS\"]");
    private final By getStartedWithCancelButton = By.xpath("//button[text()=\"Cancel\"]");
    private final By getStartedWithProceedButton = By.xpath("//a[text()=\"Proceed\"]");
    private final By getStartedWithCloseButton = By.xpath("//img[@class=\"speedbump-modal__close\"]");
    private final By aboutToLeaveG1Div = By.xpath("//div[text()=\"YOU ARE ABOUT TO LEAVE GOLDEN 1 WEBSITE\"]");
    private final By speedBumpModal = By.xpath("//div[@class=\"speedbump-modal\"]");
    private final By buttonFaqs = By.cssSelector("button.accordion_title");
    private final By additionalFaqsButton = By.xpath("//a[text()=\"Additional FAQs\"]");
    private final By backButtonAdditionalFaqs = By.xpath("//a[text()=\"Back\"]");
    private final By brokerCheckLink = By.xpath("//a[text()=\"BrokerCheck\"]");
    private final By downloadForIos = By.xpath("//a[text()=\"Download for iOS\"]");
    private final By downloadForAndroid = By.xpath("//a[text()=\"Download for Android\"]");
   
    @Parameters("browser")
    @BeforeTest

    public void setUp(@Optional("chrome") String browser, ITestContext context){
        //We capture the suite name
        TestConstantsTest.SUITE_NAME = context.getSuite().getName();
        TestConstantsTest.TEST_NAME = context.getName();
        ExtentReports extentReport = ExtentReportManagerTest.getInstance(TestConstantsTest.SUITE_NAME);
        test = extentReport.createTest("Self Guided Investing Behavior Test - Smoke Test",
        "Validate the behavior after a user clicks on Self Guided Investing Tab");
        driver = DriverUtilsTest.getDriver(browser);
        test.log(Status.INFO,"Browser Started and maximized " + browser);
        System.out.println("Starting the setup");
    }

    @Test
    public void testSelfGuidedInvestingTabBehavior() throws InterruptedException{
        //Step 1: Navigate to Golden1.com
        driver.get(TestConstantsTest.G1_HOME_URL);
        Thread.sleep(1000);
        //Step 2: Click on Self Guided Investing under Investig Tab
        clickSelfGuidedInvestingLink();
        //Step 2.1: Validate all the information displayed on SelfGuided Investing Screen
        validateSelfGuidedInvestingScreenContent(TestConstantsTest.SELF_GUIDED_INVESTMENT_HEADERS_JSON,"SelfGuidedInvestmentHeaders"); 
        //Step 3: Click the video under About Investing With Guided Wealth Portfolios and click Play Button
        clickVideo();
        //Step 3.1: Check Video is playing
        checkVideoInvestingWithGuidedWealthPortfoliosIsPlaying();
        //Step 4: Click (X) button on video and check its closed
        closeVideo();
        //Step 5: Click Get Start With Basic button
        clickGetStartWithBasic();
        //Step 5.1: Check "You are about to leave Golden 1" popup displayed with Cancel, Proceed buttons and
        //X icon to close the popup
        checkAboutToLeaveGolden1PopUpDisplays();
        //Step 6: Click Proceed Button check LPL Financial screen is displayed
        checkLplFinancialScreenDisplays();
        //Step 7: Check that X button or Cancel closes the Popup from About to leave G1
        validateCloseCancelButtonAboutToLeaveG1PopUp();
        //Step 8: Click "Get Start with Plus" 
        clickGetStartedWithPlus();
        //9 This step has been validated on Step 7 since is the same component
        //Step 10: Navigate back and Click each questions on GWP, every answer should expand
        clickEachFaq(TestConstantsTest.SELF_GUIDED_INVESTMENT_HEADERS_JSON,"FAQS");
        //Step 11: Click Additional Faqs Button
        clickAdditionalFaqsButton();
        //Step 11.2: Check additional questions are displayed
        checkAdditionalFaqs(TestConstantsTest.SELF_GUIDED_INVESTMENT_HEADERS_JSON,"AdditionalFAQS");
        //Step 12 : Check Back button works as expected
        checkBackButtonAdditionalFaqs();
        //13: Check the brokercheck link under FINRA brokercheck
        checkBrokerCheckLink();
        //Step 14: Validate Download for iOS button
        validateDownloadForiOSButton();
        //Step 15: Validate Download for Android
        validateDownloadForAndroidButton();
    }


    private void validateDownloadForAndroidButton() {
        try{
            //First we get the button
            WebElement downloadForAndroidButton = BaseUtilities.findElementByXpath(driver, this.downloadForAndroid);
            //Now we scroll to the button
            BaseUtilities.ScrollToElement(driver, downloadForAndroidButton);
            //Now we click into the button
            takeScreenshot("CLICKING_DOWNLOAD_FOR_ANDROID");
            downloadForAndroidButton.click();
            Assert.assertTrue(TestConstantsTest.DOWNLOAD_FOR_ANDROID_URL.equals(BaseUtilities.getCurrentUrl(driver)));
            takeScreenshot("DOWNLOAD_FOR_ANDROID_BUTTON_REDIRECTS_CORRECTLY");
            //if the previous assertion pass then we can pass the test
            test.pass("Download for Android button works as expected");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Download for Android Button does not work "+e.getLocalizedMessage());
            test.fail("Download For Android button does not work");
        }
    }

    private void validateDownloadForiOSButton() {
        try{
            //First we get the button
            WebElement downloadForAndroidIos = BaseUtilities.findElementByXpath(driver, this.downloadForIos);
            //Now we scroll to the button
            BaseUtilities.ScrollToElement(driver, downloadForAndroidIos);
            //Now we click into the button
            takeScreenshot("CLICKING_DOWNLOAD_FOR_IOS");
            downloadForAndroidIos.click();
            //Now we need to change tab
            BaseUtilities.changeTab(driver);
            //We wait for the website to load
            BaseUtilities.waitForWebsiteToLoad(driver);
            Assert.assertTrue(TestConstantsTest.DOWNLOAD_FOR_IOS_URL.equals(BaseUtilities.getCurrentUrl(driver)));
            takeScreenshot("DOWNLOAD_FOR_IOS_BUTTON_REDIRECTS_CORRECTLY");
            //Now we close the tab and get back
            BaseUtilities.closeRecentTabAndReturnMainTab(driver);
            //if the previous assertion pass then we can pass the test
            test.pass("Download for iOS button works as expected");
            //Now we navigate back
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Download for iOS Button does not work "+e.getLocalizedMessage());
            test.fail("Download For iOS button does not work");
        }    
    }

    private void checkBrokerCheckLink() {
        try{
            //First we get the broker check link
            WebElement brokerCheckLink = BaseUtilities.findElementByXpath(driver, this.brokerCheckLink);
            //Now we scroll to the element
            BaseUtilities.ScrollToElement(driver, brokerCheckLink);
            //Now we click on it
            takeScreenshot("CLICKING_BROKER_CHECK_LINK");
            brokerCheckLink.click();
            //Now we need to change tab 
            BaseUtilities.changeTab(driver);
            //We wait for the website to load
            BaseUtilities.waitForWebsiteToLoad(driver);
            //Now we check the current url
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.BROKER_CHECK_URL));
            //We take the screenshot in the broker check page
            takeScreenshot("BROKERCHECK_WEBPAGE");
            //If the previous assertion returns true then we can close the tab 
            BaseUtilities.closeRecentTabAndReturnMainTab(driver);
            //And now we can pass the test
            test.pass("Brokercheck link is working as expected");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Broker check link does not work as expected "+e.getLocalizedMessage());
            test.fail("Broker check link is not working as expected");
        }
    }

    private void checkBackButtonAdditionalFaqs() {
        try{
            //First we check the button is displayed
            WebElement backButton = BaseUtilities.waitForElementToBeVisible(driver, this.backButtonAdditionalFaqs,
            TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now that we know is displayed we click on it
            BaseUtilities.ScrollToElement(driver, backButton);
            takeScreenshot("CLICKING_BACK_BUTTON_ADDITIONAL_FAQS");
            backButton.click();
            //and we should be back on the main page of self guided investing
            BaseUtilities.waitForWebsiteToLoad(driver);
            //We check the current url is our previous page
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.SELF_GUIDED_INVESTING_URL_REDIRECTION));
            //If the previous assertion works then we can pass the test
            test.pass("Back button from Additional Faqs of Self Guided Investing works as expected");
            takeScreenshot("BACK_BUTTON_ADDITIONAL_FAQS_SELF_GUIDED_INVESTING_REDIRECTS_CORRECTLY");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Back button Additional Faqs does not work");
            test.fail("Additional Faqs Back button does not work as expected");
        }
    }

    private void checkAdditionalFaqs(String jsonFilePath, String rootNode) {
        try{
        //In this method we will go each by each Additional faq and checking answer and question
        //that is displayed on the page vs the expected question and answer
        Map<String,List<String>> additionalFaqMap = new HashMap<>();
        //now we create the list of web elements that will contain the questions
        List<WebElement> additionalFaqsButtons = BaseUtilities.findElements(driver, this.buttonFaqs);
        for(WebElement button : additionalFaqsButtons){
            String question = button.findElement(By.tagName("h3")).getText().trim();
            System.out.println("CURRENT_QUESTION_"+question);
            BaseUtilities.ScrollToElement(driver, button.findElement(By.tagName("h3")));
            String cleanedQuestion = question.replaceAll("[^a-zA-Z0-9 ]", "");
            takeScreenshot("QUESTION_"+cleanedQuestion);
            if("false".equals(button.getAttribute("aria-expanded"))){
                button.click();
            }
            String panelId = button.getAttribute("aria-controls");
            WebElement panel = driver.findElement(By.id(panelId));

            List<WebElement> paragraphs = panel.findElements(By.tagName("p"));
            BaseUtilities.ScrollToElement(driver, panel.findElement(By.tagName("p")));
            List<String> answer = new ArrayList<>();
            for(WebElement p: paragraphs){
                String text = p.getText().trim();
                if(!text.isEmpty()){
                    System.out.println("Current answer from Webpage: "+text);
                    answer.add(text);
                }
            }
            additionalFaqMap.put(question, answer);
        }
        //Now we load the json file information
        Map<String,List<String>> expectedAdditionalFaqs = new HashMap<>();
        expectedAdditionalFaqs = BaseUtilities.loadExpectedFaqMap(jsonFilePath, rootNode);
        //Now we compare the both maps they should be equal
        boolean result = BaseUtilities.compareFaqMaps(additionalFaqMap, expectedAdditionalFaqs);
        //And we do the assertion
        Assert.assertTrue(result);
        //If the previous validation passed then we can pass the test
        test.pass("Additional Faqs and answers displayed and validated correctly");
        takeScreenshot("ADDITIONAL_FAQS_VALIDATED");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Additional FAQS are not as expected: "+e.getLocalizedMessage());
            test.fail("Additional Faqs are not as expected");
        }
    }

    private void clickAdditionalFaqsButton() {
        try{
            //We need to get the additional FAQs button first
            WebElement additionalFaqsButton = BaseUtilities.findElementByXpath(driver,this.additionalFaqsButton);
            //Now we scroll to the element
            BaseUtilities.ScrollToElement(driver, additionalFaqsButton);
            //We take an screenshot before the click
            takeScreenshot("CLICKING_ADDITIONAL_FAQS_BUTTON");
            //Now we do the click in the button
            additionalFaqsButton.click();
            //Now we wait the website to load
            BaseUtilities.waitForWebsiteToLoad(driver);
            //now we check we are in the right URL
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.ADDITIONAL_FAQS_SELF_GUIDED_INVESTING_PAGE));
            //If the previous assertion passed then we can pass the current test
            test.pass("Additional FAQs button is working as expecetd");
            takeScreenshot("ADDITIONAL_FAQS_PAGE");
        }catch(Exception e){
            e.printStackTrace();
            test.fail("Additional FAQs button is not working as expected");
            System.out.println("Additional FAQs button its not working as expected "+e.getLocalizedMessage());
        }
    }

    private void clickEachFaq(String jsonFilePath, String rootNode){
        try{
                //First we wait for the website to load back
                BaseUtilities.waitForWebsiteToLoad(driver);
                //First step will be to extract every single FAQ
                Map<String,List<String>> faqMap = new HashMap<>();
                //Now we create a list that will contain all our faq buttons text
                List<WebElement> faqButtons = BaseUtilities.findElements(driver,this.buttonFaqs);
                for(WebElement button : faqButtons){
                    String question = button.findElement(By.tagName("h3")).getText().trim();
                    System.out.println("CURRENT_QUESTION_"+question);
                    BaseUtilities.ScrollToElement(driver, button.findElement(By.tagName("h3")));
                    String cleanedQuestion = question.replaceAll("[^a-zA-Z0-9 ]", "");
                    takeScreenshot("QUESTION_"+cleanedQuestion);
                    if("false".equals(button.getAttribute("aria-expanded"))){
                        button.click();
                    }
                    String panelId = button.getAttribute("aria-controls");
                    WebElement panel = driver.findElement(By.id(panelId));

                    List<WebElement> paragraphs = panel.findElements(By.tagName("p"));
                    BaseUtilities.ScrollToElement(driver, panel.findElement(By.tagName("p")));
                    List<String> answer = new ArrayList<>();
                    for(WebElement p: paragraphs){
                        String text = p.getText().trim();
                        if(!text.isEmpty()){
                            System.out.println("Current Answer: "+text);
                            answer.add(text);
                        }
                    }
                    faqMap.put(question, answer);
                }
                    //we push every single question and asnwer from the webpage into the hasmap
                    //Now that we have the information from the FAQ section we load what we have into the json as expected
                    //questions/answers
                    Map<String,List<String>> expectedFaqs = new HashMap<>();
                    expectedFaqs = BaseUtilities.loadExpectedFaqMap(jsonFilePath,rootNode);
                    //Now that both maps have been loaded its time to compate 
                    //This should result in true if they are both good
                    boolean result =  BaseUtilities.compareFaqMaps(faqMap, expectedFaqs);
                    //Now we do the assertion to see if it passed
                    Assert.assertTrue(result);
                    //If the previous line returns true then we can pass the test
                    test.pass("The question and answers are as the expected ones");
                    takeScreenshot("FAQS_VALIDATED_SUCCESSFULLY");
            }catch(Exception e){
            e.printStackTrace();
            System.out.println("FAQS could not be validated: "+e.getLocalizedMessage());
            test.fail("Could not validate FAQS there was an error");
        }
        
    }

    private void clickGetStartedWithPlus() {
        try{
        //First we are going to locate the get start with plus button
        WebElement getStartedWithPlus = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithPlusButton,
         TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
         //Now that we already have the button we click on it
         getStartedWithPlus.click();
         //Now we are going to check that the speedbump is displayed
         Assert.assertTrue(!BaseUtilities.isElementNotPresent(driver, this.speedBumpModal));
        //Now we check that the action buttons are displayed too    
        Assert.assertTrue(!BaseUtilities.isElementNotPresent(driver, this.getStartedWithCancelButton));
        Assert.assertTrue(!BaseUtilities.isElementNotPresent(driver, this.getStartedWithCloseButton));
        Assert.assertTrue(!BaseUtilities.isElementNotPresent(driver, this.getStartedWithProceedButton));
        //If all the previous elements are displayed then we click on proceed button
        BaseUtilities.findElementByXpath(driver, this.getStartedWithProceedButton).click();
        //Now we wait for the website to load
        BaseUtilities.waitForWebsiteToLoad(driver);
        //Now we check that we are in the right website
        String currentUrl= BaseUtilities.getCurrentUrl(driver);
        //Uncomment the next line when running from G1 domain
        Assert.assertTrue(currentUrl.equals(TestConstantsTest.LPL_FINANCIAL_WEBSITE_PLUS));
        //Now we need to validate back
        BaseUtilities.navigateBack(driver);
        //And if the previous validation works then we can pass the test 
        takeScreenshot("GET_STARTED_WITH_PLUS_BUTTON_WORKS");
        test.pass("The Get Started With Plus button works as expected");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The Get Started with Plus button does not work as expected");
            test.fail("The Get Started with Plus button does not works as expected");
        }
    }

    private void validateCloseCancelButtonAboutToLeaveG1PopUp() {
       try{
        //First we wait for the website to load again
        BaseUtilities.waitForWebsiteToLoad(driver);
        //First thing we are going to do is to capture the get started with button
            WebElement getStartedWithButton = BaseUtilities.waitForElementToBeVisible(driver,this.getStartedWithBasicButton,
            TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we scroll to the element
            BaseUtilities.ScrollToElement(driver, getStartedWithButton);
            //Now we click into the get started with button
            getStartedWithButton.click();
            //Now we check the speedbump is displayed
            Assert.assertFalse(BaseUtilities.isElementNotPresent(driver,this.speedBumpModal));
            //we take an screenshot when the speedbump is displayed
            takeScreenshot("SPEEDBUMP_DISPLAYED_CORRECTLY");
            //We wait for a second before closing
            Thread.sleep(1000);
            //And now we close using the cancel button
            WebElement cancelButton = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithCancelButton,
            TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we click on cancel
            cancelButton.click();
            //We wait for a second
            Thread.sleep(1000);
            //Now we check the speedbump is not displayed
            Assert.assertTrue(BaseUtilities.isElementNotPresent(driver, this.speedBumpModal));
            //We take our first screenshot because the cancel button is displayed and working
            takeScreenshot("CANCEL_BUTTON_WORKING_CORRECTLY");
            //Now we click again into the get Started button (any of them)
            //But we need to relocate it
            BaseUtilities.findElementByXpath(driver, this.getStartedWithBasicButton).click();
            //Now we do a wait until the speedbump becomes displayed
            BaseUtilities.waitUntilVisible(driver, this.speedBumpModal,TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //We check the speedbump is displayed, since its just opened this should return true
            Assert.assertFalse(BaseUtilities.isElementNotPresent(driver, this.speedBumpModal));
            //Now we click on the close button
            WebElement closeSpeedBumpButton = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithCloseButton,
             TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
             //now we check that the close button is displayed
             Assert.assertTrue(BaseUtilities.isElementDisplayed(closeSpeedBumpButton));
              //And if the last assertion passed we can pass the test
             takeScreenshot("X_QUIT_CANCEL_BUTTON_DISPLAYED_SPEEDBUMP");
             //And if its displayed then we can proceed with the click
             closeSpeedBumpButton.click();
             //Now we check that the speedbump is not displayed
             Assert.assertTrue(BaseUtilities.isElementNotPresent(driver, this.speedBumpModal));
             //Now we take another screenshot to show they are both working
             takeScreenshot("X_CANCEL_SPEEDBUMP_BUTTON_WORKING");
             test.pass("The Cancel/X button from the Speedbump are working as expected");
       }catch(Exception e){
        e.printStackTrace();
        System.out.println("X/Cancel button does not work");
        takeScreenshot("X_CANCEL_BUTTON_POP_UP_NOT_WORKING");
       }
    }

    private void checkLplFinancialScreenDisplays() {
       try{
            //Now that we have displayed the popup
            WebElement proceedButton = BaseUtilities.waitForElementToBeVisible(driver,this.getStartedWithProceedButton,
            TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now that we already capture the proceed button we do the click
            proceedButton.click();
            //now we wait until the LPL Financial website loads
            BaseUtilities.waitForWebsiteToLoad(driver);
            //And now we do the check
            //Uncomment the next line when running from G1 domain
            Assert.assertTrue(BaseUtilities.getCurrentUrl(driver).equals(TestConstantsTest.LPL_FINANCIAL_WEBSITE_BASIC));
            //If the previous assertion is correct then we can pass the test
            takeScreenshot("LPL_FINANCIAL_WEBSITE");
            test.pass("The LPL Financial Webiste is presented after clicking on proceed button");
            //And finally we navigate back to our G1 website
            BaseUtilities.navigateBack(driver);
       }catch(Exception e){
        e.printStackTrace();
        System.out.println("Proceed button is not redirecting to the LPL website");
        test.fail("Proceed button is not redirecting to the expected Website");
        takeScreenshot("PROCEED_BUTTON_LEAVE_G1_ERROR");
       }
    }

    private void checkAboutToLeaveGolden1PopUpDisplays() {
       try{
        //First we locate the about to leave G1
       WebElement aboutToLeaveG1 = BaseUtilities.waitForElementToBeVisible(driver, this.aboutToLeaveG1Div,
       TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
       //Now we check About To Leave G1 is displayed
        Assert.assertTrue(BaseUtilities.isElementDisplayed(aboutToLeaveG1));
       //Now we check Cancel button is displayed
       WebElement cancelButton = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithCancelButton,
        TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
        //Now we check Cancel button is displayed
        Assert.assertTrue(BaseUtilities.isElementDisplayed(cancelButton));
        //Now we check Proceed button is displayed
       WebElement proceedButton = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithProceedButton,
        TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
        //Now we check Proceed button is displayed
        Assert.assertTrue(BaseUtilities.isElementDisplayed(proceedButton));
        //finally we check the X icon/Close button is displayed
        WebElement closeButton = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithCloseButton,
         TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
        //Now we check Proceed button is displayed
        Assert.assertTrue(BaseUtilities.isElementDisplayed(closeButton));
         //And if everything is displayed then we can take an screenshot and pass the test
         takeScreenshot("ABOUT_TO_LEAVE_G1_MODAL");
        test.pass("About to leave G1 modal is displaying correctly with his elements!");
       }catch(Exception e){
        e.printStackTrace();
        System.out.println("About to Leave G1 modal is not displaying correctly");
        test.fail("About to leave G1 modal is not displaying correctly");
       }
    }

    private void clickGetStartWithBasic() {
        try{
        //First we need to locate the element
        WebElement getStartedWithBasic = BaseUtilities.waitForElementToBeVisible(driver, this.getStartedWithBasicButton, 
        TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
        //now we are going to scroll to the get started with basic button
        BaseUtilities.ScrollToElement(driver, getStartedWithBasic);
        //Now we are going to proceed with the click but first we take an screenshot
        takeScreenshot("GET_STARTED_WITH_BASIC_BUTTON_CLICKED");
        getStartedWithBasic.click();
        test.pass("Get Started with Basic button clicked successfully");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The button Get Started With Basic button could not be clicked");
            test.fail("The Get Started With Basic Button could not be clicked");
        }
    }

    private void closeVideo() {
        try{
            //Now we are going to locate the close button
            WebElement closeVideoButton = BaseUtilities.waitForElementToBeVisible(driver, this.closeVideoButton,TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we click the close button
            closeVideoButton.click();
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The video could not be closed");
            test.fail("The close button does not work");
        }
    }

    private void checkVideoInvestingWithGuidedWealthPortfoliosIsPlaying() {
        try{
            //First we wait until the element is visible to capture it
            WebElement videoPoster = BaseUtilities.waitForElementToBeVisible(driver, investingGuidedWealthPortfoliosVideoPoster,
            TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we click on the poster to reproduce the video
            videoPoster.click();
            //Now we check that the video is actually playing
            //If this class shows inside the video element its because its playing
            String className = "video-react-playing";
            //If this assertion pass that means the video is playing
            Assert.assertTrue(BaseUtilities.elementHasClass(driver, investingGuidedWealthPortfoliosVideoPoster, className));
            //We wait for the video to play a little bit before taking the screenshot
            Thread.sleep(5000);
            //so now we can take the screenshot and pass the test
            takeScreenshot("INVESTING_WITH_GUIDED_WEALTH_PORTFOLIOS_VIDEO_IS_PLAYING");
            test.pass("Investing with Guided Wealth Portfolios video is playing");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The video is not playing");
            test.fail("The video is not reproducing");
        }
    }

    private void clickVideo() {
        try{
            //First we wait until the element becomes visible to capture it
            WebElement video = BaseUtilities.waitForElementToBeVisible(driver, this.investingWithGuidedWealthPortfoliosVideo
            ,TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we scroll to the element
            BaseUtilities.ScrollToElement(driver, video);
            //Now we take an screenshot
            takeScreenshot("CLICKING_VIDEO_FROM_INVESTING_WITH_GUIDED_WEALTH_PORTFOLIOS");
            video.click();
            //And if we were able to do this we pass the test
            test.pass("Video from Investing with Guided Wealth Portfolios was clicked successfully");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The video could not be clicked");
            test.fail("The video could not be reproduced");
        }
    }

    private void validateSelfGuidedInvestingScreenContent(String selfGuidedInvestmentHeadersJson, String headersArray) {
      try{
        //First we are going to validate all the content itself
        BaseUtilities.validateElementByTag(driver, selfGuidedInvestmentHeadersJson, headersArray);
        test.pass("Self Guided Investing Page content validated");
      }catch(Exception e){
        test.fail("Self Guided Investing content could not be validated");
        e.printStackTrace();
        System.out.println("Self Guided Investing Tab Content could not be validated");
      }
    }

    private void clickSelfGuidedInvestingLink() {
        try{
            //First we need to locate the Investing Tab
            WebElement investingTab = BaseUtilities.waitForElementToBeVisible(driver, this.investingTabButton
            ,TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            //Now we check is the investing tab is displayed
            Assert.assertTrue(BaseUtilities.isElementDisplayed(investingTab));
            //If the previous Assertion passed then now we can click on the self guided investing tab
            investingTab.click();
            //but first lets figure out if its displayed
            WebElement selfGuidedInvestingButton = BaseUtilities.waitForElementToBeVisible(driver, this.selfGuidedInvestingButton
            ,TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
            Assert.assertTrue(BaseUtilities.isElementDisplayed(selfGuidedInvestingButton));
            //But before lets take an Screenhot
            takeScreenshot("SELF_GUIDED_INVESTING_LINK_CLICKED");
            //And if its displayed then we can proceed and click
            selfGuidedInvestingButton.click();
            //Now we wait until the website is fully loaded
            BaseUtilities.waitForWebsiteToLoad(driver);
            //Now we need to validate that we are in the right url
            String selfGuidedRedirection = BaseUtilities.getCurrentUrl(driver);
            //Lets check with an assertion
            Assert.assertTrue(selfGuidedRedirection.equals(TestConstantsTest.SELF_GUIDED_INVESTING_URL_REDIRECTION));
            //We take screenshot of the screen
            takeScreenshot("SELF_INVESTING_GUIDE_PAGE_LOADED");
            //And now we pass the test
            test.pass("Self guided investing button redirects correctly");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Unable to find Self Guided Investing option");
            test.fail("Could not found Self Guided Investing Option");
        }    
    }

    @AfterTest
    public void tearDown(){
        if(driver != null){
            //Closes the browser
            driver.quit();
            test.pass("Browser closed");
        }
        //Sends the report to the desired location
        ExtentReportManagerTest.flushReport();
    }

    private void takeScreenshot(String screenshotName){
        if(TestConstantsTest.ENABLE_SCREENSHOTS){
            try{
                DriverUtilsTest.takeScreenshot(driver, screenshotName);
            }catch(Exception e){
                test.warning("Failed to capture screenshot: "+ e.getMessage());
            }
        }
    }
}
