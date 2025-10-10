import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.utils.DateUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import java.util.List;
import java.util.Random;

public class BookingTest extends BaseTest{

    WebDriverWait wait;
    @Test(groups = "smoke")
    public void selectHotel() { // Removed 'throws InterruptedException' as sleep is gone
        getDriver().get(propReaderUtil.getPropValue("bookingurl"));

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15)); // 15-second timeout

        By destinationInput = By.xpath("//input[@name='ss']");

        wait.until(ExpectedConditions.elementToBeClickable(destinationInput));

       // getDriver().findElement(destinationInput).sendKeys("Mussoorie");
        typeSlowly(getDriver().findElement(destinationInput),"Mussoorie",500);

        By autocompleteResult = By.xpath("//div[@id='autocomplete-results']//li[@id='autocomplete-result-0']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(autocompleteResult));

        getDriver().findElement(autocompleteResult).click();

        String currMonth= DateUtils.getCurrentMonth("MMMM uuuu");

        selectDates(currMonth);

        String nextMonth= DateUtils.getNextMonth("MMMM uuuu");
        selectDates(nextMonth);

        getDriver().findElement(By.xpath("//span[contains(text(),'Search')]")).click();

        By resultsHeader = By.xpath("//h1[contains(text(),'properties found')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultsHeader));


        Assert.assertTrue(getDriver().findElement(resultsHeader).isDisplayed(),"Assertion failed for property search.");

        WebElement desiredElement=getDriver().findElement(By.xpath("//div[contains(text(),'WelcomHeritage Kasmanda Palace')]"));

        JavascriptExecutor js= (JavascriptExecutor) getDriver();

        js.executeScript("arguments[0].scrollIntoView(true)",desiredElement);

        desiredElement.click();

        System.out.println("Booking Test completed.");

    }

    public void selectDates(String month)
    {
        String xpath="//h3[contains(text(),'"+month+"')]/..//span[@aria-pressed='false' and not(contains(@aria-disabled,'true'))]";
        List<WebElement> availableDates=getDriver().findElements(By.xpath(xpath));
        availableDates.get(new Random().nextInt(availableDates.size())).click();
    }
}
