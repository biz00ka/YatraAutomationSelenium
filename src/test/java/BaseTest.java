// REVISED BaseTest.java

import org.factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.utils.PropReaderUtil;

import java.io.IOException;

public class BaseTest {

    protected WebDriver driver;
    // 1. MAKE PropReaderUtil STATIC
    protected static PropReaderUtil propReaderUtil;

    // 2. USE A STATIC BLOCK FOR FAIL-SAFE INITIALIZATION
    static {
        try {
            // Initialize utility here, before any thread starts
            propReaderUtil = new PropReaderUtil("test.properties");
        } catch (IOException e) {
            // If initialization fails, log it and terminate gracefully
            System.err.println("FATAL ERROR: Could not load test.properties from classpath.");
            e.printStackTrace();
            // Optional: throw a RuntimeException to stop all TestNG execution immediately
            throw new RuntimeException("Failed to initialize PropReaderUtil due to file error.", e);
        }
    }

    @BeforeClass
    public void setup() throws IOException {
        // No need to initialize propReaderUtil again, it's already static and ready
        // The driver initialization is now the only thing left.
        try {
            driver = DriverFactory.getInstance().getDriver();
        } catch (Exception e) {
            System.err.println("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    @AfterClass
    public void tearDown()
    {
        DriverFactory.getInstance().tearDown();
    }
}