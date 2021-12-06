package com.tubai.study_html.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatUtils {
    /**
     * 返回null说明格式化失败
     * @param format "yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String getFormatDate(String format, Date date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String format1 = sdf.format(date);
            return format1;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 返回北京时间的当前时间
     * @param format
     * @return
     */
    public static String getNowTime(String format){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(timeZone);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowTime = sdf.format(now);
        return nowTime;
    }

    public static Date getNowDate(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(timeZone);
        return new Date();
    }

    /**
     * 返回北京时间的当前时间
     * 格式为yyyy-MM-dd
     * @return
     */
    public static String getNowTime(){
        return getNowTime("yyyy-MM-dd");
    }
}
