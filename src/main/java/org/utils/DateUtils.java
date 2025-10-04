package org.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String getCurrentMonth(String pattern)
    {
        LocalDate localDate=LocalDate.now();
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern(pattern);
        String currMonth=localDate.format(dateTimeFormatter);
        System.out.println("Current month is "+currMonth);
        return currMonth;
    }

    public static String getNextMonth(String pattern)
    {
        LocalDate localDate=LocalDate.now();
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern(pattern);
        LocalDate nextMonthData=localDate.plusMonths(1);
        String nextMonth= nextMonthData.format(dateTimeFormatter);
        System.out.println("Next month is "+nextMonth);
        return nextMonth;
    }
}
