package com.example.jasmeet.studentcompanion.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.models.Course;
import com.example.jasmeet.studentcompanion.models.Lecture;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmeet on 4/11/2017.
 */

public class ChartHandler {

    private Context activityContext;
    private Activity activity;
    private Course course;
    private LineChart chart;

    public ChartHandler(Activity act, Course c1) {
        activityContext = act.getBaseContext();
        activity = act;
        course = c1;
        chart = (LineChart) activity.findViewById(R.id.chart);
    }

    public void setUpChart(List<Lecture> myData) {
        chart.setScaleEnabled(false);
        Description d = new Description();
        d.setText("");
        chart.setDescription(d);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter());
        chart.getAxisRight().setValueFormatter(new YAxisValueFormatter());

        List<Entry> entries = new ArrayList<>();
        List<Entry> emptyEntries = new ArrayList<>();
        List<Entry> presentInitialEntries = new ArrayList<>();
        List<Entry> absentInitialEntries = new ArrayList<>();
        List<Entry> presentEntries = new ArrayList<>();
        List<Entry> absentEntries = new ArrayList<>();

        ArrayList<Integer> chartColor = new ArrayList<>();
        ArrayList<Integer> presentEntriesColor = new ArrayList<>();
        ArrayList<Integer> absentEntriesColor = new ArrayList<>();

        for (Lecture data : myData) {
            if (data.isSafe()) {
                chartColor.add(ContextCompat.getColor(activityContext, R.color.colorGreen));
                entries.add(new Entry(data.getLectureNumber(), data.getAttendance()));
            } else {
                chartColor.add(ContextCompat.getColor(activityContext, R.color.colorRed));
                entries.add(new Entry(data.getLectureNumber(), data.getAttendance()));
            }
            emptyEntries.add(new Entry(data.getLectureNumber(), course.getMinimumAttendanceRequired()));
        }

        presentInitialEntries.add(new Entry(course.getTotalLectures(), course.getAttendance()));
        presentInitialEntries.add(new Entry(course.getTotalLectures() + 1, computeAttendance(course.getLecturesAttended() + 1, course.getTotalLectures() + 1)));
        absentInitialEntries.add(new Entry(course.getTotalLectures(), course.getAttendance()));
        absentInitialEntries.add(new Entry(course.getTotalLectures() + 1, computeAttendance(course.getLecturesAttended(), course.getTotalLectures() + 1)));

