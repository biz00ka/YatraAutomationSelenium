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
    public void setup() throws IOException {
        driver=DriverFactory.getInstance().getDriver();

        propReaderUtil=new PropReaderUtil("test.properties");

    }

    @AfterClass
    public void tearDown()
    {
        DriverFactory.getInstance().tearDown();
    }
}
