package com.example.jasmeet.studentcompanion.helper;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Jasmeet on 3/10/2017.
 */

public class MyAttendanceValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyAttendanceValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value) + "%"; // e.g. append a dollar-sign
    }
}
