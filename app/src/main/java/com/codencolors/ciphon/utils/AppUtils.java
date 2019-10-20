package com.codencolors.ciphon.utils;

import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static final String ENCRYPTED_FILE_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + ".ciphon";

    public static String convertDateIntoString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String getFileSizeMegaBytes(File file) {
        return round((double) file.length() / (1024 * 1024), 2) + " MB";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
