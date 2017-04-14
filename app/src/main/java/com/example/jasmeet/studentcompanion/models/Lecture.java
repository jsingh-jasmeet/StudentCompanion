package com.example.jasmeet.studentcompanion.models;

/**
 * Created by Jasmeet on 3/9/2017.
 */

public class Lecture {
    private long mCourseID;
    private int mLectureNumber;
    //private String mDate;
    private boolean mPresent;
    private int mLecturesAttended;
    private int mAttendance;
    private int mMinimumAttendanceRequired;
    private boolean mSafe;

    public Lecture() {
        mCourseID = 0;
        mLectureNumber = 0;
        //mDate =
        mPresent = false;
        mLecturesAttended = 0;
        mAttendance = 0;
        mSafe = false;
    }

    public long getCourseID() {
        return mCourseID;
    }

    public void setCourseID(long courseID) {
        mCourseID = courseID;
    }

    public int getLectureNumber() {
        return mLectureNumber;
    }

    public void setLectureNumber(int lectureNumber) {
        mLectureNumber = lectureNumber;
        mAttendance = (int) ((double) mLecturesAttended / mLectureNumber * 100);
        mSafe = mAttendance >= mMinimumAttendanceRequired;
    }

    public boolean isPresent() {
        return mPresent;
    }

    public void setPresent(boolean present) {
        mPresent = present;
    }

    public int getLecturesAttended() {
        return mLecturesAttended;
    }

    public void setLecturesAttended(int lecturesAttended) {
        mLecturesAttended = lecturesAttended;
        mAttendance = (int) ((double) mLecturesAttended / mLectureNumber * 100);
        mSafe = mAttendance >= mMinimumAttendanceRequired;
    }

    public int getAttendance() {
        return mAttendance;
    }

    public int getMinimumAttendanceRequired() {
        return mMinimumAttendanceRequired;
    }

    public void setMinimumAttendanceRequired(int minimumAttendanceRequired) {
        mMinimumAttendanceRequired = minimumAttendanceRequired;
        mSafe = mAttendance >= mMinimumAttendanceRequired;
    }

    public boolean isSafe() {
        return mSafe;
    }
}
