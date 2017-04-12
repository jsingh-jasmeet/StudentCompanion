package com.example.jasmeet.studentcompanion.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jasmeet on 3/3/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //Courses Table Name and Columns
    public static final String TABLE_NAME = "COURSES";
    public static final String _ID = "_id";
    public static final String COURSECODE = "courseCode";
    public static final String COURSENAME = "courseName";
    public static final String LECTURESATTENDED = "lecturesAttended";
    public static final String TOTALLECTURES = "totalLectures";
    public static final String ATTENDANCE = "attendance";
    public static final String MINIMUMATTENDANCEREQUIRED = "minimumAttendanceRequired";
    public static final String EXPECTEDTOTALLECTURES = "expectedTotalLectures";
    public static final String SAFE = "safe";

    //Lectures Table Name and Columns
    public static final String SUB_TABLE_NAME = "LECTURE";
    public static final String SUB_ID = "_id";
    public static final String SUB_COURSEID = "courseID";
    public static final String SUB_LECTURENUMBER = "lectureNumber";
    //public static final String SUB_DATEADDED = "dateAdded";
    public static final String SUB_PRESENT = "present";
    public static final String SUB_LECTURESATTENDED = "subLecturesAttended";
    public static final String SUB_ATTENDANCE = "subAttendance";
    public static final String SUB_MINIMUMATTENDANCEREQUIRED = "minimumAttendanceRequired";
    public static final String SUB_SAFE = "subSafe";

    //Database information
    static final String DB_NAME = "STUDENTCOMPANION_COURSES.DB";

    //Database version
    static final int DB_VERSION = 1;

    //Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSECODE + " TEXT, " + COURSENAME + " TEXT, " + LECTURESATTENDED + " INTEGER, " + TOTALLECTURES + " INTEGER, " + ATTENDANCE + " INTEGER, " + MINIMUMATTENDANCEREQUIRED + " INTEGER, " + EXPECTEDTOTALLECTURES + " INTEGER, " + SAFE + " INTEGER);";

    //Creating sub table query
    //private static final String CREATE_SUB_TABLE = "create table " + SUB_TABLE_NAME + "(" +  SUB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUB_COURSEID + " INTEGER, " + SUB_LECTURENUMBER + " INTEGER, " + SUB_DATEADDED + " TEXT, " + SUB_PRESENT + " INTEGER, " + SUB_LECTURESATTENDED + " INTEGER, " + SUB_ATTENDANCE + " INTEGER, " + SUB_MINIMUMATTENDANCEREQUIRED + " INTEGER, " + SUB_SAFE + " INTEGER);";
    private static final String CREATE_SUB_TABLE = "create table " + SUB_TABLE_NAME + "(" + SUB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUB_COURSEID + " INTEGER, " + SUB_LECTURENUMBER + " INTEGER, " + SUB_PRESENT + " INTEGER, " + SUB_LECTURESATTENDED + " INTEGER, " + SUB_ATTENDANCE + " INTEGER, " + SUB_MINIMUMATTENDANCEREQUIRED + " INTEGER, " + SUB_SAFE + " INTEGER);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_SUB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_TABLE_NAME);
        onCreate(db);
    }

    public void deleteTable(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SUB_TABLE_NAME);
        onCreate(db);
    }
}
