package com.spring2go.easyevent.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@Slf4j
public class DateUtil {
    // https://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format-with-date-hour-and-minute
    public static String formatDateInISOString(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    // https://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
    public static Date convertISOStringToDate(String isoDateString) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(isoDateString);
        Instant i = Instant.from(ta);
        Date d = Date.from(i);
        return d;
    }
}
