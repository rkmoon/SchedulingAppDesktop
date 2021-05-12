package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeConversions {

    private static final String estTime = "US/Eastern";


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
}
