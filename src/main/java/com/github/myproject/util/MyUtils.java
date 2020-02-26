package com.github.myproject.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by madong on 2016/7/8.
 */
public class MyUtils {
    private final static Logger logger = LoggerFactory.getLogger(MyUtils.class);
    public final static Gson DEFAULT_GSON = new Gson();
    public final static Gson SERIALIZE_NULLS_GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
    public static final Gson TOKEN_GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    /**
     * 判断是否是数字
     *
     * @param strnum
     * @return
     */
    private static boolean isNumeric(String strnum) {
        String p = "[0-9]*";
        Pattern pattern = Pattern.compile(p);
        Matcher isNum = pattern.matcher(strnum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是日期
     *
     * @param strDate
     * @return
     */
    private static boolean isDate(String strDate) {
        String p = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$";
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取域名
     *
     * @param url 地址
     * @return 域名
     */
    public static String getDomain(String url) {
        return url.substring(0, url.indexOf("/", url.indexOf("//") + 2));
    }


    /**
     * 时间转毫秒数
     *
     * @param time
     * @param timeFormatter
     * @return
     */
    public static long getLongTime(String time, String timeFormatter) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter);
            LocalDateTime LocalDate = LocalDateTime.parse(time, formatter);
            Instant instantend = LocalDate.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instantend);
            return date.getTime();
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 根据时间戳获取时间
     *
     * @param time
     * @param timeFormatter
     * @return
     */
    public static String getDateStr(String time, String timeFormatter) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(time)), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormatter);
        return dateTime.format(formatter);
    }


    /**
     * 生成UGID
     *
     * @return
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
