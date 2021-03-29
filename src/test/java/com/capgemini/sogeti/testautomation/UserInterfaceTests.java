package com.capgemini.sogeti.testautomation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.slf4j.LoggerFactory.getLogger;

@SpringBootTest
public class UserInterfaceTests {

    private static final int TIMEOUT_IN_SECONDS = 3;
    private static final String XPATH_SERVICES = "//*[@id=\"header\"]/div[1]/nav/ul/li[3]/div[1]/span";
    private static final String URL_AUTOMATION = "https://www.sogeti.com/services/automation/";
    private static final String XPATH_SELECTED_SERVICES = "//*[@id=\"header\"]/div[1]/nav/ul/li[3]";
    private static final String XPATH_SELECTED_AUTOMATION = "//*[@id=\"header\"]/div[1]/nav/ul/li[3]/div[2]/ul/li[4]";
    private static final String CLASS_NAME_WORLDWIDE_NAVIGATION = "navbar-global";
    private static final String CLASS_NAME_WORLDWIDE_LIST = "country-list";
    private static final String NAME_FIRST_NAME = "__field_123927";
    private static final String NAME_LAST_NAME = "__field_123938";
    private static final String NAME_EMAIL = "__field_123928";
    private static final String NAME_PHONE = "__field_123929";
    private static final String NAME_MESSAGE = "__field_123931";
    private static final String NAME_COUNTRY = "__field_132596";
    private static final String NAME_AGREEMENT = "__field_123935";
    private static final int MIN_FIRST_NAME_LENGTH = 2;
    private static final int MAX_FIRST_NAME_LENGTH = 38;
    private static final int MIN_LAST_NAME_LENGTH = 2;
    private static final int MAX_LAST_NAME_LENGTH = 726;
    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 30;
    private static final int MIN_HOSTNAME_LENGTH = 2;
    private static final int MAX_HOSTNAME_LENGTH = 48;
    private static final int MIN_SUFFIX_LENGTH = 2;
    private static final int MAX_SUFFIX_LENGTH = 4;
    private static final int MIN_PHONE_NO_LENGTH = 10;
    private static final int MAX_PHONE_NO_LENGTH = 15;
    private static final int MIN_MSG_LENGTH = 1;
    private static final int MAX_MSG_LENGTH = 256;
    private final Logger logger = getLogger(UserInterfaceTests.class);
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;
    private JavascriptExecutor javascriptExecutor;
    private Actions actions;

