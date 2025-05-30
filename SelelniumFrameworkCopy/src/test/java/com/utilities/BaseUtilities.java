package com.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseUtilities {

    // Method to find element with tag, child tag and text
    public static WebElement findElementByTagsWithText(WebDriver driver, String parentTag, String childTag,
            String expectedText) {
        // Build the Xpath structure dynamically with the text condition
        String xpath = String.format("//%s//%s[text()='%s']", parentTag, childTag, expectedText);

        // We use the driver to find the element by the generated xpath
        return driver.findElement(By.xpath(xpath));
    }
    //Method to find elements return a List of WebElements
    public static List<WebElement> findElements(WebDriver driver, By locator){
        return driver.findElements(locator);
    }
    // Method to navigate back on a webpage
    public static void navigateBack(WebDriver driver) {
        driver.navigate().back();
    }

    // Method to validate the current URL
    public static void validateCurrentURL(WebDriver driver, String expectedURL) {
        String currentUrl = driver.getCurrentUrl();
        try {
            Assert.assertEquals(currentUrl, expectedURL);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Expected URL does not match current URL");
        }
    }
    public static boolean elementHasClass(WebDriver driver, By selector, String className){
        WebElement element = waitForElementToBeVisible(driver, selector, 
        TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS);
        String classes = element.getAttribute("class");
        return classes != null && classes.contains(className);
    }

    //Check if an element is displayed
    public static boolean isElementDisplayed(WebElement element){
        return element.isDisplayed();
    }

    // Method that returns the current url
    public static String getCurrentUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    // Method to construct an xpath to find an element by using the root node tag,
    // class and parent node tag, tag and element text
    public static WebElement findElementFromRootNodeWithTextAndClass(WebDriver driver, String rootNodeTag,
            String rootNodeClass, String parentTag, String tag, String expectedText) {
        // We construct the Xpath first
        String xpath = String.format("//%s[@class='%s']/%s/%s[text()='%s']", rootNodeTag, rootNodeClass, parentTag, tag,
                expectedText);
        return driver.findElement(By.xpath(xpath));
    }

    // Method to scroll to an element (to avoid element click interception)
    public static void ScrollToElement(WebDriver driver, WebElement element) throws InterruptedException {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
        jsExecutor.executeScript("window.scrollBy(0,-100);");
        Thread.sleep(500);
    }

    // Method to find an element using the tag and expected text
    public static WebElement findElementByTagWithText(WebDriver driver, String tag,
            String expectedText) {
        // Build the Xpath structure dynamically with the text condition
        String xpath = String.format("//%s[text()='%s']", tag, expectedText);
        // We use the driver to find the element by the generated xpath
        return driver.findElement(By.xpath(xpath));
    }

    // Method to get the advisor list set into a HashMap
    public static Map<String, List<String>> getAdvisorsByLocationFromJson(String filePath) {
        Map<String, List<String>> advisorsByLocation = new HashMap<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode advisorArray = root.get("advisorsByLocation");

            for (JsonNode node : advisorArray) {
                String location = node.get("location").asText();
                List<String> advisorList = mapper.convertValue(
                        node.get("advisorList"), new TypeReference<List<String>>() {
                        });
                advisorsByLocation.put(location, advisorList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load advisors JSON", e);
        }
        return advisorsByLocation;
    }

    // Method to find an element using the tag and property from the element
    public static WebElement findElementByTagAndProperty(WebDriver driver, String tag, String property,
            String propertyValue) {
        // We build the xpath using a regular expression
        String xpath = String.format("//%s[@%s='%s']", tag, property, propertyValue);
        // we return the element that we have found based on the previous description
        return driver.findElement(By.xpath(xpath));
    }

    // Method to find a webelement by its name
    public static WebElement findElementByName(WebDriver driver, String tag, String name) {
        // We first build the Xpath to get the element
        String xpath = String.format("//%s[@name='%s']", tag, name);
        // now we return the element found by the name
        return driver.findElement(By.xpath(xpath));
    }

    public static WebElement findElementById(WebDriver driver, String tag, String id) {
        // We define the Xpath first
        String xpath = String.format("//%s[@id='%s']", tag, id);
        // Now we return a web element
        return driver.findElement(By.xpath(xpath));
    }

    // Get all the elements that has a locator in common and are displayed e.g.
    // class, name, etc.
    public static List<WebElement> getVisibleElements(WebDriver driver, By locator) {
        return driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .collect(Collectors.toList());
    }

    //Method to wait for a single element to be visible
    public static WebElement waitForElementToBeVisible(WebDriver driver,By locator,int timeout){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    //Method to wait for an element to ve displayed
    public static void waitUntilVisible(WebDriver driver, By locator, int timeout){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    //Method to check if an element is present
    public static boolean isElementNotPresent(WebDriver driver, By locactor){
        return driver.findElements(locactor).isEmpty();
    }
    // Method to wait for an element to be visible
    public static List<WebElement> waitForVisibleElements(WebDriver driver, String xpath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS));
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(By.xpath(xpath));
                List<WebElement> visible = elements.stream()
                        .filter(WebElement::isDisplayed)
                        .collect(Collectors.toList());
                return visible.isEmpty() ? null : visible;
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not found any visible element with the defined criteria");
        }
        return null;
    }

    // Method to click the first visible element
    public static void clickFirstVisible(List<WebElement> elements) {
        try {
            if (elements != null && !elements.isEmpty()) {
                elements.get(0).click();
            } else {
                System.out.println("No visible elements to click");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The list of elements came empty" + e.getLocalizedMessage());
        }
    }

    // Method to find an element using its xpath
    public static WebElement findElementByXpath(WebDriver driver, By xpath) {
        return driver.findElement(xpath);
    }

    // Method to get the first visible element
    public static WebElement getFirstVisible(List<WebElement> elements) {
        try {
            if (elements != null && !elements.isEmpty()) {
                return elements.get(0);
            } else {
                System.out.println("No visible elements to return");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The list of elements came empty" + e.getLocalizedMessage());
        }
        return null;
    }

    // Method to validate the current URL
    public static void validateCurrentURL(String expectedURL, WebDriver driver) {
        try {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.equals(expectedURL));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The URL does not match the expected URL");
        }
    }

    // Wait for website to be completely loaded method
    public static void waitForWebsiteToLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(TestConstantsTest.DEFAULT_WAIT_TIME_SECONDS)).until(
                d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    // Method to change tab
    public static void changeTab(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();

        if (windowHandles.size() > 1) {
            ArrayList<String> tabs = new ArrayList<>(windowHandles);
            String currentTab = driver.getWindowHandle();

            for (String tab : tabs) {
                if (!tab.equals(currentTab)) {
                    driver.switchTo().window(tab);
                    System.out.println("Switched to next tab");
                    return;
                } else {
                    System.out.println("Only one Tab is opened. No switch performed.");
                }
            }
        }
    }

    // Closes the most recently opened tab and switches back to the main one
    public static void closeRecentTabAndReturnMainTab(WebDriver driver) {
        // First we store all handles in a list
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());

        // Assuming the first tab is the main one, and we are on a secondary one
        if (tabs.size() > 1) {
            driver.close();// Closing current tab
            driver.switchTo().window(tabs.get(0));// Switching back to main tab
        } else {
            System.out.println("Only one tab is opened no need to switch");
        }
    }

    public static void checkVideoIsPlaying(WebDriver driver) {
        try {
            // We are going to use JS to check if an Element with <video> is playing in a
            // page
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // We define the script to see if its playing
            Boolean isPlaying = (Boolean) js.executeScript(
                    "var video = document.querySelector('video');" +
                            "return !!(video && !video.paused &&!video.ended &&video.readyState>2);");
            // Now we do an Assertion to check if the video is actually playing
            Assert.assertTrue(isPlaying, "Video is not playing as expected");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The video is not playing");
        }
    }

    // Method to find an element by using the Css Class Selector
    public static WebElement findElementByCssClassSelector(WebDriver driver, String cssClass) {
        try {
            return driver.findElement(By.cssSelector(cssClass));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Element could not be found by using the Css Class Selector" + e.getLocalizedMessage());
        }
        return null;
    }

    // Method to get the desired property from the desired element by its text
    public static String getDesiredPropertyText(String jsonFilePath, String expectedText, String rootNodeArray,
            String property)
            throws IOException {
        // We initialize Jackson Object Mapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Read the JSON file and we convert it to JsonNode
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
        // Get the JSON array
        JsonNode contentArray = rootNode.path(rootNodeArray);

        // Iterate over the array to find the object matching the expectedText
        for (JsonNode node : contentArray) {
            if (node.has("expectedText") && node.get("expectedText").asText().equals(expectedText)) {
                // If we find the matching text we return the child tag
                if (node.has(property)) {
                    return node.get(property).asText();
                } else {
                    return null;
                }

            }
        }
        return null; // Return null if no matching text is found
    }
    //Method to load the json content that contains the expected faq
    public static Map<String, List<String>> loadExpectedFaqMap(String jsonFilePath, String rootNode) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFilePath));
        JsonNode faqsNode = root.get(rootNode);

        Map<String, List<String>> faqMap = new HashMap<>();

        if(faqsNode != null && faqsNode.isArray()){
            for(JsonNode node : faqsNode){
                String question  = node.get("question").asText();
                List<String> answerList  = new ArrayList<>();
                for(JsonNode asnwerLine: node.get("answer")){
                    String line = asnwerLine.asText().trim();
                    System.out.println("ANSWER LINE FROM JSON: "+line);
                    if(!line.isEmpty()){
                        answerList.add(line);
                    }
                }
                faqMap.put(question, answerList);
            }
        }
        return faqMap;
    }

    public static boolean compareFaqMaps(Map<String,List<String>> actual, Map<String,List<String>> expected){
        boolean matches = true;

        for(String expectedQuestion : expected.keySet()){
            if(!actual.containsKey(expectedQuestion)){
                System.out.printf("X Missing Question: \"%s\"%n",expectedQuestion);
                matches = false;
                continue;
            }
            List<String> expectedAnswer = expected.get(expectedQuestion);
            List<String> actualAnswer = actual.get(expectedQuestion);

            if(!expectedAnswer.equals(actualAnswer)){
                System.out.printf("X Mismatch in answer for: \"%s\"%n",expectedQuestion);
                System.out.println("Expected: ");
                expectedAnswer.forEach(line-> System.out.println(" . "+line));
                System.out.println("Actual: ");
                actualAnswer.forEach(line-> System.out.println(" ."+line));
                matches = false;
            }
        }
        
        //check for extra questions in actual that does not exist in expected
        for(String actualQuestion : actual.keySet()){
            if(!expected.containsKey(actualQuestion)){
                System.out.printf(" Extra question in Actual FAQ: \"%s\"%n",actualQuestion);
                matches = false;
            }
        }
        return matches;
    }

    // Method to validate elements by reading from a JSON file in the resources
    // folder
    public static void validateElementByTag(WebDriver driver, String jsonFilePath, String rootNodeArray)
            throws IOException {
        // Load JSON content from resources folder
        String jsonContent = new String(
                Files.readAllBytes(Paths.get(jsonFilePath)));

        // Parse the JSON content
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        JsonNode investmentServicesSectionContent = rootNode.path(rootNodeArray);

        // Iterate through the invesmentServicesSection array
        for (JsonNode element : investmentServicesSectionContent) {
            String tag = element.path("tag").asText();
            String expectedText = element.path("expectedText").asText();
            String xpathExpression = "";
            if(element.has("property")){
                String property = element.path("property").asText();
                xpathExpression = String.format("//%s[@%s='%s']",tag,property,expectedText);
            }
            else{
                // Create XPath expression to find element by tag and text
                xpathExpression = String.format("//%s[contains(text(),'%s')]", tag, expectedText);
            }
            try {
                // Find the element using the Xpath expression if the driver is able to find the
                // element using the tag and text then
                // means that the element contains the text that we have in our json file
                WebElement webElement = driver.findElement(By.xpath(xpathExpression));
                if(expectedText.contains("?")){
                    expectedText = expectedText.replace("?","");
                }
                // validate if the element is found and if its displayed
                if (webElement != null && webElement.isDisplayed()) {
                    System.out.println("Text validation is passed for tag: " + tag);
                    System.out.println("Expected Text validated as well: " + expectedText);
                    ScrollToElement(driver, webElement);
                    DriverUtilsTest.takeScreenshot(driver, "HEADER_VALIDATED_" + expectedText);
                } else {
                    System.out.println("Text validation failed for tag: " + tag + ". Expected: " + expectedText);
                }
            } catch (Exception e) {
                System.err.println("Error locating element with Xpath: " + xpathExpression + "for tag: " + tag);
                e.printStackTrace();
            }
        }
    }
}