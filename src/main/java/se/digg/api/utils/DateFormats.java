package se.digg.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormats {

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";

    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String STANDARD_TIME_FORMAT = "HH:mm:ss";

    public static String getDateTimeString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
