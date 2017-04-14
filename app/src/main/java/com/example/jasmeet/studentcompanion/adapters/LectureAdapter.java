package com.example.jasmeet.studentcompanion.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.data.DBManager;
import com.example.jasmeet.studentcompanion.models.Lecture;
import com.example.jasmeet.studentcompanion.models.LectureTag;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

/**
 * Created by Jasmeet on 3/9/2017.
 */

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.MyViewHolder> {

    private static final String TAG = "LectureAdapter";
    private static Long ID;
    private static View lastSelectedView;
    private static int lastSelectedViewLectureNumber;
    private static Context context;
    private static RelativeLayout viewCourseActivityLayout;
    private static List<Lecture> mainList;
    private static LineChart chart;
    private DBManager dbManager;
    private View view;
    private DisplayMetrics displayMetrics;

    public LectureAdapter(Context ctx, List<Lecture> list) {
        context = ctx;
        mainList = list;
        displayMetrics = context.getResources().getDisplayMetrics();
        dbManager = new DBManager(context);
        dbManager.open();
    }

    public void setChart(LineChart c) {
        chart = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.lecture_list_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        myViewHolder.lectureNumberTextView.setWidth((int) (displayMetrics.widthPixels * 0.17));
        myViewHolder.statusTextView.setWidth((int) (displayMetrics.widthPixels * 0.33));
        myViewHolder.lecturesAttendedTextView.setWidth((int) (displayMetrics.widthPixels * 0.25));
        myViewHolder.attendanceTextView.setWidth((int) (displayMetrics.widthPixels * 0.25));

        viewCourseActivityLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_view_course, null);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(LectureAdapter.MyViewHolder holder, int position) {

        Lecture lecture = mainList.get(position);

        holder.lectureNumberTextView.setText(Integer.toString(lecture.getLectureNumber()));
        holder.statusTextView.setText(lecture.isPresent() ? "Present" : "Absent");
        holder.statusTextView.setTextColor(lecture.isPresent() ? ContextCompat.getColor(context, R.color.colorGreen) : ContextCompat.getColor(context, R.color.colorRed));
        holder.lecturesAttendedTextView.setText(Integer.toString(lecture.getLecturesAttended()) + "/" + Integer.toString(lecture.getLectureNumber()));
        holder.attendanceTextView.setText(Integer.toString(lecture.getAttendance()) + "%");
        holder.attendanceTextView.setTextColor(lecture.isSafe() ? ContextCompat.getColor(context, R.color.colorGreen) : ContextCompat.getColor(context, R.color.colorRed));

        int newRecycledViewLectureNumber = Integer.parseInt(((TextView) holder.itemView.findViewById(R.id.lecture_number)).getText().toString());

        if (lastSelectedViewLectureNumber != -1 && lastSelectedViewLectureNumber != newRecycledViewLectureNumber)
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        else if (lastSelectedViewLectureNumber == newRecycledViewLectureNumber)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
    }


    public void remove(int position) {
        ID = mainList.get(position).getCourseID();

        dbManager.deleteLecture(mainList.get(position));
        mainList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        mainList = dbManager.fetchLectures(ID);

        LineChart chart = (LineChart) viewCourseActivityLayout.findViewById(R.id.chart);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void setSelectionsToNull() {
        lastSelectedViewLectureNumber = -1;
        lastSelectedView = null;
        mainList = null;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView lectureNumberTextView;
        TextView statusTextView;
        TextView lecturesAttendedTextView;
        TextView attendanceTextView;

        MyViewHolder(View itemView) {
            super(itemView);

            lectureNumberTextView = (TextView) itemView.findViewById(R.id.lecture_number);
            statusTextView = (TextView) itemView.findViewById(R.id.lecture_status);
            lecturesAttendedTextView = (TextView) itemView.findViewById(R.id.lectures_attended);
            attendanceTextView = (TextView) itemView.findViewById(R.id.lectures_attendance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int viewXValue = Integer.parseInt(((TextView) view.findViewById(R.id.lecture_number)).getText().toString());
            int viewTag;

            if (view.getTag() != null)
                viewTag = ((LectureTag) view.getTag()).getTag();
            else
                viewTag = 3;

            if (lastSelectedView == view) {
                if (viewTag != 0) {
                    lastSelectedView.setBackgroundColor(Color.TRANSPARENT);
                    lastSelectedView = null;
                    lastSelectedViewLectureNumber = -1;
                    chart.highlightValue(viewXValue, -1, false);
                }
            } else {
                if (lastSelectedView != null) {
                    lastSelectedView.setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                    lastSelectedView = view;
                    lastSelectedViewLectureNumber = Integer.parseInt(((TextView) lastSelectedView.findViewById(R.id.lecture_number)).getText().toString());
                    chart.highlightValue(viewXValue, 4, false);

                } else {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                    lastSelectedView = view;
                    lastSelectedViewLectureNumber = Integer.parseInt(((TextView) lastSelectedView.findViewById(R.id.lecture_number)).getText().toString());
                    chart.highlightValue(viewXValue, 4, false);
                }

                int lowestVisibleX = (int) chart.getLowestVisibleX();
                int highestVisibleX = (int) chart.getHighestVisibleX();

                if (mainList.size() <= 14) {
                    chart.moveViewToX(1);
                } else if (viewXValue < lowestVisibleX) {
                    chart.moveViewToX(viewXValue);
                } else if (viewXValue > highestVisibleX) {
                    if (viewXValue - 14 < 1)
                        chart.moveViewToX(1);
                    else
                        chart.moveViewToX(viewXValue - 14);
                }
            }
            view.setTag(null);
        }
    }
}
