package com.google.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps.model.MyLocationStyle;
import com.google.android.databinding.ActivityMainBinding;
import com.google.android.ui.forum.ForumFragment;
import com.google.android.ui.main.MainFragment;
import com.google.android.ui.mine.MineFragment;
import com.google.base.activity.BaseActivity;
import com.google.base.utils.ToastUtils;
import com.permissionx.guolindev.PermissionX;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void initActivity() {

        Log.e("tag11", sha1(this) );
        fragmentManager = getSupportFragmentManager();
        switchFragment(new MainFragment());
        getSupportActionBar().setTitle("主页");
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    if (!allGranted) {
                        ToastUtils.showToast(context,"以下权限被禁止" + deniedList);
                    }
                });

        binding.mainBottom.setOnItemSelectedListener(item -> {
            switch(item.toString()){
                case "主页":
                    getSupportActionBar().setTitle("主页");
                    item.setChecked(true);
                    switchFragment(new MainFragment());
                    break;
                case "动态":
                    getSupportActionBar().setTitle("动态");
                    item.setChecked(true);
                    switchFragment(new ForumFragment());
                    break;
                case "我的":
                    getSupportActionBar().setTitle("我的");
                    item.setChecked(true);
                    switchFragment(new MineFragment());
                    break;
            }
            return false;
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (this.fragment == null){
            fragmentTransaction.add(R.id.main_fLayout,fragment).commit();
            this.fragment = fragment;
        }

        if (this.fragment != fragment){
            // 先判断是否被add过
            if (!fragment.isAdded()){
                // 隐藏当前的fragment，add下一个到Activity中
                fragmentTransaction.hide(this.fragment).add(R.id.main_fLayout,fragment).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                fragmentTransaction.hide(this.fragment).show(fragment).commit();
            }
            this.fragment = fragment;
        }
    }
    public static String sha1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}