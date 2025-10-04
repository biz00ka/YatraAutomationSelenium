package org.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static ThreadLocal<WebDriver> localDriver = new ThreadLocal<>();
    private static DriverFactory driverFactory;

    private DriverFactory() {
    }

    // Thread-safe Singleton getter
    public static DriverFactory getInstance() {
        if (driverFactory == null) {
            // Double-checked locking for thread-safe instantiation
            synchronized (DriverFactory.class) {
                if (driverFactory == null) {
                    driverFactory = new DriverFactory();
                }
            }
        }
        return driverFactory;
    }

    public WebDriver getDriver() {
        if (localDriver.get() == null) {
            setup();
        }
        return localDriver.get();
    }

    private void setup() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        System.out.println("Browser name provided is " + browser);

        WebDriver driverInstance;

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                ChromeOptions chromeOptions = new ChromeOptions();

                // Headless for Jenkins/CI
//                chromeOptions.addArguments("--headless");
//                chromeOptions.addArguments("--disable-gpu");
//                chromeOptions.addArguments("--window-size=1920,1080");

                chromeOptions.setExperimentalOption("prefs", prefs);
                driverInstance = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.popup_maximum", 0);
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Headless for Jenkins/CI
//                firefoxOptions.addArguments("-headless");

                firefoxOptions.setProfile(profile);
                driverInstance = new FirefoxDriver(firefoxOptions);
                break;

            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setUseTechnologyPreview(true);
                driverInstance = new SafariDriver(safariOptions);
                break;

            default:
                throw new IllegalArgumentException("Incorrect browser name provided: " + browser);
        }

        localDriver.set(driverInstance);
    }

    public void tearDown() {
        if (localDriver.get() != null) {
            localDriver.get().quit();
            localDriver.remove();
        }
        System.out.println("Browser closed successfully.");
    }
}