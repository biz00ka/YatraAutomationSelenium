// REVISED BaseTest.java

import org.factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.utils.PropReaderUtil;

import java.io.IOException;

public class BaseTest {

    protected static PropReaderUtil propReaderUtil;

    static {
        try {
            // Initialize utility here, before any thread starts
            propReaderUtil = new PropReaderUtil("test.properties");
        } catch (IOException e) {
            // If initialization fails, log it and terminate gracefully
            System.err.println("FATAL ERROR: Could not load test.properties from classpath.");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize PropReaderUtil due to file error.", e);
        }
    }

    public WebDriver getDriver() {
        return DriverFactory.getInstance().getDriver();
    }

    @BeforeClass
    public void setup() {
        DriverFactory.getInstance().getDriver();
    }

    @AfterClass
    public void tearDown()
    {
        DriverFactory.getInstance().tearDown();
    }
}