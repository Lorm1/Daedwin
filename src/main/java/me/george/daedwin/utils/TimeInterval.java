package me.george.daedwin.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

    @Getter @AllArgsConstructor
    public enum TimeInterval {
        SECOND("s", java.util.concurrent.TimeUnit.SECONDS, Calendar.SECOND),
        MINUTE("min", java.util.concurrent.TimeUnit.MINUTES, Calendar.MINUTE),
        HOUR("hr", java.util.concurrent.TimeUnit.HOURS, Calendar.HOUR_OF_DAY),
        DAY("day", java.util.concurrent.TimeUnit.DAYS, Calendar.DAY_OF_MONTH),
        WEEK("week", 7, Calendar.WEEK_OF_MONTH),
        MONTH("month", 30, Calendar.MONTH),
        YEAR("yr", 365, Calendar.YEAR);

        private String suffix;
        private TimeUnit unit;
        private int interval;
        private int calendarId;

        TimeInterval(String s, java.util.concurrent.TimeUnit unit, int calendar) {
            this(s, unit, (int) java.util.concurrent.TimeUnit.SECONDS.convert(1, unit), calendar);
        }

        TimeInterval(String s, int days, int calendar) {
            this(s, null, (int) java.util.concurrent.TimeUnit.SECONDS.convert(days, java.util.concurrent.TimeUnit.DAYS), calendar);
        }

        public static TimeInterval getByCode(String code) {
            return Arrays.stream(values()).filter(ti -> ti.getSuffix().startsWith(code.toLowerCase())).findFirst().orElse(SECOND);
        }

        /**
         * Get the current time unit value for this interval.
         *
         * @return unit
         */
        @SuppressWarnings("MagicConstant")
        public int getValue() {
            return Calendar.getInstance().get(getCalendarId());
        }
    }
