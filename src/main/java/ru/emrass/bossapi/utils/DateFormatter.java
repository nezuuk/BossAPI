package ru.emrass.bossapi.utils;

import java.text.SimpleDateFormat;

public class DateFormatter {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public static String formatTime(long time){
        return timeFormat.format(time);
    }

    public static String formatDateTime(long time){
        return dateFormat.format(time);
    }
}
