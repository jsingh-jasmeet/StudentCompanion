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

    public Lecture(long courseID, int lectureNumber, boolean present, int lecturesAttended, int minimumAttendanceRequired) {
        mCourseID = courseID;
        mLectureNumber = lectureNumber;
        //mDate =
        mPresent = present;
        mLecturesAttended = lecturesAttended;
        mAttendance = (int) ((double) mLecturesAttended / mLectureNumber * 100);
        mMinimumAttendanceRequired = minimumAttendanceRequired;

        if (mAttendance < mMinimumAttendanceRequired)
            mSafe = false;
        else
            mSafe = true;
    }

    public void setCourseID(long courseID) {
        mCourseID = courseID;
    }

    public void setLectureNumber(int lectureNumber) {
        mLectureNumber = lectureNumber;

        mAttendance = (int) ((double) mLecturesAttended / mLectureNumber * 100);

        if (mAttendance < mMinimumAttendanceRequired)
            mSafe = false;
        else
            mSafe = true;
    }

    public void setPresent(boolean present) {
        mPresent = present;
    }

    public void setLecturesAttended(int lecturesAttended) {
        mLecturesAttended = lecturesAttended;

        mAttendance = (int) ((double) mLecturesAttended / mLectureNumber * 100);

        if (mAttendance < mMinimumAttendanceRequired)
            mSafe = false;
        else
            mSafe = true;
    }

    public void setMinimumAttendanceRequired(int minimumAttendanceRequired) {
        mMinimumAttendanceRequired = minimumAttendanceRequired;

        if (mAttendance < mMinimumAttendanceRequired)
            mSafe = false;
        else
            mSafe = true;
    }

    public long getCourseID() {
        return mCourseID;
    }

    public int getLectureNumber() {
        return mLectureNumber;
    }

    public boolean isPresent() {
        return mPresent;
    }

    public int getLecturesAttended() {
        return mLecturesAttended;
    }

    public int getAttendance() {
        return mAttendance;
    }

    public int getMinimumAttendanceRequired() {
        return mMinimumAttendanceRequired;
    }

    public boolean isSafe() {
        return mSafe;
    }
}
