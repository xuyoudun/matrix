package org.iteration.matrix.core.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static ZoneId ZONEID_DEFAULT_SHANGHAI = ZoneId.of("Asia/Shanghai");

    private static final SimpleDateFormat SimpleDateFormatDefault = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static Date now4Zone(String zoneId) {
        return now4Zone(ZoneId.of(zoneId));
    }

    public static Date now4Zone(ZoneId zoneId) {
        Date dateToConvert = new Date();
        LocalDateTime dateTime = LocalDateTime.ofInstant(dateToConvert.toInstant(), zoneId);
        Date out = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        return out;
    }

    public static Date now() {
        return now4Zone(ZONEID_DEFAULT_SHANGHAI);
    }

    public static String date2FormattedString(Date date) {
        String dateString = null;
        if (date != null) {
            dateString = SimpleDateFormatDefault.format(date);
        }
        return dateString;
    }
}
