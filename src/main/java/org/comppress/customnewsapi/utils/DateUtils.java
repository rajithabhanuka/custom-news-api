package org.comppress.customnewsapi.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDateTime stringToLocalDateTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = null;
        if (time != null){
            dateTime = LocalDateTime.parse(time, formatter);
        }
        return dateTime;
    }

}
