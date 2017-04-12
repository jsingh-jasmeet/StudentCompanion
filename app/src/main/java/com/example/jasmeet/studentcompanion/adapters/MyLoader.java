package com.example.jasmeet.studentcompanion.adapters;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.jasmeet.studentcompanion.data.DBManager;

/**
 * Created by Jasmeet on 3/9/2017.
 */

public class MyLoader extends AsyncTaskLoader<Cursor> {

    public static final String TAG = "MyLoader";

    // Database Helper Class
    DBManager dbManager;

    Cursor cursor;

    // We hold a reference to the Loader’s data here.
    private Cursor mData;

    Context ctx;

    public MyLoader(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    public Cursor loadInBackground() {
        // This method is called on a background thread and should generate a
        // new set of data to be delivered back to the client.

        // Initialize Database Helper Class
        dbManager = new DBManager(ctx);
        dbManager.open();

        // Get all records from the Database
        cursor = dbManager.fetch();

        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Cursor oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
            Intent intent = new Intent("my-event");
            intent.putExtra("message", "message");
            getContext().sendBroadcast(intent);
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

//        // The Loader is being reset, so we should stop monitoring for changes.
//        if (mObserver != null) {
//            try {
//                getContext().unregisterReceiver(mObserver);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mObserver = null;
//        }
    }

    @Override
    public void onCanceled(Cursor data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(Cursor data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

}