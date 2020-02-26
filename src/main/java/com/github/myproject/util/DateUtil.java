package com.github.myproject.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public abstract class DateUtil {

    public static final DateTimeFormatter NORMAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter NORMAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS_SSSSSS_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyyMMdd");


    public static String getCurrentDatetime() {
        return LocalDateTime.now().format(NORMAL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String text) {
        return parseDateTime(text, NORMAL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String text, DateTimeFormatter formatter) {
        return LocalDateTime.parse(text, formatter);
    }

    public static boolean isExpiredAtDays(long timestamp, int ttlDays) {
        if (ttlDays < 0) {
            return false;
        }
        return System.currentTimeMillis() - timestamp > ttlDays * 24L * 60 * 60 * 1000;
    }

    public static long toEpochMilli(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long toEpochSecond(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static long toEpochSecond(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    public static String formatEpochMilli(String epochMilli) {
        long longValue = Long.parseLong(epochMilli);
        return Instant.ofEpochMilli(longValue)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(NORMAL_DATE_FORMATTER);
    }

    public static long betweenDays(long start, long end) {
        LocalDateTime localDateTime_1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault());
        LocalDateTime localDateTime_2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), ZoneId.systemDefault());
        return localDateTime_2.toLocalDate().toEpochDay() - localDateTime_1.toLocalDate().toEpochDay();
    }

    /**
     * 获取UTC时间
     *
     * @return 字符串时间，格式yyyyMMddHHmmssSSS
     */
    @Deprecated
    public static String getUTCTimeStr() {
        StringBuffer timeBuffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milliseconds = cal.get(Calendar.MILLISECOND);
        String millisecondsStr = "000000" + milliseconds;
        timeBuffer.append(year).append(month < 10 ? ("0" + month) : month)
                .append(day < 10 ? ("0" + day) : day)
                .append(hour < 10 ? ("0" + hour) : hour)
                .append(minute < 10 ? ("0" + minute) : minute)
                .append(second < 10 ? ("0" + second) : second).append(millisecondsStr.substring(millisecondsStr.length() - 6));
        return timeBuffer.toString();
    }

    /**
     * 获取UTC时间
     *
     * @return 字符串时间，格式yyyyMMddHHmmssSSSSSS
     */
    public static String getUTCTimeStrNew() {
        return YYYY_MM_DD_HH_MM_SS_SSSSSS_FORMATTER.format(LocalDateTime.now(Clock.systemUTC()));
    }

}
