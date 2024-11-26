package com.sunjoy.common.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 *
 * @author sunjoy
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 判断时间点是否周几
     *
     * @param dateTime
     * @param targetDay
     * @return
     */
    public static boolean isSameDay(LocalDateTime dateTime, DayOfWeek targetDay) {
        // 获取日期对应的星期
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        // 判断是否为目标星期几
        return dayOfWeek == targetDay;
    }

    /**
     * 判断日期compareDate相对于endDate是否过期
     *
     * @param compareDate 要比较的日期
     * @param endDate     要对比的日期，一般是最后时刻（23:59:59.999999999）
     * @return
     */

    public static boolean isExpired(Date compareDate, Date endDate) {
        LocalDateTime tempDay = endDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(); // 转换为 LocalDateTime

        LocalDateTime compayDay = compareDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(); // 转换为 LocalDateTime

        return isExpired(compayDay, tempDay);
    }

    public static boolean isExpired(LocalDateTime compareDate, LocalDateTime endDate) {

        LocalDateTime endOfToday = LocalDateTime.of(endDate.toLocalDate(), LocalTime.MAX).plusNanos(1);
        return compareDate.isAfter(endOfToday);
    }

    /**
     * 获取两个时间的日期跨度
     *
     * @param startDateTime
     * @param endDateTime
     * @return Long 返回天数跨度
     */
    public static Long daysBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 获取日期部分
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // 如果结束时间的日期部分和开始时间的日期部分相同，则不计入
        /*if (endDateTime.toLocalTime().isBefore(startDateTime.toLocalTime())) {
            daysBetween--;
        }*/
        daysBetween++;
        return daysBetween;
    }

    public static Long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 计算两个 LocalDateTime 之间的持续时间
        Duration duration = Duration.between(startDateTime, endDateTime);

        // 获取相差的分钟数
        return duration.toMinutes();
    }

    /**
     * 从日期时间段B（开始时间startB，结否时间endB）中统计出分段A(开始时间startA，结束时间endA)的每段时长
     *
     * @param startB
     * @param endB
     * @param startA
     * @param endA
     * @return
     */
    public static List<Duration> getMatchingTimeSlots(LocalDateTime startB, LocalDateTime endB, LocalTime startA, LocalTime endA) {
        List<Duration> matchingDurations = new ArrayList<>();

        // 遍历时间段 B 的每一天
        LocalDateTime currentDay = startB.toLocalDate().atStartOfDay();

        while (currentDay.isBefore(endB.toLocalDate().atStartOfDay().plusDays(1))) {
            // 获取当前日期的时间段 A
            LocalDateTime startAWithDate = currentDay.with(startA);
            LocalDateTime endAWithDate = currentDay.with(endA);

            // 计算 A 和 B 的交集
            LocalDateTime overlapStart = startB.isAfter(startAWithDate) ? startB : startAWithDate;
            LocalDateTime overlapEnd = endB.isBefore(endAWithDate) ? endB : endAWithDate;

            // 检查是否存在交集
            if (overlapStart.isBefore(overlapEnd)) {
                Duration duration = Duration.between(overlapStart, overlapEnd);
                matchingDurations.add(duration);
            }

            // 移动到下一天
            currentDay = currentDay.plusDays(1);
        }

        return matchingDurations;
    }
}