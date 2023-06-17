package com.google.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.base.OnBaseListener;
import com.google.base.utils.ViewBindingUtils;

import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public abstract class BaseAdapter<T, E extends ViewBinding> extends RecyclerView.Adapter<BaseAdapter<T, E>.ViewHolder> implements DefaultLifecycleObserver {
    protected Context context;
    protected List<T> objectArrayList;
    protected OnBaseListener<T> onBaseListener;
    protected ViewHolder viewHolder;
    
    public BaseAdapter(Context context, LifecycleOwner owner, List<T> objectArrayList) {
        this.context = context;
        this.objectArrayList = objectArrayList;
        if (owner != null) owner.getLifecycle().addObserver(this);
    }

    public void setLifecycleOwner(LifecycleOwner owner) {
        if (owner != null) owner.getLifecycle().addObserver(this);
    }

    public void setObjectArrayList(List<T> objectArrayList) {
        this.objectArrayList = objectArrayList;
        notifyDataSetChanged();
    }

    public List<T> getObjectArrayList() {
        return objectArrayList;
    }

    public void setObjectArrayList(List<T> objectArrayList, Object... objects) {
        this.objectArrayList = objectArrayList;
        notifyDataSetChanged();
    }

    public void setOnBaseListener(OnBaseListener<T> onBaseListener) {
        this.onBaseListener = onBaseListener;
    }

    //移除指定的item 以及 他的数据 带动画
    protected void removeItem(int position) {
        notifyItemRemoved(position);
        if (objectArrayList != null && objectArrayList.size() > position) {
            notifyItemRangeChanged(position, objectArrayList.size() - position);
            objectArrayList.remove(position);
        }
    }

    //指定位置添加item 以及 他的数据 带动画
    protected void addItem(int position, T t) {
        if (objectArrayList != null) {
            objectArrayList.add(t);
            notifyItemInserted(position);
        }
    }

    //尾部添加item 以及 他的数据 带动画
    public void addItem(T t) {
        if (objectArrayList != null) {
            objectArrayList.add(t);
            notifyItemInserted(objectArrayList.size());
        }
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        public E binding;

        public ViewHolder(@NonNull E binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return new ViewHolder(ViewBindingUtil.create(getClass(), LayoutInflater.from(context))); v1.1.1
        return new ViewHolder(ViewBindingUtils.create(getClass(), LayoutInflater.from(context), parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initAdapter(holder, objectArrayList.get(position), position);
    }

    protected abstract void initAdapter(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }
}
