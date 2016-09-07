package com.will.custom_rxandroid.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by will on 16/9/7.
 */

public abstract class Custom_BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    public Custom_BaseRecyclerViewHolder(View view, boolean isItme) {
        super(view);
        if (isItme) {
            findViews(view);
        }
    }

    public abstract void findViews(View view);
}
