package com.example.jasmeet.studentcompanion.helper;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by Jasmeet on 3/10/2017.
 */

public class YAxisValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public YAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + "%";
    }
}
