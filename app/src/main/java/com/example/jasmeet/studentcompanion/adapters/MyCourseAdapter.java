package com.example.jasmeet.studentcompanion.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.activities.CourseFormActivity;
import com.example.jasmeet.studentcompanion.activities.ViewCourseActivity;
import com.example.jasmeet.studentcompanion.data.DBManager;
import com.example.jasmeet.studentcompanion.data.DatabaseHelper;
import com.example.jasmeet.studentcompanion.models.Course;
import com.example.jasmeet.studentcompanion.models.Lecture;

/**
 * Created by Jasmeet on 3/4/2017.
 */

public class MyCourseAdapter extends SimpleCursorAdapter {

    private Context ctx;
    final float scale;

    DBManager dbManager;

    private static final String TAG = "MyCourseAdapter";

    public MyCourseAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        ctx = context;
        scale = ctx.getResources().getDisplayMetrics().density;

        dbManager = new DBManager(ctx);
        dbManager.open();
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final int position = cursor.getPosition();
        final View view1 = view;

        super.bindView(view, context, cursor);
        if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SAFE)) == 1) {
            TextView attendanceTextView = (TextView) view.findViewById(R.id.list_item_attendance_textview);
            TextView percentSymbolTextView = (TextView) view.findViewById(R.id.list_item_attendance_percent_symbol_textview);
            attendanceTextView.setTextColor(ContextCompat.getColor(ctx, R.color.colorGreen));
            percentSymbolTextView.setTextColor(ContextCompat.getColor(ctx, R.color.colorGreen));
        } else {
            TextView attendanceTextView = (TextView) view.findViewById(R.id.list_item_attendance_textview);
            TextView percentSymbolTextView = (TextView) view.findViewById(R.id.list_item_attendance_percent_symbol_textview);
            attendanceTextView.setTextColor(ContextCompat.getColor(ctx, R.color.colorRed));
            percentSymbolTextView.setTextColor(ContextCompat.getColor(ctx, R.color.colorRed));
        }


        /* Setting values for Safe by/Short by textviews */

        updateShortBy(view, cursor);

        /* POP UP WINDOW OPTIONS */

        String[] options = ctx.getResources().getStringArray(R.array.popup_menu_list_items);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ctx, R.layout.popup_window_layout, options);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(ctx);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth((int) (128 * scale + 0.5f));

        ImageButton popupMenuImageButton = (ImageButton) view.findViewById(R.id.list_item_popup_menu);
        popupMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (((ColorDrawable) v.getBackground()).getColor() == Color.TRANSPARENT)
                    v.setBackgroundColor(Color.LTGRAY);
                listPopupWindow.setAnchorView(v);
                listPopupWindow.show();
                listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                });
            }
        });

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                cursor.moveToPosition(position);
                switch (position1) {

                    case 0:
                        Course viewCourseItem = getCourseFromDatabase(cursor);
                        Intent viewCourse = new Intent(ctx, ViewCourseActivity.class);
                        viewCourse.putExtra("id", cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID)));
                        viewCourse.putExtra("course", viewCourseItem);
                        ctx.startActivity(viewCourse);
                        break;

                    case 1:
                        Course editCourseItem = getCourseFromDatabase(cursor);
                        Intent editCourse = new Intent(ctx, CourseFormActivity.class);
                        editCourse.putExtra("id", cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID)));
                        editCourse.putExtra("course", editCourseItem);
                        ctx.startActivity(editCourse);
                        break;

                    case 2:
                        dbManager.delete(cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)));

                        listPopupWindow.dismiss();

                        Cursor cursor1 = dbManager.fetch();
                        swapCursor(cursor1);
                        notifyDataSetChanged();

                        break;
                }
            }
        });

        /*--------Set listeners on PRESENT and ABSENT buttons---------*/

        Button presentButton = (Button) view.findViewById(R.id.list_item_present_button);
        presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                buttonClicked(true, cursor);
                Cursor cursor1 = dbManager.fetch();
                swapCursor(cursor1);
                cursor1.moveToPosition(position);
                addLecture(true, cursor1);
                notifyDataSetChanged();
            }
        });

        Button absentButton = (Button) view.findViewById(R.id.list_item_absent_button);
        absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                buttonClicked(false, cursor);
                Cursor cursor1 = dbManager.fetch();
                swapCursor(cursor1);
                cursor1.moveToPosition(position);
                addLecture(false, cursor1);
                notifyDataSetChanged();
            }
        });

    }

    public void updateShortBy(View view, Cursor cursor) {
        int totalLectures = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TOTALLECTURES));
        double minimumAttendanceRequired = (double) cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MINIMUMATTENDANCEREQUIRED)) / 100;
        int lecturesAttended = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED));

        int shortBy = (int) ((Math.ceil((double) totalLectures * minimumAttendanceRequired)) - lecturesAttended);

        TextView shortByTextView = (TextView) view.findViewById(R.id.list_item_short_by_textview);
        TextView shortByValueTextView = (TextView) view.findViewById(R.id.list_item_short_by_value_textview);

        if (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SAFE)) == 1) {
            shortByTextView.setText("Safe by ");
            shortByValueTextView.setText(Integer.toString(Math.abs(shortBy)));
        } else {
            shortByTextView.setText("Short by ");
            shortByValueTextView.setText(Integer.toString(shortBy));
        }

        if (shortBy == 1 || shortBy == -1) {
            TextView lecturesTextView = (TextView) view.findViewById(R.id.list_item_short_by_lectures_textview);
            lecturesTextView.setText(" class");
        } else {
            TextView lecturesTextView = (TextView) view.findViewById(R.id.list_item_short_by_lectures_textview);
            lecturesTextView.setText(" lectures");
        }
    }

    public void buttonClicked(boolean present, Cursor cursor) {
        Course c1 = new Course();

        long columnID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID));
        c1.setCourseCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSECODE)));
        c1.setCourseName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSENAME)));
        c1.setTotalLectures(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TOTALLECTURES)) + 1);

        if (present == true)
            c1.setLecturesAttended(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED)) + 1);
        else
            c1.setLecturesAttended(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED)));


        c1.setExpectedTotalLectures(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXPECTEDTOTALLECTURES)));
        if (c1.getExpectedTotalLectures() == c1.getTotalLectures() - 1)
            c1.setExpectedTotalLectures(c1.getExpectedTotalLectures() + 1);

        c1.setMinimumAttendanceRequired(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MINIMUMATTENDANCEREQUIRED)));

        long l = dbManager.update(c1, columnID);
    }

    public Course getCourseFromDatabase(Cursor cursor) {
        Course course = new Course();

        course.setCourseCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSECODE)));
        course.setCourseName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSENAME)));
        course.setLecturesAttended(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED)));
        course.setTotalLectures(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TOTALLECTURES)));
        course.setMinimumAttendanceRequired(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MINIMUMATTENDANCEREQUIRED)));
        course.setExpectedTotalLectures(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXPECTEDTOTALLECTURES)));

        return course;
    }

    public void addLecture(boolean present, Cursor cursor) {
        Lecture lecture = new Lecture();

        lecture.setCourseID(cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID)));
        lecture.setMinimumAttendanceRequired(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MINIMUMATTENDANCEREQUIRED)));
        lecture.setLectureNumber(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TOTALLECTURES)));
        lecture.setPresent(present);
        lecture.setLecturesAttended(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED)));

        dbManager.insertLecture(lecture);
    }
}
