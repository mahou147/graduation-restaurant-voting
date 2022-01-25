package com.github.mahou147.voting.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.LocalDate;

@UtilityClass
public class DateTimeUtil {
    private static Clock clock; // https://stackoverflow.com/a/45833128

    static {
        resetClock();
    }

    public static void setClock(Clock clock) {
        DateTimeUtil.clock = clock;
    }

    public static Clock getClock() {
        return clock;
    }

    public static void resetClock() {
        setClock(Clock.systemDefaultZone());
    }

    public static LocalDate setCurrentDate() {
        return LocalDate.now(clock);
    }
}
