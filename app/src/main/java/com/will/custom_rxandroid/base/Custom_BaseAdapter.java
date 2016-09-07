package com.will.custom_rxandroid.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.pojo.ZhuangBiImage;

import java.util.List;


/**
 * Created by will on 16/9/6.
 */

public class Custom_BaseAdapter extends android.widget.BaseAdapter {
    List<ZhuangBiImage> data;
    Context context;

    public Custom_BaseAdapter(Context context) {
        this.context = context;
    }

    public void clear() {
        if (data != null && data.size() != 0)
            data.clear();
    }

    public void setData(List<ZhuangBiImage> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZhuangBiImage zhuangBiImage = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_baseadapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv_base_adapter = (ImageView) convertView.findViewById(R.id.iv_base_adapter);
            viewHolder.tv_base_adapter = (TextView) convertView.findViewById(R.id.tv_base_adapter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String image_path = zhuangBiImage.getImage_url();
        String text = zhuangBiImage.getDescription();

        viewHolder.tv_base_adapter.setText(text);

        Glide.with(context).load(image_path).into(viewHolder.iv_base_adapter);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_base_adapter;
        TextView tv_base_adapter;
    }
}
