package demo.pluto.maven.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;



public class DateUtil {

    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 返回时间戳字符串
     * 
     * @param timestamp
     * @param pattern 返回的时间戳格式，默认为（"yyyy-MM-dd HH:mm:ss"）
     * @return
     */
    public static String timestamp2String(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            throw new java.lang.IllegalArgumentException("timestamp null illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp.getTime()));
    }

    /**
     * 日期转字符串。
     * 
     * @param date
     * @param pattern 返回的日期格式，默认为（"yyyy-MM-dd"）
     * @return
     */
    public static String date2String(Date date, String pattern) {
        if (date == null) {
            throw new java.lang.IllegalArgumentException("timestamp null illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_DATE;;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取当前时间戳对象
     * 
     * @return
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 获取当前时间戳格式字符串
     * 
     * @param pattern 时间戳格式，如果为空或null，则为"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String currentTimestamp2String(String pattern) {
        return timestamp2String(currentTimestamp(), pattern);
    }

    /**
     * 将时间戳字符串转为时间戳对象
     * 
     * @param strDateTime
     * @param pattern 字符串的格式，默认为 ("yyyy-MM-dd HH:mm:ss")
     * @return
     */
    public static Timestamp string2Timestamp(String strDateTime, String pattern) {
        if (strDateTime == null || strDateTime.equals("")) {
            throw new java.lang.IllegalArgumentException("Date Time Null Illegal");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = PATTERN_STANDARD;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Timestamp(date.getTime());
    }

    /**
     * 将日期字符串转为日期对象
     * 
     * @param strDate
     * @param pattern 字符串的格式，默认为 ("yyyy-MM-dd")
     * @return
     */
    public static Date string2Date(String strDate, String pattern) {
        if (strDate == null || strDate.equals("")) {
            throw new RuntimeException("str date null");
        }
        if (pattern == null || pattern.equals("")) {
            pattern = DateUtil.PATTERN_DATE;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 返回日期字符串中的年。
     * 
     * @param strDest
     * @return
     */
    public static String stringToYear(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new java.lang.IllegalArgumentException("str dest null");
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return String.valueOf(c.get(Calendar.YEAR));
    }

    /**
     * 返回日期字符串中的两位数的月，例如2016-1-23 返回01。
     * 
     * @param strDest
     * @return
     */
    public static String stringToMonth(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new java.lang.IllegalArgumentException("str dest null");
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // return String.valueOf(c.get(Calendar.MONTH));
        int month = c.get(Calendar.MONTH);
        month = month + 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    /**
     * 返回日期字符串中的两位数的日，例如2016-10-1 返回01。
     * 
     * @param strDest
     * @return
     */
    public static String stringToDay(String strDest) {
        if (strDest == null || strDest.equals("")) {
            throw new java.lang.IllegalArgumentException("str dest null");
        }

        Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0" + day;
        }
        return "" + day;
    }

    /**
     * 获取指定日期的当月第一天的日期
     * 
     * @param c
     * @return
     */
    public static Date getFirstDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day, 0, 0, 0);
        return c.getTime();
    }

    /**
     * 获取指定日期的当月第一天的日期
     * 
     * @param c
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取指定日期的当月最后一天的日期
     * 
     * @param c
     * @return
     */
    public static Date getLastDayOfMonth(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = 1;
        if (month > 11) {
            month = 0;
            year = year + 1;
        }
        c.set(year, month, day - 1, 0, 0, 0);
        return c.getTime();
    }


    /**
     * 获取当前日期的当月最后一天的日期
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        return c.getTime();
    }

    /**
     * 返回日期的格林尼治时间字符串。(E.g: 2010-12-31T16:00:00.000Z)
     * 
     * @param date
     * @return
     */
    public static String date2GregorianCalendarString(Date date) {
        if (date == null) {
            throw new java.lang.IllegalArgumentException("Date is null");
        }
        long tmp = date.getTime();
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTimeInMillis(tmp);
        try {
            XMLGregorianCalendar t_XMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(ca);
            return t_XMLGregorianCalendar.normalize().toString();
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new java.lang.IllegalArgumentException("Date is null");
        }

    }

    /**
     * 比较两个日期是否相等
     * 
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean compareDate(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            throw new java.lang.RuntimeException();
        }

        String strFirstDate = date2String(firstDate, "yyyy-MM-dd");
        String strSecondDate = date2String(secondDate, "yyyy-MM-dd");
        if (strFirstDate.equals(strSecondDate)) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定日期额当天的起始时间
     * 
     * @param currentDate
     * @return
     */
    public static Date getStartTimeOfDate(Date currentDate) {
        String strDateTime = date2String(currentDate, "yyyy-MM-dd") + " 00:00:00";
        return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 获取指定日期的当天的结束时间
     * 
     * @param currentDate
     * @return
     */
    public static Date getEndTimeOfDate(Date currentDate) {
        String strDateTime = date2String(currentDate, "yyyy-MM-dd") + " 59:59:59";
        return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
    }

    /**
     * 根据起始日期，获取和当前日期差day的所有日期列表。
     * 
     * @param dateStr ("yyyy-MM-dd HH:mm:ss")
     * @param day
     * @return
     */
    public static List<Date> getDateList(String dateStr, int day) {
        List<Date> dateList = new ArrayList<Date>();
        Date date = DateUtil.string2Timestamp(dateStr, null);
        Calendar c1 = Calendar.getInstance();
        for (int i = 0; i < day; i++) {
            c1.setTime(date);
            c1.add(Calendar.DAY_OF_YEAR, i);
            dateList.add(c1.getTime());
        }
        return dateList;
    }


    public static void main(String[] args) {
        String str1 = "2011-01-01";
        String str2 = "1988-09-09";
        Date date1 = DateUtil.string2Date(str1, "yyyy-MM-dd");
        Date date2 = DateUtil.string2Date(str2, "yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        c2.add(Calendar.YEAR, 4);
        if (c2.before(c1)) {
            System.out.println("illegal");
        } else {
            System.out.println("ok");
        }

        // 测试格林尼治日期输出
        System.out.println("=====================================");
        System.out.println(date2GregorianCalendarString(c1.getTime()));

        // 测试获取日期列表
        System.out.println("=====================================");
        List<Date> dateList = getDateList("2016-10-27 11:07:00", 10);
        for (Date date : dateList) {
            System.out.println(DateUtil.date2String(date, null));
        }

        // 测试获取当月最后一天
        System.out.println("=====================================");
        Date date3 = DateUtil.string2Date("2016-12-17", "yyyy-MM-dd");
        Calendar c3 = Calendar.getInstance();
        c3.setTime(date3);
        System.out.println(DateUtil.date2String(getLastDayOfMonth(c3), null));
        System.out.println(DateUtil.date2String(getLastDayOfMonth(date3), null));

        // 测试获取天数
        System.out.println("=====================================");
        System.out.println(DateUtil.stringToDay("2016-12-1"));
        System.out.println(DateUtil.stringToDay("2016-12-11"));



    }



}
