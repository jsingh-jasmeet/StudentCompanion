package com.example.jasmeet.studentcompanion.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jasmeet.studentcompanion.R;

/**
 * Created by Jasmeet on 3/16/2017.
 */

public class RecyclerViewDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public RecyclerViewDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.recycler_view_divider_line);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
