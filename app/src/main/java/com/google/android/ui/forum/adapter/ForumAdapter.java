package com.google.android.ui.forum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.google.android.databinding.ItemForumBinding;
import com.google.android.ui.forum.bean.Update;
import com.google.base.adapter.BaseAdapter;

import java.util.List;

public class ForumAdapter extends BaseAdapter<Update, ItemForumBinding> {
    public ForumAdapter(Context context, LifecycleOwner owner, List<Update> objectArrayList) {
        super(context, owner, objectArrayList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initAdapter(BaseAdapter<Update, ItemForumBinding>.ViewHolder holder, Update update, int position) {
        holder.binding.title.setText("来自:" + update.getUpdate_title());
        Glide.with(context).load(update.getUpdate_image()).into(holder.binding.image);
        holder.binding.text.setText(update.getUpdate_content());
        holder.binding.time.setText("发布时间:" + update.getUpdate_time());
    }
}
