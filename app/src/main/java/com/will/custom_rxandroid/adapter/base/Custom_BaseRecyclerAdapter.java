package com.will.custom_rxandroid.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by will on 16/9/7.
 */

public abstract class Custom_BaseRecyclerAdapter<T> extends
        BaseRecyclerAdapter<Custom_BaseRecyclerViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas = new LinkedList<T>();
    public OnItemClickListener<T> mOnItemClickListener;

    public Custom_BaseRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    // interface of onclick
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T model);

        void onItemLongClick(View view, int position, T model);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public void onBindViewHolder(final Custom_BaseRecyclerViewHolder holder, final int position, boolean isItem) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position, getDatas().get(position));
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position, getDatas().get(position));
                    return true;
                }
            });
        }
        onShowView(holder, position, isItem);
    }

    public abstract void onShowView(Custom_BaseRecyclerViewHolder holder, final int position, boolean isItem);

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        int count = 0;
        if (mDatas != null)
            count = mDatas.size();
        return count;
    }

    public void setData(List<T> list) {
        if (list != null) {
            clear();
            mDatas = list;
            notifyDataSetChanged();
        }
    }

    public void addItemLast(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addItemLast(T data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }


    public void insert(T data, int position) {
        insert(mDatas, data, position);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDatas != null)
            clear(mDatas);
    }

    public List<T> getDatas() {
        return mDatas;
    }
}
