package com.example.finalyear.common.utils;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {
    @NonNull
    public static String millisToDate(Long date, String format) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(date));
    }

    public static String millisToDate(Long date) {
        return millisToDate(date, "dd/MM/yyyy");
    }

    @NonNull
    public static Long dateToMillis(String date, String dateFormat) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        try {
            Date dt = dateFormatter.parse(date);
            assert dt != null;
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @NonNull
    public static Long dateToMillis(String date) {
        return dateToMillis(date, "MM/dd/yyyy");
    }


    public static int splitMeasureAmount(@NonNull String input) {
        String[] parts = input.split("(?<=\\d)(?=\\D)");
        return Integer.parseInt(parts[0]);
    }

    public static String splitMeasureUnits(@NonNull String input) {
        String[] parts = input.split("(?<=\\d)(?=\\D)");
        return parts[1];
    }

    public static String secondToHr(Long duration) {
        if (duration < 60) {
            return duration + "s";
        }
        String date = "";
        long hours = duration / 3600;
        if (hours > 0) date += hours + "h";
        long minutes = (duration % 3600) / 60;
        date += minutes + "m";
        return date;
    }

    public static int caloriesBurn(float weight, int stepsTaken) {
        double met = 3.5; // MET value for walking at a moderate pace// User information
        double weightInPounds = weight * 2.2; // User weight in pounds

        // Calculate calories burned per step
        double caloriesBurnedPerStep = ((weightInPounds * 0.45) * 0.57) / 220;

        // Calculate total calories burned during the walk
        double totalCaloriesBurned = stepsTaken * caloriesBurnedPerStep;
        return Integer.parseInt(String.valueOf((int) totalCaloriesBurned));

    }

    public static String getMilesString(float dist){
        return String.format(Locale.getDefault(),"%.3f", dist);
    }

    @NonNull
    public static List<InventoryItem> sortWithQuantity(List<InventoryItem> newList) {
        Collections.sort(newList, new Comparator<InventoryItem>() {
            @Override
            public int compare(InventoryItem o1, InventoryItem o2) {
                return Integer.compare(o1.getQuantity(), o2.getQuantity());
            }
        });
        return newList;
    }
}
