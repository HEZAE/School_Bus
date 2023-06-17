package com.google.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.google.base.utils.ActivityUtils;
import com.google.base.utils.ViewBindingUtils;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected Context context;
    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(this));
        setContentView(binding.getRoot());
        context = this;
        ActivityUtils.getInstance().addActivity(this);
        initActivity();
    }

    protected abstract void initActivity();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().removeActivity(this);
    }
}
