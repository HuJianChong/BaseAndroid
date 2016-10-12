package com.hjc.baselibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author k.yang
 * @version 创建时间：2015-11-19 下午4:45:35 类说明
 */
public class DatesUtils {

    public static final String APP_NAME_HOTODAY = "Hotoday";
    public static final String APP_NAME_MATA = "Mata";
    public static final String APP_NAME_NOTICIAS = "Noticias Águila";
    public static final String HINDI = "Hindi";
    public static final String ENGLISH = "English";
    public static final String INDONESIAN = "Indonesian";
    public static final String SPANISH = "Spanish";
    public static final String DEFAULT = "default";

    public static final long ONE_HOUR = 60 * 60;

    /**
     * 时间戳转为"MM月dd日"
     */
    public static String getDateString(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        String date = sdf.format(new Date(timeStamp * 1000));
        return date;
    }

    @SuppressWarnings("deprecation")
    public static boolean isToday(long timeStamp) {
        Log.d("DateUtils", "DateUtils,isToday,new Date().getDay()==" + new Date().getDay());
        Log.d("DateUtils", "DateUtils,isToday,Date(timeStamp * 1000).getDay()==" + new Date(timeStamp * 1000).getDay());

        return new Date(timeStamp * 1000).getDay() == new Date().getDay();
    }

    /**
     * 比较时间戳是不是同一天
     */
    public static boolean areSameDay(long timeStampA, long timeStampB) {
        Date dateA = new Date(timeStampA * 1000);
        Date dateB = new Date(timeStampB * 1000);
        return areSameDay(dateA, dateB);
    }


    /**
     * 比较时间戳是不是同一天
     */
    public static boolean areSameDayMill(long timeStampA, long timeStampB) {
        Date dateA = new Date(timeStampA);
        Date dateB = new Date(timeStampB);
        return areSameDay(dateA, dateB);
    }

    /**
     * 比较date是不是同一天
     */
    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * publis time exchange
     *
     * @param publishTime
     * @return
     */
    public static String getTimeSytle(Context mContext, String curLan, String publishTime) {
        if (TextUtils.isEmpty(publishTime)) {
            return "";

        }
        Calendar publishCalendar = Calendar.getInstance();
        Long time = 0L;
        try {
            time = Integer.valueOf(publishTime) * 1000L;
            publishCalendar.setTime(new Date(time));
        } catch (Exception e) {
            Log.e("DatesUtils", e.toString());
            return "";
        }
        StringBuilder sb = new StringBuilder();

        if (TextUtils.isEmpty(curLan)) {
            curLan = DEFAULT;
        }

        switch (curLan) {
            case HINDI:
                sb.append(new SimpleDateFormat("HH:mm").format(time)).append(",").append(publishCalendar.get(Calendar.DAY_OF_MONTH)).append(" ")
                        .append(getMonthIndia(publishCalendar.get(Calendar.MONTH) + 1))
                        .append(getYearsIndia(publishCalendar.get(Calendar.YEAR)));
                break;
            case ENGLISH:
                sb.append(getMonthDefault(publishCalendar.get(Calendar.MONTH) + 1)).append(" ")
                        .append(publishCalendar.get(Calendar.DAY_OF_MONTH)).append(",").append(getYears(publishCalendar.get(Calendar.YEAR))).append(" ")
                        .append(new SimpleDateFormat("HH:mm").format(time));
                break;
            case INDONESIAN:
                sb.append(publishCalendar.get(Calendar.DAY_OF_MONTH)).append(" ").append(getMonthMata(publishCalendar.get(Calendar.MONTH) + 1))
                        .append(",").append(getYears(publishCalendar.get(Calendar.YEAR))).append(" ")
                        .append(new SimpleDateFormat("HH:mm").format(time));
                break;
            case SPANISH:
                sb.append(getMonthES(publishCalendar.get(Calendar.MONTH) + 1)).append(" ")
                        .append(publishCalendar.get(Calendar.DAY_OF_MONTH)).append(",").append(getYears(publishCalendar.get(Calendar.YEAR))).append(" ")
                        .append(new SimpleDateFormat("HH:mm").format(time));
                break;
            default:
                sb.append(getMonthDefault(publishCalendar.get(Calendar.MONTH) + 1)).append(" ")
                        .append(publishCalendar.get(Calendar.DAY_OF_MONTH)).append(",").append(getYears(publishCalendar.get(Calendar.YEAR))).append(" ")
                        .append(new SimpleDateFormat("HH:mm").format(time));
                break;
        }
        return sb.toString();
    }

