package com.will.custom_rxandroid.adapter.zip;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerAdapter;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerViewHolder;
import com.will.custom_rxandroid.pojo.map.GankBean;

/**
 * Created by will on 16/9/8.
 */

public class ZipAdapter extends Custom_BaseRecyclerAdapter<GankBean> {

    public ZipAdapter(Context context) {
        super(context);
    }

    @Override
    public void onShowView(Custom_BaseRecyclerViewHolder holder, int position, boolean isItem) {
        ViewHolder viewHolder = (ViewHolder) holder;
        GankBean item = getDatas().get(position);

        String text = item.getCreatedAt();
        String image_path = item.getUrl();

        viewHolder.tv_zip.setText(text);
        Glide.with(mContext).load(image_path).into(viewHolder.iv_zip);
    }

    @Override
    public Custom_BaseRecyclerViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public Custom_BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = mInflater.inflate(R.layout.item_zipadapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, true);
        return viewHolder;
    }

    class ViewHolder extends Custom_BaseRecyclerViewHolder {
        ImageView iv_zip;
        TextView tv_zip;

        public ViewHolder(View view, boolean isItme) {
            super(view, isItme);
            Log.e("-------", "执行了构造方法");
        }

        @Override
        public void findViews(View view) {
            iv_zip = (ImageView) view.findViewById(R.id.iv_zip);
            tv_zip = (TextView) view.findViewById(R.id.tv_zip);
        }
    }
}
