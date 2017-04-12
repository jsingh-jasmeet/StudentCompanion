package com.example.jasmeet.studentcompanion.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.jasmeet.studentcompanion.adapters.LectureAdapter;

/**
 * Created by Jasmeet on 3/11/2017.
 */

public class LectureTouchHelper extends ItemTouchHelper.SimpleCallback {

    private LectureAdapter mLectureAdapter;

    public LectureTouchHelper(LectureAdapter lectureAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mLectureAdapter = lectureAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mLectureAdapter.remove(viewHolder.getAdapterPosition());
    }
}
