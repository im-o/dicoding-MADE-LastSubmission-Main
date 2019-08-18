package com.stimednp.aplikasimoviecataloguesub4.addingmethod;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class AllOtherMethod {
    public String changeFormatDate(String changeFormat) {
        try {
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = formatter.parse(changeFormat);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return changeFormat;
    }

    public String getLastYear(String setReleasedate) { //get year exp : (2018)
        String releaseYear = "";
        if (setReleasedate.length() >= 4) {
            releaseYear = setReleasedate.substring(setReleasedate.length() - 4);
        } else {
            releaseYear = "****";
        }
        return releaseYear;
    }
}
