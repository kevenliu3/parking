package com.hibox.carclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Everyday is another day, keep going.
 * author:  Ramo
 * email:   327300401@qq.com
 * date:    2017/5/11 11:33
 * desc:    用于时间的封装方法
 */

public class CalendarTool {
    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_MONTH = "MM-dd HH:mm";
    public static final String FORMAT_DAY = "HH:mm";

    /**
     * 获取多样化的时间展示
     *
     * @param time 毫秒数
     * @return 字符串格式时间
     */
    public static String getTimeString(long time) {
        String timeString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format;
        //判断是不是同年
        if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            format = new SimpleDateFormat(FORMAT_FULL, Locale.getDefault());
            timeString = format.format(calendar.getTime());
            //判断是不是同一周
            if (now.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR)) {
                //判断是不是同一天
                if (now.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 0) {
                    format = new SimpleDateFormat(FORMAT_DAY, Locale.getDefault());
                    //判断是不是上午
                    if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                        timeString = "早上 " + format.format(calendar.getTime());
                    }
                    //判断是不是下午
                    else if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
                        timeString = "下午 " + format.format(calendar.getTime());
                    }
                    //判断是不是晚上
                    else {
                        timeString = "晚上 " + format.format(calendar.getTime());
                    }
                }
                //如果多一天，那就是昨天
                else if (now.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    timeString = "昨天";
                }
                //如果时间间隔大于1天，判断是周几，因为今天最多是周六，所以只用判断到周四
                else {
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (day) {
                        case Calendar.MONDAY:
                            timeString = "周一";
                            break;
                        case Calendar.TUESDAY:
                            timeString = "周二";
                            break;
                        case Calendar.WEDNESDAY:
                            timeString = "周三";
                            break;
                        case Calendar.THURSDAY:
                            timeString = "周四";
                            break;
                        case Calendar.FRIDAY:
                            break;
                        case Calendar.SATURDAY:
                            break;
                        case Calendar.SUNDAY:
                            break;
                    }
                }
            }
            //如果不是同一个周
            else {
                //判断是不是昨天
                if (now.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    timeString = "昨天";
                }
                //不是昨天，直接显示日期
                else {
                    format = new SimpleDateFormat(FORMAT_MONTH, Locale.getDefault());
                    timeString = format.format(calendar.getTime());
                }
            }
        }
        //不是同年，显示完整日期
        else {
            format = new SimpleDateFormat(FORMAT_FULL, Locale.getDefault());
            timeString = format.format(calendar.getTime());
        }
        return timeString;
    }

    /**
     * 获取指定格式的时间字符串
     *
     * @param time         毫秒数
     * @param formatString 格式
     * @return 字符串格式时间
     */
    public static String getFormatTime(long time, String formatString) {

        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        return format.format(new Date(time));

    }

    /**
     * 获取字符串格式时间的毫秒数
     *
     * @param timeString   时间字符串
     * @param formatString 格式
     * @return 毫秒数
     */
    public static long getTimeMills(String timeString, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString, Locale.getDefault());
        try {
            return format.parse(timeString).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getPastTimeString(long time) {
        String timeString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar now = Calendar.getInstance();
        long timeMillis = (now.getTimeInMillis() - time) / 1000;
        if (timeMillis < 60) {
            timeString = timeMillis + "秒前";
        } else if (timeMillis < 60 * 60) {
            timeString = (int) (timeMillis / 60.0f) + "分钟前";
        } else if (timeMillis < 60 * 60 * 24) {
            timeString = (int) (timeMillis / 60.0f / 60) + "小时前";
        } else if (timeMillis < 60 * 60 * 24 * 2) {
            timeString = "昨天";
        } else {
            timeString = (int) (timeMillis / 60.0f / 60 / 24) + "天前";
        }


        return timeString;
    }
}
