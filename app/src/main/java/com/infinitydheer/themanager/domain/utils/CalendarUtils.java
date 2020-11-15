package com.infinitydheer.themanager.domain.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class which encapsulates all the calendar utility methods used in the Application
 */
public final class CalendarUtils {
    private final static String DATE_FORMAT="dd-MM-yyyy";
    private final static String TIME_FORMAT="hh:mma";

    private final static String[] READABLE_MONTHS ={"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
            "OCTOBER", "NOVEMBER", "DECEMBER"};

    private CalendarUtils(){}

    /**
     * Converts a {@link Calendar} to a date format that is convenient for a person
     * @param calendar Calendar to be converted to a human-readable format
     * @return The converted date
     */
    public static String convertToReadableDate(Calendar calendar){
        if(calendar==null) return "";

        Calendar c=Calendar.getInstance();
        if(isSameDay(calendar,c)) return "TODAY";

        c.add(Calendar.HOUR,-24);
        if(isSameDay(calendar,c)) return "YESTERDAY";

        String res="";
        res+=calendar.get(Calendar.DATE);
        res+=" ";
        res+= READABLE_MONTHS[calendar.get(Calendar.MONTH)];
        res+=", ";
        res+=calendar.get(Calendar.YEAR);
        return res;
    }

    public static String convertToReadableDate(String c){
        return convertToReadableDate(convertToCalendar(c,false));
    }

    /**
     * Converts a {@link Calendar} to the corresponding {@link String} representation
     * @param calendar Calendar to be converted
     * @param both Flag to indicate whether both time and date should be included in the
     *             String representation
     * @return The equivalent String representation
     */
    public static String convertToString(Calendar calendar, boolean both){
        if(calendar==null) return "";

        Date date=calendar.getTime();
        String format=DATE_FORMAT;
        if(both) format=format+" "+TIME_FORMAT;

        DateFormat dateFormat=new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * Converts a {@link String} representation to the corresponding {@link Calendar}
     * @param date String to be converted
     * @param both Flag to indicate whether both time and date are included in the
     *             String representation. Passing the wrong input here will cause
     *             a problematic output
     * @return The equivalent Calendar representation
     */
    public static Calendar convertToCalendar(String date, boolean both){
        if(date.length()==0) return null;

        String format=DATE_FORMAT;
        if(both) format= format + " " + TIME_FORMAT;
        DateFormat dateFormat=new SimpleDateFormat(format);

        Date dateDate=null;
        try {
            dateDate=dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar res=Calendar.getInstance();
        if(dateDate==null) return null;
        res.setTime(dateDate);

        return res;
    }

    /**
     * Determines whether the {@link Calendar}s are on the same day or not
     * @param c1 First Calendar
     * @param c2 Second Calendar
     * @return True if the Calendars represent the same day, false otherwise
     */
    public static boolean isSameDay(Calendar c1, Calendar c2) {
        if(c1==null||c2==null) return false;

        return c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR) &&
                c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH) &&
                c2.get(Calendar.DATE) == c1.get(Calendar.DATE);
    }

    public static boolean isSameDay(String c1, String c2){
        return isSameDay(convertToCalendar(c1,false),convertToCalendar(c2,false));
    }
}
