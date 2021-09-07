package com.movieBooking.backend.misc;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateSGFormat {
    private final SimpleDateFormat simpleDateFormat;
    private final TimeZone tz;

    public DateSGFormat() {
        this.simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm", Locale.ENGLISH);
        this.tz = TimeZone.getTimeZone("Asia/Singapore");
        simpleDateFormat.setTimeZone(tz);
    }

    public SimpleDateFormat get()
    {
        return simpleDateFormat;
    }
}
