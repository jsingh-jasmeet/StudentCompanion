package com.example.jasmeet.studentcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jasmeet.studentcompanion.R;
import com.example.jasmeet.studentcompanion.helper.CourseFormHelper;
import com.example.jasmeet.studentcompanion.models.Course;

public class CourseFormActivity extends AppCompatActivity {

    private static final String TAG = "CourseFormActivity";
    ArrayAdapter<CharSequence> adapter;
    private long ID;
    private Course originalCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        Spinner minimumAttendanceRequiredSpinner = (Spinner) findViewById(R.id.minimum_attendance_required_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.required_attendance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minimumAttendanceRequiredSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("id")) {
                setTitle("Edit Course");
                ID = intent.getLongExtra("id", 0);
                originalCourse = intent.getParcelableExtra("course");
                setValues(originalCourse);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_new_course_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_course:
                // add new course
                if (!validValues())
                    return false;

                if (originalCourse == null)
                    new CourseFormHelper(this, null, 0).addCourseToDatabase();
                else
                    new CourseFormHelper(this, originalCourse, ID).addCourseToDatabase();

                Intent main = new Intent(CourseFormActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean validValues() {
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        String courseCodeEditTextString = ((EditText) findViewById(R.id.course_code_edittext)).getText().toString();
        String courseNameEditTextString = ((EditText) findViewById(R.id.course_name_edittext)).getText().toString();
        String lecturesAttendedEditTextString = ((EditText) findViewById(R.id.list_item_lectures_attended_edit_text)).getText().toString();
        String totalLecturesEditTextString = ((EditText) findViewById(R.id.list_item_total_lectures_edit_text)).getText().toString();
        String expectedTotalLecturesEditTextString = ((EditText) findViewById(R.id.list_item_expected_total_lectures_edit_text)).getText().toString();

        if (courseCodeEditTextString.compareTo("") == 0) {
            toast.setText("Course Code cannot be empty");
            toast.show();
            return false;
        } else if (courseNameEditTextString.compareTo("") == 0) {
            toast.setText("Course Name cannot be empty");
            toast.show();
            return false;
        } else if (lecturesAttendedEditTextString.compareTo("") == 0) {
            toast.setText("Lectures Attended cannot be empty");
            toast.show();
            return false;
        } else if (totalLecturesEditTextString.compareTo("") == 0) {
            toast.setText("Total Lectures cannot be empty");
            toast.show();
            return false;
        } else if (expectedTotalLecturesEditTextString.compareTo("") == 0) {
            toast.setText("Expected Total Lectures cannot be empty");
            toast.show();
            return false;
        }

        int lecturesAttended = Integer.parseInt(lecturesAttendedEditTextString);
        int totalLectures = Integer.parseInt(totalLecturesEditTextString);
        int expectedTotalLectures = Integer.parseInt(expectedTotalLecturesEditTextString);

        if (totalLectures < lecturesAttended) {
            toast.setText("Total Lectures cannot be lower than Lectures Attended");
            toast.show();
            return false;
        } else if (expectedTotalLectures < totalLectures) {
            toast.setText("Expected Total Lectures cannot be lower than Total Lectures");
            toast.show();
            return false;
        }

        return true;
    }

    public void setValues(Course course) {
        EditText courseCodeEditText = (EditText) findViewById(R.id.course_code_edittext);
        EditText courseNameEditText = (EditText) findViewById(R.id.course_name_edittext);
        EditText lecturesAttendedEditText = (EditText) findViewById(R.id.list_item_lectures_attended_edit_text);
        EditText totalLecturesEditText = (EditText) findViewById(R.id.list_item_total_lectures_edit_text);
        EditText expectedTotalLecturesEditText = (EditText) findViewById(R.id.list_item_expected_total_lectures_edit_text);

        courseCodeEditText.setText(course.getCourseCode());
        courseNameEditText.setText(course.getCourseName());
        lecturesAttendedEditText.setText(Integer.toString(course.getLecturesAttended()));
        totalLecturesEditText.setText(Integer.toString(course.getTotalLectures()));
        expectedTotalLecturesEditText.setText(Integer.toString(course.getExpectedTotalLectures()));

        Spinner minimumAttendanceRequiredSpinner = (Spinner) findViewById(R.id.minimum_attendance_required_spinner);
        minimumAttendanceRequiredSpinner.setSelection(adapter.getPosition(Integer.toString(course.getMinimumAttendanceRequired()) + "%"));
    }
}
