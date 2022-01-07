package com.example.myapplication;


import android.app.AlarmManager;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @ProjectName : DateUtil
 * @Author : muyf
 * @Time : 2021/9/6 14:24
 * @Description : 日期工具类
 */
public class DateUtil {

    //获取年月日时分秒
    public static String getAll(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取年月日时分
    public static String getAll2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取年月日
    public static String getYMD(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取年月日
    public static String getMYD(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取月日
    public static String getMD(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取年月日
    public static String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-时分秒
    public static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-时分
    public static String getHourMinute(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-时分
    public static String getHourMinuteAmPm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-时
    public static String getHour(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取  分秒
    public static String getMinuteSeconds(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-分
    public static String getMinute(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String date = sdf.format(new Date(time));
        return date;
    }

    //获取时间-秒
    public static String getSecond(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        String date = sdf.format(new Date(time));
        return date;
    }

    //日期转换为毫秒值
    public static long date2Millseconds(String date) {
        long millseconds = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            millseconds = df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millseconds;
    }

    //日期转换为毫秒值
    public static long date2MillsecondsFromLocal(String date) {
        long millseconds = 0;
        DateFormat df = new SimpleDateFormat("MMM, dd yyyy", Locale.getDefault());
        try {
            millseconds = df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millseconds;
    }

    //时间转换为毫秒值
    public static long time2Millseconds(String date) {
        long millseconds = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            millseconds = df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millseconds;
    }

    //时间转换为毫秒值
    public static long time2Millseconds2(String date) {
        long millseconds = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            millseconds = df.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millseconds;
    }

    /**
     * 根据日期查询星期几
     *
     * @param date 需要查询的日期，格式为2019-08-28
     * @return
     */
    public static String getWeekFromDate(String date) {
        String week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "日";
        }
        if (Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "一";
        }
        if (Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "二";
        }
        if (Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "三";
        }
        if (Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "四";
        }
        if (Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "五";
        }
        if (Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)) {
            week += "六";
        }
        return week;
    }

    /**
     * @return
     */
    public static String getAmPm() {
        Date d = new Date();
        return new SimpleDateFormat("a", Locale.getDefault()).format(d);
    }

    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String getCurrentIDTimeZone(String id) {
        TimeZone tz = TimeZone.getTimeZone(id);
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }


    public static String[] getAllTimeZoneIDS() {
        String[] ids = TimeZone.getAvailableIDs();
        getTimeZoneIDS();
        return ids;
    }

    public static String[] getTimeZoneIDS() {
        String[] ids = TimeZone.getAvailableIDs();
        String[] names = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {

            String gmt = getCurrentIDTimeZone(ids[i]);
            TimeZone d = TimeZone.getTimeZone(ids[i]);

            names[i] = d.getDisplayName();

            LogUtil.v("gmt zone." + gmt);
            LogUtil.v("time zone." + d.getDisplayName());


            LogUtil.v("Availalbe ids.................." + ids[i]);


            LogUtil.v("savings." + d.getDSTSavings());
            LogUtil.v("offset." + d.getRawOffset());

            if (!ids[i].matches(".*/.*")) {
                continue;
            }

            String region = ids[i].replaceAll(".*/", "").replaceAll("_", " ");
            int hours = Math.abs(d.getRawOffset()) / 3600000;
            int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
            String sign = d.getRawOffset() >= 0 ? "+" : "-";

            String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);

        }

        return names;
    }

    public static void setSysDate(Context context, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        long when = c.getTimeInMillis();

        if (when / 1000 < Long.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }


}