        if (course.getTotalLectures() < course.getExpectedTotalLectures()) {

            for (int i = course.getTotalLectures() + 1; i <= course.getExpectedTotalLectures(); i++) {
                int attendance = computeAttendance(course.getLecturesAttended() + (i - course.getTotalLectures()), i);
                presentEntries.add(new Entry(i, attendance));
                if (attendance >= course.getMinimumAttendanceRequired())
                    presentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorGreen));
                else
                    presentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorRed));

                attendance = computeAttendance(course.getLecturesAttended(), i);
                absentEntries.add(new Entry(i, attendance));
                if (attendance >= course.getMinimumAttendanceRequired())
                    absentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorGreen));
                else
                    absentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorRed));

                emptyEntries.add(new Entry(i, course.getMinimumAttendanceRequired()));
            }
        } else {
            for (int i = course.getTotalLectures() + 1; i <= course.getTotalLectures() + 5; i++) {
                int attendance = computeAttendance(course.getLecturesAttended() + (i - course.getTotalLectures()), i);
                presentEntries.add(new Entry(i, attendance));
                if (attendance >= course.getMinimumAttendanceRequired())
                    presentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorGreen));
                else
                    presentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorRed));

                attendance = computeAttendance(course.getLecturesAttended(), i);
                absentEntries.add(new Entry(i, attendance));
                if (attendance >= course.getMinimumAttendanceRequired())
                    absentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorGreen));
                else
                    absentEntriesColor.add(ContextCompat.getColor(activityContext, R.color.colorRed));

                emptyEntries.add(new Entry(i, course.getMinimumAttendanceRequired()));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Attendance");
        dataSet.setColor(ContextCompat.getColor(activityContext, R.color.colorPrimary));
        dataSet.setDrawValues(false);
        dataSet.setCircleColors(chartColor);
        dataSet.setCircleRadius(3);
        dataSet.setValueTextSize(10);
        dataSet.setValueFormatter(new MyAttendanceValueFormatter());
        dataSet.setHighlightLineWidth((float) 1.5);
        dataSet.setLineWidth((float) 1.5);

        LimitLine minimumAttendanceLine = new LimitLine(myData.get(0).getMinimumAttendanceRequired(), "");
        minimumAttendanceLine.setLineColor(Color.parseColor("#999999"));
        minimumAttendanceLine.enableDashedLine(20, 20, 0);
        minimumAttendanceLine.setLineWidth((float) 1.25);
        chart.getAxisLeft().addLimitLine(minimumAttendanceLine);

        LimitLine totalLecturesLine = new LimitLine(myData.size(), "");
        totalLecturesLine.setLineColor(Color.parseColor("#999999"));
        totalLecturesLine.enableDashedLine(10, 10, 0);
        totalLecturesLine.setLineWidth((float) 1.25);
        xAxis.addLimitLine(totalLecturesLine);

        LineDataSet expectedAttendanceGreenDataSet = new LineDataSet(presentEntries, "");
        expectedAttendanceGreenDataSet.setColor(ContextCompat.getColor(activityContext, R.color.colorGreen));
        expectedAttendanceGreenDataSet.setCircleColor(ContextCompat.getColor(activityContext, R.color.colorGreen));
        expectedAttendanceGreenDataSet.enableDashedLine(10, 10, 0);
        expectedAttendanceGreenDataSet.setValueTextColor(ContextCompat.getColor(activityContext, R.color.colorGreen));
        expectedAttendanceGreenDataSet.setHighlightEnabled(false);
        expectedAttendanceGreenDataSet.setValueFormatter(new MyAttendanceValueFormatter());
        expectedAttendanceGreenDataSet.setValueTextSize(7);
        expectedAttendanceGreenDataSet.setCircleColors(presentEntriesColor);
        expectedAttendanceGreenDataSet.setValueTextColors(presentEntriesColor);
        expectedAttendanceGreenDataSet.setLineWidth((float) 1.5);

        LineDataSet expectedAttendanceRedDataSet = new LineDataSet(absentEntries, "");
        expectedAttendanceRedDataSet.setColor(ContextCompat.getColor(activityContext, R.color.colorRed));
        expectedAttendanceRedDataSet.setCircleColor(ContextCompat.getColor(activityContext, R.color.colorRed));
        expectedAttendanceRedDataSet.enableDashedLine(10, 10, 0);
        expectedAttendanceRedDataSet.setValueTextColor(ContextCompat.getColor(activityContext, R.color.colorRed));
        expectedAttendanceRedDataSet.setHighlightEnabled(false);
        expectedAttendanceRedDataSet.setValueFormatter(new MyAttendanceValueFormatter());
        expectedAttendanceRedDataSet.setValueTextSize(8);
        expectedAttendanceRedDataSet.setCircleColors(absentEntriesColor);
        expectedAttendanceRedDataSet.setValueTextColors(absentEntriesColor);
        expectedAttendanceRedDataSet.setLineWidth((float) 1.5);

        LineDataSet initialExpectedAttendanceGreenDataSet = new LineDataSet(presentInitialEntries, "");
        initialExpectedAttendanceGreenDataSet.setColor(ContextCompat.getColor(activityContext, R.color.colorGreen));
        initialExpectedAttendanceGreenDataSet.setCircleColor(ContextCompat.getColor(activityContext, R.color.colorGreen));
        initialExpectedAttendanceGreenDataSet.enableDashedLine(20, 20, 0);
        initialExpectedAttendanceGreenDataSet.setDrawValues(false);
        initialExpectedAttendanceGreenDataSet.setHighlightEnabled(false);

        LineDataSet initialExpectedAttendanceRedDataSet = new LineDataSet(absentInitialEntries, "");
        initialExpectedAttendanceRedDataSet.setColor(ContextCompat.getColor(activityContext, R.color.colorRed));
        initialExpectedAttendanceRedDataSet.setCircleColor(ContextCompat.getColor(activityContext, R.color.colorRed));
        initialExpectedAttendanceRedDataSet.enableDashedLine(20, 20, 0);
        initialExpectedAttendanceRedDataSet.setDrawValues(false);
        initialExpectedAttendanceRedDataSet.setHighlightEnabled(false);

        LineData lineData = new LineData(initialExpectedAttendanceGreenDataSet, initialExpectedAttendanceRedDataSet, expectedAttendanceGreenDataSet, expectedAttendanceRedDataSet, dataSet);
        chart.setData(lineData);
        chart.invalidate();
        chart.getLegend().setEnabled(false);

        if (myData.size() < 14) {
            chart.setVisibleXRangeMaximum(myData.size());
        } else {
            chart.setVisibleXRangeMaximum(14);
            chart.moveViewToX(myData.size() - 14);
        }
    }

    private int computeAttendance(int lecturesAttended, int totalLectures) {
        return (int) ((double) lecturesAttended / totalLectures * 100);
    }
}
