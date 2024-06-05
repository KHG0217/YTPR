package com.khg.work;

import java.util.Calendar;
import java.util.List;

import com.tapacross.sns.util.DateUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
		String from = DateUtil.getDateString(-1, "");
		String to = DateUtil.getDateString(1, "");
        List<String> days = DateUtil.getDayTimeArrange(from, to, Calendar.HOUR_OF_DAY);
        System.out.println(days);
    }
}
