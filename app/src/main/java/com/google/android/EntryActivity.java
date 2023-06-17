package com.google.android;


import com.google.base.activity.BaseActivity;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.BaseUtils;
import com.google.android.databinding.ActivityEntryBinding;

public class EntryActivity extends BaseActivity<ActivityEntryBinding> {

    @Override
    protected void initActivity() {
        if (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID,-1) == -1){
            //未登陆 跳转登陆/注册界面
            BaseUtils.startActivity(this,context,AccountActivity.class,1500);
        }else {
            //已登陆 跳转首页
            BaseUtils.startActivity(this,context,MainActivity.class,1500);
        }
    }
}