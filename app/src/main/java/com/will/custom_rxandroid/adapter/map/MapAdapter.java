package com.will.custom_rxandroid.adapter.map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerAdapter;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerViewHolder;
import com.will.custom_rxandroid.pojo.map.GankBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by will on 16/9/7.
 */

public class MapAdapter extends Custom_BaseRecyclerAdapter<GankBean> {

    public MapAdapter(Context context) {
        super(context);
    }

    @Override
    public void onShowView(Custom_BaseRecyclerViewHolder holder, int position, boolean isItem) {
        MapViewHolder viewHolder = (MapViewHolder) holder;
        GankBean item = getDatas().get(position);

        String text = item.getCreatedAt();
        String image_path = item.getUrl();

        viewHolder.tv_map.setText(text);
        Glide.with(mContext).load(image_path).into(viewHolder.iv_map);
    }

    @Override
    public Custom_BaseRecyclerViewHolder getViewHolder(View view) {
        return new MapViewHolder(view, false);
    }

    @Override
    public Custom_BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View item_view = mInflater.inflate(R.layout.item_mapadapter, parent, false);
        MapViewHolder mapViewHolder = new MapViewHolder(item_view, true);
        return mapViewHolder;
    }

    class MapViewHolder extends Custom_BaseRecyclerViewHolder {
        TextView tv_map;
        ImageView iv_map;

        public MapViewHolder(View view, boolean isItme) {
            super(view, isItme);
        }

        @Override
        public void findViews(View view) {
            tv_map = (TextView) view.findViewById(R.id.tv_map);
            iv_map = (ImageView) view.findViewById(R.id.iv_map);
        }
    }
}