    //印地语
    public static String getMonthIndia(int month) {
        String str = "";
        switch (month) {
            case 1:
                str = "जनवरी";
                break;
            case 2:
                str = "फरवरी";
                break;
            case 3:
                str = "मार्च";
                break;
            case 4:
                str = "अप्रैल";
                break;
            case 5:
                str = "मई";
                break;
            case 6:
                str = "जून";
                break;
            case 7:
                str = "जुलाई";
                break;
            case 8:
                str = "अगस्त";
                break;
            case 9:
                str = "सितंबर";
                break;
            case 10:
                str = "अक्टूबर";
                break;
            case 11:
                str = "नवंबर";
                break;
            case 12:
                str = "दिसंबर";
                break;
            default:
                break;
        }
        return str;
    }

    //默认英语
    public static String getMonthDefault(int month) {
        String str = "";
        switch (month) {
            case 1:
                str = "Jan";
                break;
            case 2:
                str = "Feb";
                break;
            case 3:
                str = "Mar";
                break;
            case 4:
                str = "Apr";
                break;
            case 5:
                str = "May";
                break;
            case 6:
                str = "Jun";
                break;
            case 7:
                str = "Jul";
                break;
            case 8:
                str = "Aug";
                break;
            case 9:
                str = "Sept";
                break;
            case 10:
                str = "Oct";
                break;
            case 11:
                str = "Nov";
                break;
            case 12:
                str = "Dec";
                break;
            default:
                break;
        }
        return str;
    }

    //印尼语
    public static String getMonthMata(int month) {
        String str = "";
        switch (month) {
            case 1:
                str = "JAN";
                break;
            case 2:
                str = "FEB";
                break;
            case 3:
                str = "MAR";
                break;
            case 4:
                str = "APR";
                break;
            case 5:
                str = "MEI";
                break;
            case 6:
                str = "JUN";
                break;
            case 7:
                str = "JUL";
                break;
            case 8:
                str = "AGS";
                break;
            case 9:
                str = "SEP";
                break;
            case 10:
                str = "OKT";
                break;
            case 11:
                str = "NOV";
                break;
            case 12:
                str = "DES";
                break;
            default:
                break;
        }
        return str;
    }

    //印尼语
    public static String getMonthES(int month) {
        String str = "";
        switch (month) {
            case 1:
                str = "eno.";
                break;
            case 2:
                str = "fbro.";
                break;
            case 3:
                str = "mzo.";
                break;
            case 4:
                str = "ab.";
                break;
            case 5:
                str = "mayo";
                break;
            case 6:
                str = "jun.";
                break;
            case 7:
                str = "jul.";
                break;
            case 8:
                str = "agto.";
                break;
            case 9:
                str = "sbre.";
                break;
            case 10:
                str = "obre.";
                break;
            case 11:
                str = "nbre.";
                break;
            case 12:
                str = "dbre.";
                break;
            default:
                break;
        }
        return str;
    }

    public static String getYears(int year) {
        if (year != Calendar.getInstance().get(Calendar.YEAR)) {
            return year + ", ";
        }
        return "";
    }

    public static String getYearsIndia(int year) {
        if (year != Calendar.getInstance().get(Calendar.YEAR)) {
            return ", " + year;
        }
        return "";
    }
}
