package com.pingjin.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author pingjin create 2018年4月11日
 *
 */
public class TimeUtil {
	
	public static final String DATE_FORMAT_EXPR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_TIME_EXPR = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_MILLI_TIME_EXPR = "yyyyMMddHHmmssSSS";
    
    public TimeUtil() {
    	
    }
    
    /**
     * 获取当前时间字符串 yyyy-MM-dd HH:mm:ss格式
     * @return
     */
    public static String getCurrentTime() {
        return getTimeStr(new Date());
    }

    /**
     * 获取当前时间字符串 yyyyMMddHHmmss格式
     * @return
     */
    public static String getDateCurrentTime() {
        return getTimeStr(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获取当前时间 
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    public static String getTimeStr(Date date) {
        if(date == null)
            return null;
        else
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    /**
     * 根据时间戳返回yyyy-MM-dd HH:mm:ss 格式的字符串
     * @param time
     * @return
     */
    public static String getTimeStr(long time) {
        return getTimeStr(new Date(time));
    }

    /**
     * 返回时间字符串
     * @param fromStr
     * @param formatExpr 最后需要的时间格式
     * @return
     */
    public static String getTimeStrFromStr(String fromStr, String formatExpr) {
        Date date = getDate(fromStr, formatExpr);
        return getTimeStr(date, formatExpr);
    }

    /**
     * 返回时间字符串
     * @param fromStr
     * @param fromFormatExpr
     * @param toFormatExpr
     * @return
     */
    public static String getTimeStrFromStr(String fromStr, String fromFormatExpr, String toFormatExpr) {
        Date date = getDate(fromStr, fromFormatExpr);
        return getTimeStr(date, toFormatExpr);
    }

    /**
     * 以指定格式返回时间字符串
     * @param date
     * @param formatExpr
     * @return
     */
    public static String getTimeStr(Date date, String formatExpr) {
        if(date == null)
            return null;
        else
            return (new SimpleDateFormat(formatExpr)).format(date);
    }

    /**
     * 根据指定格式 返回当前时间字符串
     * @param formatExpr
     * @return
     */
    public static String getCurrentTimeStr(String formatExpr) {
        return getTimeStr(new Date(), formatExpr);
    }

    /**
     * 返回当前时间的字符串 yyyyMMddHHmmssSSS格式
     * @return
     */
    public static String getTimeStamp() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return formater.format(new Date());
    }

    /**
     * 根据时间字符串返回时间戳
     * @param timeStr
     * @return
     */
    public static long getTime(String timeStr) {
        if(timeStr == null)
            return -1L;
        timeStr = timeStr.trim();
        long milliseconds = -1L;
        try
        {
            Date date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(timeStr);
            milliseconds = date.getTime();
        }
        catch(Exception exception) { }
        return milliseconds;
    }

    /**
     * 比较两个时间大小
     * @param time
     * @param compareTime
     * @return
     */
    public static long compare(Date time, Date compareTime) {
        return time.getTime() - compareTime.getTime();
    }

    public static Date toDate(Date date) {
        if(date instanceof java.sql.Date)
            return new Date(date.getTime());
        else
            return date;
    }

    /**
     * 根据时间字符串 比较时间大小 
     * @param timeStr
     * @param compareTimeStr
     * @return
     */
    public static long compare(String timeStr, String compareTimeStr) {
        long timeStrLong = getTime(timeStr);
        long compareTimeStrLong = getTime(compareTimeStr);
        return timeStrLong - compareTimeStrLong;
    }

    /**
     * 根据指定格式 时间字符串 返回日期
     * @param timeStr
     * @param formatExpr
     * @return
     */
    public static Date getDate(String timeStr, String formatExpr) {
        if(timeStr == null)
            return null;
        timeStr = timeStr.trim();
        Date date = null;
        try
        {
            date = (new SimpleDateFormat(formatExpr)).parse(timeStr);
        }
        catch(Exception exception) { }
        return date;
    }

    public static Date getDefaultDate(String timeStr) {
        return getDate(timeStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String timeStr) {
        return getDefaultDate(timeStr);
    }

    public static long getTime() {
        return getTime(getCurrentTime());
    }

    public static Date addYear(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(1, n);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, n);
        return cal.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(7, n);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(12, n);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(13, n);
        return cal.getTime();
    }
}
