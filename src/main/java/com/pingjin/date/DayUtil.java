/**
 * 
 */
package com.pingjin.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * @author pingjin create 2018年4月11日
 *
 */
public class DayUtil {

    /**
     * 获取月的最后一天
     * 
     * @param date
     * @return
     */
    public static String getLastDayOfMonth(Date date) {  
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
        
        Calendar cal = Calendar.getInstance(); 
        cal.clear();
        cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));     
        cal.set(Calendar.MONTH, cal1.get(Calendar.MONTH));     
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }   
    
    /**
     * 获取某星期最后一天
     * 
     * @param date
     * @return
     */
    public static String getLastDayOfWeek(Date date) {
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
                
        int dayOfWeek = cal1.get(Calendar.DAY_OF_WEEK);
        
        Calendar cal2 = Calendar.getInstance();  
        cal2.setTime(date);
        cal2.add(Calendar.DAY_OF_WEEK,-(dayOfWeek - 1));  
        
        Calendar cal = Calendar.getInstance();  
        cal.setTime(cal2.getTime());
        cal.add(Calendar.DAY_OF_WEEK,6);  
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }
    
    /**
     * 获取某年的最后一天
     * 
     * @param date
     * @return
     */
    public static String getLastDayOfYear(Date date) {  
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
        
        Calendar cal = Calendar.getInstance();  
        cal.clear();
        cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));    
        cal.roll(Calendar.DAY_OF_YEAR, -1);  
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }   
        
    /**
     * 获取某星期第一天
     * 
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(Date date) { 
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
                
        int dayOfWeek = cal1.get(Calendar.DAY_OF_WEEK);
        
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK,-(dayOfWeek - 1));  
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }
    
    /**
     * 获取月的第一天
     * 
     * @param date
     * @return
     */
    public static String getFirstDayOfMonth(Date date) { 
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
        
        Calendar cal = Calendar.getInstance();  
        cal.clear();
        cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));     
        cal.set(Calendar.MONTH, cal1.get(Calendar.MONTH));  
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }
    
    /**
     * 获取某年的第一天
     * 
     * @param date
     * @return
     */
    public static String getFirstDayOfYear(Date date) { 
        Calendar cal1 = Calendar.getInstance();   
        cal1.setTime(date);
        
        Calendar cal = Calendar.getInstance();  
        cal.clear();
        cal.set(Calendar.YEAR, cal1.get(Calendar.YEAR));   
       return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());  
    }
}
