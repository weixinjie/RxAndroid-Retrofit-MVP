package com.will.custom_rxandroid.adapter.elementary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerAdapter;
import com.will.custom_rxandroid.adapter.base.Custom_BaseRecyclerViewHolder;
import com.will.custom_rxandroid.pojo.ZhuangBiImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by will on 16/9/7.
 */

public class ElementaryAdapter extends Custom_BaseRecyclerAdapter<ZhuangBiImage> {

    public ElementaryAdapter(Context context) {
        super(context);
    }

    @Override
    public void onShowView(Custom_BaseRecyclerViewHolder holder, int position, boolean isItem) {
        ElementaryHolder elementaryHolder = (ElementaryHolder) holder;
        ZhuangBiImage zhuangBiImage = mDatas.get(position);
        String image_path = zhuangBiImage.getImage_url();
        String text = zhuangBiImage.getDescription();

        elementaryHolder.tv_base_adapter.setText(text);
        Glide.with(mContext).load(image_path).into(elementaryHolder.iv_base_adapter);
    }

    @Override
    public Custom_BaseRecyclerViewHolder getViewHolder(View view) {
        return new ElementaryHolder(view, false);
    }

    @Override
    public Custom_BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View item_view = mInflater.inflate(R.layout.item_baseadapter, parent, false);
        ElementaryHolder holder = new ElementaryHolder(item_view, true);
        return holder;
    }

    class ElementaryHolder extends Custom_BaseRecyclerViewHolder {
        public ImageView iv_base_adapter;
        public TextView tv_base_adapter;

        public ElementaryHolder(View view, boolean isItme) {
            super(view, isItme);
        }

        @Override
        public void findViews(View view) {
            iv_base_adapter = (ImageView) view.findViewById(R.id.iv_base_adapter);
            tv_base_adapter = (TextView) view.findViewById(R.id.tv_base_adapter);
        }
    }
}
