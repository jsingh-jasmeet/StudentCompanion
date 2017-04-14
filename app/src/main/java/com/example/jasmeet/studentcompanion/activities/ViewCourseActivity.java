package com.example.jasmeet.studentcompanion.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.adapters.LectureAdapter;
import com.example.jasmeet.studentcompanion.data.DBManager;
import com.example.jasmeet.studentcompanion.helper.ChartHandler;
import com.example.jasmeet.studentcompanion.helper.LectureTouchHelper;
import com.example.jasmeet.studentcompanion.helper.RecyclerViewDividerItemDecoration;
import com.example.jasmeet.studentcompanion.models.Course;
import com.example.jasmeet.studentcompanion.models.Lecture;
import com.example.jasmeet.studentcompanion.models.LectureTag;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ViewCourseActivity extends AppCompatActivity {

    private static final String TAG = "ViewCourseActivity";
    boolean didScroll;
    boolean didScrollStateChange;
    boolean triggered;
    private Context ctx = this;
    private View lastSelectedItem;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm = new LinearLayoutManager(this);
    private Entry entry;
    private LectureAdapter adapter;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        Intent intent = getIntent();

        long ID = intent.getLongExtra("id", 0);
        course = intent.getParcelableExtra("course");

        setWidths();

        final LineChart chart = (LineChart) findViewById(R.id.chart);

        recyclerView = (RecyclerView) findViewById(R.id.lecture_recycler_view);
        recyclerView.setLayoutManager(llm);
        DBManager dbManager = new DBManager(this);
        dbManager.open();
        List<Lecture> myData = dbManager.fetchLectures(ID);

        initialSelection(myData.size());

        recyclerView.addItemDecoration(new RecyclerViewDividerItemDecoration(ctx));

        adapter = new LectureAdapter(this, myData);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new LectureTouchHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        ChartHandler ch = new ChartHandler(this, course);
        ch.setUpChart(myData);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                chart.highlightValue(h);

                recyclerView.smoothScrollToPosition((int) e.getX() - 1);

                if (recyclerView.findViewHolderForLayoutPosition((int) e.getX() - 1) != null) {
                    boolean scroll = listenToScroll(e, true);
                    if (!scroll && !didScroll) {
                        recyclerView.findViewHolderForLayoutPosition((int) e.getX() - 1).itemView.setTag(new LectureTag(0));
                        recyclerView.findViewHolderForLayoutPosition((int) e.getX() - 1).itemView.callOnClick();
                    }
                    lastSelectedItem = recyclerView.findViewHolderForLayoutPosition((int) e.getX() - 1).itemView;
                } else {
                    listenToScroll(e, true);
                }
            }

            @Override
            public void onNothingSelected() {
                if (lastSelectedItem != null) {
                    lastSelectedItem.setTag(new LectureTag(1));
                    lastSelectedItem.callOnClick();
                    lastSelectedItem = null;
                }
            }
        });

        adapter.setChart(chart);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.setSelectionsToNull();
        }
    }

    public boolean listenToScroll(Entry e, boolean t) {
        entry = e;
        triggered = t;

        didScroll = false;
        didScrollStateChange = false;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE && !didScrollStateChange && triggered) {

                    if (recyclerView.findViewHolderForLayoutPosition((int) entry.getX() - 1) != null) {
                        int color = Color.TRANSPARENT;
                        Drawable background = recyclerView.findViewHolderForLayoutPosition((int) entry.getX() - 1).itemView.getBackground();
                        if (background instanceof ColorDrawable)
                            color = ((ColorDrawable) background).getColor();

                        if (color != ContextCompat.getColor(ctx, R.color.colorPrimaryLight)) {
                            recyclerView.findViewHolderForLayoutPosition((int) entry.getX() - 1).itemView.setTag(new LectureTag(0));
                            recyclerView.findViewHolderForLayoutPosition((int) entry.getX() - 1).itemView.callOnClick();
                            lastSelectedItem = recyclerView.findViewHolderForLayoutPosition((int) entry.getX() - 1).itemView;
                        }
                    }
                    didScroll = true;
                    didScrollStateChange = true;
                    triggered = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                didScroll = true;
            }
        });
        return didScrollStateChange;
    }

    /*
    * initialSelection() method scrolls down to the bottom of the list of lectures and highlights the last lecture
    * attended.
    */

    public void initialSelection(int position) {

        recyclerView.smoothScrollToPosition(position);

        if (recyclerView.findViewHolderForLayoutPosition(position) != null) {
            boolean scroll = listenToScroll(new Entry(position, 0), true);
            if (!scroll && !didScroll) {
                recyclerView.findViewHolderForLayoutPosition(position).itemView.setTag(new LectureTag(0));
                recyclerView.findViewHolderForLayoutPosition(position).itemView.callOnClick();
            }

            lastSelectedItem = recyclerView.findViewHolderForLayoutPosition(position).itemView;

        } else {
            listenToScroll(new Entry(position, 0), true);
        }
    }

    /*
    * The setWidths() method fixes the widths of the columns in the table below the chart by taking into account the width
    * of the device. This helps maintaining a consistent look.
    */

    private void setWidths() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        TextView lectureHeaderLectureNumberTextView = (TextView) findViewById(R.id.lecture_header_lecture_number);
        TextView lectureHeaderStatusTextView = (TextView) findViewById(R.id.lecture_header_status);
        TextView lectureHeaderAttendedTextView = (TextView) findViewById(R.id.lecture_header_attended);
        TextView lectureHeaderAttendanceTextView = (TextView) findViewById(R.id.lecture_header_attendance);

        lectureHeaderLectureNumberTextView.setWidth((int) (displayMetrics.widthPixels * 0.15));
        lectureHeaderStatusTextView.setWidth((int) (displayMetrics.widthPixels * 0.35));
        lectureHeaderAttendedTextView.setWidth((int) (displayMetrics.widthPixels * 0.25));
        lectureHeaderAttendanceTextView.setWidth((int) (displayMetrics.widthPixels * 0.25));
    }
}
