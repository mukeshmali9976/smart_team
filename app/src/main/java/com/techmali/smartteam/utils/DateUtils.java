package com.techmali.smartteam.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {


    public static final String DEFAULT_DATE_FORMAT_SHORT = "dd-MM-yyyy";

    // source date format which api will provide
    public static final String API_DATE_FORMAT_FULL = "dd MMM ''yy hh:mm a";
    public static final String API_DATE_FORMAT_SHORT = "dd MMM ''yy";
    public static final String API_TIME_FORMAT_SHORT = "hh:mm a";
    // display date format which is displayed in app
    public static final String API_DISPLAY_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String API_DISPLAY_FORMAT_SHORT = "yyyy-MM-dd";

    public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat dbFormat = new SimpleDateFormat(DB_DATE_FORMAT, Locale.ENGLISH);

    public static final SimpleDateFormat defaultDateFormatShort = new SimpleDateFormat(DEFAULT_DATE_FORMAT_SHORT, Locale.ENGLISH);

    public static final SimpleDateFormat sourceFormatFull = new SimpleDateFormat(API_DATE_FORMAT_FULL, Locale.ENGLISH);
    public static final SimpleDateFormat sourceFormatShort = new SimpleDateFormat(API_DATE_FORMAT_SHORT, Locale.ENGLISH);
    public static final SimpleDateFormat displayDateFormatFull = new SimpleDateFormat(API_DISPLAY_FORMAT_FULL, Locale.ENGLISH);
    public static final SimpleDateFormat displayDateFormatShort = new SimpleDateFormat(API_DISPLAY_FORMAT_SHORT, Locale.ENGLISH);
    public static final SimpleDateFormat timeFormatShort = new SimpleDateFormat(API_TIME_FORMAT_SHORT, Locale.ENGLISH);

    public static String getDisplayDateFromServerDate(String dateAsString, boolean isUTC) {

        try {
            if (!Utils.isEmptyString(dateAsString)) {
                displayDateFormatFull.setTimeZone(TimeZone.getDefault());
                if (isUTC)
                    sourceFormatFull.setTimeZone(TimeZone.getTimeZone("UTC"));

                return displayDateFormatFull.format(sourceFormatFull.parse(dateAsString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets local date from utc.
     *
     * @param dateAsString       the date as string
     * @param requiredDateFormat the required date format
     * @return the local date from utc
     */
    public static String getLocalDateFromUTC(String dateAsString, String requiredDateFormat) {

        try {
            if (!Utils.isEmptyString(dateAsString)) {
                if (Utils.isEmptyString(requiredDateFormat)) {
                    requiredDateFormat = "MM/dd/yyy hh:mm a";
                }
                SimpleDateFormat destFormat = new SimpleDateFormat(requiredDateFormat, Locale.getDefault());
                destFormat.setTimeZone(TimeZone.getDefault());
                dbFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                return destFormat.format(dbFormat.parse(dateAsString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(String dateStr) {
        String dateString = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat startDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = format.parse(dateStr);
            System.out.println(date);
            dateString = startDateFormat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateString;

    }

    public static String convertCurrentToUTC(Date date, String requiredFormat) {
        SimpleDateFormat dateFormat;
        if (!Utils.isEmptyString(requiredFormat))
            dateFormat = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        else
            dateFormat = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String currentUTCDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date = new Date();
        return dateFormat.format(date.getTime());
    }
}
