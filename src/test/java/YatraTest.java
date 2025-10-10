import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.utils.DateUtils;
import java.text.NumberFormat;
import java.util.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
public class YatraTest extends BaseTest{

    WebDriverWait wait;
    @Test(groups = "smoke")
    public void selectMinPriceDate() {
        getDriver().get(propReaderUtil.getPropValue("yatraurl"));

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        By crossImg = By.xpath("//img[@alt='cross']");
        wait.until(ExpectedConditions.elementToBeClickable(crossImg)).click();

        By departureDateBox = By.xpath("//span[contains(text(),'Departure Date')]/../..");
        wait.until(ExpectedConditions.elementToBeClickable(departureDateBox)).click();
        String currMonth= DateUtils.getCurrentMonth("yyyy-MM");

        By calendarElement = By.xpath("//div[@aria-label='month  " + currMonth + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(calendarElement));


        String nextMonth=DateUtils.getNextMonth("yyyy-MM");

        selectMinFareForMonth(currMonth);


        By returnDateBox = By.xpath("//span[contains(text(),'Return Date')]/../..");
        wait.until(ExpectedConditions.elementToBeClickable(returnDateBox)).click();

        By nextMonthCalendar = By.xpath("//div[@aria-label='month  " + nextMonth + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(nextMonthCalendar));

        selectMinFareForMonth(nextMonth);
        System.out.println("Yatra Test completed.");
    }

    public void selectMinFareForMonth(String month)
    {
        String path="//div[@aria-label='month  "+month+"']//div[@aria-disabled='false' and not(contains(@class,'outside-month'))]//span[contains(text(),'₹')]";

       // System.out.println("Path is "+path);

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(path)));

        List<WebElement> fareLists=
                getDriver().findElements(By.xpath(path));

       // System.out.println("Fares list size is "+fareLists.size());

        List<Integer> formattedFares=fareLists.stream().map(e-> Integer.valueOf(e.getText().replaceAll("\\D",""))).toList();

       // System.out.println("Formatted fare size is "+formattedFares.size());

        int minFare=formattedFares.stream().min(Integer::compare).get();


        Locale locale= new Locale("en","IN");

        NumberFormat numberFormat= NumberFormat.getCurrencyInstance(locale);

        numberFormat.setMaximumFractionDigits(0);

        String minimumFare=numberFormat.format(minFare);

       // System.out.println("Minimum Formatted fares is "+minimumFare);

        List<WebElement> minimumFareList=getDriver().findElements(By.xpath("//span[contains(text(),'"+minimumFare+"')]"));

        minimumFareList.get(new Random().nextInt(minimumFareList.size())).click();



    }
}
