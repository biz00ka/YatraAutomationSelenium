import org.factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.utils.PropReaderUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected WebDriver driver;
    protected ChromeOptions options;

    protected PropReaderUtil propReaderUtil;
    @BeforeClass
    public void setup() {
        try {
            // Initialize PropReaderUtil first to ensure it's available
            propReaderUtil = new PropReaderUtil("test.properties");
            // Initialize WebDriver using DriverFactory
            driver = DriverFactory.getInstance().getDriver();
        } catch (IOException e) {
            System.err.println("Failed to load test.properties: " + e.getMessage());
            throw new RuntimeException("Failed to initialize PropReaderUtil due to missing or inaccessible test.properties", e);
        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    @AfterClass
    public void tearDown() {
        try {
            if (DriverFactory.getInstance() != null) {
                DriverFactory.getInstance().tearDown();
            }
        } catch (Exception e) {
            System.err.println("Error during tearDown: " + e.getMessage());
        }
    }
}