    @BeforeAll
    public static void beforeAll() {
        chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {

        // Initialize the instances
        this.webDriver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(this.webDriver, TIMEOUT_IN_SECONDS);
        this.javascriptExecutor = (JavascriptExecutor) this.webDriver;
        this.actions = new Actions(this.webDriver);

        // Navigate to the homepage of SOGETI
        this.webDriver.get("https://www.sogeti.com/");
        this.logger.info("Display the homepage of sogeti");

        // Click the button to allow all the cookies to close the popup dialog
        this.webDriverWait.until(elementToBeClickable(this.webDriver.findElement(By.className("acceptCookie"))));
        this.javascriptExecutor.executeScript(
                "arguments[0].click();", this.webDriver.findElement(By.className("acceptCookie"))
        );
        this.logger.info("Allow all the cookies");

    }

    @AfterEach
    public void afterEach() {
        if (this.webDriver != null) {
            this.webDriver.quit();
            this.logger.info("Close the web browser");
        }
    }

    private void visitAutomationPage() {

        // Move the mouse over the primary menu item "Services"
        WebElement servicesUnorderedListItem = this.webDriver.findElement(By.xpath(XPATH_SERVICES));
        this.actions.moveToElement(servicesUnorderedListItem).perform();
        this.logger.info("Mouse hover over the primary menu item \"Services\"");

        // Move the mouse over the secondary menu item "Automation"
        WebElement automationUnorderedListItem = this.webDriver.findElement(By.linkText("Automation"));
        this.actions.moveToElement(automationUnorderedListItem).perform();
        this.logger.info("Mouse hover over the secondary menu item \"Automation\"");

        // Click the hyperlink of the secondary menu item "Automation"
        this.webDriverWait.until(elementToBeClickable(automationUnorderedListItem));
        this.javascriptExecutor.executeScript("arguments[0].click();", automationUnorderedListItem);
        this.logger.info("Click the secondary menu item \"Automation\"");

    }

    private void clickWebElement(String elementName) {
        WebElement webElement = this.webDriver.findElement(By.name(elementName));
        this.webDriverWait.until(elementToBeClickable(webElement));
        this.javascriptExecutor.executeScript("arguments[0].click();", webElement);
    }

    private void clickWebElementWithValue(String elementName, String value) {
        WebElement webElement = this.webDriver.findElement(By.name(elementName));
        this.webDriverWait.until(elementToBeClickable(webElement));
        this.javascriptExecutor.executeScript("arguments[0].click();", webElement);
        this.javascriptExecutor.executeScript("arguments[0].value='" + value + "';", webElement);
    }

    /**
     * Test Case 1
     */
    @Test
    public void clickAndDisplayMenuItemTest() {

        visitAutomationPage();

        // Assert that the Automation screen is displayed
        assertEquals(URL_AUTOMATION, this.webDriver.getCurrentUrl());
        this.logger.info("Display the Automation screen");

        // Assert that the Automation text is visible in the page
        assertTrue(this.webDriver.findElement(By.tagName("body")).getText().contains("Automation"));
        this.logger.info("Confirm the visibility of the Automation text");

        // Assert that both "Services" and "Automation" are selected
        WebElement selectedServicesUnorderedListItem = this.webDriver.findElement(By.xpath(XPATH_SELECTED_SERVICES));
        WebElement selectedAutomationUnorderedListItem = this.webDriver.findElement(By.xpath(XPATH_SELECTED_AUTOMATION));
        assertTrue(selectedServicesUnorderedListItem.getAttribute("class").contains("selected"));
        this.logger.info("\"Services\" is selected");
        assertTrue(selectedAutomationUnorderedListItem.getAttribute("class").contains("selected"));
        this.logger.info("\"Automation\" is selected");

    }

    /**
     * Test Case 2
     */
    @Test
    public void fillContactFormTest() {

        visitAutomationPage();

        String firstName = randomAlphabetic(MIN_FIRST_NAME_LENGTH, MAX_FIRST_NAME_LENGTH);
        clickWebElementWithValue(NAME_FIRST_NAME, firstName);
        this.logger.info("Input the first name: " + firstName);

        String lastName = randomAlphabetic(MIN_LAST_NAME_LENGTH, MAX_LAST_NAME_LENGTH);
        clickWebElementWithValue(NAME_LAST_NAME, lastName);
        this.logger.info("Input the last name: " + lastName);

        String username = randomAlphanumeric(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH).toLowerCase();
        String hostname = randomAlphanumeric(MIN_HOSTNAME_LENGTH, MAX_HOSTNAME_LENGTH).toLowerCase();
        String suffix = randomAlphabetic(MIN_SUFFIX_LENGTH, MAX_SUFFIX_LENGTH).toLowerCase();
        String emailAddress = username + "@" + hostname + "." + suffix;
        clickWebElementWithValue(NAME_EMAIL, emailAddress);
        this.logger.info("Input the Email address: " + emailAddress);

        String phoneNo = randomNumeric(MIN_PHONE_NO_LENGTH, MAX_PHONE_NO_LENGTH);
        clickWebElementWithValue(NAME_PHONE, phoneNo);
        this.logger.info("Input the phone number: " + phoneNo);

        String message = randomAlphanumeric(MIN_MSG_LENGTH, MAX_MSG_LENGTH);
        clickWebElementWithValue(NAME_MESSAGE, message);
        this.logger.info("Input the message: " + message);

        Select selectedCountry = new Select(this.webDriver.findElement(By.name(NAME_COUNTRY)));
        int optionIndex = (int) (Math.random() * selectedCountry.getOptions().size());
        selectedCountry.selectByIndex(optionIndex);
        String countryName = selectedCountry.getOptions().get(optionIndex).getText();
        this.logger.info("Choose the country: " + countryName);

        clickWebElement(NAME_AGREEMENT);
        this.logger.info("Check the \"I Agree\" box");

        // TODO: Measurement to bypass the recaptcha

        clickWebElement("submit");
        this.logger.info("Click the \"Submit\" button");

    }

    /**
     * Test Case 3
     */
    @Test
    public void clickSpecificCountryLinks() throws IOException {

        // Move the mouse over the navigation bar "Worldwide"
        WebElement worldwideNavigation = this.webDriver.findElement(By.className(CLASS_NAME_WORLDWIDE_NAVIGATION));
        this.actions.moveToElement(worldwideNavigation).perform();
        this.logger.info("Mouse hover over the navigation bar \"Worldwide\"");

        // Click the navigation bar "Worldwide"
        this.webDriverWait.until(elementToBeClickable(worldwideNavigation));
        this.javascriptExecutor.executeScript("arguments[0].click();", worldwideNavigation);
        this.logger.info("Click the navigation bar \"Worldwide\"");

        // Assert that the drop-down list "Worldwide" is displayed in the page
        WebElement worldwideDropDownList = this.webDriver.findElement(By.className(CLASS_NAME_WORLDWIDE_LIST));
        assertTrue(worldwideDropDownList.isDisplayed());
        this.logger.info("Display the drop-down list \"Worldwide\"");

        // Check the validity of the SOGETI specific country links
        List<WebElement> countryList = worldwideDropDownList.findElements(By.tagName("a"));
        for (WebElement country : countryList) {
            String url = country.getAttribute("href");
            HttpURLConnection httpURLConnection = (HttpURLConnection) ((new URL(url)).openConnection());
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            assertEquals(HTTP_OK, httpURLConnection.getResponseCode());
            this.logger.info("\"" + url + "\" is a valid link");
        }

    }

}