package com.djukim.thisnthat.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    public static final DateTimeFormatter FORMAT_DATETIME_DASH =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter FORMAT_DATE_DASH =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** LocalDateTime을 "yyyy-MM-dd HH:mm:ss" 형식의 문자열로 변환 */
    public static String toFormatDateTimeDash(LocalDateTime localDateTime) {
        return localDateTime.format(FORMAT_DATETIME_DASH);
    }

    /** "yyyy-MM-dd HH:mm:ss" 형식의 문자열을 LocalDateTime으로 변환 */
    public static LocalDateTime fromFormatDateTimeDash(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, FORMAT_DATETIME_DASH);
    }

    /** "yyyyMMdd" 형식의 문자열을 LocalDate로 변환 */
    public static LocalDate fromFormatDate(String dateStr) {
        return LocalDate.parse(dateStr, FORMAT_DATE);
    }

    /** LocalDate를 "yyyy-MM-dd" 형식의 문자열로 변환 */
    public static String toFormatDateDash(LocalDate localDate) {
        return localDate.format(FORMAT_DATE_DASH);
    }
}
