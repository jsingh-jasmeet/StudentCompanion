package com.example.jasmeet.studentcompanion.helper;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.data.DBManager;
import com.example.jasmeet.studentcompanion.models.Course;
import com.example.jasmeet.studentcompanion.models.Lecture;

import java.util.List;

/**
 * Created by Jasmeet on 4/12/2017.
 */

public class CourseFormHelper {
    private Activity activity;
    private Course originalCourse;
    private DBManager dbManager;
    private Long ID;

    public CourseFormHelper(Activity act, Course course, long id) {
        this.activity = act;
        originalCourse = course;
        ID = id;

        dbManager = new DBManager(act.getBaseContext());
        dbManager.open();
    }

    public void addCourseToDatabase() {
        Course newCourseValues = new Course();

        EditText courseCodeEditText = (EditText) activity.findViewById(R.id.course_code_edittext);
        EditText courseNameEditText = (EditText) activity.findViewById(R.id.course_name_edittext);
        EditText lecturesAttendedEditText = (EditText) activity.findViewById(R.id.list_item_lectures_attended_edit_text);
        EditText totalLecturesEditText = (EditText) activity.findViewById(R.id.list_item_total_lectures_edit_text);
        Spinner minimumAttendanceRequiredSpinner = (Spinner) activity.findViewById(R.id.minimum_attendance_required_spinner);
        EditText expectedTotalLecturesEditText = (EditText) activity.findViewById(R.id.list_item_expected_total_lectures_edit_text);

        newCourseValues.setCourseCode(courseCodeEditText.getText().toString());
        newCourseValues.setCourseName(courseNameEditText.getText().toString());
        newCourseValues.setMinimumAttendanceRequired(Integer.parseInt(minimumAttendanceRequiredSpinner.getSelectedItem().toString().substring(0, 2)));
        newCourseValues.setTotalLectures(Integer.parseInt(totalLecturesEditText.getText().toString()));
        newCourseValues.setLecturesAttended(Integer.parseInt(lecturesAttendedEditText.getText().toString()));
        newCourseValues.setExpectedTotalLectures(Integer.parseInt(expectedTotalLecturesEditText.getText().toString()));

        if (originalCourse == null) {
            long courseID = dbManager.insert(newCourseValues);
            addLectures(courseID, newCourseValues.getMinimumAttendanceRequired(), newCourseValues.getLecturesAttended(), newCourseValues.getTotalLectures());
        } else {
            editCourse(newCourseValues);
        }
    }

    private void addLectures(long courseID, int minimumAttendanceRequired, int lecturesAttended, int totalLectures) {
        int i = 1;

        while (i <= totalLectures) {
            Lecture lecture = new Lecture();

            lecture.setCourseID(courseID);
            lecture.setMinimumAttendanceRequired(minimumAttendanceRequired);
            lecture.setLectureNumber(i);

            if (i <= lecturesAttended) {
                lecture.setPresent(true);
                lecture.setLecturesAttended(i);
            } else {
                lecture.setPresent(false);
                lecture.setLecturesAttended(lecturesAttended);
            }

            dbManager.insertLecture(lecture);

            i++;
        }
    }

    private void editCourse(Course newCourseValues) {
        List<Lecture> myData;

        while (originalCourse.getTotalLectures() > newCourseValues.getTotalLectures()) {
            myData = dbManager.fetchLectures(ID);

            Lecture lecture = myData.get(originalCourse.getTotalLectures() - 1);
            dbManager.deleteLecture(lecture);
            originalCourse.setTotalLectures(originalCourse.getTotalLectures() - 1);
            if (lecture.isPresent()) {
                originalCourse.setLecturesAttended(originalCourse.getLecturesAttended() - 1);
            }
        }

        while (originalCourse.getTotalLectures() < newCourseValues.getTotalLectures()) {

            Lecture lecture = new Lecture();
            lecture.setCourseID(ID);
            lecture.setMinimumAttendanceRequired(originalCourse.getMinimumAttendanceRequired());
            lecture.setLectureNumber(originalCourse.getTotalLectures() + 1);
            lecture.setPresent(false);
            lecture.setLecturesAttended(originalCourse.getLecturesAttended());

            dbManager.insertLecture(lecture);

            originalCourse.setTotalLectures(originalCourse.getTotalLectures() + 1);
        }

        while (originalCourse.getLecturesAttended() > newCourseValues.getLecturesAttended()) {
            myData = dbManager.fetchLectures(ID);
            int i = originalCourse.getTotalLectures() - 1;
            Lecture lecture = myData.get(i);

            while (!lecture.isPresent()) {
                i--;
                lecture = myData.get(i);
            }

            lecture.setPresent(false);
            lecture.setLecturesAttended(lecture.getLecturesAttended() - 1);
            dbManager.updateLecture(lecture, lecture.getLectureNumber());
            i++;

            while (i < myData.size()) {
                lecture = myData.get(i);
                lecture.setLecturesAttended(lecture.getLecturesAttended() - 1);
                dbManager.updateLecture(lecture, lecture.getLectureNumber());
                i++;
            }

            originalCourse.setLecturesAttended(originalCourse.getLecturesAttended() - 1);
        }

        while (originalCourse.getLecturesAttended() < newCourseValues.getLecturesAttended()) {
            myData = dbManager.fetchLectures(ID);

            int i = originalCourse.getTotalLectures() - 1;

            Lecture lecture = myData.get(i);

            while (lecture.isPresent()) {
                i--;
                lecture = myData.get(i);
            }

            lecture.setPresent(true);
            lecture.setLecturesAttended(lecture.getLecturesAttended() + 1);
            dbManager.updateLecture(lecture, lecture.getLectureNumber());
            i++;

            while (i < myData.size()) {
                lecture = myData.get(i);
                lecture.setLecturesAttended(lecture.getLecturesAttended() + 1);
                dbManager.updateLecture(lecture, lecture.getLectureNumber());
                i++;
            }

            originalCourse.setLecturesAttended(originalCourse.getLecturesAttended() + 1);
        }

        if (originalCourse.getMinimumAttendanceRequired() != newCourseValues.getMinimumAttendanceRequired()) {

            myData = dbManager.fetchLectures(ID);
            int i = 0;

            while (i < myData.size()) {
                Lecture lecture = myData.get(i);
                lecture.setMinimumAttendanceRequired(newCourseValues.getMinimumAttendanceRequired());
                dbManager.updateLecture(lecture, lecture.getLectureNumber());
            }

        }
        dbManager.update(newCourseValues, ID);
    }


}
