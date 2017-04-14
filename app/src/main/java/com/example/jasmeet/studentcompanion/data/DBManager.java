package com.example.jasmeet.studentcompanion.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.jasmeet.studentcompanion.models.Course;
import com.example.jasmeet.studentcompanion.models.Lecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmeet on 3/4/2017.
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public long insert(Course course) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COURSECODE, course.getCourseCode());
        contentValues.put(DatabaseHelper.COURSENAME, course.getCourseName());
        contentValues.put(DatabaseHelper.LECTURESATTENDED, course.getLecturesAttended());
        contentValues.put(DatabaseHelper.TOTALLECTURES, course.getTotalLectures());
        contentValues.put(DatabaseHelper.ATTENDANCE, course.getAttendance());
        contentValues.put(DatabaseHelper.MINIMUMATTENDANCEREQUIRED, course.getMinimumAttendanceRequired());
        contentValues.put(DatabaseHelper.EXPECTEDTOTALLECTURES, course.getExpectedTotalLectures());
        if (course.isSafe())
            contentValues.put(DatabaseHelper.SAFE, 1);
        else
            contentValues.put(DatabaseHelper.SAFE, 0);

        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public long insertLecture(Lecture lecture) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.SUB_COURSEID, lecture.getCourseID());
        contentValues.put(DatabaseHelper.SUB_LECTURENUMBER, lecture.getLectureNumber());
        if (lecture.isPresent())
            contentValues.put(DatabaseHelper.SUB_PRESENT, 1);
        else
            contentValues.put(DatabaseHelper.SUB_PRESENT, 0);
        contentValues.put(DatabaseHelper.SUB_LECTURESATTENDED, lecture.getLecturesAttended());
        contentValues.put(DatabaseHelper.SUB_ATTENDANCE, lecture.getAttendance());
        contentValues.put(DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED, lecture.getMinimumAttendanceRequired());
        if (lecture.isSafe())
            contentValues.put(DatabaseHelper.SUB_SAFE, 1);
        else
            contentValues.put(DatabaseHelper.SUB_SAFE, 0);

        return database.insert(DatabaseHelper.SUB_TABLE_NAME, null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.COURSECODE, DatabaseHelper.COURSENAME, DatabaseHelper.LECTURESATTENDED, DatabaseHelper.TOTALLECTURES, DatabaseHelper.ATTENDANCE, DatabaseHelper.MINIMUMATTENDANCEREQUIRED, DatabaseHelper.EXPECTEDTOTALLECTURES, DatabaseHelper.SAFE};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public long update(Course course, long id) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COURSECODE, course.getCourseCode());
        contentValues.put(DatabaseHelper.COURSENAME, course.getCourseName());
        contentValues.put(DatabaseHelper.LECTURESATTENDED, course.getLecturesAttended());
        contentValues.put(DatabaseHelper.TOTALLECTURES, course.getTotalLectures());
        contentValues.put(DatabaseHelper.ATTENDANCE, course.getAttendance());
        contentValues.put(DatabaseHelper.MINIMUMATTENDANCEREQUIRED, course.getMinimumAttendanceRequired());
        contentValues.put(DatabaseHelper.EXPECTEDTOTALLECTURES, course.getExpectedTotalLectures());
        if (course.isSafe())
            contentValues.put(DatabaseHelper.SAFE, 1);
        else
            contentValues.put(DatabaseHelper.SAFE, 0);

        return (long) database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = \"" + id + "\"", null);
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + " = " + id, null);
        database.delete(DatabaseHelper.SUB_TABLE_NAME, DatabaseHelper.SUB_COURSEID + " = " + id, null);
    }

    public List<Lecture> fetchLectures(long courseID) {
        List<Lecture> allLectureRecords = new ArrayList<>();
        //Cursor cursor = db.query(TABLE_MAIN, new String[] { NAME, AGE, PHONE}, null, null, null, null, null);
        String[] columns = new String[]{DatabaseHelper.SUB_ID, DatabaseHelper.SUB_COURSEID, DatabaseHelper.SUB_LECTURENUMBER, DatabaseHelper.SUB_PRESENT, DatabaseHelper.SUB_LECTURESATTENDED, DatabaseHelper.SUB_ATTENDANCE, DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED, DatabaseHelper.SUB_SAFE};
        String whereClause = DatabaseHelper.SUB_COURSEID + " = ?";
        String[] whereArgs = new String[]{Long.toString(courseID)};

        Cursor cursor = database.query(DatabaseHelper.SUB_TABLE_NAME, columns, whereClause, whereArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Lecture lecture = new Lecture();

                    lecture.setCourseID(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.SUB_COURSEID)));
                    lecture.setMinimumAttendanceRequired(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED)));
                    lecture.setLectureNumber(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SUB_LECTURENUMBER)));
                    lecture.setPresent(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SUB_PRESENT)) == 1);
                    lecture.setLecturesAttended(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SUB_LECTURESATTENDED)));

                    allLectureRecords.add(lecture);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return allLectureRecords;
    }

    public void deleteLecture(Lecture lecture) {

        String[] lectureColumns = new String[]{DatabaseHelper.SUB_ID, DatabaseHelper.SUB_COURSEID, DatabaseHelper.SUB_LECTURENUMBER, DatabaseHelper.SUB_PRESENT, DatabaseHelper.SUB_LECTURESATTENDED, DatabaseHelper.SUB_ATTENDANCE, DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED, DatabaseHelper.SUB_SAFE};

        String lectureWhereClause = DatabaseHelper.SUB_COURSEID + " = ? AND " + DatabaseHelper.SUB_LECTURENUMBER + " > ?";
        String[] lectureWhereArgs = new String[]{Long.toString(lecture.getCourseID()), Integer.toString(lecture.getLectureNumber())};

        Cursor lectureCursor = database.query(DatabaseHelper.SUB_TABLE_NAME, lectureColumns, lectureWhereClause, lectureWhereArgs, null, null, null);

        database.delete(DatabaseHelper.SUB_TABLE_NAME, DatabaseHelper.SUB_COURSEID + " = " + Long.toString(lecture.getCourseID()) + " AND " + DatabaseHelper.SUB_LECTURENUMBER + " = " + Integer.toString(lecture.getLectureNumber()), null);

        if (lectureCursor != null) {
            if (lectureCursor.moveToFirst()) {
                do {
                    Lecture lecture2 = new Lecture();

                    lecture2.setCourseID(lectureCursor.getLong(lectureCursor.getColumnIndex(DatabaseHelper.SUB_COURSEID)));
                    lecture2.setMinimumAttendanceRequired(lectureCursor.getInt(lectureCursor.getColumnIndex(DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED)));
                    lecture2.setLectureNumber(lectureCursor.getInt(lectureCursor.getColumnIndex(DatabaseHelper.SUB_LECTURENUMBER)));
                    lecture2.setPresent(lectureCursor.getInt(lectureCursor.getColumnIndex(DatabaseHelper.SUB_PRESENT)) == 1);
                    lecture2.setLecturesAttended(lectureCursor.getInt(lectureCursor.getColumnIndex(DatabaseHelper.SUB_LECTURESATTENDED)));

                    lecture2.setLectureNumber(lecture2.getLectureNumber() - 1);

                    if (lecture.isPresent())
                        lecture2.setLecturesAttended(lecture2.getLecturesAttended() - 1);

                    this.updateLecture(lecture2, lectureCursor.getInt(lectureCursor.getColumnIndex(DatabaseHelper.SUB_LECTURENUMBER)));

                } while (lectureCursor.moveToNext());
            }
            lectureCursor.close();
        }

        String[] courseColumns = new String[]{DatabaseHelper._ID, DatabaseHelper.COURSECODE, DatabaseHelper.COURSENAME, DatabaseHelper.LECTURESATTENDED, DatabaseHelper.TOTALLECTURES, DatabaseHelper.ATTENDANCE, DatabaseHelper.MINIMUMATTENDANCEREQUIRED, DatabaseHelper.EXPECTEDTOTALLECTURES, DatabaseHelper.SAFE};

        String courseWhereClause = DatabaseHelper._ID + " = ?";
        String[] courseWhereArgs = new String[]{Long.toString(lecture.getCourseID())};

        Cursor courseCursor = database.query(DatabaseHelper.TABLE_NAME, courseColumns, courseWhereClause, courseWhereArgs, null, null, null);

        if (courseCursor != null) {
            if (courseCursor.moveToFirst()) {
                do {
                    Course course = new Course();

                    Long ID = lecture.getCourseID();

                    course.setCourseCode(courseCursor.getString(courseCursor.getColumnIndex(DatabaseHelper.COURSECODE)));
                    course.setCourseName(courseCursor.getString(courseCursor.getColumnIndex(DatabaseHelper.COURSENAME)));
                    course.setLecturesAttended(courseCursor.getInt(courseCursor.getColumnIndex(DatabaseHelper.LECTURESATTENDED)));
                    course.setTotalLectures(courseCursor.getInt(courseCursor.getColumnIndex(DatabaseHelper.TOTALLECTURES)) - 1);
                    course.setMinimumAttendanceRequired(courseCursor.getInt(courseCursor.getColumnIndex(DatabaseHelper.MINIMUMATTENDANCEREQUIRED)));
                    course.setExpectedTotalLectures(courseCursor.getInt(courseCursor.getColumnIndex(DatabaseHelper.EXPECTEDTOTALLECTURES)) - 1);

                    if (lecture.isPresent()) {
                        course.setLecturesAttended(course.getLecturesAttended() - 1);
                    }

                    this.update(course, ID);

                } while (courseCursor.moveToNext());
            }
            courseCursor.close();
        }
    }

    public long updateLecture(Lecture lecture, int lectureNumber) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.SUB_COURSEID, lecture.getCourseID());
        contentValues.put(DatabaseHelper.SUB_LECTURENUMBER, lecture.getLectureNumber());
        if (lecture.isPresent())
            contentValues.put(DatabaseHelper.SUB_PRESENT, 1);
        else
            contentValues.put(DatabaseHelper.SUB_PRESENT, 0);
        contentValues.put(DatabaseHelper.SUB_LECTURESATTENDED, lecture.getLecturesAttended());
        contentValues.put(DatabaseHelper.SUB_ATTENDANCE, lecture.getAttendance());
        contentValues.put(DatabaseHelper.SUB_MINIMUMATTENDANCEREQUIRED, lecture.getMinimumAttendanceRequired());
        if (lecture.isSafe())
            contentValues.put(DatabaseHelper.SUB_SAFE, 1);
        else
            contentValues.put(DatabaseHelper.SUB_SAFE, 0);

        return (long) database.update(DatabaseHelper.SUB_TABLE_NAME, contentValues, DatabaseHelper.SUB_COURSEID + " = \"" + lecture.getCourseID() + "\" AND " + DatabaseHelper.SUB_LECTURENUMBER + " = \"" + lectureNumber + "\"", null);
    }
}