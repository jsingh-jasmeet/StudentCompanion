package com.example.jasmeet.studentcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jasmeet on 2/23/2017.
 */

public class Course implements Parcelable {
    private String mCourseCode;
    private String mCourseName;
    private int mLecturesAttended;
    private int mTotalLectures;
    private int mAttendance;
    private int mMinimumAttendanceRequired;
    private int mExpectedTotalLectures;
    private boolean mSafe;

    public Course() {
        mCourseCode = "000";
        mCourseName = "XYZ";
        mLecturesAttended = 0;
        mTotalLectures = 0;
        mAttendance = 0;
        mMinimumAttendanceRequired = 0;
        mExpectedTotalLectures = 0;
        mSafe = false;
    }

    public void setCourseCode(String courseCode) {
        mCourseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public void setLecturesAttended(int LecturesAttended) {
        mLecturesAttended = LecturesAttended;
        mAttendance = (int) ((double) mLecturesAttended / mTotalLectures * 100);

        if (mAttendance < mMinimumAttendanceRequired)
            mSafe = false;
        else
            mSafe = true;
    }

    public void setTotalLectures(int TotalLectures) {
        mTotalLectures = TotalLectures;
        mAttendance = (int) ((double) mLecturesAttended / mTotalLectures * 100);

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

    public void setExpectedTotalLectures(int expectedTotalLectures) {
        mExpectedTotalLectures = expectedTotalLectures;
    }

    public String getCourseCode() {
        return mCourseCode;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public int getLecturesAttended() {
        return mLecturesAttended;
    }

    public int getTotalLectures() {
        return mTotalLectures;
    }

    public int getAttendance() {
        return mAttendance;
    }

    public int getMinimumAttendanceRequired() {
        return mMinimumAttendanceRequired;
    }

    public int getExpectedTotalLectures() {
        return mExpectedTotalLectures;
    }

    public boolean isSafe() {
        return mSafe;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mCourseCode);
        parcel.writeString(mCourseName);
        parcel.writeInt(mLecturesAttended);
        parcel.writeInt(mTotalLectures);
        parcel.writeInt(mAttendance);
        parcel.writeInt(mMinimumAttendanceRequired);
        parcel.writeInt(mExpectedTotalLectures);
        parcel.writeByte((byte) (mSafe ? 1 : 0));
    }

    public Course(Parcel in) {
        mCourseCode = in.readString();
        mCourseName = in.readString();
        mLecturesAttended = in.readInt();
        mTotalLectures = in.readInt();
        mAttendance = in.readInt();
        mMinimumAttendanceRequired = in.readInt();
        mExpectedTotalLectures = in.readInt();
        mSafe = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


}
