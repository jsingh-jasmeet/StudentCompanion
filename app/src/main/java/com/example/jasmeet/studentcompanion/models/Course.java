package com.example.jasmeet.studentcompanion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jasmeet on 2/23/2017.
 */

public class Course implements Parcelable {
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

    public String getCourseCode() {
        return mCourseCode;
    }

    public void setCourseCode(String courseCode) {
        mCourseCode = courseCode;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public int getLecturesAttended() {
        return mLecturesAttended;
    }

    public void setLecturesAttended(int LecturesAttended) {
        mLecturesAttended = LecturesAttended;
        mAttendance = (int) ((double) mLecturesAttended / mTotalLectures * 100);
        mSafe = mAttendance >= mMinimumAttendanceRequired;
    }

    public int getTotalLectures() {
        return mTotalLectures;
    }

    public void setTotalLectures(int TotalLectures) {
        mTotalLectures = TotalLectures;
        mAttendance = (int) ((double) mLecturesAttended / mTotalLectures * 100);
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

    public int getExpectedTotalLectures() {
        return mExpectedTotalLectures;
    }

    public void setExpectedTotalLectures(int expectedTotalLectures) {
        mExpectedTotalLectures = expectedTotalLectures;
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

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
}
