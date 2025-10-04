import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.utils.DateUtils;
import java.text.NumberFormat;
import java.util.*;

public class YatraTest extends BaseTest{





    @Test(groups = "smoke")
    public void selectMinPriceDate() throws InterruptedException {
        getDriver().get(propReaderUtil.getPropValue("yatraurl"));
        getDriver().findElement(By.xpath("//img[@alt='cross']")).click();

        getDriver().findElement(By.xpath("//span[contains(text(),'Departure Date')]/../..")).click();
        Thread.sleep(2000);
        String currMonth= DateUtils.getCurrentMonth("yyyy-MM");

        String nextMonth=DateUtils.getNextMonth("yyyy-MM");

        selectMinFareForMonth(currMonth);
        Thread.sleep(2000);

        getDriver().findElement(By.xpath("//span[contains(text(),'Return Date')]/../..")).click();
        Thread.sleep(2000);
        selectMinFareForMonth(nextMonth);

    }

    public void selectMinFareForMonth(String month)
    {
        String path="//div[@aria-label='month  "+month+"']//div[@aria-disabled='false' and not(contains(@class,'outside-month'))]//span[contains(text(),'₹')]";

        List<WebElement> fareLists=
                getDriver().findElements(By.xpath(path));

        List<Integer> formattedFares=fareLists.stream().map(e-> Integer.valueOf(e.getText().replaceAll("\\D",""))).toList();

        int minFare=formattedFares.stream().min(Integer::compare).get();


        Locale locale= new Locale("en","IN");

        NumberFormat numberFormat= NumberFormat.getCurrencyInstance(locale);

        numberFormat.setMaximumFractionDigits(0);

        String minimumFare=numberFormat.format(minFare);

        System.out.println("Minimum Formatted fares is "+minimumFare);

        List<WebElement> minimumFareList=getDriver().findElements(By.xpath("//span[contains(text(),'"+minimumFare+"')]"));

        minimumFareList.get(new Random().nextInt(minimumFareList.size())).click();



    }
}
