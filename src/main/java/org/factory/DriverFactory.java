package org.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static ThreadLocal<WebDriver> localDriver= new ThreadLocal<>();
    private static DriverFactory driverFactory;

    private DriverFactory()
    {
        this.setup();
    }
    private void setup()
    {
        String browser=System.getProperty("browser","chrome").toLowerCase();
        System.out.println("Browser name provided is "+browser);
        switch (browser) {
            case "chrome":
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", prefs);
                localDriver.set(new ChromeDriver(chromeOptions));
                break;
            case "firefox":
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("dom.popup_maximum", 0);
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);
                localDriver.set(new FirefoxDriver(firefoxOptions));
                break;
            case "safari":
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setUseTechnologyPreview(true);
                localDriver.set(new SafariDriver(safariOptions));
                break;
            default:
                throw new IllegalArgumentException("Incorrect browser name provided");
        }

    }

    public static DriverFactory getInstance()
    {
        if(driverFactory==null)
            driverFactory=new DriverFactory();

        return driverFactory;
    }

    public WebDriver getDriver() {
        // Return the WebDriver instance associated with the calling thread
        return localDriver.get();
    }

    public void tearDown()
    {

        if(localDriver.get()!=null) {
            localDriver.get().quit();
            localDriver.remove();
        }
        System.out.println("Browser closed successfully.");
    }
}
