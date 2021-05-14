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


    public static LocalDateTime localToUTC(LocalDateTime local){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(local.toLocalDate(),local.toLocalTime(), ZoneId.systemDefault());
        ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return LocalDateTime.from(utc);
    }

    public static LocalDateTime utcToLocal(LocalDateTime utc){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(utc.toLocalDate(), utc.toLocalTime(), ZoneOffset.UTC);
        ZonedDateTime local = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        return LocalDateTime.from(local);
    }

    public static LocalDateTime localToEST(LocalDateTime local){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(local.toLocalDate(), local.toLocalTime(), ZoneId.systemDefault());
        ZonedDateTime est = zonedDateTime.withZoneSameInstant(ZoneId.of((estTime)));
        return LocalDateTime.from(est);
    }

    public static LocalDateTime utcToEST(LocalDateTime utc){
        ZonedDateTime zonedDateTime = ZonedDateTime.of(utc.toLocalDate(), utc.toLocalTime(), ZoneOffset.UTC);
        ZonedDateTime est = zonedDateTime.withZoneSameInstant(ZoneId.of(estTime));
        return LocalDateTime.from(est);
    }

    public static boolean checkBeforeOpenHours(LocalDateTime time){
        LocalDateTime convertedTime = utcToEST(time);
        LocalTime localTime = LocalTime.of(convertedTime.getHour(), convertedTime.getMinute());
        return localTime.isBefore(openTime);
    }

    public static boolean checkAfterCloseHours(LocalDateTime time){
        LocalDateTime convertedTime = utcToEST(time);
        LocalTime localTime = LocalTime.of(convertedTime.getHour(), convertedTime.getMinute());
        return localTime.isAfter(closeTime);
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime start2, LocalDateTime end1, LocalDateTime end2){
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
