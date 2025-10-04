import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.utils.DateUtils;

import java.util.List;
import java.util.Random;

public class BookingTest extends BaseTest{

    @Test(groups = "smoke")
    public void selectHotel() throws InterruptedException {
        getDriver().get(propReaderUtil.getPropValue("bookingurl"));
        Thread.sleep(3000);
        getDriver().findElement(By.xpath("//input[@name='ss']")).sendKeys("Mussoorie");
        Thread.sleep(3000);
        getDriver().findElement(By.xpath("//div[@id='autocomplete-results']//li[@id='autocomplete-result-0']")).click();

        String currMonth= DateUtils.getCurrentMonth("MMMM uuuu");

        //h3[contains(text(),'October')]/..//span[@aria-pressed='false' and not(contains(@aria-disabled,'true'))]
        selectDates(currMonth);

        String nextMonth= DateUtils.getNextMonth("MMMM uuuu");
        selectDates(nextMonth);

        getDriver().findElement(By.xpath("//span[contains(text(),'Search')]")).click();
        Thread.sleep(2000);

        Assert.assertTrue(getDriver().findElement(By.xpath("//h1[contains(text(),'properties found')]")).isDisplayed(),"Assertion failed for property search.");

        WebElement desiredElement=getDriver().findElement(By.xpath("//div[contains(text(),'WelcomHeritage Kasmanda Palace')]"));

        JavascriptExecutor js= (JavascriptExecutor) getDriver();

        js.executeScript("arguments[0].scrollIntoView(true)",desiredElement);

        desiredElement.click();
        Thread.sleep(2000);

    }

    public void selectDates(String month)
    {
        String xpath="//h3[contains(text(),'"+month+"')]/..//span[@aria-pressed='false' and not(contains(@aria-disabled,'true'))]";
        List<WebElement> availableDates=getDriver().findElements(By.xpath(xpath));
        availableDates.get(new Random().nextInt(availableDates.size())).click();
    }
}
