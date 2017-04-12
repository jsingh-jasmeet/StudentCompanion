package com.example.jasmeet.studentcompanion.models;

/**
 * Created by Jasmeet on 3/11/2017.
 */

public class LectureTag {

    private static String[] tags = {"OnValueSelected", "OnNothingSelected"};
    private String tag;

    public LectureTag(int i) {
        tag = tags[i];
    }

    public int getTag() {
        if (tag.compareTo("OnValueSelected") == 0)
            return 0;
        else
            return 1;
    }
}
