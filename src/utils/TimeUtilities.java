package utils;

import java.time.*;

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
}
