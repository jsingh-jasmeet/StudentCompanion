package com.example.jasmeet.studentcompanion.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.jasmeet.studentcompanion.adapters.MyLoader;
import com.example.jasmeet.studentcompanion.data.DBManager;
import com.example.jasmeet.studentcompanion.data.DatabaseHelper;
import com.example.jasmeet.studentcompanion.adapters.MyCourseAdapter;
import com.example.jasmeet.studentcompanion.R;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private DBManager dbManager;
    private static int i = 0;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private static final int LOADER_ID = 1;
    MyLoader myLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] from = new String[]{DatabaseHelper.COURSECODE, DatabaseHelper.COURSENAME, DatabaseHelper.LECTURESATTENDED, DatabaseHelper.TOTALLECTURES, DatabaseHelper.ATTENDANCE};

        final int[] to = new int[]{R.id.list_item_course_code_textview, R.id.list_item_course_name_textview, R.id.list_item_attended_value_textview, R.id.list_item_total_value_textview, R.id.list_item_attendance_textview};

        getLoaderManager().initLoader(LOADER_ID, null, this);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.activity_main_listview);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new MyCourseAdapter(this, R.layout.course_list_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_add_new_course:
                // add new course
                Intent addNewCourse = new Intent(this, CourseFormActivity.class);
                startActivity(addNewCourse);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* This function will be called when Loader is initialized */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        myLoader = new MyLoader(this);
        return myLoader;
    }

    /*
              When the cursor is finally available, it is added by calling
              swapCursor() on the adapter and passing in the cursor object of the callback method
    */

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "onLoadFinished " + cursor.getCount());
        // Notify the adapter to change the values
        adapter.swapCursor(cursor);
        //Set the Selection to the last row in the ListView.
        listView.setSelection(cursor.getCount() - 1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset");
        adapter.swapCursor(null);
    }
}
