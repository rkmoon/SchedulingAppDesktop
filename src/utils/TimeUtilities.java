package utils;

import java.time.*;

/**
 * This class contains a number of useful functions for dealing with the various time operations in the application.
 * It can convert between EST, local, and UTC time as well as containing the opening and closing business hours.
 */
public class TimeUtilities {

    private static final String estTime = "US/Eastern";
    private static final LocalTime openTime = LocalTime.of(8,0);
    private static final LocalTime closeTime = LocalTime.of(22, 0);


    /**
     * Converts local time to UTC
     * @param local local time
     * @return UTC time
     */
    public static LocalDateTime localToUTC(LocalDateTime local){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(local.toLocalDate(),local.toLocalTime(), ZoneId.systemDefault());
        ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return LocalDateTime.from(utc);
    }

    /**
     * Converts UTC time to local time
     * @param utc UTC time
     * @return local time
     */
    public static LocalDateTime utcToLocal(LocalDateTime utc){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(utc.toLocalDate(), utc.toLocalTime(), ZoneOffset.UTC);
        ZonedDateTime local = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return LocalDateTime.from(local);
    }

    /**
     * Converts local time to EST time
     * @param local local time
     * @return EST time
     */
    public static LocalDateTime localToEST(LocalDateTime local){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(local.toLocalDate(), local.toLocalTime(), ZoneId.systemDefault());
        ZonedDateTime est = zonedDateTime.withZoneSameInstant(ZoneId.of((estTime)));
        return LocalDateTime.from(est);
    }

    /**
     * Converts UTC time to EST time
     * @param utc UST time
     * @return EST time
     */
    public static LocalDateTime utcToEST(LocalDateTime utc){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(utc.toLocalDate(), utc.toLocalTime(), ZoneOffset.UTC);
        ZonedDateTime est = zonedDateTime.withZoneSameInstant(ZoneId.of(estTime));
        return LocalDateTime.from(est);
    }

    /**
     * Checks to see if local time is before business opening hours
     * @param time local time
     * @return true if before opening time, false if not
     */
    public static boolean checkBeforeOpenHours(LocalDateTime time){
        LocalDateTime convertedTime = utcToEST(time);
        LocalTime localTime = LocalTime.of(convertedTime.getHour(), convertedTime.getMinute());
        return localTime.isBefore(openTime);
    }

    /**
     * Checks to see if local time is after business closing hours
     * @param time local time
     * @return true if after closing time, false if not
     */
    public static boolean checkAfterCloseHours(LocalDateTime time){
        LocalDateTime convertedTime = utcToEST(time);
        LocalTime localTime = LocalTime.of(convertedTime.getHour(), convertedTime.getMinute());
        return localTime.isAfter(closeTime);
    }

    /**
     * checks to see if two times overlap
     * @param start1 start of first time
     * @param start2 start of second time
     * @param end1 end of first time
     * @param end2 end of second time
     * @return true if overlapping, false if not
     */
    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime start2, LocalDateTime end1, LocalDateTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
